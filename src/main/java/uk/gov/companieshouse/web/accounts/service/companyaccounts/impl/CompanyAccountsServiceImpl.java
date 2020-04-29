package uk.gov.companieshouse.web.accounts.service.companyaccounts.impl;

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
    private ApiClientService apiClientService;

    private static final Pattern COMPANY_ACCOUNTS_ID_MATCHER = Pattern.compile("/company-accounts/(.*)");

    private static final UriTemplate CREATE_COMPANY_ACCOUNTS_URI =
            new UriTemplate("/transactions/{transactionId}/company-accounts");

    private static final UriTemplate GET_COMPANY_ACCOUNTS_URI =
            new UriTemplate("/transactions/{transactionId}/company-accounts/{companyAccountsId}");

    @Override
    public String createCompanyAccounts(String transactionId) throws ServiceException {

        ApiClient apiClient = apiClientService.getApiClient();

        CompanyAccountsApi companyAccounts = new CompanyAccountsApi();

        String uri = CREATE_COMPANY_ACCOUNTS_URI.expand(transactionId).toString();

        try {
            companyAccounts = apiClient.companyAccounts().create(uri, companyAccounts).execute().getData();
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

    @Override
    public CompanyAccountsApi getCompanyAccounts(String transactionId, String companyAccountsId)
            throws ServiceException {

        ApiClient apiClient = apiClientService.getApiClient();

        String uri = GET_COMPANY_ACCOUNTS_URI.expand(transactionId, companyAccountsId).toString();

        try {
            return apiClient.companyAccounts().get(uri).execute().getData();
        } catch (ApiErrorResponseException e) {

            throw new ServiceException("Error retrieving company accounts", e);
        } catch (URIValidationException e) {

            throw new ServiceException("Invalid URI for company accounts resource", e);
        }
    }
}
