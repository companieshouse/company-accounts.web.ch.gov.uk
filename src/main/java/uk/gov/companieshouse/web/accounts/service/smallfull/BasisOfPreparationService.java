package uk.gov.companieshouse.web.accounts.service.smallfull;

import java.util.List;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.accountingpolicies.BasisOfPreparation;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

public interface BasisOfPreparationService {

    List<ValidationError> submitBasisOfPreparation(String transactionId, String companyAccountsId,
            BasisOfPreparation
                    basisOfPreparation) throws ServiceException;

    BasisOfPreparation getBasisOfPreparation(String transactionId, String companyAccountsId)
            throws ServiceException;

}
