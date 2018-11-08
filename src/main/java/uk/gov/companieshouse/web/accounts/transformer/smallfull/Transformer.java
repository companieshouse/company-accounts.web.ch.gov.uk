package uk.gov.companieshouse.web.accounts.transformer.smallfull;

import uk.gov.companieshouse.api.model.accounts.smallfull.BalanceSheetApi;
import uk.gov.companieshouse.web.accounts.model.smallfull.BalanceSheet;

public interface Transformer {

    /**
     * Populate the API model with values from the web model for the current
     * period.
     *
     * @param balanceSheetApi  the API model
     * @param balanceSheet the web model
     */
    void addCurrentPeriodToApiModel(BalanceSheetApi balanceSheetApi, BalanceSheet balanceSheet);

    /**
     * Populate the API model with values from the web model for the previous
     * period.
     *
     * @param balanceSheetApi  the API model
     * @param balanceSheet the web model
     */
    void addPreviousPeriodToApiModel(BalanceSheetApi balanceSheetApi, BalanceSheet balanceSheet);

    /**
     * Populate the web model with values from the API model for the current
     * period.
     *
     * @param balanceSheet the web model
     * @param balanceSheetApi  the API model
     */
    void addCurrentPeriodToWebModel(BalanceSheet balanceSheet, BalanceSheetApi balanceSheetApi);

    /**
     * Populate the web model with values from the API model for the previous
     * period.
     *
     * @param balanceSheet the web model
     * @param balanceSheetApi  the API model
     */
    void addPreviousPeriodToWebModel(BalanceSheet balanceSheet, BalanceSheetApi balanceSheetApi);
}
