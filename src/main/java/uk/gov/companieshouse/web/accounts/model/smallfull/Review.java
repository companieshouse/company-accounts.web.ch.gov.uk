package uk.gov.companieshouse.web.accounts.model.smallfull;

import lombok.Getter;
import lombok.Setter;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.accountingpolicies.BasisOfPreparation;

@Getter
@Setter
public class Review {

    BalanceSheet balanceSheet;

    Statements statements;

    BasisOfPreparation basisOfPreparation;
}
