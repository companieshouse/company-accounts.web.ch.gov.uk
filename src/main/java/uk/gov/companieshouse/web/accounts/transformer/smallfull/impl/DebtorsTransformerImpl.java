package uk.gov.companieshouse.web.accounts.transformer.smallfull.impl;

import org.springframework.stereotype.Component;
import uk.gov.companieshouse.api.model.accounts.smallfull.Debtors.DebtorsApi;
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
        }

        if (debtorsApi != null && debtorsApi.getDebtorsPreviousPeriod() != null) {
            debtors.setCurrentTradeDebtors(debtorsApi.getDebtorsPreviousPeriod().getTradeDebtors());
            debtors.setCurrentPrepaymentsAndAccruedIncome(debtorsApi.getDebtorsPreviousPeriod().getPrepaymentsAndAccruedIncome());
            debtors.setCurrentOtherDebtors(debtorsApi.getDebtorsPreviousPeriod().getOtherDebtors());
            debtors.setCurrentTotal(debtorsApi.getDebtorsPreviousPeriod().getTotal());
        }

        return debtors;
    }
}
