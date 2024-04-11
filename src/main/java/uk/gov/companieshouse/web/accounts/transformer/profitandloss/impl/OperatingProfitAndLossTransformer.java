package uk.gov.companieshouse.web.accounts.transformer.profitandloss.impl;

import org.springframework.stereotype.Component;
import uk.gov.companieshouse.api.model.accounts.profitandloss.ProfitAndLossApi;
import uk.gov.companieshouse.web.accounts.model.profitandloss.ProfitAndLoss;
import uk.gov.companieshouse.web.accounts.model.profitandloss.operatingprofitorloss.OperatingProfitOrLoss;
import uk.gov.companieshouse.web.accounts.model.profitandloss.operatingprofitorloss.items.AdministrativeExpenses;
import uk.gov.companieshouse.web.accounts.model.profitandloss.operatingprofitorloss.items.DistributionCosts;
import uk.gov.companieshouse.web.accounts.model.profitandloss.operatingprofitorloss.items.OperatingTotal;
import uk.gov.companieshouse.web.accounts.model.profitandloss.operatingprofitorloss.items.OtherOperatingIncome;

import java.util.Objects;
import java.util.stream.Stream;

@Component
public class OperatingProfitAndLossTransformer {
    public void addCurrentPeriodToWebModel(ProfitAndLoss profitAndLoss, ProfitAndLossApi currentPeriodProfitAndLoss) {
        if (currentPeriodProfitAndLoss.getOperatingProfitOrLoss() != null) {
            createOperatingProfitOrLoss(profitAndLoss);

            if (currentPeriodProfitAndLoss.getOperatingProfitOrLoss().getAdministrativeExpenses() != null) {
                AdministrativeExpenses administrativeExpenses = createAdministrativeExpenses(profitAndLoss);
                administrativeExpenses.setCurrentAmount(
                        currentPeriodProfitAndLoss.getOperatingProfitOrLoss().getAdministrativeExpenses());
            }

            if (currentPeriodProfitAndLoss.getOperatingProfitOrLoss().getDistributionCosts() != null) {
                DistributionCosts distributionCosts = createDistributionCosts(profitAndLoss);
                distributionCosts.setCurrentAmount(
                        currentPeriodProfitAndLoss.getOperatingProfitOrLoss().getDistributionCosts());
            }

            if (currentPeriodProfitAndLoss.getOperatingProfitOrLoss().getOperatingTotal() != null) {
                OperatingTotal operatingTotal = createOperatingTotal(profitAndLoss);
                operatingTotal.setCurrentAmount(
                        currentPeriodProfitAndLoss.getOperatingProfitOrLoss().getOperatingTotal());
            }

            if (currentPeriodProfitAndLoss.getOperatingProfitOrLoss().getOtherOperatingIncome() != null) {
                OtherOperatingIncome otherOperatingIncome = createOtherOperatingIncome(profitAndLoss);
                otherOperatingIncome.setCurrentAmount(
                        currentPeriodProfitAndLoss.getOperatingProfitOrLoss().getOtherOperatingIncome());
            }
        }
    }

    public void addPreviousPeriodToWebModel(ProfitAndLoss profitAndLoss, ProfitAndLossApi previousPeriodProfitAndLoss) {
        if (previousPeriodProfitAndLoss.getOperatingProfitOrLoss() != null) {
            createOperatingProfitOrLoss(profitAndLoss);

            if (previousPeriodProfitAndLoss.getOperatingProfitOrLoss().getAdministrativeExpenses() != null) {
                AdministrativeExpenses administrativeExpenses = createAdministrativeExpenses(profitAndLoss);
                administrativeExpenses.setPreviousAmount(
                        previousPeriodProfitAndLoss.getOperatingProfitOrLoss().getAdministrativeExpenses());
            }

            if (previousPeriodProfitAndLoss.getOperatingProfitOrLoss().getDistributionCosts() != null) {
                DistributionCosts distributionCosts = createDistributionCosts(profitAndLoss);
                distributionCosts.setPreviousAmount(
                        previousPeriodProfitAndLoss.getOperatingProfitOrLoss().getDistributionCosts());
            }

            if (previousPeriodProfitAndLoss.getOperatingProfitOrLoss().getOperatingTotal() != null) {
                OperatingTotal operatingTotal = createOperatingTotal(profitAndLoss);
                operatingTotal.setPreviousAmount(
                        previousPeriodProfitAndLoss.getOperatingProfitOrLoss().getOperatingTotal());
            }

            if (previousPeriodProfitAndLoss.getOperatingProfitOrLoss().getOtherOperatingIncome() != null) {
                OtherOperatingIncome otherOperatingIncome = createOtherOperatingIncome(profitAndLoss);
                otherOperatingIncome.setPreviousAmount(
                        previousPeriodProfitAndLoss.getOperatingProfitOrLoss().getOtherOperatingIncome());
            }
        }
    }

    public void addCurrentPeriodToApiModel(ProfitAndLoss profitAndLoss, ProfitAndLossApi currentPeriodProfitAndLoss) {
        if (hasCurrentPeriodOperatingProfitOrLoss(profitAndLoss)) {
            uk.gov.companieshouse.api.model.accounts.profitandloss.OperatingProfitOrLoss operatingProfitOrLoss =
                    new uk.gov.companieshouse.api.model.accounts.profitandloss.OperatingProfitOrLoss();

            operatingProfitOrLoss.setAdministrativeExpenses(profitAndLoss.getOperatingProfitOrLoss().
                    getAdministrativeExpenses().getCurrentAmount());
            operatingProfitOrLoss.setDistributionCosts(profitAndLoss.getOperatingProfitOrLoss().
                    getDistributionCosts().getCurrentAmount());
            operatingProfitOrLoss.setOperatingTotal(profitAndLoss.getOperatingProfitOrLoss().
                    getOperatingTotal().getCurrentAmount());
            operatingProfitOrLoss.setOtherOperatingIncome(profitAndLoss.getOperatingProfitOrLoss().
                    getOtherOperatingIncome().getCurrentAmount());

            currentPeriodProfitAndLoss.setOperatingProfitOrLoss(operatingProfitOrLoss);
        }
    }

