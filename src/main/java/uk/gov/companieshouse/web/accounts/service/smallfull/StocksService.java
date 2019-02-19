package uk.gov.companieshouse.web.accounts.service.smallfull;

import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.stocks.StocksNote;

public interface StocksService {

    /**
     * Fetch stocks note
     *
     * @param transactionId The id of the CHS transaction
     * @param companyAccountsId The company accounts identifier
     * @param companyNumber The company identifier
     * @return the stocks note
     */
    StocksNote getStocks(String transactionId, String companyAccountsId, String companyNumber) throws ServiceException;
}
