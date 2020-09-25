package uk.gov.companieshouse.web.accounts.service.smallfull;

import java.util.List;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.loanstodirectors.LoansToDirectorsAdditionalInfo;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

public interface LoansToDirectorsAdditionalInfoService {

    LoansToDirectorsAdditionalInfo getAdditionalInformation(String transactionId, String companyAccountsId)
            throws ServiceException;

    List<ValidationError> createAdditionalInformation(String transactionId, String companyAccountsId, LoansToDirectorsAdditionalInfo additionalInformation)
            throws ServiceException;

    List<ValidationError> updateAdditionalInformation(String transactionId, String companyAccountsId, LoansToDirectorsAdditionalInfo additionalInformation)
            throws ServiceException;

    void deleteAdditionalInformation(String transactionId, String companyAccountsId)
            throws ServiceException;
}
