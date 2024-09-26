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
import uk.gov.companieshouse.web.accounts.model.directorsreport.PoliticalAndCharitableDonationsSelection;
import uk.gov.companieshouse.web.accounts.service.smallfull.DirectorsReportStatementsService;
import uk.gov.companieshouse.web.accounts.service.smallfull.PoliticalAndCharitableDonationsSelectionService;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PoliticalAndCharitableDonationsSelectionServiceImplTest {

    @Mock
    private DirectorsReportStatementsService directorsReportStatementsService;

    @InjectMocks
    private PoliticalAndCharitableDonationsSelectionService politicalAndCharitableDonationsSelectionService =
            new PoliticalAndCharitableDonationsSelectionServiceImpl();

    @Mock
    private PoliticalAndCharitableDonationsSelection politicalAndCharitableDonationsSelection;

    @Mock
    private StatementsApi statementsApi;

    private static final String TRANSACTION_ID = "transactionId";

    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";

    private static final String POLITICAL_AND_CHARITABLE_DONATIONS = "politicalAndCharitableDonations";

    private static final String PRINCIPAL_ACTIVITIES = "principalActivities";

    @Test
    @DisplayName("Get political and charitable donations selection - no existing statements")
    void getPoliticalAndCharitableDonationsSelectionNoExistingStatements() throws ServiceException {

        when(directorsReportStatementsService.getDirectorsReportStatements(TRANSACTION_ID,
                COMPANY_ACCOUNTS_ID))
                .thenReturn(null);

        PoliticalAndCharitableDonationsSelection returned =
                politicalAndCharitableDonationsSelectionService.getPoliticalAndCharitableDonationsSelection(
                        TRANSACTION_ID, COMPANY_ACCOUNTS_ID);

        assertNotNull(returned);
        assertNull(returned.getHasPoliticalAndCharitableDonations());
    }

    @Test
    @DisplayName("Get political and charitable donations selection - statements do not include political and charitable donations")
    void getPoliticalAndCharitableDonationsSelectionStatementsDoNotIncludePoliticalAndCharitableDonations()
            throws ServiceException {

        when(directorsReportStatementsService.getDirectorsReportStatements(TRANSACTION_ID,
                COMPANY_ACCOUNTS_ID))
                .thenReturn(statementsApi);

        when(statementsApi.getPoliticalAndCharitableDonations()).thenReturn(null);

        PoliticalAndCharitableDonationsSelection returned =
                politicalAndCharitableDonationsSelectionService.getPoliticalAndCharitableDonationsSelection(
                        TRANSACTION_ID, COMPANY_ACCOUNTS_ID);

        assertNotNull(returned);
        assertNull(returned.getHasPoliticalAndCharitableDonations());
    }

    @Test
    @DisplayName("Get political and charitable donations selection - statements include political and charitable donations")
    void getPoliticalAndCharitableDonationsSelectionStatementsIncludePoliticalAndCharitableDonations()
            throws ServiceException {

        when(directorsReportStatementsService.getDirectorsReportStatements(TRANSACTION_ID,
                COMPANY_ACCOUNTS_ID))
                .thenReturn(statementsApi);

        when(statementsApi.getPoliticalAndCharitableDonations()).thenReturn(
                POLITICAL_AND_CHARITABLE_DONATIONS);

        PoliticalAndCharitableDonationsSelection returned =
                politicalAndCharitableDonationsSelectionService.getPoliticalAndCharitableDonationsSelection(
                        TRANSACTION_ID, COMPANY_ACCOUNTS_ID);

        assertNotNull(returned);
        assertTrue(returned.getHasPoliticalAndCharitableDonations());
    }

    @Test
    @DisplayName("Submit political and charitable donations selection - has political and charitable donations")
    void submitPoliticalAndCharitableDonationsSelectionHasPoliticalAndCharitableDonations()
            throws ServiceException {

        when(politicalAndCharitableDonationsSelection.getHasPoliticalAndCharitableDonations()).thenReturn(
                true);

        assertAll(() ->
                politicalAndCharitableDonationsSelectionService
                        .submitPoliticalAndCharitableDonationsSelection(TRANSACTION_ID,
                                COMPANY_ACCOUNTS_ID, politicalAndCharitableDonationsSelection));

        verify(directorsReportStatementsService, never()).getDirectorsReportStatements(
                TRANSACTION_ID, COMPANY_ACCOUNTS_ID);
    }

    @Test
    @DisplayName("Submit political and charitable donations selection - no existing statements")
    void submitPoliticalAndCharitableDonationsSelectionNoExistingStatements()
            throws ServiceException {

        when(politicalAndCharitableDonationsSelection.getHasPoliticalAndCharitableDonations()).thenReturn(
                false);

        when(directorsReportStatementsService.getDirectorsReportStatements(TRANSACTION_ID,
                COMPANY_ACCOUNTS_ID))
                .thenReturn(null);

        assertAll(() ->
                politicalAndCharitableDonationsSelectionService
                        .submitPoliticalAndCharitableDonationsSelection(TRANSACTION_ID,
                                COMPANY_ACCOUNTS_ID, politicalAndCharitableDonationsSelection));
    }

    @Test
    @DisplayName("Submit political and charitable donations selection - has other statements")
    void submitPoliticalAndCharitableDonationsSelectionHasOtherStatements()
            throws ServiceException {

        when(politicalAndCharitableDonationsSelection.getHasPoliticalAndCharitableDonations()).thenReturn(
                false);

        when(directorsReportStatementsService.getDirectorsReportStatements(TRANSACTION_ID,
                COMPANY_ACCOUNTS_ID))
                .thenReturn(statementsApi);

        when(statementsApi.getPrincipalActivities()).thenReturn(PRINCIPAL_ACTIVITIES);

        assertAll(() ->
                politicalAndCharitableDonationsSelectionService
                        .submitPoliticalAndCharitableDonationsSelection(TRANSACTION_ID,
                                COMPANY_ACCOUNTS_ID, politicalAndCharitableDonationsSelection));

        verify(statementsApi).setPoliticalAndCharitableDonations(null);

        verify(directorsReportStatementsService).updateDirectorsReportStatements(TRANSACTION_ID,
                COMPANY_ACCOUNTS_ID, statementsApi);
    }

    @Test
    @DisplayName("Submit political and charitable donations selection - has no other statements")
    void submitPoliticalAndCharitableDonationsSelectionHasNoOtherStatements()
            throws ServiceException {

        when(politicalAndCharitableDonationsSelection.getHasPoliticalAndCharitableDonations()).thenReturn(
                false);

        when(directorsReportStatementsService.getDirectorsReportStatements(TRANSACTION_ID,
                COMPANY_ACCOUNTS_ID))
                .thenReturn(statementsApi);

        assertAll(() ->
                politicalAndCharitableDonationsSelectionService
                        .submitPoliticalAndCharitableDonationsSelection(TRANSACTION_ID,
                                COMPANY_ACCOUNTS_ID, politicalAndCharitableDonationsSelection));

        verify(directorsReportStatementsService).deleteDirectorsReportStatements(TRANSACTION_ID,
                COMPANY_ACCOUNTS_ID);
    }
}
