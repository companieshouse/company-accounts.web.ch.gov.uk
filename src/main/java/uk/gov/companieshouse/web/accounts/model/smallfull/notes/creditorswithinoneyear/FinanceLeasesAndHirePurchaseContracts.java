package uk.gov.companieshouse.web.accounts.model.smallfull.notes.creditorswithinoneyear;

import uk.gov.companieshouse.web.accounts.validation.ValidationMapping;

public class FinanceLeasesAndHirePurchaseContracts {
    @ValidationMapping("$.creditors_within_one_year.current_period.finance_leases_and_hire_purchase_contracts")
    private Long currentFinanceLeasesAndHirePurchaseContracts;

    @ValidationMapping("$.creditors_within_one_year.previous_period.finance_leases_and_hire_purchase_contracts")
    private Long previousFinanceLeasesAndHirePurchaseContracts;

    public Long getCurrentFinanceLeasesAndHirePurchaseContracts() {
        return currentFinanceLeasesAndHirePurchaseContracts;
    }

    public void setCurrentFinanceLeasesAndHirePurchaseContracts(
        Long currentFinanceLeasesAndHirePurchaseContracts) {
        this.currentFinanceLeasesAndHirePurchaseContracts = currentFinanceLeasesAndHirePurchaseContracts;
    }

    public Long getPreviousFinanceLeasesAndHirePurchaseContracts() {
        return previousFinanceLeasesAndHirePurchaseContracts;
    }

    public void setPreviousFinanceLeasesAndHirePurchaseContracts(
        Long previousFinanceLeasesAndHirePurchaseContracts) {
        this.previousFinanceLeasesAndHirePurchaseContracts = previousFinanceLeasesAndHirePurchaseContracts;
    }
}
