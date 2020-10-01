package uk.gov.companieshouse.web.accounts.service.cic.statements.impl;

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
import uk.gov.companieshouse.api.model.accounts.cic.statements.CicStatementsApi;
import uk.gov.companieshouse.api.model.accounts.cic.statements.ReportStatementsApi;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.cic.statements.DirectorsRemunerationSelection;
import uk.gov.companieshouse.web.accounts.service.cic.statements.CicStatementsService;
import uk.gov.companieshouse.web.accounts.service.cic.statements.DirectorsRemunerationSelectionService;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DirectorsRemunerationSelectionServiceImplTest {

    @Mock
    private CicStatementsService cicStatementsService;

    @Mock
    private CicStatementsApi cicStatementsApi;

    @Mock
    private ReportStatementsApi reportStatementsApi;

    @Mock
    private DirectorsRemunerationSelection directorsRemunerationSelection;

    @InjectMocks
    private DirectorsRemunerationSelectionService selectionService =
            new DirectorsRemunerationSelectionServiceImpl();

    private static final String TRANSACTION_ID = "transactionId";

    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";

    private static final String DEFAULT_DIRECTORS_REMUNERATION_STATEMENT =
            "No remuneration was received";

    private static final String OTHER_STATEMENT = "Other statement";

    @Test
    @DisplayName("Get directors remuneration selection - default statement")
    void getDirectorsRemunerationSelectionFromDefaultStatement() throws ServiceException {

        when(cicStatementsService.getCicStatementsApi(TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(cicStatementsApi);

        when(cicStatementsApi.getReportStatements()).thenReturn(reportStatementsApi);

        when(reportStatementsApi.getDirectorsRemuneration())
                .thenReturn(DEFAULT_DIRECTORS_REMUNERATION_STATEMENT);

        DirectorsRemunerationSelection selection =
                selectionService.getDirectorsRemunerationSelection(TRANSACTION_ID, COMPANY_ACCOUNTS_ID);

        assertNotNull(selection);
        assertNull(selection.getHasProvidedDirectorsRemuneration());
    }

    @Test
    @DisplayName("Get directors remuneration selection - other statement")
    void getDirectorsRemunerationSelectionFromOtherStatement() throws ServiceException {

        when(cicStatementsService.getCicStatementsApi(TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(cicStatementsApi);

        when(cicStatementsApi.getReportStatements()).thenReturn(reportStatementsApi);

        when(reportStatementsApi.getDirectorsRemuneration())
                .thenReturn(OTHER_STATEMENT);

        DirectorsRemunerationSelection selection =
                selectionService.getDirectorsRemunerationSelection(TRANSACTION_ID, COMPANY_ACCOUNTS_ID);

        assertNotNull(selection);
        assertTrue(selection.getHasProvidedDirectorsRemuneration());
    }

    @Test
    @DisplayName("Submit directors remuneration selection - has provided remuneration")
    void submitDirectorsRemunerationHasProvidedRemuneration() throws ServiceException {

        when(directorsRemunerationSelection.getHasProvidedDirectorsRemuneration())
                .thenReturn(true);

        selectionService.submitDirectorsRemunerationSelection(
                TRANSACTION_ID, COMPANY_ACCOUNTS_ID, directorsRemunerationSelection);

        verify(cicStatementsService, never()).getCicStatementsApi(TRANSACTION_ID, COMPANY_ACCOUNTS_ID);
    }

    @Test
    @DisplayName("Submit directors remuneration selection - no remuneration but API resource already default")
    void submitDirectorsRemunerationNoRemunerationButApiResourceAlreadyDefault() throws ServiceException {

        when(directorsRemunerationSelection.getHasProvidedDirectorsRemuneration())
                .thenReturn(false);

        when(cicStatementsService.getCicStatementsApi(TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(cicStatementsApi);

        when(cicStatementsApi.getReportStatements()).thenReturn(reportStatementsApi);

        when(reportStatementsApi.getDirectorsRemuneration())
                .thenReturn(DEFAULT_DIRECTORS_REMUNERATION_STATEMENT);

        selectionService.submitDirectorsRemunerationSelection(
                TRANSACTION_ID, COMPANY_ACCOUNTS_ID, directorsRemunerationSelection);

        verify(cicStatementsService, never())
                .updateCicStatementsApi(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, cicStatementsApi);
    }

    @Test
    @DisplayName("Submit directors remuneration selection - no remuneration and API resource statement not default")
    void submitDirectorsRemunerationNoRemunerationAndApiResourceNotDefault() throws ServiceException {

        when(directorsRemunerationSelection.getHasProvidedDirectorsRemuneration())
                .thenReturn(false);

        when(cicStatementsService.getCicStatementsApi(TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(cicStatementsApi);

        when(cicStatementsApi.getReportStatements()).thenReturn(reportStatementsApi);

        when(reportStatementsApi.getDirectorsRemuneration())
                .thenReturn(OTHER_STATEMENT);

        selectionService.submitDirectorsRemunerationSelection(
                TRANSACTION_ID, COMPANY_ACCOUNTS_ID, directorsRemunerationSelection);

        verify(reportStatementsApi).setDirectorsRemuneration(DEFAULT_DIRECTORS_REMUNERATION_STATEMENT);

        verify(cicStatementsService)
                .updateCicStatementsApi(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, cicStatementsApi);
    }
}
