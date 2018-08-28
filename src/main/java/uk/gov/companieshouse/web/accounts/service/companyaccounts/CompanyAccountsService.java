package uk.gov.companieshouse.web.accounts.service.companyaccounts;

import com.google.api.client.util.DateTime;
import java.util.Date;
import uk.gov.companieshouse.api.error.ApiErrorResponseException;
import uk.gov.companieshouse.api.model.accounts.CompanyAccountsApi;

public interface CompanyAccountsService {

    String createCompanyAccounts(String transactionId, DateTime periodEndOn) throws ApiErrorResponseException;
}
