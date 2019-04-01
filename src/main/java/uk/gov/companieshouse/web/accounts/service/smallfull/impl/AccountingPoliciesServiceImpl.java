package uk.gov.companieshouse.web.accounts.service.smallfull.impl;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriTemplate;
import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.error.ApiErrorResponseException;
import uk.gov.companieshouse.api.handler.exception.URIValidationException;
import uk.gov.companieshouse.api.model.accounts.smallfull.AccountingPoliciesApi;
import uk.gov.companieshouse.web.accounts.api.ApiClientService;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.service.smallfull.AccountingPoliciesService;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;
import uk.gov.companieshouse.web.accounts.validation.helper.ServiceExceptionHandler;

@Service
public class AccountingPoliciesServiceImpl implements AccountingPoliciesService {

    @Autowired
    private ApiClientService apiClientService;

    @Autowired
    private ServiceExceptionHandler serviceExceptionHandler;

    private static final UriTemplate ACCOUNTING_POLICIES_URI =
            new UriTemplate("/transactions/{transactionId}/company-accounts/{companyAccountsId}/small-full/notes/accounting-policy");

    private static final String RESOURCE_NAME = "accounting policies";

    /**
     * {@inheritDoc}
     */
    @Override
    public AccountingPoliciesApi getAccountingPoliciesApi(String transactionId, String companyAccountsId)
                        throws ServiceException {

        ApiClient apiClient = apiClientService.getApiClient();

        String uri = ACCOUNTING_POLICIES_URI.expand(transactionId, companyAccountsId).toString();

        try {
            return apiClient.smallFull().accountingPolicies().get(uri).execute();
        } catch (URIValidationException e) {
            serviceExceptionHandler.handleURIValidationException(e, RESOURCE_NAME);
        } catch (ApiErrorResponseException e) {
            serviceExceptionHandler.handleRetrievalException(e, RESOURCE_NAME);
        }

        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ValidationError> createAccountingPoliciesApi(String transactionId, String companyAccountsId,
                        AccountingPoliciesApi accountingPoliciesApi) throws ServiceException {

        ApiClient apiClient = apiClientService.getApiClient();

        String uri = ACCOUNTING_POLICIES_URI.expand(transactionId, companyAccountsId).toString();

        try {
            apiClient.smallFull().accountingPolicies().create(uri, accountingPoliciesApi).execute();
        } catch (URIValidationException e) {
            serviceExceptionHandler.handleURIValidationException(e, RESOURCE_NAME);
        } catch (ApiErrorResponseException e) {
            return serviceExceptionHandler.handleSubmissionException(e, RESOURCE_NAME);
        }

        return new ArrayList<>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ValidationError> updateAccountingPoliciesApi(String transactionId, String companyAccountsId,
            AccountingPoliciesApi accountingPoliciesApi) throws ServiceException {

        ApiClient apiClient = apiClientService.getApiClient();

        String uri = ACCOUNTING_POLICIES_URI.expand(transactionId, companyAccountsId).toString();

        try {
            apiClient.smallFull().accountingPolicies().update(uri, accountingPoliciesApi).execute();
        } catch (URIValidationException e) {
            serviceExceptionHandler.handleURIValidationException(e, RESOURCE_NAME);
        } catch (ApiErrorResponseException e) {
            return serviceExceptionHandler.handleSubmissionException(e, RESOURCE_NAME);
        }

        return new ArrayList<>();
    }
}
