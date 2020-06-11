package uk.gov.companieshouse.web.accounts.model.smallfull.notes.accountingpolicies;

import uk.gov.companieshouse.web.accounts.model.Note;
import uk.gov.companieshouse.web.accounts.validation.ValidationModel;

@ValidationModel
public class AccountingPolicies implements Note {

    public AccountingPolicies() {
        this.intangibleAmortisationPolicy = new IntangibleAmortisationPolicy();
        this.basisOfPreparation = new BasisOfPreparation();
        this.otherAccountingPolicy = new OtherAccountingPolicy();
        this.tangibleDepreciationPolicy = new TangibleDepreciationPolicy();
        this.turnoverPolicy = new TurnoverPolicy();
        this.valuationInformationPolicy = new ValuationInformationPolicy();
    }

    private IntangibleAmortisationPolicy intangibleAmortisationPolicy;

    private BasisOfPreparation basisOfPreparation;

    private OtherAccountingPolicy otherAccountingPolicy;

    private TangibleDepreciationPolicy tangibleDepreciationPolicy;

    private TurnoverPolicy turnoverPolicy;

    private ValuationInformationPolicy valuationInformationPolicy;

    public IntangibleAmortisationPolicy getIntangibleAmortisationPolicy() {
        return intangibleAmortisationPolicy;
    }

    public void setIntangibleAmortisationPolicy(IntangibleAmortisationPolicy intangibleAmortisationPolicy) {
        this.intangibleAmortisationPolicy = intangibleAmortisationPolicy;
    }

    public BasisOfPreparation getBasisOfPreparation() {
        return basisOfPreparation;
    }

    public void setBasisOfPreparation(BasisOfPreparation basisOfPreparation) {
        this.basisOfPreparation = basisOfPreparation;
    }

    public OtherAccountingPolicy getOtherAccountingPolicy() {
        return otherAccountingPolicy;
    }

    public void setOtherAccountingPolicy(OtherAccountingPolicy otherAccountingPolicy) {
        this.otherAccountingPolicy = otherAccountingPolicy;
    }

    public TangibleDepreciationPolicy getTangibleDepreciationPolicy() {
        return tangibleDepreciationPolicy;
    }

    public void setTangibleDepreciationPolicy(TangibleDepreciationPolicy tangibleDepreciationPolicy) {
        this.tangibleDepreciationPolicy = tangibleDepreciationPolicy;
    }

    public TurnoverPolicy getTurnoverPolicy() {
        return turnoverPolicy;
    }

    public void setTurnoverPolicy(TurnoverPolicy turnoverPolicy) {
        this.turnoverPolicy = turnoverPolicy;
    }

    public ValuationInformationPolicy getValuationInformationPolicy() {
        return valuationInformationPolicy;
    }

    public void setValuationInformationPolicy(ValuationInformationPolicy valuationInformationPolicy) {
        this.valuationInformationPolicy = valuationInformationPolicy;
    }
}
