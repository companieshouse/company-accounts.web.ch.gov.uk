package uk.gov.companieshouse.web.accounts.transformer.profitandloss;

import uk.gov.companieshouse.api.model.accounts.profitandloss.ProfitAndLossApi;
import uk.gov.companieshouse.web.accounts.model.profitandloss.ProfitAndLoss;

public interface ProfitAndLossTransformer {
    ProfitAndLoss getProfitAndLoss(
            ProfitAndLossApi currentPeriodProfitAndLoss,
                    ProfitAndLossApi previousPeriodProfitAndLoss);

    ProfitAndLossApi getCurrentPeriodProfitAndLoss(ProfitAndLoss profitAndLoss);

    ProfitAndLossApi getPreviousPeriodProfitAndLoss(ProfitAndLoss profitAndLoss);
}
