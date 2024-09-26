package uk.gov.companieshouse.web.accounts.service.smallfull.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.directorsreport.AdditionalInformation;
import uk.gov.companieshouse.web.accounts.model.directorsreport.CompanyPolicyOnDisabledEmployees;
import uk.gov.companieshouse.web.accounts.model.directorsreport.Director;
import uk.gov.companieshouse.web.accounts.model.directorsreport.DirectorsReportReview;
import uk.gov.companieshouse.web.accounts.model.directorsreport.PoliticalAndCharitableDonations;
import uk.gov.companieshouse.web.accounts.model.directorsreport.PrincipalActivities;
import uk.gov.companieshouse.web.accounts.service.smallfull.AdditionalInformationService;
import uk.gov.companieshouse.web.accounts.service.smallfull.CompanyPolicyOnDisabledEmployeesService;
import uk.gov.companieshouse.web.accounts.service.smallfull.DirectorService;
import uk.gov.companieshouse.web.accounts.service.smallfull.DirectorsReportReviewService;
import uk.gov.companieshouse.web.accounts.service.smallfull.PoliticalAndCharitableDonationsService;
import uk.gov.companieshouse.web.accounts.service.smallfull.PrincipalActivitiesService;
import uk.gov.companieshouse.web.accounts.service.smallfull.SecretaryService;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DirectorsReportReviewServiceImplTest {

    @Mock
    private DirectorService directorService;

    @Mock
    private SecretaryService secretaryService;

    @Mock
    private AdditionalInformationService additionalInformationService;

    @Mock
    private CompanyPolicyOnDisabledEmployeesService companyPolicyOnDisabledEmployeesService;

    @Mock
    private PoliticalAndCharitableDonationsService politicalAndCharitableDonationsService;

    @Mock
    private PrincipalActivitiesService principalActivitiesService;

    @InjectMocks
    private DirectorsReportReviewService directorsReportReviewService = new DirectorsReportReviewServiceImpl();

    @Mock
    private AdditionalInformation additionalInformation;

    @Mock
    private CompanyPolicyOnDisabledEmployees companyPolicyOnDisabledEmployees;

    @Mock
    private PoliticalAndCharitableDonations politicalAndCharitableDonations;

    @Mock
    private PrincipalActivities principalActivities;

    private static final String SECRETARY = "secretary";

    private static final String TRANSACTION_ID = "transactionId";

    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";

    @Test
    @DisplayName("Get review")
    void getReview() throws ServiceException {

        Director[] directors = new Director[]{new Director()};
        when(directorService.getAllDirectors(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, false))
                .thenReturn(directors);

        when(secretaryService.getSecretary(TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(SECRETARY);

        when(additionalInformationService.getAdditionalInformation(TRANSACTION_ID,
                COMPANY_ACCOUNTS_ID))
                .thenReturn(additionalInformation);

        when(companyPolicyOnDisabledEmployeesService.getCompanyPolicyOnDisabledEmployees(
                TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(companyPolicyOnDisabledEmployees);

        when(politicalAndCharitableDonationsService.getPoliticalAndCharitableDonations(
                TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(politicalAndCharitableDonations);

        when(principalActivitiesService.getPrincipalActivities(TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(principalActivities);

        DirectorsReportReview review = directorsReportReviewService.getReview(TRANSACTION_ID,
                COMPANY_ACCOUNTS_ID);

        assertNotNull(review);
        assertEquals(directors, review.getDirectors());
        assertEquals(SECRETARY, review.getSecretary());
        assertEquals(additionalInformation, review.getAdditionalInformation());
        assertEquals(companyPolicyOnDisabledEmployees,
                review.getCompanyPolicyOnDisabledEmployees());
        assertEquals(politicalAndCharitableDonations, review.getPoliticalAndCharitableDonations());
        assertEquals(principalActivities, review.getPrincipalActivities());
    }
}
