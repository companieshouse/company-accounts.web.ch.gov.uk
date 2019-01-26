package uk.gov.companieshouse.web.accounts.transformer.smallfull.impl;

import org.springframework.stereotype.Component;
import uk.gov.companieshouse.api.model.accounts.smallfull.creditorsafteroneyear.CreditorsAfterOneYearApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.creditorsafteroneyear.CurrentPeriod;
import uk.gov.companieshouse.api.model.accounts.smallfull.creditorsafteroneyear.PreviousPeriod;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.creditorsafteroneyear.BankLoansAndOverdrafts;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.creditorsafteroneyear.CreditorsAfterOneYear;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.creditorsafteroneyear.FinanceLeasesAndHirePurchaseContracts;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.creditorsafteroneyear.OtherCreditors;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.creditorsafteroneyear.Total;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.CreditorsAfterOneYearTransformer;

@Component
public class CreditorsAfterOneYearTranformerImpl implements CreditorsAfterOneYearTransformer {


    @Override
    public CreditorsAfterOneYear getCreditorsAfterOneYear(CreditorsAfterOneYearApi creditorsAfterOneYearApi) {

        CreditorsAfterOneYear creditorsAfterOneYear = new CreditorsAfterOneYear();

        if (creditorsAfterOneYearApi == null) {
            return creditorsAfterOneYear;
        }

        BankLoansAndOverdrafts bankLoansAndOverdrafts = new BankLoansAndOverdrafts();
        FinanceLeasesAndHirePurchaseContracts financeLeasesAndHirePurchaseContracts =
                new FinanceLeasesAndHirePurchaseContracts();
        OtherCreditors otherCreditors = new OtherCreditors();
        Total total = new Total();

        populateCurrentPeriodForWeb(creditorsAfterOneYearApi, creditorsAfterOneYear,
                bankLoansAndOverdrafts, otherCreditors, total, financeLeasesAndHirePurchaseContracts);

        populatePreviousPeriodForWeb(creditorsAfterOneYearApi, creditorsAfterOneYear,
                bankLoansAndOverdrafts, otherCreditors, total, financeLeasesAndHirePurchaseContracts);

        creditorsAfterOneYear.setBankLoansAndOverdrafts(bankLoansAndOverdrafts);
        creditorsAfterOneYear.setFinanceLeasesAndHirePurchaseContracts(financeLeasesAndHirePurchaseContracts);
        creditorsAfterOneYear.setOtherCreditors(otherCreditors);
        creditorsAfterOneYear.setTotal(total);

        return creditorsAfterOneYear;
    }


    private void populateCurrentPeriodForWeb(CreditorsAfterOneYearApi creditorsAfterOneYearApi,
            CreditorsAfterOneYear creditorsAfterOneYear,
            BankLoansAndOverdrafts bankLoansAndOverdrafts, OtherCreditors otherCreditors,
            Total total, FinanceLeasesAndHirePurchaseContracts financeLeasesAndHirePurchaseContracts) {

        CurrentPeriod currentPeriod =
                creditorsAfterOneYearApi.getCurrentPeriod();

        if (currentPeriod != null) {

            creditorsAfterOneYear.setDetails(currentPeriod.getDetails());


            bankLoansAndOverdrafts.setCurrentBankLoansAndOverdrafts(currentPeriod
                    .getBankLoansAndOverdrafts());
            otherCreditors.setCurrentOtherCreditors(currentPeriod.getOtherCreditors());
            financeLeasesAndHirePurchaseContracts.setCurrentFinanceLeasesAndHirePurchaseContracts(currentPeriod.getFinanceLeasesAndHirePurchaseContracts());
            total.setCurrentTotal(currentPeriod.getTotal());
        }
    }

    private void populatePreviousPeriodForWeb(CreditorsAfterOneYearApi creditorsAfterOneYearApi,
    BankLoansAndOverdrafts bankLoansAndOverdrafts, OtherCreditors otherCreditors,
    Total total, FinanceLeasesAndHirePurchaseContracts financeLeasesAndHirePurchaseContracts)  {

        PreviousPeriod previousPeriod =
                creditorsAfterOneYearApi.getPreviousPeriod();

        if (previousPeriod != null) {

            bankLoansAndOverdrafts.setPreviousBankLoansAndOverdrafts(previousPeriod
                    .getBankLoansAndOverdrafts());
            otherCreditors.setPreviousOtherCreditors(previousPeriod.getOtherCreditors());
            financeLeasesAndHirePurchaseContracts.setPreviousFinanceLeasesAndHirePurchaseContracts(previousPeriod.getFinanceLeasesAndHirePurchaseContracts());
            total.setPreviousTotal(previousPeriod.getTotal());
        }
    }


    @Override
    public CreditorsAfterOneYearApi getCreditorsAfterOneYearApi(CreditorsAfterOneYear creditorsAfterOneYear) {
        return null;
    }
}
