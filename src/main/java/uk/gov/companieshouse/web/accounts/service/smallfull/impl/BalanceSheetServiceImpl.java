package uk.gov.companieshouse.web.accounts.service.smallfull.impl;

import com.google.api.client.util.DateTime;
import java.time.LocalDate;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import uk.gov.companieshouse.accountsdates.AccountsDatesHelper;
import uk.gov.companieshouse.accountsdates.impl.AccountsDatesHelperImpl;
import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.error.ApiErrorResponseException;
import uk.gov.companieshouse.api.model.accounts.abridged.PreviousPeriodApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.CurrentPeriodApi;
import uk.gov.companieshouse.api.model.company.CompanyProfileApi;
import uk.gov.companieshouse.api.model.company.account.LastAccountsApi;
import uk.gov.companieshouse.api.model.company.account.NextAccountsApi;
import uk.gov.companieshouse.web.accounts.api.ApiClientService;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.smallfull.BalanceSheet;
import uk.gov.companieshouse.web.accounts.model.smallfull.BalanceSheetHeadings;
import uk.gov.companieshouse.web.accounts.service.smallfull.BalanceSheetService;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.BalanceSheetTransformer;

@Service
public class BalanceSheetServiceImpl implements BalanceSheetService {

    @Autowired
    private BalanceSheetTransformer transformer;

    @Autowired
    private ApiClientService apiClientService;

    private AccountsDatesHelper accountsDatesHelper = new AccountsDatesHelperImpl();

    @Override
    public BalanceSheet getBalanceSheet(String transactionId, String companyAccountsId)
            throws ServiceException {

        ApiClient apiClient = apiClientService.getApiClient();


        CurrentPeriodApi currentPeriod;

        try {
            currentPeriod = apiClient.transaction(transactionId)
                .companyAccount(companyAccountsId)
                .smallFull()
                .currentPeriod().get();
        } catch (ApiErrorResponseException e) {

            if (e.getStatusCode() == HttpStatus.NOT_FOUND.value()) {
                return new BalanceSheet();
            }

            throw new ServiceException("Error retrieving balance sheet", e);
        }

        return transformer.getBalanceSheet(currentPeriod);
    }

    @Override
    public void postBalanceSheet(String transactionId, String companyAccountsId, BalanceSheet balanceSheet)
            throws ServiceException {

        ApiClient apiClient = apiClientService.getApiClient();

        CurrentPeriodApi currentPeriod = transformer.getCurrentPeriod(balanceSheet);

        try {
            apiClient.transaction(transactionId)
                .companyAccount(companyAccountsId)
                .smallFull()
                .currentPeriod().create(currentPeriod);
        } catch (ApiErrorResponseException e) {
            throw new ServiceException("Error posting balance sheet", e);
        }
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
