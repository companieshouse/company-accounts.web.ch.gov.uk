package uk.gov.companieshouse.web.accounts.model.smallfull;

import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Criteria {

    @NotNull(message = "{criteria.selectionNotMade}")
    private String isCriteriaMet;
}
