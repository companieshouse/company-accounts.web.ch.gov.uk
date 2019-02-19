package uk.gov.companieshouse.web.accounts.service.smallfull;

import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.stocks.StocksNote;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

import java.util.List;

public interface StocksService {

    /**
     * Fetch stocks note
     *
     * @param transactionId     The id of the CHS transaction
     * @param companyAccountsId The company accounts identifier
     * @param companyNumber     The company identifier
     * @return the stocks note
     */
    StocksNote getStocks(String transactionId, String companyAccountsId, String companyNumber) throws ServiceException;

    /**
     * Submit the stocks note
     *
     * @param transactionId     The id of the CHS transaction
     * @param companyAccountsId The company accounts identifier
     * @param stocksNote        stocks note to submit
     * @param companyNumber     The company number
     * @return A list of validation errors, or an empty array list if none are present
     * @throws ServiceException if there's an error on submission
     */
    List<ValidationError> submitStocks(String transactionId, String companyAccountsId, StocksNote stocksNote, String companyNumber)
        throws ServiceException;
}

