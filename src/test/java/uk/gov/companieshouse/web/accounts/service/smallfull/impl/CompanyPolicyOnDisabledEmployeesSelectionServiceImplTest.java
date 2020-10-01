package uk.gov.companieshouse.web.accounts.service.smallfull.impl;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.api.model.accounts.directorsreport.StatementsApi;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.directorsreport.CompanyPolicyOnDisabledEmployeesSelection;
import uk.gov.companieshouse.web.accounts.service.smallfull.CompanyPolicyOnDisabledEmployeesSelectionService;
import uk.gov.companieshouse.web.accounts.service.smallfull.DirectorsReportStatementsService;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CompanyPolicyOnDisabledEmployeesSelectionServiceImplTest {

    @Mock
    private DirectorsReportStatementsService directorsReportStatementsService;

    @InjectMocks
    private CompanyPolicyOnDisabledEmployeesSelectionService companyPolicyOnDisabledEmployeesSelectionService = 
            new CompanyPolicyOnDisabledEmployeesSelectionServiceImpl();

    @Mock
    private CompanyPolicyOnDisabledEmployeesSelection companyPolicyOnDisabledEmployeesSelection;

    @Mock
    private StatementsApi statementsApi;

    private static final String TRANSACTION_ID = "transactionId";

    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";

    private static final String COMPANY_POLICY_ON_DISABLED_EMPLOYEES = "companyPolicyOnDisabledEmployees";

    private static final String PRINCIPAL_ACTIVITIES = "principalActivities";

    @Test
    @DisplayName("Get company policy on disabled employees selection - no existing statements")
    void getCompanyPolicyOnDisabledEmployeesSelectionNoExistingStatements() throws ServiceException {

        when(directorsReportStatementsService.getDirectorsReportStatements(TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(null);

        CompanyPolicyOnDisabledEmployeesSelection returned =
                companyPolicyOnDisabledEmployeesSelectionService.getCompanyPolicyOnDisabledEmployeesSelection(TRANSACTION_ID, COMPANY_ACCOUNTS_ID);

        assertNotNull(returned);
        assertNull(returned.getHasCompanyPolicyOnDisabledEmployees());
    }

    @Test
    @DisplayName("Get company policy on disabled employees selection - statements do not include company policy on disabled employees")
    void getCompanyPolicyOnDisabledEmployeesSelectionStatementsDoNotIncludeCompanyPolicyOnDisabledEmployees() throws ServiceException {

        when(directorsReportStatementsService.getDirectorsReportStatements(TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(statementsApi);

        when(statementsApi.getCompanyPolicyOnDisabledEmployees()).thenReturn(null);

        CompanyPolicyOnDisabledEmployeesSelection returned =
                companyPolicyOnDisabledEmployeesSelectionService.getCompanyPolicyOnDisabledEmployeesSelection(TRANSACTION_ID, COMPANY_ACCOUNTS_ID);

        assertNotNull(returned);
        assertNull(returned.getHasCompanyPolicyOnDisabledEmployees());
    }

    @Test
    @DisplayName("Get company policy on disabled employees selection - statements include company policy on disabled employees")
    void getCompanyPolicyOnDisabledEmployeesSelectionStatementsIncludeCompanyPolicyOnDisabledEmployees() throws ServiceException {

        when(directorsReportStatementsService.getDirectorsReportStatements(TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(statementsApi);

        when(statementsApi.getCompanyPolicyOnDisabledEmployees()).thenReturn(
                COMPANY_POLICY_ON_DISABLED_EMPLOYEES);

        CompanyPolicyOnDisabledEmployeesSelection returned =
                companyPolicyOnDisabledEmployeesSelectionService.getCompanyPolicyOnDisabledEmployeesSelection(TRANSACTION_ID, COMPANY_ACCOUNTS_ID);

        assertNotNull(returned);
        assertTrue(returned.getHasCompanyPolicyOnDisabledEmployees());
    }

    @Test
    @DisplayName("Submit company policy on disabled employees selection - has company policy on disabled employees")
    void submitCompanyPolicyOnDisabledEmployeesSelectionHasCompanyPolicyOnDisabledEmployees() throws ServiceException {

        when(companyPolicyOnDisabledEmployeesSelection.getHasCompanyPolicyOnDisabledEmployees()).thenReturn(true);

        assertAll(() ->
                companyPolicyOnDisabledEmployeesSelectionService
                        .submitCompanyPolicyOnDisabledEmployeesSelection(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, companyPolicyOnDisabledEmployeesSelection));

        verify(directorsReportStatementsService, never()).getDirectorsReportStatements(TRANSACTION_ID, COMPANY_ACCOUNTS_ID);
    }

    @Test
    @DisplayName("Submit company policy on disabled employees selection - no existing statements")
    void submitCompanyPolicyOnDisabledEmployeesSelectionNoExistingStatements() throws ServiceException {

        when(companyPolicyOnDisabledEmployeesSelection.getHasCompanyPolicyOnDisabledEmployees()).thenReturn(false);

        when(directorsReportStatementsService.getDirectorsReportStatements(TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(null);

        assertAll(() ->
                companyPolicyOnDisabledEmployeesSelectionService
                        .submitCompanyPolicyOnDisabledEmployeesSelection(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, companyPolicyOnDisabledEmployeesSelection));
    }

    @Test
    @DisplayName("Submit company policy on disabled employees selection - has other statements")
    void submitCompanyPolicyOnDisabledEmployeesSelectionHasOtherStatements() throws ServiceException {

        when(companyPolicyOnDisabledEmployeesSelection.getHasCompanyPolicyOnDisabledEmployees()).thenReturn(false);

        when(directorsReportStatementsService.getDirectorsReportStatements(TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(statementsApi);

        when(statementsApi.getPrincipalActivities()).thenReturn(PRINCIPAL_ACTIVITIES);

        assertAll(() ->
                companyPolicyOnDisabledEmployeesSelectionService
                        .submitCompanyPolicyOnDisabledEmployeesSelection(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, companyPolicyOnDisabledEmployeesSelection));

        verify(statementsApi).setCompanyPolicyOnDisabledEmployees(null);

        verify(directorsReportStatementsService).updateDirectorsReportStatements(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, statementsApi);
    }

    @Test
    @DisplayName("Submit company policy on disabled employees selection - has no other statements")
    void submitCompanyPolicyOnDisabledEmployeesSelectionHasNoOtherStatements() throws ServiceException {

        when(companyPolicyOnDisabledEmployeesSelection.getHasCompanyPolicyOnDisabledEmployees()).thenReturn(false);

        when(directorsReportStatementsService.getDirectorsReportStatements(TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(statementsApi);

        assertAll(() ->
                companyPolicyOnDisabledEmployeesSelectionService
                        .submitCompanyPolicyOnDisabledEmployeesSelection(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, companyPolicyOnDisabledEmployeesSelection));

        verify(directorsReportStatementsService).deleteDirectorsReportStatements(TRANSACTION_ID, COMPANY_ACCOUNTS_ID);
    }
}
