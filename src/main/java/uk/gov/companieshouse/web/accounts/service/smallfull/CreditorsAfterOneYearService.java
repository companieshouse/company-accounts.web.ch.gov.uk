package uk.gov.companieshouse.web.accounts.service.smallfull;

import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.creditorsafteroneyear.CreditorsAfterOneYear;

public interface CreditorsAfterOneYearService {

    /**
     * Fetch creditors after one year note
     *
     * @param transactionId The id of the CHS transaction
     * @param companyAccountsId The company accounts identifier
     * @param companyNumber The company identifier
     * @return the creditors after one year note
     * @throws ServiceException if there's an error when retrieving the debtors note
     */
    CreditorsAfterOneYear getCreditorsAfterOneYear(String transactionId, String companyAccountsId, String companyNumber)
            throws ServiceException;
}
