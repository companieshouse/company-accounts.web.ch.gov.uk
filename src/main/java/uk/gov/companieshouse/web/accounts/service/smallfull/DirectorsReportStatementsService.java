package uk.gov.companieshouse.web.accounts.service.smallfull;

import java.util.List;
import uk.gov.companieshouse.api.model.accounts.directorsreport.StatementsApi;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

public interface DirectorsReportStatementsService {
    StatementsApi getDirectorsReportStatements(String transactionId, String companyAccountsId)
            throws ServiceException;

    List<ValidationError> createDirectorsReportStatements(String transactionId, String companyAccountsId, StatementsApi statementsApi)
            throws ServiceException;

    List<ValidationError> updateDirectorsReportStatements(String transactionId, String companyAccountsId, StatementsApi statementsApi)
            throws ServiceException;

    void deleteDirectorsReportStatements(String transactionId, String companyAccountsId)
            throws ServiceException;
}
