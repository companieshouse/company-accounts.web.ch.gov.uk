package uk.gov.companieshouse.web.accounts.transformer.smallfull.impl;

import org.springframework.stereotype.Component;
import uk.gov.companieshouse.api.model.accounts.smallfull.Debtors.CurrentPeriod;
import uk.gov.companieshouse.api.model.accounts.smallfull.Debtors.DebtorsApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.Debtors.PreviousPeriod;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.debtors.Debtors;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.debtors.GreaterThanOneYear;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.debtors.OtherDebtors;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.debtors.PrepaymentsAndAccruedIncome;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.debtors.Total;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.debtors.TradeDebtors;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.DebtorsTransformer;

@Component
public class DebtorsTransformerImpl implements DebtorsTransformer {

    @Override
    public Debtors getDebtors(DebtorsApi debtorsApi) {

        Debtors debtors = new Debtors();

        TradeDebtors tradeDebtors = new TradeDebtors();
        PrepaymentsAndAccruedIncome prepaymentsAndAccruedIncome = new PrepaymentsAndAccruedIncome();
        OtherDebtors otherDebtors = new OtherDebtors();
        Total total = new Total();
        GreaterThanOneYear greaterThanOneYear = new GreaterThanOneYear();


        if (debtorsApi != null && debtorsApi.getDebtorsCurrentPeriod() != null) {
            debtors.setDetails(debtorsApi.getDebtorsCurrentPeriod().getDetails());

            tradeDebtors.setCurrentTradeDebtors(debtorsApi.getDebtorsCurrentPeriod().getTradeDebtors());
            prepaymentsAndAccruedIncome.setCurrentPrepaymentsAndAccruedIncome(debtorsApi.getDebtorsCurrentPeriod().getPrepaymentsAndAccruedIncome());
            otherDebtors.setCurrentOtherDebtors(debtorsApi.getDebtorsCurrentPeriod().getOtherDebtors());
            total.setCurrentTotal(debtorsApi.getDebtorsCurrentPeriod().getTotal());
            greaterThanOneYear.setCurrentGreaterThanOneYear(debtorsApi.getDebtorsCurrentPeriod().getGreaterThanOneYear());
        }

        if (debtorsApi != null && debtorsApi.getDebtorsPreviousPeriod() != null) {

            tradeDebtors.setPreviousTradeDebtors(debtorsApi.getDebtorsPreviousPeriod().getTradeDebtors());
            prepaymentsAndAccruedIncome.setPreviousPrepaymentsAndAccruedIncome(debtorsApi.getDebtorsPreviousPeriod().getPrepaymentsAndAccruedIncome());
            otherDebtors.setPreviousOtherDebtors(debtorsApi.getDebtorsPreviousPeriod().getOtherDebtors());
            total.setPreviousTotal(debtorsApi.getDebtorsPreviousPeriod().getTotal());
            greaterThanOneYear.setPreviousGreaterThanOneYear(debtorsApi.getDebtorsPreviousPeriod().getGreaterThanOneYear());
        }

        debtors.setGreaterThanOneYear(greaterThanOneYear);
        debtors.setTradeDebtors(tradeDebtors);
        debtors.setPrepaymentsAndAccruedIncome(prepaymentsAndAccruedIncome);
        debtors.setOtherDebtors(otherDebtors);
        debtors.setTotal(total);

        return debtors;
    }

    @Override
    public void setDebtors(Debtors debtors, DebtorsApi debtorsApi) {

        if (debtorsApi == null) {
            debtorsApi = new DebtorsApi();
        }

        CurrentPeriod currentPeriod = new CurrentPeriod();

        if (debtors.getDetails().equals("")) {
            currentPeriod.setDetails(null);
        } else {
            currentPeriod.setDetails(debtors.getDetails());
        }

        currentPeriod.setTradeDebtors(debtors.getTradeDebtors().getCurrentTradeDebtors());
        currentPeriod.setPrepaymentsAndAccruedIncome(debtors.getPrepaymentsAndAccruedIncome().getCurrentPrepaymentsAndAccruedIncome());
        currentPeriod.setOtherDebtors(debtors.getOtherDebtors().getCurrentOtherDebtors());
        currentPeriod.setTotal(debtors.getTotal().getCurrentTotal());
        currentPeriod.setGreaterThanOneYear(debtors.getGreaterThanOneYear().getCurrentGreaterThanOneYear());

        debtorsApi.setDebtorsCurrentPeriod(currentPeriod);

        PreviousPeriod previousPeriod = new PreviousPeriod();

        previousPeriod.setTradeDebtors(debtors.getTradeDebtors().getPreviousTradeDebtors());
        previousPeriod.setPrepaymentsAndAccruedIncome(debtors.getPrepaymentsAndAccruedIncome().getPreviousPrepaymentsAndAccruedIncome());
        previousPeriod.setOtherDebtors(debtors.getOtherDebtors().getPreviousOtherDebtors());
        previousPeriod.setTotal(debtors.getTotal().getPreviousTotal());
        previousPeriod.setGreaterThanOneYear(debtors.getGreaterThanOneYear().getPreviousGreaterThanOneYear());

        debtorsApi.setDebtorsPreviousPeriod(previousPeriod);
    }
}
