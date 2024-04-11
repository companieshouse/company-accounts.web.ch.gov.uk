package uk.gov.companieshouse.web.accounts.service.smallfull;

import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.directorsreport.PrincipalActivitiesSelection;

public interface PrincipalActivitiesSelectionService {
    PrincipalActivitiesSelection getPrincipalActivitiesSelection(String transactionId,
            String companyAccountsId) throws ServiceException;

    void submitPrincipalActivitiesSelection(String transactionId, String companyAccountsId,
            PrincipalActivitiesSelection principalActivitiesSelection) throws ServiceException;
}
