package uk.gov.companieshouse.web.accounts.service.smallfull.impl;

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
import uk.gov.companieshouse.api.handler.smallfull.creditorswithinoneyear.request.CreditorsWithinOneYearDelete;
import uk.gov.companieshouse.api.handler.smallfull.creditorswithinoneyear.request.CreditorsWithinOneYearGet;
import uk.gov.companieshouse.api.handler.smallfull.creditorswithinoneyear.request.CreditorsWithinOneYearUpdate;
import uk.gov.companieshouse.api.model.ApiResponse;
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
import uk.gov.companieshouse.web.accounts.validation.helper.ServiceExceptionHandler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

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
    private CreditorsWithinOneYearDelete mockCreditorsWithinOneYearDelete;

    @Mock
    private ServiceExceptionHandler serviceExceptionHandler;

    @Mock
    private ValidationContext validationContext;

    @Mock
    private CreditorsWithinOneYearTransformer mockCreditorsWithinOneYearTransformer;

    @Mock
    private SmallFullService smallFullService;

    @Mock
    private BalanceSheetService mockBalanceSheetService;

    @Mock
    private BalanceSheet mockBalanceSheet;

    @Mock
    private List<ValidationError> mockValidationErrors;

    @Mock
    private ApiResponse<CreditorsWithinOneYearApi> responseWithData;

    @Mock
    private ApiResponse<Void> responseNoData;

    @Mock
    private ApiErrorResponseException apiErrorResponseException;

    @Mock
    private URIValidationException uriValidationException;

    @InjectMocks
    private CreditorsWithinOneYearService creditorsWithinOneYearService = new CreditorsWithinOneYearServiceImpl();

    private static final String TRANSACTION_ID = "transactionId";

    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";

    private static final String COMPANY_NUMBER = "companyNumber";

    private static final String BASE_SMALL_FULL_URI = "/transactions/" + TRANSACTION_ID +
        "/company-accounts/" + COMPANY_ACCOUNTS_ID +
        "/small-full";

    private static final String CREDITORS_WITHIN_ONE_YEAR_URI = BASE_SMALL_FULL_URI + "/notes/creditors-within-one-year";

    private static final String RESOURCE_NAME = "creditors within one year";

    @Test
    @DisplayName("GET - Creditors Within One Year successful path")
    void getCreditorsWithinOneYearSuccess() throws Exception {

        CreditorsWithinOneYearApi creditorsWithinOneYearApi = new CreditorsWithinOneYearApi();
        getMockCreditorsWithinOneYearApi(creditorsWithinOneYearApi);

        when(mockCreditorsWithinOneYearTransformer.getCreditorsWithinOneYear(creditorsWithinOneYearApi)).thenReturn(createCreditorsWithinOneYear());

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

        getMockCreditorsWithinOneYearResourceHandler();
        when(mockCreditorsWithinOneYearResourceHandler.get(CREDITORS_WITHIN_ONE_YEAR_URI)).thenReturn(mockCreditorsWithinOneYearGet);
        when(mockCreditorsWithinOneYearGet.execute()).thenThrow(apiErrorResponseException);

        doNothing()
                .when(serviceExceptionHandler)
                .handleRetrievalException(apiErrorResponseException, RESOURCE_NAME);

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

        getMockCreditorsWithinOneYearResourceHandler();
        when(mockCreditorsWithinOneYearResourceHandler.get(CREDITORS_WITHIN_ONE_YEAR_URI)).thenReturn(mockCreditorsWithinOneYearGet);
        when(mockCreditorsWithinOneYearGet.execute()).thenThrow(apiErrorResponseException);

        doThrow(ServiceException.class)
                .when(serviceExceptionHandler)
                .handleRetrievalException(apiErrorResponseException, RESOURCE_NAME);

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

        when(mockCreditorsWithinOneYearGet.execute()).thenThrow(uriValidationException);

        doThrow(ServiceException.class)
                .when(serviceExceptionHandler)
                .handleURIValidationException(uriValidationException, RESOURCE_NAME);

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

        when(responseWithData.hasErrors()).thenReturn(false);

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

        when(mockCreditorsWithinOneYearCreate.execute()).thenThrow(apiErrorResponseException);

        doThrow(ServiceException.class)
                .when(serviceExceptionHandler)
                        .handleSubmissionException(apiErrorResponseException, RESOURCE_NAME);

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

        creditorsWithinOneYearCreate(creditorsWithinOneYearApi);

        when(responseWithData.hasErrors()).thenReturn(true);

        when(validationContext.getValidationErrors(responseWithData.getErrors()))
                .thenReturn(mockValidationErrors);

        List<ValidationError> validationErrors = creditorsWithinOneYearService.submitCreditorsWithinOneYear(
            TRANSACTION_ID,
            COMPANY_ACCOUNTS_ID,
            creditorsWithinOneYear,
            COMPANY_NUMBER);

        assertEquals(mockValidationErrors, validationErrors);
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

        when(responseNoData.hasErrors()).thenReturn(false);

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

        when(mockCreditorsWithinOneYearUpdate.execute()).thenThrow(uriValidationException);

        doThrow(ServiceException.class)
                .when(serviceExceptionHandler)
                .handleURIValidationException(uriValidationException, RESOURCE_NAME);

        assertThrows(ServiceException.class, () -> creditorsWithinOneYearService.submitCreditorsWithinOneYear(
            TRANSACTION_ID,
            COMPANY_ACCOUNTS_ID,
            creditorsWithinOneYear,
            COMPANY_NUMBER));
    }
    

    @Test
    @DisplayName("DELETE - Creditors Within One Year successful delete path")
    void deleteCreditorsWithinOneYear() throws Exception {

        getMockCreditorsWithinOneYearResourceHandler();
        when(mockCreditorsWithinOneYearResourceHandler.delete(CREDITORS_WITHIN_ONE_YEAR_URI)).thenReturn(mockCreditorsWithinOneYearDelete);

        creditorsWithinOneYearService.deleteCreditorsWithinOneYear(TRANSACTION_ID,
            COMPANY_ACCOUNTS_ID);

        verify(mockCreditorsWithinOneYearDelete, times(1)).execute();
    }

    @Test
    @DisplayName("DELETE - Creditors Within One Year throws ServiceExcepiton due to URIValidationException")
    void deleteCreditorsWithinOneYearUriValidationException() throws Exception {

        getMockCreditorsWithinOneYearResourceHandler();
        when(mockCreditorsWithinOneYearResourceHandler.delete(CREDITORS_WITHIN_ONE_YEAR_URI)).thenReturn(mockCreditorsWithinOneYearDelete);

        when(mockCreditorsWithinOneYearDelete.execute()).thenThrow(uriValidationException);

        doThrow(ServiceException.class)
                .when(serviceExceptionHandler)
                .handleURIValidationException(uriValidationException, RESOURCE_NAME);

        assertThrows(ServiceException.class, () -> creditorsWithinOneYearService.deleteCreditorsWithinOneYear(
            TRANSACTION_ID,
            COMPANY_ACCOUNTS_ID));
    }

    @Test
    @DisplayName("DELETE - Creditors Within One Year throws ServiceExcepiton due to ApiErrorResponseException - 404 Not Found")
    void deleteCreditorsWithinOneYearApiErrorResponseExceptionNotFound() throws Exception {

        getMockCreditorsWithinOneYearResourceHandler();
        when(mockCreditorsWithinOneYearResourceHandler.delete(CREDITORS_WITHIN_ONE_YEAR_URI)).thenReturn(mockCreditorsWithinOneYearDelete);

        when(mockCreditorsWithinOneYearDelete.execute()).thenThrow(apiErrorResponseException);

        doThrow(ServiceException.class)
                .when(serviceExceptionHandler)
                .handleDeletionException(apiErrorResponseException, RESOURCE_NAME);

        assertThrows(ServiceException.class, () -> creditorsWithinOneYearService.deleteCreditorsWithinOneYear(
            TRANSACTION_ID,
            COMPANY_ACCOUNTS_ID));
    }    

    private void getMockSmallFullResourceHandler() {
        when(mockApiClientService.getApiClient()).thenReturn(mockApiClient);
        when(mockApiClient.smallFull()).thenReturn(mockSmallFullResourceHandler);
    }

    private void getMockCreditorsWithinOneYearResourceHandler() {
        getMockSmallFullResourceHandler();
        when(mockSmallFullResourceHandler.creditorsWithinOneYear()).thenReturn(mockCreditorsWithinOneYearResourceHandler);
    }

    private void getMockCreditorsWithinOneYearApi(CreditorsWithinOneYearApi creditorsWithinOneYearApi) throws Exception {
        getMockCreditorsWithinOneYearResourceHandler();
        when(mockCreditorsWithinOneYearResourceHandler.get(CREDITORS_WITHIN_ONE_YEAR_URI)).thenReturn(mockCreditorsWithinOneYearGet);
        when(mockCreditorsWithinOneYearGet.execute()).thenReturn(responseWithData);
        when(responseWithData.getData()).thenReturn(creditorsWithinOneYearApi);
    }

    private void creditorsWithinOneYearCreate(CreditorsWithinOneYearApi creditorsWithinOneYearApi) throws Exception {
        getMockCreditorsWithinOneYearResourceHandler();
        when(mockCreditorsWithinOneYearResourceHandler.create(CREDITORS_WITHIN_ONE_YEAR_URI, creditorsWithinOneYearApi)).thenReturn(mockCreditorsWithinOneYearCreate);
        when(mockCreditorsWithinOneYearCreate.execute()).thenReturn(responseWithData);
    }

    private void creditorsWithinOneYearUpdate(CreditorsWithinOneYearApi creditorsWithinOneYearApi) throws Exception {
        getMockCreditorsWithinOneYearResourceHandler();
        when(mockCreditorsWithinOneYearResourceHandler.update(CREDITORS_WITHIN_ONE_YEAR_URI, creditorsWithinOneYearApi)).thenReturn(mockCreditorsWithinOneYearUpdate);
        when(mockCreditorsWithinOneYearUpdate.execute()).thenReturn(responseNoData);
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
