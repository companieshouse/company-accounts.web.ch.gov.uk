package uk.gov.companieshouse.web.accounts.transformer.smallfull.loanstodirectors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.api.model.accounts.smallfull.loanstodirectors.LoanApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.loanstodirectors.LoanBreakdownApi;
import uk.gov.companieshouse.web.accounts.model.loanstodirectors.Breakdown;
import uk.gov.companieshouse.web.accounts.model.loanstodirectors.LoanToAdd;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BreakdownTransformerTest {


    private BreakdownTransformer breakdownTransformer = new BreakdownTransformer();

    private LoanApi loanApi = new LoanApi();

    @Test
    @DisplayName("Map loan breakdown to api")
    void testMapLoanBreakdownToApi() {

        Breakdown breakdown = new Breakdown();

        breakdown.setBalanceAtPeriodEnd(5L);
        breakdown.setBalanceAtPeriodStart(6L);
        breakdown.setAdvancesCreditsRepaid(7L);
        breakdown.setAdvancesCreditsMade(8L);

        LoanToAdd loanToAdd = new LoanToAdd();

        loanToAdd.setBreakdown(breakdown);

        LoanBreakdownApi loanBreakdownApi = breakdownTransformer.mapLoanBreakdownToApi(loanToAdd);

        assertNotNull(loanBreakdownApi);
        assertEquals(loanBreakdownApi.getBalanceAtPeriodEnd(),(Long) 5L);
        assertEquals(loanBreakdownApi.getBalanceAtPeriodStart(),(Long) 6L);
        assertEquals(loanBreakdownApi.getAdvancesCreditsRepaid(),(Long) 7L);
        assertEquals(loanBreakdownApi.getAdvancesCreditsMade(),(Long) 8L);
    }

    @Test
    @DisplayName("Map loan breakdown to web")
    void testMapLoanBreakdownToWeb() {

        LoanBreakdownApi loanBreakdownApi = new LoanBreakdownApi();

        loanBreakdownApi.setBalanceAtPeriodEnd(1L);
        loanBreakdownApi.setBalanceAtPeriodStart(2L);
        loanBreakdownApi.setAdvancesCreditsRepaid(3L);
        loanBreakdownApi.setAdvancesCreditsMade(4L);

        loanApi.setBreakdown(loanBreakdownApi);

        Breakdown breakdown = breakdownTransformer.mapLoanBreakdownToWeb(loanApi);

        assertNotNull(breakdown);
        assertEquals(breakdown.getBalanceAtPeriodEnd(),(Long) 1L);
        assertEquals(breakdown.getBalanceAtPeriodStart(),(Long) 2L);
        assertEquals(breakdown.getAdvancesCreditsRepaid(),(Long) 3L);
        assertEquals(breakdown.getAdvancesCreditsMade(),(Long) 4L);
    }
}