package uk.gov.companieshouse.web.accounts.model.smallfull.notes.creditorswithinoneyear;

import lombok.Getter;
import lombok.Setter;

import uk.gov.companieshouse.web.accounts.validation.ValidationMapping;
import uk.gov.companieshouse.web.accounts.validation.ValidationModel;

@Getter
@Setter
@ValidationModel
public class CreditorsWithinOneYear {

    @ValidationMapping("$.creditors_within_one_year.current_period.details")
    private String details;

    private AccrualsAndDeferredIncome accrualsAndDeferredIncome;

    private BankLoansAndOverdrafts bankLoansAndOverdrafts;

    private FinanceLeasesAndHirePurchaseContracts financeLeasesAndHirePurchaseContracts;
    
    private OtherCreditors otherCreditors;
    
    private TaxationAndSocialSecurity taxationAndSocialSecurity;
    
    private Total total;

    private TradeCreditors tradeCreditors;
}
