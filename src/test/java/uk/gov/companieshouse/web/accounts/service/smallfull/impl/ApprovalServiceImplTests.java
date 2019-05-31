package uk.gov.companieshouse.web.accounts.service.smallfull.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

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
import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.error.ApiErrorResponseException;
import uk.gov.companieshouse.api.handler.exception.URIValidationException;
import uk.gov.companieshouse.api.handler.smallfull.SmallFullResourceHandler;
import uk.gov.companieshouse.api.handler.smallfull.approval.ApprovalResourceHandler;
import uk.gov.companieshouse.api.handler.smallfull.approval.request.ApprovalCreate;
import uk.gov.companieshouse.api.handler.smallfull.approval.request.ApprovalUpdate;
import uk.gov.companieshouse.api.model.ApiResponse;
import uk.gov.companieshouse.api.model.accounts.smallfull.ApprovalApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.SmallFullApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.SmallFullLinks;
import uk.gov.companieshouse.web.accounts.api.ApiClientService;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.smallfull.Approval;
import uk.gov.companieshouse.web.accounts.model.smallfull.ApprovalDate;
import uk.gov.companieshouse.web.accounts.service.smallfull.ApprovalService;
import uk.gov.companieshouse.web.accounts.service.smallfull.SmallFullService;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.ApprovalTransformer;
import uk.gov.companieshouse.web.accounts.util.ValidationContext;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;
import uk.gov.companieshouse.web.accounts.validation.helper.ServiceExceptionHandler;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ApprovalServiceImplTests {

    @Mock
    private ApprovalTransformer approvalTransformer;

    @Mock
    private ApiClientService apiClientService;

    @Mock
    private ApiClient apiClient;

    @Mock
    private SmallFullResourceHandler smallFullResourceHandler;

    @Mock
    private ApprovalResourceHandler approvalResourceHandler;

    @Mock
    private ApprovalCreate approvalCreate;

    @Mock
    private ApprovalUpdate approvalUpdate;

    @Mock
    private SmallFullService smallFullService;

    @Mock
    private SmallFullApi smallFullApi;

    @Mock
    private SmallFullLinks smallFullLinks;

    @Mock
    private ServiceExceptionHandler serviceExceptionHandler;

    @Mock
    private ValidationContext validationContext;

    @Mock
    private ApiResponse<ApprovalApi> responseWithData;

    @Mock
    private ApiResponse<Void> responseNoData;

    @Mock
    private ApprovalApi approvalApi;

    @Mock
    private Approval approval;

    @Mock
    private URIValidationException uriValidationException;

    @Mock
    private ApiErrorResponseException apiErrorResponseException;

    @Mock
    private List<ValidationError> mockValidationErrors;

    @InjectMocks
    private ApprovalService approvalService = new ApprovalServiceImpl();

    private static final String TRANSACTION_ID = "transactionId";

    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";

    private static final String APPROVAL_URI = "/transactions/" + TRANSACTION_ID + "/company-accounts/" +
                                                COMPANY_ACCOUNTS_ID + "/small-full/approval";

    private static final String APPROVAL_DATE_ERROR_LOCATION = ".approval.date";

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

        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);

        when(smallFullResourceHandler.approval()).thenReturn(approvalResourceHandler);

        when(smallFullService.getSmallFullAccounts(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(smallFullApi);

        when(smallFullApi.getLinks()).thenReturn(smallFullLinks);

        when(smallFullLinks.getApproval()).thenReturn(null);

        when(approvalTransformer.getApprovalApi(approval)).thenReturn(approvalApi);

        when(approvalResourceHandler.create(APPROVAL_URI, approvalApi)).thenReturn(approvalCreate);

        when(approvalCreate.execute()).thenReturn(responseWithData);

        when(responseWithData.hasErrors()).thenReturn(false);

        List<ValidationError> validationErrors =
                approvalService.submitApproval(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, approval);

        assertTrue(validationErrors.isEmpty());
    }

    @Test
    @DisplayName("Submit Approval - POST - URIValidationException Thrown")
    void createApprovalThrowsURIValidationException() throws ApiErrorResponseException, URIValidationException,
                ServiceException {

        when(apiClientService.getApiClient()).thenReturn(apiClient);

        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);

        when(smallFullResourceHandler.approval()).thenReturn(approvalResourceHandler);

        when(smallFullService.getSmallFullAccounts(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(smallFullApi);

        when(smallFullApi.getLinks()).thenReturn(smallFullLinks);

        when(smallFullLinks.getApproval()).thenReturn(null);

        when(approvalTransformer.getApprovalApi(approval)).thenReturn(approvalApi);

        when(approvalResourceHandler.create(APPROVAL_URI, approvalApi)).thenReturn(approvalCreate);

        when(approvalCreate.execute()).thenThrow(uriValidationException);

        doThrow(ServiceException.class)
                .when(serviceExceptionHandler)
                        .handleURIValidationException(uriValidationException, RESOURCE_NAME);

        assertThrows(ServiceException.class, () ->
                approvalService.submitApproval(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, approval));
    }

    @Test
    @DisplayName("Submit Approval - POST - Validation Errors")
    void createApprovalWithValidationErrors()
            throws ApiErrorResponseException, URIValidationException, ServiceException {

        when(apiClientService.getApiClient()).thenReturn(apiClient);

        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);

        when(smallFullResourceHandler.approval()).thenReturn(approvalResourceHandler);

        when(smallFullService.getSmallFullAccounts(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(smallFullApi);

        when(smallFullApi.getLinks()).thenReturn(smallFullLinks);

        when(smallFullLinks.getApproval()).thenReturn(null);

        when(approvalTransformer.getApprovalApi(approval)).thenReturn(approvalApi);

        when(approvalResourceHandler.create(APPROVAL_URI, approvalApi)).thenReturn(approvalCreate);

        when(approvalCreate.execute()).thenReturn(responseWithData);

        when(responseWithData.hasErrors()).thenReturn(true);

        when(validationContext.getValidationErrors(responseWithData.getErrors()))
                .thenReturn(mockValidationErrors);

        List<ValidationError> validationErrors =
                approvalService.submitApproval(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, approval);

        assertEquals(mockValidationErrors, validationErrors);
    }

    @Test
    @DisplayName("Submit Approval - POST - ApiErrorResponseException Thrown")
    void createApprovalThrowsApiErrorResponseException()
            throws ApiErrorResponseException, URIValidationException, ServiceException {

        when(apiClientService.getApiClient()).thenReturn(apiClient);

        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);

        when(smallFullResourceHandler.approval()).thenReturn(approvalResourceHandler);

        when(smallFullService.getSmallFullAccounts(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(smallFullApi);

        when(smallFullApi.getLinks()).thenReturn(smallFullLinks);

        when(smallFullLinks.getApproval()).thenReturn(null);

        when(approvalTransformer.getApprovalApi(approval)).thenReturn(approvalApi);

        when(approvalResourceHandler.create(APPROVAL_URI, approvalApi)).thenReturn(approvalCreate);

        when(approvalCreate.execute()).thenThrow(apiErrorResponseException);

        doThrow(ServiceException.class)
                .when(serviceExceptionHandler)
                        .handleSubmissionException(apiErrorResponseException, RESOURCE_NAME);

        assertThrows(ServiceException.class, () ->
                approvalService.submitApproval(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, approval));
    }

    @Test
    @DisplayName("Submit Approval - PUT - Success Path")
    void updateApprovalSuccess() throws ApiErrorResponseException, URIValidationException, ServiceException {

        when(apiClientService.getApiClient()).thenReturn(apiClient);

        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);

        when(smallFullResourceHandler.approval()).thenReturn(approvalResourceHandler);

        when(smallFullService.getSmallFullAccounts(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(smallFullApi);

        when(smallFullApi.getLinks()).thenReturn(smallFullLinks);

        when(smallFullLinks.getApproval()).thenReturn(MOCK_APPROVAL_LINK);

        when(approvalTransformer.getApprovalApi(approval)).thenReturn(approvalApi);

        when(approvalResourceHandler.update(APPROVAL_URI, approvalApi)).thenReturn(approvalUpdate);

        when(approvalUpdate.execute()).thenReturn(responseNoData);

        when(responseNoData.hasErrors()).thenReturn(false);

        List<ValidationError> validationErrors =
                approvalService.submitApproval(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, approval);

        assertTrue(validationErrors.isEmpty());
    }

    @Test
    @DisplayName("Submit Approval - PUT - URIValidationException Thrown")
    void updateApprovalURIValidationExceptionThrown() throws ApiErrorResponseException, URIValidationException,
                    ServiceException {

        when(apiClientService.getApiClient()).thenReturn(apiClient);

        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);

        when(smallFullResourceHandler.approval()).thenReturn(approvalResourceHandler);

        when(smallFullService.getSmallFullAccounts(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(smallFullApi);

        when(smallFullApi.getLinks()).thenReturn(smallFullLinks);

        when(smallFullLinks.getApproval()).thenReturn(MOCK_APPROVAL_LINK);

        when(approvalTransformer.getApprovalApi(approval)).thenReturn(approvalApi);

        when(approvalResourceHandler.update(APPROVAL_URI, approvalApi)).thenReturn(approvalUpdate);

        when(approvalUpdate.execute()).thenThrow(uriValidationException);

        doThrow(ServiceException.class)
                .when(serviceExceptionHandler)
                    .handleURIValidationException(uriValidationException, RESOURCE_NAME);

        assertThrows(ServiceException.class, () ->
                approvalService.submitApproval(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, approval));
    }

    @Test
    @DisplayName("Submit Approval - PUT - Validation Errors")
    void updateApprovalWithValidationErrors()
            throws ApiErrorResponseException, URIValidationException, ServiceException {

        when(apiClientService.getApiClient()).thenReturn(apiClient);

        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);

        when(smallFullResourceHandler.approval()).thenReturn(approvalResourceHandler);

        when(smallFullService.getSmallFullAccounts(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(smallFullApi);

        when(smallFullApi.getLinks()).thenReturn(smallFullLinks);

        when(smallFullLinks.getApproval()).thenReturn(MOCK_APPROVAL_LINK);

        when(approvalTransformer.getApprovalApi(approval)).thenReturn(approvalApi);

        when(approvalResourceHandler.update(APPROVAL_URI, approvalApi)).thenReturn(approvalUpdate);

        when(approvalUpdate.execute()).thenReturn(responseNoData);

        when(responseNoData.hasErrors()).thenReturn(true);

        when(validationContext.getValidationErrors(responseNoData.getErrors()))
                .thenReturn(mockValidationErrors);

        List<ValidationError> validationErrors =
                approvalService.submitApproval(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, approval);

        assertEquals(mockValidationErrors, validationErrors);
    }

    @Test
    @DisplayName("Submit Approval - PUT - ApiErrorResponseException Thrown")
    void updateApprovalThrowsApiErrorResponseException()
            throws ApiErrorResponseException, URIValidationException, ServiceException {

        when(apiClientService.getApiClient()).thenReturn(apiClient);

        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);

        when(smallFullResourceHandler.approval()).thenReturn(approvalResourceHandler);

        when(smallFullService.getSmallFullAccounts(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(smallFullApi);

        when(smallFullApi.getLinks()).thenReturn(smallFullLinks);

        when(approvalTransformer.getApprovalApi(approval)).thenReturn(approvalApi);

        when(approvalResourceHandler.update(APPROVAL_URI, approvalApi)).thenReturn(approvalUpdate);

        when(smallFullLinks.getApproval()).thenReturn(MOCK_APPROVAL_LINK);

        when(approvalUpdate.execute()).thenThrow(apiErrorResponseException);

        doThrow(ServiceException.class)
                .when(serviceExceptionHandler)
                        .handleSubmissionException(apiErrorResponseException, RESOURCE_NAME);

        assertThrows(ServiceException.class, () ->
                approvalService.submitApproval(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, approval));
    }

    @Test
    @DisplayName("Validate Approval Date - No Fields Provided")
    void validateApprovalDateNoFieldsProvided() {

        ApprovalDate approvalDate = new ApprovalDate();

        Approval approval = new Approval();
        approval.setDate(approvalDate);

        List<ValidationError> validationErrors = approvalService.validateApprovalDate(approval);

        assertNotNull(validationErrors);
        assertEquals(1, validationErrors.size());
        assertEquals(DATE_MISSING, validationErrors.get(0).getMessageKey());
    }

    @Test
    @DisplayName("Validate Approval Date - Day Not Provided")
    void validateApprovalDateDayNotProvided() {

        ApprovalDate approvalDate = new ApprovalDate();
        approvalDate.setMonth("12");
        approvalDate.setYear("2018");

        Approval approval = new Approval();
        approval.setDate(approvalDate);

        List<ValidationError> validationErrors = approvalService.validateApprovalDate(approval);

        assertNotNull(validationErrors);
        assertEquals(1, validationErrors.size());
        assertEquals(DATE_INCOMPLETE, validationErrors.get(0).getMessageKey());
    }

    @Test
    @DisplayName("Validate Approval Date - Month Not Provided")
    void validateApprovalDateMonthNotProvided() {

        ApprovalDate approvalDate = new ApprovalDate();
        approvalDate.setDay("12");
        approvalDate.setYear("2018");

        Approval approval = new Approval();
        approval.setDate(approvalDate);

        List<ValidationError> validationErrors = approvalService.validateApprovalDate(approval);

        assertNotNull(validationErrors);
        assertEquals(1, validationErrors.size());
        assertEquals(DATE_INCOMPLETE, validationErrors.get(0).getMessageKey());
    }

    @Test
    @DisplayName("Validate Approval Date - Year Not Provided")
    void validateApprovalDateYearNotProvided() {

        ApprovalDate approvalDate = new ApprovalDate();
        approvalDate.setDay("12");
        approvalDate.setMonth("12");

        Approval approval = new Approval();
        approval.setDate(approvalDate);

        List<ValidationError> validationErrors = approvalService.validateApprovalDate(approval);

        assertNotNull(validationErrors);
        assertEquals(1, validationErrors.size());
        assertEquals(DATE_INCOMPLETE, validationErrors.get(0).getMessageKey());
    }

    @Test
    @DisplayName("Validate Approval Date - Invalid Day Format")
    void validateApprovalDateInvalidDayFormat() {

        ApprovalDate approvalDate = new ApprovalDate();
        approvalDate.setDay("1st");
        approvalDate.setMonth("3");
        approvalDate.setYear("2018");

        Approval approval = new Approval();
        approval.setDate(approvalDate);

        List<ValidationError> validationErrors = approvalService.validateApprovalDate(approval);

        assertNotNull(validationErrors);
        assertEquals(1, validationErrors.size());
        assertEquals(DATE_FORMAT_INVALID, validationErrors.get(0).getMessageKey());
    }

    @Test
    @DisplayName("Validate Approval Date - Invalid Month Format")
    void validateApprovalDateInvalidMonthFormat() {

        ApprovalDate approvalDate = new ApprovalDate();
        approvalDate.setDay("12");
        approvalDate.setMonth("Mar");
        approvalDate.setYear("2018");

        Approval approval = new Approval();
        approval.setDate(approvalDate);

        List<ValidationError> validationErrors = approvalService.validateApprovalDate(approval);

        assertNotNull(validationErrors);
        assertEquals(1, validationErrors.size());
        assertEquals(DATE_FORMAT_INVALID, validationErrors.get(0).getMessageKey());
    }

    @Test
    @DisplayName("Validate Approval Date - Invalid Year Format")
    void validateApprovalDateInvalidYearFormat() {

        ApprovalDate approvalDate = new ApprovalDate();
        approvalDate.setDay("12");
        approvalDate.setMonth("3");
        approvalDate.setYear("18");

        Approval approval = new Approval();
        approval.setDate(approvalDate);

        List<ValidationError> validationErrors = approvalService.validateApprovalDate(approval);

        assertNotNull(validationErrors);
        assertEquals(1, validationErrors.size());
        assertEquals(DATE_FORMAT_INVALID, validationErrors.get(0).getMessageKey());
    }

    @Test
    @DisplayName("Validate Approval Date - Invalid Date")
    void validateApprovalDateInvalidDate() {

        ApprovalDate approvalDate = new ApprovalDate();
        approvalDate.setDay("12");
        approvalDate.setMonth("13");
        approvalDate.setYear("2018");

        Approval approval = new Approval();
        approval.setDate(approvalDate);

        when(approvalTransformer.getApprovalDate(approval)).thenThrow(DateTimeParseException.class);

        List<ValidationError> validationErrors = approvalService.validateApprovalDate(approval);

        assertNotNull(validationErrors);
        assertEquals(1, validationErrors.size());
        assertEquals(DATE_INVALID, validationErrors.get(0).getMessageKey());
    }

    @Test
    @DisplayName("Validate Approval Date - Valid Date")
    void validateApprovalDateValidDate() {

        ApprovalDate approvalDate = new ApprovalDate();
        approvalDate.setDay("12");
        approvalDate.setMonth("10");
        approvalDate.setYear("2018");

        Approval approval = new Approval();
        approval.setDate(approvalDate);

        when(approvalTransformer.getApprovalDate(approval)).thenReturn(LocalDate.now());

        List<ValidationError> validationErrors = approvalService.validateApprovalDate(approval);

        assertEquals(0, validationErrors.size());
    }

}
