package uk.gov.companieshouse.web.accounts.service.smallfull;

import uk.gov.companieshouse.api.model.accounts.directorsreport.SecretaryApi;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.directorsreport.AddOrRemoveDirectors;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

import java.util.List;

public interface SecretaryService {

    AddOrRemoveDirectors getSecretary(String transactionId, String companyAccountsId, String secretaryId) throws ServiceException;

    List<ValidationError> createSecretary(String transactionId, String companyAccountsId,
                                          AddOrRemoveDirectors addOrRemoveDirectors) throws ServiceException;

    void deleteSecretary(String transactionId, String companyAccountsId, String secretaryId) throws ServiceException;
}
