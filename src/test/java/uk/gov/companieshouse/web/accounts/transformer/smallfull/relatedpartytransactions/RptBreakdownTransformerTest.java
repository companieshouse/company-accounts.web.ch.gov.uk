package uk.gov.companieshouse.web.accounts.transformer.smallfull.relatedpartytransactions;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.api.model.accounts.smallfull.relatedpartytransactions.RptTransactionApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.relatedpartytransactions.RptTransactionBreakdownApi;
import uk.gov.companieshouse.web.accounts.model.relatedpartytransactions.RptTransactionBreakdown;
import uk.gov.companieshouse.web.accounts.model.relatedpartytransactions.RptTransactionToAdd;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RptBreakdownTransformerTest {

    private RptBreakdownTransformer rptBreakdownTransformer = new RptBreakdownTransformer();

    private RptTransactionApi rptTransactionApi = new RptTransactionApi();

    @Test
    @DisplayName("Map RPT transaction breakdown to api")
    void testMapRptTransactionBreakdownToApi() {

        RptTransactionBreakdown breakdown = new RptTransactionBreakdown();

        breakdown.setBalanceAtPeriodEnd(5L);
        breakdown.setBalanceAtPeriodStart(6L);

        RptTransactionToAdd rptTransactionToAdd = new RptTransactionToAdd();

        rptTransactionToAdd.setBreakdown(breakdown);

        RptTransactionBreakdownApi rptTransactionApi = rptBreakdownTransformer.mapRptTransactionsBreakdownToApi(rptTransactionToAdd);

        assertNotNull(rptTransactionApi);
        assertEquals(rptTransactionApi.getBalanceAtPeriodEnd(), (Long) 5L);
        assertEquals(rptTransactionApi.getBalanceAtPeriodStart(), (Long) 6L);
    }

    @Test
    @DisplayName("Map Rpt transaction breakdown to web")
    void testMapRptTransactionBreakdownToWeb() {

        RptTransactionBreakdownApi rptTransactionBreakdownApi = new RptTransactionBreakdownApi();

        rptTransactionBreakdownApi.setBalanceAtPeriodEnd(1L);
        rptTransactionBreakdownApi.setBalanceAtPeriodStart(2L);

        rptTransactionApi.setBreakdown(rptTransactionBreakdownApi);

        RptTransactionBreakdown breakdown = rptBreakdownTransformer.mapRptTransactionsBreakdownToWeb(rptTransactionApi);

        assertNotNull(breakdown);
        assertEquals(breakdown.getBalanceAtPeriodEnd(), (Long) 1L);
        assertEquals(breakdown.getBalanceAtPeriodStart(), (Long) 2L);
    }
}