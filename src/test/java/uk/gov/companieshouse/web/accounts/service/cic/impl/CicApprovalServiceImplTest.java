package uk.gov.companieshouse.web.accounts.service.cic.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpResponseException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
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
import uk.gov.companieshouse.api.handler.cic.CicReportResourceHandler;
import uk.gov.companieshouse.api.handler.cic.approval.CicApprovalResourceHandler;
import uk.gov.companieshouse.api.handler.cic.approval.request.CicApprovalCreate;
import uk.gov.companieshouse.api.handler.cic.approval.request.CicApprovalUpdate;
import uk.gov.companieshouse.api.handler.exception.URIValidationException;
import uk.gov.companieshouse.api.model.ApiResponse;
import uk.gov.companieshouse.api.model.accounts.cic.CicReportApi;
import uk.gov.companieshouse.api.model.accounts.cic.CicReportLinks;
import uk.gov.companieshouse.api.model.accounts.cic.approval.CicApprovalApi;
import uk.gov.companieshouse.web.accounts.api.ApiClientService;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.cic.CicApproval;
import uk.gov.companieshouse.web.accounts.model.smallfull.Date;
import uk.gov.companieshouse.web.accounts.service.cic.CicApprovalService;
import uk.gov.companieshouse.web.accounts.service.cic.CicReportService;
import uk.gov.companieshouse.web.accounts.transformer.cic.CicApprovalTransformer;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;
import uk.gov.companieshouse.web.accounts.validation.helper.ServiceExceptionHandler;

    @ExtendWith(MockitoExtension.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    public class CicApprovalServiceImplTest {

        @Mock
        private CicApprovalTransformer cicApprovalTransformer;

        @Mock
        private ApiClientService apiClientService;

        @Mock
        private CicReportService cicReportService;

        @Mock
        private ApiClient apiClient;

        @Mock
        private CicReportResourceHandler cicReportResourceHandler;

        @Mock
        private CicApprovalResourceHandler cicApprovalResourceHandler;

        @Mock
        private CicApprovalCreate cicApprovalCreate;

        @Mock
        private CicApprovalUpdate cicApprovalUpdate;

        @Mock
        private CicReportApi cicReportApi;

        @Mock
        private CicReportLinks cicReportLinks;

        @Mock
        private ServiceExceptionHandler serviceExceptionHandler;

        @Mock
        private List<ValidationError> mockValidationErrors;

        @Mock
        private ApiResponse<CicApprovalApi> mockApiResponse;

        @Mock
        private ApiResponse<Void> mockVoidApiResponse;

        @InjectMocks
        private CicApprovalService cicApprovalService = new CicApprovalServiceImpl();

        private static final String TRANSACTION_ID = "transactionId";

        private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";

        private static final String APPROVAL_URI = "/transactions/" + TRANSACTION_ID + "/company-accounts/" +
            COMPANY_ACCOUNTS_ID + "/cic-report/cic-approval";

        private static final String APPROVAL_DATE_ERROR_LOCATION = ".cic_approval.date";

        private static final String DATE_MISSING = "validation.date.missing" + APPROVAL_DATE_ERROR_LOCATION;

        private static final String DATE_INCOMPLETE = "validation.date.incomplete" + APPROVAL_DATE_ERROR_LOCATION;

        private static final String DATE_FORMAT_INVALID = "validation.date.format" + APPROVAL_DATE_ERROR_LOCATION;

        private static final String DATE_INVALID = "validation.date.nonExistent";

        private static final String MOCK_APPROVAL_LINK = "mockApprovalLink";

        private static final String RESOURCE_NAME = "approval";

        @Test
        @DisplayName("Submit Approval - POST - Success Path")
        void createApprovalSuccess() throws ApiErrorResponseException, URIValidationException, ServiceException {

            when(apiClientService.getApiClient()).thenReturn(apiClient);

            when(apiClient.cicReport()).thenReturn(cicReportResourceHandler);

            when(cicReportResourceHandler.approval()).thenReturn(cicApprovalResourceHandler);

            when(cicReportService.getCicReport(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(cicReportApi);

            when(cicReportApi.getLinks()).thenReturn(cicReportLinks);

            CicApproval cicApproval = new CicApproval();

            CicApprovalApi cicApprovalApi = new CicApprovalApi();

            when(cicApprovalTransformer.getCicApprovalApi(cicApproval)).thenReturn(cicApprovalApi);

            when(cicApprovalResourceHandler.create(APPROVAL_URI, cicApprovalApi)).thenReturn(cicApprovalCreate);

            when(cicReportLinks.getApproval()).thenReturn(null);

            when(cicApprovalCreate.execute()).thenReturn(mockApiResponse);

            List<ValidationError> validationErrors =
                cicApprovalService.submitCicApproval(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, cicApproval);

            verify(cicApprovalCreate, times(1)).execute();

            assertTrue(validationErrors.isEmpty());
        }

        @Test
        @DisplayName("Submit Approval - POST - URIValidationException Thrown")
        void createApprovalThrowsURIValidationException() throws ApiErrorResponseException, URIValidationException,
            ServiceException {

            when(apiClientService.getApiClient()).thenReturn(apiClient);

            when(apiClient.cicReport()).thenReturn(cicReportResourceHandler);

            when(cicReportResourceHandler.approval()).thenReturn(cicApprovalResourceHandler);

            when(cicReportService.getCicReport(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(cicReportApi);

            when(cicReportApi.getLinks()).thenReturn(cicReportLinks);

            CicApproval cicApproval = new CicApproval();

            CicApprovalApi cicApprovalApi = new CicApprovalApi();

            when(cicApprovalTransformer.getCicApprovalApi(cicApproval)).thenReturn(cicApprovalApi);

            when(cicApprovalResourceHandler.create(APPROVAL_URI, cicApprovalApi)).thenReturn(cicApprovalCreate);

            when(cicReportLinks.getApproval()).thenReturn(null);

            URIValidationException uriValidationException = new URIValidationException("invalid uri");

            when(cicApprovalCreate.execute()).thenThrow(uriValidationException);

            doThrow(ServiceException.class)
                .when(serviceExceptionHandler)
                .handleURIValidationException(uriValidationException, RESOURCE_NAME);

            assertThrows(ServiceException.class, () ->
                cicApprovalService.submitCicApproval(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, cicApproval));

            verify(cicApprovalCreate, times(1)).execute();
        }

        @Test
        @DisplayName("Submit Approval - POST - ApiErrorResponseException Thrown - Validation Errors")
        void createApprovalThrowsApiErrorResponseExceptionWithValidationErrors()
            throws ApiErrorResponseException, URIValidationException, ServiceException {

            when(apiClientService.getApiClient()).thenReturn(apiClient);

            when(apiClient.cicReport()).thenReturn(cicReportResourceHandler);

            when(cicReportResourceHandler.approval()).thenReturn(cicApprovalResourceHandler);

            when(cicReportService.getCicReport(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(cicReportApi);

            when(cicReportApi.getLinks()).thenReturn(cicReportLinks);

            CicApproval cicApproval = new CicApproval();

            CicApprovalApi cicApprovalApi = new CicApprovalApi();

            when(cicApprovalTransformer.getCicApprovalApi(cicApproval)).thenReturn(cicApprovalApi);

            when(cicApprovalResourceHandler.create(APPROVAL_URI, cicApprovalApi)).thenReturn(cicApprovalCreate);

            when(cicReportLinks.getApproval()).thenReturn(null);

            HttpResponseException httpResponseException =
                new HttpResponseException.Builder(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), new HttpHeaders())
                    .build();

            ApiErrorResponseException apiErrorResponseException =
                ApiErrorResponseException.fromHttpResponseException(httpResponseException);

            when(cicApprovalCreate.execute()).thenThrow(apiErrorResponseException);

            cicApprovalService.submitCicApproval(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, cicApproval);

            verify(cicApprovalCreate, times(1)).execute();
        }

        @Test
        @DisplayName("Submit Approval - POST - Generic ApiErrorResponseException Thrown")
        void createApprovalThrowsGenericApiErrorResponseException()
            throws ApiErrorResponseException, URIValidationException, ServiceException {

            when(apiClientService.getApiClient()).thenReturn(apiClient);

            when(apiClient.cicReport()).thenReturn(cicReportResourceHandler);

            when(cicReportResourceHandler.approval()).thenReturn(cicApprovalResourceHandler);

            when(cicReportService.getCicReport(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(cicReportApi);

            when(cicReportApi.getLinks()).thenReturn(cicReportLinks);

            CicApproval cicApproval = new CicApproval();

            CicApprovalApi cicApprovalApi = new CicApprovalApi();

            when(cicApprovalTransformer.getCicApprovalApi(cicApproval)).thenReturn(cicApprovalApi);

            when(cicApprovalResourceHandler.create(APPROVAL_URI, cicApprovalApi)).thenReturn(cicApprovalCreate);

            when(cicReportLinks.getApproval()).thenReturn(null);

            HttpResponseException httpResponseException =
                new HttpResponseException.Builder(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), new HttpHeaders())
                    .build();

            ApiErrorResponseException apiErrorResponseException =
                ApiErrorResponseException.fromHttpResponseException(httpResponseException);

            when(cicApprovalCreate.execute()).thenThrow(apiErrorResponseException);

            cicApprovalService.submitCicApproval(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, cicApproval);

            verify(cicApprovalCreate, times(1)).execute();
        }

        @Test
        @DisplayName("Submit Approval - PUT - Success Path")
        void updateApprovalSuccess() throws ApiErrorResponseException, URIValidationException, ServiceException {

            when(apiClientService.getApiClient()).thenReturn(apiClient);

            when(apiClient.cicReport()).thenReturn(cicReportResourceHandler);

            when(cicReportResourceHandler.approval()).thenReturn(cicApprovalResourceHandler);

            when(cicReportService.getCicReport(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(cicReportApi);

            when(cicReportApi.getLinks()).thenReturn(cicReportLinks);

            CicApproval cicApproval = new CicApproval();

            CicApprovalApi cicApprovalApi = new CicApprovalApi();

            when(cicApprovalTransformer.getCicApprovalApi(cicApproval)).thenReturn(cicApprovalApi);

            when(cicApprovalResourceHandler.update(APPROVAL_URI, cicApprovalApi)).thenReturn(cicApprovalUpdate);

            when(cicReportLinks.getApproval()).thenReturn(MOCK_APPROVAL_LINK);

            when(cicApprovalUpdate.execute()).thenReturn(mockVoidApiResponse);

            List<ValidationError> validationErrors =
                cicApprovalService.submitCicApproval(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, cicApproval);

            verify(cicApprovalUpdate, times(1)).execute();

            assertTrue(validationErrors.isEmpty());
        }

        @Test
        @DisplayName("Submit Approval - PUT - URIValidationException Thrown")
        void updateApprovalURIValidationExceptionThrown() throws ApiErrorResponseException, URIValidationException,
            ServiceException {

            when(apiClientService.getApiClient()).thenReturn(apiClient);

            when(apiClient.cicReport()).thenReturn(cicReportResourceHandler);

            when(cicReportResourceHandler.approval()).thenReturn(cicApprovalResourceHandler);

            when(cicReportService.getCicReport(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(cicReportApi);

            when(cicReportApi.getLinks()).thenReturn(cicReportLinks);

            CicApproval cicApproval = new CicApproval();

            CicApprovalApi cicApprovalApi = new CicApprovalApi();

            when(cicApprovalTransformer.getCicApprovalApi(cicApproval)).thenReturn(cicApprovalApi);

            when(cicApprovalResourceHandler.update(APPROVAL_URI, cicApprovalApi)).thenReturn(cicApprovalUpdate);

            when(cicReportLinks.getApproval()).thenReturn(MOCK_APPROVAL_LINK);

            URIValidationException uriValidationException = new URIValidationException("invalid uri");

            doThrow(uriValidationException).when(cicApprovalUpdate).execute();

            doThrow(ServiceException.class)
                .when(serviceExceptionHandler)
                .handleURIValidationException(uriValidationException, RESOURCE_NAME);

            assertThrows(ServiceException.class, () ->
                cicApprovalService.submitCicApproval(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, cicApproval));

            verify(cicApprovalUpdate, times(1)).execute();
        }

        @Test
        @DisplayName("Submit Approval - PUT - ApiErrorResponseException Thrown - Validation Errors")
        void updateApprovalThrowsApiErrorResponseExceptionWithValidationErrors()
            throws ApiErrorResponseException, URIValidationException, ServiceException {

            when(apiClientService.getApiClient()).thenReturn(apiClient);

            when(apiClient.cicReport()).thenReturn(cicReportResourceHandler);

            when(cicReportResourceHandler.approval()).thenReturn(cicApprovalResourceHandler);

            when(cicReportService.getCicReport(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(cicReportApi);

            when(cicReportApi.getLinks()).thenReturn(cicReportLinks);

            CicApproval cicApproval = new CicApproval();

            CicApprovalApi cicApprovalApi = new CicApprovalApi();

            when(cicApprovalTransformer.getCicApprovalApi(cicApproval)).thenReturn(cicApprovalApi);

            when(cicApprovalResourceHandler.update(APPROVAL_URI, cicApprovalApi)).thenReturn(cicApprovalUpdate);

            when(cicReportLinks.getApproval()).thenReturn(MOCK_APPROVAL_LINK);

            HttpResponseException httpResponseException =
                new HttpResponseException.Builder(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), new HttpHeaders())
                    .build();

            ApiErrorResponseException apiErrorResponseException =
                ApiErrorResponseException.fromHttpResponseException(httpResponseException);

            doThrow(apiErrorResponseException).when(cicApprovalUpdate).execute();

            cicApprovalService.submitCicApproval(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, cicApproval);

            verify(cicApprovalUpdate, times(1)).execute();
        }

        @Test
        @DisplayName("Submit Approval - PUT - Generic ApiErrorResponseException Thrown")
        void updateApprovalThrowsGenericApiErrorResponseException()
            throws ApiErrorResponseException, URIValidationException, ServiceException {

            when(apiClientService.getApiClient()).thenReturn(apiClient);

            when(apiClient.cicReport()).thenReturn(cicReportResourceHandler);

            when(cicReportResourceHandler.approval()).thenReturn(cicApprovalResourceHandler);

            when(cicReportService.getCicReport(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(cicReportApi);

            when(cicReportApi.getLinks()).thenReturn(cicReportLinks);

            CicApproval cicApproval = new CicApproval();

            CicApprovalApi cicApprovalApi = new CicApprovalApi();

            when(cicApprovalTransformer.getCicApprovalApi(cicApproval)).thenReturn(cicApprovalApi);

            when(cicApprovalResourceHandler.update(APPROVAL_URI, cicApprovalApi)).thenReturn(cicApprovalUpdate);

            when(cicReportLinks.getApproval()).thenReturn(MOCK_APPROVAL_LINK);

            HttpResponseException httpResponseException =
                new HttpResponseException.Builder(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), new HttpHeaders())
                    .build();

            ApiErrorResponseException apiErrorResponseException =
                ApiErrorResponseException.fromHttpResponseException(httpResponseException);

            doThrow(apiErrorResponseException).when(cicApprovalUpdate).execute();

            doThrow(ServiceException.class).when(serviceExceptionHandler).handleSubmissionException(apiErrorResponseException, RESOURCE_NAME);

            assertThrows(ServiceException.class, () ->
                cicApprovalService.submitCicApproval(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, cicApproval));

            verify(cicApprovalUpdate, times(1)).execute();
        }

        @Test
        @DisplayName("Validate Approval Date - No Fields Provided")
        void validateApprovalDateNoFieldsProvided() {

            Date approvalDate = new Date();

            CicApproval cicApproval = new CicApproval();
            cicApproval.setDate(approvalDate);

            List<ValidationError> validationErrors = cicApprovalService.validateCicApprovalDate(cicApproval);

            assertNotNull(validationErrors);
            assertEquals(1, validationErrors.size());
            assertEquals(DATE_MISSING, validationErrors.get(0).getMessageKey());
        }

        @Test
        @DisplayName("Validate Approval Date - Day Not Provided")
        void validateApprovalDateDayNotProvided() {

            Date approvalDate = new Date();
            approvalDate.setMonth("12");
            approvalDate.setYear("2018");

            CicApproval cicApproval = new CicApproval();
            cicApproval.setDate(approvalDate);

            List<ValidationError> validationErrors = cicApprovalService.validateCicApprovalDate(cicApproval);

            assertNotNull(validationErrors);
            assertEquals(1, validationErrors.size());
            assertEquals(DATE_INCOMPLETE, validationErrors.get(0).getMessageKey());
        }

        @Test
        @DisplayName("Validate Approval Date - Month Not Provided")
        void validateApprovalDateMonthNotProvided() {

            Date approvalDate = new Date();
            approvalDate.setDay("12");
            approvalDate.setYear("2018");

            CicApproval cicApproval = new CicApproval();
            cicApproval.setDate(approvalDate);

            List<ValidationError> validationErrors = cicApprovalService.validateCicApprovalDate(cicApproval);

            assertNotNull(validationErrors);
            assertEquals(1, validationErrors.size());
            assertEquals(DATE_INCOMPLETE, validationErrors.get(0).getMessageKey());
        }

        @Test
        @DisplayName("Validate Approval Date - Year Not Provided")
        void validateApprovalDateYearNotProvided() {

            Date approvalDate = new Date();
            approvalDate.setDay("12");
            approvalDate.setMonth("12");

            CicApproval cicApproval = new CicApproval();
            cicApproval.setDate(approvalDate);

            List<ValidationError> validationErrors = cicApprovalService.validateCicApprovalDate(cicApproval);

            assertNotNull(validationErrors);
            assertEquals(1, validationErrors.size());
            assertEquals(DATE_INCOMPLETE, validationErrors.get(0).getMessageKey());
        }

        @Test
        @DisplayName("Validate Approval Date - Invalid Day Format")
        void validateApprovalDateInvalidDayFormat() {

            Date approvalDate = new Date();
            approvalDate.setDay("1st");
            approvalDate.setMonth("3");
            approvalDate.setYear("2018");

            CicApproval cicApproval = new CicApproval();
            cicApproval.setDate(approvalDate);

            List<ValidationError> validationErrors = cicApprovalService.validateCicApprovalDate(cicApproval);

            assertNotNull(validationErrors);
            assertEquals(1, validationErrors.size());
            assertEquals(DATE_FORMAT_INVALID, validationErrors.get(0).getMessageKey());
        }

        @Test
        @DisplayName("Validate Approval Date - Invalid Month Format")
        void validateApprovalDateInvalidMonthFormat() {

            Date approvalDate = new Date();
            approvalDate.setDay("12");
            approvalDate.setMonth("Mar");
            approvalDate.setYear("2018");

            CicApproval cicApproval = new CicApproval();
            cicApproval.setDate(approvalDate);

            List<ValidationError> validationErrors = cicApprovalService.validateCicApprovalDate(cicApproval);

            assertNotNull(validationErrors);
            assertEquals(1, validationErrors.size());
            assertEquals(DATE_FORMAT_INVALID, validationErrors.get(0).getMessageKey());
        }

        @Test
        @DisplayName("Validate Approval Date - Invalid Year Format")
        void validateApprovalDateInvalidYearFormat() {

            Date approvalDate = new Date();
            approvalDate.setDay("12");
            approvalDate.setMonth("3");
            approvalDate.setYear("18");

            CicApproval cicApproval = new CicApproval();
            cicApproval.setDate(approvalDate);

            List<ValidationError> validationErrors = cicApprovalService.validateCicApprovalDate(cicApproval);

            assertNotNull(validationErrors);
            assertEquals(1, validationErrors.size());
            assertEquals(DATE_FORMAT_INVALID, validationErrors.get(0).getMessageKey());
        }

        @Test
        @DisplayName("Validate Approval Date - Invalid Date")
        void validateApprovalDateInvalidDate() {

            Date approvalDate = new Date();
            approvalDate.setDay("12");
            approvalDate.setMonth("13");
            approvalDate.setYear("2018");

            CicApproval cicApproval = new CicApproval();
            cicApproval.setDate(approvalDate);

            when(cicApprovalTransformer.getCicApprovalDate(cicApproval)).thenThrow(DateTimeParseException.class);

            List<ValidationError> validationErrors = cicApprovalService.validateCicApprovalDate(cicApproval);

            assertNotNull(validationErrors);
            assertEquals(1, validationErrors.size());
            assertEquals(DATE_INVALID, validationErrors.get(0).getMessageKey());
        }

        @Test
        @DisplayName("Validate Approval Date - Valid Date")
        void validateApprovalDateValidDate() {

            Date approvalDate = new Date();
            approvalDate.setDay("12");
            approvalDate.setMonth("10");
            approvalDate.setYear("2018");

            CicApproval cicApproval = new CicApproval();
            cicApproval.setDate(approvalDate);

            when(cicApprovalTransformer.getCicApprovalDate(cicApproval)).thenReturn(LocalDate.now());

            List<ValidationError> validationErrors = cicApprovalService.validateCicApprovalDate(cicApproval);

            assertEquals(0, validationErrors.size());
        }
    }
