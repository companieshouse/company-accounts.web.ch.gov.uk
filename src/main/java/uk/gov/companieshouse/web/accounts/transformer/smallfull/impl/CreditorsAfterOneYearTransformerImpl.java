package uk.gov.companieshouse.web.accounts.transformer.smallfull.impl;

import java.util.Objects;
import java.util.stream.Stream;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.api.model.accounts.smallfull.creditorsafteroneyear.CreditorsAfterOneYearApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.creditorsafteroneyear.CurrentPeriod;
import uk.gov.companieshouse.api.model.accounts.smallfull.creditorsafteroneyear.PreviousPeriod;
import uk.gov.companieshouse.web.accounts.enumeration.NoteType;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.creditorsafteroneyear.BankLoansAndOverdrafts;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.creditorsafteroneyear.CreditorsAfterOneYear;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.creditorsafteroneyear.FinanceLeasesAndHirePurchaseContracts;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.creditorsafteroneyear.OtherCreditors;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.creditorsafteroneyear.Total;
import uk.gov.companieshouse.web.accounts.transformer.NoteTransformer;

@Component
public class CreditorsAfterOneYearTransformerImpl
        implements NoteTransformer<CreditorsAfterOneYear, CreditorsAfterOneYearApi> {

    @Override
    public CreditorsAfterOneYear toWeb(CreditorsAfterOneYearApi creditorsAfterOneYearApi) {

        CreditorsAfterOneYear creditorsAfterOneYear = new CreditorsAfterOneYear();

        if (creditorsAfterOneYearApi == null) {
            return creditorsAfterOneYear;
        }

        populateCurrentPeriodForWeb(creditorsAfterOneYearApi, creditorsAfterOneYear);
        populatePreviousPeriodForWeb(creditorsAfterOneYearApi, creditorsAfterOneYear);

        return creditorsAfterOneYear;
    }

    @Override
    public CreditorsAfterOneYearApi toApi(CreditorsAfterOneYear creditorsAfterOneYear) {

        CreditorsAfterOneYearApi creditorsAfterOneYearApi = new CreditorsAfterOneYearApi();

        setCurrentPeriodOnApiModel(creditorsAfterOneYear, creditorsAfterOneYearApi);

        setPreviousPeriodOnApiModel(creditorsAfterOneYear, creditorsAfterOneYearApi);

        return creditorsAfterOneYearApi;
    }

    private void populateCurrentPeriodForWeb(CreditorsAfterOneYearApi creditorsAfterOneYearApi,
            CreditorsAfterOneYear creditorsAfterOneYear) {

        CurrentPeriod currentPeriod =
                creditorsAfterOneYearApi.getCurrentPeriod();

        if (currentPeriod != null) {

            creditorsAfterOneYear.setDetails(currentPeriod.getDetails());

            if (currentPeriod.getBankLoansAndOverdrafts() != null) {
                BankLoansAndOverdrafts bankLoansAndOverdrafts = createBankLoansAndOverdrafts(creditorsAfterOneYear);
                bankLoansAndOverdrafts.setCurrentBankLoansAndOverdrafts(currentPeriod.getBankLoansAndOverdrafts());
            }

            if (currentPeriod.getOtherCreditors() != null) {
                OtherCreditors otherCreditors = createOtherCreditors(creditorsAfterOneYear);
                otherCreditors.setCurrentOtherCreditors(currentPeriod.getOtherCreditors());
            }

            if (currentPeriod.getFinanceLeasesAndHirePurchaseContracts() != null) {
                FinanceLeasesAndHirePurchaseContracts financeLeasesAndHirePurchaseContracts =
                        createFinanceLeasesAndHirePurchaseContracts(creditorsAfterOneYear);
                financeLeasesAndHirePurchaseContracts.setCurrentFinanceLeasesAndHirePurchaseContracts(
                        currentPeriod.getFinanceLeasesAndHirePurchaseContracts());
            }

            if (currentPeriod.getTotal() != null) {
                Total total = createTotal(creditorsAfterOneYear);
                total.setCurrentTotal(currentPeriod.getTotal());
            }
        }
    }

    private void populatePreviousPeriodForWeb(CreditorsAfterOneYearApi creditorsAfterOneYearApi,
            CreditorsAfterOneYear creditorsAfterOneYear) {

        PreviousPeriod previousPeriod =
                creditorsAfterOneYearApi.getPreviousPeriod();

        if (previousPeriod != null) {

            if (previousPeriod.getBankLoansAndOverdrafts() != null) {
                BankLoansAndOverdrafts bankLoansAndOverdrafts = createBankLoansAndOverdrafts(creditorsAfterOneYear);
                bankLoansAndOverdrafts.setPreviousBankLoansAndOverdrafts(previousPeriod.getBankLoansAndOverdrafts());
            }

            if (previousPeriod.getOtherCreditors() != null) {
                OtherCreditors otherCreditors = createOtherCreditors(creditorsAfterOneYear);
                otherCreditors.setPreviousOtherCreditors(previousPeriod.getOtherCreditors());
            }

            if (previousPeriod.getFinanceLeasesAndHirePurchaseContracts() != null) {
                FinanceLeasesAndHirePurchaseContracts financeLeasesAndHirePurchaseContracts =
                        createFinanceLeasesAndHirePurchaseContracts(creditorsAfterOneYear);
                financeLeasesAndHirePurchaseContracts.setPreviousFinanceLeasesAndHirePurchaseContracts(
                        previousPeriod.getFinanceLeasesAndHirePurchaseContracts());
            }

            if (previousPeriod.getTotal() != null) {
                Total total = createTotal(creditorsAfterOneYear);
                total.setPreviousTotal(previousPeriod.getTotal());
            }
        }
    }

    private BankLoansAndOverdrafts createBankLoansAndOverdrafts(CreditorsAfterOneYear creditorsAfterOneYear) {

        BankLoansAndOverdrafts bankLoansAndOverdrafts;

        if (creditorsAfterOneYear.getBankLoansAndOverdrafts() != null) {
            bankLoansAndOverdrafts = creditorsAfterOneYear.getBankLoansAndOverdrafts();
        } else {
            bankLoansAndOverdrafts = new BankLoansAndOverdrafts();
            creditorsAfterOneYear.setBankLoansAndOverdrafts(bankLoansAndOverdrafts);
        }

        return bankLoansAndOverdrafts;
    }

    private OtherCreditors createOtherCreditors(CreditorsAfterOneYear creditorsAfterOneYear) {

        OtherCreditors otherCreditors;

        if (creditorsAfterOneYear.getOtherCreditors() != null) {
            otherCreditors = creditorsAfterOneYear.getOtherCreditors();
        } else {
            otherCreditors = new OtherCreditors();
            creditorsAfterOneYear.setOtherCreditors(otherCreditors);
        }

        return otherCreditors;
    }

    private FinanceLeasesAndHirePurchaseContracts createFinanceLeasesAndHirePurchaseContracts(CreditorsAfterOneYear creditorsAfterOneYear) {

        FinanceLeasesAndHirePurchaseContracts financeLeasesAndHirePurchaseContracts;

        if (creditorsAfterOneYear.getFinanceLeasesAndHirePurchaseContracts() != null) {
            financeLeasesAndHirePurchaseContracts = creditorsAfterOneYear.getFinanceLeasesAndHirePurchaseContracts();
        } else {
            financeLeasesAndHirePurchaseContracts = new FinanceLeasesAndHirePurchaseContracts();
            creditorsAfterOneYear.setFinanceLeasesAndHirePurchaseContracts(financeLeasesAndHirePurchaseContracts);
        }

        return financeLeasesAndHirePurchaseContracts;
    }

    private Total createTotal(CreditorsAfterOneYear creditorsAfterOneYear) {

        Total total;

        if (creditorsAfterOneYear.getTotal() != null) {
            total = creditorsAfterOneYear.getTotal();
        } else {
            total = new Total();
            creditorsAfterOneYear.setTotal(total);
        }

        return total;
    }

    private void setCurrentPeriodOnApiModel(CreditorsAfterOneYear creditorsAfterOneYear,
            CreditorsAfterOneYearApi creditorsAfterOneYearApi) {
        CurrentPeriod currentPeriod = new CurrentPeriod();

        if (StringUtils.isNotBlank(creditorsAfterOneYear.getDetails())) {
            currentPeriod.setDetails(creditorsAfterOneYear.getDetails());
        }

        currentPeriod.setBankLoansAndOverdrafts(creditorsAfterOneYear.getBankLoansAndOverdrafts()
                .getCurrentBankLoansAndOverdrafts());

        currentPeriod.setFinanceLeasesAndHirePurchaseContracts(creditorsAfterOneYear
                .getFinanceLeasesAndHirePurchaseContracts()
                .getCurrentFinanceLeasesAndHirePurchaseContracts());

        currentPeriod.setOtherCreditors(creditorsAfterOneYear.getOtherCreditors()
                .getCurrentOtherCreditors());

        currentPeriod.setTotal(creditorsAfterOneYear.getTotal().getCurrentTotal());

        if (isCurrentPeriodPopulated(currentPeriod)) {
            creditorsAfterOneYearApi.setCurrentPeriod(currentPeriod);
        }
    }

    private void setPreviousPeriodOnApiModel(CreditorsAfterOneYear creditorsAfterOneYear,
            CreditorsAfterOneYearApi creditorsAfterOneYearApi) {
        PreviousPeriod previousPeriod = new PreviousPeriod();

        previousPeriod.setBankLoansAndOverdrafts(creditorsAfterOneYear.getBankLoansAndOverdrafts()
                .getPreviousBankLoansAndOverdrafts());

        previousPeriod.setFinanceLeasesAndHirePurchaseContracts(creditorsAfterOneYear
                .getFinanceLeasesAndHirePurchaseContracts()
                .getPreviousFinanceLeasesAndHirePurchaseContracts());

        previousPeriod.setOtherCreditors(creditorsAfterOneYear.getOtherCreditors()
                .getPreviousOtherCreditors());

        previousPeriod.setTotal(creditorsAfterOneYear.getTotal().getPreviousTotal());

        if (isPreviousPeriodPopulated(previousPeriod)) {
            creditorsAfterOneYearApi.setPreviousPeriod(previousPeriod);
        }
    }

    private boolean isCurrentPeriodPopulated(CurrentPeriod currentPeriod) {

        return Stream.of(currentPeriod.getBankLoansAndOverdrafts(),
                currentPeriod.getFinanceLeasesAndHirePurchaseContracts(),
                currentPeriod.getOtherCreditors(),
                currentPeriod.getTotal(),
                currentPeriod.getDetails()).anyMatch(Objects::nonNull);
    }

    private boolean isPreviousPeriodPopulated(PreviousPeriod previousPeriod) {

        return Stream.of(previousPeriod.getBankLoansAndOverdrafts(),
                previousPeriod.getFinanceLeasesAndHirePurchaseContracts(),
                previousPeriod.getOtherCreditors(),
                previousPeriod.getTotal()).anyMatch(Objects::nonNull);
    }

    @Override
    public NoteType getNoteType() {
        return NoteType.SMALL_FULL_CREDITORS_AFTER_ONE_YEAR;
    }
}
