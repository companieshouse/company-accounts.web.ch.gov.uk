package uk.gov.companieshouse.web.accounts.service.smallfull.impl;

import org.springframework.stereotype.Component;
import uk.gov.companieshouse.web.accounts.model.smallfull.BalanceSheet;
import uk.gov.companieshouse.web.accounts.service.smallfull.BalanceSheetService;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.BalanceSheetTransformer;

@Component
public class BalanceSheetServiceImpl implements BalanceSheetService {

    private static BalanceSheetTransformer transformer = new BalanceSheetTransformer();

    @Override
    public BalanceSheet getBalanceSheet(String companyId, String transactionId, String accountsId) {

        //get data from API


        //transform API data to web-readable data
        BalanceSheet balanceSheet = transformer.getBalanceSheet();

        return balanceSheet;
    }
}
