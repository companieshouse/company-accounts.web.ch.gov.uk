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
import uk.gov.companieshouse.api.model.accounts.smallfull.CurrentPeriodApi;
import uk.gov.companieshouse.api.model.company.CompanyProfileApi;
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

    private static final String NULL_VALUE = "null";

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

        LocalDate currentPeriodEndOn =
                convertDateTimeToLocalDate(companyProfile.getAccounts().getNextAccounts().getPeriodEndOn());
        LocalDate currentPeriodStartOn =
                convertDateTimeToLocalDate(companyProfile.getAccounts().getNextAccounts().getPeriodStartOn());

        boolean isSameYear = false;

        if (companyProfile.getAccounts().getLastAccounts() != null &&
                companyProfile.getAccounts().getLastAccounts().getType() != NULL_VALUE) {

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
