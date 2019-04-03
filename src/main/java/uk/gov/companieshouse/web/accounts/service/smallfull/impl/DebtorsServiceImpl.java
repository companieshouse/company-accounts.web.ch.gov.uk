package uk.gov.companieshouse.web.accounts.service.smallfull.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriTemplate;
import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.error.ApiErrorResponseException;
import uk.gov.companieshouse.api.handler.exception.URIValidationException;
import uk.gov.companieshouse.api.model.accounts.smallfull.Debtors.DebtorsApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.SmallFullApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.SmallFullLinks;
import uk.gov.companieshouse.web.accounts.api.ApiClientService;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.smallfull.BalanceSheetHeadings;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.debtors.Debtors;
import uk.gov.companieshouse.web.accounts.service.smallfull.BalanceSheetService;
import uk.gov.companieshouse.web.accounts.service.smallfull.DebtorsService;
import uk.gov.companieshouse.web.accounts.service.smallfull.SmallFullService;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.DebtorsTransformer;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

import java.util.ArrayList;
import java.util.List;
import uk.gov.companieshouse.web.accounts.validation.helper.ServiceExceptionHandler;

@Service
public class DebtorsServiceImpl implements DebtorsService {

    @Autowired
    private ApiClientService apiClientService;

    @Autowired
    private DebtorsTransformer transformer;

    @Autowired
    private BalanceSheetService balanceSheetService;

    @Autowired
    private ServiceExceptionHandler serviceExceptionHandler;

    @Autowired
    private SmallFullService smallFullService;

    private static final UriTemplate DEBTORS_URI =
        new UriTemplate("/transactions/{transactionId}/company-accounts/{companyAccountsId}/small-full/notes/debtors");

    private static final String RESOURCE_NAME = "debtors";

    @Override
    public Debtors getDebtors(String transactionId, String companyAccountsId, String companyNumber) throws ServiceException {

        DebtorsApi debtorsApi = getDebtorsApi(transactionId, companyAccountsId);
        Debtors debtors = transformer.getDebtors(debtorsApi);

        BalanceSheetHeadings balanceSheetHeadings = balanceSheetService.getBalanceSheet(transactionId, companyAccountsId, companyNumber).getBalanceSheetHeadings();
        debtors.setBalanceSheetHeadings(balanceSheetHeadings);

        return debtors;
    }

    @Override
    public List<ValidationError> submitDebtors(String transactionId, String companyAccountsId,
            Debtors debtors, String companyNumber) throws ServiceException {

        ApiClient apiClient = apiClientService.getApiClient();

        String uri = DEBTORS_URI.expand(transactionId, companyAccountsId).toString();

        SmallFullApi smallFullApi = smallFullService.getSmallFullAccounts(apiClient, transactionId, companyAccountsId);

        DebtorsApi debtorsApi = transformer.getDebtorsApi(debtors);

        boolean debtorsResourceExists = hasDebtors(smallFullApi.getLinks());

        try {
            if (!debtorsResourceExists) {
                apiClient.smallFull().debtors().create(uri, debtorsApi).execute();
            } else {
                apiClient.smallFull().debtors().update(uri, debtorsApi).execute();
            }
        } catch (URIValidationException e) {
            serviceExceptionHandler.handleURIValidationException(e, RESOURCE_NAME);
        } catch (ApiErrorResponseException e) {
            return serviceExceptionHandler.handleSubmissionException(e, RESOURCE_NAME);
        }

        return new ArrayList<>();
    }

    @Override
    public void deleteDebtors(String transactionId, String companyAccountsId) throws ServiceException {
        ApiClient apiClient = apiClientService.getApiClient();

        String uri = DEBTORS_URI.expand(transactionId, companyAccountsId).toString();

        try {
            apiClient.smallFull().debtors().delete(uri).execute();
        } catch (URIValidationException e) {
            serviceExceptionHandler.handleURIValidationException(e, RESOURCE_NAME);
        } catch (ApiErrorResponseException e) {
            serviceExceptionHandler.handleDeletionException(e, RESOURCE_NAME);
        }
    }

    private DebtorsApi getDebtorsApi(String transactionId, String companyAccountsId) throws ServiceException {
        ApiClient apiClient = apiClientService.getApiClient();

        String uri = DEBTORS_URI.expand(transactionId, companyAccountsId).toString();

        try {
            return apiClient.smallFull().debtors().get(uri).execute();
        } catch (ApiErrorResponseException e) {
            serviceExceptionHandler.handleRetrievalException(e, RESOURCE_NAME);
        } catch (URIValidationException e) {
            serviceExceptionHandler.handleURIValidationException(e, RESOURCE_NAME);
        }

        return null;
    }

    private boolean hasDebtors(SmallFullLinks smallFullLinks) {
        return smallFullLinks.getDebtorsNote() != null;
    }
}
