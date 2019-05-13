package uk.gov.companieshouse.web.accounts.service.cic.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriTemplate;
import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.error.ApiErrorResponseException;
import uk.gov.companieshouse.api.handler.exception.URIValidationException;
import uk.gov.companieshouse.api.model.accounts.cic.CicReportApi;
import uk.gov.companieshouse.web.accounts.api.ApiClientService;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.service.cic.CicReportService;

@Service
public class CicReportServiceImpl implements CicReportService {

    @Autowired
    private ApiClientService apiClientService;

    private static final UriTemplate CIC_REPORT_URI =
            new UriTemplate("/transactions/{transactionId}/company-accounts/{companyAccountsId}/cic-report");

    /**
     * {@inheritDoc}
     */
    @Override
    public void createCicReport(String transactionId, String companyAccountsId)
            throws ServiceException {

        ApiClient apiClient = apiClientService.getApiClient();

        String uri = CIC_REPORT_URI.expand(transactionId, companyAccountsId).toString();

        try {
            apiClient.cicReport().create(uri, new CicReportApi()).execute();
        } catch (ApiErrorResponseException e) {

            throw new ServiceException("Error creating cic report resource", e);
        } catch (URIValidationException e) {

            throw new ServiceException("Invalid URI for cic report resource", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CicReportApi getCicReport(ApiClient apiClient, String transactionId, String companyAccountsId)
            throws ServiceException {

        String uri = CIC_REPORT_URI.expand(transactionId, companyAccountsId).toString();

        try {
            return apiClient.cicReport().get(uri).execute();
        } catch (ApiErrorResponseException e) {

            throw new ServiceException("Error retrieving cic report resource", e);
        } catch (URIValidationException e) {

            throw new ServiceException("Invalid URI for cic report resource", e);
        }
    }
}
