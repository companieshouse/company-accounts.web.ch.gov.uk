package uk.gov.companieshouse.web.accounts.transformer.smallfull;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import uk.gov.companieshouse.api.model.accounts.smallfull.creditorsafteroneyear.CreditorsAfterOneYearApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.creditorsafteroneyear.CurrentPeriod;
import uk.gov.companieshouse.api.model.accounts.smallfull.creditorsafteroneyear.PreviousPeriod;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.creditorsafteroneyear.CreditorsAfterOneYear;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.impl.CreditorsAfterOneYearTranformerImpl;

public class CreditorsAfterOneYearTransformerTests {

    private static final Long BANK_LOANS_CURRENT = 2L;
    private static final Long FINANCE_LEASES_CURRENT = 3L;
    private static final Long OTHER_CREDITORS_CURRENT = 4L;
    private static final Long TOTAL_CURRENT = 9L;

    private static final Long BANK_LOANS_PREVIOUS = 20L;
    private static final Long FINANCE_LEASES_PREVIOUS = 30L;
    private static final Long OTHER_CREDITORS_PREVIOUS = 40L;
    private static final Long TOTAL_PREVIOUS = 90L;

    private static final String DETAILS = "DETAILS";

    private CreditorsAfterOneYearTransformer transformer = new CreditorsAfterOneYearTranformerImpl();

    @Test
    @DisplayName("All Current period values added to creditors after one year web model")
    void getCreditorsAfterOneYearForCurrentPeriod() {

        CreditorsAfterOneYearApi creditorsAfterOneYearApi = new CreditorsAfterOneYearApi();

        CurrentPeriod currentPeriod = new CurrentPeriod();

        currentPeriod.setBankLoansAndOverdrafts(BANK_LOANS_CURRENT);
        currentPeriod.setFinanceLeasesAndHirePurchaseContracts(FINANCE_LEASES_CURRENT);
        currentPeriod.setOtherCreditors(OTHER_CREDITORS_CURRENT);
        currentPeriod.setTotal(TOTAL_CURRENT);
        currentPeriod.setDetails(DETAILS);

        creditorsAfterOneYearApi.setCurrentPeriod(currentPeriod);

        CreditorsAfterOneYear creditorsAfterOneYear = transformer.getCreditorsAfterOneYear(creditorsAfterOneYearApi);

        assertEquals(BANK_LOANS_CURRENT, creditorsAfterOneYear.getBankLoansAndOverdrafts().getCurrentBankLoansAndOverdrafts());
        assertEquals(FINANCE_LEASES_CURRENT, creditorsAfterOneYear.getFinanceLeasesAndHirePurchaseContracts().getCurrentFinanceLeasesAndHirePurchaseContracts());
        assertEquals(OTHER_CREDITORS_CURRENT, creditorsAfterOneYear.getOtherCreditors().getCurrentOtherCreditors());
        assertEquals(TOTAL_CURRENT, creditorsAfterOneYear.getTotal().getCurrentTotal());
        assertEquals(DETAILS, creditorsAfterOneYear.getDetails());
    }

    @Test
    @DisplayName("Only populated Current period values added to creditors after one year web model")
    void getCreditorsAfterOneYearForCurrentPeriodPopulatedValues() {

        CreditorsAfterOneYearApi creditorsAfterOneYearApi = new CreditorsAfterOneYearApi();

        CurrentPeriod currentPeriod = new CurrentPeriod();

        currentPeriod.setOtherCreditors(OTHER_CREDITORS_CURRENT);
        currentPeriod.setTotal(TOTAL_CURRENT);
        currentPeriod.setDetails(DETAILS);

        creditorsAfterOneYearApi.setCurrentPeriod(currentPeriod);

        CreditorsAfterOneYear creditorsAfterOneYear = transformer.getCreditorsAfterOneYear(creditorsAfterOneYearApi);

        assertEquals(OTHER_CREDITORS_CURRENT, creditorsAfterOneYear.getOtherCreditors().getCurrentOtherCreditors());
        assertNull(creditorsAfterOneYear.getBankLoansAndOverdrafts().getCurrentBankLoansAndOverdrafts());
        assertNull(creditorsAfterOneYear.getFinanceLeasesAndHirePurchaseContracts().getCurrentFinanceLeasesAndHirePurchaseContracts());
        assertEquals(TOTAL_CURRENT, creditorsAfterOneYear.getTotal().getCurrentTotal());
        assertEquals(DETAILS, creditorsAfterOneYear.getDetails());
    }

    @Test
    @DisplayName("Previous period values added to creditors after one year web model")
    void getCreditorsAfterOneYearForPreviousPeriod() {

        CreditorsAfterOneYearApi creditorsAfterOneYearApi = new CreditorsAfterOneYearApi();

        PreviousPeriod previousPeriod = new PreviousPeriod();


        previousPeriod.setBankLoansAndOverdrafts(BANK_LOANS_PREVIOUS);
        previousPeriod.setFinanceLeasesAndHirePurchaseContracts(FINANCE_LEASES_PREVIOUS);
        previousPeriod.setOtherCreditors(OTHER_CREDITORS_PREVIOUS);
        previousPeriod.setTotal(TOTAL_PREVIOUS);

        creditorsAfterOneYearApi.setPreviousPeriod(previousPeriod);

        CreditorsAfterOneYear creditorsAfterOneYear = transformer.getCreditorsAfterOneYear(creditorsAfterOneYearApi);

        assertEquals(BANK_LOANS_PREVIOUS, creditorsAfterOneYear.getBankLoansAndOverdrafts().getPreviousBankLoansAndOverdrafts());
        assertEquals(FINANCE_LEASES_PREVIOUS, creditorsAfterOneYear.getFinanceLeasesAndHirePurchaseContracts().getPreviousFinanceLeasesAndHirePurchaseContracts());
        assertEquals(OTHER_CREDITORS_PREVIOUS, creditorsAfterOneYear.getOtherCreditors().getPreviousOtherCreditors());
        assertEquals(TOTAL_PREVIOUS, creditorsAfterOneYear.getTotal().getPreviousTotal());
    }
}
