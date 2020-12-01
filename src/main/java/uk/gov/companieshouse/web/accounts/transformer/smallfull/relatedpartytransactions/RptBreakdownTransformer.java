package uk.gov.companieshouse.web.accounts.transformer.smallfull.relatedpartytransactions;

import org.springframework.stereotype.Component;
import uk.gov.companieshouse.api.model.accounts.smallfull.relatedpartytransactions.RptTransactionApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.relatedpartytransactions.RptTransactionBreakdownApi;
import uk.gov.companieshouse.web.accounts.model.relatedpartytransactions.RptTransactionBreakdown;
import uk.gov.companieshouse.web.accounts.model.relatedpartytransactions.RptTransactionToAdd;

@Component
public class RptBreakdownTransformer {

    public RptTransactionBreakdownApi mapRptTransactionsBreakdownToApi(RptTransactionToAdd rptTransactionToAdd) {

        RptTransactionBreakdownApi rptTransactionsBreakdownApi = new RptTransactionBreakdownApi();

        rptTransactionsBreakdownApi.setBalanceAtPeriodStart(rptTransactionToAdd.getBreakdown().getBalanceAtPeriodStart());
        rptTransactionsBreakdownApi.setBalanceAtPeriodEnd(rptTransactionToAdd.getBreakdown().getBalanceAtPeriodEnd());

        return rptTransactionsBreakdownApi;
    }

    public RptTransactionBreakdown mapRptTransactionsBreakdownToWeb(RptTransactionApi rptTransactionsApi) {

        RptTransactionBreakdown breakdown = new RptTransactionBreakdown();

        breakdown.setBalanceAtPeriodStart(rptTransactionsApi.getBreakdown().getBalanceAtPeriodStart());
        breakdown.setBalanceAtPeriodEnd(rptTransactionsApi.getBreakdown().getBalanceAtPeriodEnd());

        return breakdown;
    }
}
