package uk.gov.companieshouse.web.accounts.transformer.smallfull.impl;

import org.springframework.stereotype.Component;
import uk.gov.companieshouse.api.model.accounts.smallfull.Debtors.CurrentPeriod;
import uk.gov.companieshouse.api.model.accounts.smallfull.Debtors.DebtorsApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.Debtors.PreviousPeriod;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.debtors.Debtors;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.DebtorsTransformer;

@Component
public class DebtorsTransformerImpl implements DebtorsTransformer {

    @Override
    public Debtors getDebtors(DebtorsApi debtorsApi) {

        Debtors debtors = new Debtors();

        if (debtorsApi != null && debtorsApi.getDebtorsCurrentPeriod() != null) {
            debtors.setDetails(debtorsApi.getDebtorsCurrentPeriod().getDetails());
            debtors.getTradeDebtors().setCurrentTradeDebtors(debtorsApi.getDebtorsCurrentPeriod().getTradeDebtors());
            debtors.getPrepaymentsAndAccruedIncome().setCurrentPrepaymentsAndAccruedIncome(debtorsApi.getDebtorsCurrentPeriod().getPrepaymentsAndAccruedIncome());
            debtors.getOtherDebtors().setCurrentOtherDebtors(debtorsApi.getDebtorsCurrentPeriod().getOtherDebtors());
            debtors.getTotal().setCurrentTotal(debtorsApi.getDebtorsCurrentPeriod().getTotal());
            debtors.getGreaterThanOneYear().setCurrentGreaterThanOneYear(debtorsApi.getDebtorsCurrentPeriod().getGreaterThanOneYear());
        }

        if (debtorsApi != null && debtorsApi.getDebtorsPreviousPeriod() != null) {
            debtors.getTradeDebtors().setPreviousTradeDebtors(debtorsApi.getDebtorsPreviousPeriod().getTradeDebtors());
            debtors.getPrepaymentsAndAccruedIncome().setPreviousPrepaymentsAndAccruedIncome(debtorsApi.getDebtorsPreviousPeriod().getPrepaymentsAndAccruedIncome());
            debtors.getOtherDebtors().setPreviousOtherDebtors(debtorsApi.getDebtorsPreviousPeriod().getOtherDebtors());
            debtors.getTotal().setPreviousTotal(debtorsApi.getDebtorsPreviousPeriod().getTotal());
            debtors.getGreaterThanOneYear().setPreviousGreaterThanOneYear(debtorsApi.getDebtorsPreviousPeriod().getGreaterThanOneYear());
        }

        return debtors;
    }

    @Override
    public void setDebtors(Debtors debtors, DebtorsApi debtorsApi) {

        if (debtorsApi == null) {
            debtorsApi = new DebtorsApi();
        }

        CurrentPeriod currentPeriod = new CurrentPeriod();

        currentPeriod.setDetails(debtors.getDetails());
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
