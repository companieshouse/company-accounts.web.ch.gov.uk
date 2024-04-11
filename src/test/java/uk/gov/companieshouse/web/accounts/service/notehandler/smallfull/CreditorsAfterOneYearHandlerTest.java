package uk.gov.companieshouse.web.accounts.service.notehandler.smallfull;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.error.ApiErrorResponseException;
import uk.gov.companieshouse.api.handler.Executor;
import uk.gov.companieshouse.api.handler.exception.URIValidationException;
import uk.gov.companieshouse.api.handler.smallfull.SmallFullResourceHandler;
import uk.gov.companieshouse.api.handler.smallfull.creditorsafteroneyear.CreditorsAfterOneYearResourceHandler;
import uk.gov.companieshouse.api.handler.smallfull.creditorsafteroneyear.request.CreditorsAfterOneYearCreate;
import uk.gov.companieshouse.api.handler.smallfull.creditorsafteroneyear.request.CreditorsAfterOneYearDelete;
import uk.gov.companieshouse.api.handler.smallfull.creditorsafteroneyear.request.CreditorsAfterOneYearGet;
import uk.gov.companieshouse.api.handler.smallfull.creditorsafteroneyear.request.CreditorsAfterOneYearUpdate;
import uk.gov.companieshouse.api.model.ApiResponse;
import uk.gov.companieshouse.api.model.accounts.smallfull.SmallFullApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.SmallFullLinks;
import uk.gov.companieshouse.api.model.accounts.smallfull.creditorsafteroneyear.CreditorsAfterOneYearApi;
import uk.gov.companieshouse.web.accounts.enumeration.NoteType;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.service.smallfull.SmallFullService;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CreditorsAfterOneYearHandlerTest {
    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";
    private static final String TRANSACTION_ID = "transactionId";

    private static final String URI = "/transactions/" + TRANSACTION_ID + "/company-accounts/" +
            COMPANY_ACCOUNTS_ID + "/small-full/notes/creditors-after-more-than-one-year";

    private static final String CREDITORS_AFTER_ONE_YEAR_NOTE = "creditorsAfterOneYear";

    @Mock
    private ApiClient apiClient;

    @Mock
    private SmallFullService smallFullService;

    @Mock
    private SmallFullResourceHandler smallFullResourceHandler;

    @Mock
    private CreditorsAfterOneYearGet creditorsAfterOneYearGet;

    @Mock
    private CreditorsAfterOneYearUpdate creditorsAfterOneYearUpdate;

    @Mock
    private CreditorsAfterOneYearDelete creditorsAfterOneYearDelete;

    @Mock
    private CreditorsAfterOneYearCreate creditorsAfterOneYearCreate;

    @Mock
    private CreditorsAfterOneYearApi creditorsAfterOneYearApi;

    @Mock
    private SmallFullLinks smallFullLinks;

    @Mock
    private SmallFullApi smallFullApi;

    @Mock
    private CreditorsAfterOneYearResourceHandler creditorsAfterOneYearResourceHandler;

    private CreditorsAfterOneYearHandler creditorsAfterOneYearHandler;

    @BeforeEach
    void before () {
        creditorsAfterOneYearHandler = new CreditorsAfterOneYearHandler(smallFullService);
    }

    @Test
    @DisplayName("Get CreditorsAfterOneYear resource URI")
    void getCreditorsAfterOneYearURI() {
        assertEquals(URI, creditorsAfterOneYearHandler.getUri(TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }

    @Test
    @DisplayName("Get CreditorsAfterOneYear Resource")
    void getCreditorsAfterOneYearResource() {
        setupCreditorsAfterOneYearResourceHandler();

        when(creditorsAfterOneYearResourceHandler.get(URI)).thenReturn(creditorsAfterOneYearGet);

        Executor<ApiResponse<CreditorsAfterOneYearApi>> creditorsAfterOneYearApiGet = creditorsAfterOneYearHandler.get(apiClient, URI);

        assertNotNull(creditorsAfterOneYearApiGet);
        assertEquals(creditorsAfterOneYearApiGet, creditorsAfterOneYearGet);
        verify(creditorsAfterOneYearResourceHandler).get(URI);
    }

    @Test
    @DisplayName("Update CreditorsAfterOneYear Resource")
    void updateCreditorsAfterOneYearResource() {
        setupCreditorsAfterOneYearResourceHandler();

        when(creditorsAfterOneYearResourceHandler.update(URI, creditorsAfterOneYearApi)).thenReturn(creditorsAfterOneYearUpdate);

        Executor<ApiResponse<Void>> updatedCreditorsAfterOneYear = creditorsAfterOneYearHandler.update(apiClient, URI, creditorsAfterOneYearApi);

        assertNotNull(updatedCreditorsAfterOneYear);
        assertEquals(updatedCreditorsAfterOneYear, creditorsAfterOneYearUpdate);
        verify(creditorsAfterOneYearResourceHandler).update(URI, creditorsAfterOneYearApi);
    }

    @Test
    @DisplayName("Create CreditorsAfterOneYear Resource")
    void createCreditorsAfterOneYearResource() {
        setupCreditorsAfterOneYearResourceHandler();

        when(creditorsAfterOneYearResourceHandler.create(URI, creditorsAfterOneYearApi)).thenReturn(creditorsAfterOneYearCreate);

        Executor<ApiResponse<CreditorsAfterOneYearApi>> createCreditorsAfterOneYearApi = creditorsAfterOneYearHandler.create(apiClient, URI, creditorsAfterOneYearApi);

        assertNotNull(createCreditorsAfterOneYearApi);
        assertEquals(createCreditorsAfterOneYearApi, creditorsAfterOneYearCreate);
        verify(creditorsAfterOneYearResourceHandler).create(URI, creditorsAfterOneYearApi);
    }

    @Test
    @DisplayName("Delete CreditorsAfterOneYear Resource")
    void deleteCreditorsAfterOneYearResource() {
        setupCreditorsAfterOneYearResourceHandler();

        when(creditorsAfterOneYearResourceHandler.delete(URI)).thenReturn(creditorsAfterOneYearDelete);

        Executor<ApiResponse<Void>> deleteCreditorsAfterOneYearApi = creditorsAfterOneYearHandler.delete(apiClient, URI);

        assertNotNull(deleteCreditorsAfterOneYearApi);
        assertEquals(deleteCreditorsAfterOneYearApi, creditorsAfterOneYearDelete);
        verify(creditorsAfterOneYearResourceHandler).delete(URI);
    }

    @Test
    @DisplayName("Test parent resource exist")
    void testParentResourceExist() throws ServiceException {
        when(smallFullService.getSmallFullAccounts(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID)).thenReturn(smallFullApi);
        when(smallFullApi.getLinks()).thenReturn(smallFullLinks);
        when(smallFullLinks.getCreditorsAfterMoreThanOneYearNote()).thenReturn(CREDITORS_AFTER_ONE_YEAR_NOTE);

        assertTrue(creditorsAfterOneYearHandler.parentResourceExists(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }

    @Test
    @DisplayName("Test parent resource throws service exception")
    void testParentResourceThrowsServiceException() throws ServiceException {
        when(smallFullService.getSmallFullAccounts(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID)).thenThrow(ServiceException.class);

        assertThrows(ServiceException.class, () -> creditorsAfterOneYearHandler.parentResourceExists(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }

    @Test
    @DisplayName("Test method returns CreditorsAfterOneYear as NoteType")
    void testCreditorsAfterOneYearReturned()  {
        assertEquals(NoteType.SMALL_FULL_CREDITORS_AFTER_ONE_YEAR, creditorsAfterOneYearHandler.getNoteType());
    }

    private void setupCreditorsAfterOneYearResourceHandler() {
        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);
        when(smallFullResourceHandler.creditorsAfterOneYear()).thenReturn(creditorsAfterOneYearResourceHandler);
    }

}