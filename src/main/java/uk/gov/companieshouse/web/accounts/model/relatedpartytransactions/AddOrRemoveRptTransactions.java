package uk.gov.companieshouse.web.accounts.model.relatedpartytransactions;

public class AddOrRemoveRptTransactions {

    private RptTransaction[] existingTransactions;

    private RptTransactionToAdd rptTransactionToAdd;

    public RptTransaction[] getExistingRptTransactions() {
        return existingTransactions;
    }

    public void setExistingRptTransactions(RptTransaction[] existingTransactions) {
        this.existingTransactions = existingTransactions;
    }

    public RptTransactionToAdd getRptTransactionToAdd() {
        return rptTransactionToAdd;
    }

    public void setRptTransactionToAdd(RptTransactionToAdd rptTransactionToAdd) {
        this.rptTransactionToAdd = rptTransactionToAdd;
    }
}
