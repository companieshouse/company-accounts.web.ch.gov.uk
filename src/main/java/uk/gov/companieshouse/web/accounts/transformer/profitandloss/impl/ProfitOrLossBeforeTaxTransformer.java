package uk.gov.companieshouse.web.accounts.transformer.profitandloss.impl;

import org.springframework.stereotype.Component;
import uk.gov.companieshouse.api.model.accounts.profitandloss.ProfitAndLossApi;
import uk.gov.companieshouse.web.accounts.model.profitandloss.ProfitAndLoss;
import uk.gov.companieshouse.web.accounts.model.profitandloss.profitorlossbeforetax.ProfitOrLossBeforeTax;
import uk.gov.companieshouse.web.accounts.model.profitandloss.profitorlossbeforetax.items.InterestPayableAndSimilarCharges;
import uk.gov.companieshouse.web.accounts.model.profitandloss.profitorlossbeforetax.items.InterestReceivableAndSimilarIncome;
import uk.gov.companieshouse.web.accounts.model.profitandloss.profitorlossbeforetax.items.TotalProfitOrLossBeforeTax;

import java.util.Objects;
import java.util.stream.Stream;

@Component
public class ProfitOrLossBeforeTaxTransformer {
    public void addCurrentPeriodToWebModel(ProfitAndLoss profitAndLoss, ProfitAndLossApi currentPeriodProfitAndLoss) {
        if (currentPeriodProfitAndLoss.getProfitOrLossBeforeTax() != null) {
            createProfitOrLossBeforeTax(profitAndLoss);

            if (currentPeriodProfitAndLoss.getProfitOrLossBeforeTax().getInterestReceivableAndSimilarIncome() != null) {
                InterestReceivableAndSimilarIncome interestReceivableAndSimilarIncome = createInterestReceivableAndSimilarIncome(profitAndLoss);
                interestReceivableAndSimilarIncome.setCurrentAmount(
                        currentPeriodProfitAndLoss.getProfitOrLossBeforeTax().getInterestReceivableAndSimilarIncome());
            }

            if (currentPeriodProfitAndLoss.getProfitOrLossBeforeTax().getInterestPayableAndSimilarCharges() != null) {
                InterestPayableAndSimilarCharges interestPayableAndSimilarCharges = createInterestPayableAndSimilarCharges(profitAndLoss);
                interestPayableAndSimilarCharges.setCurrentAmount(
                        currentPeriodProfitAndLoss.getProfitOrLossBeforeTax().getInterestPayableAndSimilarCharges());
            }

            if (currentPeriodProfitAndLoss.getProfitOrLossBeforeTax().getTotalProfitOrLossBeforeTax() != null) {
                TotalProfitOrLossBeforeTax totalProfitOrLossBeforeTax = createTotalProfitOrLossBeforeTax(profitAndLoss);
                totalProfitOrLossBeforeTax.setCurrentAmount(
                        currentPeriodProfitAndLoss.getProfitOrLossBeforeTax().getTotalProfitOrLossBeforeTax());
            }
        }
    }

    public void addPreviousPeriodToWebModel(ProfitAndLoss profitAndLoss, ProfitAndLossApi previousPeriodProfitAndLoss) {
        if (previousPeriodProfitAndLoss.getProfitOrLossBeforeTax() != null) {
            createProfitOrLossBeforeTax(profitAndLoss);

            if (previousPeriodProfitAndLoss.getProfitOrLossBeforeTax().getInterestReceivableAndSimilarIncome() != null) {
                InterestReceivableAndSimilarIncome interestReceivableAndSimilarIncome = createInterestReceivableAndSimilarIncome(profitAndLoss);
                interestReceivableAndSimilarIncome.setPreviousAmount(
                        previousPeriodProfitAndLoss.getProfitOrLossBeforeTax().getInterestReceivableAndSimilarIncome());
            }

            if (previousPeriodProfitAndLoss.getProfitOrLossBeforeTax().getInterestPayableAndSimilarCharges() != null) {
                InterestPayableAndSimilarCharges interestPayableAndSimilarCharges = createInterestPayableAndSimilarCharges(profitAndLoss);
                interestPayableAndSimilarCharges.setPreviousAmount(
                        previousPeriodProfitAndLoss.getProfitOrLossBeforeTax().getInterestPayableAndSimilarCharges());
            }

            if (previousPeriodProfitAndLoss.getProfitOrLossBeforeTax().getTotalProfitOrLossBeforeTax() != null) {
                TotalProfitOrLossBeforeTax totalProfitOrLossBeforeTax = createTotalProfitOrLossBeforeTax(profitAndLoss);
                totalProfitOrLossBeforeTax.setPreviousAmount(
                        previousPeriodProfitAndLoss.getProfitOrLossBeforeTax().getTotalProfitOrLossBeforeTax());
            }
        }
    }

    public void addCurrentPeriodToApiModel(ProfitAndLoss profitAndLoss, ProfitAndLossApi currentPeriodProfitAndLoss) {
        if (hasCurrentPeriodProfitOrLossBeforeTax(profitAndLoss)) {
            uk.gov.companieshouse.api.model.accounts.profitandloss.ProfitOrLossBeforeTax profitOrLossBeforeTax =
                    new uk.gov.companieshouse.api.model.accounts.profitandloss.ProfitOrLossBeforeTax();

            profitOrLossBeforeTax.setInterestReceivableAndSimilarIncome(profitAndLoss.getProfitOrLossBeforeTax().
                    getInterestReceivableAndSimilarIncome().getCurrentAmount());
            profitOrLossBeforeTax.setInterestPayableAndSimilarCharges(profitAndLoss.getProfitOrLossBeforeTax().
                    getInterestPayableAndSimilarCharges().getCurrentAmount());
            profitOrLossBeforeTax.setTotalProfitOrLossBeforeTax(profitAndLoss.getProfitOrLossBeforeTax().
                    getTotalProfitOrLossBeforeTax().getCurrentAmount());

            currentPeriodProfitAndLoss.setProfitOrLossBeforeTax(profitOrLossBeforeTax);
        }
    }

