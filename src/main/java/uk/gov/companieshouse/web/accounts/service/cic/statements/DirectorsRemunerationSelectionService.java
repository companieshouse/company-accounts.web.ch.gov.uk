package uk.gov.companieshouse.web.accounts.service.cic.statements;

import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.cic.statements.ConsultationWithStakeholdersSelection;
import uk.gov.companieshouse.web.accounts.model.cic.statements.DirectorsRemunerationSelection;

public interface DirectorsRemunerationSelectionService {

    DirectorsRemunerationSelection getDirectorsRemunerationSelection(
            String transactionId, String companyAccountsId) throws ServiceException;

    void submitDirectorsRemunerationSelection(String transactionId, String companyAccountsId,
            DirectorsRemunerationSelection selection) throws ServiceException;
}
