package uk.gov.companieshouse.web.accounts.service.cic.statements;

import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.cic.statements.ConsultationWithStakeholdersSelection;

public interface ConsultationWithStakeholdersSelectionService {
    ConsultationWithStakeholdersSelection getConsultationWithStakeholdersSelection(
            String transactionId, String companyAccountsId) throws ServiceException;

    void submitConsultationWithStakeholdersSelection(String transactionId, String companyAccountsId,
            ConsultationWithStakeholdersSelection selection) throws ServiceException;
}
