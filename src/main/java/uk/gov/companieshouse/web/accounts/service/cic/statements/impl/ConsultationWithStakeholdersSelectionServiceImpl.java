package uk.gov.companieshouse.web.accounts.service.cic.statements.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.companieshouse.api.model.accounts.cic.statements.CicStatementsApi;
import uk.gov.companieshouse.web.accounts.enumeration.DefaultCicStatements;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.cic.statements.ConsultationWithStakeholdersSelection;
import uk.gov.companieshouse.web.accounts.service.cic.statements.CicStatementsService;
import uk.gov.companieshouse.web.accounts.service.cic.statements.ConsultationWithStakeholdersSelectionService;

@Service
public class ConsultationWithStakeholdersSelectionServiceImpl implements
        ConsultationWithStakeholdersSelectionService {

    @Autowired
    private CicStatementsService cicStatementsService;

    @Override
    public ConsultationWithStakeholdersSelection getConsultationWithStakeholdersSelection(
            String transactionId, String companyAccountsId) throws ServiceException {

        ConsultationWithStakeholdersSelection selection = new ConsultationWithStakeholdersSelection();

        CicStatementsApi statements =
                cicStatementsService.getCicStatementsApi(transactionId, companyAccountsId);

        if (!statements.getReportStatements()
                .getConsultationWithStakeholders()
                        .equals(DefaultCicStatements.CONSULTATION_WITH_STAKEHOLDERS.getDefaultStatement())) {

            selection.setHasProvidedConsultationWithStakeholders(true);
        }

        return selection;
    }

    @Override
    public void submitConsultationWithStakeholdersSelection(String transactionId, String companyAccountsId,
            ConsultationWithStakeholdersSelection selection)
            throws ServiceException {

        if (Boolean.FALSE.equals(selection.getHasProvidedConsultationWithStakeholders())) {

            CicStatementsApi statements =
                    cicStatementsService.getCicStatementsApi(transactionId, companyAccountsId);

            if (!statements.getReportStatements()
                    .getConsultationWithStakeholders()
                            .equals(DefaultCicStatements.CONSULTATION_WITH_STAKEHOLDERS.getDefaultStatement())) {

                statements.getReportStatements().setConsultationWithStakeholders(
                        DefaultCicStatements.CONSULTATION_WITH_STAKEHOLDERS.getDefaultStatement());

                cicStatementsService.updateCicStatementsApi(transactionId, companyAccountsId, statements);
            }
        }
    }
}
