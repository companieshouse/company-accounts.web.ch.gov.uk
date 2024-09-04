package uk.gov.companieshouse.web.accounts.service.smallfull;

import java.util.List;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.directorsreport.AddOrRemoveDirectors;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

public interface SecretaryService {

    String getSecretary(String transactionId, String companyAccountsId) throws ServiceException;

    List<ValidationError> submitSecretary(String transactionId, String companyAccountsId,
                                          AddOrRemoveDirectors addOrRemoveDirectors) throws ServiceException;

    void deleteSecretary(String transactionId, String companyAccountsId) throws ServiceException;
}
