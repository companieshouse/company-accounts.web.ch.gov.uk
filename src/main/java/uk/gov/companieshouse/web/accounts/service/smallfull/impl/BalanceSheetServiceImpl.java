package uk.gov.companieshouse.web.accounts.service.smallfull.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.error.ApiErrorResponseException;
import uk.gov.companieshouse.api.model.accounts.smallfull.CurrentPeriodApi;
import uk.gov.companieshouse.web.accounts.api.ApiClientService;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.smallfull.BalanceSheet;
import uk.gov.companieshouse.web.accounts.service.smallfull.BalanceSheetService;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.BalanceSheetTransformer;

@Service
public class BalanceSheetServiceImpl implements BalanceSheetService {

    @Autowired
    private BalanceSheetTransformer transformer;

    @Autowired
    private ApiClientService apiClientService;

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

            throw new ServiceException(e);
        }

        //transform API data to web-readable data
        BalanceSheet balanceSheet = transformer.getBalanceSheet(currentPeriod);

        return balanceSheet;
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

            throw new ServiceException(e);
        }
    }
}
