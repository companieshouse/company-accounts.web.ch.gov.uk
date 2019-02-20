package uk.gov.companieshouse.web.accounts.transformer.smallfull.impl;

import java.util.Objects;
import java.util.stream.Stream;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
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
import uk.gov.companieshouse.web.accounts.transformer.smallfull.CreditorsWithinOneYearTransformer;

@Component
public class CreditorsWithinOneYearTransformerImpl implements CreditorsWithinOneYearTransformer {

  @Override
  public CreditorsWithinOneYear getCreditorsWithinOneYear(
      CreditorsWithinOneYearApi creditorsWithinOneYearApi) {

    CreditorsWithinOneYear creditorsWithinOneYear = new CreditorsWithinOneYear();

    if (creditorsWithinOneYearApi == null) {
      return creditorsWithinOneYear;
    }
      
    AccrualsAndDeferredIncome accrualsAndDeferredIncome = new AccrualsAndDeferredIncome();
    BankLoansAndOverdrafts bankLoansAndOverdrafts = new BankLoansAndOverdrafts();
    FinanceLeasesAndHirePurchaseContracts financeLeasesAndHirePurchaseContracts =
        new FinanceLeasesAndHirePurchaseContracts();
    OtherCreditors otherCreditors = new OtherCreditors();
    TaxationAndSocialSecurity taxationAndSocialSecurity = new TaxationAndSocialSecurity();
    Total total = new Total();
    TradeCreditors tradeCreditors = new TradeCreditors();

    populateCurrentPeriodForWeb(creditorsWithinOneYearApi, creditorsWithinOneYear,
        accrualsAndDeferredIncome, bankLoansAndOverdrafts, financeLeasesAndHirePurchaseContracts,
        otherCreditors, taxationAndSocialSecurity, total, tradeCreditors);

    populatePreviousPeriodForWeb(creditorsWithinOneYearApi, accrualsAndDeferredIncome,
        bankLoansAndOverdrafts, financeLeasesAndHirePurchaseContracts, otherCreditors,
        taxationAndSocialSecurity, total, tradeCreditors);
    
    creditorsWithinOneYear.setAccrualsAndDeferredIncome(accrualsAndDeferredIncome);
    creditorsWithinOneYear.setBankLoansAndOverdrafts(bankLoansAndOverdrafts);
    creditorsWithinOneYear.setFinanceLeasesAndHirePurchaseContracts(financeLeasesAndHirePurchaseContracts);
    creditorsWithinOneYear.setOtherCreditors(otherCreditors);
    creditorsWithinOneYear.setTaxationAndSocialSecurity(taxationAndSocialSecurity);
    creditorsWithinOneYear.setTotal(total);
    creditorsWithinOneYear.setTradeCreditors(tradeCreditors);

    return creditorsWithinOneYear;
  }


  private void populateCurrentPeriodForWeb(CreditorsWithinOneYearApi creditorsWithinOneYearApi,
      CreditorsWithinOneYear creditorsWithinOneYear,
      AccrualsAndDeferredIncome accrualsAndDeferredIncome,
      BankLoansAndOverdrafts bankLoansAndOverdrafts,
      FinanceLeasesAndHirePurchaseContracts financeLeasesAndHirePurchaseContracts,
      OtherCreditors otherCreditors, TaxationAndSocialSecurity taxationAndSocialSecurity,
      Total total, TradeCreditors tradeCreditors) {

    CurrentPeriod currentPeriod =
        creditorsWithinOneYearApi.getCreditorsWithinOneYearCurrentPeriod();

    if (currentPeriod != null) {

      creditorsWithinOneYear.setDetails(currentPeriod.getDetails());

      accrualsAndDeferredIncome.setCurrentAccrualsAndDeferredIncome(currentPeriod
          .getAccrualsAndDeferredIncome());
      bankLoansAndOverdrafts.setCurrentBankLoansAndOverdrafts(currentPeriod
          .getBankLoansAndOverdrafts());
      financeLeasesAndHirePurchaseContracts
          .setCurrentFinanceLeasesAndHirePurchaseContracts(currentPeriod
              .getFinanceLeasesAndHirePurchaseContracts());
      otherCreditors.setCurrentOtherCreditors(currentPeriod.getOtherCreditors());
      taxationAndSocialSecurity.setCurrentTaxationAndSocialSecurity(currentPeriod
          .getTaxationAndSocialSecurity());
      total.setCurrentTotal(currentPeriod.getTotal());
      tradeCreditors.setCurrentTradeCreditors(currentPeriod.getTradeCreditors());
    }
  }

