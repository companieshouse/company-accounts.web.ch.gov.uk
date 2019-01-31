package uk.gov.companieshouse.web.accounts.service.smallfull.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriTemplate;
import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.error.ApiErrorResponseException;
import uk.gov.companieshouse.api.handler.exception.URIValidationException;
import uk.gov.companieshouse.api.model.accounts.smallfull.stocks.StocksApi;
import uk.gov.companieshouse.web.accounts.api.ApiClientService;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.smallfull.BalanceSheet;
import uk.gov.companieshouse.web.accounts.model.smallfull.BalanceSheetHeadings;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.stocks.StocksNote;
import uk.gov.companieshouse.web.accounts.service.smallfull.BalanceSheetService;
import uk.gov.companieshouse.web.accounts.service.smallfull.StocksService;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.StocksTransformer;

@Service
public class StocksServiceImpl implements StocksService {

    @Autowired
    private StocksTransformer stocksTransformer;

    @Autowired
    private BalanceSheetService balanceSheetService;

    @Autowired
    private ApiClientService apiClientService;



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
}
