package uk.gov.companieshouse.web.accounts.service.smallfull.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpResponseException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
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
import uk.gov.companieshouse.api.model.accounts.smallfull.ApprovalApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.SmallFullApi;
import uk.gov.companieshouse.web.accounts.api.ApiClientService;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.links.SmallFullLinkType;
import uk.gov.companieshouse.web.accounts.model.smallfull.Approval;
import uk.gov.companieshouse.web.accounts.model.smallfull.ApprovalDate;
import uk.gov.companieshouse.web.accounts.service.smallfull.ApprovalService;
import uk.gov.companieshouse.web.accounts.service.smallfull.SmallFullService;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.ApprovalTransformer;
import uk.gov.companieshouse.web.accounts.util.ValidationContext;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

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
    private Map<String, String> smallFullLinks;

    @Mock
    private ValidationContext validationContext;

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

    @Test
    @DisplayName("Submit Approval - POST - Success Path")
    void createApprovalSuccess() throws ApiErrorResponseException, URIValidationException, ServiceException {

        when(apiClientService.getApiClient()).thenReturn(apiClient);

        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);

        when(smallFullResourceHandler.approval()).thenReturn(approvalResourceHandler);

        when(smallFullService.getSmallFullAccounts(TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(smallFullApi);

        when(smallFullApi.getLinks()).thenReturn(smallFullLinks);

        Approval approval = new Approval();

        ApprovalApi approvalApi = new ApprovalApi();

        when(approvalTransformer.getApprovalApi(approval)).thenReturn(approvalApi);

        when(approvalResourceHandler.create(APPROVAL_URI, approvalApi)).thenReturn(approvalCreate);

        when(smallFullLinks.containsKey(SmallFullLinkType.APPROVAL.getLink()))
                .thenReturn(false);

        when(approvalCreate.execute()).thenReturn(approvalApi);

        List<ValidationError> validationErrors =
                approvalService.submitApproval(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, approval);

        verify(approvalCreate, times(1)).execute();

        assertTrue(validationErrors.isEmpty());
    }

    @Test
    @DisplayName("Submit Approval - POST - URIValidationException Thrown")
    void createApprovalThrowsURIValidationException() throws ApiErrorResponseException, URIValidationException,
                ServiceException {

        when(apiClientService.getApiClient()).thenReturn(apiClient);

        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);

        when(smallFullResourceHandler.approval()).thenReturn(approvalResourceHandler);

        when(smallFullService.getSmallFullAccounts(TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(smallFullApi);

        when(smallFullApi.getLinks()).thenReturn(smallFullLinks);

        Approval approval = new Approval();

        ApprovalApi approvalApi = new ApprovalApi();

        when(approvalTransformer.getApprovalApi(approval)).thenReturn(approvalApi);

        when(approvalResourceHandler.create(APPROVAL_URI, approvalApi)).thenReturn(approvalCreate);

        when(smallFullLinks.containsKey(SmallFullLinkType.APPROVAL.getLink()))
                .thenReturn(false);

        when(approvalCreate.execute()).thenThrow(URIValidationException.class);

        assertThrows(ServiceException.class, () ->
                approvalService.submitApproval(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, approval));

        verify(approvalCreate, times(1)).execute();
    }

    @Test
    @DisplayName("Submit Approval - POST - ApiErrorResponseException Thrown - Validation Errors")
    void createApprovalThrowsApiErrorResponseExceptionWithValidationErrors()
            throws ApiErrorResponseException, URIValidationException, ServiceException {

        when(apiClientService.getApiClient()).thenReturn(apiClient);

        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);

        when(smallFullResourceHandler.approval()).thenReturn(approvalResourceHandler);

        when(smallFullService.getSmallFullAccounts(TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(smallFullApi);

        when(smallFullApi.getLinks()).thenReturn(smallFullLinks);

        Approval approval = new Approval();

        ApprovalApi approvalApi = new ApprovalApi();

        when(approvalTransformer.getApprovalApi(approval)).thenReturn(approvalApi);

        when(approvalResourceHandler.create(APPROVAL_URI, approvalApi)).thenReturn(approvalCreate);

        when(smallFullLinks.containsKey(SmallFullLinkType.APPROVAL.getLink()))
                .thenReturn(false);

        HttpResponseException httpResponseException =
                new HttpResponseException.Builder(400,"Bad Request", new HttpHeaders()).build();

        ApiErrorResponseException apiErrorResponseException =
                ApiErrorResponseException.fromHttpResponseException(httpResponseException);

        when(approvalCreate.execute()).thenThrow(apiErrorResponseException);

        when(validationContext.getValidationErrors(apiErrorResponseException)).thenReturn(mockValidationErrors);

        List<ValidationError> validationErrors =
                approvalService.submitApproval(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, approval);

        verify(approvalCreate, times(1)).execute();

        assertNotNull(validationErrors);
    }

    @Test
    @DisplayName("Submit Approval - POST - ApiErrorResponseException Thrown - No Validation Errors")
    void createApprovalThrowsApiErrorResponseExceptionWithoutValidationErrors()
            throws ApiErrorResponseException, URIValidationException, ServiceException {

        when(apiClientService.getApiClient()).thenReturn(apiClient);

        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);

        when(smallFullResourceHandler.approval()).thenReturn(approvalResourceHandler);

        when(smallFullService.getSmallFullAccounts(TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(smallFullApi);

        when(smallFullApi.getLinks()).thenReturn(smallFullLinks);

        Approval approval = new Approval();

        ApprovalApi approvalApi = new ApprovalApi();

        when(approvalTransformer.getApprovalApi(approval)).thenReturn(approvalApi);

        when(approvalResourceHandler.create(APPROVAL_URI, approvalApi)).thenReturn(approvalCreate);

        when(smallFullLinks.containsKey(SmallFullLinkType.APPROVAL.getLink()))
                .thenReturn(false);

        HttpResponseException httpResponseException =
                new HttpResponseException.Builder(400,"Bad Request", new HttpHeaders()).build();

        ApiErrorResponseException apiErrorResponseException =
                ApiErrorResponseException.fromHttpResponseException(httpResponseException);

        when(approvalCreate.execute()).thenThrow(apiErrorResponseException);

        when(validationContext.getValidationErrors(apiErrorResponseException)).thenReturn(null);

        assertThrows(ServiceException.class, () ->
                approvalService.submitApproval(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, approval));

        verify(approvalCreate, times(1)).execute();
    }

    @Test
    @DisplayName("Submit Approval - POST - Generic ApiErrorResponseException Thrown")
    void createApprovalThrowsGenericApiErrorResponseException()
            throws ApiErrorResponseException, URIValidationException, ServiceException {

        when(apiClientService.getApiClient()).thenReturn(apiClient);

        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);

        when(smallFullResourceHandler.approval()).thenReturn(approvalResourceHandler);

        when(smallFullService.getSmallFullAccounts(TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(smallFullApi);

        when(smallFullApi.getLinks()).thenReturn(smallFullLinks);

        Approval approval = new Approval();

        ApprovalApi approvalApi = new ApprovalApi();

        when(approvalTransformer.getApprovalApi(approval)).thenReturn(approvalApi);

        when(approvalResourceHandler.create(APPROVAL_URI, approvalApi)).thenReturn(approvalCreate);

        when(smallFullLinks.containsKey(SmallFullLinkType.APPROVAL.getLink()))
                .thenReturn(false);

        when(approvalCreate.execute()).thenThrow(ApiErrorResponseException.class);

        assertThrows(ServiceException.class, () ->
                approvalService.submitApproval(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, approval));

        verify(approvalCreate, times(1)).execute();
    }

    @Test
    @DisplayName("Submit Approval - PUT - Success Path")
    void updateApprovalSuccess() throws ApiErrorResponseException, URIValidationException, ServiceException {

        when(apiClientService.getApiClient()).thenReturn(apiClient);

        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);

        when(smallFullResourceHandler.approval()).thenReturn(approvalResourceHandler);

        when(smallFullService.getSmallFullAccounts(TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(smallFullApi);

        when(smallFullApi.getLinks()).thenReturn(smallFullLinks);

        Approval approval = new Approval();

        ApprovalApi approvalApi = new ApprovalApi();

        when(approvalTransformer.getApprovalApi(approval)).thenReturn(approvalApi);

        when(approvalResourceHandler.update(APPROVAL_URI, approvalApi)).thenReturn(approvalUpdate);

        when(smallFullLinks.containsKey(SmallFullLinkType.APPROVAL.getLink()))
                .thenReturn(true);

        doNothing().when(approvalUpdate).execute();

        List<ValidationError> validationErrors =
                approvalService.submitApproval(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, approval);

        verify(approvalUpdate, times(1)).execute();

        assertTrue(validationErrors.isEmpty());
    }

    @Test
    @DisplayName("Submit Approval - PUT - URIValidationException Thrown")
    void updateApprovalURIValidationExceptionThrown() throws ApiErrorResponseException, URIValidationException,
                    ServiceException {

        when(apiClientService.getApiClient()).thenReturn(apiClient);

        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);

        when(smallFullResourceHandler.approval()).thenReturn(approvalResourceHandler);

        when(smallFullService.getSmallFullAccounts(TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(smallFullApi);

        when(smallFullApi.getLinks()).thenReturn(smallFullLinks);

        Approval approval = new Approval();

        ApprovalApi approvalApi = new ApprovalApi();

        when(approvalTransformer.getApprovalApi(approval)).thenReturn(approvalApi);

        when(approvalResourceHandler.update(APPROVAL_URI, approvalApi)).thenReturn(approvalUpdate);

        when(smallFullLinks.containsKey(SmallFullLinkType.APPROVAL.getLink()))
                .thenReturn(true);

        doThrow(URIValidationException.class).when(approvalUpdate).execute();

        assertThrows(ServiceException.class, () ->
                approvalService.submitApproval(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, approval));

        verify(approvalUpdate, times(1)).execute();
    }

    @Test
    @DisplayName("Submit Approval - PUT - ApiErrorResponseException Thrown - Validation Errors")
    void updateApprovalThrowsApiErrorResponseExceptionWithValidationErrors()
            throws ApiErrorResponseException, URIValidationException, ServiceException {

        when(apiClientService.getApiClient()).thenReturn(apiClient);

        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);

        when(smallFullResourceHandler.approval()).thenReturn(approvalResourceHandler);

        when(smallFullService.getSmallFullAccounts(TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(smallFullApi);

        when(smallFullApi.getLinks()).thenReturn(smallFullLinks);

        Approval approval = new Approval();

        ApprovalApi approvalApi = new ApprovalApi();

        when(approvalTransformer.getApprovalApi(approval)).thenReturn(approvalApi);

        when(approvalResourceHandler.update(APPROVAL_URI, approvalApi)).thenReturn(approvalUpdate);

        when(smallFullLinks.containsKey(SmallFullLinkType.APPROVAL.getLink()))
                .thenReturn(true);

        HttpResponseException httpResponseException =
                new HttpResponseException.Builder(400,"Bad Request", new HttpHeaders()).build();

        ApiErrorResponseException apiErrorResponseException =
                ApiErrorResponseException.fromHttpResponseException(httpResponseException);

        doThrow(apiErrorResponseException).when(approvalUpdate).execute();

        when(validationContext.getValidationErrors(apiErrorResponseException)).thenReturn(mockValidationErrors);

        List<ValidationError> validationErrors =
                approvalService.submitApproval(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, approval);

        verify(approvalUpdate, times(1)).execute();

        assertNotNull(validationErrors);
    }

    @Test
    @DisplayName("Submit Approval - PUT - ApiErrorResponseException Thrown - No Validation Errors")
    void updateApprovalThrowsApiErrorResponseExceptionWithoutValidationErrors()
            throws ApiErrorResponseException, URIValidationException, ServiceException {

        when(apiClientService.getApiClient()).thenReturn(apiClient);

        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);

        when(smallFullResourceHandler.approval()).thenReturn(approvalResourceHandler);

        when(smallFullService.getSmallFullAccounts(TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(smallFullApi);

        when(smallFullApi.getLinks()).thenReturn(smallFullLinks);

        Approval approval = new Approval();

        ApprovalApi approvalApi = new ApprovalApi();

        when(approvalTransformer.getApprovalApi(approval)).thenReturn(approvalApi);

        when(approvalResourceHandler.update(APPROVAL_URI, approvalApi)).thenReturn(approvalUpdate);

        when(smallFullLinks.containsKey(SmallFullLinkType.APPROVAL.getLink()))
                .thenReturn(true);

        HttpResponseException httpResponseException =
                new HttpResponseException.Builder(400,"Bad Request", new HttpHeaders()).build();

        ApiErrorResponseException apiErrorResponseException =
                ApiErrorResponseException.fromHttpResponseException(httpResponseException);

        doThrow(apiErrorResponseException).when(approvalUpdate).execute();

        when(validationContext.getValidationErrors(apiErrorResponseException)).thenReturn(null);

        assertThrows(ServiceException.class, () ->
                approvalService.submitApproval(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, approval));

        verify(approvalUpdate, times(1)).execute();
    }

    @Test
    @DisplayName("Submit Approval - PUT - Generic ApiErrorResponseException Thrown")
    void updateApprovalThrowsGenericApiErrorResponseException()
            throws ApiErrorResponseException, URIValidationException, ServiceException {

        when(apiClientService.getApiClient()).thenReturn(apiClient);

        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);

        when(smallFullResourceHandler.approval()).thenReturn(approvalResourceHandler);

        when(smallFullService.getSmallFullAccounts(TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(smallFullApi);

        when(smallFullApi.getLinks()).thenReturn(smallFullLinks);

        Approval approval = new Approval();

        ApprovalApi approvalApi = new ApprovalApi();

        when(approvalTransformer.getApprovalApi(approval)).thenReturn(approvalApi);

        when(approvalResourceHandler.update(APPROVAL_URI, approvalApi)).thenReturn(approvalUpdate);

        when(smallFullLinks.containsKey(SmallFullLinkType.APPROVAL.getLink()))
                .thenReturn(true);

        doThrow(ApiErrorResponseException.class).when(approvalUpdate).execute();

        assertThrows(ServiceException.class, () ->
                approvalService.submitApproval(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, approval));

        verify(approvalUpdate, times(1)).execute();
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
    @DisplayName("Validate Approval Date - Only Day Provided")
    void validateApprovalDateOnlyDayProvided() {

        ApprovalDate approvalDate = new ApprovalDate();
        approvalDate.setDay("12");

        Approval approval = new Approval();
        approval.setDate(approvalDate);

        List<ValidationError> validationErrors = approvalService.validateApprovalDate(approval);

        assertNotNull(validationErrors);
        assertEquals(1, validationErrors.size());
        assertEquals(DATE_INCOMPLETE, validationErrors.get(0).getMessageKey());
    }

    @Test
    @DisplayName("Validate Approval Date - Only Month Provided")
    void validateApprovalDateOnlyMonthProvided() {

        ApprovalDate approvalDate = new ApprovalDate();
        approvalDate.setMonth("12");

        Approval approval = new Approval();
        approval.setDate(approvalDate);

        List<ValidationError> validationErrors = approvalService.validateApprovalDate(approval);

        assertNotNull(validationErrors);
        assertEquals(1, validationErrors.size());
        assertEquals(DATE_INCOMPLETE, validationErrors.get(0).getMessageKey());
    }

    @Test
    @DisplayName("Validate Approval Date - Only Year Provided")
    void validateApprovalDateOnlyYearProvided() {

        ApprovalDate approvalDate = new ApprovalDate();
        approvalDate.setYear("2018");

        Approval approval = new Approval();
        approval.setDate(approvalDate);

        List<ValidationError> validationErrors = approvalService.validateApprovalDate(approval);

        assertNotNull(validationErrors);
        assertEquals(1, validationErrors.size());
        assertEquals(DATE_INCOMPLETE, validationErrors.get(0).getMessageKey());
    }

    @Test
    @DisplayName("Validate Approval Date - Invalid Format")
    void validateApprovalDateInvalidFormat() {

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
