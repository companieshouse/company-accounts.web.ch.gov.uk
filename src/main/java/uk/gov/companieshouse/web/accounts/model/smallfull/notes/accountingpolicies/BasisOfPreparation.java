package uk.gov.companieshouse.web.accounts.model.smallfull.notes.accountingpolicies;

import javax.validation.constraints.NotNull;
import uk.gov.companieshouse.web.accounts.validation.ValidationMapping;
import uk.gov.companieshouse.web.accounts.validation.ValidationModel;

@ValidationModel
public class BasisOfPreparation {

    @NotNull(message = "{basisOfPreparation.selectionNotMade}")
    private Boolean isPreparedInAccordanceWithStandards;

    @ValidationMapping("$.accounting_policies.basis_of_measurement_and_preparation")
    private String customStatement;

    private String preparedStatement =
            "These financial statements have been prepared in accordance with the provisions of "
                    + "Section 1A (Small Entities) of Financial Reporting Standard 102";

    public Boolean getIsPreparedInAccordanceWithStandards() {
        return isPreparedInAccordanceWithStandards;
    }

    public void setIsPreparedInAccordanceWithStandards(Boolean preparedInAccordanceWithStandards) {
        isPreparedInAccordanceWithStandards = preparedInAccordanceWithStandards;
    }

    public String getCustomStatement() {
        return customStatement;
    }

    public void setCustomStatement(String customStatement) {
        this.customStatement = customStatement;
    }

    public String getPreparedStatement() {
        return preparedStatement;
    }

    public void setPreparedStatement(String preparedStatement) {
        this.preparedStatement = preparedStatement;
    }
}
