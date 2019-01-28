package uk.gov.companieshouse.web.accounts.service.smallfull.impl;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

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
import uk.gov.companieshouse.api.handler.smallfull.creditorsafteroneyear.CreditorsAfterOneYearResourceHandler;
import uk.gov.companieshouse.api.handler.smallfull.creditorsafteroneyear.request.CreditorsAfterOneYearGet;
import uk.gov.companieshouse.api.model.accounts.smallfull.creditorsafteroneyear.CreditorsAfterOneYearApi;
import uk.gov.companieshouse.web.accounts.api.ApiClientService;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.smallfull.BalanceSheet;
import uk.gov.companieshouse.web.accounts.model.smallfull.BalanceSheetHeadings;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.creditorsafteroneyear.CreditorsAfterOneYear;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.creditorsafteroneyear.OtherCreditors;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.creditorsafteroneyear.Total;
import uk.gov.companieshouse.web.accounts.service.smallfull.BalanceSheetService;
import uk.gov.companieshouse.web.accounts.service.smallfull.CreditorsAfterOneYearService;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.CreditorsAfterOneYearTransformer;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CreditorsAfterOneYearServiceImplTests {

    @InjectMocks
    private CreditorsAfterOneYearService mockCreditorsAfterOneYearService =
            new CreditorsAfterOneYearServiceImpl();

    @Mock
    CreditorsAfterOneYearTransformer mockTransformer;

    @Mock
    private CreditorsAfterOneYearResourceHandler mockCreditorsAfterOneYearResourceHandler;

    @Mock
    BalanceSheetService mockBalanceSheetService;

    @Mock
    BalanceSheet mockBalanceSheet;

    @Mock
    private CreditorsAfterOneYearGet mockCreditorsAfterOneYearGet;

    @Mock
    private ApiClientService mockApiClientService;

    @Mock
    private ApiClient mockApiClient;

    @Mock
    private SmallFullResourceHandler mockSmallFullResourceHandler;

    private static final String TRANSACTION_ID = "transactionId";

    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";

    private static final String COMPANY_NUMBER = "companyNumber";

    private static final String BASE_SMALL_FULL_URI = "/transactions/" + TRANSACTION_ID +
            "/company-accounts/" + COMPANY_ACCOUNTS_ID +
            "/small-full";

    private static final String CREDITORS_AFTER_ONE_YEAR_URI = BASE_SMALL_FULL_URI + "/notes" +
            "/creditors-after-more-than-one-year";


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
                mockCreditorsAfterOneYearService.getCreditorsAfterOneYear(TRANSACTION_ID,
                        COMPANY_ACCOUNTS_ID, COMPANY_NUMBER);

        validateCreditorsAfterOneYear(creditorsAfterOneYear);
    }

    @Test
    @DisplayName("GET - Creditors After One Year successful path when http status not found")
    void getCreditorsAfterOneYearSuccessHttpStatusNotFound() throws Exception {

        HttpResponseException httpResponseException = new HttpResponseException.Builder(404, "Not" +
                " Found", new HttpHeaders()).build();
        ApiErrorResponseException apiErrorResponseException =
                ApiErrorResponseException.fromHttpResponseException(httpResponseException);

        MockCreditorsAfterOneYearResourceHandler();
        when(mockCreditorsAfterOneYearResourceHandler.get(CREDITORS_AFTER_ONE_YEAR_URI)).
                thenReturn(mockCreditorsAfterOneYearGet);
        when(mockCreditorsAfterOneYearGet.execute()).thenThrow(apiErrorResponseException);
        when(mockBalanceSheetService.getBalanceSheet(TRANSACTION_ID, COMPANY_ACCOUNTS_ID,
                COMPANY_NUMBER)).thenReturn(mockBalanceSheet);

        when(mockTransformer.getCreditorsAfterOneYear(null))
                .thenReturn(createCreditorsAfterOneYear());

        CreditorsAfterOneYear creditorsAfterOneYear =
                mockCreditorsAfterOneYearService.getCreditorsAfterOneYear(TRANSACTION_ID,
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

        HttpResponseException httpResponseException = new HttpResponseException.Builder(400, "Bad" +
                " Request", new HttpHeaders()).build();
        ApiErrorResponseException apiErrorResponseException =
                ApiErrorResponseException.fromHttpResponseException(httpResponseException);

        MockCreditorsAfterOneYearResourceHandler();
        when(mockCreditorsAfterOneYearResourceHandler.get(CREDITORS_AFTER_ONE_YEAR_URI))
                .thenReturn(mockCreditorsAfterOneYearGet);
        when(mockCreditorsAfterOneYearGet.execute()).thenThrow(apiErrorResponseException);

        assertThrows(ApiErrorResponseException.class, () -> mockCreditorsAfterOneYearGet.execute());
        assertThrows(ServiceException.class,
                () -> mockCreditorsAfterOneYearService.getCreditorsAfterOneYear(
                        TRANSACTION_ID,
                        COMPANY_ACCOUNTS_ID,
                        COMPANY_NUMBER));
    }

    @Test
    @DisplayName("GET - Creditors After One Year throws ServiceExcepiton due to " +
            "URIValidationException")
    void getCreditorsAfterOneYearURIValidationException() throws Exception {

        MockCreditorsAfterOneYearResourceHandler();
        when(mockCreditorsAfterOneYearResourceHandler.get(CREDITORS_AFTER_ONE_YEAR_URI))
                .thenReturn(mockCreditorsAfterOneYearGet);
        when(mockCreditorsAfterOneYearGet.execute()).thenThrow(URIValidationException.class);

        assertThrows(URIValidationException.class, () -> mockCreditorsAfterOneYearGet.execute());
        assertThrows(ServiceException.class,
                () -> mockCreditorsAfterOneYearService.getCreditorsAfterOneYear(
                        TRANSACTION_ID,
                        COMPANY_ACCOUNTS_ID,
                        COMPANY_NUMBER));
    }

    private void getMockCreditorsAfterOneYearApi(CreditorsAfterOneYearApi creditorsAfterOneYearApi) throws Exception {
        MockCreditorsAfterOneYearResourceHandler();
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

    private void MockCreditorsAfterOneYearResourceHandler() {
        MockSmallFullResourceHandler();
        when(mockSmallFullResourceHandler.creditorsAfterOnerYear())
                .thenReturn(mockCreditorsAfterOneYearResourceHandler);
    }

    private void MockSmallFullResourceHandler() {
        when(mockApiClientService.getApiClient()).thenReturn(mockApiClient);
        when(mockApiClient.smallFull()).thenReturn(mockSmallFullResourceHandler);
    }
}
