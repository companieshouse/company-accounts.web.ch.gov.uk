package uk.gov.companieshouse.web.accounts.model.smallfull.notes.debtors;

import uk.gov.companieshouse.web.accounts.validation.ValidationMapping;

public class PrepaymentsAndAccruedIncome {
    @ValidationMapping("$.debtors.current_period.prepayments_and_accrued_income")
    private Long currentPrepaymentsAndAccruedIncome;

    @ValidationMapping("$.debtors.previous_period.prepayments_and_accrued_income")
    private Long previousPrepaymentsAndAccruedIncome;

    public Long getCurrentPrepaymentsAndAccruedIncome() {
        return currentPrepaymentsAndAccruedIncome;
    }

    public void setCurrentPrepaymentsAndAccruedIncome(Long currentPrepaymentsAndAccruedIncome) {
        this.currentPrepaymentsAndAccruedIncome = currentPrepaymentsAndAccruedIncome;
    }

    public Long getPreviousPrepaymentsAndAccruedIncome() {
        return previousPrepaymentsAndAccruedIncome;
    }

    public void setPreviousPrepaymentsAndAccruedIncome(Long previousPrepaymentsAndAccruedIncome) {
        this.previousPrepaymentsAndAccruedIncome = previousPrepaymentsAndAccruedIncome;
    }
}
