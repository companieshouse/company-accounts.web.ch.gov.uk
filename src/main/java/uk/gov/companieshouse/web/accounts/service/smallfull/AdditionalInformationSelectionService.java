package uk.gov.companieshouse.web.accounts.service.smallfull;

import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.directorsreport.AdditionalInformationSelection;

public interface AdditionalInformationSelectionService {

    AdditionalInformationSelection getAdditionalInformationSelection(String transactionId, String companyAccountsId)
            throws ServiceException;

    void submitAdditionalInformation(String transactionId, String companyAccountsId,
            AdditionalInformationSelection additionalInformationSelection) throws ServiceException;
}
