package uk.gov.companieshouse.web.accounts.service.smallfull;

import java.util.List;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.directorsreport.AdditionalInformation;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

public interface AdditionalInformationService {
    AdditionalInformation getAdditionalInformation(String transactionId, String companyAccountsId)
            throws ServiceException;

    List<ValidationError> submitAdditionalInformation(String transactionId, String companyAccountsId,
            AdditionalInformation additionalInformation) throws ServiceException;
}
