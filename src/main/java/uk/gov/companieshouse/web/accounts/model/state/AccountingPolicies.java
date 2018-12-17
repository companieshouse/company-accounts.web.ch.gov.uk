package uk.gov.companieshouse.web.accounts.model.state;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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
}
