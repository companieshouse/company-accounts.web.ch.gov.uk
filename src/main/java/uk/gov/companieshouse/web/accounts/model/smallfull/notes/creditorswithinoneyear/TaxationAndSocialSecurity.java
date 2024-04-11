package uk.gov.companieshouse.web.accounts.model.smallfull.notes.creditorswithinoneyear;

import uk.gov.companieshouse.web.accounts.validation.ValidationMapping;

public class TaxationAndSocialSecurity {
    @ValidationMapping("$.creditors_within_one_year.current_period.taxation_and_social_security")
    private Long currentTaxationAndSocialSecurity;

    @ValidationMapping("$.creditors_within_one_year.previous_period.taxation_and_social_security")
    private Long previousTaxationAndSocialSecurity;

    public Long getCurrentTaxationAndSocialSecurity() {
        return currentTaxationAndSocialSecurity;
    }

    public void setCurrentTaxationAndSocialSecurity(Long currentTaxationAndSocialSecurity) {
        this.currentTaxationAndSocialSecurity = currentTaxationAndSocialSecurity;
    }

    public Long getPreviousTaxationAndSocialSecurity() {
        return previousTaxationAndSocialSecurity;
    }

    public void setPreviousTaxationAndSocialSecurity(Long previousTaxationAndSocialSecurity) {
        this.previousTaxationAndSocialSecurity = previousTaxationAndSocialSecurity;
    }
}
