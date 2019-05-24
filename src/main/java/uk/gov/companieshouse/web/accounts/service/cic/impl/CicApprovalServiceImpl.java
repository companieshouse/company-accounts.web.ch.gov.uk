package uk.gov.companieshouse.web.accounts.service.cic.impl;

import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriTemplate;
import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.error.ApiErrorResponseException;
import uk.gov.companieshouse.api.handler.exception.URIValidationException;
import uk.gov.companieshouse.api.model.accounts.cic.CicReportApi;
import uk.gov.companieshouse.api.model.accounts.cic.approval.CicApprovalApi;
import uk.gov.companieshouse.web.accounts.api.ApiClientService;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.cic.CicApproval;
import uk.gov.companieshouse.web.accounts.service.cic.CicApprovalService;
import uk.gov.companieshouse.web.accounts.service.cic.CicReportService;
import uk.gov.companieshouse.web.accounts.transformer.cic.CicApprovalTransformer;
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

    private static final UriTemplate APPROVAL_URI =
        new UriTemplate("/transactions/{transactionId}/company-accounts/{companyAccountsId}/cic-report/cic-approval");

    private static final String DAY_MONTH_REGEX = "\\d{1,2}";

    private static final String YEAR_REGEX = "\\d{4}";

    private static final String APPROVAL_DATE_FIELD_PATH = "date";

    private static final String APPROVAL_DATE_ERROR_LOCATION = ".approval.date";

    private static final String DATE_MISSING =
        "validation.date.missing" + APPROVAL_DATE_ERROR_LOCATION;

    private static final String DATE_INCOMPLETE =
        "validation.date.incomplete" + APPROVAL_DATE_ERROR_LOCATION;

    private static final String DATE_FORMAT_INVALID =
        "validation.date.format" + APPROVAL_DATE_ERROR_LOCATION;

    private static final String DATE_INVALID = "validation.date.nonExistent";

    private static final String RESOURCE_NAME = "approval";

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ValidationError> submitCicApproval(String transactionId, String companyAccountsId,
        CicApproval cicApproval) throws ServiceException {

        ApiClient apiClient = apiClientService.getApiClient();

        String uri = APPROVAL_URI.expand(transactionId, companyAccountsId).toString();

        CicApprovalApi cicApprovalApi = transformer.getCicApprovalApi(cicApproval);

        try {
            CicReportApi cicReportApi = cicReportService.getCicReport(apiClient, transactionId,
                companyAccountsId);

            if (cicReportApi.getLinks().getApproval() != null) {
                apiClient.cicReport().approval().update(uri, cicApprovalApi).execute();
            } else {
                apiClient.cicReport().approval().create(uri, cicApprovalApi).execute();
            }
        } catch (ApiErrorResponseException e) {
            return serviceExceptionHandler.handleSubmissionException(e, RESOURCE_NAME);
        } catch (URIValidationException e) {
            serviceExceptionHandler.handleURIValidationException(e, RESOURCE_NAME);
        }

        return new ArrayList<>();
    }

    @Override
    public List<ValidationError> validateCicApprovalDate(CicApproval cicApproval) {

        List<ValidationError> validationErrors = new ArrayList<>();

        if (StringUtils.isBlank(cicApproval.getDate().getDay()) &&
            StringUtils.isBlank(cicApproval.getDate().getMonth()) &&
            StringUtils.isBlank(cicApproval.getDate().getYear())) {

            ValidationError error = new ValidationError();
            error.setFieldPath(APPROVAL_DATE_FIELD_PATH);
            error.setMessageKey(DATE_MISSING);
            validationErrors.add(error);

        } else if (StringUtils.isBlank(cicApproval.getDate().getDay()) ||
            StringUtils.isBlank(cicApproval.getDate().getMonth()) ||
            StringUtils.isBlank(cicApproval.getDate().getYear())) {

            ValidationError error = new ValidationError();
            error.setFieldPath(APPROVAL_DATE_FIELD_PATH);
            error.setMessageKey(DATE_INCOMPLETE);
            validationErrors.add(error);

        } else if (!cicApproval.getDate().getDay().matches(DAY_MONTH_REGEX) ||
            !cicApproval.getDate().getMonth().matches(DAY_MONTH_REGEX) ||
            !cicApproval.getDate().getYear().matches(YEAR_REGEX)) {

            ValidationError error = new ValidationError();
            error.setFieldPath(APPROVAL_DATE_FIELD_PATH);
            error.setMessageKey(DATE_FORMAT_INVALID);
            validationErrors.add(error);

        } else {

            try {
                transformer.getCicApprovalDate(cicApproval);
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
