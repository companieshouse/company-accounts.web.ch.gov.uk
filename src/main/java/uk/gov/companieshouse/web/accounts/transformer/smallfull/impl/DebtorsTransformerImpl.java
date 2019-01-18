package uk.gov.companieshouse.web.accounts.transformer.smallfull.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.api.model.accounts.smallfull.Debtors.CurrentPeriod;
import uk.gov.companieshouse.api.model.accounts.smallfull.Debtors.DebtorsApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.Debtors.PreviousPeriod;
import uk.gov.companieshouse.api.model.transaction.Transaction;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.debtors.Debtors;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.debtors.GreaterThanOneYear;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.debtors.OtherDebtors;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.debtors.PrepaymentsAndAccruedIncome;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.debtors.Total;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.debtors.TradeDebtors;
import uk.gov.companieshouse.web.accounts.service.company.CompanyService;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.DebtorsTransformer;

@Component
public class DebtorsTransformerImpl implements DebtorsTransformer {

    @Autowired
    CompanyService companyService;

    @Override
    public Debtors getDebtors(DebtorsApi debtorsApi) {

        if (debtorsApi == null) {
            return new Debtors();
        }

        Debtors debtors = new Debtors();

        TradeDebtors tradeDebtors = new TradeDebtors();
        PrepaymentsAndAccruedIncome prepaymentsAndAccruedIncome = new PrepaymentsAndAccruedIncome();
        OtherDebtors otherDebtors = new OtherDebtors();
        Total total = new Total();
        GreaterThanOneYear greaterThanOneYear = new GreaterThanOneYear();

        getDebtorsCurrentPeriodForWeb(debtorsApi, debtors, tradeDebtors,
                prepaymentsAndAccruedIncome, otherDebtors, total, greaterThanOneYear);
        getDebtorsPreviousPeriodForWeb(debtorsApi, tradeDebtors, prepaymentsAndAccruedIncome,
                otherDebtors, total, greaterThanOneYear);

        debtors.setGreaterThanOneYear(greaterThanOneYear);
        debtors.setTradeDebtors(tradeDebtors);
        debtors.setPrepaymentsAndAccruedIncome(prepaymentsAndAccruedIncome);
        debtors.setOtherDebtors(otherDebtors);
        debtors.setTotal(total);

        return debtors;
    }

    private void getDebtorsPreviousPeriodForWeb(DebtorsApi debtorsApi, TradeDebtors tradeDebtors,
            PrepaymentsAndAccruedIncome prepaymentsAndAccruedIncome, OtherDebtors otherDebtors,
            Total total, GreaterThanOneYear greaterThanOneYear) {
        if (debtorsApi.getDebtorsPreviousPeriod() != null) {

            tradeDebtors.setPreviousTradeDebtors(debtorsApi.getDebtorsPreviousPeriod().getTradeDebtors());
            prepaymentsAndAccruedIncome.setPreviousPrepaymentsAndAccruedIncome(debtorsApi.getDebtorsPreviousPeriod().getPrepaymentsAndAccruedIncome());
            otherDebtors.setPreviousOtherDebtors(debtorsApi.getDebtorsPreviousPeriod().getOtherDebtors());
            total.setPreviousTotal(debtorsApi.getDebtorsPreviousPeriod().getTotal());
            greaterThanOneYear.setPreviousGreaterThanOneYear(debtorsApi.getDebtorsPreviousPeriod().getGreaterThanOneYear());
        }
    }

    private void getDebtorsCurrentPeriodForWeb(DebtorsApi debtorsApi, Debtors debtors,
            TradeDebtors tradeDebtors, PrepaymentsAndAccruedIncome prepaymentsAndAccruedIncome,
            OtherDebtors otherDebtors, Total total, GreaterThanOneYear greaterThanOneYear) {
        if (debtorsApi.getDebtorsCurrentPeriod() != null) {
            debtors.setDetails(debtorsApi.getDebtorsCurrentPeriod().getDetails());

            tradeDebtors.setCurrentTradeDebtors(debtorsApi.getDebtorsCurrentPeriod().getTradeDebtors());
            prepaymentsAndAccruedIncome.setCurrentPrepaymentsAndAccruedIncome(debtorsApi.getDebtorsCurrentPeriod().getPrepaymentsAndAccruedIncome());
            otherDebtors.setCurrentOtherDebtors(debtorsApi.getDebtorsCurrentPeriod().getOtherDebtors());
            total.setCurrentTotal(debtorsApi.getDebtorsCurrentPeriod().getTotal());
            greaterThanOneYear.setCurrentGreaterThanOneYear(debtorsApi.getDebtorsCurrentPeriod().getGreaterThanOneYear());
        }
    }

