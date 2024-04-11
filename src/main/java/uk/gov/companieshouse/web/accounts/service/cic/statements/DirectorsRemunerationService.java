package uk.gov.companieshouse.web.accounts.service.cic.statements;

import java.util.List;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.cic.statements.DirectorsRemuneration;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

public interface DirectorsRemunerationService {
    /**
     * Retrieves the directors remuneration
     *
     * @param transactionId The id of the CHS transaction
     * @param companyAccountsId The company accounts identifier
     * @return the directors remuneration
     * @throws ServiceException if there's an error while fetching the resource
     */
    DirectorsRemuneration getDirectorsRemuneration(String transactionId, String companyAccountsId)
        throws ServiceException;

    /**
     * Submits the directors remuneration
     *
     * @param transactionId The id of the CHS transaction
     * @param companyAccountsId The company accounts identifier
     * @return a list of validation errors, or an empty list if none are present
     * @throws ServiceException if there's an error while submitting the resource
     */
    List<ValidationError> submitDirectorsRemuneration(String transactionId,
        String companyAccountsId,
        DirectorsRemuneration directorsRemuneration) throws ServiceException;
}
