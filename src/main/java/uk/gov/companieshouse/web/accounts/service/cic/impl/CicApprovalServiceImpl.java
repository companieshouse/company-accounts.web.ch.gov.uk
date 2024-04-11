package uk.gov.companieshouse.web.accounts.service.cic.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriTemplate;
import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.error.ApiErrorResponseException;
import uk.gov.companieshouse.api.handler.exception.URIValidationException;
import uk.gov.companieshouse.api.model.ApiResponse;
import uk.gov.companieshouse.api.model.accounts.cic.CicReportApi;
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

@Service
public class CicApprovalServiceImpl implements CicApprovalService {
    @Autowired
    private ApiClientService apiClientService;

    @Autowired
    private ServiceExceptionHandler serviceExceptionHandler;

    @Autowired
    private CicApprovalTransformer transformer;

    @Autowired
    private CicReportService cicReportService;

    @Autowired
    private ValidationContext validationContext;

    @Autowired
    private DateValidator dateValidator;

    private static final UriTemplate APPROVAL_URI =
        new UriTemplate("/transactions/{transactionId}/company-accounts/{companyAccountsId}/cic-report/cic-approval");

    private static final String RESOURCE_NAME = "cic approval";

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ValidationError> submitCicApproval(String transactionId, String companyAccountsId,
        CicApproval cicApproval) throws ServiceException {
        List<ValidationError> validationErrors = dateValidator.validateDate(cicApproval.getDate(), "date",  ".cic_approval.date");
        if (!validationErrors.isEmpty()) {
            return validationErrors;
        }

        ApiClient apiClient = apiClientService.getApiClient();

        String uri = APPROVAL_URI.expand(transactionId, companyAccountsId).toString();

        CicApprovalApi cicApprovalApi = transformer.getCicApprovalApi(cicApproval);

        try {
            CicReportApi cicReportApi = cicReportService.getCicReport(apiClient, transactionId,
                companyAccountsId);

            ApiResponse apiResponse;

            if (cicReportApi.getLinks().getApproval() != null) {
                apiResponse = apiClient.cicReport().approval().update(uri, cicApprovalApi).execute();
            } else {
                apiResponse = apiClient.cicReport().approval().create(uri, cicApprovalApi).execute();
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
    public CicApproval getCicApproval(String transactionId, String companyAccountsId) throws ServiceException {
        ApiClient apiClient = apiClientService.getApiClient();

        String uri = APPROVAL_URI.expand(transactionId, companyAccountsId).toString();

        try {
            CicApprovalApi cicApprovalApi = apiClient.cicReport().approval().get(uri).execute().getData();
            return transformer.getCicApproval(cicApprovalApi);

        } catch (ApiErrorResponseException e) {
            serviceExceptionHandler.handleRetrievalException(e, RESOURCE_NAME);
        } catch (URIValidationException e) {
            serviceExceptionHandler.handleURIValidationException(e, RESOURCE_NAME);
        }

        return new CicApproval();
    }
}
