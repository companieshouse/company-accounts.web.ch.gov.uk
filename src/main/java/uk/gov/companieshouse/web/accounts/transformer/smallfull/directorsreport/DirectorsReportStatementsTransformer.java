package uk.gov.companieshouse.web.accounts.transformer.smallfull.directorsreport;

import uk.gov.companieshouse.api.model.accounts.directorsreport.StatementsApi;
import uk.gov.companieshouse.web.accounts.model.directorsreport.AdditionalInformation;
import uk.gov.companieshouse.web.accounts.model.directorsreport.CompanyPolicyOnDisabledEmployees;
import uk.gov.companieshouse.web.accounts.model.directorsreport.PoliticalAndCharitableDonations;
import uk.gov.companieshouse.web.accounts.model.directorsreport.PrincipalActivities;

public interface DirectorsReportStatementsTransformer {

    AdditionalInformation getAdditionalInformation(StatementsApi statementsApi);

    void setAdditionalInformation(StatementsApi statementsApi, AdditionalInformation additionalInformation);

    CompanyPolicyOnDisabledEmployees getCompanyPolicyOnDisabledEmployees(StatementsApi statementsApi);

    void setCompanyPolicyOnDisabledEmployees(StatementsApi statementsApi, CompanyPolicyOnDisabledEmployees companyPolicyOnDisabledEmployees);

    PoliticalAndCharitableDonations getPoliticalAndCharitableDonations(StatementsApi statementsApi);

    void setPoliticalAndCharitableDonations(StatementsApi statementsApi, PoliticalAndCharitableDonations politicalAndCharitableDonations);

    PrincipalActivities getPrincipalActivities(StatementsApi statementsApi);

    void setPrincipalActivities(StatementsApi statementsApi, PrincipalActivities principalActivities);
}
