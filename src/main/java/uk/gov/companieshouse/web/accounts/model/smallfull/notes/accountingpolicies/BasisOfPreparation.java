package uk.gov.companieshouse.web.accounts.model.smallfull.notes.accountingpolicies;

import jakarta.validation.constraints.NotNull;
import uk.gov.companieshouse.web.accounts.enumeration.AccountingRegulatoryStandard;
import uk.gov.companieshouse.web.accounts.validation.ValidationModel;

@ValidationModel
public class BasisOfPreparation {

    @NotNull(message = "{basisOfPreparation.selectionNotMade}")
    private AccountingRegulatoryStandard accountingRegulatoryStandard;

    public AccountingRegulatoryStandard getAccountingRegulatoryStandard() {
        return accountingRegulatoryStandard;
    }

    public void setAccountingRegulatoryStandard(AccountingRegulatoryStandard accountingRegulatoryStandard) {
        this.accountingRegulatoryStandard = accountingRegulatoryStandard;
    }
}
