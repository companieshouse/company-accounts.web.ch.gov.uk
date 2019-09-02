package uk.gov.companieshouse.web.accounts.model.cic.statements;

import javax.validation.constraints.NotNull;

public class DirectorsRemunerationSelection {

    @NotNull(message = "{directorsRemunerationSelection.selectionNotMade}")
    private Boolean hasProvidedDirectorsRemuneration;

    public Boolean getHasProvidedDirectorsRemuneration() {
        return hasProvidedDirectorsRemuneration;
    }

    public void setHasProvidedDirectorsRemuneration(Boolean hasProvidedDirectorsRemuneration) {
        this.hasProvidedDirectorsRemuneration = hasProvidedDirectorsRemuneration;
    }
}
