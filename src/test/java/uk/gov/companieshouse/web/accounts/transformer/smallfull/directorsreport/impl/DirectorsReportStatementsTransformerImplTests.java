package uk.gov.companieshouse.web.accounts.transformer.smallfull.directorsreport.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.api.model.accounts.directorsreport.StatementsApi;
import uk.gov.companieshouse.web.accounts.model.directorsreport.AdditionalInformation;
import uk.gov.companieshouse.web.accounts.model.directorsreport.CompanyPolicyOnDisabledEmployees;
import uk.gov.companieshouse.web.accounts.model.directorsreport.PoliticalAndCharitableDonations;
import uk.gov.companieshouse.web.accounts.model.directorsreport.PrincipalActivities;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.directorsreport.DirectorsReportStatementsTransformer;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DirectorsReportStatementsTransformerImplTests {
    private DirectorsReportStatementsTransformer statementsTransformer = new DirectorsReportStatementsTransformerImpl();

    private static final String ADDITIONAL_INFORMATION = "additionalInformation";
    private static final String COMPANY_POLICY_ON_DISABLED_EMPLOYEES = "companyPolicyOnDisabledEmployees";
    private static final String POLITICAL_AND_CHARITABLE_DONATIONS = "politicalAndCharitableDonations";
    private static final String PRINCIPAL_ACTIVITIES = "principalActivities";

    @Test
    @DisplayName("Get additional information - null statements")
    void getAdditionalInformationNullStatements() {
        AdditionalInformation additionalInformation =
                statementsTransformer.getAdditionalInformation(null);

        assertNotNull(additionalInformation);
        assertNull(additionalInformation.getAdditionalInformationDetails());
    }

    @Test
    @DisplayName("Get additional information - provided statements")
    void getAdditionalInformationProvidedStatements() {
        StatementsApi statementsApi = new StatementsApi();
        statementsApi.setAdditionalInformation(ADDITIONAL_INFORMATION);

        AdditionalInformation additionalInformation =
                statementsTransformer.getAdditionalInformation(statementsApi);

        assertNotNull(additionalInformation);
        assertEquals(ADDITIONAL_INFORMATION, additionalInformation.getAdditionalInformationDetails());
    }

    @Test
    @DisplayName("Set additional information")
    void setAdditionalInformation() {
        AdditionalInformation additionalInformation = new AdditionalInformation();
        additionalInformation.setAdditionalInformationDetails(ADDITIONAL_INFORMATION);

        StatementsApi statementsApi = new StatementsApi();

        statementsTransformer.setAdditionalInformation(statementsApi, additionalInformation);

        assertEquals(ADDITIONAL_INFORMATION, statementsApi.getAdditionalInformation());
    }

    @Test
    @DisplayName("Get company policy on disabled employees - null statements")
    void getCompanyPolicyOnDisabledEmployeesNullStatements() {
        CompanyPolicyOnDisabledEmployees companyPolicyOnDisabledEmployees =
                statementsTransformer.getCompanyPolicyOnDisabledEmployees(null);

        assertNotNull(companyPolicyOnDisabledEmployees);
        assertNull(companyPolicyOnDisabledEmployees.getCompanyPolicyOnDisabledEmployeesDetails());
    }

    @Test
    @DisplayName("Get company policy on disabled employees - provided statements")
    void getCompanyPolicyOnDisabledEmployeesProvidedStatements() {
        StatementsApi statementsApi = new StatementsApi();
        statementsApi.setCompanyPolicyOnDisabledEmployees(COMPANY_POLICY_ON_DISABLED_EMPLOYEES);

        CompanyPolicyOnDisabledEmployees companyPolicyOnDisabledEmployees =
                statementsTransformer.getCompanyPolicyOnDisabledEmployees(statementsApi);

        assertNotNull(companyPolicyOnDisabledEmployees);
        assertEquals(COMPANY_POLICY_ON_DISABLED_EMPLOYEES, companyPolicyOnDisabledEmployees.getCompanyPolicyOnDisabledEmployeesDetails());
    }

    @Test
    @DisplayName("Set company policy on disabled employees")
    void setCompanyPolicyOnDisabledEmployees() {
        CompanyPolicyOnDisabledEmployees companyPolicyOnDisabledEmployees = new CompanyPolicyOnDisabledEmployees();
        companyPolicyOnDisabledEmployees.setCompanyPolicyOnDisabledEmployeesDetails(COMPANY_POLICY_ON_DISABLED_EMPLOYEES);

        StatementsApi statementsApi = new StatementsApi();

        statementsTransformer.setCompanyPolicyOnDisabledEmployees(statementsApi, companyPolicyOnDisabledEmployees);

        assertEquals(COMPANY_POLICY_ON_DISABLED_EMPLOYEES, statementsApi.getCompanyPolicyOnDisabledEmployees());
    }

    @Test
    @DisplayName("Get political and charitable donations - null statements")
    void getPoliticalAndCharitableDonationsNullStatements() {
        PoliticalAndCharitableDonations politicalAndCharitableDonations =
                statementsTransformer.getPoliticalAndCharitableDonations(null);

        assertNotNull(politicalAndCharitableDonations);
        assertNull(politicalAndCharitableDonations.getPoliticalAndCharitableDonationsDetails());
    }

    @Test
    @DisplayName("Get political and charitable donations - provided statements")
    void getPoliticalAndCharitableDonationsProvidedStatements() {
        StatementsApi statementsApi = new StatementsApi();
        statementsApi.setPoliticalAndCharitableDonations(POLITICAL_AND_CHARITABLE_DONATIONS);

        PoliticalAndCharitableDonations politicalAndCharitableDonations =
                statementsTransformer.getPoliticalAndCharitableDonations(statementsApi);

        assertNotNull(politicalAndCharitableDonations);
        assertEquals(POLITICAL_AND_CHARITABLE_DONATIONS, politicalAndCharitableDonations.getPoliticalAndCharitableDonationsDetails());
    }

    @Test
    @DisplayName("Set political and charitable donations")
    void setPoliticalAndCharitableDonations() {
        PoliticalAndCharitableDonations politicalAndCharitableDonations = new PoliticalAndCharitableDonations();
        politicalAndCharitableDonations.setPoliticalAndCharitableDonationsDetails(POLITICAL_AND_CHARITABLE_DONATIONS);

        StatementsApi statementsApi = new StatementsApi();

        statementsTransformer.setPoliticalAndCharitableDonations(statementsApi, politicalAndCharitableDonations);

        assertEquals(POLITICAL_AND_CHARITABLE_DONATIONS, statementsApi.getPoliticalAndCharitableDonations());
    }

    @Test
    @DisplayName("Get principal activities - null statements")
    void getPrincipalActivitiesNullStatements() {
        PrincipalActivities principalActivities =
                statementsTransformer.getPrincipalActivities(null);

        assertNotNull(principalActivities);
        assertNull(principalActivities.getPrincipalActivitiesDetails());
    }

    @Test
    @DisplayName("Get principal activities - provided statements")
    void getPrincipalActivitiesProvidedStatements() {
        StatementsApi statementsApi = new StatementsApi();
        statementsApi.setPrincipalActivities(PRINCIPAL_ACTIVITIES);

        PrincipalActivities principalActivities =
                statementsTransformer.getPrincipalActivities(statementsApi);

        assertNotNull(principalActivities);
        assertEquals(PRINCIPAL_ACTIVITIES, principalActivities.getPrincipalActivitiesDetails());
    }

    @Test
    @DisplayName("Set principal activities")
    void setPrincipalActivities() {
        PrincipalActivities principalActivities = new PrincipalActivities();
        principalActivities.setPrincipalActivitiesDetails(PRINCIPAL_ACTIVITIES);

        StatementsApi statementsApi = new StatementsApi();

        statementsTransformer.setPrincipalActivities(statementsApi, principalActivities);

        assertEquals(PRINCIPAL_ACTIVITIES, statementsApi.getPrincipalActivities());
    }
}
