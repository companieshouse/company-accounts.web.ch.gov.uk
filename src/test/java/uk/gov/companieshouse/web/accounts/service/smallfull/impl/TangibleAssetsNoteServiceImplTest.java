package uk.gov.companieshouse.web.accounts.service.smallfull.impl;

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
import uk.gov.companieshouse.api.handler.exception.URIValidationException;
import uk.gov.companieshouse.api.handler.smallfull.SmallFullResourceHandler;
import uk.gov.companieshouse.api.handler.smallfull.tangible.TangibleResourceHandler;
import uk.gov.companieshouse.api.handler.smallfull.tangible.request.TangibleCreate;
import uk.gov.companieshouse.api.handler.smallfull.tangible.request.TangibleDelete;
import uk.gov.companieshouse.api.handler.smallfull.tangible.request.TangibleGet;
import uk.gov.companieshouse.api.handler.smallfull.tangible.request.TangibleUpdate;
import uk.gov.companieshouse.api.model.ApiResponse;
import uk.gov.companieshouse.api.model.accounts.smallfull.AccountingPeriodApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.SmallFullApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.SmallFullLinks;
import uk.gov.companieshouse.api.model.accounts.smallfull.tangible.TangibleApi;
import uk.gov.companieshouse.api.model.company.CompanyProfileApi;
import uk.gov.companieshouse.api.model.company.account.CompanyAccountApi;
import uk.gov.companieshouse.api.model.company.account.LastAccountsApi;
import uk.gov.companieshouse.api.model.company.account.NextAccountsApi;
import uk.gov.companieshouse.web.accounts.api.ApiClientService;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.tangible.TangibleAssets;
import uk.gov.companieshouse.web.accounts.service.smallfull.SmallFullService;
import uk.gov.companieshouse.web.accounts.service.smallfull.TangibleAssetsNoteService;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.tangible.TangibleAssetsTransformer;
import uk.gov.companieshouse.web.accounts.util.ValidationContext;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;
import uk.gov.companieshouse.web.accounts.validation.helper.ServiceExceptionHandler;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TangibleAssetsNoteServiceImplTest {

    @Mock
    private ApiClientService apiClientService;

    @Mock
    private ApiClient apiClient;

    @Mock
    private TangibleApi tangibleApi;

    @Mock
    private TangibleAssets tangibleAssets;

    @Mock
    private SmallFullResourceHandler smallFullResourceHandler;

    @Mock
    private TangibleResourceHandler tangibleResourceHandler;

    @Mock
    private TangibleAssetsTransformer tangibleAssetsTransformer;

    @Mock
    private TangibleGet tangibleGet;

    @Mock
    private TangibleCreate tangibleCreate;

    @Mock
    private TangibleUpdate tangibleUpdate;

    @Mock
    private TangibleDelete tangibleDelete;

    @Mock
    private SmallFullApi smallFullApi;

    @Mock
    private SmallFullService smallFullService;

    @Mock
    private AccountingPeriodApi accountingPeriodApi;

    @Mock
    private AccountingPeriodApi lastAccountingPeriodApi;

    @Mock
    private SmallFullLinks smallFullLinks;

    @Mock
    private ServiceExceptionHandler serviceExceptionHandler;

    @Mock
    private ValidationContext validationContext;

    @Mock
    private ApiResponse<TangibleApi> responseWithData;

    @Mock
    private ApiResponse<Void> responseNoData;

    @Mock
    private List<ValidationError> mockValidationErrors;

    @Mock
    private ApiErrorResponseException apiErrorResponseException;

    @Mock
    private URIValidationException uriValidationException;

    @InjectMocks
    private TangibleAssetsNoteService tangibleAssetsNoteService = new TangibleAssetsNoteServiceImpl();

    private static final String TRANSACTION_ID = "transactionId";

    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";

    private static final String COMPANY_NUMBER = "companyNumber";

    private static final String TANGIBLE_URI = "/transactions/" + TRANSACTION_ID + "/company-accounts/" +
                                                    COMPANY_ACCOUNTS_ID + "/small-full/notes/tangible-assets";

    private static final LocalDate LAST_PERIOD_END_ON = LocalDate.parse("2017-06-30");

    private static final LocalDate NEXT_PERIOD_START_ON = LocalDate.parse("2017-07-01");

    private static final LocalDate NEXT_PERIOD_END_ON = LocalDate.parse("2018-06-30");

    private static final String RESOURCE_NAME = "tangible assets";

    @BeforeEach
    private void setUp() {
        when(apiClientService.getApiClient()).thenReturn(apiClient);
        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);
        when(smallFullResourceHandler.tangible()).thenReturn(tangibleResourceHandler);
    }

    @Test
    @DisplayName("GET - Tangible successful path")
    void getTangibleSuccess() throws Exception {

        when(tangibleResourceHandler.get(TANGIBLE_URI)).thenReturn(tangibleGet);

        when(tangibleGet.execute()).thenReturn(responseWithData);
        when(responseWithData.getData()).thenReturn(tangibleApi);

        when(tangibleAssetsTransformer.getTangibleAssets(tangibleApi)).thenReturn(new TangibleAssets());

        smallFullServiceAccountsDate();

        TangibleAssets testResult = tangibleAssetsNoteService
            .getTangibleAssets(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, COMPANY_NUMBER);

        assertNotNull(testResult);
        assertCompanyDatesSetOnTangibleAssets(testResult);
    }

    @Test
    @DisplayName("GET - Tangible successful path when http status not found")
    void getTangibleSuccessHttpStatusNotFound() throws Exception {

        when(tangibleResourceHandler.get(TANGIBLE_URI)).thenReturn(tangibleGet);

        when(tangibleGet.execute()).thenThrow(apiErrorResponseException);

        doNothing()
                .when(serviceExceptionHandler)
                        .handleRetrievalException(apiErrorResponseException, RESOURCE_NAME);

        smallFullServiceAccountsDate();

        TangibleAssets testResult = tangibleAssetsNoteService
            .getTangibleAssets(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, COMPANY_NUMBER);

        assertNotNull(testResult);
        assertCompanyDatesSetOnTangibleAssets(testResult);
    }

    @Test
    @DisplayName("GET - Tangible ApiErrorResponseException is thrown")
    void getTangibleApiErrorResponseException() throws Exception {

        when(tangibleResourceHandler.get(TANGIBLE_URI)).thenReturn(tangibleGet);

        when(tangibleGet.execute()).thenThrow(apiErrorResponseException);

        doThrow(ServiceException.class)
                .when(serviceExceptionHandler)
                        .handleRetrievalException(apiErrorResponseException, RESOURCE_NAME);

        assertThrows(ServiceException.class, () -> tangibleAssetsNoteService.getTangibleAssets(
            TRANSACTION_ID,
            COMPANY_ACCOUNTS_ID,
            COMPANY_NUMBER));
    }

    @Test
    @DisplayName("GET - Tangible URIValidationException is thrown")
    void getTangibleURIValidationException() throws Exception {

        when(tangibleResourceHandler.get(TANGIBLE_URI)).thenReturn(tangibleGet);

        when(tangibleGet.execute()).thenThrow(uriValidationException);

        doThrow(ServiceException.class)
                .when(serviceExceptionHandler)
                        .handleURIValidationException(uriValidationException, RESOURCE_NAME);

        assertThrows(ServiceException.class, () -> tangibleAssetsNoteService.getTangibleAssets(
                TRANSACTION_ID,
                COMPANY_ACCOUNTS_ID,
                COMPANY_NUMBER));
    }

    @Test
    @DisplayName("POST - Tangible successful path")
    void postTangibleSuccess() throws Exception {

        when(smallFullService.getSmallFullAccounts(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
            .thenReturn(smallFullApi);

        when(smallFullApi.getLinks()).thenReturn(smallFullLinks);
        when(smallFullLinks.getTangibleAssetsNote()).thenReturn(null);

        when(tangibleAssetsTransformer.getTangibleApi(tangibleAssets)).thenReturn(tangibleApi);

        when(tangibleResourceHandler.create(TANGIBLE_URI, tangibleApi)).thenReturn(tangibleCreate);

        when(tangibleCreate.execute()).thenReturn(responseWithData);

        when(responseWithData.hasErrors()).thenReturn(false);

        List<ValidationError> validationErrors = tangibleAssetsNoteService
            .postTangibleAssets(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, tangibleAssets,
                COMPANY_NUMBER);

        assertTrue(validationErrors.isEmpty());
    }

    @Test
    @DisplayName("POST - Tangible with validation errors")
    void postTangibleWithValidationErrors() throws Exception {

        when(smallFullService.getSmallFullAccounts(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(smallFullApi);

        when(smallFullApi.getLinks()).thenReturn(smallFullLinks);
        when(smallFullLinks.getTangibleAssetsNote()).thenReturn(null);

        when(tangibleAssetsTransformer.getTangibleApi(tangibleAssets)).thenReturn(tangibleApi);

        when(tangibleResourceHandler.create(TANGIBLE_URI, tangibleApi)).thenReturn(tangibleCreate);

        when(tangibleCreate.execute()).thenReturn(responseWithData);

        when(responseWithData.hasErrors()).thenReturn(true);

        when(validationContext.getValidationErrors(responseWithData.getErrors()))
                .thenReturn(mockValidationErrors);

        List<ValidationError> validationErrors = tangibleAssetsNoteService
                .postTangibleAssets(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, tangibleAssets,
                        COMPANY_NUMBER);

        assertEquals(mockValidationErrors, validationErrors);
    }

    @Test
    @DisplayName("PUT - Tangible successful path")
    void putTangibleSuccess() throws Exception {

        when(smallFullService.getSmallFullAccounts(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
            .thenReturn(smallFullApi);

        when(smallFullApi.getLinks()).thenReturn(smallFullLinks);
        when(smallFullLinks.getTangibleAssetsNote()).thenReturn("");

        when(tangibleAssetsTransformer.getTangibleApi(tangibleAssets)).thenReturn(tangibleApi);

        when(tangibleResourceHandler.update(TANGIBLE_URI, tangibleApi)).thenReturn(tangibleUpdate);

        when(tangibleUpdate.execute()).thenReturn(responseNoData);

        when(responseNoData.hasErrors()).thenReturn(false);

        List<ValidationError> validationErrors = tangibleAssetsNoteService
            .postTangibleAssets(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, tangibleAssets,
                COMPANY_NUMBER);

        assertTrue(validationErrors.isEmpty());
    }

    @Test
    @DisplayName("PUT - Tangible with validation errors")
    void putTangibleWithValidationErrors() throws Exception {

        when(smallFullService.getSmallFullAccounts(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(smallFullApi);

        when(smallFullApi.getLinks()).thenReturn(smallFullLinks);
        when(smallFullLinks.getTangibleAssetsNote()).thenReturn("");

        when(tangibleAssetsTransformer.getTangibleApi(tangibleAssets)).thenReturn(tangibleApi);

        when(tangibleResourceHandler.update(TANGIBLE_URI, tangibleApi)).thenReturn(tangibleUpdate);

        when(tangibleUpdate.execute()).thenReturn(responseNoData);

        when(responseNoData.hasErrors()).thenReturn(true);

        when(validationContext.getValidationErrors(responseNoData.getErrors()))
                .thenReturn(mockValidationErrors);

        List<ValidationError> validationErrors = tangibleAssetsNoteService
                .postTangibleAssets(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, tangibleAssets,
                        COMPANY_NUMBER);

        assertEquals(mockValidationErrors, validationErrors);
    }

    @Test
    @DisplayName("DELETE - Tangible successful path")
    void deleteTangibleSuccess() {

        when(tangibleResourceHandler.delete(TANGIBLE_URI)).thenReturn(tangibleDelete);

        assertAll(() -> tangibleAssetsNoteService
                .deleteTangibleAssets(TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }

    @Test
    @DisplayName("DELETE - Tangible failure path due to a thrown ApiErrorResponseException")
    void deleteTangibleApiErrorResponseException() throws Exception {

        when(tangibleResourceHandler.delete(TANGIBLE_URI)).thenReturn(tangibleDelete);

        when(tangibleDelete.execute()).thenThrow(apiErrorResponseException);

        doThrow(ServiceException.class)
                .when(serviceExceptionHandler)
                        .handleDeletionException(apiErrorResponseException, RESOURCE_NAME);

        assertThrows(ServiceException.class, () -> tangibleAssetsNoteService
                .deleteTangibleAssets(TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }

    @Test
    @DisplayName("DELETE - Tangible failure path due to a thrown URIValidationException")
    void deleteTangibleURIValidationException() throws Exception {

        when(tangibleResourceHandler.delete(TANGIBLE_URI)).thenReturn(tangibleDelete);

        when(tangibleDelete.execute()).thenThrow(uriValidationException);

        doThrow(ServiceException.class)
                .when(serviceExceptionHandler)
                        .handleURIValidationException(uriValidationException, RESOURCE_NAME);

        assertThrows(ServiceException.class, () -> tangibleAssetsNoteService
                .deleteTangibleAssets(TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }

    @Test
    @DisplayName("CREATE - Tangible failure path due to thrown URIValidationException")
    void createTangibleURIValidationException() throws Exception {

        when(smallFullService.getSmallFullAccounts(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(smallFullApi);

        when(smallFullApi.getLinks()).thenReturn(smallFullLinks);

        when(tangibleAssetsTransformer.getTangibleApi(tangibleAssets)).thenReturn(tangibleApi);

        when(tangibleResourceHandler.create(TANGIBLE_URI, tangibleApi)).thenReturn(tangibleCreate);

        when(tangibleCreate.execute()).thenThrow(uriValidationException);

        doThrow(ServiceException.class)
                .when(serviceExceptionHandler)
                .handleURIValidationException(uriValidationException, RESOURCE_NAME);

        assertThrows(ServiceException.class, () -> tangibleAssetsNoteService
                .postTangibleAssets(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, tangibleAssets, COMPANY_NUMBER));
    }

    private void assertCompanyDatesSetOnTangibleAssets(TangibleAssets tangibleAssets) {

        assertEquals(NEXT_PERIOD_START_ON, tangibleAssets.getNextAccountsPeriodStartOn());
        assertEquals(NEXT_PERIOD_END_ON, tangibleAssets.getNextAccountsPeriodEndOn());
        assertEquals(LAST_PERIOD_END_ON, tangibleAssets.getLastAccountsPeriodEndOn());
    }

    private CompanyProfileApi getCompanyProfile() {

        NextAccountsApi nextAccounts = new NextAccountsApi();
        nextAccounts.setPeriodStartOn(NEXT_PERIOD_START_ON);
        nextAccounts.setPeriodEndOn(NEXT_PERIOD_END_ON);

        LastAccountsApi lastAccounts = new LastAccountsApi();
        lastAccounts.setPeriodEndOn(LAST_PERIOD_END_ON);

        CompanyAccountApi companyAccounts = new CompanyAccountApi();
        companyAccounts.setNextAccounts(nextAccounts);
        companyAccounts.setLastAccounts(lastAccounts);

        CompanyProfileApi companyProfile = new CompanyProfileApi();
        companyProfile.setAccounts(companyAccounts);

        return companyProfile;
    }

    private void smallFullServiceAccountsDate() throws ServiceException {
        when(smallFullService.getSmallFullAccounts(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID)).thenReturn(smallFullApi);
        when(smallFullApi.getNextAccounts()).thenReturn(accountingPeriodApi);
        when(accountingPeriodApi.getPeriodStartOn()).thenReturn(NEXT_PERIOD_START_ON);
        when(accountingPeriodApi.getPeriodEndOn()).thenReturn(NEXT_PERIOD_END_ON);
        when(smallFullApi.getLastAccounts()).thenReturn(lastAccountingPeriodApi);
        when(lastAccountingPeriodApi.getPeriodEndOn()).thenReturn(LAST_PERIOD_END_ON);
    }
}