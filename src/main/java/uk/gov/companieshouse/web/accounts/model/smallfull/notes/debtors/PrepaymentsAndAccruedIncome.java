package uk.gov.companieshouse.web.accounts.model.smallfull.notes.debtors;

import lombok.Getter;
import lombok.Setter;
import uk.gov.companieshouse.web.accounts.validation.ValidationMapping;

@Getter
@Setter
public class PrepaymentsAndAccruedIncome {

    @ValidationMapping("$.debtors.current_period.prepayments_and_accrued_income")
    private Long currentPrepaymentsAndAccruedIncome;

    @ValidationMapping("$.debtors.previous_period.prepayments_and_accrued_income")
    private Long previousPrepaymentsAndAccruedIncome;
}
