package uk.gov.companieshouse.web.accounts.model.relatedpartytransactions;

public class RptTransaction {

    private String id;

    private String nameOfRelatedParty;

    private String relationship;

    private String descriptionOfTransaction;

    private String transactionType;

    private RptTransactionBreakdown breakdown;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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
