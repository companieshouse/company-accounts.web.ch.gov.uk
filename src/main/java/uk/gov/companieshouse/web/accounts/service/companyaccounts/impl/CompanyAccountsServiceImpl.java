package uk.gov.companieshouse.web.accounts.service.companyaccounts.impl;

import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriTemplate;
import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.error.ApiErrorResponseException;
import uk.gov.companieshouse.api.handler.exception.URIValidationException;
import uk.gov.companieshouse.api.model.accounts.CompanyAccountsApi;
import uk.gov.companieshouse.web.accounts.api.ApiClientService;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.service.companyaccounts.CompanyAccountsService;

@Service
public class CompanyAccountsServiceImpl implements CompanyAccountsService {

    @Autowired
    ApiClientService apiClientService;

    private static final Pattern COMPANY_ACCOUNTS_ID_MATCHER = Pattern.compile("/company-accounts/(.*)");

    private static final UriTemplate CREATE_COMPANY_ACCOUNTS_URI =
            new UriTemplate("/transactions/{transactionId}/company-accounts");

    @Override
    public String createCompanyAccounts(String transactionId, LocalDate periodEndOn) throws ServiceException {

        ApiClient apiClient = apiClientService.getApiClient();

        CompanyAccountsApi companyAccounts = new CompanyAccountsApi();
        companyAccounts.setPeriodEndOn(periodEndOn);

        String uri = CREATE_COMPANY_ACCOUNTS_URI.expand(transactionId).toString();

        try {
            companyAccounts = apiClient.companyAccounts().create(uri, companyAccounts).execute();
        } catch (ApiErrorResponseException e) {

            throw new ServiceException("Error creating company account", e);
        } catch (URIValidationException e) {

            throw new ServiceException("Invalid URI for company accounts resource", e);
        }

        String selfLink = companyAccounts.getLinks().getSelf();

        Matcher matcher = COMPANY_ACCOUNTS_ID_MATCHER.matcher(selfLink);
        matcher.find();

        return matcher.group(1);
    }
}
