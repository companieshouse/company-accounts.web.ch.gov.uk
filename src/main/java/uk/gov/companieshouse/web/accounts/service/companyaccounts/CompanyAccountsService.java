package uk.gov.companieshouse.web.accounts.service.companyaccounts;

import com.google.api.client.util.DateTime;
import uk.gov.companieshouse.api.error.ApiErrorResponseException;

public interface CompanyAccountsService {

    String createCompanyAccounts(String transactionId, DateTime periodEndOn) throws ApiErrorResponseException;
}
