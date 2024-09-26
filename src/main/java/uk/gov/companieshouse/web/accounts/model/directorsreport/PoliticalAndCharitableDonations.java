package uk.gov.companieshouse.web.accounts.model.directorsreport;

import jakarta.validation.constraints.NotBlank;
import uk.gov.companieshouse.web.accounts.validation.ValidationMapping;
import uk.gov.companieshouse.web.accounts.validation.ValidationModel;

@ValidationModel
public class PoliticalAndCharitableDonations {

    @NotBlank(message = "{directorsReport.politicalAndCharitableDonations.details.missing}")
    @ValidationMapping("$.statements.political_and_charitable_donations")
    private String politicalAndCharitableDonationsDetails;

    public String getPoliticalAndCharitableDonationsDetails() {
        return politicalAndCharitableDonationsDetails;
    }

    public void setPoliticalAndCharitableDonationsDetails(
            String politicalAndCharitableDonationsDetails) {
        this.politicalAndCharitableDonationsDetails = politicalAndCharitableDonationsDetails;
    }
}
