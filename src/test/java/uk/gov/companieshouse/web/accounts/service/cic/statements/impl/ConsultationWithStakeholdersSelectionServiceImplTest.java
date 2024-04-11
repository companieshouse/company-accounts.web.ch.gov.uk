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
import uk.gov.companieshouse.web.accounts.model.cic.statements.ConsultationWithStakeholdersSelection;
import uk.gov.companieshouse.web.accounts.service.cic.statements.CicStatementsService;
import uk.gov.companieshouse.web.accounts.service.cic.statements.ConsultationWithStakeholdersSelectionService;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ConsultationWithStakeholdersSelectionServiceImplTest {
    @Mock
    private CicStatementsService cicStatementsService;

    @Mock
    private CicStatementsApi cicStatementsApi;

    @Mock
    private ReportStatementsApi reportStatementsApi;

    @Mock
    private ConsultationWithStakeholdersSelection consultationWithStakeholdersSelection;

    @InjectMocks
    private ConsultationWithStakeholdersSelectionService selectionService =
            new ConsultationWithStakeholdersSelectionServiceImpl();

    private static final String TRANSACTION_ID = "transactionId";

    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";

    private static final String DEFAULT_CONSULTATION_WITH_STAKEHOLDERS_STATEMENT =
            "No consultation with stakeholders";

    private static final String OTHER_STATEMENT = "Other statement";

    @Test
    @DisplayName("Get consultation with stakeholders selection - default statement")
    void getConsultationWithStakeholdersSelectionFromDefaultStatement() throws ServiceException {
        when(cicStatementsService.getCicStatementsApi(TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(cicStatementsApi);

        when(cicStatementsApi.getReportStatements()).thenReturn(reportStatementsApi);

        when(reportStatementsApi.getConsultationWithStakeholders())
                .thenReturn(DEFAULT_CONSULTATION_WITH_STAKEHOLDERS_STATEMENT);

        ConsultationWithStakeholdersSelection selection =
                selectionService.getConsultationWithStakeholdersSelection(TRANSACTION_ID, COMPANY_ACCOUNTS_ID);

        assertNotNull(selection);
        assertNull(selection.getHasProvidedConsultationWithStakeholders());
    }

    @Test
    @DisplayName("Get consultation with stakeholders selection - other statement")
    void getConsultationWithStakeholdersSelectionFromOtherStatement() throws ServiceException {
        when(cicStatementsService.getCicStatementsApi(TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(cicStatementsApi);

        when(cicStatementsApi.getReportStatements()).thenReturn(reportStatementsApi);

        when(reportStatementsApi.getConsultationWithStakeholders())
                .thenReturn(OTHER_STATEMENT);

        ConsultationWithStakeholdersSelection selection =
                selectionService.getConsultationWithStakeholdersSelection(TRANSACTION_ID, COMPANY_ACCOUNTS_ID);

        assertNotNull(selection);
        assertTrue(selection.getHasProvidedConsultationWithStakeholders());
    }

    @Test
    @DisplayName("Submit consultation with stakeholders selection - has provided consultation")
    void submitConsultationWithStakeholdersHasProvidedConsultation() throws ServiceException {
        when(consultationWithStakeholdersSelection.getHasProvidedConsultationWithStakeholders())
                .thenReturn(true);

        selectionService.submitConsultationWithStakeholdersSelection(
                TRANSACTION_ID, COMPANY_ACCOUNTS_ID, consultationWithStakeholdersSelection);

        verify(cicStatementsService, never()).getCicStatementsApi(TRANSACTION_ID, COMPANY_ACCOUNTS_ID);
    }

    @Test
    @DisplayName("Submit consultation with stakeholders selection - no consultation but API resource already default")
    void submitConsultationWithStakeholdersNoConsultationButApiResourceAlreadyDefault() throws ServiceException {
        when(consultationWithStakeholdersSelection.getHasProvidedConsultationWithStakeholders())
                .thenReturn(false);

        when(cicStatementsService.getCicStatementsApi(TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(cicStatementsApi);

        when(cicStatementsApi.getReportStatements()).thenReturn(reportStatementsApi);

        when(reportStatementsApi.getConsultationWithStakeholders())
                .thenReturn(DEFAULT_CONSULTATION_WITH_STAKEHOLDERS_STATEMENT);

        selectionService.submitConsultationWithStakeholdersSelection(
                TRANSACTION_ID, COMPANY_ACCOUNTS_ID, consultationWithStakeholdersSelection);

        verify(cicStatementsService, never())
                .updateCicStatementsApi(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, cicStatementsApi);
    }

    @Test
    @DisplayName("Submit consultation with stakeholders selection - no consultation and API resource statement not default")
    void submitConsultationWithStakeholdersNoConsultationAndApiResourceNotDefault() throws ServiceException {
        when(consultationWithStakeholdersSelection.getHasProvidedConsultationWithStakeholders())
                .thenReturn(false);

        when(cicStatementsService.getCicStatementsApi(TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(cicStatementsApi);

        when(cicStatementsApi.getReportStatements()).thenReturn(reportStatementsApi);

        when(reportStatementsApi.getConsultationWithStakeholders())
                .thenReturn(OTHER_STATEMENT);

        selectionService.submitConsultationWithStakeholdersSelection(
                TRANSACTION_ID, COMPANY_ACCOUNTS_ID, consultationWithStakeholdersSelection);

        verify(reportStatementsApi).setConsultationWithStakeholders(DEFAULT_CONSULTATION_WITH_STAKEHOLDERS_STATEMENT);

        verify(cicStatementsService)
                .updateCicStatementsApi(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, cicStatementsApi);
    }
}
