package uk.gov.companieshouse.web.accounts.transformer.profitandloss;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.api.model.accounts.profitandloss.OperatingProfitOrLoss;
import uk.gov.companieshouse.api.model.accounts.profitandloss.ProfitAndLossApi;
import uk.gov.companieshouse.web.accounts.model.profitandloss.ProfitAndLoss;
import uk.gov.companieshouse.web.accounts.model.profitandloss.operatingprofitorloss.items.AdministrativeExpenses;
import uk.gov.companieshouse.web.accounts.model.profitandloss.operatingprofitorloss.items.DistributionCosts;
import uk.gov.companieshouse.web.accounts.model.profitandloss.operatingprofitorloss.items.OperatingTotal;
import uk.gov.companieshouse.web.accounts.model.profitandloss.operatingprofitorloss.items.OtherOperatingIncome;
import uk.gov.companieshouse.web.accounts.transformer.profitandloss.impl.OperatingProfitAndLossTransformer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class OperatingProfitAndLossTransformerTests {
    private static final Long CURRENT_ADMINISTRATIVE_EXPENSES = 1L;
    private static final Long CURRENT_DISTRIBUTION_COSTS = 1L;
    private static final Long CURRENT_OTHER_OPERATING_INCOME = 2L;
    private static final Long CURRENT_OPERATING_TOTAL = 2L;

    private static final Long PREVIOUS_ADMINISTRATIVE_EXPENSES = 10L;
    private static final Long PREVIOUS_DISTRIBUTION_COSTS = 10L;
    private static final Long PREVIOUS_OTHER_OPERATING_INCOME = 20L;
    private static final Long PREVIOUS_OPERATING_TOTAL = 50L;

    private final OperatingProfitAndLossTransformer transformer = new OperatingProfitAndLossTransformer();

    @Test
    @DisplayName("Add current period to web model")
    void addCurrentPeriodToWebModel() {
        ProfitAndLossApi currentPeriodProfitAndLossApi = new ProfitAndLossApi();

        OperatingProfitOrLoss operatingProfitOrLoss = new OperatingProfitOrLoss();

        operatingProfitOrLoss.setDistributionCosts(CURRENT_DISTRIBUTION_COSTS);
        operatingProfitOrLoss.setAdministrativeExpenses(CURRENT_ADMINISTRATIVE_EXPENSES);
        operatingProfitOrLoss.setOtherOperatingIncome(CURRENT_OTHER_OPERATING_INCOME);
        operatingProfitOrLoss.setOperatingTotal(CURRENT_OPERATING_TOTAL);

        currentPeriodProfitAndLossApi.setOperatingProfitOrLoss(operatingProfitOrLoss);

        ProfitAndLoss profitAndLoss = new ProfitAndLoss();

        transformer.addCurrentPeriodToWebModel(profitAndLoss, currentPeriodProfitAndLossApi);

        assertNotNull(profitAndLoss.getOperatingProfitOrLoss());

        assertEquals(CURRENT_ADMINISTRATIVE_EXPENSES, profitAndLoss.getOperatingProfitOrLoss().
                getAdministrativeExpenses().getCurrentAmount());
        assertEquals(CURRENT_DISTRIBUTION_COSTS, profitAndLoss.getOperatingProfitOrLoss().
                getDistributionCosts().getCurrentAmount());
        assertEquals(CURRENT_OTHER_OPERATING_INCOME, profitAndLoss.getOperatingProfitOrLoss().
                getOtherOperatingIncome().getCurrentAmount());
        assertEquals(CURRENT_OPERATING_TOTAL, profitAndLoss.getOperatingProfitOrLoss().
                getOperatingTotal().getCurrentAmount());

    }

    @Test
    @DisplayName("Add previous period to web models which has current period values")
    void addPreviousPeriodToWebModelWhichHasCurrentPeriodValues() {
        ProfitAndLossApi previousPeriodProfitAndLossApi = new ProfitAndLossApi();

        OperatingProfitOrLoss operatingProfitOrLoss = new OperatingProfitOrLoss();
        operatingProfitOrLoss.setAdministrativeExpenses(PREVIOUS_ADMINISTRATIVE_EXPENSES);
        operatingProfitOrLoss.setDistributionCosts(PREVIOUS_DISTRIBUTION_COSTS);
        operatingProfitOrLoss.setOtherOperatingIncome(PREVIOUS_OTHER_OPERATING_INCOME);
        operatingProfitOrLoss.setOperatingTotal(PREVIOUS_OPERATING_TOTAL);

        previousPeriodProfitAndLossApi.setOperatingProfitOrLoss(operatingProfitOrLoss);

        ProfitAndLoss profitAndLoss = new ProfitAndLoss();

        uk.gov.companieshouse.web.accounts.model.profitandloss.operatingprofitorloss.OperatingProfitOrLoss
                operatingProfitOrLossWeb =
                new uk.gov.companieshouse.web.accounts.model.profitandloss.operatingprofitorloss.OperatingProfitOrLoss();

        AdministrativeExpenses administrativeExpenses = new AdministrativeExpenses();
        administrativeExpenses.setCurrentAmount(CURRENT_ADMINISTRATIVE_EXPENSES);

        DistributionCosts distributionCosts = new DistributionCosts();
        distributionCosts.setCurrentAmount(CURRENT_DISTRIBUTION_COSTS);

        OtherOperatingIncome otherOperatingIncome = new OtherOperatingIncome();
        otherOperatingIncome.setCurrentAmount(CURRENT_OTHER_OPERATING_INCOME);

        OperatingTotal operatingTotal = new OperatingTotal();
        operatingTotal.setCurrentAmount(CURRENT_OPERATING_TOTAL);

        operatingProfitOrLossWeb.setAdministrativeExpenses(administrativeExpenses);
        operatingProfitOrLossWeb.setDistributionCosts(distributionCosts);
        operatingProfitOrLossWeb.setOtherOperatingIncome(otherOperatingIncome);
        operatingProfitOrLossWeb.setOperatingTotal(operatingTotal);

        profitAndLoss.setOperatingProfitOrLoss(operatingProfitOrLossWeb);

        transformer.addPreviousPeriodToWebModel(profitAndLoss, previousPeriodProfitAndLossApi);

        assertEquals(CURRENT_ADMINISTRATIVE_EXPENSES, profitAndLoss.getOperatingProfitOrLoss().
                getAdministrativeExpenses().getCurrentAmount());
        assertEquals(CURRENT_DISTRIBUTION_COSTS, profitAndLoss.getOperatingProfitOrLoss().
                getDistributionCosts().getCurrentAmount());
        assertEquals(CURRENT_OTHER_OPERATING_INCOME, profitAndLoss.getOperatingProfitOrLoss().
                getOtherOperatingIncome().getCurrentAmount());
        assertEquals(CURRENT_OPERATING_TOTAL, profitAndLoss.getOperatingProfitOrLoss().
                getOtherOperatingIncome().getCurrentAmount());

    }

    @Test
    @DisplayName("Add current period to web model - no operating profit or loss")
    void addCurrentPeriodToWebModelNoOperatingProfitOrLoss() {
        ProfitAndLoss profitAndLoss = new ProfitAndLoss();

        transformer.addCurrentPeriodToWebModel(profitAndLoss, new ProfitAndLossApi());

        assertNull(profitAndLoss.getOperatingProfitOrLoss());
    }

    @Test
    @DisplayName("Add previous period to web model - no operating profit or loss")
    void addPreviousPeriodToWebModelNoOperatingProfitOrLoss() {
        ProfitAndLoss profitAndLoss = new ProfitAndLoss();

        transformer.addPreviousPeriodToWebModel(profitAndLoss, new ProfitAndLossApi());

        assertNull(profitAndLoss.getOperatingProfitOrLoss());
    }

    @Test
    @DisplayName("Add current period to api model")
    void addCurrentPeriodToApiModel() {
        ProfitAndLossApi currentPeriodProfitAndLoss = new ProfitAndLossApi();

        ProfitAndLoss profitAndLoss = new ProfitAndLoss();

        uk.gov.companieshouse.web.accounts.model.profitandloss.operatingprofitorloss.OperatingProfitOrLoss
                operatingProfitOrLoss =
                new uk.gov.companieshouse.web.accounts.model.profitandloss.operatingprofitorloss.OperatingProfitOrLoss();

        AdministrativeExpenses administrativeExpenses = new AdministrativeExpenses();
        administrativeExpenses.setCurrentAmount(CURRENT_ADMINISTRATIVE_EXPENSES);

        DistributionCosts distributionCosts = new DistributionCosts();
        distributionCosts.setCurrentAmount(CURRENT_DISTRIBUTION_COSTS);

        OtherOperatingIncome otherOperatingIncome = new OtherOperatingIncome();
        otherOperatingIncome.setCurrentAmount(CURRENT_OTHER_OPERATING_INCOME);

        OperatingTotal operatingTotal = new OperatingTotal();
        operatingTotal.setCurrentAmount(CURRENT_OPERATING_TOTAL);

        operatingProfitOrLoss.setAdministrativeExpenses(administrativeExpenses);
        operatingProfitOrLoss.setDistributionCosts(distributionCosts);
        operatingProfitOrLoss.setOtherOperatingIncome(otherOperatingIncome);
        operatingProfitOrLoss.setOperatingTotal(operatingTotal);

        profitAndLoss.setOperatingProfitOrLoss(operatingProfitOrLoss);

        transformer.addCurrentPeriodToApiModel(profitAndLoss, currentPeriodProfitAndLoss);

        assertEquals(CURRENT_ADMINISTRATIVE_EXPENSES, currentPeriodProfitAndLoss.getOperatingProfitOrLoss().
                getAdministrativeExpenses());
        assertEquals(CURRENT_DISTRIBUTION_COSTS, currentPeriodProfitAndLoss.getOperatingProfitOrLoss().
                getDistributionCosts());
        assertEquals(CURRENT_OTHER_OPERATING_INCOME, currentPeriodProfitAndLoss.getOperatingProfitOrLoss().
                getOtherOperatingIncome());
        assertEquals(CURRENT_OPERATING_TOTAL, currentPeriodProfitAndLoss.getOperatingProfitOrLoss().
                getOperatingTotal());
    }

    @Test
    @DisplayName("Add previous period to api model")
    void addPreviousPeriodToApiModel() {
        ProfitAndLossApi previousPeriodProfitAndLoss = new ProfitAndLossApi();

        ProfitAndLoss profitAndLoss = new ProfitAndLoss();

        uk.gov.companieshouse.web.accounts.model.profitandloss.operatingprofitorloss.OperatingProfitOrLoss
                operatingProfitOrLoss =
                new uk.gov.companieshouse.web.accounts.model.profitandloss.operatingprofitorloss.OperatingProfitOrLoss();

        AdministrativeExpenses administrativeExpenses = new AdministrativeExpenses();
        administrativeExpenses.setPreviousAmount(PREVIOUS_ADMINISTRATIVE_EXPENSES);

        DistributionCosts distributionCosts = new DistributionCosts();
        distributionCosts.setPreviousAmount(PREVIOUS_DISTRIBUTION_COSTS);

        OtherOperatingIncome otherOperatingIncome = new OtherOperatingIncome();
        otherOperatingIncome.setPreviousAmount(PREVIOUS_OTHER_OPERATING_INCOME);

        OperatingTotal operatingTotal = new OperatingTotal();
        operatingTotal.setPreviousAmount(PREVIOUS_OPERATING_TOTAL);

        operatingProfitOrLoss.setAdministrativeExpenses(administrativeExpenses);
        operatingProfitOrLoss.setDistributionCosts(distributionCosts);
        operatingProfitOrLoss.setOtherOperatingIncome(otherOperatingIncome);
        operatingProfitOrLoss.setOperatingTotal(operatingTotal);

        profitAndLoss.setOperatingProfitOrLoss(operatingProfitOrLoss);

        transformer.addPreviousPeriodToApiModel(profitAndLoss, previousPeriodProfitAndLoss);

        assertEquals(PREVIOUS_ADMINISTRATIVE_EXPENSES, previousPeriodProfitAndLoss.getOperatingProfitOrLoss().
                getAdministrativeExpenses());
        assertEquals(PREVIOUS_DISTRIBUTION_COSTS, previousPeriodProfitAndLoss.getOperatingProfitOrLoss().
                getDistributionCosts());
        assertEquals(PREVIOUS_OTHER_OPERATING_INCOME, previousPeriodProfitAndLoss.getOperatingProfitOrLoss().
                getOtherOperatingIncome());
        assertEquals(PREVIOUS_OPERATING_TOTAL, previousPeriodProfitAndLoss.getOperatingProfitOrLoss().
                getOperatingTotal());
    }

    @Test
    @DisplayName("Add current period to api model without operating profit and loss to map")
    void addCurrentPeriodToApiModelWithoutOperatingProfitAndLossToMap() {
        ProfitAndLossApi currentPeriodProfitAndLoss = new ProfitAndLossApi();

        ProfitAndLoss profitAndLoss = new ProfitAndLoss();

        uk.gov.companieshouse.web.accounts.model.profitandloss.operatingprofitorloss.OperatingProfitOrLoss
                operatingProfitOrLossWeb =
                new uk.gov.companieshouse.web.accounts.model.profitandloss.operatingprofitorloss.OperatingProfitOrLoss();

        operatingProfitOrLossWeb.setAdministrativeExpenses(new AdministrativeExpenses());
        operatingProfitOrLossWeb.setDistributionCosts(new DistributionCosts());
        operatingProfitOrLossWeb.setOtherOperatingIncome(new OtherOperatingIncome());
        operatingProfitOrLossWeb.setOperatingTotal(new OperatingTotal());

        profitAndLoss.setOperatingProfitOrLoss(operatingProfitOrLossWeb);

        transformer.addPreviousPeriodToApiModel(profitAndLoss, currentPeriodProfitAndLoss);

        assertNull(currentPeriodProfitAndLoss.getOperatingProfitOrLoss());
    }

    @Test
    @DisplayName("Add previous period to api model without operating profit and loss to map")
    void addPreviousPeriodToApiModelWithoutOperatingProfitAndLossToMap() {
        ProfitAndLossApi previousPeriodProfitAndLoss = new ProfitAndLossApi();

        ProfitAndLoss profitAndLoss = new ProfitAndLoss();

        uk.gov.companieshouse.web.accounts.model.profitandloss.operatingprofitorloss.OperatingProfitOrLoss
                operatingProfitOrLossWeb =
                new uk.gov.companieshouse.web.accounts.model.profitandloss.operatingprofitorloss.OperatingProfitOrLoss();

        operatingProfitOrLossWeb.setAdministrativeExpenses(new AdministrativeExpenses());
        operatingProfitOrLossWeb.setDistributionCosts(new DistributionCosts());
        operatingProfitOrLossWeb.setOtherOperatingIncome(new OtherOperatingIncome());
        operatingProfitOrLossWeb.setOperatingTotal(new OperatingTotal());

        profitAndLoss.setOperatingProfitOrLoss(operatingProfitOrLossWeb);

        transformer.addPreviousPeriodToApiModel(profitAndLoss, previousPeriodProfitAndLoss);

        assertNull(previousPeriodProfitAndLoss.getOperatingProfitOrLoss());
    }

}
