package uk.gov.companieshouse.web.accounts.service.smallfull;

import java.util.List;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.accountingpolicies.TurnoverPolicy;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

public interface TurnoverPolicyService {

    TurnoverPolicy getTurnOverPolicy(String transactionId, String companyAccountsId)
        throws ServiceException;

    List<ValidationError> postTurnoverPolicy(String transactionId, String companyAccountsId,
        TurnoverPolicy
            turnoverPolicy) throws ServiceException;
}
