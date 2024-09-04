package uk.gov.companieshouse.web.accounts.transformer.smallfull.impl;

import java.util.Objects;
import java.util.stream.Stream;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.api.model.accounts.smallfull.creditorswithinoneyear.CreditorsWithinOneYearApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.creditorswithinoneyear.CurrentPeriod;
import uk.gov.companieshouse.api.model.accounts.smallfull.creditorswithinoneyear.PreviousPeriod;
import uk.gov.companieshouse.web.accounts.enumeration.NoteType;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.creditorswithinoneyear.AccrualsAndDeferredIncome;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.creditorswithinoneyear.BankLoansAndOverdrafts;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.creditorswithinoneyear.CreditorsWithinOneYear;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.creditorswithinoneyear.FinanceLeasesAndHirePurchaseContracts;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.creditorswithinoneyear.OtherCreditors;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.creditorswithinoneyear.TaxationAndSocialSecurity;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.creditorswithinoneyear.Total;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.creditorswithinoneyear.TradeCreditors;
import uk.gov.companieshouse.web.accounts.transformer.NoteTransformer;

@Component
public class CreditorsWithinOneYearTransformerImpl implements NoteTransformer<CreditorsWithinOneYear, CreditorsWithinOneYearApi> {

    @Override
    public CreditorsWithinOneYear toWeb(
            CreditorsWithinOneYearApi creditorsWithinOneYearApi) {

        CreditorsWithinOneYear creditorsWithinOneYear = new CreditorsWithinOneYear();

        if (creditorsWithinOneYearApi == null) {
            return creditorsWithinOneYear;
        }

        populateCurrentPeriodForWeb(creditorsWithinOneYearApi, creditorsWithinOneYear);
        populatePreviousPeriodForWeb(creditorsWithinOneYearApi, creditorsWithinOneYear);

        return creditorsWithinOneYear;
    }


    private void populateCurrentPeriodForWeb(CreditorsWithinOneYearApi creditorsWithinOneYearApi,
            CreditorsWithinOneYear creditorsWithinOneYear) {

        CurrentPeriod currentPeriod =
                creditorsWithinOneYearApi.getCreditorsWithinOneYearCurrentPeriod();

        if (currentPeriod != null) {

            creditorsWithinOneYear.setDetails(currentPeriod.getDetails());

            if (currentPeriod.getAccrualsAndDeferredIncome() != null) {
                AccrualsAndDeferredIncome accrualsAndDeferredIncome =
                        createAccrualsAndDeferredIncome(creditorsWithinOneYear);
                accrualsAndDeferredIncome.setCurrentAccrualsAndDeferredIncome(
                        currentPeriod.getAccrualsAndDeferredIncome());
            }

            if (currentPeriod.getBankLoansAndOverdrafts() != null) {
                BankLoansAndOverdrafts bankLoansAndOverdrafts =
                        createBankLoansAndOverdrafts(creditorsWithinOneYear);
                bankLoansAndOverdrafts.setCurrentBankLoansAndOverdrafts(
                        currentPeriod.getBankLoansAndOverdrafts());
            }

            if (currentPeriod.getFinanceLeasesAndHirePurchaseContracts() != null) {
                FinanceLeasesAndHirePurchaseContracts financeLeasesAndHirePurchaseContracts =
                        createFinanceLeasesAndHirePurchaseContracts(creditorsWithinOneYear);
                financeLeasesAndHirePurchaseContracts.setCurrentFinanceLeasesAndHirePurchaseContracts(
                        currentPeriod.getFinanceLeasesAndHirePurchaseContracts());
            }

            if (currentPeriod.getOtherCreditors() != null) {
                OtherCreditors otherCreditors = createOtherCreditors(creditorsWithinOneYear);
                otherCreditors.setCurrentOtherCreditors(currentPeriod.getOtherCreditors());
            }

            if (currentPeriod.getTaxationAndSocialSecurity() != null) {
                TaxationAndSocialSecurity taxationAndSocialSecurity =
                        createTaxationAndSocialSecurity(creditorsWithinOneYear);
                taxationAndSocialSecurity.setCurrentTaxationAndSocialSecurity(
                        currentPeriod.getTaxationAndSocialSecurity());
            }

            if (currentPeriod.getTotal() != null) {
                Total total = createTotal(creditorsWithinOneYear);
                total.setCurrentTotal(currentPeriod.getTotal());
            }

            if (currentPeriod.getTradeCreditors() != null) {
                TradeCreditors tradeCreditors = createTradeCreditors(creditorsWithinOneYear);
                tradeCreditors.setCurrentTradeCreditors(currentPeriod.getTradeCreditors());
            }
        }
    }

