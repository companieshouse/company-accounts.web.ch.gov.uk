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
import uk.gov.companieshouse.api.model.accounts.smallfull.PreviousPeriodApi;
import uk.gov.companieshouse.api.model.company.CompanyProfileApi;
import uk.gov.companieshouse.api.model.company.account.LastAccountsApi;
import uk.gov.companieshouse.api.model.company.account.NextAccountsApi;
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

    private static final UriTemplate PREVIOUS_PERIOD_URI =
            new UriTemplate("/transactions/{transactionId}/company-accounts/{companyAccountsId}/small-full/previous-period");

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
        String currentPeriodUri = CURRENT_PERIOD_URI.expand(transactionId, companyAccountsId).toString();

        PreviousPeriodApi previousPeriodApi = transformer.getPreviousPeriod(balanceSheet);
        String previousPeriodUri = PREVIOUS_PERIOD_URI.expand(transactionId, companyAccountsId).toString();
        try {
            apiClient.smallFull().previousPeriod().create(previousPeriodUri, previousPeriodApi).execute();
            apiClient.smallFull().currentPeriod().create(currentPeriodUri, currentPeriod).execute();

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
        boolean isSameYear = isSameYear(companyProfile);
        BalanceSheetHeadings balanceSheetHeadings = new BalanceSheetHeadings();
        balanceSheetHeadings.setPreviousPeriodHeading(getPreviousPeriodHeading(companyProfile, isSameYear));
        balanceSheetHeadings.setCurrentPeriodHeading(getCurrentPeriodHeading(companyProfile, isSameYear));
        return balanceSheetHeadings;
    }

    private String getCurrentPeriodHeading(CompanyProfileApi companyProfile, boolean isSameYear) {
        NextAccountsApi nextAccountsApi = companyProfile.getAccounts().getNextAccounts();
        LocalDate currentPeriodEndOn = convertDateTimeToLocalDate(nextAccountsApi.getPeriodEndOn());
        LocalDate currentPeriodStartOn = convertDateTimeToLocalDate(nextAccountsApi.getPeriodStartOn());

        return accountsDatesHelper.generateBalanceSheetHeading(currentPeriodStartOn, currentPeriodEndOn, isSameYear);
    }

    private String getPreviousPeriodHeading(CompanyProfileApi companyProfile, boolean isSameYear) {
        LastAccountsApi lastAccountsApi = companyProfile.getAccounts().getLastAccounts();
        if (isMultipleYearFiler(companyProfile)) {
            LocalDate previousPeriodStartOn = convertDateTimeToLocalDate(lastAccountsApi.getPeriodStartOn());
            LocalDate previousPeriodEndOn = convertDateTimeToLocalDate(lastAccountsApi.getPeriodEndOn());

            return accountsDatesHelper.generateBalanceSheetHeading(previousPeriodStartOn, previousPeriodEndOn, isSameYear);
        }
        return null;
    }

    private boolean isSameYear(CompanyProfileApi companyProfile) {
        if (isMultipleYearFiler(companyProfile)) {
            LastAccountsApi lastAccountsApi = companyProfile.getAccounts().getLastAccounts();
            LocalDate previousPeriodEndOn = convertDateTimeToLocalDate(lastAccountsApi.getPeriodEndOn());

            NextAccountsApi nextAccountsApi = companyProfile.getAccounts().getNextAccounts();
            LocalDate currentPeriodEndOn = convertDateTimeToLocalDate(nextAccountsApi.getPeriodEndOn());

            return accountsDatesHelper.isSameYear(previousPeriodEndOn, currentPeriodEndOn);
        }
        return false;
    }

    private boolean isMultipleYearFiler(CompanyProfileApi companyProfile) {
        LastAccountsApi lastAccountsApi = companyProfile.getAccounts().getLastAccounts();
        return lastAccountsApi != null && lastAccountsApi.getPeriodEndOn() != null;
    }

    private LocalDate convertDateTimeToLocalDate(DateTime dateTime) {
        return accountsDatesHelper.convertDateToLocalDate(new Date(dateTime.getValue()));
    }
}
