package uk.gov.companieshouse.web.accounts.service.smallfull;

import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.model.accounts.directorsreport.DirectorsReportApi;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;

public interface DirectorsReportService {

    void createDirectorsReport(String transactionId, String companyAccountsId) throws ServiceException;

    DirectorsReportApi getDirectorsReport(ApiClient apiClient, String transactionId, String companyAccountsId)
            throws ServiceException;

    void deleteDirectorsReport(String transactionId, String companyAccountsId) throws ServiceException;
}
