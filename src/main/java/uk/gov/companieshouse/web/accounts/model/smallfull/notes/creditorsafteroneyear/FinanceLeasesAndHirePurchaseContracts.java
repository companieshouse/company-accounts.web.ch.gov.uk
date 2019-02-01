package uk.gov.companieshouse.web.accounts.model.smallfull.notes.creditorsafteroneyear;

import lombok.Getter;
import lombok.Setter;
import uk.gov.companieshouse.web.accounts.validation.ValidationMapping;

@Getter
@Setter
public class FinanceLeasesAndHirePurchaseContracts {

    @ValidationMapping("$.creditors_after_one_year.current_period.finance_leases_and_hire_purchase_contracts")
    private Long currentFinanceLeasesAndHirePurchaseContracts;

    @ValidationMapping("$.creditors_after_one_year.previous_period.finance_leases_and_hire_purchase_contracts")
    private Long previousFinanceLeasesAndHirePurchaseContracts;
}
