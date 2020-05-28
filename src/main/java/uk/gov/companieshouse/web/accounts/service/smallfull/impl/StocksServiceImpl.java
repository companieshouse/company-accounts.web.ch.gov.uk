package uk.gov.companieshouse.web.accounts.service.smallfull.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriTemplate;
import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.error.ApiErrorResponseException;
import uk.gov.companieshouse.api.handler.exception.URIValidationException;
import uk.gov.companieshouse.api.model.ApiResponse;
import uk.gov.companieshouse.api.model.accounts.smallfull.SmallFullApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.SmallFullLinks;
import uk.gov.companieshouse.api.model.accounts.smallfull.stocks.StocksApi;
import uk.gov.companieshouse.web.accounts.api.ApiClientService;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.smallfull.BalanceSheetHeadings;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.stocks.StocksNote;
import uk.gov.companieshouse.web.accounts.service.smallfull.SmallFullService;
import uk.gov.companieshouse.web.accounts.service.smallfull.StocksService;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.StocksTransformer;
import uk.gov.companieshouse.web.accounts.util.ValidationContext;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

import java.util.ArrayList;
import java.util.List;
import uk.gov.companieshouse.web.accounts.validation.helper.ServiceExceptionHandler;

@Service
public class StocksServiceImpl implements StocksService {

    @Autowired
    private StocksTransformer stocksTransformer;

    @Autowired
    private ApiClientService apiClientService;

    @Autowired
    private SmallFullService smallFullService;

    @Autowired
    private ServiceExceptionHandler serviceExceptionHandler;

    @Autowired
    private ValidationContext validationContext;

    private static final UriTemplate STOCKS_URI =
        new UriTemplate("/transactions/{transactionId}/company-accounts/{companyAccountsId}/small-full/notes/stocks");

    private static final String RESOURCE_NAME = "stocks";

    @Override
    public StocksNote getStocks(String transactionId, String companyAccountsId, String companyNumber)
        throws ServiceException {

        ApiClient apiClient = apiClientService.getApiClient();

        StocksApi stocksApi = getStocksApi(apiClient, transactionId, companyAccountsId);
        StocksNote stocksNote = stocksTransformer.getStocks(stocksApi);

        BalanceSheetHeadings balanceSheetHeadings = getStocksBalanceSheetHeadings(apiClient, transactionId, companyAccountsId);
        stocksNote.setBalanceSheetHeadings(balanceSheetHeadings);

        return stocksNote;
    }

    @Override
    public List<ValidationError> submitStocks(String transactionId, String companyAccountsId,
                                              StocksNote stocksNote, String companyNumber) throws ServiceException {

        ApiClient apiClient = apiClientService.getApiClient();

        String uri = STOCKS_URI.expand(transactionId, companyAccountsId).toString();

        SmallFullApi smallFullApi = smallFullService.getSmallFullAccounts(apiClient, transactionId, companyAccountsId);

        StocksApi stocksApi = stocksTransformer.getStocksApi(stocksNote);

        boolean stocksResourceExists = hasStocks(smallFullApi.getLinks());

        try {
            ApiResponse apiResponse;
            if (!stocksResourceExists) {
                apiResponse = apiClient.smallFull().stocks().create(uri, stocksApi).execute();
            } else {
                apiResponse = apiClient.smallFull().stocks().update(uri, stocksApi).execute();
            }

            if (apiResponse.hasErrors()) {
                return validationContext.getValidationErrors(apiResponse.getErrors());
            }
        } catch (URIValidationException e) {
            serviceExceptionHandler.handleURIValidationException(e, RESOURCE_NAME);
        } catch (ApiErrorResponseException e) {
            serviceExceptionHandler.handleSubmissionException(e, RESOURCE_NAME);
        }

        return new ArrayList<>();
    }

    private StocksApi getStocksApi(ApiClient apiClient, String transactionId, String companyAccountsId)
            throws ServiceException {

        String uri = STOCKS_URI.expand(transactionId, companyAccountsId).toString();

        try {
            return apiClient.smallFull().stocks().get(uri).execute().getData();
        } catch (ApiErrorResponseException e) {
            serviceExceptionHandler.handleRetrievalException(e, RESOURCE_NAME);
        } catch (URIValidationException e) {
            serviceExceptionHandler.handleURIValidationException(e, RESOURCE_NAME);
        }

        return null;
    }

    private boolean hasStocks(SmallFullLinks smallFullLinks) {
        return smallFullLinks.getStocksNote() != null;
    }
    
    @Override
    public void deleteStocks(String transactionId, String companyAccountsId) throws ServiceException {
        ApiClient apiClient = apiClientService.getApiClient();

        String uri = STOCKS_URI.expand(transactionId, companyAccountsId).toString();

        try {
            apiClient.smallFull().stocks().delete(uri).execute();
        } catch (URIValidationException e) {
            serviceExceptionHandler.handleURIValidationException(e, RESOURCE_NAME);
        } catch (ApiErrorResponseException e) {
            serviceExceptionHandler.handleDeletionException(e, RESOURCE_NAME);
        }
    }

    private BalanceSheetHeadings getStocksBalanceSheetHeadings(ApiClient apiClient, String transactionId, String companyAccountsId)
            throws ServiceException {

        SmallFullApi smallFullApi = smallFullService.getSmallFullAccounts(apiClient, transactionId, companyAccountsId);

        return smallFullService.getBalanceSheetHeadings(smallFullApi);
    }

}
