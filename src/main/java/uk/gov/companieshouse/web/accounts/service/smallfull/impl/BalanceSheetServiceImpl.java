package uk.gov.companieshouse.web.accounts.service.smallfull.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.error.ApiErrorResponseException;
import uk.gov.companieshouse.api.model.accounts.smallfull.CurrentPeriod;
import uk.gov.companieshouse.web.accounts.api.ApiClientService;
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
            throws ApiErrorResponseException {

        ApiClient apiClient = apiClientService.getApiClient();

        CurrentPeriod currentPeriod = apiClient.transaction(transactionId)
                                               .companyAccount(companyAccountsId)
                                               .smallFull()
                                               .currentPeriod().get();

        //transform API data to web-readable data
        BalanceSheet balanceSheet = transformer.getBalanceSheet();

        return balanceSheet;
    }
}
