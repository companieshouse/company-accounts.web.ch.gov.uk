package uk.gov.companieshouse.web.accounts.transformer.smallfull.impl;

import org.springframework.stereotype.Component;
import uk.gov.companieshouse.api.model.accounts.smallfull.AccountingPoliciesApi;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.accountingpolicies.BasisOfPreparation;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.AccountingPoliciesTransformer;

@Component
public class AccountingPoliciesTransformerImpl implements AccountingPoliciesTransformer {

    /**
     * {@inheritDoc}
     */
    @Override
    public BasisOfPreparation getBasisOfPreparation(AccountingPoliciesApi accountingPoliciesApi) {

        BasisOfPreparation basisOfPreparation = new BasisOfPreparation();
        if (accountingPoliciesApi == null) {
            return basisOfPreparation;
        }

        if (accountingPoliciesApi.getBasisOfMeasurementAndPreparation()
                .equalsIgnoreCase(basisOfPreparation.getPreparedStatement())) {

            basisOfPreparation.setIsPreparedInAccordanceWithStandards(true);
        } else {

            basisOfPreparation.setIsPreparedInAccordanceWithStandards(false);
            basisOfPreparation.setCustomStatement(accountingPoliciesApi.getBasisOfMeasurementAndPreparation());
        }

        return basisOfPreparation;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setBasisOfPreparation(BasisOfPreparation basisOfPreparation,
            AccountingPoliciesApi accountingPoliciesApi) {

        if (basisOfPreparation.getIsPreparedInAccordanceWithStandards()) {
            accountingPoliciesApi.setBasisOfMeasurementAndPreparation(basisOfPreparation.getPreparedStatement());
        } else {
            accountingPoliciesApi.setBasisOfMeasurementAndPreparation(basisOfPreparation.getCustomStatement());
        }
    }
}
