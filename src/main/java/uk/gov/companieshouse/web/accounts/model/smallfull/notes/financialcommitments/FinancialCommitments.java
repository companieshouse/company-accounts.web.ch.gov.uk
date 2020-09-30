package uk.gov.companieshouse.web.accounts.model.smallfull.notes.financialcommitments;

import uk.gov.companieshouse.web.accounts.model.Note;

public class FinancialCommitments implements Note {

    private String financialCommitmentsDetails;

    public String getFinancialCommitmentsDetails() {
        return financialCommitmentsDetails;
    }

    public void setFinancialCommitmentsDetails(String financialCommitmentsDetails) {
        this.financialCommitmentsDetails = financialCommitmentsDetails;
    }
}
