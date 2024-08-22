package uk.gov.companieshouse.web.accounts.transformer.profitandloss;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.api.model.accounts.profitandloss.ProfitAndLossApi;
import uk.gov.companieshouse.api.model.accounts.profitandloss.ProfitOrLossForFinancialYear;
import uk.gov.companieshouse.web.accounts.model.profitandloss.ProfitAndLoss;
import uk.gov.companieshouse.web.accounts.model.profitandloss.profitorlossforfinancialyear.items.Tax;
import uk.gov.companieshouse.web.accounts.model.profitandloss.profitorlossforfinancialyear.items.TotalProfitOrLossForFinancialYear;
import uk.gov.companieshouse.web.accounts.transformer.profitandloss.impl.ProfitOrLossForFinancialYearTransformer;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ProfitOrLossForFinancialYearTransformerTests {

    private static final Long CURRENT_TAX = 1L;
    private static final Long CURRENT_TOTAL = 2L;

    private static final Long PREVIOUS_TAX = 10L;
    private static final Long PREVIOUS_TOTAL = 20L;

    private final ProfitOrLossForFinancialYearTransformer transformer = new ProfitOrLossForFinancialYearTransformer();

    @Test
    @DisplayName("Add current period to web model")
    void addCurrentPeriodToWebModel() {

        ProfitAndLossApi currentPeriodProfitAndLossApi = new ProfitAndLossApi();

        ProfitOrLossForFinancialYear profitOrLossForFinancialYear = new ProfitOrLossForFinancialYear();
        profitOrLossForFinancialYear.setTax(CURRENT_TAX);
        profitOrLossForFinancialYear.setTotalProfitOrLossForFinancialYear(CURRENT_TOTAL);

        currentPeriodProfitAndLossApi.setProfitOrLossForFinancialYear(profitOrLossForFinancialYear);

        ProfitAndLoss profitAndLoss = new ProfitAndLoss();

        transformer.addCurrentPeriodToWebModel(profitAndLoss, currentPeriodProfitAndLossApi);

        assertNotNull(profitAndLoss.getProfitOrLossForFinancialYear());

        assertEquals(CURRENT_TAX,
                profitAndLoss.getProfitOrLossForFinancialYear().getTax().getCurrentAmount());
        assertEquals(CURRENT_TOTAL, profitAndLoss.getProfitOrLossForFinancialYear()
                .getTotalProfitOrLossForFinancialYear().getCurrentAmount());
    }

    @Test
    @DisplayName("Add previous period to web model which has current period values")
    void addPreviousPeriodToWebModelWhichHasCurrentPeriodValues() {

        ProfitAndLossApi previousPeriodProfitAndLossApi = new ProfitAndLossApi();

        ProfitOrLossForFinancialYear profitOrLossForFinancialYear = new ProfitOrLossForFinancialYear();
        profitOrLossForFinancialYear.setTax(PREVIOUS_TAX);
        profitOrLossForFinancialYear.setTotalProfitOrLossForFinancialYear(PREVIOUS_TOTAL);

        previousPeriodProfitAndLossApi.setProfitOrLossForFinancialYear(
                profitOrLossForFinancialYear);

        ProfitAndLoss profitAndLoss = new ProfitAndLoss();

        uk.gov.companieshouse.web.accounts.model.profitandloss.profitorlossforfinancialyear.ProfitOrLossForFinancialYear profitOrLossForFinancialYearWeb =
                new uk.gov.companieshouse.web.accounts.model.profitandloss.profitorlossforfinancialyear.ProfitOrLossForFinancialYear();

        Tax turnover = new Tax();
        turnover.setCurrentAmount(CURRENT_TAX);

        TotalProfitOrLossForFinancialYear costOfSales = new TotalProfitOrLossForFinancialYear();
        costOfSales.setCurrentAmount(CURRENT_TOTAL);

        profitOrLossForFinancialYearWeb.setTax(turnover);
        profitOrLossForFinancialYearWeb.setTotalProfitOrLossForFinancialYear(costOfSales);

        profitAndLoss.setProfitOrLossForFinancialYear(profitOrLossForFinancialYearWeb);

        transformer.addPreviousPeriodToWebModel(profitAndLoss, previousPeriodProfitAndLossApi);

        assertEquals(CURRENT_TAX,
                profitAndLoss.getProfitOrLossForFinancialYear().getTax().getCurrentAmount());
        assertEquals(CURRENT_TOTAL, profitAndLoss.getProfitOrLossForFinancialYear()
                .getTotalProfitOrLossForFinancialYear().getCurrentAmount());
        assertEquals(PREVIOUS_TAX,
                profitAndLoss.getProfitOrLossForFinancialYear().getTax().getPreviousAmount());
        assertEquals(PREVIOUS_TOTAL, profitAndLoss.getProfitOrLossForFinancialYear()
                .getTotalProfitOrLossForFinancialYear().getPreviousAmount());
    }

    @Test
    @DisplayName("Add current period to web model - no profit or loss for financial year")
    void addCurrentPeriodToWebModelNoProfitOrLossForFinancialYear() {

        ProfitAndLoss profitAndLoss = new ProfitAndLoss();

        transformer.addCurrentPeriodToWebModel(profitAndLoss, new ProfitAndLossApi());

        assertNull(profitAndLoss.getProfitOrLossForFinancialYear());
    }

    @Test
    @DisplayName("Add previous period to web model - no profit or loss for financial year")
    void addPreviousPeriodToWebModelNoProfitOrLossForFinancialYear() {

        ProfitAndLoss profitAndLoss = new ProfitAndLoss();

        transformer.addPreviousPeriodToWebModel(profitAndLoss, new ProfitAndLossApi());

        assertNull(profitAndLoss.getProfitOrLossForFinancialYear());
    }

    @Test
    @DisplayName("Add current period to api model")
    void addCurrentPeriodToApiModel() {

        ProfitAndLossApi currentPeriodProfitAndLoss = new ProfitAndLossApi();

        ProfitAndLoss profitAndLoss = new ProfitAndLoss();

        uk.gov.companieshouse.web.accounts.model.profitandloss.profitorlossforfinancialyear.ProfitOrLossForFinancialYear profitOrLossForFinancialYearWeb =
                new uk.gov.companieshouse.web.accounts.model.profitandloss.profitorlossforfinancialyear.ProfitOrLossForFinancialYear();

        Tax turnover = new Tax();
        turnover.setCurrentAmount(CURRENT_TAX);

        TotalProfitOrLossForFinancialYear costOfSales = new TotalProfitOrLossForFinancialYear();
        costOfSales.setCurrentAmount(CURRENT_TOTAL);

        profitOrLossForFinancialYearWeb.setTax(turnover);
        profitOrLossForFinancialYearWeb.setTotalProfitOrLossForFinancialYear(costOfSales);

        profitAndLoss.setProfitOrLossForFinancialYear(profitOrLossForFinancialYearWeb);

        transformer.addCurrentPeriodToApiModel(profitAndLoss, currentPeriodProfitAndLoss);

        assertEquals(CURRENT_TAX,
                currentPeriodProfitAndLoss.getProfitOrLossForFinancialYear().getTax());
        assertEquals(CURRENT_TOTAL, currentPeriodProfitAndLoss.getProfitOrLossForFinancialYear()
                .getTotalProfitOrLossForFinancialYear());
    }

    @Test
    @DisplayName("Add previous period to api model")
    void addPreviousPeriodToApiModel() {

        ProfitAndLossApi previousPeriodProfitAndLoss = new ProfitAndLossApi();

        ProfitAndLoss profitAndLoss = new ProfitAndLoss();

        uk.gov.companieshouse.web.accounts.model.profitandloss.profitorlossforfinancialyear.ProfitOrLossForFinancialYear profitOrLossForFinancialYearWeb =
                new uk.gov.companieshouse.web.accounts.model.profitandloss.profitorlossforfinancialyear.ProfitOrLossForFinancialYear();

        Tax turnover = new Tax();
        turnover.setPreviousAmount(PREVIOUS_TAX);

        TotalProfitOrLossForFinancialYear costOfSales = new TotalProfitOrLossForFinancialYear();
        costOfSales.setPreviousAmount(PREVIOUS_TOTAL);

        profitOrLossForFinancialYearWeb.setTax(turnover);
        profitOrLossForFinancialYearWeb.setTotalProfitOrLossForFinancialYear(costOfSales);

        profitAndLoss.setProfitOrLossForFinancialYear(profitOrLossForFinancialYearWeb);

        transformer.addPreviousPeriodToApiModel(profitAndLoss, previousPeriodProfitAndLoss);

        assertEquals(PREVIOUS_TAX,
                previousPeriodProfitAndLoss.getProfitOrLossForFinancialYear().getTax());
        assertEquals(PREVIOUS_TOTAL, previousPeriodProfitAndLoss.getProfitOrLossForFinancialYear()
                .getTotalProfitOrLossForFinancialYear());
    }

    @Test
    @DisplayName("Add current period to api model without profit or loss for financial year to map")
    void addCurrentPeriodToApiModelWithoutProfitOrLossForFinancialYearToMap() {

        ProfitAndLossApi currentPeriodProfitAndLoss = new ProfitAndLossApi();

        ProfitAndLoss profitAndLoss = new ProfitAndLoss();

        uk.gov.companieshouse.web.accounts.model.profitandloss.profitorlossforfinancialyear.ProfitOrLossForFinancialYear profitOrLossForFinancialYearWeb =
                new uk.gov.companieshouse.web.accounts.model.profitandloss.profitorlossforfinancialyear.ProfitOrLossForFinancialYear();

        profitOrLossForFinancialYearWeb.setTax(new Tax());
        profitOrLossForFinancialYearWeb.setTotalProfitOrLossForFinancialYear(
                new TotalProfitOrLossForFinancialYear());

        profitAndLoss.setProfitOrLossForFinancialYear(profitOrLossForFinancialYearWeb);

        transformer.addCurrentPeriodToApiModel(profitAndLoss, currentPeriodProfitAndLoss);

        assertNull(currentPeriodProfitAndLoss.getProfitOrLossForFinancialYear());
    }

    @Test
    @DisplayName("Add previous period to api model without profit or loss for financial year to map")
    void addPreviousPeriodToApiModelWithoutProfitOrLossForFinancialYearToMap() {

        ProfitAndLossApi previousPeriodProfitAndLoss = new ProfitAndLossApi();

        ProfitAndLoss profitAndLoss = new ProfitAndLoss();

        uk.gov.companieshouse.web.accounts.model.profitandloss.profitorlossforfinancialyear.ProfitOrLossForFinancialYear profitOrLossForFinancialYearWeb =
                new uk.gov.companieshouse.web.accounts.model.profitandloss.profitorlossforfinancialyear.ProfitOrLossForFinancialYear();

        profitOrLossForFinancialYearWeb.setTax(new Tax());
        profitOrLossForFinancialYearWeb.setTotalProfitOrLossForFinancialYear(
                new TotalProfitOrLossForFinancialYear());

        profitAndLoss.setProfitOrLossForFinancialYear(profitOrLossForFinancialYearWeb);

        transformer.addPreviousPeriodToApiModel(profitAndLoss, previousPeriodProfitAndLoss);

        assertNull(previousPeriodProfitAndLoss.getProfitOrLossForFinancialYear());
    }
}