    private void populatePreviousPeriodForWeb(CreditorsWithinOneYearApi creditorsWithinOneYearApi,
            CreditorsWithinOneYear creditorsWithinOneYear) {

        PreviousPeriod previousPeriod =
                creditorsWithinOneYearApi.getCreditorsWithinOneYearPreviousPeriod();

        if (previousPeriod != null) {

            if (previousPeriod.getAccrualsAndDeferredIncome() != null) {
                AccrualsAndDeferredIncome accrualsAndDeferredIncome =
                        createAccrualsAndDeferredIncome(creditorsWithinOneYear);
                accrualsAndDeferredIncome.setPreviousAccrualsAndDeferredIncome(
                        previousPeriod.getAccrualsAndDeferredIncome());
            }

            if (previousPeriod.getBankLoansAndOverdrafts() != null) {
                BankLoansAndOverdrafts bankLoansAndOverdrafts =
                        createBankLoansAndOverdrafts(creditorsWithinOneYear);
                bankLoansAndOverdrafts.setPreviousBankLoansAndOverdrafts(
                        previousPeriod.getBankLoansAndOverdrafts());
            }

            if (previousPeriod.getFinanceLeasesAndHirePurchaseContracts() != null) {
                FinanceLeasesAndHirePurchaseContracts financeLeasesAndHirePurchaseContracts =
                        createFinanceLeasesAndHirePurchaseContracts(creditorsWithinOneYear);
                financeLeasesAndHirePurchaseContracts.setPreviousFinanceLeasesAndHirePurchaseContracts(
                        previousPeriod.getFinanceLeasesAndHirePurchaseContracts());
            }

            if (previousPeriod.getOtherCreditors() != null) {
                OtherCreditors otherCreditors = createOtherCreditors(creditorsWithinOneYear);
                otherCreditors.setPreviousOtherCreditors(previousPeriod.getOtherCreditors());
            }

            if (previousPeriod.getTaxationAndSocialSecurity() != null) {
                TaxationAndSocialSecurity taxationAndSocialSecurity =
                        createTaxationAndSocialSecurity(creditorsWithinOneYear);
                taxationAndSocialSecurity.setPreviousTaxationAndSocialSecurity(
                        previousPeriod.getTaxationAndSocialSecurity());
            }

            if (previousPeriod.getTotal() != null) {
                Total total = createTotal(creditorsWithinOneYear);
                total.setPreviousTotal(previousPeriod.getTotal());
            }

            if (previousPeriod.getTradeCreditors() != null) {
                TradeCreditors tradeCreditors = createTradeCreditors(creditorsWithinOneYear);
                tradeCreditors.setPreviousTradeCreditors(previousPeriod.getTradeCreditors());
            }
        }
    }

    private AccrualsAndDeferredIncome createAccrualsAndDeferredIncome(
            CreditorsWithinOneYear creditorsWithinOneYear) {

        AccrualsAndDeferredIncome accrualsAndDeferredIncome;

        if (creditorsWithinOneYear.getAccrualsAndDeferredIncome() != null) {
            accrualsAndDeferredIncome = creditorsWithinOneYear.getAccrualsAndDeferredIncome();
        } else {
            accrualsAndDeferredIncome = new AccrualsAndDeferredIncome();
            creditorsWithinOneYear.setAccrualsAndDeferredIncome(accrualsAndDeferredIncome);
        }

        return accrualsAndDeferredIncome;
    }

