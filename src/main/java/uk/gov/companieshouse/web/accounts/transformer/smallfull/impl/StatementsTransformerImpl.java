package uk.gov.companieshouse.web.accounts.transformer.smallfull.impl;

import org.springframework.stereotype.Component;
import uk.gov.companieshouse.api.model.accounts.smallfull.BalanceSheetLegalStatementsApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.BalanceSheetStatementsApi;
import uk.gov.companieshouse.web.accounts.model.smallfull.Statements;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.StatementsTransformer;

@Component
public class StatementsTransformerImpl implements StatementsTransformer {

    @Override
    public Statements getBalanceSheetStatements(BalanceSheetStatementsApi statementsApi) {

        Statements statements = new Statements();

        BalanceSheetLegalStatementsApi legalStatementsApi = statementsApi.getLegalStatements();
        statements.setAuditNotRequiredByMembers(legalStatementsApi.getAuditNotRequiredByMembers());
        statements.setDirectorsResponsibility(legalStatementsApi.getDirectorsResponsibility());
        statements.setNoProfitAndLoss(legalStatementsApi.getNoProfitAndLoss());
        statements.setSection477(legalStatementsApi.getSection477());
        statements.setSmallCompaniesRegime(legalStatementsApi.getSmallCompaniesRegime());

        return statements;
    }
}
