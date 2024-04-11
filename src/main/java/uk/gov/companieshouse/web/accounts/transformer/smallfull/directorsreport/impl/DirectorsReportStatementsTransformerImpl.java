package uk.gov.companieshouse.web.accounts.transformer.smallfull.directorsreport.impl;

import org.springframework.stereotype.Component;
import uk.gov.companieshouse.api.model.accounts.directorsreport.StatementsApi;
import uk.gov.companieshouse.web.accounts.model.directorsreport.AdditionalInformation;
import uk.gov.companieshouse.web.accounts.model.directorsreport.CompanyPolicyOnDisabledEmployees;
import uk.gov.companieshouse.web.accounts.model.directorsreport.PoliticalAndCharitableDonations;
import uk.gov.companieshouse.web.accounts.model.directorsreport.PrincipalActivities;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.directorsreport.DirectorsReportStatementsTransformer;

@Component
public class DirectorsReportStatementsTransformerImpl implements DirectorsReportStatementsTransformer {
    @Override
    public AdditionalInformation getAdditionalInformation(StatementsApi statementsApi) {
        AdditionalInformation additionalInformation = new AdditionalInformation();
        if (statementsApi != null) {
            additionalInformation.setAdditionalInformationDetails(
                    statementsApi.getAdditionalInformation());
        }
        return additionalInformation;
    }

    @Override
    public void setAdditionalInformation(StatementsApi statementsApi,
            AdditionalInformation additionalInformation) {
        statementsApi.setAdditionalInformation(
                additionalInformation.getAdditionalInformationDetails());
    }

    @Override
    public CompanyPolicyOnDisabledEmployees getCompanyPolicyOnDisabledEmployees(
            StatementsApi statementsApi) {
        CompanyPolicyOnDisabledEmployees companyPolicyOnDisabledEmployees = new CompanyPolicyOnDisabledEmployees();
        if (statementsApi != null) {
            companyPolicyOnDisabledEmployees.setCompanyPolicyOnDisabledEmployeesDetails(
                    statementsApi.getCompanyPolicyOnDisabledEmployees());
        }
        return companyPolicyOnDisabledEmployees;
    }

    @Override
    public void setCompanyPolicyOnDisabledEmployees(StatementsApi statementsApi,
            CompanyPolicyOnDisabledEmployees companyPolicyOnDisabledEmployees) {
        statementsApi.setCompanyPolicyOnDisabledEmployees(
                companyPolicyOnDisabledEmployees.getCompanyPolicyOnDisabledEmployeesDetails());
    }

    @Override
    public PoliticalAndCharitableDonations getPoliticalAndCharitableDonations(
            StatementsApi statementsApi) {
        PoliticalAndCharitableDonations politicalAndCharitableDonations = new PoliticalAndCharitableDonations();
        if (statementsApi != null) {
            politicalAndCharitableDonations.setPoliticalAndCharitableDonationsDetails(
                    statementsApi.getPoliticalAndCharitableDonations());
        }
        return politicalAndCharitableDonations;
    }

    @Override
    public void setPoliticalAndCharitableDonations(StatementsApi statementsApi,
            PoliticalAndCharitableDonations politicalAndCharitableDonations) {
        statementsApi.setPoliticalAndCharitableDonations(
                politicalAndCharitableDonations.getPoliticalAndCharitableDonationsDetails());
    }

    @Override
    public PrincipalActivities getPrincipalActivities(StatementsApi statementsApi) {
        PrincipalActivities principalActivities = new PrincipalActivities();
        if (statementsApi != null) {
            principalActivities.setPrincipalActivitiesDetails(
                    statementsApi.getPrincipalActivities());
        }
        return principalActivities;
    }

    @Override
    public void setPrincipalActivities(StatementsApi statementsApi,
            PrincipalActivities principalActivities) {
        statementsApi.setPrincipalActivities(
                principalActivities.getPrincipalActivitiesDetails());
    }
}
