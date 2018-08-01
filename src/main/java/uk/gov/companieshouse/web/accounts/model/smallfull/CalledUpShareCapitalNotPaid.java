package uk.gov.companieshouse.web.accounts.model.smallfull;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CalledUpShareCapitalNotPaid {

    @NotNull
    @Range(min = 0, max = 99999999, message = "{invalidRange}")
    private Integer currentAmount;
}
