package uk.gov.companieshouse.web.accounts.model.relatedpartytransactions;

import uk.gov.companieshouse.web.accounts.validation.ValidationMapping;

public class RptTransactionToAdd {

    @ValidationMapping("$.rpt_transaction.name_of_related_party")
    private String nameOfRelatedParty;

    @ValidationMapping("$.rpt_transaction.relationship")
    private String relationship;

    @ValidationMapping("$.rpt_transaction.description_of_transaction")
    private String descriptionOfTransaction;

    @ValidationMapping("$.rpt_transaction.transaction_type")
    private String transactionType;

    private RptTransactionBreakdown breakdown;

    public String getNameOfRelatedParty() {
        return nameOfRelatedParty;
    }

    public void setNameOfRelatedParty(String nameOfRelatedParty) {
        this.nameOfRelatedParty = nameOfRelatedParty;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public String getDescriptionOfTransaction() {
        return descriptionOfTransaction;
    }

    public void setDescriptionOfTransaction(String descriptionOfTransaction) {
        this.descriptionOfTransaction = descriptionOfTransaction;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public RptTransactionBreakdown getBreakdown() {
        return breakdown;
    }

    public void setBreakdown(RptTransactionBreakdown breakdown) {
        this.breakdown = breakdown;
    }
}
