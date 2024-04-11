package uk.gov.companieshouse.web.accounts.model.directorsreport;

public class DirectorsReportReview {
    private Director[] directors;

    private String secretary;

    private AdditionalInformation additionalInformation;

    private CompanyPolicyOnDisabledEmployees companyPolicyOnDisabledEmployees;

    private PoliticalAndCharitableDonations politicalAndCharitableDonations;

    private PrincipalActivities principalActivities;

    public Director[] getDirectors() {
        return directors;
    }

    public void setDirectors(Director[] directors) {
        this.directors = directors;
    }

    public String getSecretary() {
        return secretary;
    }

    public void setSecretary(String secretary) {
        this.secretary = secretary;
    }

    public AdditionalInformation getAdditionalInformation() {
        return additionalInformation;
    }

    public void setAdditionalInformation(
            AdditionalInformation additionalInformation) {
        this.additionalInformation = additionalInformation;
    }

    public CompanyPolicyOnDisabledEmployees getCompanyPolicyOnDisabledEmployees() {
        return companyPolicyOnDisabledEmployees;
    }

    public void setCompanyPolicyOnDisabledEmployees(
            CompanyPolicyOnDisabledEmployees companyPolicyOnDisabledEmployees) {
        this.companyPolicyOnDisabledEmployees = companyPolicyOnDisabledEmployees;
    }

    public PoliticalAndCharitableDonations getPoliticalAndCharitableDonations() {
        return politicalAndCharitableDonations;
    }

    public void setPoliticalAndCharitableDonations(
            PoliticalAndCharitableDonations politicalAndCharitableDonations) {
        this.politicalAndCharitableDonations = politicalAndCharitableDonations;
    }

    public PrincipalActivities getPrincipalActivities() {
        return principalActivities;
    }

    public void setPrincipalActivities(
            PrincipalActivities principalActivities) {
        this.principalActivities = principalActivities;
    }
}
