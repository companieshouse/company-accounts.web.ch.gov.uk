package uk.gov.companieshouse.web.accounts.service.cic.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.util.List;
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
import uk.gov.companieshouse.web.accounts.service.cic.CicApprovalService;
import uk.gov.companieshouse.web.accounts.service.cic.CicReportService;
import uk.gov.companieshouse.web.accounts.transformer.cic.CicApprovalTransformer;
import uk.gov.companieshouse.web.accounts.util.ValidationContext;
import uk.gov.companieshouse.web.accounts.validation.DateValidator;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;
import uk.gov.companieshouse.web.accounts.validation.helper.ServiceExceptionHandler;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CicApprovalServiceImplTest {

    @Mock
    private CicApprovalTransformer cicApprovalTransformer;

    @Mock
    private DateValidator dateValidator;

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
    private CicApproval cicApproval;

    @Mock
    private CicApprovalApi cicApprovalApi;

    @Mock
    private CicReportApi cicReportApi;

    @Mock
    private CicReportLinks cicReportLinks;

    @Mock
    private ServiceExceptionHandler serviceExceptionHandler;

    @Mock
    private ValidationContext validationContext;

    @Mock
    private List<ValidationError> mockValidationErrors;

    @Mock
    private ApiErrorResponseException apiErrorResponseException;

    @Mock
    private URIValidationException uriValidationException;

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

    private static final String MOCK_APPROVAL_LINK = "mockApprovalLink";

    private static final String DATE_FIELD_PATH = "date";

    private static final String DATE_JSON_PATH_SUFFIX = ".cic_approval.date";

    private static final String RESOURCE_NAME = "cic approval";

    @Test
    @DisplayName("Submit Approval - POST - Success Path")
    void createApprovalSuccess() throws ApiErrorResponseException, URIValidationException, ServiceException {

        when(dateValidator.validateDate(cicApproval.getDate(), DATE_FIELD_PATH, DATE_JSON_PATH_SUFFIX)).thenReturn(mockValidationErrors);

        when(mockValidationErrors.isEmpty()).thenReturn(true);

        when(apiClientService.getApiClient()).thenReturn(apiClient);

        when(apiClient.cicReport()).thenReturn(cicReportResourceHandler);

        when(cicReportResourceHandler.approval()).thenReturn(cicApprovalResourceHandler);

        when(cicReportService.getCicReport(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
            .thenReturn(cicReportApi);

        when(cicReportApi.getLinks()).thenReturn(cicReportLinks);

        when(cicApprovalTransformer.getCicApprovalApi(cicApproval)).thenReturn(cicApprovalApi);

        when(cicApprovalResourceHandler.create(APPROVAL_URI, cicApprovalApi)).thenReturn(cicApprovalCreate);

        when(cicReportLinks.getApproval()).thenReturn(null);

        when(cicApprovalCreate.execute()).thenReturn(mockApiResponse);

        List<ValidationError> validationErrors =
            cicApprovalService.submitCicApproval(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, cicApproval);

        assertTrue(validationErrors.isEmpty());
    }

    @Test
    @DisplayName("Submit Approval - POST - Date Validation Errors")
    void createApprovalDateValidationErrors() throws ServiceException {

        when(dateValidator.validateDate(cicApproval.getDate(), DATE_FIELD_PATH, DATE_JSON_PATH_SUFFIX)).thenReturn(mockValidationErrors);

        when(mockValidationErrors.isEmpty()).thenReturn(false);

        List<ValidationError> validationErrors =
                cicApprovalService.submitCicApproval(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, cicApproval);

        assertEquals(mockValidationErrors, validationErrors);
    }

    @Test
    @DisplayName("Submit Approval - POST - URIValidationException Thrown")
    void createApprovalThrowsURIValidationException() throws ApiErrorResponseException, URIValidationException,
        ServiceException {

        when(dateValidator.validateDate(cicApproval.getDate(), DATE_FIELD_PATH, DATE_JSON_PATH_SUFFIX)).thenReturn(mockValidationErrors);

        when(mockValidationErrors.isEmpty()).thenReturn(true);

        when(apiClientService.getApiClient()).thenReturn(apiClient);

        when(apiClient.cicReport()).thenReturn(cicReportResourceHandler);

        when(cicReportResourceHandler.approval()).thenReturn(cicApprovalResourceHandler);

        when(cicReportService.getCicReport(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
            .thenReturn(cicReportApi);

        when(cicReportApi.getLinks()).thenReturn(cicReportLinks);

        when(cicApprovalTransformer.getCicApprovalApi(cicApproval)).thenReturn(cicApprovalApi);

        when(cicApprovalResourceHandler.create(APPROVAL_URI, cicApprovalApi)).thenReturn(cicApprovalCreate);

        when(cicReportLinks.getApproval()).thenReturn(null);

        when(cicApprovalCreate.execute()).thenThrow(uriValidationException);

        doThrow(ServiceException.class)
                .when(serviceExceptionHandler)
                        .handleURIValidationException(uriValidationException, RESOURCE_NAME);

        assertThrows(ServiceException.class, () ->
                cicApprovalService.submitCicApproval(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, cicApproval));
    }

    @Test
    @DisplayName("Submit Approval - POST - Validation Errors")
    void createApprovalReturnsValidationErrors()
        throws ApiErrorResponseException, URIValidationException, ServiceException {

        when(dateValidator.validateDate(cicApproval.getDate(), DATE_FIELD_PATH, DATE_JSON_PATH_SUFFIX)).thenReturn(mockValidationErrors);

        when(mockValidationErrors.isEmpty()).thenReturn(true);

        when(apiClientService.getApiClient()).thenReturn(apiClient);

        when(apiClient.cicReport()).thenReturn(cicReportResourceHandler);

        when(cicReportResourceHandler.approval()).thenReturn(cicApprovalResourceHandler);

        when(cicReportService.getCicReport(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
            .thenReturn(cicReportApi);

        when(cicReportApi.getLinks()).thenReturn(cicReportLinks);

        when(cicApprovalTransformer.getCicApprovalApi(cicApproval)).thenReturn(cicApprovalApi);

        when(cicApprovalResourceHandler.create(APPROVAL_URI, cicApprovalApi)).thenReturn(cicApprovalCreate);

        when(cicReportLinks.getApproval()).thenReturn(null);

        when(cicApprovalCreate.execute()).thenReturn(mockApiResponse);

        when(mockApiResponse.hasErrors()).thenReturn(true);

        when(validationContext.getValidationErrors(mockApiResponse.getErrors())).thenReturn(mockValidationErrors);

        List<ValidationError> validationErrors = cicApprovalService.submitCicApproval(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, cicApproval);

        assertEquals(mockValidationErrors, validationErrors);
    }

    @Test
    @DisplayName("Submit Approval - POST - Generic ApiErrorResponseException Thrown")
    void createApprovalThrowsGenericApiErrorResponseException()
        throws ApiErrorResponseException, URIValidationException, ServiceException {

        when(dateValidator.validateDate(cicApproval.getDate(), DATE_FIELD_PATH, DATE_JSON_PATH_SUFFIX)).thenReturn(mockValidationErrors);

        when(mockValidationErrors.isEmpty()).thenReturn(true);

        when(apiClientService.getApiClient()).thenReturn(apiClient);

        when(apiClient.cicReport()).thenReturn(cicReportResourceHandler);

        when(cicReportResourceHandler.approval()).thenReturn(cicApprovalResourceHandler);

        when(cicReportService.getCicReport(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
            .thenReturn(cicReportApi);

        when(cicReportApi.getLinks()).thenReturn(cicReportLinks);

        when(cicApprovalTransformer.getCicApprovalApi(cicApproval)).thenReturn(cicApprovalApi);

        when(cicApprovalResourceHandler.create(APPROVAL_URI, cicApprovalApi)).thenReturn(cicApprovalCreate);

        when(cicReportLinks.getApproval()).thenReturn(null);

        when(cicApprovalCreate.execute()).thenThrow(apiErrorResponseException);

        doThrow(ServiceException.class)
                .when(serviceExceptionHandler)
                        .handleSubmissionException(apiErrorResponseException, RESOURCE_NAME);

        assertThrows(ServiceException.class, () ->
                cicApprovalService.submitCicApproval(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, cicApproval));
    }

    @Test
    @DisplayName("Submit Approval - PUT - Success Path")
    void updateApprovalSuccess() throws ApiErrorResponseException, URIValidationException, ServiceException {

        when(dateValidator.validateDate(cicApproval.getDate(), DATE_FIELD_PATH, DATE_JSON_PATH_SUFFIX)).thenReturn(mockValidationErrors);

        when(mockValidationErrors.isEmpty()).thenReturn(true);

        when(apiClientService.getApiClient()).thenReturn(apiClient);

        when(apiClient.cicReport()).thenReturn(cicReportResourceHandler);

        when(cicReportResourceHandler.approval()).thenReturn(cicApprovalResourceHandler);

        when(cicReportService.getCicReport(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
            .thenReturn(cicReportApi);

        when(cicReportApi.getLinks()).thenReturn(cicReportLinks);

        when(cicApprovalTransformer.getCicApprovalApi(cicApproval)).thenReturn(cicApprovalApi);

        when(cicApprovalResourceHandler.update(APPROVAL_URI, cicApprovalApi)).thenReturn(cicApprovalUpdate);

        when(cicReportLinks.getApproval()).thenReturn(MOCK_APPROVAL_LINK);

        when(cicApprovalUpdate.execute()).thenReturn(mockVoidApiResponse);

        List<ValidationError> validationErrors =
            cicApprovalService.submitCicApproval(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, cicApproval);

        assertTrue(validationErrors.isEmpty());
    }

    @Test
    @DisplayName("Submit Approval - PUT - URIValidationException Thrown")
    void updateApprovalURIValidationExceptionThrown() throws ApiErrorResponseException, URIValidationException,
        ServiceException {

        when(dateValidator.validateDate(cicApproval.getDate(), DATE_FIELD_PATH, DATE_JSON_PATH_SUFFIX)).thenReturn(mockValidationErrors);

        when(mockValidationErrors.isEmpty()).thenReturn(true);

        when(apiClientService.getApiClient()).thenReturn(apiClient);

        when(apiClient.cicReport()).thenReturn(cicReportResourceHandler);

        when(cicReportResourceHandler.approval()).thenReturn(cicApprovalResourceHandler);

        when(cicReportService.getCicReport(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
            .thenReturn(cicReportApi);

        when(cicReportApi.getLinks()).thenReturn(cicReportLinks);

        when(cicApprovalTransformer.getCicApprovalApi(cicApproval)).thenReturn(cicApprovalApi);

        when(cicApprovalResourceHandler.update(APPROVAL_URI, cicApprovalApi)).thenReturn(cicApprovalUpdate);

        when(cicReportLinks.getApproval()).thenReturn(MOCK_APPROVAL_LINK);

        doThrow(uriValidationException).when(cicApprovalUpdate).execute();

        doThrow(ServiceException.class)
                .when(serviceExceptionHandler)
                        .handleURIValidationException(uriValidationException, RESOURCE_NAME);

        assertThrows(ServiceException.class, () ->
                cicApprovalService.submitCicApproval(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, cicApproval));
    }

    @Test
    @DisplayName("Submit Approval - PUT - Validation Errors")
    void updateApprovalReturnsValidationErrors()
        throws ApiErrorResponseException, URIValidationException, ServiceException {

        when(dateValidator.validateDate(cicApproval.getDate(), DATE_FIELD_PATH, DATE_JSON_PATH_SUFFIX)).thenReturn(mockValidationErrors);

        when(mockValidationErrors.isEmpty()).thenReturn(true);

        when(apiClientService.getApiClient()).thenReturn(apiClient);

        when(apiClient.cicReport()).thenReturn(cicReportResourceHandler);

        when(cicReportResourceHandler.approval()).thenReturn(cicApprovalResourceHandler);

        when(cicReportService.getCicReport(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
            .thenReturn(cicReportApi);

        when(cicReportApi.getLinks()).thenReturn(cicReportLinks);

        when(cicApprovalTransformer.getCicApprovalApi(cicApproval)).thenReturn(cicApprovalApi);

        when(cicApprovalResourceHandler.update(APPROVAL_URI, cicApprovalApi)).thenReturn(cicApprovalUpdate);

        when(cicReportLinks.getApproval()).thenReturn(MOCK_APPROVAL_LINK);

        when(cicApprovalUpdate.execute()).thenReturn(mockVoidApiResponse);

        when(mockVoidApiResponse.hasErrors()).thenReturn(true);

        when(validationContext.getValidationErrors(mockVoidApiResponse.getErrors())).thenReturn(mockValidationErrors);

        List<ValidationError> validationErrors = cicApprovalService.submitCicApproval(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, cicApproval);

        assertEquals(mockValidationErrors, validationErrors);
    }

    @Test
    @DisplayName("Submit Approval - PUT - Generic ApiErrorResponseException Thrown")
    void updateApprovalThrowsGenericApiErrorResponseException()
        throws ApiErrorResponseException, URIValidationException, ServiceException {

        when(dateValidator.validateDate(cicApproval.getDate(), DATE_FIELD_PATH, DATE_JSON_PATH_SUFFIX)).thenReturn(mockValidationErrors);

        when(mockValidationErrors.isEmpty()).thenReturn(true);

        when(apiClientService.getApiClient()).thenReturn(apiClient);

        when(apiClient.cicReport()).thenReturn(cicReportResourceHandler);

        when(cicReportResourceHandler.approval()).thenReturn(cicApprovalResourceHandler);

        when(cicReportService.getCicReport(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
            .thenReturn(cicReportApi);

        when(cicReportApi.getLinks()).thenReturn(cicReportLinks);

        when(cicApprovalTransformer.getCicApprovalApi(cicApproval)).thenReturn(cicApprovalApi);

        when(cicApprovalResourceHandler.update(APPROVAL_URI, cicApprovalApi)).thenReturn(cicApprovalUpdate);

        when(cicReportLinks.getApproval()).thenReturn(MOCK_APPROVAL_LINK);

        doThrow(apiErrorResponseException).when(cicApprovalUpdate).execute();

        doThrow(ServiceException.class)
                .when(serviceExceptionHandler)
                .handleSubmissionException(apiErrorResponseException, RESOURCE_NAME);

        assertThrows(ServiceException.class, () ->
                cicApprovalService.submitCicApproval(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, cicApproval));
    }
}
