package uk.gov.companieshouse.web.accounts.model.smallfull.notes.creditorswithinoneyear;

import lombok.Getter;
import lombok.Setter;
import uk.gov.companieshouse.web.accounts.validation.ValidationMapping;

@Getter
@Setter
public class TaxationAndSocialSecurity {

    @ValidationMapping("$.creditors_within_one_year.current_period.taxation_and_social_security")
    private Long currentTaxationAndSocialSecurity;

    @ValidationMapping("$.creditors_within_one_year.previous_period.taxation_and_social_security")
    private Long previousTaxationAndSocialSecurity;
}
