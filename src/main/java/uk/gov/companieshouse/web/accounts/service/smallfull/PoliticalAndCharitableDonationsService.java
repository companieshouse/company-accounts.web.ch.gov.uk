package uk.gov.companieshouse.web.accounts.service.smallfull;

import java.util.List;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.directorsreport.PoliticalAndCharitableDonations;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

public interface PoliticalAndCharitableDonationsService {

    PoliticalAndCharitableDonations getPoliticalAndCharitableDonations(String transactionId, String companyAccountsId)
            throws ServiceException;

    List<ValidationError> submitPoliticalAndCharitableDonations(String transactionId, String companyAccountsId,
            PoliticalAndCharitableDonations politicalAndCharitableDonations) throws ServiceException;
}
