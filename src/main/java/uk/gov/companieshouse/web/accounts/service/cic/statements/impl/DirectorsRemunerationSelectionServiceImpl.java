package uk.gov.companieshouse.web.accounts.service.cic.statements.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.companieshouse.api.model.accounts.cic.statements.CicStatementsApi;
import uk.gov.companieshouse.web.accounts.enumeration.DefaultCicStatements;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.cic.statements.DirectorsRemunerationSelection;
import uk.gov.companieshouse.web.accounts.service.cic.statements.CicStatementsService;
import uk.gov.companieshouse.web.accounts.service.cic.statements.DirectorsRemunerationSelectionService;

@Service
public class DirectorsRemunerationSelectionServiceImpl implements
        DirectorsRemunerationSelectionService {

    @Autowired
    private CicStatementsService cicStatementsService;

    @Override
    public DirectorsRemunerationSelection getDirectorsRemunerationSelection(String transactionId,
            String companyAccountsId) throws ServiceException {

        DirectorsRemunerationSelection selection = new DirectorsRemunerationSelection();

        CicStatementsApi statements =
                cicStatementsService.getCicStatementsApi(transactionId, companyAccountsId);

        if (!statements.getReportStatements()
                .getDirectorsRemuneration()
                        .equals(DefaultCicStatements.DIRECTORS_REMUNERATION.getDefaultStatement())) {

            selection.setHasProvidedDirectorsRemuneration(true);
        }

        return selection;
    }

    @Override
    public void submitDirectorsRemunerationSelection(String transactionId, String companyAccountsId,
            DirectorsRemunerationSelection selection) throws ServiceException {

        if (Boolean.FALSE.equals(selection.getHasProvidedDirectorsRemuneration())) {

            CicStatementsApi statements =
                    cicStatementsService.getCicStatementsApi(transactionId, companyAccountsId);

            if (!statements.getReportStatements()
                    .getDirectorsRemuneration()
                            .equals(DefaultCicStatements.DIRECTORS_REMUNERATION.getDefaultStatement())) {

                statements.getReportStatements().setDirectorsRemuneration(
                        DefaultCicStatements.DIRECTORS_REMUNERATION.getDefaultStatement());

                cicStatementsService.updateCicStatementsApi(transactionId, companyAccountsId, statements);
            }
        }
    }
}
