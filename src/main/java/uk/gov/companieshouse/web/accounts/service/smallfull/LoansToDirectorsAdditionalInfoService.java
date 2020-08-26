package uk.gov.companieshouse.web.accounts.service.smallfull;

import uk.gov.companieshouse.api.model.accounts.smallfull.loanstodirectors.AdditionalInformationApi;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

import java.util.List;

public interface LoansToDirectorsAdditionalInfoService {

    AdditionalInformationApi getAdditionalInformation(String transactionId, String companyAccountsId)
            throws ServiceException;

    List<ValidationError> createAdditionalInformation(String transactionId, String companyAccountsId, AdditionalInformationApi additionalInformationApi)
            throws ServiceException;

    List<ValidationError> updateAdditionalInformation(String transactionId, String companyAccountsId, AdditionalInformationApi additionalInformationApi)
            throws ServiceException;

    void deleteAdditionalInformation(String transactionId, String companyAccountsId)
            throws ServiceException;
}
