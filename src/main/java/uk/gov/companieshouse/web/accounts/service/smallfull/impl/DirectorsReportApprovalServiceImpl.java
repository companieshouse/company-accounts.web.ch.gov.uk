package uk.gov.companieshouse.web.accounts.service.smallfull.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriTemplate;
import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.error.ApiErrorResponseException;
import uk.gov.companieshouse.api.handler.exception.URIValidationException;
import uk.gov.companieshouse.api.model.ApiResponse;
import uk.gov.companieshouse.api.model.accounts.directorsreport.ApprovalApi;
import uk.gov.companieshouse.api.model.accounts.directorsreport.DirectorsReportApi;
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

@Service
public class DirectorsReportApprovalServiceImpl implements DirectorsReportApprovalService {
    @Autowired
    private ApiClientService apiClientService;

    @Autowired
    private ServiceExceptionHandler serviceExceptionHandler;

    @Autowired
    private DirectorsReportApprovalTransformer transformer;

    @Autowired
    private DirectorsReportService directorsReportService;

    @Autowired
    private ValidationContext validationContext;

    @Autowired
    private DateValidator dateValidator;

    private static final UriTemplate APPROVAL_URI =
        new UriTemplate("/transactions/{transactionId}/company-accounts/{companyAccountsId}/small-full/directors-report/approval");

    private static final String RESOURCE_NAME = "directors report approval";

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ValidationError> submitDirectorsReportApproval(String transactionId, String companyAccountsId,
                                                               DirectorsReportApproval directorsReportApproval) throws ServiceException {
        List<ValidationError> validationErrors = dateValidator.validateDate(directorsReportApproval.getDate(), "date",  ".directors_approval.date");
        if (!validationErrors.isEmpty()) {
            return validationErrors;
        }

        ApiClient apiClient = apiClientService.getApiClient();

        String uri = APPROVAL_URI.expand(transactionId, companyAccountsId).toString();

        ApprovalApi approvalApi = transformer.getDirectorsReportApprovalApi(directorsReportApproval);

        try {
            DirectorsReportApi directorsReportApi = directorsReportService.getDirectorsReport(apiClient, transactionId, companyAccountsId);

            ApiResponse apiResponse;

            if (directorsReportApi.getLinks().getApproval() != null) {
                apiResponse = apiClient.smallFull().directorsReport().approval().update(uri, approvalApi).execute();
            } else {
                apiResponse = apiClient.smallFull().directorsReport().approval().create(uri, approvalApi).execute();
            }

            if (apiResponse.hasErrors()) {
                validationErrors.addAll(validationContext.getValidationErrors(apiResponse.getErrors()));
            }
        } catch (ApiErrorResponseException e) {
            serviceExceptionHandler.handleSubmissionException(e, RESOURCE_NAME);
        } catch (URIValidationException e) {
            serviceExceptionHandler.handleURIValidationException(e, RESOURCE_NAME);
        }

        return validationErrors;
    }

    @Override
    public DirectorsReportApproval getDirectorsReportApproval(String transactionId, String companyAccountsId) throws ServiceException {
        ApiClient apiClient = apiClientService.getApiClient();

        String uri = APPROVAL_URI.expand(transactionId, companyAccountsId).toString();

        try {
            ApprovalApi directorsReportApprovalApi = apiClient.smallFull().directorsReport().approval().get(uri).execute().getData();
            return transformer.getDirectorsReportApproval(directorsReportApprovalApi);

        } catch (ApiErrorResponseException e) {
            serviceExceptionHandler.handleRetrievalException(e, RESOURCE_NAME);
        } catch (URIValidationException e) {
            serviceExceptionHandler.handleURIValidationException(e, RESOURCE_NAME);
        }

        return new DirectorsReportApproval();
    }
}
