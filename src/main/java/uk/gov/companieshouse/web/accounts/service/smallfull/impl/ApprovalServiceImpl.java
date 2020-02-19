package uk.gov.companieshouse.web.accounts.service.smallfull.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriTemplate;
import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.error.ApiErrorResponseException;
import uk.gov.companieshouse.api.handler.exception.URIValidationException;
import uk.gov.companieshouse.api.model.ApiResponse;
import uk.gov.companieshouse.api.model.accounts.smallfull.ApprovalApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.SmallFullApi;
import uk.gov.companieshouse.web.accounts.api.ApiClientService;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.smallfull.Approval;
import uk.gov.companieshouse.web.accounts.service.smallfull.ApprovalService;
import uk.gov.companieshouse.web.accounts.service.smallfull.SmallFullService;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.ApprovalTransformer;
import uk.gov.companieshouse.web.accounts.util.ValidationContext;
import uk.gov.companieshouse.web.accounts.validation.DateValidator;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;
import uk.gov.companieshouse.web.accounts.validation.helper.ServiceExceptionHandler;

@Service
public class ApprovalServiceImpl implements ApprovalService {

    @Autowired
    private ApiClientService apiClientService;

    @Autowired
    private ServiceExceptionHandler serviceExceptionHandler;

    @Autowired
    private ValidationContext validationContext;

    @Autowired
    private DateValidator dateValidator;

    @Autowired
    private ApprovalTransformer transformer;

    @Autowired
    private SmallFullService smallFullService;

    private static final UriTemplate APPROVAL_URI =
            new UriTemplate("/transactions/{transactionId}/company-accounts/{companyAccountsId}/small-full/approval");

    private static final String RESOURCE_NAME = "approval";

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ValidationError> submitApproval(String transactionId, String companyAccountsId,
            Approval approval) throws ServiceException {

        List<ValidationError> validationErrors = dateValidator.validateDate(approval.getDate(), "date", ".approval.date");
        if (!validationErrors.isEmpty()) {
            return validationErrors;
        }

        ApiClient apiClient = apiClientService.getApiClient();

        String uri = APPROVAL_URI.expand(transactionId, companyAccountsId).toString();

        ApprovalApi approvalApi = transformer.getApprovalApi(approval);

        try {
            SmallFullApi smallFullApi = smallFullService.getSmallFullAccounts(apiClient, transactionId,
                    companyAccountsId);

            ApiResponse apiResponse;

            if (smallFullApi.getLinks().getApproval() != null) {
                apiResponse = apiClient.smallFull().approval().update(uri, approvalApi).execute();
            } else {
                apiResponse = apiClient.smallFull().approval().create(uri, approvalApi).execute();
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
}
