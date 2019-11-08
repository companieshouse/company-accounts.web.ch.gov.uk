package uk.gov.companieshouse.web.accounts.transformer.profitandloss.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.api.model.accounts.profitandloss.ProfitAndLossApi;
import uk.gov.companieshouse.web.accounts.model.profitandloss.ProfitAndLoss;
import uk.gov.companieshouse.web.accounts.transformer.profitandloss.ProfitAndLossTransformer;

@Component
public class ProfitAndLossTransformerImpl implements ProfitAndLossTransformer {

    @Autowired
    private GrossProfitAndLossTransformer grossProfitAndLossTransformer;

    @Autowired
    private OperatingProfitAndLossTransformer operatingProfitAndLossTransformer;

    @Autowired
    private ProfitOrLossBeforeTaxTransformer profitOrLossBeforeTaxTransformer;

    @Override
    public ProfitAndLoss getProfitAndLoss(ProfitAndLossApi currentPeriodProfitAndLoss,
            ProfitAndLossApi previousPeriodProfitAndLoss) {

        ProfitAndLoss profitAndLoss = new ProfitAndLoss();

        if (currentPeriodProfitAndLoss != null) {
            grossProfitAndLossTransformer
                    .addCurrentPeriodToWebModel(profitAndLoss, currentPeriodProfitAndLoss);
            operatingProfitAndLossTransformer
                    .addCurrentPeriodToWebModel(profitAndLoss, currentPeriodProfitAndLoss);
            profitOrLossBeforeTaxTransformer
                    .addCurrentPeriodToWebModel(profitAndLoss, currentPeriodProfitAndLoss);
        }

        if (previousPeriodProfitAndLoss != null) {
            grossProfitAndLossTransformer
                    .addPreviousPeriodToWebModel(profitAndLoss, previousPeriodProfitAndLoss);
            operatingProfitAndLossTransformer
                    .addPreviousPeriodToWebModel(profitAndLoss, previousPeriodProfitAndLoss);
            profitOrLossBeforeTaxTransformer
                    .addPreviousPeriodToWebModel(profitAndLoss, previousPeriodProfitAndLoss);
        }

        return profitAndLoss;
    }

    @Override
    public ProfitAndLossApi getCurrentPeriodProfitAndLoss(ProfitAndLoss profitAndLoss) {

        ProfitAndLossApi currentPeriodProfitAndLoss = new ProfitAndLossApi();

        grossProfitAndLossTransformer.addCurrentPeriodToApiModel(profitAndLoss, currentPeriodProfitAndLoss);
        operatingProfitAndLossTransformer.addCurrentPeriodToApiModel(profitAndLoss, currentPeriodProfitAndLoss);
        profitOrLossBeforeTaxTransformer.addCurrentPeriodToApiModel(profitAndLoss, currentPeriodProfitAndLoss);

        return currentPeriodProfitAndLoss;
    }

    @Override
    public ProfitAndLossApi getPreviousPeriodProfitAndLoss(ProfitAndLoss profitAndLoss) {

        ProfitAndLossApi previousPeriodProfitAndLoss = new ProfitAndLossApi();

        grossProfitAndLossTransformer.addPreviousPeriodToApiModel(profitAndLoss, previousPeriodProfitAndLoss);
        operatingProfitAndLossTransformer.addPreviousPeriodToApiModel(profitAndLoss, previousPeriodProfitAndLoss);
        profitOrLossBeforeTaxTransformer.addPreviousPeriodToApiModel(profitAndLoss, previousPeriodProfitAndLoss);

        return previousPeriodProfitAndLoss;
    }
}
