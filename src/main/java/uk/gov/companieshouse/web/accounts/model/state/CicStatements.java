package uk.gov.companieshouse.web.accounts.model.state;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;

public class CicStatements implements Serializable {
    @JsonProperty("has_provided_consultation_with_stakeholders")
    private Boolean hasProvidedConsultationWithStakeholders;

    @JsonProperty("has_provided_transfer_of_assets")
    private Boolean hasProvidedTransferOfAssets;

    @JsonProperty("has_provided_directors_remuneration")
    private Boolean hasProvidedDirectorsRemuneration;

    public Boolean getHasProvidedConsultationWithStakeholders() {
        return hasProvidedConsultationWithStakeholders;
    }

    public void setHasProvidedConsultationWithStakeholders(
        Boolean hasProvidedConsultationWithStakeholders) {
        this.hasProvidedConsultationWithStakeholders = hasProvidedConsultationWithStakeholders;
    }

    public Boolean getHasProvidedTransferOfAssets() {
        return hasProvidedTransferOfAssets;
    }

    public void setHasProvidedTransferOfAssets(Boolean hasProvidedTransferOfAssets) {
        this.hasProvidedTransferOfAssets = hasProvidedTransferOfAssets;
    }

    public Boolean getHasProvidedDirectorsRemuneration() {
        return hasProvidedDirectorsRemuneration;
    }

    public void setHasProvidedDirectorsRemuneration(Boolean hasProvidedDirectorsRemuneration) {
        this.hasProvidedDirectorsRemuneration = hasProvidedDirectorsRemuneration;
    }
}
