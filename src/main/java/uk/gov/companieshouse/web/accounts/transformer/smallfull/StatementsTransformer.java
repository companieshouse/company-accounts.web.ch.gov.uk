package uk.gov.companieshouse.web.accounts.transformer.smallfull;

import uk.gov.companieshouse.api.model.accounts.smallfull.BalanceSheetStatementsApi;
import uk.gov.companieshouse.web.accounts.model.smallfull.Statements;

public interface StatementsTransformer {

    Statements getBalanceSheetStatements(BalanceSheetStatementsApi statementsApi);

}
