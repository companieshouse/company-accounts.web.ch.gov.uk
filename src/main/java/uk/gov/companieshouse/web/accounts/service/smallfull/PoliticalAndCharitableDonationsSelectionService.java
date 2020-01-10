package uk.gov.companieshouse.web.accounts.service.smallfull;

import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.directorsreport.PoliticalAndCharitableDonationsSelection;

public interface PoliticalAndCharitableDonationsSelectionService {

    PoliticalAndCharitableDonationsSelection getPoliticalAndCharitableDonationsSelection(String transactionId,
            String companyAccountsId) throws ServiceException;

    void submitPoliticalAndCharitableDonations(String transactionId, String companyAccountsId,
            PoliticalAndCharitableDonationsSelection politicalAndCharitableDonationsSelection) throws ServiceException;
}
