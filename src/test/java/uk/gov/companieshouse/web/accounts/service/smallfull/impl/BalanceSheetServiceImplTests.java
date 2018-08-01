package uk.gov.companieshouse.web.accounts.service.smallfull.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.gov.companieshouse.web.accounts.model.smallfull.BalanceSheet;
import uk.gov.companieshouse.web.accounts.service.smallfull.BalanceSheetService;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class BalanceSheetServiceImplTests {

    BalanceSheetService balanceSheetService;

    @BeforeEach
    private void init() {

        balanceSheetService = new BalanceSheetServiceImpl();
    }

    @Test
    public void getBalanceSheetSuccess() {

        BalanceSheet balanceSheet = balanceSheetService.getBalanceSheet(any(String.class), any(String.class), any(String.class));
        assertNotNull(balanceSheet);
    }
}
