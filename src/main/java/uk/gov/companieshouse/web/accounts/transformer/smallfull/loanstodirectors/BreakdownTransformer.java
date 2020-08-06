package uk.gov.companieshouse.web.accounts.transformer.smallfull.loanstodirectors;

import org.springframework.stereotype.Component;
import uk.gov.companieshouse.api.model.accounts.smallfull.loanstodirectors.LoanApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.loanstodirectors.LoanBreakdownApi;
import uk.gov.companieshouse.web.accounts.model.loanstodirectors.Breakdown;
import uk.gov.companieshouse.web.accounts.model.loanstodirectors.LoanToAdd;

@Component
public class BreakdownTransformer {

    public LoanBreakdownApi mapLoanBreakdownToApi(LoanToAdd loanToAdd) {

        LoanBreakdownApi loanBreakdownApi = new LoanBreakdownApi();

        loanBreakdownApi.setAdvancesCreditsMade(loanToAdd.getBreakdown().getAdvancesCreditsMade());
        loanBreakdownApi.setAdvancesCreditsRepaid(loanToAdd.getBreakdown().getAdvancesCreditsRepaid());
        loanBreakdownApi.setBalanceAtPeriodStart(loanToAdd.getBreakdown().getBalanceAtPeriodStart());
        loanBreakdownApi.setBalanceAtPeriodEnd(loanToAdd.getBreakdown().getBalanceAtPeriodEnd());

        return loanBreakdownApi;
    }

    public Breakdown mapLoanBreakdownToWeb(LoanApi loanApi) {

        Breakdown breakdown = new Breakdown();

        breakdown.setAdvancesCreditsMade(loanApi.getBreakdown().getAdvancesCreditsMade());
        breakdown.setAdvancesCreditsRepaid(loanApi.getBreakdown().getAdvancesCreditsRepaid());
        breakdown.setBalanceAtPeriodStart(loanApi.getBreakdown().getBalanceAtPeriodStart());
        breakdown.setBalanceAtPeriodEnd(loanApi.getBreakdown().getBalanceAtPeriodEnd());

        return breakdown;
    }
}