    @Override
    public DebtorsApi getDebtorsApi(Debtors debtors) {

        DebtorsApi debtorsApi = new DebtorsApi();
        if (debtors.getTotal().getCurrentTotal() != null) {

            setCurrentPeriodDebtorsOnApiModel(debtors, debtorsApi);
        }
        
        if (debtors.getTotal().getPreviousTotal() != null) {

            setPreviousPeriodDebtorsOnApiModel(debtors, debtorsApi);
        }
        return debtorsApi;

    }

    private void setPreviousPeriodDebtorsOnApiModel(Debtors debtors, DebtorsApi debtorsApi) {
        PreviousPeriod previousPeriod = new PreviousPeriod();

        if (debtors.getTradeDebtors() != null && debtors.getTradeDebtors().getPreviousTradeDebtors() != null) {
            previousPeriod.setTradeDebtors(debtors.getTradeDebtors().getPreviousTradeDebtors());
        }

        if (debtors.getPrepaymentsAndAccruedIncome() != null && debtors.getPrepaymentsAndAccruedIncome().getPreviousPrepaymentsAndAccruedIncome() != null) {
            previousPeriod.setPrepaymentsAndAccruedIncome(debtors.getPrepaymentsAndAccruedIncome().getPreviousPrepaymentsAndAccruedIncome());
        }

        if (debtors.getOtherDebtors() != null && debtors.getOtherDebtors().getPreviousOtherDebtors() != null) {
            previousPeriod.setOtherDebtors(debtors.getOtherDebtors().getPreviousOtherDebtors());
        }

        if (debtors.getTotal() != null && debtors.getTotal().getPreviousTotal() != null) {
            previousPeriod.setTotal(debtors.getTotal().getPreviousTotal());
        }

        if (debtors.getGreaterThanOneYear() != null && debtors.getGreaterThanOneYear().getPreviousGreaterThanOneYear() != null) {
            previousPeriod.setGreaterThanOneYear(debtors.getGreaterThanOneYear().getPreviousGreaterThanOneYear());
        }
        debtorsApi.setDebtorsPreviousPeriod(previousPeriod);
    }

    private void setCurrentPeriodDebtorsOnApiModel(Debtors debtors, DebtorsApi debtorsApi) {
        CurrentPeriod currentPeriod = new CurrentPeriod();

        if (debtors.getDetails() != null && debtors.getDetails().equals("")) {
            currentPeriod.setDetails(null);
        } else {
            currentPeriod.setDetails(debtors.getDetails());
        }

        if (debtors.getTradeDebtors() != null && debtors.getTradeDebtors().getCurrentTradeDebtors() != null) {
            currentPeriod.setTradeDebtors(debtors.getTradeDebtors().getCurrentTradeDebtors());
        }

        if (debtors.getPrepaymentsAndAccruedIncome() != null && debtors.getPrepaymentsAndAccruedIncome().getCurrentPrepaymentsAndAccruedIncome() != null) {
            currentPeriod.setPrepaymentsAndAccruedIncome(debtors.getPrepaymentsAndAccruedIncome().getCurrentPrepaymentsAndAccruedIncome());
        }

        if (debtors.getOtherDebtors() != null && debtors.getOtherDebtors().getCurrentOtherDebtors() != null) {
            currentPeriod.setOtherDebtors(debtors.getOtherDebtors().getCurrentOtherDebtors());
        }

        if (debtors.getTotal() != null && debtors.getTotal().getCurrentTotal() != null) {
            currentPeriod.setTotal(debtors.getTotal().getCurrentTotal());
        }

        if (debtors.getGreaterThanOneYear() != null && debtors.getGreaterThanOneYear().getCurrentGreaterThanOneYear() != null) {
            currentPeriod.setGreaterThanOneYear(debtors.getGreaterThanOneYear().getCurrentGreaterThanOneYear());
        }

        debtorsApi.setDebtorsCurrentPeriod(currentPeriod);
    }
}
