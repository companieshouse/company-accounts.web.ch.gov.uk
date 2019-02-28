package uk.gov.companieshouse.web.accounts.service.smallfull;

import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.CurrentAssetsInvestments;

public interface CurrentAssetsInvestmentsService {

    /**
     * Fetch the current assets investments note
     *
     * @param transactionId The id of the CHS transaction
     * @param companyAccountsId The company accounts identifier
     * @param companyNumber The company identifier
     * @return the current assets investments note
     * @throws ServiceException if there's an error when retrieving the current assets investments note
     */
    CurrentAssetsInvestments getCurrentAssetsInvestments(String transactionId, String companyAccountsId, String companyNumber)
        throws ServiceException;
}
