package uk.gov.companieshouse.web.accounts.transformer.smallfull;

import uk.gov.companieshouse.api.model.accounts.smallfull.Debtors.DebtorsApi;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.debtors.Debtors;

public interface DebtorsTransformer {

    Debtors getDebtors(DebtorsApi debtorsApi);

    void setDebtors(Debtors debtors, DebtorsApi debtorsApi);
}
