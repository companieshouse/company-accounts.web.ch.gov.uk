package uk.gov.companieshouse.web.accounts.model.state;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;

public class AccountingPolicies implements Serializable {

    @JsonProperty("has_provided_turnover_policy")
    private Boolean hasProvidedTurnoverPolicy;

    @JsonProperty("has_provided_tangible_policy")
    private Boolean hasProvidedTangiblePolicy;

    @JsonProperty("has_provided_intangible_policy")
    private Boolean hasProvidedIntangiblePolicy;

    @JsonProperty("has_provided_valuation_information_policy")
    private Boolean hasProvidedValuationInformationPolicy;

    @JsonProperty("has_provided_other_accounting_policy")
    private Boolean hasProvidedOtherAccountingPolicy;

    public Boolean getHasProvidedTurnoverPolicy() {
        return hasProvidedTurnoverPolicy;
    }

    public void setHasProvidedTurnoverPolicy(Boolean hasProvidedTurnoverPolicy) {
        this.hasProvidedTurnoverPolicy = hasProvidedTurnoverPolicy;
    }

    public Boolean getHasProvidedTangiblePolicy() {
        return hasProvidedTangiblePolicy;
    }

    public void setHasProvidedTangiblePolicy(Boolean hasProvidedTangiblePolicy) {
        this.hasProvidedTangiblePolicy = hasProvidedTangiblePolicy;
    }

    public Boolean getHasProvidedIntangiblePolicy() {
        return hasProvidedIntangiblePolicy;
    }

    public void setHasProvidedIntangiblePolicy(Boolean hasProvidedIntangiblePolicy) {
        this.hasProvidedIntangiblePolicy = hasProvidedIntangiblePolicy;
    }

    public Boolean getHasProvidedValuationInformationPolicy() {
        return hasProvidedValuationInformationPolicy;
    }

    public void setHasProvidedValuationInformationPolicy(
        Boolean hasProvidedValuationInformationPolicy) {
        this.hasProvidedValuationInformationPolicy = hasProvidedValuationInformationPolicy;
    }

    public Boolean getHasProvidedOtherAccountingPolicy() {
        return hasProvidedOtherAccountingPolicy;
    }

    public void setHasProvidedOtherAccountingPolicy(Boolean hasProvidedOtherAccountingPolicy) {
        this.hasProvidedOtherAccountingPolicy = hasProvidedOtherAccountingPolicy;
    }
}
