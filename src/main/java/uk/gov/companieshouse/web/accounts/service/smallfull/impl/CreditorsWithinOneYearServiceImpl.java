package uk.gov.companieshouse.web.accounts.service.smallfull.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriTemplate;
import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.error.ApiErrorResponseException;
import uk.gov.companieshouse.api.handler.exception.URIValidationException;
import uk.gov.companieshouse.api.model.ApiResponse;
import uk.gov.companieshouse.api.model.accounts.smallfull.SmallFullApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.creditorswithinoneyear.CreditorsWithinOneYearApi;
import uk.gov.companieshouse.web.accounts.api.ApiClientService;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.smallfull.BalanceSheet;
import uk.gov.companieshouse.web.accounts.model.smallfull.BalanceSheetHeadings;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.creditorswithinoneyear.CreditorsWithinOneYear;
import uk.gov.companieshouse.web.accounts.service.smallfull.BalanceSheetService;
import uk.gov.companieshouse.web.accounts.service.smallfull.CreditorsWithinOneYearService;
import uk.gov.companieshouse.web.accounts.service.smallfull.SmallFullService;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.CreditorsWithinOneYearTransformer;
import uk.gov.companieshouse.web.accounts.util.ValidationContext;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;
import java.util.ArrayList;
import java.util.List;
import uk.gov.companieshouse.web.accounts.validation.helper.ServiceExceptionHandler;

@Service
public class CreditorsWithinOneYearServiceImpl implements CreditorsWithinOneYearService {

    @Autowired
    private ApiClientService apiClientService;

    @Autowired
    private ServiceExceptionHandler serviceExceptionHandler;

    @Autowired
    private ValidationContext validationContext;

    @Autowired
    private CreditorsWithinOneYearTransformer transformer;

    @Autowired
    private BalanceSheetService balanceSheetService;

    @Autowired
    private SmallFullService smallFullService;

    private static final UriTemplate CREDITORS_WITHIN_ONE_YEAR_URI =
            new UriTemplate(
                    "/transactions/{transactionId}/company-accounts/{companyAccountsId}/small-full/notes/creditors-within-one-year");

    private static final String RESOURCE_NAME = "creditors within one year";

    @Override
    public CreditorsWithinOneYear getCreditorsWithinOneYear(String transactionId,
            String companyAccountsId, String companyNumber) throws ServiceException {
        CreditorsWithinOneYearApi creditorsWithinOneYearApi =
                getCreditorsWithinOneYearApi(transactionId, companyAccountsId);
        CreditorsWithinOneYear creditorsWithinOneYear =
                transformer.getCreditorsWithinOneYear(creditorsWithinOneYearApi);

        BalanceSheet balanceSheet =
                balanceSheetService
                        .getBalanceSheet(transactionId, companyAccountsId, companyNumber);
        BalanceSheetHeadings balanceSheetHeadings = balanceSheet.getBalanceSheetHeadings();
        creditorsWithinOneYear.setBalanceSheetHeadings(balanceSheetHeadings);

        return creditorsWithinOneYear;
    }

    @Override
    public List<ValidationError> submitCreditorsWithinOneYear(String transactionId,
            String companyAccountsId, CreditorsWithinOneYear creditorsWithinOneYear,
            String companyNumber)
            throws ServiceException {

        ApiClient apiClient = apiClientService.getApiClient();
        String uri = CREDITORS_WITHIN_ONE_YEAR_URI.expand(transactionId, companyAccountsId)
                .toString();

        CreditorsWithinOneYearApi creditorsWithinOneYearApi =
                transformer.getCreditorsWithinOneYearApi(creditorsWithinOneYear);

        boolean creditorsWithinOneYearResourceExists =
                hasCreditorsWithinOneYear(apiClient, transactionId, companyAccountsId);
        try {
            ApiResponse apiResponse;
            if (!creditorsWithinOneYearResourceExists) {
                apiResponse =
                        apiClient.smallFull().creditorsWithinOneYear()
                                .create(uri, creditorsWithinOneYearApi).execute();
            } else {
                apiResponse =
                        apiClient.smallFull().creditorsWithinOneYear()
                                .update(uri, creditorsWithinOneYearApi).execute();
            }

            if (apiResponse.hasErrors()) {
                return validationContext.getValidationErrors(apiResponse.getErrors());
            }
        } catch (URIValidationException e) {
            serviceExceptionHandler.handleURIValidationException(e, RESOURCE_NAME);
        } catch (ApiErrorResponseException e) {
            serviceExceptionHandler.handleSubmissionException(e, RESOURCE_NAME);
        }

        return new ArrayList<>();
    }

    @Override
    public void deleteCreditorsWithinOneYear(String transactionId, String companyAccountsId)
            throws ServiceException {
        ApiClient apiClient = apiClientService.getApiClient();

        String uri = CREDITORS_WITHIN_ONE_YEAR_URI.expand(transactionId, companyAccountsId)
                .toString();

        try {
            apiClient.smallFull().creditorsWithinOneYear().delete(uri).execute();
        } catch (URIValidationException e) {
            serviceExceptionHandler.handleURIValidationException(e, RESOURCE_NAME);
        } catch (ApiErrorResponseException e) {
            serviceExceptionHandler.handleDeletionException(e, RESOURCE_NAME);
        }
    }

    private CreditorsWithinOneYearApi getCreditorsWithinOneYearApi(String transactionId,
            String companyAccountsId) throws ServiceException {
        ApiClient apiClient = apiClientService.getApiClient();

        String uri = CREDITORS_WITHIN_ONE_YEAR_URI.expand(transactionId, companyAccountsId)
                .toString();

        try {
            return apiClient.smallFull().creditorsWithinOneYear().get(uri).execute().getData();
        } catch (ApiErrorResponseException e) {
            serviceExceptionHandler.handleRetrievalException(e, RESOURCE_NAME);
        } catch (URIValidationException e) {
            serviceExceptionHandler.handleURIValidationException(e, RESOURCE_NAME);
        }

        return null;
    }

    private boolean hasCreditorsWithinOneYear(ApiClient apiClient, String transactionId,
            String companyAccountsId) throws ServiceException {
        SmallFullApi smallFullApi =
                smallFullService.getSmallFullAccounts(apiClient, transactionId, companyAccountsId);
        return smallFullApi.getLinks().getCreditorsWithinOneYearNote() != null;
    }
}
