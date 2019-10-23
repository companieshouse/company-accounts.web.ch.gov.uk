package uk.gov.companieshouse.web.accounts.transformer.profitandloss.impl;

import java.util.Objects;
import java.util.stream.Stream;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.api.model.accounts.profitandloss.ProfitAndLossApi;
import uk.gov.companieshouse.web.accounts.model.profitandloss.ProfitAndLoss;
import uk.gov.companieshouse.web.accounts.model.profitandloss.grossprofitorloss.GrossProfitOrLoss;
import uk.gov.companieshouse.web.accounts.model.profitandloss.grossprofitorloss.items.CostOfSales;
import uk.gov.companieshouse.web.accounts.model.profitandloss.grossprofitorloss.items.GrossTotal;
import uk.gov.companieshouse.web.accounts.model.profitandloss.grossprofitorloss.items.Turnover;

@Component
public class GrossProfitAndLossTransformer {

    public void addCurrentPeriodToWebModel(ProfitAndLoss profitAndLoss, ProfitAndLossApi currentPeriodProfitAndLoss) {

        if (currentPeriodProfitAndLoss.getGrossProfitOrLoss() != null) {

            createGrossProfitOrLoss(profitAndLoss);

            if (currentPeriodProfitAndLoss.getGrossProfitOrLoss().getTurnover() != null) {

                Turnover turnover = createTurnover(profitAndLoss);
                turnover.setCurrentAmount(
                        currentPeriodProfitAndLoss.getGrossProfitOrLoss().getTurnover());
            }

            if (currentPeriodProfitAndLoss.getGrossProfitOrLoss().getCostOfSales() != null) {

                CostOfSales costOfSales = createCostOfSales(profitAndLoss);
                costOfSales.setCurrentAmount(
                        currentPeriodProfitAndLoss.getGrossProfitOrLoss().getCostOfSales());
            }

            if (currentPeriodProfitAndLoss.getGrossProfitOrLoss().getGrossTotal() != null) {

                GrossTotal grossTotal = createGrossTotal(profitAndLoss);
                grossTotal.setCurrentAmount(
                        currentPeriodProfitAndLoss.getGrossProfitOrLoss().getGrossTotal());
            }
        }
    }

    public void addPreviousPeriodToWebModel(ProfitAndLoss profitAndLoss, ProfitAndLossApi previousPeriodProfitAndLoss) {

        if (previousPeriodProfitAndLoss.getGrossProfitOrLoss() != null) {

            createGrossProfitOrLoss(profitAndLoss);

            if (previousPeriodProfitAndLoss.getGrossProfitOrLoss().getTurnover() != null) {

                Turnover turnover = createTurnover(profitAndLoss);
                turnover.setCurrentAmount(
                        previousPeriodProfitAndLoss.getGrossProfitOrLoss().getTurnover());
            }

            if (previousPeriodProfitAndLoss.getGrossProfitOrLoss().getCostOfSales() != null) {

                CostOfSales costOfSales = createCostOfSales(profitAndLoss);
                costOfSales.setCurrentAmount(
                        previousPeriodProfitAndLoss.getGrossProfitOrLoss().getCostOfSales());
            }

            if (previousPeriodProfitAndLoss.getGrossProfitOrLoss().getGrossTotal() != null) {

                GrossTotal grossTotal = createGrossTotal(profitAndLoss);
                grossTotal.setCurrentAmount(
                        previousPeriodProfitAndLoss.getGrossProfitOrLoss().getGrossTotal());
            }
        }
    }

    public void addCurrentPeriodToApiModel(ProfitAndLoss profitAndLoss, ProfitAndLossApi currentPeriodProfitAndLoss) {

        if (hasCurrentPeriodGrossProfitOrLoss(profitAndLoss)) {

            uk.gov.companieshouse.api.model.accounts.profitandloss.GrossProfitOrLoss grossProfitOrLoss =
                    new uk.gov.companieshouse.api.model.accounts.profitandloss.GrossProfitOrLoss();

            grossProfitOrLoss.setTurnover(
                    profitAndLoss.getGrossProfitOrLoss().getTurnover().getCurrentAmount());
            grossProfitOrLoss.setCostOfSales(
                    profitAndLoss.getGrossProfitOrLoss().getCostOfSales().getCurrentAmount());
            grossProfitOrLoss.setGrossTotal(
                    profitAndLoss.getGrossProfitOrLoss().getGrossTotal().getCurrentAmount());

            currentPeriodProfitAndLoss.setGrossProfitOrLoss(grossProfitOrLoss);
        }
    }

