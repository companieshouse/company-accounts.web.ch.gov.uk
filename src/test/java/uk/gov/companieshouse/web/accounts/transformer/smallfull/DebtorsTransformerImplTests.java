package uk.gov.companieshouse.web.accounts.transformer.smallfull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import uk.gov.companieshouse.api.model.accounts.smallfull.Debtors.CurrentPeriod;
import uk.gov.companieshouse.api.model.accounts.smallfull.Debtors.DebtorsApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.Debtors.PreviousPeriod;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.debtors.PrepaymentsAndAccruedIncome;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.debtors.Debtors;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.debtors.GreaterThanOneYear;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.debtors.OtherDebtors;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.debtors.Total;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.debtors.TradeDebtors;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.impl.DebtorsTransformerImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;


public class DebtorsTransformerImplTests {

    private static final long TRADE_DEBTORS_CURRENT = 1L;
    private static final long PREPAYMENTS_AND_ACCRUED_INCOME_CURRENT = 2L;
    private static final long OTHER_DEBTORS_CURRENT = 3L;
    private static final long GREATER_THAN_ONE_YEAR_CURRENT = 4L;
    private static final long TOTAL_CURRENT = 6L;

    private static final long TRADE_DEBTORS_PREVIOUS = 10L;
    private static final long PREPAYMENTS_AND_ACCRUED_INCOME_PREVIOUS = 20L;
    private static final long OTHER_DEBTORS_PREVIOUS= 30L;
    private static final long GREATER_THAN_ONE_YEAR_PREVIOUS = 40L;
    private static final long TOTAL_PREVIOUS = 60L;
    private static final String DETAILS = "DETAILS";
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
    @DisplayName("Current period value added to debtors API model when all present - Details blanks string")
    void currentPeriodValueAddedToApiModelDetailsBlank() {
        Debtors debtors = new Debtors();
        debtors.setDetails("");
        createFullCurrentDebtors(debtors);
    }

    @Test
    @DisplayName("Current period value added to debtors API model when all present - Details present")
    void currentPeriodValueAddedToApiModelDetailsPresent() {
        Debtors debtors = new Debtors();
        debtors.setDetails(DETAILS);
        createFullCurrentDebtors(debtors);
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

        PrepaymentsAndAccruedIncome prepaymentsAndAccruedIncome = new PrepaymentsAndAccruedIncome();
        prepaymentsAndAccruedIncome.setPreviousPrepaymentsAndAccruedIncome(PREPAYMENTS_AND_ACCRUED_INCOME_PREVIOUS);
        debtors.setPrepaymentsAndAccruedIncome(prepaymentsAndAccruedIncome);

        GreaterThanOneYear greaterThanOneYear = new GreaterThanOneYear();
        greaterThanOneYear.setPreviousGreaterThanOneYear(GREATER_THAN_ONE_YEAR_PREVIOUS);
        debtors.setGreaterThanOneYear(greaterThanOneYear);

        Total total = new Total();
        total.setPreviousTotal(TOTAL_PREVIOUS);
        debtors.setTotal(total);

        DebtorsApi debtorsApi = transformer.getDebtorsApi(debtors);

        assertEquals(TRADE_DEBTORS_PREVIOUS, debtorsApi.getDebtorsPreviousPeriod().getTradeDebtors().longValue());
        assertEquals(GREATER_THAN_ONE_YEAR_PREVIOUS, debtorsApi.getDebtorsPreviousPeriod().getGreaterThanOneYear().longValue());
        assertEquals(PREPAYMENTS_AND_ACCRUED_INCOME_PREVIOUS, debtorsApi.getDebtorsPreviousPeriod().getPrepaymentsAndAccruedIncome().longValue());
        assertEquals(OTHER_DEBTORS_PREVIOUS, debtorsApi.getDebtorsPreviousPeriod().getOtherDebtors().longValue());
        assertEquals(TOTAL_PREVIOUS, debtorsApi.getDebtorsPreviousPeriod().getTotal().longValue());
    }

    private void createFullCurrentDebtors(Debtors debtors) {
        TradeDebtors tradeDebtors = new TradeDebtors();
        tradeDebtors.setCurrentTradeDebtors(TRADE_DEBTORS_CURRENT);
        debtors.setTradeDebtors(tradeDebtors);

        PrepaymentsAndAccruedIncome prepaymentsAndAccruedIncome = new PrepaymentsAndAccruedIncome();
        prepaymentsAndAccruedIncome.setCurrentPrepaymentsAndAccruedIncome(PREPAYMENTS_AND_ACCRUED_INCOME_CURRENT);
        debtors.setPrepaymentsAndAccruedIncome(prepaymentsAndAccruedIncome);

        GreaterThanOneYear greaterThanOneYear = new GreaterThanOneYear();
        greaterThanOneYear.setCurrentGreaterThanOneYear(GREATER_THAN_ONE_YEAR_CURRENT);
        debtors.setGreaterThanOneYear(greaterThanOneYear);

        OtherDebtors otherDebtors = new OtherDebtors();
        otherDebtors.setCurrentOtherDebtors(OTHER_DEBTORS_CURRENT);
        debtors.setOtherDebtors(otherDebtors);

        Total total = new Total();
        total.setCurrentTotal(TOTAL_CURRENT);
        debtors.setTotal(total);

        DebtorsApi debtorsApi = transformer.getDebtorsApi(debtors);

        assertEquals(TRADE_DEBTORS_CURRENT, debtorsApi.getDebtorsCurrentPeriod().getTradeDebtors().longValue());
        assertEquals(PREPAYMENTS_AND_ACCRUED_INCOME_CURRENT, debtorsApi.getDebtorsCurrentPeriod().getPrepaymentsAndAccruedIncome().longValue());
        assertEquals(GREATER_THAN_ONE_YEAR_CURRENT, debtorsApi.getDebtorsCurrentPeriod().getGreaterThanOneYear().longValue());
        assertEquals(OTHER_DEBTORS_CURRENT, debtorsApi.getDebtorsCurrentPeriod().getOtherDebtors().longValue());
        assertEquals(TOTAL_CURRENT, debtors.getTotal().getCurrentTotal().longValue());
    }
}