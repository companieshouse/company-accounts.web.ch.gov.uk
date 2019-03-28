package uk.gov.companieshouse.web.accounts.service.smallfull.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriTemplate;
import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.error.ApiErrorResponseException;
import uk.gov.companieshouse.api.handler.exception.URIValidationException;
import uk.gov.companieshouse.api.model.accounts.smallfull.SmallFullApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.creditorsafteroneyear.CreditorsAfterOneYearApi;
import uk.gov.companieshouse.web.accounts.api.ApiClientService;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.smallfull.BalanceSheet;
import uk.gov.companieshouse.web.accounts.model.smallfull.BalanceSheetHeadings;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.creditorsafteroneyear.CreditorsAfterOneYear;
import uk.gov.companieshouse.web.accounts.service.smallfull.BalanceSheetService;
import uk.gov.companieshouse.web.accounts.service.smallfull.CreditorsAfterOneYearService;
import uk.gov.companieshouse.web.accounts.service.smallfull.SmallFullService;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.CreditorsAfterOneYearTransformer;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

import java.util.ArrayList;
import java.util.List;
import uk.gov.companieshouse.web.accounts.validation.helper.ServiceExceptionHandler;

@Service
public class CreditorsAfterOneYearServiceImpl implements CreditorsAfterOneYearService {

    private static final UriTemplate CREDITORS_AFTER_ONE_YEAR_URI =
            new UriTemplate(
                    "/transactions/{transactionId}/company-accounts/{companyAccountsId}/small" +
                            "-full/notes/creditors-after-more-than-one-year");

    @Autowired
    private BalanceSheetService balanceSheetService;

    @Autowired
    private ApiClientService apiClientService;

    @Autowired
    private CreditorsAfterOneYearTransformer transformer;

    @Autowired
    private SmallFullService smallFullService;

    @Autowired
    private ServiceExceptionHandler serviceExceptionHandler;

    private static final String RESOURCE_NAME = "creditors after one year";

    @Override
    public CreditorsAfterOneYear getCreditorsAfterOneYear(String transactionId,
            String companyAccountsId, String companyNumber) throws ServiceException {
        CreditorsAfterOneYearApi creditorsAfterOneYearApi =
                getCreditorsAfterOneYearApi(transactionId, companyAccountsId);
        CreditorsAfterOneYear creditorsAfterOneYear =
                transformer.getCreditorsAfterOneYear(creditorsAfterOneYearApi);

        BalanceSheet balanceSheet =
                balanceSheetService.getBalanceSheet(transactionId, companyAccountsId,
                        companyNumber);
        BalanceSheetHeadings balanceSheetHeadings = balanceSheet.getBalanceSheetHeadings();
        creditorsAfterOneYear.setBalanceSheetHeadings(balanceSheetHeadings);

        return creditorsAfterOneYear;
    }

    @Override
    public List<ValidationError> submitCreditorsAfterOneYear(String transactionId,
            String companyAccountsId, CreditorsAfterOneYear creditorsAfterOneYear
            ) throws ServiceException {

        ApiClient apiClient = apiClientService.getApiClient();
        String uri = CREDITORS_AFTER_ONE_YEAR_URI.expand(transactionId, companyAccountsId).toString();

        CreditorsAfterOneYearApi creditorsAfterOneYearApi =
                transformer.getCreditorsAfterOneYearApi(creditorsAfterOneYear);

        boolean creditorsAfterOneYearResourceExists = hasCreditorsAfterOneYear(apiClient, transactionId, companyAccountsId);
        try {
            if (!creditorsAfterOneYearResourceExists) {
                apiClient.smallFull().creditorsAfterOneYear().create(uri, creditorsAfterOneYearApi)
                        .execute();
            } else {
                apiClient.smallFull().creditorsAfterOneYear().update(uri, creditorsAfterOneYearApi)
                        .execute();
            }

        } catch (URIValidationException e) {
            serviceExceptionHandler.handleURIValidationException(e, RESOURCE_NAME);
        } catch (ApiErrorResponseException e) {
            return serviceExceptionHandler.handleSubmissionException(e, RESOURCE_NAME);
        }

        return new ArrayList<>();
    }

    @Override
    public void deleteCreditorsAfterOneYear(String transactionId, String companyAccountsId) throws ServiceException {
        ApiClient apiClient = apiClientService.getApiClient();

        String uri = CREDITORS_AFTER_ONE_YEAR_URI.expand(transactionId, companyAccountsId).toString();

        try {
            apiClient.smallFull().creditorsAfterOneYear().delete(uri).execute();
        } catch (URIValidationException e) {
            serviceExceptionHandler.handleURIValidationException(e, RESOURCE_NAME);
        } catch (ApiErrorResponseException e) {
            serviceExceptionHandler.handleDeletionException(e, RESOURCE_NAME);
        }
    }

    private CreditorsAfterOneYearApi getCreditorsAfterOneYearApi(String transactionId,
            String companyAccountsId) throws ServiceException {
        ApiClient apiClient = apiClientService.getApiClient();

        String uri =
                CREDITORS_AFTER_ONE_YEAR_URI.expand(transactionId, companyAccountsId).toString();

        try {
            return apiClient.smallFull().creditorsAfterOneYear().get(uri).execute();

        } catch (ApiErrorResponseException e) {
            serviceExceptionHandler.handleRetrievalException(e, RESOURCE_NAME);
        } catch (URIValidationException e) {
            serviceExceptionHandler.handleURIValidationException(e, RESOURCE_NAME);
        }

        return null;
    }

    private boolean hasCreditorsAfterOneYear(ApiClient apiClient, String transactionId,
            String companyAccountsId) throws ServiceException {
        SmallFullApi smallFullApi =
                smallFullService.getSmallFullAccounts(apiClient, transactionId, companyAccountsId);
        return smallFullApi.getLinks().getCreditorsAfterMoreThanOneYearNote() != null;
    }
}
