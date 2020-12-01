package uk.gov.companieshouse.web.accounts.transformer.smallfull.relatedpartytransactions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.api.model.accounts.smallfull.relatedpartytransactions.RptTransactionApi;
import uk.gov.companieshouse.web.accounts.model.relatedpartytransactions.RptTransaction;
import uk.gov.companieshouse.web.accounts.model.relatedpartytransactions.RptTransactionToAdd;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class RptTransactionsTransformerImpl implements RptTransactionsTransformer {

    @Autowired
    private RptBreakdownTransformer rptBreakdownTransformer;

    private static final Pattern RPT_TRANSACTION_URI_PATTERN = Pattern.compile("/transactions/.+?/company-accounts/.+?/small-full/notes/related-party-transactions/transactions/(.*)");

    @Override
    public RptTransactionApi getRptTransactionsApi(RptTransactionToAdd rptTransactionToAdd) {

        RptTransactionApi rptTransactionsApi = new RptTransactionApi();

        rptTransactionsApi.setNameOfRelatedParty(rptTransactionToAdd.getNameOfRelatedParty());

        rptTransactionsApi.setRelationship(rptTransactionToAdd.getRelationship());

        rptTransactionsApi.setDescriptionOfTransaction(rptTransactionToAdd.getDescriptionOfTransaction());

        rptTransactionsApi.setTransactionType(rptTransactionToAdd.getTransactionType());

        rptTransactionsApi.setBreakdown(rptBreakdownTransformer.mapRptTransactionsBreakdownToApi(rptTransactionToAdd));

        return rptTransactionsApi;
    }

    @Override
    public RptTransaction[] getAllRptTransactions(RptTransactionApi[] rptTransactions) {

        RptTransaction[] allRptTransactions = new RptTransaction[rptTransactions.length];

        for (int i = 0; i < rptTransactions.length; i++) {

            RptTransaction rptTransaction = new RptTransaction();

            rptTransaction.setNameOfRelatedParty(rptTransactions[i].getNameOfRelatedParty());

            rptTransaction.setRelationship(rptTransactions[i].getRelationship());

            rptTransaction.setDescriptionOfTransaction(rptTransactions[i].getDescriptionOfTransaction());

            rptTransaction.setTransactionType(rptTransactions[i].getTransactionType());

            rptTransaction.setBreakdown(rptBreakdownTransformer.mapRptTransactionsBreakdownToWeb(rptTransactions[i]));

            Matcher matcher = RPT_TRANSACTION_URI_PATTERN.matcher(rptTransactions[i].getLinks().getSelf());
            matcher.find();
            rptTransaction.setId(matcher.group(1));

            allRptTransactions[i] = rptTransaction;
        }

        return allRptTransactions;
    }
}
