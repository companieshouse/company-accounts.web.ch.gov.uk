package uk.gov.companieshouse.web.accounts.model.profitandloss.profitorlossbeforetax;

import uk.gov.companieshouse.web.accounts.model.profitandloss.profitorlossbeforetax.items.InterestPayableAndSimilarCharges;
import uk.gov.companieshouse.web.accounts.model.profitandloss.profitorlossbeforetax.items.InterestReceivableAndSimilarIncome;
import uk.gov.companieshouse.web.accounts.model.profitandloss.profitorlossbeforetax.items.TotalProfitOrLossBeforeTax;

public class ProfitOrLossBeforeTax {
    private InterestPayableAndSimilarCharges interestPayableAndSimilarCharges;

    private InterestReceivableAndSimilarIncome interestReceivableAndSimilarIncome;

    private TotalProfitOrLossBeforeTax totalProfitOrLossBeforeTax;

    public InterestPayableAndSimilarCharges getInterestPayableAndSimilarCharges() {
        return interestPayableAndSimilarCharges;
    }

    public void setInterestPayableAndSimilarCharges(InterestPayableAndSimilarCharges interestPayableAndSimilarCharges) {
        this.interestPayableAndSimilarCharges = interestPayableAndSimilarCharges;
    }

    public InterestReceivableAndSimilarIncome getInterestReceivableAndSimilarIncome() {
        return interestReceivableAndSimilarIncome;
    }

    public void setInterestReceivableAndSimilarIncome(InterestReceivableAndSimilarIncome interestReceivableAndSimilarIncome) {
        this.interestReceivableAndSimilarIncome = interestReceivableAndSimilarIncome;
    }

    public TotalProfitOrLossBeforeTax getTotalProfitOrLossBeforeTax() {
        return totalProfitOrLossBeforeTax;
    }

    public void setTotalProfitOrLossBeforeTax(TotalProfitOrLossBeforeTax totalProfitOrLossBeforeTax) {
        this.totalProfitOrLossBeforeTax = totalProfitOrLossBeforeTax;
    }
}
