package uk.gov.companieshouse.web.accounts.transformer.smallfull;

import uk.gov.companieshouse.api.model.accounts.smallfull.stocks.StocksApi;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.stocks.StocksNote;

public interface StocksTransformer {

    StocksNote getStocks(StocksApi stocksApi);
}
