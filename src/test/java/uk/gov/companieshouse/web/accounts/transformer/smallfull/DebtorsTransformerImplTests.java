package uk.gov.companieshouse.web.accounts.transformer.smallfull;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import uk.gov.companieshouse.api.model.accounts.smallfull.BalanceSheetApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.Debtors.CurrentPeriod;
import uk.gov.companieshouse.api.model.accounts.smallfull.Debtors.DebtorsApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.Debtors.PreviousPeriod;
import uk.gov.companieshouse.web.accounts.model.smallfull.BalanceSheet;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.debtors.Debtors;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.debtors.GreaterThanOneYear;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.debtors.OtherDebtors;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.debtors.Total;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.debtors.TradeDebtors;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.impl.DebtorsTransformerImpl;


public class DebtorsTransformerImplTests {

    public static final long TRADE_DEBTORS_CURRENT = 1L;
    public static final long PREPAYMENTS_AND_ACCRUED_INCOME_CURRENT = 2L;
    public static final long OTHER_DEBTORS_CURRENT = 3L;
    public static final long GREATER_THAN_ONE_YEAR_CURRENT = 4L;
    public static final long TOTAL_CURRENT = 6L;

    public static final long TRADE_DEBTORS_PREVIOUS = 10L;
    public static final long PREPAYMENTS_AND_ACCRUED_INCOME_PREVIOUS = 20L;
    public static final long OTHER_DEBTORS_PREVIOUS= 30L;
    public static final long GREATER_THAN_ONE_YEAR_PREVIOUS = 40L;
    public static final long TOTAL_PREVIOUS = 60L;
    public static final String DETAILS = "DETAILS";
    private DebtorsTransformer transformer = new DebtorsTransformerImpl();

    @Test
    @DisplayName("All Current period values added to debtors sheet web model")
    void getDebtorsForCurrentPeriod() {

        DebtorsApi debtorsApi = new DebtorsApi();

        CurrentPeriod debtorsCurrentPeriod = new CurrentPeriod();

        debtorsCurrentPeriod.setTradeDebtors(TRADE_DEBTORS_CURRENT);
        debtorsCurrentPeriod.setPrepaymentsAndAccruedIncome(PREPAYMENTS_AND_ACCRUED_INCOME_CURRENT);
        debtorsCurrentPeriod.setOtherDebtors(OTHER_DEBTORS_CURRENT);
        debtorsCurrentPeriod.setGreaterThanOneYear(GREATER_THAN_ONE_YEAR_CURRENT);
        debtorsCurrentPeriod.setTotal(TOTAL_CURRENT);
        debtorsCurrentPeriod.setDetails(DETAILS);

        debtorsApi.setDebtorsCurrentPeriod(debtorsCurrentPeriod);

        Debtors debtors = transformer.getDebtors(debtorsApi);

        assertEquals(TRADE_DEBTORS_CURRENT, debtors.getTradeDebtors().getCurrentTradeDebtors().longValue());
        assertEquals(PREPAYMENTS_AND_ACCRUED_INCOME_CURRENT, debtors.getPrepaymentsAndAccruedIncome().getCurrentPrepaymentsAndAccruedIncome().longValue());
        assertEquals(OTHER_DEBTORS_CURRENT, debtors.getOtherDebtors().getCurrentOtherDebtors().longValue());
        assertEquals(GREATER_THAN_ONE_YEAR_CURRENT, debtors.getGreaterThanOneYear().getCurrentGreaterThanOneYear().longValue());
        assertEquals(TOTAL_CURRENT, debtors.getTotal().getCurrentTotal().longValue());
        assertEquals(DETAILS, debtors.getDetails());
    }

    @Test
    @DisplayName("Only populated Current period values added to debtors sheet web model")
    void getDebtorsForCurrentPeriodPopulatedValues() {

        DebtorsApi debtorsApi = new DebtorsApi();

        CurrentPeriod debtorsCurrentPeriod = new CurrentPeriod();

        debtorsCurrentPeriod.setTradeDebtors(TRADE_DEBTORS_CURRENT);
        debtorsCurrentPeriod.setPrepaymentsAndAccruedIncome(PREPAYMENTS_AND_ACCRUED_INCOME_CURRENT);
        debtorsCurrentPeriod.setTotal(TOTAL_CURRENT);
        debtorsCurrentPeriod.setDetails(DETAILS);

        debtorsApi.setDebtorsCurrentPeriod(debtorsCurrentPeriod);

        Debtors debtors = transformer.getDebtors(debtorsApi);

        assertEquals(TRADE_DEBTORS_CURRENT, debtors.getTradeDebtors().getCurrentTradeDebtors().longValue());
        assertEquals(PREPAYMENTS_AND_ACCRUED_INCOME_CURRENT, debtors.getPrepaymentsAndAccruedIncome().getCurrentPrepaymentsAndAccruedIncome().longValue());
        assertNull(debtors.getOtherDebtors().getCurrentOtherDebtors());
        assertNull(debtors.getGreaterThanOneYear().getCurrentGreaterThanOneYear());
        assertEquals(TOTAL_CURRENT, debtors.getTotal().getCurrentTotal().longValue());
        assertEquals(DETAILS, debtors.getDetails());
    }

