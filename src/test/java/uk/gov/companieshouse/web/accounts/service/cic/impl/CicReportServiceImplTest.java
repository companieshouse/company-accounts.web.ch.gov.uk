package uk.gov.companieshouse.web.accounts.service.cic.impl;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import uk.gov.companieshouse.api.handler.cic.CicReportResourceHandler;
import uk.gov.companieshouse.api.handler.cic.request.CicReportCreate;
import uk.gov.companieshouse.api.handler.cic.request.CicReportGet;
import uk.gov.companieshouse.api.handler.exception.URIValidationException;
import uk.gov.companieshouse.api.model.ApiResponse;
import uk.gov.companieshouse.api.model.accounts.cic.CicReportApi;
import uk.gov.companieshouse.web.accounts.api.ApiClientService;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.service.cic.CicReportService;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CicReportServiceImplTest {

    @Mock
    private ApiClient apiClient;

    @Mock
    private ApiClientService apiClientService;

    @Mock
    private CicReportResourceHandler cicReportResourceHandler;

    @Mock
    private CicReportCreate cicReportCreate;

    @Mock
    private CicReportGet cicReportGet;

    @Mock
    private ApiResponse<CicReportApi> responseWithData;

    @Mock
    private CicReportApi cicReport;

    @InjectMocks
    private CicReportService cicReportService = new CicReportServiceImpl();

    private static final String TRANSACTION_ID = "transactionId";

    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";

    private static final String CIC_REPORT_URI = "/transactions/" + TRANSACTION_ID +
                                                    "/company-accounts/" + COMPANY_ACCOUNTS_ID +
                                                    "/cic-report";

    @BeforeEach
    private void setUp() {

        when(apiClient.cicReport()).thenReturn(cicReportResourceHandler);
    }

    @Test
    @DisplayName("Create CIC report - success")
    void createCicReportSuccess() throws ApiErrorResponseException, URIValidationException {

        when(apiClientService.getApiClient()).thenReturn(apiClient);

        when(cicReportResourceHandler.create(eq(CIC_REPORT_URI), any(CicReportApi.class)))
                .thenReturn(cicReportCreate);

        assertAll(() -> cicReportService.createCicReport(TRANSACTION_ID, COMPANY_ACCOUNTS_ID));

        verify(cicReportCreate).execute();
    }

    @Test
    @DisplayName("Create CIC report - ApiErrorResponseException")
    void createCicReportApiErrorResponseException() throws ApiErrorResponseException, URIValidationException {

        when(apiClientService.getApiClient()).thenReturn(apiClient);

        when(cicReportResourceHandler.create(eq(CIC_REPORT_URI), any(CicReportApi.class)))
                .thenReturn(cicReportCreate);

        when(cicReportCreate.execute()).thenThrow(ApiErrorResponseException.class);

        assertThrows(ServiceException.class,
                () -> cicReportService.createCicReport(TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }

    @Test
    @DisplayName("Create CIC report - URIValidationException")
    void createCicReportURIValidationException() throws ApiErrorResponseException, URIValidationException {

        when(apiClientService.getApiClient()).thenReturn(apiClient);

        when(cicReportResourceHandler.create(eq(CIC_REPORT_URI), any(CicReportApi.class)))
                .thenReturn(cicReportCreate);

        when(cicReportCreate.execute()).thenThrow(URIValidationException.class);

        assertThrows(ServiceException.class,
                () -> cicReportService.createCicReport(TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }

    @Test
    @DisplayName("Get CIC report - success")
    void getCicReportSuccess()
            throws ApiErrorResponseException, URIValidationException, ServiceException {

        when(cicReportResourceHandler.get(CIC_REPORT_URI)).thenReturn(cicReportGet);

        when(cicReportGet.execute()).thenReturn(responseWithData);

        when(responseWithData.getData()).thenReturn(cicReport);

        CicReportApi returnedCicReport =
                cicReportService.getCicReport(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID);

        assertEquals(cicReport, returnedCicReport);
    }

    @Test
    @DisplayName("Get CIC report - ApiErrorResponseException")
    void getCicReportApiErrorResponseException() throws ApiErrorResponseException, URIValidationException {

        when(cicReportResourceHandler.get(CIC_REPORT_URI)).thenReturn(cicReportGet);

        when(cicReportGet.execute()).thenThrow(ApiErrorResponseException.class);

        assertThrows(ServiceException.class,
                () -> cicReportService.getCicReport(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }

    @Test
    @DisplayName("Get CIC report - URIValidationException")
    void getCicReportURIValidationException() throws ApiErrorResponseException, URIValidationException {

        when(cicReportResourceHandler.get(CIC_REPORT_URI)).thenReturn(cicReportGet);

        when(cicReportGet.execute()).thenThrow(URIValidationException.class);

        assertThrows(ServiceException.class,
                () -> cicReportService.getCicReport(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }
}
