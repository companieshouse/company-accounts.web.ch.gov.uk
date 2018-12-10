package uk.gov.companieshouse.web.accounts.model.state;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountingPolicies {

    @JsonProperty("has_provided_turnover_policy")
    private Boolean hasProvidedTurnoverPolicy;
}