    private BankLoansAndOverdrafts createBankLoansAndOverdrafts(CreditorsWithinOneYear creditorsWithinOneYear) {

        BankLoansAndOverdrafts bankLoansAndOverdrafts;

        if (creditorsWithinOneYear.getBankLoansAndOverdrafts() != null) {
            bankLoansAndOverdrafts = creditorsWithinOneYear.getBankLoansAndOverdrafts();
        } else {
            bankLoansAndOverdrafts = new BankLoansAndOverdrafts();
            creditorsWithinOneYear.setBankLoansAndOverdrafts(bankLoansAndOverdrafts);
        }

        return bankLoansAndOverdrafts;
    }

    private FinanceLeasesAndHirePurchaseContracts createFinanceLeasesAndHirePurchaseContracts(CreditorsWithinOneYear creditorsWithinOneYear) {

        FinanceLeasesAndHirePurchaseContracts financeLeasesAndHirePurchaseContracts;

        if (creditorsWithinOneYear.getFinanceLeasesAndHirePurchaseContracts() != null) {
            financeLeasesAndHirePurchaseContracts = creditorsWithinOneYear.getFinanceLeasesAndHirePurchaseContracts();
        } else {
            financeLeasesAndHirePurchaseContracts = new FinanceLeasesAndHirePurchaseContracts();
            creditorsWithinOneYear.setFinanceLeasesAndHirePurchaseContracts(financeLeasesAndHirePurchaseContracts);
        }

        return financeLeasesAndHirePurchaseContracts;
    }

    private OtherCreditors createOtherCreditors(CreditorsWithinOneYear creditorsWithinOneYear) {

        OtherCreditors otherCreditors;

        if (creditorsWithinOneYear.getOtherCreditors() != null) {
            otherCreditors = creditorsWithinOneYear.getOtherCreditors();
        } else {
            otherCreditors = new OtherCreditors();
            creditorsWithinOneYear.setOtherCreditors(otherCreditors);
        }

        return otherCreditors;
    }

    private TaxationAndSocialSecurity createTaxationAndSocialSecurity(CreditorsWithinOneYear creditorsWithinOneYear) {

        TaxationAndSocialSecurity taxationAndSocialSecurity;

        if (creditorsWithinOneYear.getTaxationAndSocialSecurity() != null) {
            taxationAndSocialSecurity = creditorsWithinOneYear.getTaxationAndSocialSecurity();
        } else {
            taxationAndSocialSecurity = new TaxationAndSocialSecurity();
            creditorsWithinOneYear.setTaxationAndSocialSecurity(taxationAndSocialSecurity);
        }

        return taxationAndSocialSecurity;
    }

    private Total createTotal(CreditorsWithinOneYear creditorsWithinOneYear) {

        Total total;

        if (creditorsWithinOneYear.getTotal() != null) {
            total = creditorsWithinOneYear.getTotal();
        } else {
            total = new Total();
            creditorsWithinOneYear.setTotal(total);
        }

        return total;
    }

    private TradeCreditors createTradeCreditors(CreditorsWithinOneYear creditorsWithinOneYear) {

        TradeCreditors tradeCreditors;

        if (creditorsWithinOneYear.getTradeCreditors() != null) {
            tradeCreditors = creditorsWithinOneYear.getTradeCreditors();
        } else {
            tradeCreditors = new TradeCreditors();
            creditorsWithinOneYear.setTradeCreditors(tradeCreditors);
        }

        return tradeCreditors;
    }

