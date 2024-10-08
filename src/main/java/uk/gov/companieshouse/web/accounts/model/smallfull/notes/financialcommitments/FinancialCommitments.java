package uk.gov.companieshouse.web.accounts.model.smallfull.notes.financialcommitments;

import jakarta.validation.constraints.NotBlank;
import uk.gov.companieshouse.web.accounts.model.Note;
import uk.gov.companieshouse.web.accounts.validation.ValidationMapping;
import uk.gov.companieshouse.web.accounts.validation.ValidationModel;

@ValidationModel
public class FinancialCommitments implements Note {

    @NotBlank(message = "{financialCommitments.details.missing}")
    @ValidationMapping("$.financial_commitments.details")
    private String financialCommitmentsDetails;

    public String getFinancialCommitmentsDetails() {
        return financialCommitmentsDetails;
    }

    public void setFinancialCommitmentsDetails(String financialCommitmentsDetails) {
        this.financialCommitmentsDetails = financialCommitmentsDetails;
    }
}
