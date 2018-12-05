package uk.gov.companieshouse.web.accounts.model.smallfull.notes.accountingpolicies;

import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import uk.gov.companieshouse.web.accounts.validation.ValidationMapping;
import uk.gov.companieshouse.web.accounts.validation.ValidationModel;

@Getter
@Setter
@ValidationModel
public class BasisOfPreparation {

    @NotNull(message = "{basisOfPreparation.selectionNotMade}")
    private Boolean isPreparedInAccordanceWithStandards;

    @ValidationMapping("$.accounting_policies.basis_of_measurement_and_preparation")
    private String customStatement;

    private String preparedStatement = "These financial statements have been prepared in accordance with section 396 of the Companies Act";
}