    @Test
    @DisplayName("Previous period values added to debtors sheet web model")
    void getDebtorsForPreviousPeriod() {

        DebtorsApi debtorsApi = new DebtorsApi();

        PreviousPeriod debtorsPreviousPeriod = new PreviousPeriod();

        debtorsPreviousPeriod.setTradeDebtors(TRADE_DEBTORS_PREVIOUS);
        debtorsPreviousPeriod.setPrepaymentsAndAccruedIncome(PREPAYMENTS_AND_ACCRUED_INCOME_PREVIOUS);
        debtorsPreviousPeriod.setOtherDebtors(OTHER_DEBTORS_PREVIOUS);
        debtorsPreviousPeriod.setGreaterThanOneYear(GREATER_THAN_ONE_YEAR_PREVIOUS);
        debtorsPreviousPeriod.setTotal(TOTAL_PREVIOUS);

        debtorsApi.setDebtorsPreviousPeriod(debtorsPreviousPeriod);

        Debtors debtors = transformer.getDebtors(debtorsApi);

        assertEquals(TRADE_DEBTORS_PREVIOUS, debtors.getTradeDebtors().getPreviousTradeDebtors().longValue());
        assertEquals(PREPAYMENTS_AND_ACCRUED_INCOME_PREVIOUS, debtors.getPrepaymentsAndAccruedIncome().getPreviousPrepaymentsAndAccruedIncome().longValue());
        assertEquals(OTHER_DEBTORS_PREVIOUS, debtors.getOtherDebtors().getPreviousOtherDebtors().longValue());
        assertEquals(GREATER_THAN_ONE_YEAR_PREVIOUS, debtors.getGreaterThanOneYear().getPreviousGreaterThanOneYear().longValue());
        assertEquals(TOTAL_PREVIOUS, debtors.getTotal().getPreviousTotal().longValue());
    }

    @Test
    @DisplayName("Current period value added to debtors API model when present")
    void currentPeriodValueAddedToApiModel() {

        Debtors debtors = new Debtors();

        TradeDebtors tradeDebtors = new TradeDebtors();
        tradeDebtors.setCurrentTradeDebtors(TRADE_DEBTORS_CURRENT);
        debtors.setTradeDebtors(tradeDebtors);

        Total total = new Total();
        total.setCurrentTotal(TOTAL_CURRENT);
        debtors.setTotal(total);

        DebtorsApi debtorsApi = new DebtorsApi();

        transformer.setDebtors(debtors, debtorsApi);

        assertNull(debtorsApi.getDebtorsCurrentPeriod().getDetails());
        assertNull(debtorsApi.getDebtorsCurrentPeriod().getGreaterThanOneYear());
        assertNull(debtorsApi.getDebtorsCurrentPeriod().getPrepaymentsAndAccruedIncome());
        assertNull(debtorsApi.getDebtorsCurrentPeriod().getOtherDebtors());

        assertEquals(TRADE_DEBTORS_CURRENT, debtorsApi.getDebtorsCurrentPeriod().getTradeDebtors().longValue());
        assertEquals(TOTAL_CURRENT, debtors.getTotal().getCurrentTotal().longValue());

    }

    @Test
    @DisplayName("All previous period values added to debtors API model when present")
    void previousPeriodValueAddedToApiModel() {

        Debtors debtors = new Debtors();

        TradeDebtors tradeDebtors = new TradeDebtors();
        tradeDebtors.setPreviousTradeDebtors(TRADE_DEBTORS_PREVIOUS);
        debtors.setTradeDebtors(tradeDebtors);

        OtherDebtors otherDebtors = new OtherDebtors();
        otherDebtors.setPreviousOtherDebtors(OTHER_DEBTORS_PREVIOUS);
        debtors.setOtherDebtors(otherDebtors);

        GreaterThanOneYear greaterThanOneYear = new GreaterThanOneYear();
        greaterThanOneYear.setPreviousGreaterThanOneYear(GREATER_THAN_ONE_YEAR_PREVIOUS);
        debtors.setGreaterThanOneYear(greaterThanOneYear);

        Total total = new Total();
        total.setPreviousTotal(TOTAL_PREVIOUS);
        debtors.setTotal(total);

        DebtorsApi debtorsApi = new DebtorsApi();

        transformer.setDebtors(debtors, debtorsApi);

        assertEquals(TRADE_DEBTORS_PREVIOUS, debtorsApi.getDebtorsPreviousPeriod().getTradeDebtors().longValue());
        assertEquals(GREATER_THAN_ONE_YEAR_PREVIOUS, debtorsApi.getDebtorsPreviousPeriod().getGreaterThanOneYear().longValue());
        assertEquals(OTHER_DEBTORS_PREVIOUS, debtorsApi.getDebtorsPreviousPeriod().getOtherDebtors().longValue());
        assertEquals(TOTAL_PREVIOUS, debtorsApi.getDebtorsPreviousPeriod().getTotal().longValue());

        assertNull(debtorsApi.getDebtorsPreviousPeriod().getPrepaymentsAndAccruedIncome());
    }
}