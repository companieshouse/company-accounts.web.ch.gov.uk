package uk.gov.companieshouse.web.accounts.transformer.smallfull.impl;

import org.springframework.stereotype.Component;
import uk.gov.companieshouse.api.model.accounts.smallfull.Debtors.DebtorsApi;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.debtors.CurrentPeriod;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.debtors.Debtors;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.debtors.PreviousPeriod;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.DebtorsTransformer;

@Component
public class DebtorsTransformerImpl implements DebtorsTransformer {

    @Override
    public Debtors getDebtors(DebtorsApi debtorsApi) {

        Debtors debtors = new Debtors();

        if (debtorsApi != null) {
            CurrentPeriod currentPeriod = new CurrentPeriod();
            currentPeriod.setDetails(debtorsApi.getDebtorsCurrentPeriod().getDetails());
            currentPeriod.setTradeDebtors(debtorsApi.getDebtorsCurrentPeriod().getTradeDebtors());
            currentPeriod.setPrepaymentsAndAccruedIncome(debtorsApi.getDebtorsCurrentPeriod().getPrepaymentsAndAccruedIncome());
            currentPeriod.setOtherDebtors(debtorsApi.getDebtorsCurrentPeriod().getOtherDebtors());
            currentPeriod.setTotal(debtorsApi.getDebtorsCurrentPeriod().getTotal());

            debtors.setCurrentPeriod(currentPeriod);

            PreviousPeriod previousPeriod = new PreviousPeriod();

            previousPeriod.setTradeDebtors(debtorsApi.getDebtorsPreviousPeriod().getTradeDebtors());
            previousPeriod.setPrepaymentsAndAccruedIncome(debtorsApi.getDebtorsPreviousPeriod().getPrepaymentsAndAccruedIncome());
            previousPeriod.setOtherDebtors(debtorsApi.getDebtorsPreviousPeriod().getOtherDebtors());
            previousPeriod.setTotal(debtorsApi.getDebtorsPreviousPeriod().getTotal());

            debtors.setPreviousPeriod(previousPeriod);
        }

        return debtors;
    }
}