    public void addPreviousPeriodToApiModel(ProfitAndLoss profitAndLoss, ProfitAndLossApi previousPeriodProfitAndLoss) {
        if (hasPreviousPeriodProfitOrLossBeforeTax(profitAndLoss)) {
            uk.gov.companieshouse.api.model.accounts.profitandloss.ProfitOrLossBeforeTax profitOrLossBeforeTax =
                    new uk.gov.companieshouse.api.model.accounts.profitandloss.ProfitOrLossBeforeTax();

            profitOrLossBeforeTax.setInterestReceivableAndSimilarIncome(profitAndLoss.getProfitOrLossBeforeTax().
                    getInterestReceivableAndSimilarIncome().getPreviousAmount());
            profitOrLossBeforeTax.setInterestPayableAndSimilarCharges(profitAndLoss.getProfitOrLossBeforeTax().
                    getInterestPayableAndSimilarCharges().getPreviousAmount());
            profitOrLossBeforeTax.setTotalProfitOrLossBeforeTax(profitAndLoss.getProfitOrLossBeforeTax().
                    getTotalProfitOrLossBeforeTax().getPreviousAmount());

            previousPeriodProfitAndLoss.setProfitOrLossBeforeTax(profitOrLossBeforeTax);
        }
    }
    private void createProfitOrLossBeforeTax(ProfitAndLoss profitAndLoss) {
        if (profitAndLoss.getProfitOrLossBeforeTax() == null) {
            profitAndLoss.setProfitOrLossBeforeTax(new ProfitOrLossBeforeTax());
        }
    }

    private InterestReceivableAndSimilarIncome createInterestReceivableAndSimilarIncome(ProfitAndLoss profitAndLoss) {
        InterestReceivableAndSimilarIncome interestReceivableAndSimilarIncome;

        if (profitAndLoss.getProfitOrLossBeforeTax().getInterestReceivableAndSimilarIncome() == null) {
            interestReceivableAndSimilarIncome = new InterestReceivableAndSimilarIncome();
            profitAndLoss.getProfitOrLossBeforeTax().setInterestReceivableAndSimilarIncome(interestReceivableAndSimilarIncome);
        } else {
            interestReceivableAndSimilarIncome = profitAndLoss.getProfitOrLossBeforeTax().getInterestReceivableAndSimilarIncome();
        }
        return interestReceivableAndSimilarIncome;
    }

    private InterestPayableAndSimilarCharges createInterestPayableAndSimilarCharges(ProfitAndLoss profitAndLoss) {
        InterestPayableAndSimilarCharges interestPayableAndSimilarCharges;
        if (profitAndLoss.getProfitOrLossBeforeTax().getInterestPayableAndSimilarCharges() == null) {
            interestPayableAndSimilarCharges = new InterestPayableAndSimilarCharges();
            profitAndLoss.getProfitOrLossBeforeTax().setInterestPayableAndSimilarCharges(interestPayableAndSimilarCharges);
        } else {
            interestPayableAndSimilarCharges = profitAndLoss.getProfitOrLossBeforeTax().getInterestPayableAndSimilarCharges();
        }
        return interestPayableAndSimilarCharges;
    }

    private TotalProfitOrLossBeforeTax createTotalProfitOrLossBeforeTax(ProfitAndLoss profitAndLoss) {
        TotalProfitOrLossBeforeTax totalProfitOrLossBeforeTax;
        if (profitAndLoss.getProfitOrLossBeforeTax().getTotalProfitOrLossBeforeTax() == null) {
            totalProfitOrLossBeforeTax = new TotalProfitOrLossBeforeTax();
            profitAndLoss.getProfitOrLossBeforeTax().setTotalProfitOrLossBeforeTax(totalProfitOrLossBeforeTax);
        } else {
            totalProfitOrLossBeforeTax = profitAndLoss.getProfitOrLossBeforeTax().getTotalProfitOrLossBeforeTax();
        }
        return totalProfitOrLossBeforeTax;
    }

    private boolean hasCurrentPeriodProfitOrLossBeforeTax(ProfitAndLoss profitAndLoss) {
        return Stream.of(profitAndLoss.getProfitOrLossBeforeTax().getInterestReceivableAndSimilarIncome().getCurrentAmount(),
                profitAndLoss.getProfitOrLossBeforeTax().getInterestPayableAndSimilarCharges().getCurrentAmount(),
                profitAndLoss.getProfitOrLossBeforeTax().getTotalProfitOrLossBeforeTax().getCurrentAmount()).
                anyMatch(Objects::nonNull);
    }

    private boolean hasPreviousPeriodProfitOrLossBeforeTax(ProfitAndLoss profitAndLoss) {
        return Stream.of(profitAndLoss.getProfitOrLossBeforeTax().getInterestReceivableAndSimilarIncome().getPreviousAmount(),
                profitAndLoss.getProfitOrLossBeforeTax().getInterestPayableAndSimilarCharges().getPreviousAmount(),
                profitAndLoss.getProfitOrLossBeforeTax().getTotalProfitOrLossBeforeTax().getPreviousAmount()).
                anyMatch(Objects::nonNull);
    }
}
