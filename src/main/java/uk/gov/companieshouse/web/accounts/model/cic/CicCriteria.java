package uk.gov.companieshouse.web.accounts.model.cic;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CicCriteria {

    @NotNull(message = "{criteria.selectionNotMade}")
    private Boolean isCriteriaMet;
}
