package uk.gov.companieshouse.web.accounts.service.smallfull.impl;

import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriTemplate;
import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.error.ApiErrorResponseException;
import uk.gov.companieshouse.api.handler.exception.URIValidationException;
import uk.gov.companieshouse.api.model.accounts.smallfull.ApprovalApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.SmallFullApi;
import uk.gov.companieshouse.web.accounts.api.ApiClientService;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.smallfull.Approval;
import uk.gov.companieshouse.web.accounts.service.smallfull.ApprovalService;
import uk.gov.companieshouse.web.accounts.service.smallfull.SmallFullService;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.ApprovalTransformer;
import uk.gov.companieshouse.web.accounts.util.ValidationContext;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

@Service
public class ApprovalServiceImpl implements ApprovalService {

    @Autowired
    private ApiClientService apiClientService;

    @Autowired
    private ValidationContext validationContext;

    @Autowired
    private ApprovalTransformer transformer;

    @Autowired
    private SmallFullService smallFullService;

    private static final UriTemplate APPROVAL_URI =
            new UriTemplate("/transactions/{transactionId}/company-accounts/{companyAccountsId}/small-full/approval");

    private static final String DAY_MONTH_REGEX = "\\d{1,2}";

    private static final String YEAR_REGEX = "\\d{4}";

    private static final String APPROVAL_DATE_FIELD_PATH = "date";

    private static final String APPROVAL_DATE_ERROR_LOCATION = ".approval.date";

    private static final String DATE_MISSING = "validation.date.missing" + APPROVAL_DATE_ERROR_LOCATION;

    private static final String DATE_INCOMPLETE = "validation.date.incomplete" + APPROVAL_DATE_ERROR_LOCATION;

    private static final String DATE_FORMAT_INVALID = "validation.date.format" + APPROVAL_DATE_ERROR_LOCATION;

    private static final String DATE_INVALID = "validation.date.nonExistent";

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
            SmallFullApi smallFullApi = smallFullService.getSmallFullAccounts(transactionId, companyAccountsId);

            if (smallFullApi.getLinks().getApproval() != null) {
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

        if (StringUtils.isBlank(approval.getDate().getDay()) &&
            StringUtils.isBlank(approval.getDate().getMonth()) &&
                StringUtils.isBlank(approval.getDate().getYear())) {

            ValidationError error = new ValidationError();
            error.setFieldPath(APPROVAL_DATE_FIELD_PATH);
            error.setMessageKey(DATE_MISSING);
            validationErrors.add(error);

        } else if (StringUtils.isBlank(approval.getDate().getDay()) ||
                StringUtils.isBlank(approval.getDate().getMonth()) ||
                StringUtils.isBlank(approval.getDate().getYear())) {

            ValidationError error = new ValidationError();
            error.setFieldPath(APPROVAL_DATE_FIELD_PATH);
            error.setMessageKey(DATE_INCOMPLETE);
            validationErrors.add(error);

        } else if (!approval.getDate().getDay().matches(DAY_MONTH_REGEX) ||
                !approval.getDate().getMonth().matches(DAY_MONTH_REGEX) ||
                !approval.getDate().getYear().matches(YEAR_REGEX)) {

            ValidationError error = new ValidationError();
            error.setFieldPath(APPROVAL_DATE_FIELD_PATH);
            error.setMessageKey(DATE_FORMAT_INVALID);
            validationErrors.add(error);

        } else {

            try {
                transformer.getApprovalDate(approval);
            } catch (DateTimeParseException e) {
                ValidationError error = new ValidationError();
                error.setFieldPath(APPROVAL_DATE_FIELD_PATH);
                error.setMessageKey(DATE_INVALID);
                validationErrors.add(error);
            }
        }

        return validationErrors;
    }
}
