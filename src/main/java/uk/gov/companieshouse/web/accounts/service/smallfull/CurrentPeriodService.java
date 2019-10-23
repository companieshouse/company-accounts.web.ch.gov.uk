package uk.gov.companieshouse.web.accounts.service.smallfull;

import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.model.accounts.smallfull.CurrentPeriodApi;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;

public interface CurrentPeriodService {

    CurrentPeriodApi getCurrentPeriod(ApiClient apiClient, String transactionId, String companyAccountsId)
            throws ServiceException;
}
