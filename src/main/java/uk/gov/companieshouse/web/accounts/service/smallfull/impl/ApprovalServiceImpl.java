package uk.gov.companieshouse.web.accounts.service.smallfull.impl;

import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriTemplate;
import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.error.ApiErrorResponseException;
import uk.gov.companieshouse.api.handler.exception.URIValidationException;
import uk.gov.companieshouse.api.model.accounts.smallfull.ApprovalApi;
import uk.gov.companieshouse.web.accounts.api.ApiClientService;
import uk.gov.companieshouse.web.accounts.enumeration.ValidationMessage;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.links.SmallFullLinkType;
import uk.gov.companieshouse.web.accounts.model.smallfull.Approval;
import uk.gov.companieshouse.web.accounts.service.smallfull.ApprovalService;
import uk.gov.companieshouse.web.accounts.service.smallfull.SmallFullResourceService;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.ApprovalTransformer;
import uk.gov.companieshouse.web.accounts.util.ValidationContext;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

@Service
public class ApprovalServiceImpl extends SmallFullResourceService implements ApprovalService {

    @Autowired
    private ApiClientService apiClientService;

    @Autowired
    private ValidationContext validationContext;

    @Autowired
    private ApprovalTransformer transformer;

    private static final UriTemplate APPROVAL_URI =
            new UriTemplate("/transactions/{transactionId}/company-accounts/{companyAccountsId}/small-full/approval");

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ValidationError> submitApproval(String transactionId, String companyAccountsId,
            Approval approval) throws ServiceException {

        ApiClient apiClient = apiClientService.getApiClient();

        String uri = APPROVAL_URI.expand(transactionId, companyAccountsId).toString();

        ApprovalApi approvalApi = transformer.getApprovalApi(approval);

        try {
            if (smallFullResourceExists(transactionId, companyAccountsId, SmallFullLinkType.APPROVAL)) {
                apiClient.smallFull().approval().update(uri, approvalApi).execute();
            } else {
                apiClient.smallFull().approval().create(uri, approvalApi).execute();
            }
        } catch (ApiErrorResponseException e) {

            if (e.getStatusCode() == HttpStatus.BAD_REQUEST.value()) {
                List<ValidationError> validationErrors = validationContext.getValidationErrors(e);
                if (validationErrors == null) {
                    throw new ServiceException("Bad request when submitting approval", e);
                }

                return validationErrors;
            }

            throw new ServiceException("Error when submitting approval", e);
        } catch (URIValidationException e) {

            throw new ServiceException("Invalid URI for approval resource", e);
        }

        return new ArrayList<>();
    }

    @Override
    public List<ValidationError> validateApprovalDate(Approval approval) {

        List<ValidationError> validationErrors = new ArrayList<>();

        try {
            transformer.getApprovalDate(approval);
        } catch (DateTimeParseException e) {
            ValidationError error = new ValidationError();
            error.setFieldPath("approvalDate");
            error.setMessageKey(ValidationMessage.INVALID_DATE_SUPPLIED.getMessageKey() + ".approval.date");
            validationErrors.add(error);
        }

        return validationErrors;
    }
}
