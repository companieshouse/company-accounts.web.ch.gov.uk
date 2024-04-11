package uk.gov.companieshouse.web.accounts.service.notehandler.smallfull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.error.ApiErrorResponseException;
import uk.gov.companieshouse.api.handler.Executor;
import uk.gov.companieshouse.api.handler.exception.URIValidationException;
import uk.gov.companieshouse.api.handler.smallfull.SmallFullResourceHandler;
import uk.gov.companieshouse.api.handler.smallfull.financialcommitments.FinancialCommitmentsApi;
import uk.gov.companieshouse.api.handler.smallfull.financialcommitments.FinancialCommitmentsResourceHandler;
import uk.gov.companieshouse.api.handler.smallfull.financialcommitments.request.FinancialCommitmentsCreate;
import uk.gov.companieshouse.api.handler.smallfull.financialcommitments.request.FinancialCommitmentsDelete;
import uk.gov.companieshouse.api.handler.smallfull.financialcommitments.request.FinancialCommitmentsGet;
import uk.gov.companieshouse.api.handler.smallfull.financialcommitments.request.FinancialCommitmentsUpdate;
import uk.gov.companieshouse.api.model.ApiResponse;
import uk.gov.companieshouse.api.model.accounts.smallfull.SmallFullApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.SmallFullLinks;
import uk.gov.companieshouse.web.accounts.enumeration.NoteType;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.service.smallfull.SmallFullService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FinancialCommitmentsHandlerTest {
    @Mock
    private ApiClient apiClient;

    @Mock
    private SmallFullService smallFullService;

    @Mock
    private SmallFullResourceHandler smallFullResourceHandler;

    @Mock
    private FinancialCommitmentsGet financialCommitmentsGet;

    @Mock
    private FinancialCommitmentsUpdate financialCommitmentsUpdate;

    @Mock
    private FinancialCommitmentsCreate financialCommitmentsCreate;

    @Mock
    private FinancialCommitmentsDelete financialCommitmentsDelete;

    @Mock
    private FinancialCommitmentsApi financialCommitmentsApi;

    @Mock
    private SmallFullLinks smallFullLinks;

    @Mock
    private SmallFullApi smallFullApi;

    @InjectMocks
    private FinancialCommitmentsHandler financialCommitmentsHandler;

    @Mock
    private FinancialCommitmentsResourceHandler financialCommitmentsResourceHandler;

    private static final String COMPANY_NUMBER = "companyNumber";
    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";
    private static final String TRANSACTION_ID = "transactionId";

    private static final String URI = "/transactions/" + TRANSACTION_ID + "/company-accounts/" + COMPANY_ACCOUNTS_ID + "/small-full/notes/financial-commitments";

    private static final String FINANCIAL_COMMITMENTS_NOTE = "financialCommitments";

    @Test
    @DisplayName("Get the resource URI")
    void getResourceURI() {
        assertEquals(URI, financialCommitmentsHandler.getUri(TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }

    @Test
    @DisplayName("Get financial commitments Resource")
    void getOffBalanceSheetResource() {
        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);
        when(smallFullResourceHandler.financialCommitments()).thenReturn(financialCommitmentsResourceHandler);
        when(financialCommitmentsResourceHandler.get(URI)).thenReturn(financialCommitmentsGet);

        Executor<ApiResponse<FinancialCommitmentsApi>> financialCommitmentsApi = financialCommitmentsHandler.get(apiClient, URI);

        assertNotNull(financialCommitmentsApi);
        assertEquals(financialCommitmentsApi, financialCommitmentsGet);
        verify(financialCommitmentsResourceHandler).get(URI);
    }

    @Test
    @DisplayName("Update financial commitments Resource")
    void updateOffBalanceSheetResource() throws ApiErrorResponseException, URIValidationException {
        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);
        when(smallFullResourceHandler.financialCommitments()).thenReturn(financialCommitmentsResourceHandler);
        when(financialCommitmentsResourceHandler.update(URI, financialCommitmentsApi)).thenReturn(financialCommitmentsUpdate);

        Executor<ApiResponse<Void>> updatedFinancialCommitmentsApi = financialCommitmentsHandler.update(apiClient, URI, financialCommitmentsApi);

        assertNotNull(updatedFinancialCommitmentsApi);
        assertEquals(updatedFinancialCommitmentsApi, financialCommitmentsUpdate);
        verify(financialCommitmentsResourceHandler).update(URI, financialCommitmentsApi);
    }

    @Test
    @DisplayName("Create financial commitments Resource")
    void createOffBalanceSheetResource() {
        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);
        when(smallFullResourceHandler.financialCommitments()).thenReturn(financialCommitmentsResourceHandler);
        when(financialCommitmentsResourceHandler.create(URI, financialCommitmentsApi)).thenReturn(financialCommitmentsCreate);

        Executor<ApiResponse<FinancialCommitmentsApi>> createFinancialCommitmentsApi = financialCommitmentsHandler.create(apiClient, URI, financialCommitmentsApi);

        assertNotNull(createFinancialCommitmentsApi);
        assertEquals(createFinancialCommitmentsApi, financialCommitmentsCreate);
        verify(financialCommitmentsResourceHandler).create(URI, financialCommitmentsApi);
    }

    @Test
    @DisplayName("Delete financial commitments Resource")
    void deleteOffBalanceSheetResource() {
        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);
        when(smallFullResourceHandler.financialCommitments()).thenReturn(financialCommitmentsResourceHandler);
        when(financialCommitmentsResourceHandler.delete(URI)).thenReturn(financialCommitmentsDelete);

        Executor<ApiResponse<Void>> deleteFinancialCommitmentsApi = financialCommitmentsHandler.delete(apiClient, URI);

        assertNotNull(deleteFinancialCommitmentsApi);
        assertEquals(deleteFinancialCommitmentsApi, financialCommitmentsDelete);
        verify(financialCommitmentsResourceHandler).delete(URI);
    }

   @Test
    @DisplayName("Test parent resource throws service exception")
    void testParentResourceThrowsServiceException() throws ServiceException {
        when(smallFullService.getSmallFullAccounts(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID)).thenThrow(ServiceException.class);

        assertThrows(ServiceException.class, () -> financialCommitmentsHandler.parentResourceExists(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }

    @Test
    @DisplayName("Test method returns FinancialCommitments as NoteType")
    void testOffBalanceSheetArrangementsReturned()  {
        assertEquals(NoteType.SMALL_FULL_FINANCIAL_COMMITMENTS, financialCommitmentsHandler.getNoteType());
    }
}