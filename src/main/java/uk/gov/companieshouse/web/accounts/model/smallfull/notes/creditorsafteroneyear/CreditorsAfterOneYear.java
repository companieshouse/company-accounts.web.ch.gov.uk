package uk.gov.companieshouse.web.accounts.model.smallfull.notes.creditorsafteroneyear;

import lombok.Getter;
import lombok.Setter;
import uk.gov.companieshouse.web.accounts.model.smallfull.BalanceSheetHeadings;

@Getter
@Setter
public class CreditorsAfterOneYear {

    private BalanceSheetHeadings balanceSheetHeadings;

    private String details;

    private BankLoansAndOverdrafts bankLoansAndOverdrafts;

    private FinanceLeasesAndHirePurchaseContracts financeLeasesAndHirePurchaseContracts;

    private OtherCreditors otherCreditors;

    private Total total;

}
