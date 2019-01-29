package uk.gov.companieshouse.web.accounts.transformer.smallfull.impl;

import java.util.Objects;
import java.util.stream.Stream;
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
public class CreditorsAfterOneYearTransformerImpl implements CreditorsAfterOneYearTransformer {

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
                bankLoansAndOverdrafts, otherCreditors, total,
                financeLeasesAndHirePurchaseContracts);

        populatePreviousPeriodForWeb(creditorsAfterOneYearApi,
                bankLoansAndOverdrafts, otherCreditors, total,
                financeLeasesAndHirePurchaseContracts);

        creditorsAfterOneYear.setBankLoansAndOverdrafts(bankLoansAndOverdrafts);
        creditorsAfterOneYear.setFinanceLeasesAndHirePurchaseContracts(financeLeasesAndHirePurchaseContracts);
        creditorsAfterOneYear.setOtherCreditors(otherCreditors);
        creditorsAfterOneYear.setTotal(total);

        return creditorsAfterOneYear;
    }


    @Override
    public CreditorsAfterOneYearApi getCreditorsAfterOneYearApi(CreditorsAfterOneYear creditorsAfterOneYear) {

        CreditorsAfterOneYearApi creditorsAfterOneYearApi = new CreditorsAfterOneYearApi();

        if (creditorsAfterOneYear.getTotal() != null) {
            if (creditorsAfterOneYear.getTotal().getCurrentTotal() != null) {
                setCurrentPeriodOnApiModel(creditorsAfterOneYear, creditorsAfterOneYearApi);
            }

            if (creditorsAfterOneYear.getTotal().getPreviousTotal() != null) {
                setPreviousPeriodOnApiModel(creditorsAfterOneYear, creditorsAfterOneYearApi);
            }
        }

        return creditorsAfterOneYearApi;
    }

    private void populateCurrentPeriodForWeb(CreditorsAfterOneYearApi creditorsAfterOneYearApi,
            CreditorsAfterOneYear creditorsAfterOneYear,
            BankLoansAndOverdrafts bankLoansAndOverdrafts, OtherCreditors otherCreditors,
            Total total,
            FinanceLeasesAndHirePurchaseContracts financeLeasesAndHirePurchaseContracts) {

        CurrentPeriod currentPeriod =
                creditorsAfterOneYearApi.getCurrentPeriod();

        if (currentPeriod != null) {

            creditorsAfterOneYear.setDetails(currentPeriod.getDetails());


            bankLoansAndOverdrafts.setCurrentBankLoansAndOverdrafts(currentPeriod
                    .getBankLoansAndOverdrafts());
            otherCreditors.setCurrentOtherCreditors(currentPeriod.getOtherCreditors());
            financeLeasesAndHirePurchaseContracts.
                    setCurrentFinanceLeasesAndHirePurchaseContracts(currentPeriod.
                            getFinanceLeasesAndHirePurchaseContracts());
            total.setCurrentTotal(currentPeriod.getTotal());
        }
    }

    private void populatePreviousPeriodForWeb(CreditorsAfterOneYearApi creditorsAfterOneYearApi,
            BankLoansAndOverdrafts bankLoansAndOverdrafts, OtherCreditors otherCreditors,
            Total total,
            FinanceLeasesAndHirePurchaseContracts financeLeasesAndHirePurchaseContracts) {

        PreviousPeriod previousPeriod =
                creditorsAfterOneYearApi.getPreviousPeriod();

        if (previousPeriod != null) {

            bankLoansAndOverdrafts.setPreviousBankLoansAndOverdrafts(previousPeriod
                    .getBankLoansAndOverdrafts());
            otherCreditors.setPreviousOtherCreditors(previousPeriod.getOtherCreditors());
            financeLeasesAndHirePurchaseContracts.
                    setPreviousFinanceLeasesAndHirePurchaseContracts(previousPeriod
                            .getFinanceLeasesAndHirePurchaseContracts());
            total.setPreviousTotal(previousPeriod.getTotal());
        }
    }

    private void setCurrentPeriodOnApiModel(CreditorsAfterOneYear creditorsAfterOneYear,
            CreditorsAfterOneYearApi creditorsAfterOneYearApi) {
        CurrentPeriod currentPeriod = new CurrentPeriod();

        if (creditorsAfterOneYear.getDetails() != null
                && creditorsAfterOneYear.getDetails().equals("")) {
            currentPeriod.setDetails(null);
        } else {
            currentPeriod.setDetails(creditorsAfterOneYear.getDetails());
        }

        if (creditorsAfterOneYear.getBankLoansAndOverdrafts() != null
                && creditorsAfterOneYear.getBankLoansAndOverdrafts().getCurrentBankLoansAndOverdrafts() != null) {
            currentPeriod.setBankLoansAndOverdrafts(creditorsAfterOneYear.getBankLoansAndOverdrafts()
                    .getCurrentBankLoansAndOverdrafts());
        }

        if (creditorsAfterOneYear.getFinanceLeasesAndHirePurchaseContracts() != null
                && creditorsAfterOneYear.getFinanceLeasesAndHirePurchaseContracts()
                .getCurrentFinanceLeasesAndHirePurchaseContracts() != null) {
            currentPeriod.setFinanceLeasesAndHirePurchaseContracts(creditorsAfterOneYear
                    .getFinanceLeasesAndHirePurchaseContracts()
                    .getCurrentFinanceLeasesAndHirePurchaseContracts());
        }

        if (creditorsAfterOneYear.getOtherCreditors() != null
                && creditorsAfterOneYear.getOtherCreditors().getCurrentOtherCreditors() != null) {
            currentPeriod.setOtherCreditors(creditorsAfterOneYear.getOtherCreditors()
                    .getCurrentOtherCreditors());
        }

        if (creditorsAfterOneYear.getTotal() != null
                && creditorsAfterOneYear.getTotal().getCurrentTotal() != null) {
            currentPeriod.setTotal(creditorsAfterOneYear.getTotal().getCurrentTotal());
        }

        creditorsAfterOneYearApi.setCurrentPeriod(currentPeriod);
    }

    private void setPreviousPeriodOnApiModel(CreditorsAfterOneYear creditorsAfterOneYear,
            CreditorsAfterOneYearApi creditorsAfterOneYearApi) {
        PreviousPeriod previousPeriod = new PreviousPeriod();

        if (creditorsAfterOneYear.getBankLoansAndOverdrafts() != null
                && creditorsAfterOneYear.getBankLoansAndOverdrafts().getPreviousBankLoansAndOverdrafts() != null) {
            previousPeriod.setBankLoansAndOverdrafts(creditorsAfterOneYear.getBankLoansAndOverdrafts()
                    .getPreviousBankLoansAndOverdrafts());
        }

        if (creditorsAfterOneYear.getFinanceLeasesAndHirePurchaseContracts() != null
                && creditorsAfterOneYear.getFinanceLeasesAndHirePurchaseContracts()
                .getPreviousFinanceLeasesAndHirePurchaseContracts() != null) {
            previousPeriod.setFinanceLeasesAndHirePurchaseContracts(creditorsAfterOneYear
                    .getFinanceLeasesAndHirePurchaseContracts()
                    .getPreviousFinanceLeasesAndHirePurchaseContracts());
        }

        if (creditorsAfterOneYear.getOtherCreditors() != null
                && creditorsAfterOneYear.getOtherCreditors().getPreviousOtherCreditors() != null) {
            previousPeriod.setOtherCreditors(creditorsAfterOneYear.getOtherCreditors()
                    .getPreviousOtherCreditors());
        }

        if (creditorsAfterOneYear.getTotal() != null
                && creditorsAfterOneYear.getTotal().getPreviousTotal() != null) {
            previousPeriod.setTotal(creditorsAfterOneYear.getTotal().getPreviousTotal());
        }

        if (isPreviousPeriodPopulated(previousPeriod)) {
            creditorsAfterOneYearApi.setPreviousPeriod(previousPeriod);
        }
    }

    private boolean isPreviousPeriodPopulated(PreviousPeriod period) {
        return Stream.of(
                period.getBankLoansAndOverdrafts(),
                period.getFinanceLeasesAndHirePurchaseContracts(),
                period.getOtherCreditors(),
           period.getTotal()).anyMatch(Objects ::nonNull);
    }

}
