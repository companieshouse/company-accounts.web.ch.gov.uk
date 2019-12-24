package uk.gov.companieshouse.web.accounts.service.smallfull;

import java.util.List;

import uk.gov.companieshouse.api.error.ApiErrorResponseException;
import uk.gov.companieshouse.api.handler.exception.URIValidationException;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.directorsreport.AddOrRemoveDirectors;
import uk.gov.companieshouse.web.accounts.model.directorsreport.Director;
import uk.gov.companieshouse.web.accounts.model.directorsreport.DirectorToAdd;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

public interface DirectorService {

    Director[] getAllDirectors(String transactionId, String companyAccountsId) throws ServiceException;

    List<ValidationError> createDirector(String transactionId, String companyAccountsId, DirectorToAdd directorToAdd) throws ServiceException;

    void deleteDirector(String transactionId, String companyAccountsId, String directorId) throws ServiceException;

    List<ValidationError> submitDirectorsReport(String transactionId, String companyAccountsId, String companyNumber, AddOrRemoveDirectors addOrRemoveDirectors)
            throws ServiceException;
}
