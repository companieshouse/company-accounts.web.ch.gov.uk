package uk.gov.companieshouse.web.accounts.transformer.smallfull;

import uk.gov.companieshouse.api.model.accounts.smallfull.AccountingPoliciesApi;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.accountingpolicies.BasisOfPreparation;

public interface AccountingPoliciesTransformer {

    BasisOfPreparation getBasisOfPreparation(AccountingPoliciesApi accountingPoliciesApi);

    void setBasisOfPreparation(BasisOfPreparation basisOfPreparation,
            AccountingPoliciesApi accountingPoliciesApi);

}