    public void addPreviousPeriodToApiModel(ProfitAndLoss profitAndLoss, ProfitAndLossApi previousPeriodProfitAndLoss) {

        if (hasPreviousPeriodGrossProfitOrLoss(profitAndLoss)) {

            uk.gov.companieshouse.api.model.accounts.profitandloss.GrossProfitOrLoss grossProfitOrLoss =
                    new uk.gov.companieshouse.api.model.accounts.profitandloss.GrossProfitOrLoss();

            grossProfitOrLoss.setTurnover(
                    profitAndLoss.getGrossProfitOrLoss().getTurnover().getPreviousAmount());
            grossProfitOrLoss.setCostOfSales(
                    profitAndLoss.getGrossProfitOrLoss().getCostOfSales().getPreviousAmount());
            grossProfitOrLoss.setGrossTotal(
                    profitAndLoss.getGrossProfitOrLoss().getGrossTotal().getPreviousAmount());

            previousPeriodProfitAndLoss.setGrossProfitOrLoss(grossProfitOrLoss);
        }
    }

    private void createGrossProfitOrLoss(ProfitAndLoss profitAndLoss) {

        if (profitAndLoss.getGrossProfitOrLoss() == null) {

            profitAndLoss.setGrossProfitOrLoss(new GrossProfitOrLoss());
        }
    }

    private Turnover createTurnover(ProfitAndLoss profitAndLoss) {

        Turnover turnover;

        if (profitAndLoss.getGrossProfitOrLoss().getTurnover() == null) {
            turnover = new Turnover();
            profitAndLoss.getGrossProfitOrLoss().setTurnover(turnover);
        } else {
            turnover = profitAndLoss.getGrossProfitOrLoss().getTurnover();
        }

        return turnover;
    }

    private CostOfSales createCostOfSales(ProfitAndLoss profitAndLoss) {

        CostOfSales costOfSales;

        if (profitAndLoss.getGrossProfitOrLoss().getCostOfSales() == null) {
            costOfSales = new CostOfSales();
            profitAndLoss.getGrossProfitOrLoss().setCostOfSales(costOfSales);
        } else {
            costOfSales = profitAndLoss.getGrossProfitOrLoss().getCostOfSales();
        }

        return costOfSales;
    }

    private GrossTotal createGrossTotal(ProfitAndLoss profitAndLoss) {

        GrossTotal grossTotal;

        if (profitAndLoss.getGrossProfitOrLoss().getGrossTotal() == null) {
            grossTotal = new GrossTotal();
            profitAndLoss.getGrossProfitOrLoss().setGrossTotal(grossTotal);
        } else {
            grossTotal = profitAndLoss.getGrossProfitOrLoss().getGrossTotal();
        }

        return grossTotal;
    }

    private boolean hasCurrentPeriodGrossProfitOrLoss(ProfitAndLoss profitAndLoss) {

        return Stream.of(profitAndLoss.getGrossProfitOrLoss().getTurnover().getCurrentAmount(),
                profitAndLoss.getGrossProfitOrLoss().getCostOfSales().getCurrentAmount(),
                profitAndLoss.getGrossProfitOrLoss().getGrossTotal().getCurrentAmount()).
                anyMatch(Objects::nonNull);
    }

    private boolean hasPreviousPeriodGrossProfitOrLoss(ProfitAndLoss profitAndLoss) {

        return Stream.of(profitAndLoss.getGrossProfitOrLoss().getTurnover().getPreviousAmount(),
                profitAndLoss.getGrossProfitOrLoss().getCostOfSales().getPreviousAmount(),
                profitAndLoss.getGrossProfitOrLoss().getGrossTotal().getPreviousAmount()).
                anyMatch(Objects::nonNull);
    }
}