    public void addPreviousPeriodToApiModel(ProfitAndLoss profitAndLoss, ProfitAndLossApi previousPeriodProfitAndLoss) {
        if (hasPreviousPeriodOperatingProfitOrLoss(profitAndLoss)) {
            uk.gov.companieshouse.api.model.accounts.profitandloss.OperatingProfitOrLoss operatingProfitOrLoss =
                    new uk.gov.companieshouse.api.model.accounts.profitandloss.OperatingProfitOrLoss();

            operatingProfitOrLoss.setAdministrativeExpenses(profitAndLoss.getOperatingProfitOrLoss().
                    getAdministrativeExpenses().getPreviousAmount());
            operatingProfitOrLoss.setDistributionCosts(profitAndLoss.getOperatingProfitOrLoss().
                    getDistributionCosts().getPreviousAmount());
            operatingProfitOrLoss.setOperatingTotal(profitAndLoss.getOperatingProfitOrLoss().
                    getOperatingTotal().getPreviousAmount());
            operatingProfitOrLoss.setOtherOperatingIncome(profitAndLoss.getOperatingProfitOrLoss().
                    getOtherOperatingIncome().getPreviousAmount());

            previousPeriodProfitAndLoss.setOperatingProfitOrLoss(operatingProfitOrLoss);
        }
    }

    private void createOperatingProfitOrLoss(ProfitAndLoss profitAndLoss) {
        if (profitAndLoss.getOperatingProfitOrLoss() == null) {
            profitAndLoss.setOperatingProfitOrLoss(new OperatingProfitOrLoss());
        }
    }

    private AdministrativeExpenses createAdministrativeExpenses(ProfitAndLoss profitAndLoss) {
        AdministrativeExpenses administrativeExpenses;

        if (profitAndLoss.getOperatingProfitOrLoss().getAdministrativeExpenses() == null) {
            administrativeExpenses = new AdministrativeExpenses();
            profitAndLoss.getOperatingProfitOrLoss().setAdministrativeExpenses(administrativeExpenses);
        } else {
            administrativeExpenses = profitAndLoss.getOperatingProfitOrLoss().getAdministrativeExpenses();
        }
        return administrativeExpenses;
    }

    private DistributionCosts createDistributionCosts(ProfitAndLoss profitAndLoss) {
        DistributionCosts distributionCosts;

        if (profitAndLoss.getOperatingProfitOrLoss().getDistributionCosts() == null) {
            distributionCosts = new DistributionCosts();
            profitAndLoss.getOperatingProfitOrLoss().setDistributionCosts(distributionCosts);
        } else {
            distributionCosts = profitAndLoss.getOperatingProfitOrLoss().getDistributionCosts();
        }
        return distributionCosts;

    }

    private OperatingTotal createOperatingTotal(ProfitAndLoss profitAndLoss) {
        OperatingTotal operatingTotal;

        if (profitAndLoss.getOperatingProfitOrLoss().getOperatingTotal() == null) {
            operatingTotal = new OperatingTotal();
            profitAndLoss.getOperatingProfitOrLoss().setOperatingTotal(operatingTotal);
        } else {
            operatingTotal = profitAndLoss.getOperatingProfitOrLoss().getOperatingTotal();
        }
        return operatingTotal;
    }

    private OtherOperatingIncome createOtherOperatingIncome(ProfitAndLoss profitAndLoss) {
        OtherOperatingIncome otherOperatingIncome;

        if (profitAndLoss.getOperatingProfitOrLoss().getOtherOperatingIncome() == null) {
            otherOperatingIncome = new OtherOperatingIncome();
            profitAndLoss.getOperatingProfitOrLoss().setOtherOperatingIncome(otherOperatingIncome);
        } else {
            otherOperatingIncome = profitAndLoss.getOperatingProfitOrLoss().getOtherOperatingIncome();
        }
        return otherOperatingIncome;
    }

    private boolean hasCurrentPeriodOperatingProfitOrLoss(ProfitAndLoss profitAndLoss) {
        return Stream.of(profitAndLoss.getOperatingProfitOrLoss().getAdministrativeExpenses().getCurrentAmount(),
                profitAndLoss.getOperatingProfitOrLoss().getDistributionCosts().getCurrentAmount(),
                profitAndLoss.getOperatingProfitOrLoss().getOperatingTotal().getCurrentAmount(),
                profitAndLoss.getOperatingProfitOrLoss().getOtherOperatingIncome().getCurrentAmount()).
                anyMatch(Objects::nonNull);
    }

    private boolean hasPreviousPeriodOperatingProfitOrLoss(ProfitAndLoss profitAndLoss) {
        return Stream.of(profitAndLoss.getOperatingProfitOrLoss().getAdministrativeExpenses().getPreviousAmount(),
                profitAndLoss.getOperatingProfitOrLoss().getDistributionCosts().getPreviousAmount(),
                profitAndLoss.getOperatingProfitOrLoss().getOperatingTotal().getPreviousAmount(),
                profitAndLoss.getOperatingProfitOrLoss().getOtherOperatingIncome().getPreviousAmount()).
                anyMatch(Objects::nonNull);
    }
}
