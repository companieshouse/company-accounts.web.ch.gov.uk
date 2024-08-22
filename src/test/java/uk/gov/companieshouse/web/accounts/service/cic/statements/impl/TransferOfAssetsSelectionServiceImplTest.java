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
import uk.gov.companieshouse.web.accounts.model.cic.statements.TransferOfAssetsSelection;
import uk.gov.companieshouse.web.accounts.service.cic.statements.CicStatementsService;
import uk.gov.companieshouse.web.accounts.service.cic.statements.TransferOfAssetsSelectionService;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TransferOfAssetsSelectionServiceImplTest {

    @Mock
    private CicStatementsService cicStatementsService;

    @Mock
    private CicStatementsApi cicStatementsApi;

    @Mock
    private ReportStatementsApi reportStatementsApi;

    @Mock
    private TransferOfAssetsSelection transferOfAssetsSelection;

    @InjectMocks
    private TransferOfAssetsSelectionService selectionService =
            new TransferOfAssetsSelectionServiceImpl();

    private static final String TRANSACTION_ID = "transactionId";

    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";

    private static final String DEFAULT_TRANSFER_OF_ASSETS_STATEMENT =
            "No transfer of assets other than for full consideration";

    private static final String OTHER_STATEMENT = "Other statement";

    @Test
    @DisplayName("Get transfer of assets selection - default statement")
    void getTransferOfAssetsSelectionFromDefaultStatement() throws ServiceException {

        when(cicStatementsService.getCicStatementsApi(TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(cicStatementsApi);

        when(cicStatementsApi.getReportStatements()).thenReturn(reportStatementsApi);

        when(reportStatementsApi.getTransferOfAssets())
                .thenReturn(DEFAULT_TRANSFER_OF_ASSETS_STATEMENT);

        TransferOfAssetsSelection selection =
                selectionService.getTransferOfAssetsSelection(TRANSACTION_ID, COMPANY_ACCOUNTS_ID);

        assertNotNull(selection);
        assertNull(selection.getHasProvidedTransferOfAssets());
    }

    @Test
    @DisplayName("Get transfer of assets selection - other statement")
    void getTransferOfAssetsSelectionFromOtherStatement() throws ServiceException {

        when(cicStatementsService.getCicStatementsApi(TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(cicStatementsApi);

        when(cicStatementsApi.getReportStatements()).thenReturn(reportStatementsApi);

        when(reportStatementsApi.getTransferOfAssets())
                .thenReturn(OTHER_STATEMENT);

        TransferOfAssetsSelection selection =
                selectionService.getTransferOfAssetsSelection(TRANSACTION_ID, COMPANY_ACCOUNTS_ID);

        assertNotNull(selection);
        assertTrue(selection.getHasProvidedTransferOfAssets());
    }

    @Test
    @DisplayName("Submit transfer of assets selection - has provided transfer")
    void submitTransferOfAssetsHasProvidedTransfer() throws ServiceException {

        when(transferOfAssetsSelection.getHasProvidedTransferOfAssets())
                .thenReturn(true);

        selectionService.submitTransferOfAssetsSelection(
                TRANSACTION_ID, COMPANY_ACCOUNTS_ID, transferOfAssetsSelection);

        verify(cicStatementsService, never()).getCicStatementsApi(TRANSACTION_ID,
                COMPANY_ACCOUNTS_ID);
    }

    @Test
    @DisplayName("Submit transfer of assets selection - no transfer but API resource already default")
    void submitTransferOfAssetsNoTransferButApiResourceAlreadyDefault() throws ServiceException {

        when(transferOfAssetsSelection.getHasProvidedTransferOfAssets())
                .thenReturn(false);

        when(cicStatementsService.getCicStatementsApi(TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(cicStatementsApi);

        when(cicStatementsApi.getReportStatements()).thenReturn(reportStatementsApi);

        when(reportStatementsApi.getTransferOfAssets())
                .thenReturn(DEFAULT_TRANSFER_OF_ASSETS_STATEMENT);

        selectionService.submitTransferOfAssetsSelection(
                TRANSACTION_ID, COMPANY_ACCOUNTS_ID, transferOfAssetsSelection);

        verify(cicStatementsService, never())
                .updateCicStatementsApi(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, cicStatementsApi);
    }

    @Test
    @DisplayName("Submit transfer of assets selection - no transfer and API resource statement not default")
    void submitTransferOfAssetsNoTransferAndApiResourceNotDefault() throws ServiceException {

        when(transferOfAssetsSelection.getHasProvidedTransferOfAssets())
                .thenReturn(false);

        when(cicStatementsService.getCicStatementsApi(TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(cicStatementsApi);

        when(cicStatementsApi.getReportStatements()).thenReturn(reportStatementsApi);

        when(reportStatementsApi.getTransferOfAssets())
                .thenReturn(OTHER_STATEMENT);

        selectionService.submitTransferOfAssetsSelection(
                TRANSACTION_ID, COMPANY_ACCOUNTS_ID, transferOfAssetsSelection);

        verify(reportStatementsApi).setTransferOfAssets(DEFAULT_TRANSFER_OF_ASSETS_STATEMENT);

        verify(cicStatementsService)
                .updateCicStatementsApi(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, cicStatementsApi);
    }
}
