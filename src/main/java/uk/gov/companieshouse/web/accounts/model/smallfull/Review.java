package uk.gov.companieshouse.web.accounts.model.smallfull;

import lombok.Getter;
import lombok.Setter;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.accountingpolicies.BasisOfPreparation;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.accountingpolicies.IntangibleAmortisationPolicy;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.accountingpolicies.TangibleDepreciationPolicy;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.accountingpolicies.OtherAccountingPolicy;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.accountingpolicies.TurnoverPolicy;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.accountingpolicies.ValuationInformationPolicy;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.creditorswithinoneyear.CreditorsWithinOneYear;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.creditorsafteroneyear.CreditorsAfterOneYear;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.debtors.Debtors;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.tangible.TangibleAssets;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.stocks.StocksNote;

@Getter
@Setter
public class Review {

    BalanceSheet balanceSheet;

    Statements statements;

    BasisOfPreparation basisOfPreparation;

    TurnoverPolicy turnoverPolicy;

    TangibleDepreciationPolicy tangibleDepreciationPolicy;

    IntangibleAmortisationPolicy intangibleAmortisationPolicy;

    ValuationInformationPolicy valuationInformationPolicy;

    OtherAccountingPolicy otherAccountingPolicy;
    
    CreditorsWithinOneYear creditorsWithinOneYear;

    CreditorsAfterOneYear creditorsAfterOneYear;

    Debtors debtors;
    
    TangibleAssets tangibleAssets;
    
    StocksNote stocks;
}
