package uk.gov.companieshouse.web.accounts.service.smallfull.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriTemplate;
import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.error.ApiErrorResponseException;
import uk.gov.companieshouse.api.handler.exception.URIValidationException;
import uk.gov.companieshouse.api.model.accounts.smallfull.SmallFullApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.SmallFullLinks;
import uk.gov.companieshouse.api.model.accounts.smallfull.stocks.StocksApi;
import uk.gov.companieshouse.web.accounts.api.ApiClientService;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.smallfull.BalanceSheet;
import uk.gov.companieshouse.web.accounts.model.smallfull.BalanceSheetHeadings;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.stocks.StocksNote;
import uk.gov.companieshouse.web.accounts.service.smallfull.BalanceSheetService;
import uk.gov.companieshouse.web.accounts.service.smallfull.SmallFullService;
import uk.gov.companieshouse.web.accounts.service.smallfull.StocksService;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.StocksTransformer;
import uk.gov.companieshouse.web.accounts.util.ValidationContext;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

import java.util.ArrayList;
import java.util.List;

@Service
public class StocksServiceImpl implements StocksService {

    @Autowired
    private StocksTransformer stocksTransformer;

    @Autowired
    private BalanceSheetService balanceSheetService;

    @Autowired
    private ApiClientService apiClientService;

    @Autowired
    private SmallFullService smallFullService;

    @Autowired
    private ValidationContext validationContext;

    private static final UriTemplate STOCKS_URI =
        new UriTemplate("/transactions/{transactionId}/company-accounts/{companyAccountsId}/small-full/notes/stocks");

    private static final String INVALID_URI_MESSAGE = "Invalid URI for stocks resource";

    @Override
    public StocksNote getStocks(String transactionId, String companyAccountsId, String companyNumber)
        throws ServiceException {

        StocksApi stocksApi = getStocksApi(transactionId, companyAccountsId);
        StocksNote stocksNote = stocksTransformer.getStocks(stocksApi);

        BalanceSheet balanceSheet = balanceSheetService.getBalanceSheet(
            transactionId, companyAccountsId, companyNumber);
        BalanceSheetHeadings balanceSheetHeadings = balanceSheet.getBalanceSheetHeadings();
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
            if (!stocksResourceExists) {
                apiClient.smallFull().stocks().create(uri, stocksApi).execute();
            } else {
                apiClient.smallFull().stocks().update(uri, stocksApi).execute();
            }
        } catch (URIValidationException e) {
            throw new ServiceException(INVALID_URI_MESSAGE, e);
        } catch (ApiErrorResponseException e) {
            if (e.getStatusCode() == HttpStatus.BAD_REQUEST.value()) {
                List<ValidationError> validationErrors = validationContext.getValidationErrors(e);
                if (validationErrors.isEmpty()) {
                    throw new ServiceException("Bad request when creating stocks resource", e);
                }
                return validationErrors;
            }
            throw new ServiceException("Error creating stocks resource", e);
        }

        return new ArrayList<>();
    }

    private StocksApi getStocksApi(String transactionId, String companyAccountsId) throws ServiceException {

        ApiClient apiClient = apiClientService.getApiClient();

        String uri = STOCKS_URI.expand(transactionId, companyAccountsId).toString();

        try {
            return apiClient.smallFull().stocks().get(uri).execute();
        } catch (ApiErrorResponseException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND.value()) {
                return null;
            }
            throw new ServiceException("Error when retrieving stocks", e);
        } catch (URIValidationException e) {
            throw new ServiceException(INVALID_URI_MESSAGE, e);
        }
    }

    private boolean hasStocks(SmallFullLinks smallFullLinks) {
        return smallFullLinks.getStocksNote() != null;
    }
}
