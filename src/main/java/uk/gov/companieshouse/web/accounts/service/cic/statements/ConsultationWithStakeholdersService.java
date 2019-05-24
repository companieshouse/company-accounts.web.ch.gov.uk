package uk.gov.companieshouse.web.accounts.service.cic.statements;

import java.util.List;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.cic.statements.ConsultationWithStakeholders;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

public interface ConsultationWithStakeholdersService {

    /**
     * Retrieves the consultation with stakeholders
     *
     * @param transactionId The id of the CHS transaction
     * @param companyAccountsId The company accounts identifier
     * @return the consultation with stakeholders
     * @throws ServiceException if there's an error while fetching the resource
     */
    ConsultationWithStakeholders getConsultationWithStakeholders(String transactionId,
        String companyAccountsId)
        throws ServiceException;

    /**
     * Submits the consultation with stakeholders
     *
     * @param transactionId The id of the CHS transaction
     * @param companyAccountsId The company accounts identifier
     * @return a list of validation errors, or an empty list if none are present
     * @throws ServiceException if there's an error while submitting the resource
     */
    List<ValidationError> submitConsultationWithStakeholders(String transactionId,
        String companyAccountsId,
        ConsultationWithStakeholders consultationWithStakeholders) throws ServiceException;
}
