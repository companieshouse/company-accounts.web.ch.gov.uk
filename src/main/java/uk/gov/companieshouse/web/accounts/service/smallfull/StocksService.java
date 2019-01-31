package uk.gov.companieshouse.web.accounts.service.smallfull;

import java.util.List;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.stocks.StocksNote;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

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

    /**
     * Delete the stocks note
     *
     * @param transactionId The id of the CHS transaction
     * @param companyAccountsId The company accounts identifier
     * @return A list of validation errors, or an empty array list if none are present
     * @throws ServiceException if there's an error on deletion
     */
    List<ValidationError> deleteStocks(String transactionId, String companyAccountsId) throws ServiceException;

}
