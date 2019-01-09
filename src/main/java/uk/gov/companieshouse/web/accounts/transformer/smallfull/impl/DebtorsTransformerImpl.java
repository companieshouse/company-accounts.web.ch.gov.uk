package uk.gov.companieshouse.web.accounts.transformer.smallfull.impl;

import org.springframework.stereotype.Component;
import uk.gov.companieshouse.api.model.accounts.smallfull.Debtors.CurrentPeriod;
import uk.gov.companieshouse.api.model.accounts.smallfull.Debtors.DebtorsApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.Debtors.PreviousPeriod;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.Debtors;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.DebtorsTransformer;

@Component
public class DebtorsTransformerImpl implements DebtorsTransformer {

    @Override
    public Debtors getDebtors(DebtorsApi debtorsApi) {

        Debtors debtors = new Debtors();

        if (debtorsApi != null && debtorsApi.getDebtorsCurrentPeriod() != null) {
            debtors.setDetails(debtorsApi.getDebtorsCurrentPeriod().getDetails());
            debtors.setCurrentTradeDebtors(debtorsApi.getDebtorsCurrentPeriod().getTradeDebtors());
            debtors.setCurrentPrepaymentsAndAccruedIncome(debtorsApi.getDebtorsCurrentPeriod().getPrepaymentsAndAccruedIncome());
            debtors.setCurrentOtherDebtors(debtorsApi.getDebtorsCurrentPeriod().getOtherDebtors());
            debtors.setCurrentTotal(debtorsApi.getDebtorsCurrentPeriod().getTotal());
            debtors.setCurrentGreaterThanOneYear(debtorsApi.getDebtorsCurrentPeriod().getGreaterThanOneYear());
        }

        if (debtorsApi != null && debtorsApi.getDebtorsPreviousPeriod() != null) {
            debtors.setPreviousTradeDebtors(debtorsApi.getDebtorsPreviousPeriod().getTradeDebtors());
            debtors.setPreviousPrepaymentsAndAccruedIncome(debtorsApi.getDebtorsPreviousPeriod().getPrepaymentsAndAccruedIncome());
            debtors.setPreviousOtherDebtors(debtorsApi.getDebtorsPreviousPeriod().getOtherDebtors());
            debtors.setPreviousTotal(debtorsApi.getDebtorsPreviousPeriod().getTotal());
            debtors.setPreviousGreaterThanOneYear(debtorsApi.getDebtorsPreviousPeriod().getGreaterThanOneYear());
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
        currentPeriod.setTradeDebtors(debtors.getCurrentTradeDebtors());
        currentPeriod.setPrepaymentsAndAccruedIncome(debtors.getCurrentPrepaymentsAndAccruedIncome());
        currentPeriod.setOtherDebtors(debtors.getCurrentOtherDebtors());
        currentPeriod.setTotal(debtors.getCurrentTotal());
        currentPeriod.setGreaterThanOneYear(debtors.getCurrentGreaterThanOneYear());

        debtorsApi.setDebtorsCurrentPeriod(currentPeriod);

        PreviousPeriod previousPeriod = new PreviousPeriod();

        previousPeriod.setTradeDebtors(debtors.getPreviousTradeDebtors());
        previousPeriod.setPrepaymentsAndAccruedIncome(debtors.getPreviousPrepaymentsAndAccruedIncome());
        previousPeriod.setOtherDebtors(debtors.getPreviousOtherDebtors());
        previousPeriod.setTotal(debtors.getPreviousTotal());
        previousPeriod.setGreaterThanOneYear(debtors.getPreviousGreaterThanOneYear());

        debtorsApi.setDebtorsPreviousPeriod(previousPeriod);
    }
}
