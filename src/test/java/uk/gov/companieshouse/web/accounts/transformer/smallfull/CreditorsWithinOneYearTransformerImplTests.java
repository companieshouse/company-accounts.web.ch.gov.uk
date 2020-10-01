package uk.gov.companieshouse.web.accounts.transformer.smallfull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.api.model.accounts.smallfull.creditorswithinoneyear.CreditorsWithinOneYearApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.creditorswithinoneyear.CurrentPeriod;
import uk.gov.companieshouse.api.model.accounts.smallfull.creditorswithinoneyear.PreviousPeriod;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.creditorswithinoneyear.AccrualsAndDeferredIncome;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.creditorswithinoneyear.BankLoansAndOverdrafts;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.creditorswithinoneyear.CreditorsWithinOneYear;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.creditorswithinoneyear.FinanceLeasesAndHirePurchaseContracts;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.creditorswithinoneyear.OtherCreditors;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.creditorswithinoneyear.TaxationAndSocialSecurity;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.creditorswithinoneyear.Total;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.creditorswithinoneyear.TradeCreditors;
import uk.gov.companieshouse.web.accounts.transformer.NoteTransformer;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.impl.CreditorsWithinOneYearTransformerImpl;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CreditorsWithinOneYearTransformerImplTests {
    
    private static final Long ACCRUALS_CURRENT = 1L;
    private static final Long BANK_LOANS_CURRENT = 2L;
    private static final Long FINANCE_LEASES_CURRENT = 3L;
    private static final Long OTHER_CREDITORS_CURRENT = 4L;
    private static final Long TAXATION_CURRENT = 5L;
    private static final Long TRADE_CREDITORS_CURRENT = 6L;
    private static final Long TOTAL_CURRENT = 21L;
    
    private static final Long ACCRUALS_PREVIOUS = 10L;
    private static final Long BANK_LOANS_PREVIOUS = 20L;
    private static final Long FINANCE_LEASES_PREVIOUS = 30L;
    private static final Long OTHER_CREDITORS_PREVIOUS = 40L;
    private static final Long TAXATION_PREVIOUS = 50L;
    private static final Long TRADE_CREDITORS_PREVIOUS = 60L;
    private static final Long TOTAL_PREVIOUS = 210L;

    private static final String DETAILS = "DETAILS";
    
    private final NoteTransformer<CreditorsWithinOneYear, CreditorsWithinOneYearApi> transformer = new CreditorsWithinOneYearTransformerImpl();

    @Test
    @DisplayName("All Current period values added to creditors within one year web model")
    void getDebtorsForCurrentPeriod() {

        CreditorsWithinOneYearApi creditorsWithinOneYearApi = new CreditorsWithinOneYearApi();

        CurrentPeriod currentPeriod = new CurrentPeriod();

        currentPeriod.setAccrualsAndDeferredIncome(ACCRUALS_CURRENT);
        currentPeriod.setBankLoansAndOverdrafts(BANK_LOANS_CURRENT);
        currentPeriod.setFinanceLeasesAndHirePurchaseContracts(FINANCE_LEASES_CURRENT);
        currentPeriod.setOtherCreditors(OTHER_CREDITORS_CURRENT);
        currentPeriod.setTaxationAndSocialSecurity(TAXATION_CURRENT);
        currentPeriod.setTradeCreditors(TRADE_CREDITORS_CURRENT);
        currentPeriod.setTotal(TOTAL_CURRENT);
        currentPeriod.setDetails(DETAILS);

        creditorsWithinOneYearApi.setCreditorsWithinOneYearCurrentPeriod(currentPeriod);

        CreditorsWithinOneYear creditorsWithinOneYear = transformer.toWeb(creditorsWithinOneYearApi);

        assertEquals(ACCRUALS_CURRENT, creditorsWithinOneYear.getAccrualsAndDeferredIncome().getCurrentAccrualsAndDeferredIncome());
        assertEquals(BANK_LOANS_CURRENT, creditorsWithinOneYear.getBankLoansAndOverdrafts().getCurrentBankLoansAndOverdrafts());
        assertEquals(FINANCE_LEASES_CURRENT, creditorsWithinOneYear.getFinanceLeasesAndHirePurchaseContracts().getCurrentFinanceLeasesAndHirePurchaseContracts());
        assertEquals(OTHER_CREDITORS_CURRENT, creditorsWithinOneYear.getOtherCreditors().getCurrentOtherCreditors());
        assertEquals(TAXATION_CURRENT, creditorsWithinOneYear.getTaxationAndSocialSecurity().getCurrentTaxationAndSocialSecurity());
        assertEquals(TRADE_CREDITORS_CURRENT, creditorsWithinOneYear.getTradeCreditors().getCurrentTradeCreditors());
        assertEquals(TOTAL_CURRENT, creditorsWithinOneYear.getTotal().getCurrentTotal());
        assertEquals(DETAILS, creditorsWithinOneYear.getDetails());
    }

    @Test
    @DisplayName("Only populated Current period values added to creditors within one year web model")
    void getCreditorsWithinOneYearForCurrentPeriodPopulatedValues() {

        CreditorsWithinOneYearApi creditorsWithinOneYearApi = new CreditorsWithinOneYearApi();

        CurrentPeriod currentPeriod = new CurrentPeriod();

        currentPeriod.setTradeCreditors(TRADE_CREDITORS_CURRENT);
        currentPeriod.setTotal(TOTAL_CURRENT);
        currentPeriod.setDetails(DETAILS);

        creditorsWithinOneYearApi.setCreditorsWithinOneYearCurrentPeriod(currentPeriod);

        CreditorsWithinOneYear creditorsWithinOneYear = transformer.toWeb(creditorsWithinOneYearApi);

        assertEquals(TRADE_CREDITORS_CURRENT, creditorsWithinOneYear.getTradeCreditors().getCurrentTradeCreditors());
        assertEquals(TOTAL_CURRENT, creditorsWithinOneYear.getTotal().getCurrentTotal());
        assertEquals(DETAILS, creditorsWithinOneYear.getDetails());
    }

    @Test
    @DisplayName("Previous period values added to creditors within one year web model")
    void getCreditorsWithinOneYearForPreviousPeriod() {

        CreditorsWithinOneYearApi creditorsWithinOneYearApi = new CreditorsWithinOneYearApi();

        PreviousPeriod previousPeriod = new PreviousPeriod();

        previousPeriod.setAccrualsAndDeferredIncome(ACCRUALS_PREVIOUS);
        previousPeriod.setBankLoansAndOverdrafts(BANK_LOANS_PREVIOUS);
        previousPeriod.setFinanceLeasesAndHirePurchaseContracts(FINANCE_LEASES_PREVIOUS);
        previousPeriod.setOtherCreditors(OTHER_CREDITORS_PREVIOUS);
        previousPeriod.setTaxationAndSocialSecurity(TAXATION_PREVIOUS);
        previousPeriod.setTradeCreditors(TRADE_CREDITORS_PREVIOUS);
        previousPeriod.setTotal(TOTAL_PREVIOUS);

        creditorsWithinOneYearApi.setCreditorsWithinOneYearPreviousPeriod(previousPeriod);

        CreditorsWithinOneYear creditorsWithinOneYear = transformer.toWeb(creditorsWithinOneYearApi);

        assertEquals(ACCRUALS_PREVIOUS, creditorsWithinOneYear.getAccrualsAndDeferredIncome().getPreviousAccrualsAndDeferredIncome());
        assertEquals(BANK_LOANS_PREVIOUS, creditorsWithinOneYear.getBankLoansAndOverdrafts().getPreviousBankLoansAndOverdrafts());
        assertEquals(FINANCE_LEASES_PREVIOUS, creditorsWithinOneYear.getFinanceLeasesAndHirePurchaseContracts().getPreviousFinanceLeasesAndHirePurchaseContracts());
        assertEquals(OTHER_CREDITORS_PREVIOUS, creditorsWithinOneYear.getOtherCreditors().getPreviousOtherCreditors());
        assertEquals(TAXATION_PREVIOUS, creditorsWithinOneYear.getTaxationAndSocialSecurity().getPreviousTaxationAndSocialSecurity());
        assertEquals(TRADE_CREDITORS_PREVIOUS, creditorsWithinOneYear.getTradeCreditors().getPreviousTradeCreditors());
        assertEquals(TOTAL_PREVIOUS, creditorsWithinOneYear.getTotal().getPreviousTotal());
    }

    @Test
    @DisplayName("Current period value added to creditors within one year API model when present")
    void currentPeriodValueAddedToCreditorsWithinOneYearApiModel() {

        CreditorsWithinOneYear creditorsWithinOneYear = new CreditorsWithinOneYear();

        AccrualsAndDeferredIncome accrualsAndDeferredIncome = new AccrualsAndDeferredIncome();
        accrualsAndDeferredIncome.setCurrentAccrualsAndDeferredIncome(ACCRUALS_CURRENT);
        creditorsWithinOneYear.setAccrualsAndDeferredIncome(accrualsAndDeferredIncome);

        BankLoansAndOverdrafts bankLoansAndOverdrafts = new BankLoansAndOverdrafts();
        bankLoansAndOverdrafts.setCurrentBankLoansAndOverdrafts(BANK_LOANS_CURRENT);
        creditorsWithinOneYear.setBankLoansAndOverdrafts(bankLoansAndOverdrafts);
        
        FinanceLeasesAndHirePurchaseContracts financeLeasesAndHirePurchaseContracts = new FinanceLeasesAndHirePurchaseContracts();
        financeLeasesAndHirePurchaseContracts.setCurrentFinanceLeasesAndHirePurchaseContracts(FINANCE_LEASES_CURRENT);
        creditorsWithinOneYear.setFinanceLeasesAndHirePurchaseContracts(financeLeasesAndHirePurchaseContracts);
        
        OtherCreditors otherCreditors = new OtherCreditors();
        otherCreditors.setCurrentOtherCreditors(OTHER_CREDITORS_CURRENT);
        creditorsWithinOneYear.setOtherCreditors(otherCreditors);
        
        TaxationAndSocialSecurity taxationAndSocialSecurity = new TaxationAndSocialSecurity();
        taxationAndSocialSecurity.setCurrentTaxationAndSocialSecurity(TAXATION_CURRENT);
        creditorsWithinOneYear.setTaxationAndSocialSecurity(taxationAndSocialSecurity);
        
        TradeCreditors tradeCreditors = new TradeCreditors();
        tradeCreditors.setCurrentTradeCreditors(TRADE_CREDITORS_CURRENT);
        creditorsWithinOneYear.setTradeCreditors(tradeCreditors);

        Total total = new Total();
        total.setCurrentTotal(TOTAL_CURRENT);
        creditorsWithinOneYear.setTotal(total);

        CreditorsWithinOneYearApi creditorsWithinOneYearApi = transformer.toApi(creditorsWithinOneYear);

        assertNull(creditorsWithinOneYearApi.getCreditorsWithinOneYearCurrentPeriod().getDetails());
        CurrentPeriod currentPeriod = creditorsWithinOneYearApi.getCreditorsWithinOneYearCurrentPeriod();
        
        assertEquals(ACCRUALS_CURRENT, currentPeriod.getAccrualsAndDeferredIncome());
        assertEquals(BANK_LOANS_CURRENT, currentPeriod.getBankLoansAndOverdrafts());
        assertEquals(FINANCE_LEASES_CURRENT, currentPeriod.getFinanceLeasesAndHirePurchaseContracts());
        assertEquals(OTHER_CREDITORS_CURRENT, currentPeriod.getOtherCreditors());
        assertEquals(TAXATION_CURRENT, currentPeriod.getTaxationAndSocialSecurity());
        assertEquals(TRADE_CREDITORS_CURRENT, currentPeriod.getTradeCreditors());
        assertEquals(TOTAL_CURRENT, currentPeriod.getTotal());
    }

    @Test
    @DisplayName("All previous period values added to creditors within one year API model when present")
    void previousPeriodValueAddedToCreditorsWithinOneYearApiModel() {

        CreditorsWithinOneYear creditorsWithinOneYear = new CreditorsWithinOneYear();
        
        AccrualsAndDeferredIncome accrualsAndDeferredIncome = new AccrualsAndDeferredIncome();
        accrualsAndDeferredIncome.setPreviousAccrualsAndDeferredIncome(ACCRUALS_PREVIOUS);
        creditorsWithinOneYear.setAccrualsAndDeferredIncome(accrualsAndDeferredIncome);

        BankLoansAndOverdrafts bankLoansAndOverdrafts = new BankLoansAndOverdrafts();
        bankLoansAndOverdrafts.setPreviousBankLoansAndOverdrafts(BANK_LOANS_PREVIOUS);
        creditorsWithinOneYear.setBankLoansAndOverdrafts(bankLoansAndOverdrafts);
        
        FinanceLeasesAndHirePurchaseContracts financeLeasesAndHirePurchaseContracts = new FinanceLeasesAndHirePurchaseContracts();
        financeLeasesAndHirePurchaseContracts.setPreviousFinanceLeasesAndHirePurchaseContracts(FINANCE_LEASES_PREVIOUS);
        creditorsWithinOneYear.setFinanceLeasesAndHirePurchaseContracts(financeLeasesAndHirePurchaseContracts);
        
        OtherCreditors otherCreditors = new OtherCreditors();
        otherCreditors.setPreviousOtherCreditors(OTHER_CREDITORS_PREVIOUS);
        creditorsWithinOneYear.setOtherCreditors(otherCreditors);
        
        TaxationAndSocialSecurity taxationAndSocialSecurity = new TaxationAndSocialSecurity();
        taxationAndSocialSecurity.setPreviousTaxationAndSocialSecurity(TAXATION_PREVIOUS);
        creditorsWithinOneYear.setTaxationAndSocialSecurity(taxationAndSocialSecurity);
        
        TradeCreditors tradeCreditors = new TradeCreditors();
        tradeCreditors.setPreviousTradeCreditors(TRADE_CREDITORS_PREVIOUS);
        creditorsWithinOneYear.setTradeCreditors(tradeCreditors);

        Total total = new Total();
        total.setPreviousTotal(TOTAL_PREVIOUS);
        creditorsWithinOneYear.setTotal(total);

        CreditorsWithinOneYearApi creditorsWithinOneYearApi = transformer.toApi(creditorsWithinOneYear);

        PreviousPeriod previousPeriod = creditorsWithinOneYearApi.getCreditorsWithinOneYearPreviousPeriod();
        
        assertEquals(ACCRUALS_PREVIOUS, previousPeriod.getAccrualsAndDeferredIncome());
        assertEquals(BANK_LOANS_PREVIOUS, previousPeriod.getBankLoansAndOverdrafts());
        assertEquals(FINANCE_LEASES_PREVIOUS, previousPeriod.getFinanceLeasesAndHirePurchaseContracts());
        assertEquals(OTHER_CREDITORS_PREVIOUS, previousPeriod.getOtherCreditors());
        assertEquals(TAXATION_PREVIOUS, previousPeriod.getTaxationAndSocialSecurity());
        assertEquals(TRADE_CREDITORS_PREVIOUS, previousPeriod.getTradeCreditors());
        assertEquals(TOTAL_PREVIOUS, previousPeriod.getTotal());
    }
    
    @Test
    @DisplayName("Current period details are null in creditors within one year API model if empty string passed in web model")
    void detailsNullWithCreditorsWithinOneYearApiModel() {

        CreditorsWithinOneYear creditorsWithinOneYear = new CreditorsWithinOneYear();
        creditorsWithinOneYear.setDetails("");

        creditorsWithinOneYear.setAccrualsAndDeferredIncome(new AccrualsAndDeferredIncome());
        creditorsWithinOneYear.setBankLoansAndOverdrafts(new BankLoansAndOverdrafts());
        creditorsWithinOneYear.setFinanceLeasesAndHirePurchaseContracts(new FinanceLeasesAndHirePurchaseContracts());
        creditorsWithinOneYear.setOtherCreditors(new OtherCreditors());
        creditorsWithinOneYear.setTaxationAndSocialSecurity(new TaxationAndSocialSecurity());

        Total total = new Total();
        total.setCurrentTotal(TOTAL_CURRENT);
        creditorsWithinOneYear.setTotal(total);

        creditorsWithinOneYear.setTradeCreditors(new TradeCreditors());

        CreditorsWithinOneYearApi creditorsWithinOneYearApi = transformer.toApi(creditorsWithinOneYear);

        assertNull(creditorsWithinOneYearApi.getCreditorsWithinOneYearCurrentPeriod().getDetails());
        CurrentPeriod currentPeriod = creditorsWithinOneYearApi.getCreditorsWithinOneYearCurrentPeriod();

        assertEquals(TOTAL_CURRENT, currentPeriod.getTotal());
    }
    
    @Test
    @DisplayName("No previous period added to creditors within one year API model when total not present")
    void previousPeriodValueNotAddedToCreditorsWithinOneYearApiModel() {

        CreditorsWithinOneYear creditorsWithinOneYear = new CreditorsWithinOneYear();
        creditorsWithinOneYear.setDetails(DETAILS);

        creditorsWithinOneYear.setAccrualsAndDeferredIncome(new AccrualsAndDeferredIncome());
        creditorsWithinOneYear.setBankLoansAndOverdrafts(new BankLoansAndOverdrafts());
        creditorsWithinOneYear.setFinanceLeasesAndHirePurchaseContracts(new FinanceLeasesAndHirePurchaseContracts());
        creditorsWithinOneYear.setOtherCreditors(new OtherCreditors());
        creditorsWithinOneYear.setTaxationAndSocialSecurity(new TaxationAndSocialSecurity());
        creditorsWithinOneYear.setTotal(new Total());
        creditorsWithinOneYear.setTradeCreditors(new TradeCreditors());
        
        CreditorsWithinOneYearApi creditorsWithinOneYearApi = transformer.toApi(creditorsWithinOneYear);

        assertNull(creditorsWithinOneYearApi.getCreditorsWithinOneYearPreviousPeriod());
    }
    
    @Test
    @DisplayName("When creditors within one year API model is null the returned web model is non null")
    void NullCreditorsWithinOneYearWebModelWhenNullAPIModel() {
        CreditorsWithinOneYear creditorsWithinOneYear = transformer.toWeb(null);

        assertNotNull(creditorsWithinOneYear);
    }
}