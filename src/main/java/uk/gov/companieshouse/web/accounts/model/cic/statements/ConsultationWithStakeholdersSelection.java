package uk.gov.companieshouse.web.accounts.model.cic.statements;

import jakarta.validation.constraints.NotNull;

public class ConsultationWithStakeholdersSelection {
    @NotNull(message = "{consultationWithStakeholdersSelection.selectionNotMade}")
    private Boolean hasProvidedConsultationWithStakeholders;

    public Boolean getHasProvidedConsultationWithStakeholders() {
        return hasProvidedConsultationWithStakeholders;
    }

    public void setHasProvidedConsultationWithStakeholders(
        Boolean hasProvidedConsultationWithStakeholders) {
        this.hasProvidedConsultationWithStakeholders = hasProvidedConsultationWithStakeholders;
    }
}
