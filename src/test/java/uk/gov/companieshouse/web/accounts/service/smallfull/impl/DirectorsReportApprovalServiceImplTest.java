package uk.gov.companieshouse.web.accounts.service.smallfull.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
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
import uk.gov.companieshouse.api.handler.exception.URIValidationException;
import uk.gov.companieshouse.api.handler.smallfull.SmallFullResourceHandler;
import uk.gov.companieshouse.api.handler.smallfull.directorsreport.DirectorsReportResourceHandler;
import uk.gov.companieshouse.api.handler.smallfull.directorsreport.approval.ApprovalResourceHandler;
import uk.gov.companieshouse.api.handler.smallfull.directorsreport.approval.request.ApprovalCreate;
import uk.gov.companieshouse.api.handler.smallfull.directorsreport.approval.request.ApprovalGet;
import uk.gov.companieshouse.api.handler.smallfull.directorsreport.approval.request.ApprovalUpdate;
import uk.gov.companieshouse.api.model.ApiResponse;
import uk.gov.companieshouse.api.model.accounts.directorsreport.ApprovalApi;
import uk.gov.companieshouse.api.model.accounts.directorsreport.DirectorsReportApi;
import uk.gov.companieshouse.api.model.accounts.directorsreport.DirectorsReportLinks;
import uk.gov.companieshouse.web.accounts.api.ApiClientService;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.directorsreport.DirectorsReportApproval;
import uk.gov.companieshouse.web.accounts.service.smallfull.DirectorsReportApprovalService;
import uk.gov.companieshouse.web.accounts.service.smallfull.DirectorsReportService;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.directorsreport.DirectorsReportApprovalTransformer;
import uk.gov.companieshouse.web.accounts.util.ValidationContext;
import uk.gov.companieshouse.web.accounts.validation.DateValidator;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;
import uk.gov.companieshouse.web.accounts.validation.helper.ServiceExceptionHandler;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DirectorsReportApprovalServiceImplTest {

    @Mock
    private DirectorsReportApprovalTransformer directorsReportApprovalTransformer;

    @Mock
    private DateValidator dateValidator;

    @Mock
    private ApiClientService apiClientService;

    @Mock
    private DirectorsReportService directorsReportReportService;

    @Mock
    private ApiClient apiClient;
    
    @Mock
    private SmallFullResourceHandler smallFullResourceHandler;

    @Mock
    private DirectorsReportResourceHandler directorsReportResourceHandler;

    @Mock
    private ApprovalResourceHandler approvalResourceHandler;

    @Mock
    private ApprovalCreate approvalCreate;

    @Mock
    private ApprovalUpdate approvalUpdate;

    @Mock
    private ApprovalGet approvalGet;

    @Mock
    private DirectorsReportApproval directorsReportApproval;

    @Mock
    private ApprovalApi approvalApi;

    @Mock
    private DirectorsReportApi directorsReportApi;

    @Mock
    private DirectorsReportLinks directorsReportLinks;

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
    private ApiResponse<ApprovalApi> mockApiResponse;

    @Mock
    private ApiResponse<Void> mockVoidApiResponse;

    @InjectMocks
    private DirectorsReportApprovalService directorsReportApprovalService = new DirectorsReportApprovalServiceImpl();

    private static final String TRANSACTION_ID = "transactionId";

    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";

    private static final String APPROVAL_URI = "/transactions/" + TRANSACTION_ID + "/company-accounts/" +
        COMPANY_ACCOUNTS_ID + "/small-full/directors-report/approval";

    private static final String MOCK_APPROVAL_LINK = "mockApprovalLink";

    private static final String DATE_FIELD_PATH = "date";

    private static final String DATE_JSON_PATH_SUFFIX = ".directors_approval.date";

    private static final String RESOURCE_NAME = "directors report approval";

    @Test
    @DisplayName("Submit Approval - POST - Success Path")
    void createApprovalSuccess() throws ApiErrorResponseException, URIValidationException, ServiceException {

        when(dateValidator.validateDate(directorsReportApproval.getDate(), DATE_FIELD_PATH, DATE_JSON_PATH_SUFFIX)).thenReturn(mockValidationErrors);

        when(mockValidationErrors.isEmpty()).thenReturn(true);

        when(apiClientService.getApiClient()).thenReturn(apiClient);

        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);
        
        when(smallFullResourceHandler.directorsReport()).thenReturn(directorsReportResourceHandler);

        when(directorsReportResourceHandler.approval()).thenReturn(approvalResourceHandler);

        when(directorsReportReportService.getDirectorsReport(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
            .thenReturn(directorsReportApi);

        when(directorsReportApi.getLinks()).thenReturn(directorsReportLinks);

        when(directorsReportApprovalTransformer.getDirectorsReportApprovalApi(directorsReportApproval)).thenReturn(
                approvalApi);

        when(approvalResourceHandler.create(APPROVAL_URI, approvalApi)).thenReturn(
                approvalCreate);

        when(directorsReportLinks.getApproval()).thenReturn(null);

        when(approvalCreate.execute()).thenReturn(mockApiResponse);

        List<ValidationError> validationErrors =
            directorsReportApprovalService.submitDirectorsReportApproval(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, directorsReportApproval);

        assertTrue(validationErrors.isEmpty());
    }

    @Test
    @DisplayName("Submit Approval - POST - Date Validation Errors")
    void createApprovalDateValidationErrors() throws ServiceException {

        when(dateValidator.validateDate(directorsReportApproval.getDate(), DATE_FIELD_PATH, DATE_JSON_PATH_SUFFIX)).thenReturn(mockValidationErrors);

        when(mockValidationErrors.isEmpty()).thenReturn(false);

        List<ValidationError> validationErrors =
                directorsReportApprovalService.submitDirectorsReportApproval(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, directorsReportApproval);

        assertEquals(mockValidationErrors, validationErrors);
    }

    @Test
    @DisplayName("Submit Approval - POST - URIValidationException Thrown")
    void createApprovalThrowsURIValidationException() throws ApiErrorResponseException, URIValidationException,
        ServiceException {

        when(dateValidator.validateDate(directorsReportApproval.getDate(), DATE_FIELD_PATH, DATE_JSON_PATH_SUFFIX)).thenReturn(mockValidationErrors);

        when(mockValidationErrors.isEmpty()).thenReturn(true);

        when(apiClientService.getApiClient()).thenReturn(apiClient);

        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);
        
        when(smallFullResourceHandler.directorsReport()).thenReturn(directorsReportResourceHandler);

        when(directorsReportResourceHandler.approval()).thenReturn(approvalResourceHandler);

        when(directorsReportReportService.getDirectorsReport(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
            .thenReturn(directorsReportApi);

        when(directorsReportApi.getLinks()).thenReturn(directorsReportLinks);

        when(directorsReportApprovalTransformer.getDirectorsReportApprovalApi(directorsReportApproval)).thenReturn(
                approvalApi);

        when(approvalResourceHandler.create(APPROVAL_URI, approvalApi)).thenReturn(
                approvalCreate);

        when(directorsReportLinks.getApproval()).thenReturn(null);

        when(approvalCreate.execute()).thenThrow(uriValidationException);

        doThrow(ServiceException.class)
                .when(serviceExceptionHandler)
                        .handleURIValidationException(uriValidationException, RESOURCE_NAME);

        assertThrows(ServiceException.class, () ->
                directorsReportApprovalService.submitDirectorsReportApproval(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, directorsReportApproval));
    }

    @Test
    @DisplayName("Submit Approval - POST - Validation Errors")
    void createApprovalReturnsValidationErrors()
        throws ApiErrorResponseException, URIValidationException, ServiceException {

        when(dateValidator.validateDate(directorsReportApproval.getDate(), DATE_FIELD_PATH, DATE_JSON_PATH_SUFFIX)).thenReturn(mockValidationErrors);

        when(mockValidationErrors.isEmpty()).thenReturn(true);

        when(apiClientService.getApiClient()).thenReturn(apiClient);

        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);
        
        when(smallFullResourceHandler.directorsReport()).thenReturn(directorsReportResourceHandler);

        when(directorsReportResourceHandler.approval()).thenReturn(approvalResourceHandler);

        when(directorsReportReportService.getDirectorsReport(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
            .thenReturn(directorsReportApi);

        when(directorsReportApi.getLinks()).thenReturn(directorsReportLinks);

        when(directorsReportApprovalTransformer.getDirectorsReportApprovalApi(directorsReportApproval)).thenReturn(
                approvalApi);

        when(approvalResourceHandler.create(APPROVAL_URI, approvalApi)).thenReturn(
                approvalCreate);

        when(directorsReportLinks.getApproval()).thenReturn(null);

        when(approvalCreate.execute()).thenReturn(mockApiResponse);

        when(mockApiResponse.hasErrors()).thenReturn(true);

        when(validationContext.getValidationErrors(mockApiResponse.getErrors())).thenReturn(mockValidationErrors);

        List<ValidationError> validationErrors = directorsReportApprovalService.submitDirectorsReportApproval(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, directorsReportApproval);

        assertEquals(mockValidationErrors, validationErrors);
    }

    @Test
    @DisplayName("Submit Approval - POST - Generic ApiErrorResponseException Thrown")
    void createApprovalThrowsGenericApiErrorResponseException()
        throws ApiErrorResponseException, URIValidationException, ServiceException {

        when(dateValidator.validateDate(directorsReportApproval.getDate(), DATE_FIELD_PATH, DATE_JSON_PATH_SUFFIX)).thenReturn(mockValidationErrors);

        when(mockValidationErrors.isEmpty()).thenReturn(true);

        when(apiClientService.getApiClient()).thenReturn(apiClient);

        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);
        
        when(smallFullResourceHandler.directorsReport()).thenReturn(directorsReportResourceHandler);

        when(directorsReportResourceHandler.approval()).thenReturn(approvalResourceHandler);

        when(directorsReportReportService.getDirectorsReport(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
            .thenReturn(directorsReportApi);

        when(directorsReportApi.getLinks()).thenReturn(directorsReportLinks);

        when(directorsReportApprovalTransformer.getDirectorsReportApprovalApi(directorsReportApproval)).thenReturn(
                approvalApi);

        when(approvalResourceHandler.create(APPROVAL_URI, approvalApi)).thenReturn(
                approvalCreate);

        when(directorsReportLinks.getApproval()).thenReturn(null);

        when(approvalCreate.execute()).thenThrow(apiErrorResponseException);

        doThrow(ServiceException.class)
                .when(serviceExceptionHandler)
                        .handleSubmissionException(apiErrorResponseException, RESOURCE_NAME);

        assertThrows(ServiceException.class, () ->
                directorsReportApprovalService.submitDirectorsReportApproval(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, directorsReportApproval));
    }

    @Test
    @DisplayName("Submit Approval - PUT - Success Path")
    void updateApprovalSuccess() throws ApiErrorResponseException, URIValidationException, ServiceException {

        when(dateValidator.validateDate(directorsReportApproval.getDate(), DATE_FIELD_PATH, DATE_JSON_PATH_SUFFIX)).thenReturn(mockValidationErrors);

        when(mockValidationErrors.isEmpty()).thenReturn(true);

        when(apiClientService.getApiClient()).thenReturn(apiClient);

        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);
        
        when(smallFullResourceHandler.directorsReport()).thenReturn(directorsReportResourceHandler);

        when(directorsReportResourceHandler.approval()).thenReturn(approvalResourceHandler);

        when(directorsReportReportService.getDirectorsReport(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
            .thenReturn(directorsReportApi);

        when(directorsReportApi.getLinks()).thenReturn(directorsReportLinks);

        when(directorsReportApprovalTransformer.getDirectorsReportApprovalApi(directorsReportApproval)).thenReturn(
                approvalApi);

        when(approvalResourceHandler.update(APPROVAL_URI, approvalApi)).thenReturn(
                approvalUpdate);

        when(directorsReportLinks.getApproval()).thenReturn(MOCK_APPROVAL_LINK);

        when(approvalUpdate.execute()).thenReturn(mockVoidApiResponse);

        List<ValidationError> validationErrors =
            directorsReportApprovalService.submitDirectorsReportApproval(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, directorsReportApproval);

        assertTrue(validationErrors.isEmpty());
    }

    @Test
    @DisplayName("Submit Approval - PUT - URIValidationException Thrown")
    void updateApprovalURIValidationExceptionThrown() throws ApiErrorResponseException, URIValidationException,
        ServiceException {

        when(dateValidator.validateDate(directorsReportApproval.getDate(), DATE_FIELD_PATH, DATE_JSON_PATH_SUFFIX)).thenReturn(mockValidationErrors);

        when(mockValidationErrors.isEmpty()).thenReturn(true);

        when(apiClientService.getApiClient()).thenReturn(apiClient);

        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);
        
        when(smallFullResourceHandler.directorsReport()).thenReturn(directorsReportResourceHandler);

        when(directorsReportResourceHandler.approval()).thenReturn(approvalResourceHandler);

        when(directorsReportReportService.getDirectorsReport(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
            .thenReturn(directorsReportApi);

        when(directorsReportApi.getLinks()).thenReturn(directorsReportLinks);

        when(directorsReportApprovalTransformer.getDirectorsReportApprovalApi(directorsReportApproval)).thenReturn(
                approvalApi);

        when(approvalResourceHandler.update(APPROVAL_URI, approvalApi)).thenReturn(
                approvalUpdate);

        when(directorsReportLinks.getApproval()).thenReturn(MOCK_APPROVAL_LINK);

        doThrow(uriValidationException).when(approvalUpdate).execute();

        doThrow(ServiceException.class)
                .when(serviceExceptionHandler)
                        .handleURIValidationException(uriValidationException, RESOURCE_NAME);

        assertThrows(ServiceException.class, () ->
                directorsReportApprovalService.submitDirectorsReportApproval(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, directorsReportApproval));
    }

    @Test
    @DisplayName("Submit Approval - PUT - Validation Errors")
    void updateApprovalReturnsValidationErrors()
        throws ApiErrorResponseException, URIValidationException, ServiceException {

        when(dateValidator.validateDate(directorsReportApproval.getDate(), DATE_FIELD_PATH, DATE_JSON_PATH_SUFFIX)).thenReturn(mockValidationErrors);

        when(mockValidationErrors.isEmpty()).thenReturn(true);

        when(apiClientService.getApiClient()).thenReturn(apiClient);

        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);
        
        when(smallFullResourceHandler.directorsReport()).thenReturn(directorsReportResourceHandler);

        when(directorsReportResourceHandler.approval()).thenReturn(approvalResourceHandler);

        when(directorsReportReportService.getDirectorsReport(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
            .thenReturn(directorsReportApi);

        when(directorsReportApi.getLinks()).thenReturn(directorsReportLinks);

        when(directorsReportApprovalTransformer.getDirectorsReportApprovalApi(directorsReportApproval)).thenReturn(
                approvalApi);

        when(approvalResourceHandler.update(APPROVAL_URI, approvalApi)).thenReturn(
                approvalUpdate);

        when(directorsReportLinks.getApproval()).thenReturn(MOCK_APPROVAL_LINK);

        when(approvalUpdate.execute()).thenReturn(mockVoidApiResponse);

        when(mockVoidApiResponse.hasErrors()).thenReturn(true);

        when(validationContext.getValidationErrors(mockVoidApiResponse.getErrors())).thenReturn(mockValidationErrors);

        List<ValidationError> validationErrors = directorsReportApprovalService.submitDirectorsReportApproval(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, directorsReportApproval);

        assertEquals(mockValidationErrors, validationErrors);
    }

    @Test
    @DisplayName("Submit Approval - PUT - Generic ApiErrorResponseException Thrown")
    void updateApprovalThrowsGenericApiErrorResponseException()
        throws ApiErrorResponseException, URIValidationException, ServiceException {

        when(dateValidator.validateDate(directorsReportApproval.getDate(), DATE_FIELD_PATH, DATE_JSON_PATH_SUFFIX)).thenReturn(mockValidationErrors);

        when(mockValidationErrors.isEmpty()).thenReturn(true);

        when(apiClientService.getApiClient()).thenReturn(apiClient);

        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);
        
        when(smallFullResourceHandler.directorsReport()).thenReturn(directorsReportResourceHandler);

        when(directorsReportResourceHandler.approval()).thenReturn(approvalResourceHandler);

        when(directorsReportReportService.getDirectorsReport(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
            .thenReturn(directorsReportApi);

        when(directorsReportApi.getLinks()).thenReturn(directorsReportLinks);

        when(directorsReportApprovalTransformer.getDirectorsReportApprovalApi(directorsReportApproval)).thenReturn(
                approvalApi);

        when(approvalResourceHandler.update(APPROVAL_URI, approvalApi)).thenReturn(
                approvalUpdate);

        when(directorsReportLinks.getApproval()).thenReturn(MOCK_APPROVAL_LINK);

        doThrow(apiErrorResponseException).when(approvalUpdate).execute();

        doThrow(ServiceException.class)
                .when(serviceExceptionHandler)
                .handleSubmissionException(apiErrorResponseException, RESOURCE_NAME);

        assertThrows(ServiceException.class, () ->
                directorsReportApprovalService.submitDirectorsReportApproval(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, directorsReportApproval));
    }

    @Test
    @DisplayName("Get Approval - Success Path")
    void getApprovalSuccess() throws ApiErrorResponseException, URIValidationException, ServiceException {

        when(apiClientService.getApiClient()).thenReturn(apiClient);

        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);

        when(smallFullResourceHandler.directorsReport()).thenReturn(directorsReportResourceHandler);

        when(directorsReportResourceHandler.approval()).thenReturn(approvalResourceHandler);

        when(approvalResourceHandler.get(APPROVAL_URI)).thenReturn(approvalGet);

        when(approvalGet.execute()).thenReturn(mockApiResponse);

        when(mockApiResponse.getData()).thenReturn(approvalApi);

        when(directorsReportApprovalTransformer.getDirectorsReportApproval(approvalApi)).thenReturn(directorsReportApproval);

        assertEquals(directorsReportApproval,
                directorsReportApprovalService.getDirectorsReportApproval(TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }

    @Test
    @DisplayName("Get Approval - Not Found")
    void getApprovalNotFound() throws ApiErrorResponseException, URIValidationException, ServiceException {

        when(apiClientService.getApiClient()).thenReturn(apiClient);

        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);

        when(smallFullResourceHandler.directorsReport()).thenReturn(directorsReportResourceHandler);

        when(directorsReportResourceHandler.approval()).thenReturn(approvalResourceHandler);

        when(approvalResourceHandler.get(APPROVAL_URI)).thenReturn(approvalGet);

        when(approvalGet.execute()).thenThrow(apiErrorResponseException);

        doNothing().when(serviceExceptionHandler).handleRetrievalException(apiErrorResponseException, RESOURCE_NAME);

        assertNotNull(directorsReportApprovalService.getDirectorsReportApproval(TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }

    @Test
    @DisplayName("Get Approval - Generic ApiErrorResponseException Thrown")
    void getApprovalThrowsGenericApiErrorResponseException() throws ApiErrorResponseException, URIValidationException, ServiceException {

        when(apiClientService.getApiClient()).thenReturn(apiClient);

        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);

        when(smallFullResourceHandler.directorsReport()).thenReturn(directorsReportResourceHandler);

        when(directorsReportResourceHandler.approval()).thenReturn(approvalResourceHandler);

        when(approvalResourceHandler.get(APPROVAL_URI)).thenReturn(approvalGet);

        when(approvalGet.execute()).thenThrow(apiErrorResponseException);

        doThrow(ServiceException.class).when(serviceExceptionHandler).handleRetrievalException(apiErrorResponseException, RESOURCE_NAME);

        assertThrows(ServiceException.class, () ->
                directorsReportApprovalService.getDirectorsReportApproval(TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }

    @Test
    @DisplayName("Get Approval - URIValidationException Thrown")
    void getApprovalThrowsURIValidationException() throws ApiErrorResponseException, URIValidationException, ServiceException {

        when(apiClientService.getApiClient()).thenReturn(apiClient);

        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);

        when(smallFullResourceHandler.directorsReport()).thenReturn(directorsReportResourceHandler);

        when(directorsReportResourceHandler.approval()).thenReturn(approvalResourceHandler);

        when(approvalResourceHandler.get(APPROVAL_URI)).thenReturn(approvalGet);

        when(approvalGet.execute()).thenThrow(uriValidationException);

        doThrow(ServiceException.class).when(serviceExceptionHandler).handleURIValidationException(uriValidationException, RESOURCE_NAME);

        assertThrows(ServiceException.class, () ->
                directorsReportApprovalService.getDirectorsReportApproval(TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }
}