  private void populatePreviousPeriodForWeb(CreditorsWithinOneYearApi creditorsWithinOneYearApi,
      AccrualsAndDeferredIncome accrualsAndDeferredIncome,
      BankLoansAndOverdrafts bankLoansAndOverdrafts,
      FinanceLeasesAndHirePurchaseContracts financeLeasesAndHirePurchaseContracts,
      OtherCreditors otherCreditors, TaxationAndSocialSecurity taxationAndSocialSecurity,
      Total total, TradeCreditors tradeCreditors) {

    PreviousPeriod previousPeriod =
        creditorsWithinOneYearApi.getCreditorsWithinOneYearPreviousPeriod();

    if (previousPeriod != null) {

      accrualsAndDeferredIncome.setPreviousAccrualsAndDeferredIncome(previousPeriod
          .getAccrualsAndDeferredIncome());
      bankLoansAndOverdrafts.setPreviousBankLoansAndOverdrafts(previousPeriod
          .getBankLoansAndOverdrafts());
      financeLeasesAndHirePurchaseContracts
          .setPreviousFinanceLeasesAndHirePurchaseContracts(previousPeriod
              .getFinanceLeasesAndHirePurchaseContracts());
      otherCreditors.setPreviousOtherCreditors(previousPeriod.getOtherCreditors());
      taxationAndSocialSecurity.setPreviousTaxationAndSocialSecurity(previousPeriod
          .getTaxationAndSocialSecurity());
      total.setPreviousTotal(previousPeriod.getTotal());
      tradeCreditors.setPreviousTradeCreditors(previousPeriod.getTradeCreditors());
    }
  }

  @Override
  public CreditorsWithinOneYearApi getCreditorsWithinOneYearApi(
      CreditorsWithinOneYear creditorsWithinOneYear) {

    CreditorsWithinOneYearApi creditorsWithinOneYearApi = new CreditorsWithinOneYearApi();

    setCurrentPeriodOnApiModel(creditorsWithinOneYear, creditorsWithinOneYearApi);

    setPreviousPeriodOnApiModel(creditorsWithinOneYear, creditorsWithinOneYearApi);

    return creditorsWithinOneYearApi;
  }

  private void setCurrentPeriodOnApiModel(CreditorsWithinOneYear creditorsWithinOneYear,
      CreditorsWithinOneYearApi creditorsWithinOneYearApi) {
    CurrentPeriod currentPeriod = new CurrentPeriod();

    if (StringUtils.isNotBlank(creditorsWithinOneYear.getDetails())) {
      currentPeriod.setDetails(creditorsWithinOneYear.getDetails());
    }

    if (creditorsWithinOneYear.getAccrualsAndDeferredIncome() != null
        && creditorsWithinOneYear.getAccrualsAndDeferredIncome()
            .getCurrentAccrualsAndDeferredIncome() != null) {
      currentPeriod.setAccrualsAndDeferredIncome(creditorsWithinOneYear
          .getAccrualsAndDeferredIncome().getCurrentAccrualsAndDeferredIncome());
    }

    if (creditorsWithinOneYear.getBankLoansAndOverdrafts() != null
        && creditorsWithinOneYear.getBankLoansAndOverdrafts().getCurrentBankLoansAndOverdrafts() != null) {
      currentPeriod.setBankLoansAndOverdrafts(creditorsWithinOneYear.getBankLoansAndOverdrafts()
          .getCurrentBankLoansAndOverdrafts());
    }

    if (creditorsWithinOneYear.getFinanceLeasesAndHirePurchaseContracts() != null
        && creditorsWithinOneYear.getFinanceLeasesAndHirePurchaseContracts()
            .getCurrentFinanceLeasesAndHirePurchaseContracts() != null) {
      currentPeriod.setFinanceLeasesAndHirePurchaseContracts(creditorsWithinOneYear
          .getFinanceLeasesAndHirePurchaseContracts()
          .getCurrentFinanceLeasesAndHirePurchaseContracts());
    }

    if (creditorsWithinOneYear.getOtherCreditors() != null
        && creditorsWithinOneYear.getOtherCreditors().getCurrentOtherCreditors() != null) {
      currentPeriod.setOtherCreditors(creditorsWithinOneYear.getOtherCreditors()
          .getCurrentOtherCreditors());
    }

    if (creditorsWithinOneYear.getTaxationAndSocialSecurity() != null
        && creditorsWithinOneYear.getTaxationAndSocialSecurity()
            .getCurrentTaxationAndSocialSecurity() != null) {
      currentPeriod.setTaxationAndSocialSecurity(creditorsWithinOneYear
          .getTaxationAndSocialSecurity().getCurrentTaxationAndSocialSecurity());
    }

    if (creditorsWithinOneYear.getTotal() != null
        && creditorsWithinOneYear.getTotal().getCurrentTotal() != null) {
      currentPeriod.setTotal(creditorsWithinOneYear.getTotal().getCurrentTotal());
    }

    if (creditorsWithinOneYear.getTradeCreditors() != null
        && creditorsWithinOneYear.getTradeCreditors().getCurrentTradeCreditors() != null) {
      currentPeriod.setTradeCreditors(creditorsWithinOneYear.getTradeCreditors()
          .getCurrentTradeCreditors());
    }

      if (isCurrentPeriodPopulated(currentPeriod)) {
          creditorsWithinOneYearApi.setCreditorsWithinOneYearCurrentPeriod(currentPeriod);
      }
  }

