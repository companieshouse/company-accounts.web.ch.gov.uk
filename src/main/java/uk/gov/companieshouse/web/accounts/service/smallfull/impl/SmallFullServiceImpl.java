package uk.gov.companieshouse.web.accounts.service.smallfull.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriTemplate;
import uk.gov.companieshouse.accountsdates.AccountsDatesHelper;
import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.error.ApiErrorResponseException;
import uk.gov.companieshouse.api.handler.exception.URIValidationException;
import uk.gov.companieshouse.api.model.accounts.smallfull.AccountingPeriodApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.SmallFullApi;
import uk.gov.companieshouse.web.accounts.api.ApiClientService;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.smallfull.BalanceSheetHeadings;
import uk.gov.companieshouse.web.accounts.service.smallfull.SmallFullService;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class SmallFullServiceImpl implements SmallFullService {
    @Autowired
    private ApiClientService apiClientService;

    @Autowired
    private AccountsDatesHelper accountsDatesHelper;

    private static final UriTemplate SMALL_FULL_URI =
            new UriTemplate("/transactions/{transactionId}/company-accounts/{companyAccountsId}/small-full");

    private static final String SMALL_FULL_INVALID_RESOURCE_URI = "Invalid URI for small full resource";

    /**
     * {@inheritDoc}
     */
    @Override
    public void createSmallFullAccounts(String transactionId, String companyAccountsId) throws ServiceException {
        ApiClient apiClient = apiClientService.getApiClient();

        String uri = SMALL_FULL_URI.expand(transactionId, companyAccountsId).toString();

        try {
            apiClient.smallFull().create(uri, new SmallFullApi()).execute();
        } catch (ApiErrorResponseException e) {
            throw new ServiceException("Error creating small full accounts", e);
        } catch (URIValidationException e) {
            throw new ServiceException(SMALL_FULL_INVALID_RESOURCE_URI, e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateSmallFullAccounts(LocalDate periodEndOn, String transactionId, String companyAccountsId) throws ServiceException {
        ApiClient apiClient = apiClientService.getApiClient();

        String uri = SMALL_FULL_URI.expand(transactionId, companyAccountsId).toString();

        SmallFullApi smallFull = new SmallFullApi();

        if (periodEndOn != null) {
            AccountingPeriodApi nextAccounts = new AccountingPeriodApi();
            nextAccounts.setPeriodEndOn(periodEndOn);
            smallFull.setNextAccounts(nextAccounts);
        }

        try {
            apiClient.smallFull().update(uri, smallFull).execute();
        } catch (ApiErrorResponseException e) {
            throw new ServiceException("Error updating small full accounts", e);
        } catch (URIValidationException e) {
            throw new ServiceException(SMALL_FULL_INVALID_RESOURCE_URI, e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SmallFullApi getSmallFullAccounts(ApiClient apiClient, String transactionId,
            String companyAccountsId) throws ServiceException {
        String uri = SMALL_FULL_URI.expand(transactionId, companyAccountsId).toString();

        try {
            return apiClient.smallFull().get(uri).execute().getData();
        } catch (ApiErrorResponseException e) {
            throw new ServiceException("Error retrieving small full accounts", e);
        } catch (URIValidationException e) {
            throw new ServiceException(SMALL_FULL_INVALID_RESOURCE_URI, e);
        }
    }

    @Override
    public boolean isMultiYearFiler(SmallFullApi smallFullApi) {
        return Optional.ofNullable(smallFullApi)
                .map(SmallFullApi::getLastAccounts)
                .map(AccountingPeriodApi::getPeriodEndOn)
                .isPresent();
    }

    @Override
    public BalanceSheetHeadings getBalanceSheetHeadings(SmallFullApi smallFullApi) {
        boolean isSameYear = isSameYearFiler(smallFullApi);
        BalanceSheetHeadings balanceSheetHeadings = new BalanceSheetHeadings();
        balanceSheetHeadings.setPreviousPeriodHeading(getPreviousPeriodHeading(smallFullApi,
                isSameYear));
        balanceSheetHeadings.setCurrentPeriodHeading(getCurrentPeriodHeading(smallFullApi,
                isSameYear));
        return balanceSheetHeadings;
    }

    private boolean isSameYearFiler(SmallFullApi smallFullApi) {
        if (isMultiYearFiler(smallFullApi)) {
            AccountingPeriodApi lastAccountsApi = smallFullApi.getLastAccounts();
            LocalDate previousPeriodEndOn = lastAccountsApi.getPeriodEndOn();

            AccountingPeriodApi nextAccountsApi = smallFullApi.getNextAccounts();
            LocalDate currentPeriodEndOn = nextAccountsApi.getPeriodEndOn();

            return accountsDatesHelper.isSameYear(previousPeriodEndOn, currentPeriodEndOn);
        }

        return false;
    }

    private String getCurrentPeriodHeading(SmallFullApi smallFullApi, boolean isSameYear) {
        AccountingPeriodApi nextAccountsApi = smallFullApi.getNextAccounts();
        LocalDate currentPeriodEndOn = nextAccountsApi.getPeriodEndOn();
        LocalDate currentPeriodStartOn = nextAccountsApi.getPeriodStartOn();

        return accountsDatesHelper.generateBalanceSheetHeading(currentPeriodStartOn,
                currentPeriodEndOn, isSameYear);
    }

    private String getPreviousPeriodHeading(SmallFullApi companyProfile, boolean isSameYear) {
        if (isMultiYearFiler(companyProfile)) {
            AccountingPeriodApi lastAccountsApi = companyProfile.getLastAccounts();
            LocalDate previousPeriodStartOn = lastAccountsApi.getPeriodStartOn();
            LocalDate previousPeriodEndOn = lastAccountsApi.getPeriodEndOn();

            return accountsDatesHelper.generateBalanceSheetHeading(previousPeriodStartOn,
                    previousPeriodEndOn, isSameYear);
        }
        return null;
    }

}
