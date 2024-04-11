package uk.gov.companieshouse.web.accounts.transformer.smallfull;

import uk.gov.companieshouse.api.model.accounts.smallfull.BalanceSheetStatementsApi;
import uk.gov.companieshouse.web.accounts.model.smallfull.Statements;

public interface StatementsTransformer {
    /**
     * Get statements web model from a given statements api model
     *
     * @param statementsApi API balance sheet statements model
     * @return a statements web model
     */
    Statements getBalanceSheetStatements(BalanceSheetStatementsApi statementsApi);

}
