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
import uk.gov.companieshouse.web.accounts.model.directorsreport.PrincipalActivitiesSelection;
import uk.gov.companieshouse.web.accounts.service.smallfull.PrincipalActivitiesSelectionService;
import uk.gov.companieshouse.web.accounts.service.smallfull.DirectorsReportStatementsService;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PrincipalActivitiesSelectionServiceImplTest {
    @Mock
    private DirectorsReportStatementsService directorsReportStatementsService;

    @InjectMocks
    private final PrincipalActivitiesSelectionService principalActivitiesSelectionService = new PrincipalActivitiesSelectionServiceImpl();

    @Mock
    private PrincipalActivitiesSelection principalActivitiesSelection;

    @Mock
    private StatementsApi statementsApi;

    private static final String TRANSACTION_ID = "transactionId";

    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";

    private static final String PRINCIPAL_ACTIVITIES = "principalActivities";

    private static final String ADDITIONAL_INFORMATION = "additionalInformation";

    @Test
    @DisplayName("Get principal activities selection - no existing statements")
    void getPrincipalActivitiesSelectionNoExistingStatements() throws ServiceException {
        when(directorsReportStatementsService.getDirectorsReportStatements(TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(null);

        PrincipalActivitiesSelection returned =
                principalActivitiesSelectionService.getPrincipalActivitiesSelection(TRANSACTION_ID, COMPANY_ACCOUNTS_ID);

        assertNotNull(returned);
        assertNull(returned.getHasPrincipalActivities());
    }

    @Test
    @DisplayName("Get principal activities selection - statements do not include principal activities")
    void getPrincipalActivitiesSelectionStatementsDoNotIncludePrincipalActivities() throws ServiceException {
        when(directorsReportStatementsService.getDirectorsReportStatements(TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(statementsApi);

        when(statementsApi.getPrincipalActivities()).thenReturn(null);

        PrincipalActivitiesSelection returned =
                principalActivitiesSelectionService.getPrincipalActivitiesSelection(TRANSACTION_ID, COMPANY_ACCOUNTS_ID);

        assertNotNull(returned);
        assertNull(returned.getHasPrincipalActivities());
    }

    @Test
    @DisplayName("Get principal activities selection - statements include principal activities")
    void getPrincipalActivitiesSelectionStatementsIncludePrincipalActivities() throws ServiceException {
        when(directorsReportStatementsService.getDirectorsReportStatements(TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(statementsApi);

        when(statementsApi.getPrincipalActivities()).thenReturn(PRINCIPAL_ACTIVITIES);

        PrincipalActivitiesSelection returned =
                principalActivitiesSelectionService.getPrincipalActivitiesSelection(TRANSACTION_ID, COMPANY_ACCOUNTS_ID);

        assertNotNull(returned);
        assertTrue(returned.getHasPrincipalActivities());
    }

    @Test
    @DisplayName("Submit principal activities selection - has principal activities")
    void submitPrincipalActivitiesSelectionHasPrincipalActivities() throws ServiceException {
        when(principalActivitiesSelection.getHasPrincipalActivities()).thenReturn(true);

        assertAll(() ->
                principalActivitiesSelectionService
                        .submitPrincipalActivitiesSelection(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, principalActivitiesSelection));

        verify(directorsReportStatementsService, never()).getDirectorsReportStatements(TRANSACTION_ID, COMPANY_ACCOUNTS_ID);
    }

    @Test
    @DisplayName("Submit principal activities selection - no existing statements")
    void submitPrincipalActivitiesSelectionNoExistingStatements() throws ServiceException {
        when(principalActivitiesSelection.getHasPrincipalActivities()).thenReturn(false);

        when(directorsReportStatementsService.getDirectorsReportStatements(TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(null);

        assertAll(() ->
                principalActivitiesSelectionService
                        .submitPrincipalActivitiesSelection(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, principalActivitiesSelection));
    }

    @Test
    @DisplayName("Submit principal activities selection - has other statements")
    void submitPrincipalActivitiesSelectionHasOtherStatements() throws ServiceException {
        when(principalActivitiesSelection.getHasPrincipalActivities()).thenReturn(false);

        when(directorsReportStatementsService.getDirectorsReportStatements(TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(statementsApi);

        when(statementsApi.getAdditionalInformation()).thenReturn(ADDITIONAL_INFORMATION);

        assertAll(() ->
                principalActivitiesSelectionService
                        .submitPrincipalActivitiesSelection(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, principalActivitiesSelection));

        verify(statementsApi).setPrincipalActivities(null);

        verify(directorsReportStatementsService).updateDirectorsReportStatements(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, statementsApi);
    }

    @Test
    @DisplayName("Submit principal activities selection - has no other statements")
    void submitPrincipalActivitiesSelectionHasNoOtherStatements() throws ServiceException {
        when(principalActivitiesSelection.getHasPrincipalActivities()).thenReturn(false);

        when(directorsReportStatementsService.getDirectorsReportStatements(TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(statementsApi);

        assertAll(() ->
                principalActivitiesSelectionService
                        .submitPrincipalActivitiesSelection(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, principalActivitiesSelection));

        verify(directorsReportStatementsService).deleteDirectorsReportStatements(TRANSACTION_ID, COMPANY_ACCOUNTS_ID);
    }
}