    @Override
    public CreditorsWithinOneYearApi toApi(
            CreditorsWithinOneYear creditorsWithinOneYear) {

        CreditorsWithinOneYearApi creditorsWithinOneYearApi = new CreditorsWithinOneYearApi();

        setCurrentPeriodOnApiModel(creditorsWithinOneYear, creditorsWithinOneYearApi);

        setPreviousPeriodOnApiModel(creditorsWithinOneYear, creditorsWithinOneYearApi);

        return creditorsWithinOneYearApi;
    }

    @Override
    public NoteType getNoteType() {
        return NoteType.SMALL_FULL_CREDITORS_WITHIN_ONE_YEAR;
    }

    private void setCurrentPeriodOnApiModel(CreditorsWithinOneYear creditorsWithinOneYear,
            CreditorsWithinOneYearApi creditorsWithinOneYearApi) {

        CurrentPeriod currentPeriod = new CurrentPeriod();

        if (StringUtils.isNotBlank(creditorsWithinOneYear.getDetails())) {
            currentPeriod.setDetails(creditorsWithinOneYear.getDetails());
        }

        currentPeriod.setAccrualsAndDeferredIncome(creditorsWithinOneYear
                .getAccrualsAndDeferredIncome().getCurrentAccrualsAndDeferredIncome());

        currentPeriod
                .setBankLoansAndOverdrafts(creditorsWithinOneYear.getBankLoansAndOverdrafts()
                        .getCurrentBankLoansAndOverdrafts());

        currentPeriod.setFinanceLeasesAndHirePurchaseContracts(creditorsWithinOneYear
                .getFinanceLeasesAndHirePurchaseContracts()
                .getCurrentFinanceLeasesAndHirePurchaseContracts());

        currentPeriod.setOtherCreditors(creditorsWithinOneYear.getOtherCreditors()
                .getCurrentOtherCreditors());

        currentPeriod.setTaxationAndSocialSecurity(creditorsWithinOneYear
                .getTaxationAndSocialSecurity().getCurrentTaxationAndSocialSecurity());

        currentPeriod.setTotal(creditorsWithinOneYear.getTotal().getCurrentTotal());

        currentPeriod.setTradeCreditors(creditorsWithinOneYear.getTradeCreditors()
                .getCurrentTradeCreditors());

        if (isCurrentPeriodPopulated(currentPeriod)) {
            creditorsWithinOneYearApi.setCreditorsWithinOneYearCurrentPeriod(currentPeriod);
        }
    }

    private void setPreviousPeriodOnApiModel(CreditorsWithinOneYear creditorsWithinOneYear,
            CreditorsWithinOneYearApi creditorsWithinOneYearApi) {

        PreviousPeriod previousPeriod = new PreviousPeriod();

        previousPeriod.setAccrualsAndDeferredIncome(creditorsWithinOneYear
                .getAccrualsAndDeferredIncome().getPreviousAccrualsAndDeferredIncome());

        previousPeriod
                .setBankLoansAndOverdrafts(creditorsWithinOneYear.getBankLoansAndOverdrafts()
                        .getPreviousBankLoansAndOverdrafts());

        previousPeriod.setFinanceLeasesAndHirePurchaseContracts(creditorsWithinOneYear
                .getFinanceLeasesAndHirePurchaseContracts()
                .getPreviousFinanceLeasesAndHirePurchaseContracts());

        previousPeriod.setOtherCreditors(creditorsWithinOneYear.getOtherCreditors()
                .getPreviousOtherCreditors());

        previousPeriod.setTaxationAndSocialSecurity(creditorsWithinOneYear
                .getTaxationAndSocialSecurity().getPreviousTaxationAndSocialSecurity());

        previousPeriod.setTotal(creditorsWithinOneYear.getTotal().getPreviousTotal());

        previousPeriod.setTradeCreditors(creditorsWithinOneYear.getTradeCreditors()
                .getPreviousTradeCreditors());

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
                currentPeriod.getTradeCreditors(),
                currentPeriod.getDetails()).anyMatch(Objects::nonNull);
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
