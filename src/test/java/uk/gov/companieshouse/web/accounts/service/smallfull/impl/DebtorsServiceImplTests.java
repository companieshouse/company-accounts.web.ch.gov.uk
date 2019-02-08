package uk.gov.companieshouse.web.accounts.service.smallfull.impl;

import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpResponseException;
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
import uk.gov.companieshouse.api.handler.smallfull.debtors.DebtorsResourceHandler;
import uk.gov.companieshouse.api.handler.smallfull.debtors.request.DebtorsCreate;
import uk.gov.companieshouse.api.handler.smallfull.debtors.request.DebtorsDelete;
import uk.gov.companieshouse.api.handler.smallfull.debtors.request.DebtorsGet;
import uk.gov.companieshouse.api.handler.smallfull.debtors.request.DebtorsUpdate;
import uk.gov.companieshouse.api.handler.smallfull.request.SmallFullGet;
import uk.gov.companieshouse.api.model.accounts.smallfull.Debtors.CurrentPeriod;
import uk.gov.companieshouse.api.model.accounts.smallfull.Debtors.DebtorsApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.Debtors.PreviousPeriod;
import uk.gov.companieshouse.api.model.accounts.smallfull.SmallFullApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.SmallFullLinks;
import uk.gov.companieshouse.web.accounts.api.ApiClientService;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.smallfull.BalanceSheet;
import uk.gov.companieshouse.web.accounts.model.smallfull.BalanceSheetHeadings;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.debtors.Debtors;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.debtors.Total;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.debtors.TradeDebtors;
import uk.gov.companieshouse.web.accounts.service.smallfull.BalanceSheetService;
import uk.gov.companieshouse.web.accounts.service.smallfull.DebtorsService;
import uk.gov.companieshouse.web.accounts.service.smallfull.SmallFullService;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.DebtorsTransformer;
import uk.gov.companieshouse.web.accounts.util.ValidationContext;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DebtorsServiceImplTests {

    @Mock
    private ApiClientService mockApiClientService;

    @Mock
    private ApiClient mockApiClient;

    @Mock
    private SmallFullResourceHandler mockSmallFullResourceHandler;

    @Mock
    private DebtorsResourceHandler mockDebtorsResourceHandler;

    @Mock
    private DebtorsGet mockDebtorsGet;

    @Mock
    private DebtorsCreate mockDebtorsCreate;

    @Mock
    private DebtorsUpdate mockDebtorsUpdate;

    @Mock
    private DebtorsDelete mockDebtorsDelete;

    @Mock
    private SmallFullGet mockSmallFullGet;

    @Mock
    private DebtorsTransformer mockDebtorsTransformer;

    @Mock
    private ValidationContext mockValidationContext;

    @Mock
    private SmallFullService smallFullService;

    @Mock
    private BalanceSheetService mockBalanceSheetService;

    @Mock
    private BalanceSheet mockBalanceSheet;

    @InjectMocks
    private DebtorsService debtorsService = new DebtorsServiceImpl();

    private static final String TRANSACTION_ID = "transactionId";

    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";

    private static final String COMPANY_NUMBER = "companyNumber";

    private static final String BASE_SMALL_FULL_URI = "/transactions/" + TRANSACTION_ID +
        "/company-accounts/" + COMPANY_ACCOUNTS_ID +
        "/small-full";

    private static final String DEBTORS_URI = BASE_SMALL_FULL_URI + "/notes/debtors";

    @Test
    @DisplayName("GET - Debtors successful path")
    void getDebtorsSuccess() throws Exception {

        DebtorsApi debtorsApi = new DebtorsApi();
        getMockDebtorsApi(debtorsApi);

        when(mockDebtorsTransformer.getDebtors(debtorsApi)).thenReturn(createDebtors());
        when(mockBalanceSheetService.getBalanceSheet(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, COMPANY_NUMBER)).thenReturn(mockBalanceSheet);
        when(mockBalanceSheet.getBalanceSheetHeadings()).thenReturn(new BalanceSheetHeadings());

        Debtors debtors = debtorsService.getDebtors(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, COMPANY_NUMBER);

        assertNotNull(debtors);
        assertNotNull(debtors.getTradeDebtors());
        assertNotNull(debtors.getTradeDebtors().getCurrentTradeDebtors());
        assertNotNull(debtors.getTotal());
        assertNotNull(debtors.getTotal().getCurrentTotal());
    }

    @Test
    @DisplayName("GET - Debtors successful path when http status not found")
    void getDebtorsSuccessHttpStatusNotFound() throws Exception {

        HttpResponseException httpResponseException = new HttpResponseException.Builder(404,"Not Found",new HttpHeaders()).build();
        ApiErrorResponseException apiErrorResponseException = ApiErrorResponseException.fromHttpResponseException(httpResponseException);

        getMockDebtorsResourceHandler();
        when(mockDebtorsResourceHandler.get(DEBTORS_URI)).thenReturn(mockDebtorsGet);
        when(mockDebtorsGet.execute()).thenThrow(apiErrorResponseException);
        when(mockBalanceSheetService.getBalanceSheet(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, COMPANY_NUMBER)).thenReturn(mockBalanceSheet);

        when(mockDebtorsTransformer.getDebtors(null)).thenReturn(createDebtors());

        Debtors debtors = debtorsService.getDebtors(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, COMPANY_NUMBER);

        assertNotNull(debtors);
        assertNotNull(debtors.getTradeDebtors());
        assertNotNull(debtors.getTradeDebtors().getCurrentTradeDebtors());
        assertNotNull(debtors.getTotal());
        assertNotNull(debtors.getTotal().getCurrentTotal());
    }

    @Test
    @DisplayName("GET - Debtors throws ServiceExcepiton due to ApiErrorResponseException - 400 Bad Request")
    void getDebtorsApiResponseException() throws Exception {

        HttpResponseException httpResponseException = new HttpResponseException.Builder(400,"Bad Request",new HttpHeaders()).build();
        ApiErrorResponseException apiErrorResponseException = ApiErrorResponseException.fromHttpResponseException(httpResponseException);

        getMockDebtorsResourceHandler();
        when(mockDebtorsResourceHandler.get(DEBTORS_URI)).thenReturn(mockDebtorsGet);
        when(mockDebtorsGet.execute()).thenThrow(apiErrorResponseException);

        assertThrows(ApiErrorResponseException.class, () -> mockDebtorsGet.execute());
        assertThrows(ServiceException.class, () -> debtorsService.getDebtors(
            TRANSACTION_ID,
            COMPANY_ACCOUNTS_ID,
            COMPANY_NUMBER));
    }

    @Test
    @DisplayName("GET - Debtors throws ServiceExcepiton due to URIValidationException")
    void getDebtorsURIValidationException() throws Exception {

        getMockDebtorsResourceHandler();
        when(mockDebtorsResourceHandler.get(DEBTORS_URI)).thenReturn(mockDebtorsGet);
        when(mockDebtorsGet.execute()).thenThrow(URIValidationException.class);

        assertThrows(URIValidationException.class, () -> mockDebtorsGet.execute());
        assertThrows(ServiceException.class, () -> debtorsService.getDebtors(
            TRANSACTION_ID,
            COMPANY_ACCOUNTS_ID,
            COMPANY_NUMBER));
    }

    @Test
    @DisplayName("POST - Debtors successful path")
    void postDebtorsSuccess() throws Exception {

        Debtors debtors = createDebtors();
        DebtorsApi debtorsApi = createDebtorsApi();

        SmallFullApi smallFullApi = new SmallFullApi();
        when(smallFullService.getSmallFullAccounts(mockApiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID)).thenReturn(smallFullApi);
        setLinksWithoutDebtors(smallFullApi);

        when(mockDebtorsTransformer.getDebtorsApi(debtors)).thenReturn(debtorsApi);

        debtorsCreate(debtorsApi);

        List<ValidationError> validationErrors = debtorsService.submitDebtors(TRANSACTION_ID,
            COMPANY_ACCOUNTS_ID, debtors, COMPANY_NUMBER);

        assertEquals(0, validationErrors.size());
    }

    @Test
    @DisplayName("POST - Debtors throws ServiceExcepiton due to ApiErrorResponseException - 404 Not Found")
    void postDebtorsApiErrorResponseExceptionNotFound() throws Exception {

        getMockDebtorsResourceHandler();

        Debtors debtors = createDebtors();
        DebtorsApi debtorsApi = createDebtorsApi();

        SmallFullApi smallFullApi = new SmallFullApi();
        setLinksWithoutDebtors(smallFullApi);

        when(smallFullService.getSmallFullAccounts(mockApiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID)).thenReturn(smallFullApi);

        when(mockDebtorsTransformer.getDebtorsApi(debtors)).thenReturn(debtorsApi);
        when(mockDebtorsResourceHandler.create(DEBTORS_URI, debtorsApi)).thenReturn(mockDebtorsCreate);

        HttpResponseException httpResponseException = new HttpResponseException.Builder(404,"Not Found",new HttpHeaders()).build();
        ApiErrorResponseException apiErrorResponseException = ApiErrorResponseException.fromHttpResponseException(httpResponseException);
        when(mockDebtorsCreate.execute()).thenThrow(apiErrorResponseException);

        assertThrows(ApiErrorResponseException.class, () -> mockDebtorsCreate.execute());
        assertThrows(ServiceException.class, () -> debtorsService.submitDebtors(
            TRANSACTION_ID,
            COMPANY_ACCOUNTS_ID,
            debtors,
            COMPANY_NUMBER));
    }

    @Test
    @DisplayName("POST - Debtors throws ServiceExcepiton due to ApiErrorResponseException - 400 Bad Request")
    void postDebtorsApiErrorResponseExceptionBadRequest() throws Exception {

        getMockDebtorsResourceHandler();

        Debtors debtors = createDebtors();
        DebtorsApi debtorsApi = createDebtorsApi();

        SmallFullApi smallFullApi = new SmallFullApi();
        setLinksWithoutDebtors(smallFullApi);

        when(smallFullService.getSmallFullAccounts(mockApiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID)).thenReturn(smallFullApi);

        when(mockDebtorsTransformer.getDebtorsApi(debtors)).thenReturn(debtorsApi);
        when(mockDebtorsResourceHandler.create(DEBTORS_URI, debtorsApi)).thenReturn(mockDebtorsCreate);

        HttpResponseException httpResponseException = new HttpResponseException.Builder(400,"Bad Request",new HttpHeaders()).build();
        ApiErrorResponseException apiErrorResponseException = ApiErrorResponseException.fromHttpResponseException(httpResponseException);
        when(mockDebtorsCreate.execute()).thenThrow(apiErrorResponseException);

        assertThrows(ApiErrorResponseException.class, () -> mockDebtorsCreate.execute());
        assertThrows(ServiceException.class, () -> debtorsService.submitDebtors(
            TRANSACTION_ID,
            COMPANY_ACCOUNTS_ID,
            debtors,
            COMPANY_NUMBER));
    }

    @Test
    @DisplayName("POST - Debtors throws ServiceExcepiton getting Smallfull data")
    void postDebtorsGetSmallFullDataApiResponseException() throws Exception {

        Debtors debtors = createDebtors();
        when(mockApiClientService.getApiClient()).thenReturn(mockApiClient);

        when(smallFullService.getSmallFullAccounts(mockApiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID)).thenThrow(ServiceException.class);

        assertThrows(ServiceException.class, () -> debtorsService.submitDebtors(
            TRANSACTION_ID,
            COMPANY_ACCOUNTS_ID,
            debtors,
            COMPANY_NUMBER));
    }

    @Test
    @DisplayName("PUT - Debtors successful path")
    void putDebtorsSuccess() throws Exception {

        Debtors debtors = createDebtors();
        DebtorsApi debtorsApi = new DebtorsApi();

        SmallFullApi smallFullApi = new SmallFullApi();
        setLinksWithDebtors(smallFullApi);

        when(smallFullService.getSmallFullAccounts(mockApiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID)).thenReturn(smallFullApi);

        when(mockDebtorsTransformer.getDebtorsApi(debtors)).thenReturn(debtorsApi);
        debtorsUpdate(debtorsApi);

        List<ValidationError> validationErrors = debtorsService.submitDebtors(TRANSACTION_ID,
            COMPANY_ACCOUNTS_ID, debtors, COMPANY_NUMBER);

        assertEquals(0, validationErrors.size());
    }

    @Test
    @DisplayName("PUT - Debtors throws ServiceExcepiton due to URIValidationException")
    void putDebtorsURIValidationException() throws Exception {

        DebtorsApi debtorsApi = createDebtorsApi();
        Debtors debtors = createDebtors();

        SmallFullApi smallFullApi = new SmallFullApi();
        setLinksWithDebtors(smallFullApi);

        when(smallFullService.getSmallFullAccounts(mockApiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID)).thenReturn(smallFullApi);

        when(mockDebtorsTransformer.getDebtorsApi(debtors)).thenReturn(debtorsApi);

        getMockDebtorsResourceHandler();
        when(mockDebtorsResourceHandler.update(DEBTORS_URI, debtorsApi)).thenReturn(mockDebtorsUpdate);

        when(mockDebtorsUpdate.execute()).thenThrow(URIValidationException.class);

        assertThrows(URIValidationException.class, () -> mockDebtorsUpdate.execute());
        assertThrows(ServiceException.class, () -> debtorsService.submitDebtors(
            TRANSACTION_ID,
            COMPANY_ACCOUNTS_ID,
            debtors,
            COMPANY_NUMBER));
    }

    @Test
    @DisplayName("DELETE - Debtors successful delete path")
    void deleteDebtors() throws Exception {

        getMockDebtorsResourceHandler();
        when(mockDebtorsResourceHandler.delete(DEBTORS_URI)).thenReturn(mockDebtorsDelete);
        doNothing().when(mockDebtorsDelete).execute();

        debtorsService.deleteDebtors(TRANSACTION_ID, COMPANY_ACCOUNTS_ID);

        verify(mockDebtorsDelete, times(1)).execute();
    }

    @Test
    @DisplayName("DELETE - Debtors throws ServiceExcepiton due to URIValidationException")
    void deleteDebtorsUriValidationException() throws Exception {

        getMockDebtorsResourceHandler();
        when(mockDebtorsResourceHandler.delete(DEBTORS_URI)).thenReturn(mockDebtorsDelete);
        when(mockDebtorsDelete.execute()).thenThrow(URIValidationException.class);

        assertThrows(URIValidationException.class, () -> mockDebtorsDelete.execute());
        assertThrows(ServiceException.class, () -> debtorsService.deleteDebtors(
            TRANSACTION_ID,
            COMPANY_ACCOUNTS_ID));
    }

    @Test
    @DisplayName("DELETE - Debtors throws ServiceExcepiton due to ApiErrorResponseException - 404 Not Found")
    void deleteDebtorsApiErrorResponseExceptionNotFound() throws Exception {

        getMockDebtorsResourceHandler();
        when(mockDebtorsResourceHandler.delete(DEBTORS_URI)).thenReturn(mockDebtorsDelete);

        HttpResponseException httpResponseException = new HttpResponseException.Builder(404,"Not Found",new HttpHeaders()).build();
        ApiErrorResponseException apiErrorResponseException = ApiErrorResponseException.fromHttpResponseException(httpResponseException);
        when(mockDebtorsDelete.execute()).thenThrow(apiErrorResponseException);

        assertThrows(ApiErrorResponseException.class, () -> mockDebtorsDelete.execute());
        assertThrows(ServiceException.class, () -> debtorsService.deleteDebtors(
            TRANSACTION_ID,
            COMPANY_ACCOUNTS_ID));
    }

    private void getMockSmallFullResourceHandler() {
        when(mockApiClientService.getApiClient()).thenReturn(mockApiClient);
        when(mockApiClient.smallFull()).thenReturn(mockSmallFullResourceHandler);
    }

    private void getMockDebtorsResourceHandler() throws Exception {
        getMockSmallFullResourceHandler();
        when(mockSmallFullResourceHandler.debtors()).thenReturn(mockDebtorsResourceHandler);
    }

    private void getMockDebtorsApi(DebtorsApi debtorsApi) throws Exception {
        getMockDebtorsResourceHandler();
        when(mockDebtorsResourceHandler.get(DEBTORS_URI)).thenReturn(mockDebtorsGet);
        when(mockDebtorsGet.execute()).thenReturn(debtorsApi);
    }

    private void debtorsCreate(DebtorsApi debtorsApi) throws Exception {
        getMockDebtorsResourceHandler();
        when(mockDebtorsResourceHandler.create(DEBTORS_URI, debtorsApi)).thenReturn(mockDebtorsCreate);
        when(mockDebtorsCreate.execute()).thenReturn(debtorsApi);
    }

    private void debtorsUpdate(DebtorsApi debtorsApi) throws Exception {
        getMockDebtorsResourceHandler();
        when(mockDebtorsResourceHandler.update(DEBTORS_URI, debtorsApi)).thenReturn(mockDebtorsUpdate);
        doNothing().when(mockDebtorsUpdate).execute();
    }

    private void setLinksWithDebtors(SmallFullApi smallFullApi) {
        SmallFullLinks links = new SmallFullLinks();
        links.setDebtorsNote("");

        smallFullApi.setLinks(links);
    }

    private void setLinksWithoutDebtors(SmallFullApi smallFullApi) {
        SmallFullLinks links = new SmallFullLinks();
        links.setCreditorsAfterMoreThanOneYearNote("");

        smallFullApi.setLinks(links);
    }

    private Debtors createDebtors() {
        Debtors debtors = new Debtors();
        TradeDebtors tradeDebtors = new TradeDebtors();
        Total total = new Total();

        BalanceSheetHeadings balanceSheetHeadings = new BalanceSheetHeadings();

        tradeDebtors.setCurrentTradeDebtors((long) 5);
        tradeDebtors.setPreviousTradeDebtors((long) 5);
        total.setCurrentTotal((long) 5);
        total.setPreviousTotal((long) 5);

        balanceSheetHeadings.setCurrentPeriodHeading("");
        balanceSheetHeadings.setPreviousPeriodHeading("");

        debtors.setTradeDebtors(tradeDebtors);
        debtors.setTotal(total);
        debtors.setBalanceSheetHeadings(balanceSheetHeadings);

        return debtors;
    }

    private DebtorsApi createDebtorsApi() {
        DebtorsApi debtorsApi = new DebtorsApi();
        CurrentPeriod currentPeriod = new CurrentPeriod();
        PreviousPeriod previousPeriod = new PreviousPeriod();

        currentPeriod.setTradeDebtors(5L);
        currentPeriod.setTotal(5L);

        previousPeriod.setTradeDebtors(5L);
        previousPeriod.setTotal(5L);

        debtorsApi.setDebtorsCurrentPeriod(currentPeriod);
        debtorsApi.setDebtorsPreviousPeriod(previousPeriod);

        return debtorsApi;
    }
}
