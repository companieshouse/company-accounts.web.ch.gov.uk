package uk.gov.companieshouse.web.accounts.model.smallfull.notes.creditorsafteroneyear;

import lombok.Getter;
import lombok.Setter;
import uk.gov.companieshouse.web.accounts.model.smallfull.BalanceSheetHeadings;
import uk.gov.companieshouse.web.accounts.validation.ValidationMapping;
import uk.gov.companieshouse.web.accounts.validation.ValidationModel;

@Getter
@Setter
@ValidationModel
public class CreditorsAfterOneYear {

    private BalanceSheetHeadings balanceSheetHeadings;

    @ValidationMapping("$.creditors_after_one_year.current_period.details")
    private String details;

    private BankLoansAndOverdrafts bankLoansAndOverdrafts;

    private FinanceLeasesAndHirePurchaseContracts financeLeasesAndHirePurchaseContracts;

    private OtherCreditors otherCreditors;

    private Total total;

}
