package uk.gov.companieshouse.web.accounts.model.smallfull;

import lombok.Getter;
import lombok.Setter;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.Debtors;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.accountingpolicies.BasisOfPreparation;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.accountingpolicies.IntangibleAmortisationPolicy;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.accountingpolicies.TangibleDepreciationPolicy;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.accountingpolicies.OtherAccountingPolicy;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.accountingpolicies.TurnoverPolicy;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.accountingpolicies.ValuationInformationPolicy;

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

    Debtors debtors;
}
