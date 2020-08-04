package uk.gov.companieshouse.web.accounts.service.smallfull;

import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.model.accounts.smallfull.loanstodirectors.LoansToDirectorsApi;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;

public interface LoansToDirectorsService {

    void createLoansToDirectors(String transactionId, String companyAccountsId) throws ServiceException;

    LoansToDirectorsApi getLoansToDirectors(ApiClient apiClient, String transactionId, String companyAccountsId)
            throws ServiceException;

    void deleteLoansToDirectors(String transactionId, String companyAccountsId) throws ServiceException;

}
