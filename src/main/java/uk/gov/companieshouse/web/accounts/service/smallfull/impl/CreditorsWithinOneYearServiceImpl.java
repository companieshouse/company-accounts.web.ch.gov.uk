package uk.gov.companieshouse.web.accounts.service.smallfull.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriTemplate;

import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.error.ApiErrorResponseException;
import uk.gov.companieshouse.api.handler.exception.URIValidationException;
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

@Service
public class CreditorsWithinOneYearServiceImpl implements CreditorsWithinOneYearService {

    @Autowired
    private ApiClientService apiClientService;

    @Autowired
    private ValidationContext validationContext;

    @Autowired
    private CreditorsWithinOneYearTransformer transformer;
    
    @Autowired
    private BalanceSheetService balanceSheetService;

    @Autowired
    private SmallFullService smallFullService;
    
    private static final UriTemplate CREDITORS_WITHIN_ONE_YEAR_URI =
        new UriTemplate("/transactions/{transactionId}/company-accounts/{companyAccountsId}/small-full/notes/creditors-within-one-year");

    private static final String INVALID_URI_MESSAGE = "Invalid URI for creditors within one year resource";

    @Override
    public CreditorsWithinOneYear getCreditorsWithinOneYear(String transactionId, String companyAccountsId, String companyNumber) throws ServiceException {
        CreditorsWithinOneYearApi creditorsWithinOneYearApi = getCreditorsWithinOneYearApi(transactionId, companyAccountsId);
        CreditorsWithinOneYear creditorsWithinOneYear = transformer.getCreditorsWithinOneYear(creditorsWithinOneYearApi);
        
        BalanceSheet balanceSheet = balanceSheetService.getBalanceSheet(transactionId, companyAccountsId, companyNumber);
        BalanceSheetHeadings balanceSheetHeadings = balanceSheet.getBalanceSheetHeadings();
        creditorsWithinOneYear.setBalanceSheetHeadings(balanceSheetHeadings);

        return creditorsWithinOneYear;
    }

    @Override
    public List<ValidationError> submitCreditorsWithinOneYear(String transactionId, String companyAccountsId,
            CreditorsWithinOneYear creditorsWithinOneYear, String companyNumber) throws ServiceException {

        ApiClient apiClient = apiClientService.getApiClient();
        String uri = CREDITORS_WITHIN_ONE_YEAR_URI.expand(transactionId, companyAccountsId).toString();

        CreditorsWithinOneYearApi creditorsWithinOneYearApi = transformer.setCreditorsWithinOneYear(creditorsWithinOneYear);

        boolean creditorsWithinOneYearResourceExists = hasCreditorsWithinOneYear(transactionId, companyAccountsId);
        try {
            if (!creditorsWithinOneYearResourceExists) {
                apiClient.smallFull().creditorsWithinOneYear().create(uri, creditorsWithinOneYearApi).execute();
            } else {
                apiClient.smallFull().creditorsWithinOneYear().update(uri, creditorsWithinOneYearApi).execute();
            }
            
            
        } catch (URIValidationException e) {
            throw new ServiceException(INVALID_URI_MESSAGE, e);
        } catch (ApiErrorResponseException e) {
            if (e.getStatusCode() == HttpStatus.BAD_REQUEST.value()) {
                List<ValidationError> validationErrors = validationContext.getValidationErrors(e);
                if (validationErrors.isEmpty()) {
                    throw new ServiceException("Bad request when creating creditors within one year resource", e);
                }
                return validationErrors;
            }
            throw new ServiceException("Error creating creditors within one year resource", e);
        }

        return new ArrayList<>();
    }


    private CreditorsWithinOneYearApi getCreditorsWithinOneYearApi(String transactionId, String companyAccountsId) throws ServiceException {
        ApiClient apiClient = apiClientService.getApiClient();

        String uri = CREDITORS_WITHIN_ONE_YEAR_URI.expand(transactionId, companyAccountsId).toString();

        try {
            return apiClient.smallFull().creditorsWithinOneYear().get(uri).execute();

        } catch (ApiErrorResponseException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND.value()) {
                return null;
            }
            throw new ServiceException("Error when retrieving creditors within one year note", e);
        } catch (URIValidationException e) {
            throw new ServiceException(INVALID_URI_MESSAGE, e);
        }
    }

    private boolean hasCreditorsWithinOneYear(String transactionId, String companyAccountsId) throws ServiceException {
        SmallFullApi smallFullApi = smallFullService.getSmallFullAccounts(transactionId, companyAccountsId);
        return smallFullApi.getLinks().getCreditorsWithinOneYearNote() != null;
    }
}