  private void setPreviousPeriodOnApiModel(CreditorsWithinOneYear creditorsWithinOneYear,
      CreditorsWithinOneYearApi creditorsWithinOneYearApi) {
    PreviousPeriod previousPeriod = new PreviousPeriod();

    if (creditorsWithinOneYear.getAccrualsAndDeferredIncome() != null
        && creditorsWithinOneYear.getAccrualsAndDeferredIncome()
            .getPreviousAccrualsAndDeferredIncome() != null) {
      previousPeriod.setAccrualsAndDeferredIncome(creditorsWithinOneYear
          .getAccrualsAndDeferredIncome().getPreviousAccrualsAndDeferredIncome());
    }

    if (creditorsWithinOneYear.getBankLoansAndOverdrafts() != null
        && creditorsWithinOneYear.getBankLoansAndOverdrafts().getPreviousBankLoansAndOverdrafts() != null) {
      previousPeriod.setBankLoansAndOverdrafts(creditorsWithinOneYear.getBankLoansAndOverdrafts()
          .getPreviousBankLoansAndOverdrafts());
    }

    if (creditorsWithinOneYear.getFinanceLeasesAndHirePurchaseContracts() != null
        && creditorsWithinOneYear.getFinanceLeasesAndHirePurchaseContracts()
            .getPreviousFinanceLeasesAndHirePurchaseContracts() != null) {
      previousPeriod.setFinanceLeasesAndHirePurchaseContracts(creditorsWithinOneYear
          .getFinanceLeasesAndHirePurchaseContracts()
          .getPreviousFinanceLeasesAndHirePurchaseContracts());
    }

    if (creditorsWithinOneYear.getOtherCreditors() != null
        && creditorsWithinOneYear.getOtherCreditors().getPreviousOtherCreditors() != null) {
      previousPeriod.setOtherCreditors(creditorsWithinOneYear.getOtherCreditors()
          .getPreviousOtherCreditors());
    }

    if (creditorsWithinOneYear.getTaxationAndSocialSecurity() != null
        && creditorsWithinOneYear.getTaxationAndSocialSecurity()
            .getPreviousTaxationAndSocialSecurity() != null) {
      previousPeriod.setTaxationAndSocialSecurity(creditorsWithinOneYear
          .getTaxationAndSocialSecurity().getPreviousTaxationAndSocialSecurity());
    }

    if (creditorsWithinOneYear.getTotal() != null
        && creditorsWithinOneYear.getTotal().getPreviousTotal() != null) {
      previousPeriod.setTotal(creditorsWithinOneYear.getTotal().getPreviousTotal());
    }

    if (creditorsWithinOneYear.getTradeCreditors() != null
        && creditorsWithinOneYear.getTradeCreditors().getPreviousTradeCreditors() != null) {
      previousPeriod.setTradeCreditors(creditorsWithinOneYear.getTradeCreditors()
          .getPreviousTradeCreditors());
    }

    if (isPreviousPeriodPopulated(previousPeriod)) {
      creditorsWithinOneYearApi.setCreditorsWithinOneYearPreviousPeriod(previousPeriod);
    }
  }

    private boolean isCurrentPeriodPopulated(CurrentPeriod currentPeriod) {

        return Stream.of(currentPeriod.getAccrualsAndDeferredIncome(),
                currentPeriod.getBankLoansAndOverdrafts(),
                currentPeriod.getFinanceLeasesAndHirePurchaseContracts(),
                currentPeriod.getOtherCreditors(),
                currentPeriod.getTaxationAndSocialSecurity(),
                currentPeriod.getTotal(),
                currentPeriod.getTradeCreditors()).anyMatch(Objects::nonNull);
    }

    private boolean isPreviousPeriodPopulated(PreviousPeriod previousPeriod) {

        return Stream.of(previousPeriod.getAccrualsAndDeferredIncome(),
                previousPeriod.getBankLoansAndOverdrafts(),
                previousPeriod.getFinanceLeasesAndHirePurchaseContracts(),
                previousPeriod.getOtherCreditors(),
                previousPeriod.getTaxationAndSocialSecurity(),
                previousPeriod.getTotal(),
                previousPeriod.getTradeCreditors()).anyMatch(Objects::nonNull);
    }
}
