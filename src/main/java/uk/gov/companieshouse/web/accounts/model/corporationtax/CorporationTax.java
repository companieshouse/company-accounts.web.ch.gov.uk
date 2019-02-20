package uk.gov.companieshouse.web.accounts.model.corporationtax;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CorporationTax {

    @NotNull(message = "{corporationTax.selectionNotMade}")
    private Boolean fileChoice;
}
