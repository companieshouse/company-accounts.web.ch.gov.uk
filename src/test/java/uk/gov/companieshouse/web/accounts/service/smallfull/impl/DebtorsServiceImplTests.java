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
import uk.gov.companieshouse.api.handler.smallfull.SmallFullResourceHandler;
import uk.gov.companieshouse.api.handler.smallfull.debtors.DebtorsResourceHandler;
import uk.gov.companieshouse.api.handler.smallfull.debtors.request.DebtorsCreate;
import uk.gov.companieshouse.api.handler.smallfull.debtors.request.DebtorsGet;
import uk.gov.companieshouse.api.handler.smallfull.debtors.request.DebtorsUpdate;
import uk.gov.companieshouse.api.handler.smallfull.request.SmallFullGet;
import uk.gov.companieshouse.api.model.accounts.smallfull.Debtors.DebtorsApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.SmallFullApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.SmallFullLinks;
import uk.gov.companieshouse.web.accounts.api.ApiClientService;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.debtors.Debtors;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.debtors.Total;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.debtors.TradeDebtors;
import uk.gov.companieshouse.web.accounts.service.smallfull.DebtorsService;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.DebtorsTransformer;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

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
    private SmallFullGet mockSmallFullGet;

    @Mock
    private DebtorsTransformer mockDebtorsTransformer;

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

        Debtors debtors = debtorsService.getDebtors(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, COMPANY_NUMBER);

        assertNotNull(debtors);
        assertNotNull(debtors.getTradeDebtors());
        assertNotNull(debtors.getTradeDebtors().getCurrentTradeDebtors());
        assertNotNull(debtors.getTotal());
        assertNotNull(debtors.getTotal().getCurrentTotal());
    }

    @Test
    @DisplayName("POST - Debtors successful path")
    void postDebtorsSuccess() throws Exception {

        DebtorsApi debtorsApi = new DebtorsApi();
        getMockDebtorsApi(debtorsApi);

        SmallFullApi smallFullApi = new SmallFullApi();
        getMockSmallFullApi(smallFullApi);
        setLinksWithoutDebtors(smallFullApi);

        debtorsCreate(debtorsApi);

        List<ValidationError> validationErrors = debtorsService.submitDebtors(TRANSACTION_ID,
            COMPANY_ACCOUNTS_ID, createDebtors(), COMPANY_NUMBER);

        assertEquals(0, validationErrors.size());
    }

    @Test
    @DisplayName("POST - Debtors ApiErrorResponseException")
    void postDebtorsApiErrorResponseException() throws Exception {

        DebtorsApi debtorsApi = new DebtorsApi();
        getMockDebtorsApi(debtorsApi);

        Debtors debtors = createDebtors();

        SmallFullApi smallFullApi = new SmallFullApi();
        getMockSmallFullApi(smallFullApi);
        setLinksWithoutDebtors(smallFullApi);

        getMockDebtorsResourceHandler();
        when(mockDebtorsResourceHandler.create(DEBTORS_URI, debtorsApi)).thenReturn(mockDebtorsCreate);

        HttpResponseException httpResponseException = new HttpResponseException.Builder(404,"Bad Request",new HttpHeaders()).build();
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
    @DisplayName("PUT - Debtors successful path")
    void putDebtorsSuccess() throws Exception {

        DebtorsApi debtorsApi = new DebtorsApi();
        getMockDebtorsApi(debtorsApi);

        SmallFullApi smallFullApi = new SmallFullApi();
        getMockSmallFullApi(smallFullApi);
        setLinksWithDebtors(smallFullApi);

        debtorsUpdate(debtorsApi);

        List<ValidationError> validationErrors = debtorsService.submitDebtors(TRANSACTION_ID,
            COMPANY_ACCOUNTS_ID, createDebtors(), COMPANY_NUMBER);

        assertEquals(0, validationErrors.size());
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

    private void getMockSmallFullApi(SmallFullApi smallFullApi) throws Exception {
        getMockSmallFullResourceHandler();
        when(mockSmallFullResourceHandler.get(BASE_SMALL_FULL_URI)).thenReturn(mockSmallFullGet);
        when(mockSmallFullGet.execute()).thenReturn(smallFullApi);
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

        tradeDebtors.setCurrentTradeDebtors((long) 5);
        tradeDebtors.setPreviousTradeDebtors((long) 5);
        total.setCurrentTotal((long) 5);
        total.setPreviousTotal((long) 5);

        debtors.setTradeDebtors(tradeDebtors);
        debtors.setTotal(total);

        return debtors;
    }
}
