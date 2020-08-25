package uk.gov.companieshouse.web.accounts.service.smallfull;

import uk.gov.companieshouse.web.accounts.exception.ServiceException;

public interface AdditionalInformationSelectionService<T> {

    T getAdditionalInformationSelection(String transactionId, String companyAccountsId)
            throws ServiceException;

    void submitAdditionalInformationSelection(String transactionId, String companyAccountsId,
            T additionalInformationSelection) throws ServiceException;
}
