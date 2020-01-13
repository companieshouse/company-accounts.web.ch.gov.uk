package uk.gov.companieshouse.web.accounts.model.directorsreport;

import javax.validation.constraints.NotNull;

public class PoliticalAndCharitableDonationsSelection {

    @NotNull(message = "directorsReport.politicalAndCharitableDonations.selectionNotMade")
    private Boolean hasPoliticalAndCharitableDonations;

    public Boolean getHasPoliticalAndCharitableDonations() {
        return hasPoliticalAndCharitableDonations;
    }

    public void setHasPoliticalAndCharitableDonations(
            Boolean hasPoliticalAndCharitableDonations) {
        this.hasPoliticalAndCharitableDonations = hasPoliticalAndCharitableDonations;
    }
}
