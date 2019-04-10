package uk.gov.companieshouse.web.accounts.service.smallfull.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpResponseException;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.error.ApiErrorResponseException;
import uk.gov.companieshouse.api.handler.exception.URIValidationException;
import uk.gov.companieshouse.api.handler.smallfull.SmallFullResourceHandler;
import uk.gov.companieshouse.api.handler.smallfull.creditorsafteroneyear.CreditorsAfterOneYearResourceHandler;
import uk.gov.companieshouse.api.handler.smallfull.creditorsafteroneyear.request.CreditorsAfterOneYearCreate;
import uk.gov.companieshouse.api.handler.smallfull.creditorsafteroneyear.request.CreditorsAfterOneYearDelete;
import uk.gov.companieshouse.api.handler.smallfull.creditorsafteroneyear.request.CreditorsAfterOneYearGet;
import uk.gov.companieshouse.api.handler.smallfull.creditorsafteroneyear.request.CreditorsAfterOneYearUpdate;
import uk.gov.companieshouse.api.model.accounts.smallfull.SmallFullApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.SmallFullLinks;
import uk.gov.companieshouse.api.model.accounts.smallfull.creditorsafteroneyear.CreditorsAfterOneYearApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.creditorsafteroneyear.CurrentPeriod;
import uk.gov.companieshouse.api.model.accounts.smallfull.creditorsafteroneyear.PreviousPeriod;
import uk.gov.companieshouse.web.accounts.api.ApiClientService;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.smallfull.BalanceSheet;
import uk.gov.companieshouse.web.accounts.model.smallfull.BalanceSheetHeadings;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.creditorsafteroneyear.CreditorsAfterOneYear;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.creditorsafteroneyear.OtherCreditors;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.creditorsafteroneyear.Total;
import uk.gov.companieshouse.web.accounts.service.smallfull.BalanceSheetService;
import uk.gov.companieshouse.web.accounts.service.smallfull.CreditorsAfterOneYearService;
import uk.gov.companieshouse.web.accounts.service.smallfull.SmallFullService;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.CreditorsAfterOneYearTransformer;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;
import uk.gov.companieshouse.web.accounts.validation.helper.ServiceExceptionHandler;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CreditorsAfterOneYearServiceImplTests {

    @InjectMocks
    private CreditorsAfterOneYearService creditorsAfterOneYearService =
            new CreditorsAfterOneYearServiceImpl();

    @Mock
    private CreditorsAfterOneYearTransformer mockTransformer;

    @Mock
    private CreditorsAfterOneYearResourceHandler mockCreditorsAfterOneYearResourceHandler;

    @Mock
    private BalanceSheetService mockBalanceSheetService;

    @Mock
    private BalanceSheet mockBalanceSheet;

    @Mock
    private SmallFullService smallFullService;

    @Mock
    private CreditorsAfterOneYearGet mockCreditorsAfterOneYearGet;

    @Mock
    private ApiClientService mockApiClientService;

    @Mock
    private ApiClient mockApiClient;

    @Mock
    private SmallFullResourceHandler mockSmallFullResourceHandler;

    @Mock
    private CreditorsAfterOneYearCreate mockCreditorsAfterOneYearCreate;

    @Mock
    private CreditorsAfterOneYearUpdate mockCreditorsAfterOneYearUpdate;

    @Mock
    private CreditorsAfterOneYearDelete mockCreditorsAfterOneYearDelete;

    @Mock
    private ServiceExceptionHandler serviceExceptionHandler;

    @Mock
    private List<ValidationError> mockValidationErrors;

    private static final String TRANSACTION_ID = "transactionId";

    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";

    private static final String COMPANY_NUMBER = "companyNumber";

    private static final String BASE_SMALL_FULL_URI = "/transactions/" + TRANSACTION_ID +
            "/company-accounts/" + COMPANY_ACCOUNTS_ID +
            "/small-full";

    private static final String CREDITORS_AFTER_ONE_YEAR_URI = BASE_SMALL_FULL_URI + "/notes" +
            "/creditors-after-more-than-one-year";

    private static final String RESOURCE_NAME = "creditors after one year";

    @Test
    @DisplayName("GET - Creditors After One Year successful path")
    void getCreditorsAfterOneYearSuccess() throws Exception {

        CreditorsAfterOneYearApi creditorsAfterOneYearApi = new CreditorsAfterOneYearApi();
        getMockCreditorsAfterOneYearApi(creditorsAfterOneYearApi);

        when(mockTransformer.getCreditorsAfterOneYear(creditorsAfterOneYearApi)).
                thenReturn(createCreditorsAfterOneYear());
        when(mockBalanceSheetService.getBalanceSheet(TRANSACTION_ID, COMPANY_ACCOUNTS_ID,
                COMPANY_NUMBER)).thenReturn(mockBalanceSheet);
        when(mockBalanceSheet.getBalanceSheetHeadings()).thenReturn(new BalanceSheetHeadings());

        CreditorsAfterOneYear creditorsAfterOneYear =
                creditorsAfterOneYearService.getCreditorsAfterOneYear(TRANSACTION_ID,
                        COMPANY_ACCOUNTS_ID, COMPANY_NUMBER);

        validateCreditorsAfterOneYear(creditorsAfterOneYear);
    }

    @Test
    @DisplayName("GET - Creditors After One Year successful path when http status not found")
    void getCreditorsAfterOneYearSuccessHttpStatusNotFound() throws Exception {

        HttpResponseException httpResponseException =
                new HttpResponseException.Builder(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.getReasonPhrase(), new HttpHeaders())
                        .build();
        ApiErrorResponseException apiErrorResponseException =
                ApiErrorResponseException.fromHttpResponseException(httpResponseException);

        mockCreditorsAfterOneYearResourceHandler();
        when(mockCreditorsAfterOneYearResourceHandler.get(CREDITORS_AFTER_ONE_YEAR_URI)).
                thenReturn(mockCreditorsAfterOneYearGet);
        when(mockCreditorsAfterOneYearGet.execute()).thenThrow(apiErrorResponseException);

        doNothing()
                .when(serviceExceptionHandler)
                .handleRetrievalException(apiErrorResponseException, RESOURCE_NAME);

        when(mockBalanceSheetService.getBalanceSheet(TRANSACTION_ID, COMPANY_ACCOUNTS_ID,
                COMPANY_NUMBER)).thenReturn(mockBalanceSheet);

        when(mockTransformer.getCreditorsAfterOneYear(null))
                .thenReturn(createCreditorsAfterOneYear());

        CreditorsAfterOneYear creditorsAfterOneYear =
                creditorsAfterOneYearService.getCreditorsAfterOneYear(TRANSACTION_ID,
                        COMPANY_ACCOUNTS_ID, COMPANY_NUMBER);

        validateCreditorsAfterOneYear(creditorsAfterOneYear);
    }

    private void validateCreditorsAfterOneYear(CreditorsAfterOneYear creditorsAfterOneYear) {
        assertNotNull(creditorsAfterOneYear);
        assertNotNull(creditorsAfterOneYear.getOtherCreditors());
        assertNotNull(creditorsAfterOneYear.getOtherCreditors().getCurrentOtherCreditors());
        assertNotNull(creditorsAfterOneYear.getTotal());
        assertNotNull(creditorsAfterOneYear.getTotal().getCurrentTotal());
    }

    @Test
    @DisplayName("GET - Creditors After One Year throws ServiceException due to " +
            "ApiErrorResponseException - 400 Bad Request")
    void getCreditorsAfterOneYearApiResponseException() throws Exception {

        HttpResponseException httpResponseException =
                new HttpResponseException.Builder(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), new HttpHeaders())
                        .build();
        ApiErrorResponseException apiErrorResponseException =
                ApiErrorResponseException.fromHttpResponseException(httpResponseException);

        mockCreditorsAfterOneYearResourceHandler();
        when(mockCreditorsAfterOneYearResourceHandler.get(CREDITORS_AFTER_ONE_YEAR_URI))
                .thenReturn(mockCreditorsAfterOneYearGet);
        when(mockCreditorsAfterOneYearGet.execute()).thenThrow(apiErrorResponseException);

        doThrow(ServiceException.class)
                .when(serviceExceptionHandler)
                .handleRetrievalException(apiErrorResponseException, RESOURCE_NAME);

        assertThrows(ServiceException.class,
                () -> creditorsAfterOneYearService.getCreditorsAfterOneYear(
                        TRANSACTION_ID,
                        COMPANY_ACCOUNTS_ID,
                        COMPANY_NUMBER));
    }

    @Test
    @DisplayName("GET - Creditors After One Year throws ServiceExcepiton due to " +
            "URIValidationException")
    void getCreditorsAfterOneYearURIValidationException() throws Exception {

        mockCreditorsAfterOneYearResourceHandler();
        when(mockCreditorsAfterOneYearResourceHandler.get(CREDITORS_AFTER_ONE_YEAR_URI))
                .thenReturn(mockCreditorsAfterOneYearGet);

        URIValidationException uriValidationException = new URIValidationException("invalid uri");

        when(mockCreditorsAfterOneYearGet.execute()).thenThrow(uriValidationException);

        doThrow(ServiceException.class)
                .when(serviceExceptionHandler)
                .handleURIValidationException(uriValidationException, RESOURCE_NAME);

        assertThrows(ServiceException.class,
                () -> creditorsAfterOneYearService.getCreditorsAfterOneYear(
                        TRANSACTION_ID,
                        COMPANY_ACCOUNTS_ID,
                        COMPANY_NUMBER));
    }

    @Test
    @DisplayName("POST - Creditors After One Year successful path")
    void postCreditorsAfterOneYearSuccess() throws Exception {

        CreditorsAfterOneYear creditorsAfterOneYear = createCreditorsAfterOneYear();
        CreditorsAfterOneYearApi creditorsAfterOneYearApi = createCreditorsAfterOneYearApi();

        SmallFullApi smallFullApi = new SmallFullApi();
        when(smallFullService.getSmallFullAccounts(mockApiClient, TRANSACTION_ID,
                COMPANY_ACCOUNTS_ID)).thenReturn(smallFullApi);
        setLinksWithoutCreditorsAfterOneYear(smallFullApi);

        when(mockTransformer.getCreditorsAfterOneYearApi(creditorsAfterOneYear)).thenReturn(creditorsAfterOneYearApi);

        creditorsAfterOneYearCreate(creditorsAfterOneYearApi);

        List<ValidationError> validationErrors =
                creditorsAfterOneYearService.submitCreditorsAfterOneYear(TRANSACTION_ID,
                COMPANY_ACCOUNTS_ID, creditorsAfterOneYear);

        assertTrue(validationErrors.isEmpty());
    }

    @Test
    @DisplayName("POST - Creditors After One Year throws ServiceException due to " +
            "ApiErrorResponseException - 404 Not Found")
    void postCreditorsAfterOneYearApiErrorResponseExceptionNotFound() throws Exception {

        getMockCreditorsAfterOneYearResourceHandler();

        CreditorsAfterOneYear creditorsAfterOneYear = createCreditorsAfterOneYear();
        CreditorsAfterOneYearApi creditorsAfterOneYearApi = createCreditorsAfterOneYearApi();

        SmallFullApi smallFullApi = new SmallFullApi();
        setLinksWithoutCreditorsAfterOneYear(smallFullApi);

        when(smallFullService.getSmallFullAccounts(mockApiClient, TRANSACTION_ID,
                COMPANY_ACCOUNTS_ID)).thenReturn(smallFullApi);

        when(mockTransformer.getCreditorsAfterOneYearApi(creditorsAfterOneYear)).thenReturn(creditorsAfterOneYearApi);
        when(mockCreditorsAfterOneYearResourceHandler.create(CREDITORS_AFTER_ONE_YEAR_URI,
                creditorsAfterOneYearApi)).thenReturn(mockCreditorsAfterOneYearCreate);

        HttpResponseException httpResponseException =
                new HttpResponseException.Builder(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.getReasonPhrase(), new HttpHeaders())
                        .build();
        ApiErrorResponseException apiErrorResponseException =
                ApiErrorResponseException.fromHttpResponseException(httpResponseException);

        when(mockCreditorsAfterOneYearCreate.execute()).thenThrow(apiErrorResponseException);

        when(serviceExceptionHandler.handleSubmissionException(apiErrorResponseException, RESOURCE_NAME))
                .thenThrow(ServiceException.class);

        assertThrows(ServiceException.class,
                () -> creditorsAfterOneYearService.submitCreditorsAfterOneYear(
                TRANSACTION_ID,
                COMPANY_ACCOUNTS_ID,
                creditorsAfterOneYear));
    }

    @Test
    @DisplayName("POST - Creditors After One Year throws ServiceExcepiton due to " +
            "ApiErrorResponseException - 400 Bad Request")
    void postCreditorsAfterOneYearApiErrorResponseExceptionBadRequest() throws Exception {

        getMockCreditorsAfterOneYearResourceHandler();

        CreditorsAfterOneYear creditorsAfterOneYear = createCreditorsAfterOneYear();
        CreditorsAfterOneYearApi creditorsAfterOneYearApi = createCreditorsAfterOneYearApi();

        SmallFullApi smallFullApi = new SmallFullApi();
        setLinksWithoutCreditorsAfterOneYear(smallFullApi);

        when(smallFullService.getSmallFullAccounts(mockApiClient, TRANSACTION_ID,
                COMPANY_ACCOUNTS_ID)).thenReturn(smallFullApi);

        when(mockTransformer.getCreditorsAfterOneYearApi(creditorsAfterOneYear)).thenReturn(creditorsAfterOneYearApi);
        when(mockCreditorsAfterOneYearResourceHandler.create(CREDITORS_AFTER_ONE_YEAR_URI,
                creditorsAfterOneYearApi)).thenReturn(mockCreditorsAfterOneYearCreate);

        HttpResponseException httpResponseException =
                new HttpResponseException.Builder(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), new HttpHeaders())
                        .build();
        ApiErrorResponseException apiErrorResponseException =
                ApiErrorResponseException.fromHttpResponseException(httpResponseException);

        when(mockCreditorsAfterOneYearCreate.execute()).thenThrow(apiErrorResponseException);

        when(serviceExceptionHandler.handleSubmissionException(apiErrorResponseException, RESOURCE_NAME))
                .thenReturn(mockValidationErrors);
        
        List<ValidationError> validationErrors =
                creditorsAfterOneYearService.submitCreditorsAfterOneYear(
                TRANSACTION_ID,
                COMPANY_ACCOUNTS_ID,
                creditorsAfterOneYear);

        assertEquals(mockValidationErrors, validationErrors);
    }

    @Test
    @DisplayName("POST - Creditors After One Year throws ServiceExcepiton getting Smallfull data")
    void postCreditorsAfterOneYearGetSmallFullDataApiResponseException() throws Exception {

        CreditorsAfterOneYear creditorsAfterOneYear = createCreditorsAfterOneYear();

        when(mockApiClientService.getApiClient()).thenReturn(mockApiClient);
        when(smallFullService.getSmallFullAccounts(mockApiClient, TRANSACTION_ID,
                COMPANY_ACCOUNTS_ID)).thenThrow(ServiceException.class);

        assertThrows(ServiceException.class,
                () -> creditorsAfterOneYearService.submitCreditorsAfterOneYear(
                TRANSACTION_ID,
                COMPANY_ACCOUNTS_ID,
                creditorsAfterOneYear));
    }

    @Test
    @DisplayName("PUT - Creditors After One Year successful path")
    void putCreditorsAfterOneYearSuccess() throws Exception {

        CreditorsAfterOneYear creditorsAfterOneYear = createCreditorsAfterOneYear();
        CreditorsAfterOneYearApi creditorsAfterOneYearApi = createCreditorsAfterOneYearApi();

        SmallFullApi smallFullApi = new SmallFullApi();
        setLinksWithoutCreditorsAfterOneYear(smallFullApi);
        setLinksWithCreditorsAfterOneYear(smallFullApi);

        when(smallFullService.getSmallFullAccounts(mockApiClient, TRANSACTION_ID,
                COMPANY_ACCOUNTS_ID)).thenReturn(smallFullApi);

        when(mockTransformer.getCreditorsAfterOneYearApi(creditorsAfterOneYear)).thenReturn(creditorsAfterOneYearApi);
        creditorsAfterOneYearUpdate(creditorsAfterOneYearApi);

        List<ValidationError> validationErrors =
                creditorsAfterOneYearService.submitCreditorsAfterOneYear(TRANSACTION_ID,
                COMPANY_ACCOUNTS_ID, creditorsAfterOneYear);

        assertEquals(0, validationErrors.size());
    }

    @Test
    @DisplayName("PUT - Creditors After One Year throws ServiceException due to " +
            "URIValidationException")
    void putCreditorsAfterOneYearURIValidationException() throws Exception {

        CreditorsAfterOneYear creditorsAfterOneYear = createCreditorsAfterOneYear();
        CreditorsAfterOneYearApi creditorsAfterOneYearApi = createCreditorsAfterOneYearApi();

        SmallFullApi smallFullApi = new SmallFullApi();
        setLinksWithCreditorsAfterOneYear(smallFullApi);

        when(smallFullService.getSmallFullAccounts(mockApiClient, TRANSACTION_ID,
                COMPANY_ACCOUNTS_ID)).thenReturn(smallFullApi);

        when(mockTransformer.getCreditorsAfterOneYearApi(creditorsAfterOneYear)).thenReturn(creditorsAfterOneYearApi);

        getMockCreditorsAfterOneYearResourceHandler();
        when(mockCreditorsAfterOneYearResourceHandler.update(CREDITORS_AFTER_ONE_YEAR_URI,
                creditorsAfterOneYearApi)).thenReturn(mockCreditorsAfterOneYearUpdate);

        URIValidationException uriValidationException = new URIValidationException("invalid uri");

        when(mockCreditorsAfterOneYearUpdate.execute()).thenThrow(uriValidationException);

        doThrow(ServiceException.class)
                .when(serviceExceptionHandler)
                .handleURIValidationException(uriValidationException, RESOURCE_NAME);

        assertThrows(ServiceException.class, () -> creditorsAfterOneYearService.submitCreditorsAfterOneYear(
                TRANSACTION_ID,
                COMPANY_ACCOUNTS_ID,
                creditorsAfterOneYear));
    }

    @Test
    @DisplayName("DELETE - Creditors after more than one year successful delete path")
    void deleteCreditorsAfterOneYear() throws Exception {

        getMockCreditorsAfterOneYearResourceHandler();
        when(mockCreditorsAfterOneYearResourceHandler.delete(CREDITORS_AFTER_ONE_YEAR_URI)).thenReturn(mockCreditorsAfterOneYearDelete);
        doNothing().when(mockCreditorsAfterOneYearDelete).execute();

        creditorsAfterOneYearService.deleteCreditorsAfterOneYear(TRANSACTION_ID, COMPANY_ACCOUNTS_ID);

        verify(mockCreditorsAfterOneYearDelete, times(1)).execute();
    }

    @Test
    @DisplayName("DELETE - Creditors after more than one year throws ServiceExcepiton due to URIValidationException")
    void deleteCreditorsAfterOneYearUriValidationException() throws Exception {

        getMockCreditorsAfterOneYearResourceHandler();
        when(mockCreditorsAfterOneYearResourceHandler.delete(CREDITORS_AFTER_ONE_YEAR_URI)).thenReturn(mockCreditorsAfterOneYearDelete);

        URIValidationException uriValidationException = new URIValidationException("invalid uri");

        when(mockCreditorsAfterOneYearDelete.execute()).thenThrow(uriValidationException);

        doThrow(ServiceException.class)
                .when(serviceExceptionHandler)
                .handleURIValidationException(uriValidationException, RESOURCE_NAME);

        assertThrows(ServiceException.class, () -> creditorsAfterOneYearService.deleteCreditorsAfterOneYear(
            TRANSACTION_ID,
            COMPANY_ACCOUNTS_ID));
    }

    @Test
    @DisplayName("DELETE - Creditors after more than one year throws ServiceExcepiton due to ApiErrorResponseException - 404 Not Found")
    void deleteCreditorsAfterOneYearApiErrorResponseExceptionNotFound() throws Exception {

        getMockCreditorsAfterOneYearResourceHandler();
        when(mockCreditorsAfterOneYearResourceHandler.delete(CREDITORS_AFTER_ONE_YEAR_URI)).thenReturn(mockCreditorsAfterOneYearDelete);

        HttpResponseException httpResponseException =
                new HttpResponseException.Builder(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.getReasonPhrase(), new HttpHeaders())
                        .build();
        ApiErrorResponseException apiErrorResponseException =
                ApiErrorResponseException.fromHttpResponseException(httpResponseException);

        when(mockCreditorsAfterOneYearDelete.execute()).thenThrow(apiErrorResponseException);

        doThrow(ServiceException.class)
                .when(serviceExceptionHandler)
                .handleDeletionException(apiErrorResponseException, RESOURCE_NAME);

        assertThrows(ServiceException.class, () -> creditorsAfterOneYearService.deleteCreditorsAfterOneYear(
            TRANSACTION_ID,
            COMPANY_ACCOUNTS_ID));
    }

    private void creditorsAfterOneYearUpdate(CreditorsAfterOneYearApi creditorsAfterOneYearApi) throws Exception {
        getMockCreditorsAfterOneYearResourceHandler();
        when(mockCreditorsAfterOneYearResourceHandler.update(CREDITORS_AFTER_ONE_YEAR_URI,
                creditorsAfterOneYearApi)).thenReturn(mockCreditorsAfterOneYearUpdate);
        doNothing().when(mockCreditorsAfterOneYearUpdate).execute();
    }

    private void creditorsAfterOneYearCreate(CreditorsAfterOneYearApi creditorsAfterOneYearApi) throws Exception {
        getMockCreditorsAfterOneYearResourceHandler();
        when(mockCreditorsAfterOneYearResourceHandler.create(CREDITORS_AFTER_ONE_YEAR_URI,
                creditorsAfterOneYearApi)).thenReturn(mockCreditorsAfterOneYearCreate);
        when(mockCreditorsAfterOneYearCreate.execute()).thenReturn(creditorsAfterOneYearApi);
    }

    private void getMockCreditorsAfterOneYearResourceHandler() throws Exception {
        getMockSmallFullResourceHandler();
        when(mockSmallFullResourceHandler.creditorsAfterOneYear()).thenReturn(mockCreditorsAfterOneYearResourceHandler);
    }

    private void getMockSmallFullResourceHandler() {
        when(mockApiClientService.getApiClient()).thenReturn(mockApiClient);
        when(mockApiClient.smallFull()).thenReturn(mockSmallFullResourceHandler);
    }


    private void setLinksWithoutCreditorsAfterOneYear(SmallFullApi smallFullApi) {
        SmallFullLinks links = new SmallFullLinks();
        links.setDebtorsNote("");
        smallFullApi.setLinks(links);
    }

    private void setLinksWithCreditorsAfterOneYear(SmallFullApi smallFullApi) {
        SmallFullLinks links = new SmallFullLinks();
        links.setCreditorsAfterMoreThanOneYearNote("");
        smallFullApi.setLinks(links);
    }

    private CreditorsAfterOneYearApi createCreditorsAfterOneYearApi() {
        CreditorsAfterOneYearApi creditorsAfterOneYearApi = new CreditorsAfterOneYearApi();
        CurrentPeriod currentPeriod = new CurrentPeriod();
        PreviousPeriod previousPeriod = new PreviousPeriod();

        currentPeriod.setOtherCreditors(5L);
        currentPeriod.setTotal(5L);

        previousPeriod.setOtherCreditors(5L);
        previousPeriod.setTotal(5L);

        creditorsAfterOneYearApi.setCurrentPeriod(currentPeriod);
        creditorsAfterOneYearApi.setPreviousPeriod(previousPeriod);

        return creditorsAfterOneYearApi;
    }

    private void getMockCreditorsAfterOneYearApi(CreditorsAfterOneYearApi creditorsAfterOneYearApi) throws Exception {
        mockCreditorsAfterOneYearResourceHandler();
        when(mockCreditorsAfterOneYearResourceHandler.get(CREDITORS_AFTER_ONE_YEAR_URI))
                .thenReturn(mockCreditorsAfterOneYearGet);
        when(mockCreditorsAfterOneYearGet.execute()).thenReturn(creditorsAfterOneYearApi);
    }

    private CreditorsAfterOneYear createCreditorsAfterOneYear() {
        CreditorsAfterOneYear creditorsAfterOneYear = new CreditorsAfterOneYear();
        OtherCreditors otherCreditors = new OtherCreditors();
        Total total = new Total();

        BalanceSheetHeadings balanceSheetHeadings = new BalanceSheetHeadings();

        otherCreditors.setCurrentOtherCreditors(5L);
        otherCreditors.setPreviousOtherCreditors(5L);
        total.setCurrentTotal(5L);
        total.setPreviousTotal(5L);

        balanceSheetHeadings.setCurrentPeriodHeading("");
        balanceSheetHeadings.setPreviousPeriodHeading("");

        creditorsAfterOneYear.setOtherCreditors(otherCreditors);
        creditorsAfterOneYear.setTotal(total);
        creditorsAfterOneYear.setBalanceSheetHeadings(balanceSheetHeadings);

        return creditorsAfterOneYear;
    }

    private void mockCreditorsAfterOneYearResourceHandler() {
        mockSmallFullResourceHandler();
        when(mockSmallFullResourceHandler.creditorsAfterOneYear())
                .thenReturn(mockCreditorsAfterOneYearResourceHandler);
    }

    private void mockSmallFullResourceHandler() {
        when(mockApiClientService.getApiClient()).thenReturn(mockApiClient);
        when(mockApiClient.smallFull()).thenReturn(mockSmallFullResourceHandler);
    }
}
