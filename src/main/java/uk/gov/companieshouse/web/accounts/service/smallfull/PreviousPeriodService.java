package uk.gov.companieshouse.web.accounts.service.smallfull;

import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.model.accounts.smallfull.PreviousPeriodApi;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;

public interface PreviousPeriodService {

    PreviousPeriodApi getPreviousPeriod(ApiClient apiClient, String transactionId, String companyAccountsId)
            throws ServiceException;
}
