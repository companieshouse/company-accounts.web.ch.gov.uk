package uk.gov.companieshouse.web.accounts.service.smallfull.impl;

import com.google.api.client.util.DateTime;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriTemplate;
import uk.gov.companieshouse.accountsdates.AccountsDatesHelper;
import uk.gov.companieshouse.accountsdates.impl.AccountsDatesHelperImpl;
import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.error.ApiErrorResponseException;
import uk.gov.companieshouse.api.handler.exception.URIValidationException;
import uk.gov.companieshouse.api.model.accounts.smallfull.CurrentPeriodApi;
import uk.gov.companieshouse.api.model.company.CompanyProfileApi;
import uk.gov.companieshouse.web.accounts.api.ApiClientService;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;
import uk.gov.companieshouse.web.accounts.model.smallfull.BalanceSheet;
import uk.gov.companieshouse.web.accounts.model.smallfull.BalanceSheetHeadings;
import uk.gov.companieshouse.web.accounts.service.smallfull.BalanceSheetService;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.BalanceSheetTransformer;
import uk.gov.companieshouse.web.accounts.util.ValidationContext;

@Service
public class BalanceSheetServiceImpl implements BalanceSheetService {

    @Autowired
    private BalanceSheetTransformer transformer;

    @Autowired
    private ApiClientService apiClientService;
    
    @Autowired
    private ValidationContext validationContext;

    private AccountsDatesHelper accountsDatesHelper = new AccountsDatesHelperImpl();

    private static final UriTemplate CURRENT_PERIOD_URI =
            new UriTemplate("/transactions/{transactionId}/company-accounts/{companyAccountsId}/small-full/current-period");

    @Override
    public BalanceSheet getBalanceSheet(String transactionId, String companyAccountsId)
            throws ServiceException {

        ApiClient apiClient = apiClientService.getApiClient();


        CurrentPeriodApi currentPeriod;

        String uri = CURRENT_PERIOD_URI.expand(transactionId, companyAccountsId).toString();

        try {
            currentPeriod = apiClient.smallFull().currentPeriod().get(uri).execute();
        } catch (ApiErrorResponseException e) {

            if (e.getStatusCode() == HttpStatus.NOT_FOUND.value()) {
                return new BalanceSheet();
            }

            throw new ServiceException("Error retrieving balance sheet", e);
        } catch (URIValidationException e) {

            throw new ServiceException("Invalid URI for current period resource", e);
        }

        return transformer.getBalanceSheet(currentPeriod);
    }

    @Override
    public List<ValidationError> postBalanceSheet(String transactionId, String companyAccountsId, BalanceSheet balanceSheet)
            throws ServiceException {

        ApiClient apiClient = apiClientService.getApiClient();

        CurrentPeriodApi currentPeriod = transformer.getCurrentPeriod(balanceSheet);

        String uri = CURRENT_PERIOD_URI.expand(transactionId, companyAccountsId).toString();

        try {
            apiClient.smallFull().currentPeriod().create(uri, currentPeriod).execute();
        } catch (ApiErrorResponseException e) {

            if (e.getStatusCode() == HttpStatus.BAD_REQUEST.value()) {
                List<ValidationError> validationErrors = validationContext.getValidationErrors(e);
                if (validationErrors == null) {
                    throw new ServiceException("Bad request posting balance sheet", e);
                }

                return validationErrors;
            }

            throw new ServiceException("Error posting balance sheet", e);
        } catch (URIValidationException e) {

            throw new ServiceException("Invalid URI for current period resource", e);
        }

        return new ArrayList<>();
    }

    @Override
    public BalanceSheetHeadings getBalanceSheetHeadings(CompanyProfileApi companyProfile) {

        LocalDate currentPeriodEndOn =
                convertDateTimeToLocalDate(companyProfile.getAccounts().getNextAccounts().getPeriodEndOn());
        LocalDate currentPeriodStartOn =
                convertDateTimeToLocalDate(companyProfile.getAccounts().getNextAccounts().getPeriodStartOn());

        boolean isSameYear = false;

        if (companyProfile.getAccounts().getLastAccounts() != null &&
                companyProfile.getAccounts().getLastAccounts().getPeriodEndOn() != null) {

            LocalDate previousPeriodEndOn =
                convertDateTimeToLocalDate(companyProfile.getAccounts().getLastAccounts().getPeriodEndOn());
            isSameYear = accountsDatesHelper.isSameYear(previousPeriodEndOn, currentPeriodEndOn);
        }

        String currentPeriodHeading = accountsDatesHelper.generateBalanceSheetHeading(currentPeriodStartOn, currentPeriodEndOn, isSameYear);

        BalanceSheetHeadings balanceSheetHeadings = new BalanceSheetHeadings();
        balanceSheetHeadings.setCurrentPeriodHeading(currentPeriodHeading);

        return balanceSheetHeadings;
    }

    private LocalDate convertDateTimeToLocalDate(DateTime dateTime) {

        Date date = new Date(dateTime.getValue());
        return accountsDatesHelper.convertDateToLocalDate(date);
    }
}
