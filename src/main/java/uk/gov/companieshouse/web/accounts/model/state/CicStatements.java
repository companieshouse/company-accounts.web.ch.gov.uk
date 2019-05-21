package uk.gov.companieshouse.web.accounts.model.state;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CicStatements {

    @JsonProperty("has_provided_consultation_with_stakeholders")
    private Boolean hasProvidedConsultationWithStakeholders;

    @JsonProperty("has_provided_transfer_of_assets")
    private Boolean hasProvidedTransferOfAssets;

    @JsonProperty("has_provided_directors_remuneration")
    private Boolean hasProvidedDirectorsRemuneration;
}
