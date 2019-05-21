package uk.gov.companieshouse.web.accounts.model.cic.statements;

import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConsultationWithStakeholdersSelection {

    @NotNull(message = "{consultationWithStakeholdersSelection.selectionNotMade}")
    private Boolean hasProvidedConsultationWithStakeholders;
}
