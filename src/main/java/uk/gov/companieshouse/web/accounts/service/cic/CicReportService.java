package uk.gov.companieshouse.web.accounts.service.cic;

import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.model.accounts.cic.CicReportApi;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;

public interface CicReportService {
    /**
     * Creates the CIC report parent resource
     *
     * @param transactionId The id of the CHS transaction
     * @param companyAccountsId The company accounts identifier
     * @throws ServiceException if there's an error while creating the resource
     */
    void createCicReport(String transactionId, String companyAccountsId) throws ServiceException;

    /**
     * Retrieve the CIC report parent resource
     *
     * @param apiClient The API client with which to execute the request
     * @param transactionId The id of the CHS transaction
     * @param companyAccountsId The company accounts identifier
     * @throws ServiceException if there's an error while fetching the resource
     */
    CicReportApi getCicReport(ApiClient apiClient, String transactionId, String companyAccountsId)
            throws ServiceException;
}
