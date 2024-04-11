package uk.gov.companieshouse.web.accounts.service.notehandler.smallfull;

import org.junit.jupiter.api.BeforeEach;
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
import uk.gov.companieshouse.api.handler.smallfull.creditorswithinoneyear.CreditorsWithinOneYearResourceHandler;
import uk.gov.companieshouse.api.handler.smallfull.creditorswithinoneyear.request.CreditorsWithinOneYearCreate;
import uk.gov.companieshouse.api.handler.smallfull.creditorswithinoneyear.request.CreditorsWithinOneYearDelete;
import uk.gov.companieshouse.api.handler.smallfull.creditorswithinoneyear.request.CreditorsWithinOneYearGet;
import uk.gov.companieshouse.api.handler.smallfull.creditorswithinoneyear.request.CreditorsWithinOneYearUpdate;
import uk.gov.companieshouse.api.model.ApiResponse;
import uk.gov.companieshouse.api.model.accounts.smallfull.SmallFullApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.SmallFullLinks;
import uk.gov.companieshouse.api.model.accounts.smallfull.creditorswithinoneyear.CreditorsWithinOneYearApi;
import uk.gov.companieshouse.web.accounts.enumeration.NoteType;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.service.smallfull.SmallFullService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CreditorsWithinOneYearHandlerTest {
    @Mock
    private ApiClient apiClient;

    @Mock
    private SmallFullService smallFullService;

    @Mock
    private SmallFullResourceHandler smallFullResourceHandler;

    @Mock
    private CreditorsWithinOneYearGet creditorsWithinOneYearGet;

    @Mock
    private CreditorsWithinOneYearUpdate creditorsWithinOneYearUpdate;

    @Mock
    private CreditorsWithinOneYearDelete creditorsWithinOneYearDelete;

    @Mock
    private CreditorsWithinOneYearCreate creditorsWithinOneYearCreate;

    @Mock
    private CreditorsWithinOneYearApi creditorsWithinOneYearApi;

    @Mock
    private SmallFullLinks smallFullLinks;

    @Mock
    private SmallFullApi smallFullApi;

    @Mock
    private CreditorsWithinOneYearResourceHandler creditorsWithinOneYearResourceHandler;

    @InjectMocks
    private CreditorsWithinOneYearHandler creditorsWithinOneYearHandler;

    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";
    private static final String TRANSACTION_ID = "transactionId";

    private static final String URI = "/transactions/" + TRANSACTION_ID + "/company-accounts/" +
            COMPANY_ACCOUNTS_ID + "/small-full/notes/creditors-within-one-year";

    private static final String CREDITORS_WITHIN_ONE_YEAR_NOTE = "creditorsWithinOneYear";

    @Test
    @DisplayName("Get CreditorsWithinOneYear resource URI")
    void getCreditorsWithinOneYearURI() {
        assertEquals(URI, creditorsWithinOneYearHandler.getUri(TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }

    @Test
    @DisplayName("Get CreditorsWithinOneYear Resource")
    void getCreditorsWithinOneYearResource() {
        setupCreditorsWithinOneYearResourceHandler();

        when(creditorsWithinOneYearResourceHandler.get(URI)).thenReturn(creditorsWithinOneYearGet);

        Executor<ApiResponse<CreditorsWithinOneYearApi>> creditorsWithinOneYearApiGet = creditorsWithinOneYearHandler.get(apiClient, URI);

        assertNotNull(creditorsWithinOneYearApiGet);
        assertEquals(creditorsWithinOneYearApiGet, creditorsWithinOneYearGet);
        verify(creditorsWithinOneYearResourceHandler).get(URI);
    }

    @Test
    @DisplayName("Update CreditorsWithinOneYear Resource")
    void updateCreditorsWithinOneYearResource() throws ApiErrorResponseException, URIValidationException {
        setupCreditorsWithinOneYearResourceHandler();

        when(creditorsWithinOneYearResourceHandler.update(URI, creditorsWithinOneYearApi)).thenReturn(creditorsWithinOneYearUpdate);

        Executor<ApiResponse<Void>> updatedCreditorsWithinOneYear = creditorsWithinOneYearHandler.update(apiClient, URI, creditorsWithinOneYearApi);

        assertNotNull(updatedCreditorsWithinOneYear);
        assertEquals(updatedCreditorsWithinOneYear, creditorsWithinOneYearUpdate);
        verify(creditorsWithinOneYearResourceHandler).update(URI, creditorsWithinOneYearApi);
    }

    @Test
    @DisplayName("Create CreditorsWithinOneYear Resource")
    void createCreditorsWithinOneYearResource() {
        setupCreditorsWithinOneYearResourceHandler();

        when(creditorsWithinOneYearResourceHandler.create(URI, creditorsWithinOneYearApi)).thenReturn(creditorsWithinOneYearCreate);

        Executor<ApiResponse<CreditorsWithinOneYearApi>> createCreditorsWithinOneYearApi = creditorsWithinOneYearHandler.create(apiClient, URI, creditorsWithinOneYearApi);

        assertNotNull(createCreditorsWithinOneYearApi);
        assertEquals(createCreditorsWithinOneYearApi, creditorsWithinOneYearCreate);
        verify(creditorsWithinOneYearResourceHandler).create(URI, creditorsWithinOneYearApi);
    }

    @Test
    @DisplayName("Delete CreditorsWithinOneYear Resource")
    void deleteCreditorsWithinOneYearResource() {
        setupCreditorsWithinOneYearResourceHandler();

        when(creditorsWithinOneYearResourceHandler.delete(URI)).thenReturn(creditorsWithinOneYearDelete);

        Executor<ApiResponse<Void>> deleteCreditorsWithinOneYearApi = creditorsWithinOneYearHandler.delete(apiClient, URI);

        assertNotNull(deleteCreditorsWithinOneYearApi);
        assertEquals(deleteCreditorsWithinOneYearApi, creditorsWithinOneYearDelete);
        verify(creditorsWithinOneYearResourceHandler).delete(URI);
    }

    @Test
    @DisplayName("Test parent resource exist")
    void testParentResourceExist() throws ServiceException {
        when(smallFullService.getSmallFullAccounts(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID)).thenReturn(smallFullApi);
        when(smallFullApi.getLinks()).thenReturn(smallFullLinks);
        when(smallFullLinks.getCreditorsWithinOneYearNote()).thenReturn(CREDITORS_WITHIN_ONE_YEAR_NOTE);

        assertTrue(creditorsWithinOneYearHandler.parentResourceExists(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }

    @Test
    @DisplayName("Test parent resource throws service exception")
    void testParentResourceThrowsServiceException() throws ServiceException {
        when(smallFullService.getSmallFullAccounts(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID)).thenThrow(ServiceException.class);

        assertThrows(ServiceException.class, () -> creditorsWithinOneYearHandler.parentResourceExists(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }

    @Test
    @DisplayName("Test method returns CreditorsWithinOneYear as NoteType")
    void testCreditorsWithinOneYearReturned()  {
        assertEquals(NoteType.SMALL_FULL_CREDITORS_WITHIN_ONE_YEAR, creditorsWithinOneYearHandler.getNoteType());
    }

    private void setupCreditorsWithinOneYearResourceHandler() {
        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);
        when(smallFullResourceHandler.creditorsWithinOneYear()).thenReturn(creditorsWithinOneYearResourceHandler);
    }

}