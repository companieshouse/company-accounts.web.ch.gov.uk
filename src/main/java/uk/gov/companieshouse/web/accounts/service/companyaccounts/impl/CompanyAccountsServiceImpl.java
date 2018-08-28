package uk.gov.companieshouse.web.accounts.service.companyaccounts.impl;

import com.google.api.client.util.DateTime;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.error.ApiErrorResponseException;
import uk.gov.companieshouse.api.model.accounts.CompanyAccountsApi;
import uk.gov.companieshouse.web.accounts.api.ApiClientService;
import uk.gov.companieshouse.web.accounts.service.companyaccounts.CompanyAccountsService;

@Service
public class CompanyAccountsServiceImpl implements CompanyAccountsService {

    @Autowired
    ApiClientService apiClientService;

    private static final Pattern COMPANY_ACCOUNTS_ID_MATCHER = Pattern.compile("/company-accounts/(.*)");

    @Override
    public String createCompanyAccounts(String transactionId, DateTime periodEndOn) throws ApiErrorResponseException {

        ApiClient apiClient = apiClientService.getApiClient();

        CompanyAccountsApi companyAccounts = new CompanyAccountsApi();
        companyAccounts.setPeriodEndOn(periodEndOn);

        companyAccounts = apiClient.transaction(transactionId).companyAccounts().create(companyAccounts);
        String selfLink = companyAccounts.getLinks().get("self");

        Matcher matcher = COMPANY_ACCOUNTS_ID_MATCHER.matcher(selfLink);
        matcher.find();

        return matcher.group(1);
    }
}
