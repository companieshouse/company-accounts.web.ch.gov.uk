package uk.gov.companieshouse.web.accounts.service.smallfull.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriTemplate;
import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.error.ApiErrorResponseException;
import uk.gov.companieshouse.api.handler.exception.URIValidationException;
import uk.gov.companieshouse.api.model.accounts.smallfull.BalanceSheetStatementsApi;
import uk.gov.companieshouse.web.accounts.api.ApiClientService;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.smallfull.Statements;
import uk.gov.companieshouse.web.accounts.service.smallfull.StatementsService;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.StatementsTransformer;

@Service
public class StatementsServiceImpl implements StatementsService {
    @Autowired
    private ApiClientService apiClientService;

    @Autowired
    private StatementsTransformer transformer;

    private static final UriTemplate STATEMENTS_URI =
            new UriTemplate("/transactions/{transactionId}/company-accounts/{companyAccountsId}/small-full/statements");

    private static final String INVALID_URI_ERROR = "Invalid URI for balance sheet statements resource";

    /**
     * {@inheritDoc}
     */
    @Override
    public void createBalanceSheetStatementsResource(String transactionId, String companyAccountsId)
            throws ServiceException {
        ApiClient apiClient = apiClientService.getApiClient();

        String uri = STATEMENTS_URI.expand(transactionId, companyAccountsId).toString();

        BalanceSheetStatementsApi statementsApi = new BalanceSheetStatementsApi();
        statementsApi.setHasAgreedToLegalStatements(false);

        try {
            apiClient.smallFull().balanceSheetStatements().create(uri, statementsApi).execute();
        } catch (ApiErrorResponseException e) {
            throw new ServiceException("Error when creating balance sheet statements", e);
        } catch (URIValidationException e) {
            throw new ServiceException(INVALID_URI_ERROR, e);
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void acceptBalanceSheetStatements(String transactionId, String companyAccountsId)
            throws ServiceException {
        ApiClient apiClient = apiClientService.getApiClient();

        String uri = STATEMENTS_URI.expand(transactionId, companyAccountsId).toString();

        BalanceSheetStatementsApi statementsApi = new BalanceSheetStatementsApi();
        statementsApi.setHasAgreedToLegalStatements(true);

        try {
            apiClient.smallFull().balanceSheetStatements().update(uri, statementsApi).execute();
        } catch (ApiErrorResponseException e) {
            throw new ServiceException("Error when accepting balance sheet statements", e);
        } catch (URIValidationException e) {
            throw new ServiceException(INVALID_URI_ERROR, e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Statements getBalanceSheetStatements(String transactionId, String companyAccountsId)
            throws ServiceException {
        ApiClient apiClient = apiClientService.getApiClient();

        String uri = STATEMENTS_URI.expand(transactionId, companyAccountsId).toString();

        try {
            BalanceSheetStatementsApi statementsApi =
                apiClient.smallFull().balanceSheetStatements().get(uri).execute().getData();

            return transformer.getBalanceSheetStatements(statementsApi);

        } catch (ApiErrorResponseException e) {
            if (e.getStatusCode() == 404) {
                return null;
            }
            throw new ServiceException("Error when retrieving balance sheet statements", e);
        } catch (URIValidationException e) {
            throw new ServiceException(INVALID_URI_ERROR, e);
        }
    }
}
