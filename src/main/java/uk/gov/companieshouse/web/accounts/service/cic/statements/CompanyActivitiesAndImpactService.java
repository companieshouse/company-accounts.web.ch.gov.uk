package uk.gov.companieshouse.web.accounts.service.cic.statements;

import java.util.List;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.cic.statements.CompanyActivitiesAndImpact;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

public interface CompanyActivitiesAndImpactService {
    /**
     * Retrieves the company's activities and impact
     *
     * @param transactionId The id of the CHS transaction
     * @param companyAccountsId The company accounts identifier
     * @return the company's activities and impact
     * @throws ServiceException if there's an error while fetching the resource
     */
    CompanyActivitiesAndImpact getCompanyActivitiesAndImpact(String transactionId, String companyAccountsId)
        throws ServiceException;

    /**
     * Submits the company's activities and impact
     *
     * @param transactionId The id of the CHS transaction
     * @param companyAccountsId The company accounts identifier
     * @return a list of validation errors, or an empty list if none are present
     * @throws ServiceException if there's an error while submitting the resource
     */
    List<ValidationError> submitCompanyActivitiesAndImpact(String transactionId, String companyAccountsId,
            CompanyActivitiesAndImpact companyActivitiesAndImpact) throws ServiceException;
}
