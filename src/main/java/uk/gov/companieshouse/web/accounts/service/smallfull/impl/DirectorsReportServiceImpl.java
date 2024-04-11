package uk.gov.companieshouse.web.accounts.service.smallfull.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriTemplate;
import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.error.ApiErrorResponseException;
import uk.gov.companieshouse.api.handler.exception.URIValidationException;
import uk.gov.companieshouse.api.model.accounts.directorsreport.DirectorsReportApi;
import uk.gov.companieshouse.web.accounts.api.ApiClientService;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.service.smallfull.DirectorsReportService;
import uk.gov.companieshouse.web.accounts.service.smallfull.SmallFullService;
import uk.gov.companieshouse.web.accounts.validation.helper.ServiceExceptionHandler;

@Service
public class DirectorsReportServiceImpl implements DirectorsReportService {
    @Autowired
    private ApiClientService apiClientService;

    @Autowired
    private SmallFullService smallFullService;

    @Autowired
    private ServiceExceptionHandler serviceExceptionHandler;

    private static final UriTemplate DIRECTORS_REPORT_URI =
            new UriTemplate("/transactions/{transactionId}/company-accounts/{companyAccountsId}/small-full/directors-report");

    private static final String RESOURCE_NAME = "directors report";

    @Override
    public void createDirectorsReport(String transactionId, String companyAccountsId) throws ServiceException {
        ApiClient apiClient = apiClientService.getApiClient();

        DirectorsReportApi directorsReport = getDirectorsReport(apiClient, transactionId, companyAccountsId);

        if (directorsReport == null) {
            String uri = DIRECTORS_REPORT_URI.expand(transactionId, companyAccountsId).toString();

            try {
                apiClient.smallFull().directorsReport().create(uri, new DirectorsReportApi())
                        .execute();
            } catch (ApiErrorResponseException e) {
                serviceExceptionHandler.handleSubmissionException(e, RESOURCE_NAME);
            } catch (URIValidationException e) {
                serviceExceptionHandler.handleURIValidationException(e, RESOURCE_NAME);
            }
        }
    }

    @Override
    public DirectorsReportApi getDirectorsReport(ApiClient apiClient, String transactionId,
            String companyAccountsId) throws ServiceException {
        String uri = DIRECTORS_REPORT_URI.expand(transactionId, companyAccountsId).toString();

        try {
            return apiClient.smallFull().directorsReport().get(uri).execute().getData();
        } catch (ApiErrorResponseException e) {
            serviceExceptionHandler.handleRetrievalException(e, RESOURCE_NAME);
        } catch (URIValidationException e) {
            serviceExceptionHandler.handleURIValidationException(e, RESOURCE_NAME);
        }

        return null;
    }

    @Override
    public void deleteDirectorsReport(String transactionId, String companyAccountsId)
            throws ServiceException {
        ApiClient apiClient = apiClientService.getApiClient();

        if (StringUtils.isNotBlank(
                smallFullService.getSmallFullAccounts(apiClient, transactionId, companyAccountsId)
                        .getLinks().getDirectorsReport())) {
            String uri = DIRECTORS_REPORT_URI.expand(transactionId, companyAccountsId).toString();

            try {
                apiClient.smallFull().directorsReport().delete(uri).execute();
            } catch (ApiErrorResponseException e) {
                serviceExceptionHandler.handleDeletionException(e, RESOURCE_NAME);
            } catch (URIValidationException e) {
                serviceExceptionHandler.handleURIValidationException(e, RESOURCE_NAME);
            }
        }
    }
}
