package uk.gov.companieshouse.web.accounts.service.smallfull.impl;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriTemplate;
import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.error.ApiErrorResponseException;
import uk.gov.companieshouse.api.handler.exception.URIValidationException;
import uk.gov.companieshouse.api.model.accounts.smallfull.AccountingPoliciesApi;
import uk.gov.companieshouse.web.accounts.api.ApiClientService;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.service.smallfull.AccountingPoliciesService;
import uk.gov.companieshouse.web.accounts.util.ValidationContext;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

@Service
public class AccountingPoliciesServiceImpl implements AccountingPoliciesService {

    @Autowired
    private ApiClientService apiClientService;

    @Autowired
    private ValidationContext validationContext;

    private static final UriTemplate ACCOUNTING_POLICIES_URI =
            new UriTemplate("/transactions/{transactionId}/company-accounts/{companyAccountsId}/small-full/notes/accounting-policy");

    private static final String INVALID_URI_MESSAGE = "Invalid URI for accounting policies resource";

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
            throw new ServiceException(INVALID_URI_MESSAGE, e);
        } catch (ApiErrorResponseException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND.value()) {
                return null;
            }
            throw new ServiceException("Error retrieving accounting policies resource", e);
        }
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
            throw new ServiceException(INVALID_URI_MESSAGE, e);
        } catch (ApiErrorResponseException e) {
            if (e.getStatusCode() == HttpStatus.BAD_REQUEST.value()) {
                List<ValidationError> validationErrors = validationContext.getValidationErrors(e);
                if (validationErrors.isEmpty()) {
                    throw new ServiceException("Bad request when creating accounting policies resource", e);
                }
                return validationErrors;
            }
            throw new ServiceException("Error creating accounting policies resource", e);
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
            throw new ServiceException(INVALID_URI_MESSAGE, e);
        } catch (ApiErrorResponseException e) {
            if (e.getStatusCode() == HttpStatus.BAD_REQUEST.value()) {
                List<ValidationError> validationErrors = validationContext.getValidationErrors(e);
                if (validationErrors.isEmpty()) {
                    throw new ServiceException("Bad request when updating accounting policies resource", e);
                }
                return validationErrors;
            }
            throw new ServiceException("Error updating accounting policies resource", e);
        }

        return new ArrayList<>();
    }
}
