package uk.gov.companieshouse.web.accounts.service.smallfull.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriTemplate;
import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.error.ApiErrorResponseException;
import uk.gov.companieshouse.api.handler.exception.URIValidationException;
import uk.gov.companieshouse.api.model.ApiResponse;
import uk.gov.companieshouse.api.model.accounts.smallfull.relatedpartytransactions.RptTransactionApi;
import uk.gov.companieshouse.web.accounts.api.ApiClientService;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.relatedpartytransactions.AddOrRemoveRptTransactions;
import uk.gov.companieshouse.web.accounts.model.relatedpartytransactions.RptTransaction;
import uk.gov.companieshouse.web.accounts.service.smallfull.RptTransactionService;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.relatedpartytransactions.RptTransactionsTransformer;
import uk.gov.companieshouse.web.accounts.util.ValidationContext;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;
import uk.gov.companieshouse.web.accounts.validation.helper.ServiceExceptionHandler;
import uk.gov.companieshouse.web.accounts.validation.smallfull.RptTransactionValidator;

import java.util.Arrays;
import java.util.List;

@Service
public class RptTransactionsServiceImpl implements RptTransactionService {

    private static final UriTemplate RPT_TRANSACTIONS_URI =
            new UriTemplate("/transactions/{transactionId}/company-accounts/{companyAccountsId}/small-full/notes/related-party-transactions/transactions");

    private static final UriTemplate RPT_TRANSACTIONS_URI_WITH_ID =
            new UriTemplate("/transactions/{transactionId}/company-accounts/{companyAccountsId}/small-full/notes/related-party-transactions/transactions/{transactionId}");

    private static final String RESOURCE_NAME = "transactions";

    private static final String NOT_PROVIDED = "Not provided";


    @Autowired
    private ApiClientService apiClientService;

    @Autowired
    private RptTransactionsTransformer rptTransactionsTransformer;

    @Autowired
    private ServiceExceptionHandler serviceExceptionHandler;

    @Autowired
    private ValidationContext validationContext;

    @Autowired
    private RptTransactionValidator rptTransactionValidator;

    @Override
    public RptTransaction[] getAllRptTransactions(String transactionId, String companyAccountsId) throws ServiceException {

        ApiClient apiClient = apiClientService.getApiClient();

        String uri = RPT_TRANSACTIONS_URI.expand(transactionId, companyAccountsId).toString();

        try {
            RptTransactionApi[] rptTransactions =  Arrays.stream(apiClient.smallFull().relatedPartyTransactions().rptTransactions().getAll(uri).execute().getData())
                    .map(transaction -> {
                        if (StringUtils.isBlank(transaction.getNameOfRelatedParty())) {
                            transaction.setNameOfRelatedParty(NOT_PROVIDED);
                        }
                        return transaction;
                    }).toArray(RptTransactionApi[]::new);

            return rptTransactionsTransformer.getAllRptTransactions(rptTransactions);
        } catch (ApiErrorResponseException e) {
            serviceExceptionHandler.handleRetrievalException(e, RESOURCE_NAME);
        } catch (URIValidationException e) {
            serviceExceptionHandler.handleURIValidationException(e, RESOURCE_NAME);
        }

        return new RptTransaction[0];
    }

    @Override
    public List<ValidationError> createRptTransaction(String transactionId, String companyAccountsId, AddOrRemoveRptTransactions addOrRemoveRptTransactions) throws ServiceException {

        List<ValidationError> validationErrors = rptTransactionValidator.validateRptTransactionToAdd(addOrRemoveRptTransactions.getRptTransactionToAdd(), addOrRemoveRptTransactions.getIsMultiYearFiler());

        if (!validationErrors.isEmpty()) {
            return validationErrors;
        }

        ApiClient apiClient = apiClientService.getApiClient();

        String uri = RPT_TRANSACTIONS_URI.expand(transactionId, companyAccountsId).toString();

        RptTransactionApi rptTransactionApi = rptTransactionsTransformer.getRptTransactionsApi(addOrRemoveRptTransactions.getRptTransactionToAdd());

        try {
            ApiResponse<RptTransactionApi> apiResponse = apiClient.smallFull().relatedPartyTransactions().rptTransactions().create(uri, rptTransactionApi).execute();

            if (apiResponse.hasErrors()) {
                validationErrors.addAll(validationContext.getValidationErrors(apiResponse.getErrors()));
            }
        } catch (ApiErrorResponseException e) {
            serviceExceptionHandler.handleSubmissionException(e, RESOURCE_NAME);
        } catch (URIValidationException e) {
            serviceExceptionHandler.handleURIValidationException(e, RESOURCE_NAME);
        }

        return validationErrors;
    }

    @Override
    public void deleteRptTransaction(String transactionId, String companyAccountsId, String rptTransactionId) throws ServiceException {

        ApiClient apiClient = apiClientService.getApiClient();
        String uri = RPT_TRANSACTIONS_URI_WITH_ID.expand(transactionId, companyAccountsId, rptTransactionId).toString();
        try {
            apiClient.smallFull().relatedPartyTransactions().rptTransactions().delete(uri).execute();
        } catch (ApiErrorResponseException e) {
            serviceExceptionHandler.handleDeletionException(e, RESOURCE_NAME);
        } catch (URIValidationException e) {
            serviceExceptionHandler.handleURIValidationException(e, RESOURCE_NAME);
        }
    }

    @Override
    public List<ValidationError> submitAddOrRemoveRptTransactions(String transactionId, String companyAccountsId, AddOrRemoveRptTransactions addOrRemoveRptTransactions) throws ServiceException {

        return rptTransactionValidator.validateAtLeastOneRptTransaction(addOrRemoveRptTransactions);
    }
}
