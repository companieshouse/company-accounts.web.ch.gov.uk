package uk.gov.companieshouse.web.accounts.transformer.profitandloss;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.api.model.accounts.profitandloss.GrossProfitOrLoss;
import uk.gov.companieshouse.api.model.accounts.profitandloss.ProfitAndLossApi;
import uk.gov.companieshouse.web.accounts.model.profitandloss.ProfitAndLoss;
import uk.gov.companieshouse.web.accounts.model.profitandloss.grossprofitorloss.items.CostOfSales;
import uk.gov.companieshouse.web.accounts.model.profitandloss.grossprofitorloss.items.GrossTotal;
import uk.gov.companieshouse.web.accounts.model.profitandloss.grossprofitorloss.items.Turnover;
import uk.gov.companieshouse.web.accounts.transformer.profitandloss.impl.GrossProfitAndLossTransformer;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GrossProfitAndLossTransformerTests {

    private static final Long CURRENT_TURNOVER = 1L;
    private static final Long CURRENT_COST_OF_SALES = 2L;
    private static final Long CURRENT_GROSS_TOTAL = 3L;

    private static final Long PREVIOUS_TURNOVER = 10L;
    private static final Long PREVIOUS_COST_OF_SALES = 20L;
    private static final Long PREVIOUS_GROSS_TOTAL = 30L;

    private final GrossProfitAndLossTransformer transformer = new GrossProfitAndLossTransformer();

    @Test
    @DisplayName("Add current period to web model")
    void addCurrentPeriodToWebModel() {

        ProfitAndLossApi currentPeriodProfitAndLossApi = new ProfitAndLossApi();

        GrossProfitOrLoss grossProfitOrLoss = new GrossProfitOrLoss();
        grossProfitOrLoss.setTurnover(CURRENT_TURNOVER);
        grossProfitOrLoss.setCostOfSales(CURRENT_COST_OF_SALES);
        grossProfitOrLoss.setGrossTotal(CURRENT_GROSS_TOTAL);

        currentPeriodProfitAndLossApi.setGrossProfitOrLoss(grossProfitOrLoss);

        ProfitAndLoss profitAndLoss = new ProfitAndLoss();

        transformer.addCurrentPeriodToWebModel(profitAndLoss, currentPeriodProfitAndLossApi);

        assertNotNull(profitAndLoss.getGrossProfitOrLoss());

        assertEquals(CURRENT_TURNOVER,
                profitAndLoss.getGrossProfitOrLoss().getTurnover().getCurrentAmount());
        assertEquals(CURRENT_COST_OF_SALES,
                profitAndLoss.getGrossProfitOrLoss().getCostOfSales().getCurrentAmount());
        assertEquals(CURRENT_GROSS_TOTAL,
                profitAndLoss.getGrossProfitOrLoss().getGrossTotal().getCurrentAmount());
    }

    @Test
    @DisplayName("Add previous period to web model which has current period values")
    void addPreviousPeriodToWebModelWhichHasCurrentPeriodValues() {

        ProfitAndLossApi previousPeriodProfitAndLossApi = new ProfitAndLossApi();

        GrossProfitOrLoss grossProfitOrLoss = new GrossProfitOrLoss();
        grossProfitOrLoss.setTurnover(PREVIOUS_TURNOVER);
        grossProfitOrLoss.setCostOfSales(PREVIOUS_COST_OF_SALES);
        grossProfitOrLoss.setGrossTotal(PREVIOUS_GROSS_TOTAL);

        previousPeriodProfitAndLossApi.setGrossProfitOrLoss(grossProfitOrLoss);

        ProfitAndLoss profitAndLoss = new ProfitAndLoss();

        uk.gov.companieshouse.web.accounts.model.profitandloss.grossprofitorloss.GrossProfitOrLoss grossProfitOrLossWeb =
                new uk.gov.companieshouse.web.accounts.model.profitandloss.grossprofitorloss.GrossProfitOrLoss();

        Turnover turnover = new Turnover();
        turnover.setCurrentAmount(CURRENT_TURNOVER);

        CostOfSales costOfSales = new CostOfSales();
        costOfSales.setCurrentAmount(CURRENT_COST_OF_SALES);

        GrossTotal grossTotal = new GrossTotal();
        grossTotal.setCurrentAmount(CURRENT_GROSS_TOTAL);

        grossProfitOrLossWeb.setTurnover(turnover);
        grossProfitOrLossWeb.setCostOfSales(costOfSales);
        grossProfitOrLossWeb.setGrossTotal(grossTotal);

        profitAndLoss.setGrossProfitOrLoss(grossProfitOrLossWeb);

        transformer.addPreviousPeriodToWebModel(profitAndLoss, previousPeriodProfitAndLossApi);

        assertEquals(CURRENT_TURNOVER,
                profitAndLoss.getGrossProfitOrLoss().getTurnover().getCurrentAmount());
        assertEquals(CURRENT_COST_OF_SALES,
                profitAndLoss.getGrossProfitOrLoss().getCostOfSales().getCurrentAmount());
        assertEquals(CURRENT_GROSS_TOTAL,
                profitAndLoss.getGrossProfitOrLoss().getGrossTotal().getCurrentAmount());
        assertEquals(PREVIOUS_TURNOVER,
                profitAndLoss.getGrossProfitOrLoss().getTurnover().getPreviousAmount());
        assertEquals(PREVIOUS_COST_OF_SALES,
                profitAndLoss.getGrossProfitOrLoss().getCostOfSales().getPreviousAmount());
        assertEquals(PREVIOUS_GROSS_TOTAL,
                profitAndLoss.getGrossProfitOrLoss().getGrossTotal().getPreviousAmount());
    }

    @Test
    @DisplayName("Add current period to web model - no gross profit or loss")
    void addCurrentPeriodToWebModelNoGrossProfitOrLoss() {

        ProfitAndLoss profitAndLoss = new ProfitAndLoss();

        transformer.addCurrentPeriodToWebModel(profitAndLoss, new ProfitAndLossApi());

        assertNull(profitAndLoss.getGrossProfitOrLoss());
    }

    @Test
    @DisplayName("Add previous period to web model - no gross profit or loss")
    void addPreviousPeriodToWebModelNoGrossProfitOrLoss() {

        ProfitAndLoss profitAndLoss = new ProfitAndLoss();

        transformer.addPreviousPeriodToWebModel(profitAndLoss, new ProfitAndLossApi());

        assertNull(profitAndLoss.getGrossProfitOrLoss());
    }

    @Test
    @DisplayName("Add current period to api model")
    void addCurrentPeriodToApiModel() {

        ProfitAndLossApi currentPeriodProfitAndLoss = new ProfitAndLossApi();

        ProfitAndLoss profitAndLoss = new ProfitAndLoss();

        uk.gov.companieshouse.web.accounts.model.profitandloss.grossprofitorloss.GrossProfitOrLoss grossProfitOrLossWeb =
                new uk.gov.companieshouse.web.accounts.model.profitandloss.grossprofitorloss.GrossProfitOrLoss();

        Turnover turnover = new Turnover();
        turnover.setCurrentAmount(CURRENT_TURNOVER);

        CostOfSales costOfSales = new CostOfSales();
        costOfSales.setCurrentAmount(CURRENT_COST_OF_SALES);

        GrossTotal grossTotal = new GrossTotal();
        grossTotal.setCurrentAmount(CURRENT_GROSS_TOTAL);

        grossProfitOrLossWeb.setTurnover(turnover);
        grossProfitOrLossWeb.setCostOfSales(costOfSales);
        grossProfitOrLossWeb.setGrossTotal(grossTotal);

        profitAndLoss.setGrossProfitOrLoss(grossProfitOrLossWeb);

        transformer.addCurrentPeriodToApiModel(profitAndLoss, currentPeriodProfitAndLoss);

        assertEquals(CURRENT_TURNOVER,
                currentPeriodProfitAndLoss.getGrossProfitOrLoss().getTurnover());
        assertEquals(CURRENT_COST_OF_SALES,
                currentPeriodProfitAndLoss.getGrossProfitOrLoss().getCostOfSales());
        assertEquals(CURRENT_GROSS_TOTAL,
                currentPeriodProfitAndLoss.getGrossProfitOrLoss().getGrossTotal());
    }

    @Test
    @DisplayName("Add previous period to api model")
    void addPreviousPeriodToApiModel() {

        ProfitAndLossApi previousPeriodProfitAndLoss = new ProfitAndLossApi();

        ProfitAndLoss profitAndLoss = new ProfitAndLoss();

        uk.gov.companieshouse.web.accounts.model.profitandloss.grossprofitorloss.GrossProfitOrLoss grossProfitOrLossWeb =
                new uk.gov.companieshouse.web.accounts.model.profitandloss.grossprofitorloss.GrossProfitOrLoss();

        Turnover turnover = new Turnover();
        turnover.setPreviousAmount(PREVIOUS_TURNOVER);

        CostOfSales costOfSales = new CostOfSales();
        costOfSales.setPreviousAmount(PREVIOUS_COST_OF_SALES);

        GrossTotal grossTotal = new GrossTotal();
        grossTotal.setPreviousAmount(PREVIOUS_GROSS_TOTAL);

        grossProfitOrLossWeb.setTurnover(turnover);
        grossProfitOrLossWeb.setCostOfSales(costOfSales);
        grossProfitOrLossWeb.setGrossTotal(grossTotal);

        profitAndLoss.setGrossProfitOrLoss(grossProfitOrLossWeb);

        transformer.addPreviousPeriodToApiModel(profitAndLoss, previousPeriodProfitAndLoss);

        assertEquals(PREVIOUS_TURNOVER,
                previousPeriodProfitAndLoss.getGrossProfitOrLoss().getTurnover());
        assertEquals(PREVIOUS_COST_OF_SALES,
                previousPeriodProfitAndLoss.getGrossProfitOrLoss().getCostOfSales());
        assertEquals(PREVIOUS_GROSS_TOTAL,
                previousPeriodProfitAndLoss.getGrossProfitOrLoss().getGrossTotal());
    }

    @Test
    @DisplayName("Add current period to api model without gross profit and loss to map")
    void addCurrentPeriodToApiModelWithoutGrossProfitAndLossToMap() {

        ProfitAndLossApi currentPeriodProfitAndLoss = new ProfitAndLossApi();

        ProfitAndLoss profitAndLoss = new ProfitAndLoss();

        uk.gov.companieshouse.web.accounts.model.profitandloss.grossprofitorloss.GrossProfitOrLoss grossProfitOrLossWeb =
                new uk.gov.companieshouse.web.accounts.model.profitandloss.grossprofitorloss.GrossProfitOrLoss();

        grossProfitOrLossWeb.setTurnover(new Turnover());
        grossProfitOrLossWeb.setCostOfSales(new CostOfSales());
        grossProfitOrLossWeb.setGrossTotal(new GrossTotal());

        profitAndLoss.setGrossProfitOrLoss(grossProfitOrLossWeb);

        transformer.addCurrentPeriodToApiModel(profitAndLoss, currentPeriodProfitAndLoss);

        assertNull(currentPeriodProfitAndLoss.getGrossProfitOrLoss());
    }

    @Test
    @DisplayName("Add previous period to api model without gross profit and loss to map")
    void addPreviousPeriodToApiModelWithoutGrossProfitAndLossToMap() {

        ProfitAndLossApi previousPeriodProfitAndLoss = new ProfitAndLossApi();

        ProfitAndLoss profitAndLoss = new ProfitAndLoss();

        uk.gov.companieshouse.web.accounts.model.profitandloss.grossprofitorloss.GrossProfitOrLoss grossProfitOrLossWeb =
                new uk.gov.companieshouse.web.accounts.model.profitandloss.grossprofitorloss.GrossProfitOrLoss();

        grossProfitOrLossWeb.setTurnover(new Turnover());
        grossProfitOrLossWeb.setCostOfSales(new CostOfSales());
        grossProfitOrLossWeb.setGrossTotal(new GrossTotal());

        profitAndLoss.setGrossProfitOrLoss(grossProfitOrLossWeb);

        transformer.addPreviousPeriodToApiModel(profitAndLoss, previousPeriodProfitAndLoss);

        assertNull(previousPeriodProfitAndLoss.getGrossProfitOrLoss());
    }
}

