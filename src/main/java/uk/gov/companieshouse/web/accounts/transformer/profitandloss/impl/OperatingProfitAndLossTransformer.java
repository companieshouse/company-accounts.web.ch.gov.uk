package uk.gov.companieshouse.web.accounts.transformer.profitandloss.impl;

import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.api.model.accounts.profitandloss.ProfitAndLossApi;
import uk.gov.companieshouse.web.accounts.model.profitandloss.ProfitAndLoss;
import uk.gov.companieshouse.web.accounts.model.profitandloss.operatingprofitorloss.OperatingProfitOrLoss;
import uk.gov.companieshouse.web.accounts.model.profitandloss.operatingprofitorloss.items.AdministrativeExpenses;
import uk.gov.companieshouse.web.accounts.model.profitandloss.operatingprofitorloss.items.DistributionCosts;
import uk.gov.companieshouse.web.accounts.model.profitandloss.operatingprofitorloss.items.OperatingTotal;
import uk.gov.companieshouse.web.accounts.model.profitandloss.operatingprofitorloss.items.OtherOperatingIncome;

import javax.print.attribute.standard.MediaSize;

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

    }

    public void addCurrentPeriodToApiModel(ProfitAndLoss profitAndLoss, ProfitAndLossApi currentPeriodProfitAndLoss) {

    }

    public void addPreviousPeriodToApiModel(ProfitAndLoss profitAndLoss, ProfitAndLossApi previousPeriodProfitAndLoss) {

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
}
