package uk.gov.companieshouse.web.accounts.service.smallfull;

import java.util.List;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.directorsreport.PrincipalActivities;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

public interface PrincipalActivitiesService {

    PrincipalActivities getPrincipalActivities(String transactionId, String companyAccountsId)
            throws ServiceException;

    List<ValidationError> submitPrincipalActivities(String transactionId, String companyAccountsId,
            PrincipalActivities principalActivities) throws ServiceException;
}
