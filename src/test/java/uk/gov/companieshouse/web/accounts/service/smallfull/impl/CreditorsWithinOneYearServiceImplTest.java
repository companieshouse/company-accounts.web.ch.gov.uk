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
import uk.gov.companieshouse.api.handler.smallfull.creditorswithinoneyear.CreditorsWithinOneYearResourceHandler;
import uk.gov.companieshouse.api.handler.smallfull.creditorswithinoneyear.request.CreditorsWithinOneYearCreate;
import uk.gov.companieshouse.api.handler.smallfull.creditorswithinoneyear.request.CreditorsWithinOneYearGet;
import uk.gov.companieshouse.api.handler.smallfull.creditorswithinoneyear.request.CreditorsWithinOneYearUpdate;
import uk.gov.companieshouse.api.handler.smallfull.request.SmallFullGet;
import uk.gov.companieshouse.api.model.accounts.smallfull.creditorswithinoneyear.CreditorsWithinOneYearApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.creditorswithinoneyear.CurrentPeriod;
import uk.gov.companieshouse.api.model.accounts.smallfull.creditorswithinoneyear.PreviousPeriod;
import uk.gov.companieshouse.api.model.accounts.smallfull.SmallFullApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.SmallFullLinks;
import uk.gov.companieshouse.web.accounts.api.ApiClientService;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.smallfull.BalanceSheet;
import uk.gov.companieshouse.web.accounts.model.smallfull.BalanceSheetHeadings;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.creditorswithinoneyear.CreditorsWithinOneYear;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.creditorswithinoneyear.Total;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.creditorswithinoneyear.TradeCreditors;
import uk.gov.companieshouse.web.accounts.service.smallfull.BalanceSheetService;
import uk.gov.companieshouse.web.accounts.service.smallfull.CreditorsWithinOneYearService;
import uk.gov.companieshouse.web.accounts.service.smallfull.SmallFullService;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.CreditorsWithinOneYearTransformer;
import uk.gov.companieshouse.web.accounts.util.ValidationContext;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CreditorsWithinOneYearServiceImplTest {

    @Mock
    private ApiClientService mockApiClientService;

    @Mock
    private ApiClient mockApiClient;

    @Mock
    private SmallFullResourceHandler mockSmallFullResourceHandler;

    @Mock
    private CreditorsWithinOneYearResourceHandler mockCreditorsWithinOneYearResourceHandler;

    @Mock
    private CreditorsWithinOneYearGet mockCreditorsWithinOneYearGet;

    @Mock
    private CreditorsWithinOneYearCreate mockCreditorsWithinOneYearCreate;

    @Mock
    private CreditorsWithinOneYearUpdate mockCreditorsWithinOneYearUpdate;

    @Mock
    private SmallFullGet mockSmallFullGet;

    @Mock
    private CreditorsWithinOneYearTransformer mockCreditorsWithinOneYearTransformer;

    @Mock
    private ValidationContext mockValidationContext;

    @Mock
    private SmallFullService smallFullService;

    @Mock
    private BalanceSheetService mockBalanceSheetService;

    @Mock
    private BalanceSheet mockBalanceSheet;

    @InjectMocks
    private CreditorsWithinOneYearService creditorsWithinOneYearService = new CreditorsWithinOneYearServiceImpl();

    private static final String TRANSACTION_ID = "transactionId";

    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";

    private static final String COMPANY_NUMBER = "companyNumber";

    private static final String BASE_SMALL_FULL_URI = "/transactions/" + TRANSACTION_ID +
        "/company-accounts/" + COMPANY_ACCOUNTS_ID +
        "/small-full";

    private static final String CREDITORS_WITHIN_ONE_YEAR_URI = BASE_SMALL_FULL_URI + "/notes/creditors-within-one-year";

    @Test
    @DisplayName("GET - Creditors Within One Year successful path")
    void getCreditorsWithinOneYearSuccess() throws Exception {

        CreditorsWithinOneYearApi creditorsWithinOneYearApi = new CreditorsWithinOneYearApi();
        getMockCreditorsWithinOneYearApi(creditorsWithinOneYearApi);

        when(mockCreditorsWithinOneYearTransformer.getCreditorsWithinOneYear(creditorsWithinOneYearApi)).thenReturn(createCreditorsWithinOneYear());
        when(mockBalanceSheetService.getBalanceSheet(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, COMPANY_NUMBER)).thenReturn(mockBalanceSheet);
        when(mockBalanceSheet.getBalanceSheetHeadings()).thenReturn(new BalanceSheetHeadings());

        CreditorsWithinOneYear creditorsWithinOneYear = creditorsWithinOneYearService.getCreditorsWithinOneYear(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, COMPANY_NUMBER);

        assertNotNull(creditorsWithinOneYear);
        assertNotNull(creditorsWithinOneYear.getTradeCreditors());
        assertNotNull(creditorsWithinOneYear.getTradeCreditors().getCurrentTradeCreditors());
        assertNotNull(creditorsWithinOneYear.getTotal());
        assertNotNull(creditorsWithinOneYear.getTotal().getCurrentTotal());
    }

    @Test
    @DisplayName("GET - Creditors Within One Year successful path when http status not found")
    void getCreditorsWithinOneYearSuccessHttpStatusNotFound() throws Exception {

        HttpResponseException httpResponseException = new HttpResponseException.Builder(404,"Not Found",new HttpHeaders()).build();
        ApiErrorResponseException apiErrorResponseException = ApiErrorResponseException.fromHttpResponseException(httpResponseException);

        getMockCreditorsWithinOneYearResourceHandler();
        when(mockCreditorsWithinOneYearResourceHandler.get(CREDITORS_WITHIN_ONE_YEAR_URI)).thenReturn(mockCreditorsWithinOneYearGet);
        when(mockCreditorsWithinOneYearGet.execute()).thenThrow(apiErrorResponseException);
        when(mockBalanceSheetService.getBalanceSheet(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, COMPANY_NUMBER)).thenReturn(mockBalanceSheet);

        when(mockCreditorsWithinOneYearTransformer.getCreditorsWithinOneYear(null)).thenReturn(createCreditorsWithinOneYear());

        CreditorsWithinOneYear creditorsWithinOneYear = creditorsWithinOneYearService.getCreditorsWithinOneYear(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, COMPANY_NUMBER);

        assertNotNull(creditorsWithinOneYear);
        assertNotNull(creditorsWithinOneYear.getTradeCreditors());
        assertNotNull(creditorsWithinOneYear.getTradeCreditors().getCurrentTradeCreditors());
        assertNotNull(creditorsWithinOneYear.getTotal());
        assertNotNull(creditorsWithinOneYear.getTotal().getCurrentTotal());
    }

    @Test
    @DisplayName("GET - Creditors Within One Year throws ServiceExcepiton due to ApiErrorResponseException - 400 Bad Request")
    void getCreditorsWithinOneYearApiResponseException() throws Exception {

        HttpResponseException httpResponseException = new HttpResponseException.Builder(400,"Bad Request",new HttpHeaders()).build();
        ApiErrorResponseException apiErrorResponseException = ApiErrorResponseException.fromHttpResponseException(httpResponseException);

        getMockCreditorsWithinOneYearResourceHandler();
        when(mockCreditorsWithinOneYearResourceHandler.get(CREDITORS_WITHIN_ONE_YEAR_URI)).thenReturn(mockCreditorsWithinOneYearGet);
        when(mockCreditorsWithinOneYearGet.execute()).thenThrow(apiErrorResponseException);

        assertThrows(ApiErrorResponseException.class, () -> mockCreditorsWithinOneYearGet.execute());
        assertThrows(ServiceException.class, () -> creditorsWithinOneYearService.getCreditorsWithinOneYear(
            TRANSACTION_ID,
            COMPANY_ACCOUNTS_ID,
            COMPANY_NUMBER));
    }

    @Test
    @DisplayName("GET - Creditors Within One Year throws ServiceExcepiton due to URIValidationException")
    void getCreditorsWithinOneYearURIValidationException() throws Exception {

        getMockCreditorsWithinOneYearResourceHandler();
        when(mockCreditorsWithinOneYearResourceHandler.get(CREDITORS_WITHIN_ONE_YEAR_URI)).thenReturn(mockCreditorsWithinOneYearGet);
        when(mockCreditorsWithinOneYearGet.execute()).thenThrow(URIValidationException.class);

        assertThrows(URIValidationException.class, () -> mockCreditorsWithinOneYearGet.execute());
        assertThrows(ServiceException.class, () -> creditorsWithinOneYearService.getCreditorsWithinOneYear(
            TRANSACTION_ID,
            COMPANY_ACCOUNTS_ID,
            COMPANY_NUMBER));
    }

    @Test
    @DisplayName("POST - Creditors Within One Year successful path")
    void postCreditorsWithinOneYearSuccess() throws Exception {

        CreditorsWithinOneYear creditorsWithinOneYear = createCreditorsWithinOneYear();
        CreditorsWithinOneYearApi creditorsWithinOneYearApi = createCreditorsWithinOneYearApi();

        SmallFullApi smallFullApi = new SmallFullApi();
        when(smallFullService.getSmallFullAccounts(mockApiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID)).thenReturn(smallFullApi);
        setLinksWithoutCreditorsWithinOneYear(smallFullApi);

        when(mockCreditorsWithinOneYearTransformer.getCreditorsWithinOneYearApi(creditorsWithinOneYear)).thenReturn(creditorsWithinOneYearApi);

        creditorsWithinOneYearCreate(creditorsWithinOneYearApi);

        List<ValidationError> validationErrors = creditorsWithinOneYearService.submitCreditorsWithinOneYear(TRANSACTION_ID,
            COMPANY_ACCOUNTS_ID, creditorsWithinOneYear, COMPANY_NUMBER);

        assertEquals(0, validationErrors.size());
    }

    @Test
    @DisplayName("POST - Creditors Within One Year throws ServiceException due to ApiErrorResponseException - 404 Not Found")
    void postCreditorsWithinOneYearApiErrorResponseExceptionNotFound() throws Exception {

        getMockCreditorsWithinOneYearResourceHandler();

        CreditorsWithinOneYear creditorsWithinOneYear = createCreditorsWithinOneYear();
        CreditorsWithinOneYearApi creditorsWithinOneYearApi = createCreditorsWithinOneYearApi();

        SmallFullApi smallFullApi = new SmallFullApi();
        setLinksWithoutCreditorsWithinOneYear(smallFullApi);

        when(smallFullService.getSmallFullAccounts(mockApiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID)).thenReturn(smallFullApi);

        when(mockCreditorsWithinOneYearTransformer.getCreditorsWithinOneYearApi(creditorsWithinOneYear)).thenReturn(creditorsWithinOneYearApi);
        when(mockCreditorsWithinOneYearResourceHandler.create(CREDITORS_WITHIN_ONE_YEAR_URI, creditorsWithinOneYearApi)).thenReturn(mockCreditorsWithinOneYearCreate);

        HttpResponseException httpResponseException = new HttpResponseException.Builder(404,"Not Found",new HttpHeaders()).build();
        ApiErrorResponseException apiErrorResponseException = ApiErrorResponseException.fromHttpResponseException(httpResponseException);
        when(mockCreditorsWithinOneYearCreate.execute()).thenThrow(apiErrorResponseException);

        assertThrows(ApiErrorResponseException.class, () -> mockCreditorsWithinOneYearCreate.execute());
        assertThrows(ServiceException.class, () -> creditorsWithinOneYearService.submitCreditorsWithinOneYear(
            TRANSACTION_ID,
            COMPANY_ACCOUNTS_ID,
            creditorsWithinOneYear,
            COMPANY_NUMBER));
    }

    @Test
    @DisplayName("POST - Creditors Within One Year throws ServiceExcepiton due to ApiErrorResponseException - 400 Bad Request")
    void postCreditorsWithinOneYearApiErrorResponseExceptionBadRequest() throws Exception {

        getMockCreditorsWithinOneYearResourceHandler();

        CreditorsWithinOneYear creditorsWithinOneYear = createCreditorsWithinOneYear();
        CreditorsWithinOneYearApi creditorsWithinOneYearApi = createCreditorsWithinOneYearApi();

        SmallFullApi smallFullApi = new SmallFullApi();
        setLinksWithoutCreditorsWithinOneYear(smallFullApi);

        when(smallFullService.getSmallFullAccounts(mockApiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID)).thenReturn(smallFullApi);

        when(mockCreditorsWithinOneYearTransformer.getCreditorsWithinOneYearApi(creditorsWithinOneYear)).thenReturn(creditorsWithinOneYearApi);
        when(mockCreditorsWithinOneYearResourceHandler.create(CREDITORS_WITHIN_ONE_YEAR_URI, creditorsWithinOneYearApi)).thenReturn(mockCreditorsWithinOneYearCreate);

        HttpResponseException httpResponseException = new HttpResponseException.Builder(400,"Bad Request",new HttpHeaders()).build();
        ApiErrorResponseException apiErrorResponseException = ApiErrorResponseException.fromHttpResponseException(httpResponseException);
        when(mockCreditorsWithinOneYearCreate.execute()).thenThrow(apiErrorResponseException);

        assertThrows(ApiErrorResponseException.class, () -> mockCreditorsWithinOneYearCreate.execute());
        assertThrows(ServiceException.class, () -> creditorsWithinOneYearService.submitCreditorsWithinOneYear(
            TRANSACTION_ID,
            COMPANY_ACCOUNTS_ID,
            creditorsWithinOneYear,
            COMPANY_NUMBER));
    }

    @Test
    @DisplayName("POST - Creditors Within One Year throws ServiceExcepiton getting Smallfull data")
    void postCreditorsWithinOneYearGetSmallFullDataApiResponseException() throws Exception {

        CreditorsWithinOneYear creditorsWithinOneYear = createCreditorsWithinOneYear();

        when(mockApiClientService.getApiClient()).thenReturn(mockApiClient);
        when(smallFullService.getSmallFullAccounts(mockApiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID)).thenThrow(ServiceException.class);

        assertThrows(ServiceException.class, () -> creditorsWithinOneYearService.submitCreditorsWithinOneYear(
            TRANSACTION_ID,
            COMPANY_ACCOUNTS_ID,
            creditorsWithinOneYear,
            COMPANY_NUMBER));
    }

    @Test
    @DisplayName("PUT - Creditors Within One Year successful path")
    void putCreditorsWithinOneYearSuccess() throws Exception {

        CreditorsWithinOneYear creditorsWithinOneYear = createCreditorsWithinOneYear();
        CreditorsWithinOneYearApi creditorsWithinOneYearApi = new CreditorsWithinOneYearApi();

        SmallFullApi smallFullApi = new SmallFullApi();
        setLinksWithCreditorsWithinOneYear(smallFullApi);

        when(smallFullService.getSmallFullAccounts(mockApiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID)).thenReturn(smallFullApi);

        when(mockCreditorsWithinOneYearTransformer.getCreditorsWithinOneYearApi(creditorsWithinOneYear)).thenReturn(creditorsWithinOneYearApi);
        creditorsWithinOneYearUpdate(creditorsWithinOneYearApi);

        List<ValidationError> validationErrors = creditorsWithinOneYearService.submitCreditorsWithinOneYear(TRANSACTION_ID,
            COMPANY_ACCOUNTS_ID, creditorsWithinOneYear, COMPANY_NUMBER);

        assertEquals(0, validationErrors.size());
    }

    @Test
    @DisplayName("PUT - Creditors Within One Year throws ServiceExcepiton due to URIValidationException")
    void putCreditorsWithinOneYearURIValidationException() throws Exception {

        CreditorsWithinOneYearApi creditorsWithinOneYearApi = createCreditorsWithinOneYearApi();
        CreditorsWithinOneYear creditorsWithinOneYear = createCreditorsWithinOneYear();

        SmallFullApi smallFullApi = new SmallFullApi();
        setLinksWithCreditorsWithinOneYear(smallFullApi);

        when(smallFullService.getSmallFullAccounts(mockApiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID)).thenReturn(smallFullApi);

        when(mockCreditorsWithinOneYearTransformer.getCreditorsWithinOneYearApi(creditorsWithinOneYear)).thenReturn(creditorsWithinOneYearApi);

        getMockCreditorsWithinOneYearResourceHandler();
        when(mockCreditorsWithinOneYearResourceHandler.update(CREDITORS_WITHIN_ONE_YEAR_URI, creditorsWithinOneYearApi)).thenReturn(mockCreditorsWithinOneYearUpdate);

        when(mockCreditorsWithinOneYearUpdate.execute()).thenThrow(URIValidationException.class);

        assertThrows(URIValidationException.class, () -> mockCreditorsWithinOneYearUpdate.execute());
        assertThrows(ServiceException.class, () -> creditorsWithinOneYearService.submitCreditorsWithinOneYear(
            TRANSACTION_ID,
            COMPANY_ACCOUNTS_ID,
            creditorsWithinOneYear,
            COMPANY_NUMBER));
    }

    private void getMockSmallFullResourceHandler() {
        when(mockApiClientService.getApiClient()).thenReturn(mockApiClient);
        when(mockApiClient.smallFull()).thenReturn(mockSmallFullResourceHandler);
    }

    private void getMockCreditorsWithinOneYearResourceHandler() throws Exception {
        getMockSmallFullResourceHandler();
        when(mockSmallFullResourceHandler.creditorsWithinOneYear()).thenReturn(mockCreditorsWithinOneYearResourceHandler);
    }

    private void getMockCreditorsWithinOneYearApi(CreditorsWithinOneYearApi creditorsWithinOneYearApi) throws Exception {
        getMockCreditorsWithinOneYearResourceHandler();
        when(mockCreditorsWithinOneYearResourceHandler.get(CREDITORS_WITHIN_ONE_YEAR_URI)).thenReturn(mockCreditorsWithinOneYearGet);
        when(mockCreditorsWithinOneYearGet.execute()).thenReturn(creditorsWithinOneYearApi);
    }

    private void creditorsWithinOneYearCreate(CreditorsWithinOneYearApi creditorsWithinOneYearApi) throws Exception {
        getMockCreditorsWithinOneYearResourceHandler();
        when(mockCreditorsWithinOneYearResourceHandler.create(CREDITORS_WITHIN_ONE_YEAR_URI, creditorsWithinOneYearApi)).thenReturn(mockCreditorsWithinOneYearCreate);
        when(mockCreditorsWithinOneYearCreate.execute()).thenReturn(creditorsWithinOneYearApi);
    }

    private void creditorsWithinOneYearUpdate(CreditorsWithinOneYearApi creditorsWithinOneYearApi) throws Exception {
        getMockCreditorsWithinOneYearResourceHandler();
        when(mockCreditorsWithinOneYearResourceHandler.update(CREDITORS_WITHIN_ONE_YEAR_URI, creditorsWithinOneYearApi)).thenReturn(mockCreditorsWithinOneYearUpdate);
        doNothing().when(mockCreditorsWithinOneYearUpdate).execute();
    }

    private void setLinksWithCreditorsWithinOneYear(SmallFullApi smallFullApi) {
        SmallFullLinks links = new SmallFullLinks();
        links.setCreditorsWithinOneYearNote("");

        smallFullApi.setLinks(links);
    }

    private void setLinksWithoutCreditorsWithinOneYear(SmallFullApi smallFullApi) {
        SmallFullLinks links = new SmallFullLinks();
        links.setCreditorsAfterMoreThanOneYearNote("");

        smallFullApi.setLinks(links);
    }

    private CreditorsWithinOneYear createCreditorsWithinOneYear() {
        CreditorsWithinOneYear creditorsWithinOneYear = new CreditorsWithinOneYear();
        TradeCreditors tradeCreditors = new TradeCreditors();
        Total total = new Total();

        BalanceSheetHeadings balanceSheetHeadings = new BalanceSheetHeadings();

        tradeCreditors.setCurrentTradeCreditors((long) 5);
        tradeCreditors.setPreviousTradeCreditors((long) 5);
        total.setCurrentTotal((long) 5);
        total.setPreviousTotal((long) 5);

        balanceSheetHeadings.setCurrentPeriodHeading("");
        balanceSheetHeadings.setPreviousPeriodHeading("");

        creditorsWithinOneYear.setTradeCreditors(tradeCreditors);
        creditorsWithinOneYear.setTotal(total);
        creditorsWithinOneYear.setBalanceSheetHeadings(balanceSheetHeadings);

        return creditorsWithinOneYear;
    }

    private CreditorsWithinOneYearApi createCreditorsWithinOneYearApi() {
        CreditorsWithinOneYearApi creditorsWithinOneYearApi = new CreditorsWithinOneYearApi();
        CurrentPeriod currentPeriod = new CurrentPeriod();
        PreviousPeriod previousPeriod = new PreviousPeriod();

        currentPeriod.setTradeCreditors(5L);
        currentPeriod.setTotal(5L);

        previousPeriod.setTradeCreditors(5L);
        previousPeriod.setTotal(5L);

        creditorsWithinOneYearApi.setCreditorsWithinOneYearCurrentPeriod(currentPeriod);
        creditorsWithinOneYearApi.setCreditorsWithinOneYearPreviousPeriod(previousPeriod);

        return creditorsWithinOneYearApi;
    }
}
