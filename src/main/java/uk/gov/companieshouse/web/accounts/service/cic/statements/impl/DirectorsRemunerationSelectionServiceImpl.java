package uk.gov.companieshouse.web.accounts.service.cic.statements.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.companieshouse.api.model.accounts.cic.statements.CicStatementsApi;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.cic.statements.DirectorsRemunerationSelection;
import uk.gov.companieshouse.web.accounts.service.cic.statements.CicStatementsService;
import uk.gov.companieshouse.web.accounts.service.cic.statements.DirectorsRemunerationSelectionService;

@Service
public class DirectorsRemunerationSelectionServiceImpl implements
        DirectorsRemunerationSelectionService {

    @Autowired
    private CicStatementsService cicStatementsService;

    private static final String DEFAULT_DIRECTORS_REMUNERATION_STATEMENT =
            "No remuneration was received";

    @Override
    public DirectorsRemunerationSelection getDirectorsRemunerationSelection(String transactionId,
            String companyAccountsId) throws ServiceException {

        DirectorsRemunerationSelection selection = new DirectorsRemunerationSelection();

        CicStatementsApi statements =
                cicStatementsService.getCicStatementsApi(transactionId, companyAccountsId);

        if (!statements.getReportStatements()
                .getDirectorsRemuneration()
                        .equals(DEFAULT_DIRECTORS_REMUNERATION_STATEMENT)) {

            selection.setHasProvidedDirectorsRemuneration(true);
        }

        return selection;
    }

    @Override
    public void submitDirectorsRemunerationSelection(String transactionId, String companyAccountsId,
            DirectorsRemunerationSelection selection) throws ServiceException {

        if (!selection.getHasProvidedDirectorsRemuneration()) {

            CicStatementsApi statements =
                    cicStatementsService.getCicStatementsApi(transactionId, companyAccountsId);

            if (!statements.getReportStatements()
                    .getDirectorsRemuneration()
                            .equals(DEFAULT_DIRECTORS_REMUNERATION_STATEMENT)) {

                statements.getReportStatements().setDirectorsRemuneration(
                        DEFAULT_DIRECTORS_REMUNERATION_STATEMENT);

                cicStatementsService.updateCicStatementsApi(transactionId, companyAccountsId, statements);
            }
        }
    }
}
