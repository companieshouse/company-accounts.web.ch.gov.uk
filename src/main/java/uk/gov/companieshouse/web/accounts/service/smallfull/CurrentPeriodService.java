package uk.gov.companieshouse.web.accounts.service.smallfull;

import java.util.List;
import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.model.accounts.smallfull.CurrentPeriodApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.SmallFullApi;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

public interface CurrentPeriodService {

    CurrentPeriodApi getCurrentPeriod(ApiClient apiClient, String transactionId, String companyAccountsId)
            throws ServiceException;

    void submitCurrentPeriod(ApiClient apiClient, SmallFullApi smallFullApi, String transactionId, String companyAccountsId,
            CurrentPeriodApi currentPeriodApi, List<ValidationError> validationErrors)
                    throws ServiceException;
}
