package uk.gov.companieshouse.web.accounts.transformer.profitandloss;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.api.model.accounts.profitandloss.ProfitAndLossApi;
import uk.gov.companieshouse.api.model.accounts.profitandloss.ProfitOrLossBeforeTax;
import uk.gov.companieshouse.web.accounts.model.profitandloss.ProfitAndLoss;
import uk.gov.companieshouse.web.accounts.model.profitandloss.profitorlossbeforetax.items.InterestPayableAndSimilarCharges;
import uk.gov.companieshouse.web.accounts.model.profitandloss.profitorlossbeforetax.items.InterestReceivableAndSimilarIncome;
import uk.gov.companieshouse.web.accounts.model.profitandloss.profitorlossbeforetax.items.TotalProfitOrLossBeforeTax;
import uk.gov.companieshouse.web.accounts.transformer.profitandloss.impl.ProfitOrLossBeforeTaxTransformer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ProfitOrLossBeforeTaxTransformerTests {

    private static final Long CURRENT_INTEREST_PAYABLE_AND_SIMILAR_CHARGES = 1L;
    private static final Long CURRENT_INTEREST_RECEIVABLE_AND_SIMILAR_INCOME = 1L;
    private static final Long CURRENT_TOTAL_PROFIT_OR_LOSS_BEFORE_TAX = 2L;

    private static final Long PREVIOUS_INTEREST_PAYABLE_AND_SIMILAR_CHARGES = 1L;
    private static final Long PREVIOUS_INTEREST_RECEIVABLE_AND_SIMILAR_INCOME = 1L;
    private static final Long PREVIOUS_TOTAL_PROFIT_OR_LOSS_BEFORE_TAX = 2L;


    private final ProfitOrLossBeforeTaxTransformer transformer = new ProfitOrLossBeforeTaxTransformer();

    @Test
    @DisplayName("Add current period to web model")
    void addCurrentPeriodToWebModel() {

        ProfitAndLossApi currentPeriodProfitAndLossApi = new ProfitAndLossApi();

        ProfitOrLossBeforeTax profitOrLossBeforeTax = new ProfitOrLossBeforeTax();

        profitOrLossBeforeTax.setInterestPayableAndSimilarCharges(CURRENT_INTEREST_PAYABLE_AND_SIMILAR_CHARGES);
        profitOrLossBeforeTax.setInterestReceivableAndSimilarIncome(CURRENT_INTEREST_RECEIVABLE_AND_SIMILAR_INCOME);
        profitOrLossBeforeTax.setTotalProfitOrLossBeforeTax(CURRENT_TOTAL_PROFIT_OR_LOSS_BEFORE_TAX);


        currentPeriodProfitAndLossApi.setProfitOrLossBeforeTax(profitOrLossBeforeTax);

        ProfitAndLoss profitAndLoss = new ProfitAndLoss();

        transformer.addCurrentPeriodToWebModel(profitAndLoss, currentPeriodProfitAndLossApi);

        assertNotNull(profitAndLoss.getProfitOrLossBeforeTax());

        assertEquals(CURRENT_INTEREST_PAYABLE_AND_SIMILAR_CHARGES, profitAndLoss.getProfitOrLossBeforeTax().
                getInterestPayableAndSimilarCharges().getCurrentAmount());
        assertEquals(CURRENT_INTEREST_RECEIVABLE_AND_SIMILAR_INCOME, profitAndLoss.getProfitOrLossBeforeTax().
                getInterestReceivableAndSimilarIncome().getCurrentAmount());
        assertEquals(CURRENT_TOTAL_PROFIT_OR_LOSS_BEFORE_TAX, profitAndLoss.getProfitOrLossBeforeTax().
                getTotalProfitOrLossBeforeTax().getCurrentAmount());
    }

    @Test
    @DisplayName("Add previous period to web models which has current period values")
    void addPreviousPeriodToWebModelWhichHasCurrentPeriodValues() {

        ProfitAndLossApi previousPeriodProfitAndLossApi = new ProfitAndLossApi();

        ProfitOrLossBeforeTax profitOrLossBeforeTax = new ProfitOrLossBeforeTax();

        profitOrLossBeforeTax.setInterestPayableAndSimilarCharges(PREVIOUS_INTEREST_PAYABLE_AND_SIMILAR_CHARGES);
        profitOrLossBeforeTax.setInterestReceivableAndSimilarIncome(PREVIOUS_INTEREST_RECEIVABLE_AND_SIMILAR_INCOME);
        profitOrLossBeforeTax.setTotalProfitOrLossBeforeTax(PREVIOUS_TOTAL_PROFIT_OR_LOSS_BEFORE_TAX);

        previousPeriodProfitAndLossApi.setProfitOrLossBeforeTax(profitOrLossBeforeTax);

        ProfitAndLoss profitAndLoss = new ProfitAndLoss();

        uk.gov.companieshouse.web.accounts.model.profitandloss.profitorlossbeforetax.ProfitOrLossBeforeTax
                profitOrLossBeforeTaxWeb =
                new uk.gov.companieshouse.web.accounts.model.profitandloss.profitorlossbeforetax.ProfitOrLossBeforeTax();

        InterestPayableAndSimilarCharges interestPayableAndSimilarCharges = new InterestPayableAndSimilarCharges();
        interestPayableAndSimilarCharges.setCurrentAmount(CURRENT_INTEREST_PAYABLE_AND_SIMILAR_CHARGES);

        InterestReceivableAndSimilarIncome interestReceivableAndSimilarIncome = new InterestReceivableAndSimilarIncome();
        interestReceivableAndSimilarIncome.setCurrentAmount(CURRENT_INTEREST_RECEIVABLE_AND_SIMILAR_INCOME);

        TotalProfitOrLossBeforeTax totalProfitOrLossBeforeTax = new TotalProfitOrLossBeforeTax();
        totalProfitOrLossBeforeTax.setCurrentAmount(CURRENT_TOTAL_PROFIT_OR_LOSS_BEFORE_TAX);

        profitOrLossBeforeTaxWeb.setInterestReceivableAndSimilarIncome(interestReceivableAndSimilarIncome);
        profitOrLossBeforeTaxWeb.setInterestPayableAndSimilarCharges(interestPayableAndSimilarCharges);
        profitOrLossBeforeTaxWeb.setTotalProfitOrLossBeforeTax(totalProfitOrLossBeforeTax);

        profitAndLoss.setProfitOrLossBeforeTax(profitOrLossBeforeTaxWeb);

        transformer.addPreviousPeriodToWebModel(profitAndLoss, previousPeriodProfitAndLossApi);

        assertEquals(CURRENT_INTEREST_PAYABLE_AND_SIMILAR_CHARGES, profitAndLoss.getProfitOrLossBeforeTax().
                getInterestPayableAndSimilarCharges().getCurrentAmount());
        assertEquals(CURRENT_INTEREST_RECEIVABLE_AND_SIMILAR_INCOME, profitAndLoss.getProfitOrLossBeforeTax().
                getInterestReceivableAndSimilarIncome().getCurrentAmount());
        assertEquals(CURRENT_TOTAL_PROFIT_OR_LOSS_BEFORE_TAX, profitAndLoss.getProfitOrLossBeforeTax().
                getTotalProfitOrLossBeforeTax().getCurrentAmount());

        assertEquals(PREVIOUS_INTEREST_PAYABLE_AND_SIMILAR_CHARGES, profitAndLoss.getProfitOrLossBeforeTax().
                getInterestPayableAndSimilarCharges().getPreviousAmount());
        assertEquals(PREVIOUS_INTEREST_RECEIVABLE_AND_SIMILAR_INCOME, profitAndLoss.getProfitOrLossBeforeTax().
                getInterestReceivableAndSimilarIncome().getPreviousAmount());
        assertEquals(PREVIOUS_TOTAL_PROFIT_OR_LOSS_BEFORE_TAX, profitAndLoss.getProfitOrLossBeforeTax().
                getTotalProfitOrLossBeforeTax().getPreviousAmount());
    }


    @Test
    @DisplayName("Add current period to web model - no profit or loss before tax")
    void addCurrentPeriodToWebModelNoOperatingProfitOrLoss() {
        ProfitAndLoss profitAndLoss = new ProfitAndLoss();

        transformer.addCurrentPeriodToWebModel(profitAndLoss, new ProfitAndLossApi());

        assertNull(profitAndLoss.getProfitOrLossBeforeTax());
    }

    @Test
    @DisplayName("Add previous period to web model - no profit or loss before tax")
    void addPreviousPeriodToWebModelNoOperatingProfitOrLoss() {

        ProfitAndLoss profitAndLoss = new ProfitAndLoss();

        transformer.addPreviousPeriodToWebModel(profitAndLoss, new ProfitAndLossApi());

        assertNull(profitAndLoss.getProfitOrLossBeforeTax());
    }

    @Test
    @DisplayName("Add current period to api model")
    void addCurrentPeriodToApiModel() {

        ProfitAndLossApi currentPeriodProfitAndLoss = new ProfitAndLossApi();

        ProfitAndLoss profitAndLoss = new ProfitAndLoss();

        uk.gov.companieshouse.web.accounts.model.profitandloss.profitorlossbeforetax.ProfitOrLossBeforeTax
                profitOrLossBeforeTax =
                new uk.gov.companieshouse.web.accounts.model.profitandloss.profitorlossbeforetax.ProfitOrLossBeforeTax();

        InterestPayableAndSimilarCharges interestPayableAndSimilarCharges = new InterestPayableAndSimilarCharges();
        interestPayableAndSimilarCharges.setCurrentAmount(CURRENT_INTEREST_PAYABLE_AND_SIMILAR_CHARGES);

        InterestReceivableAndSimilarIncome interestReceivableAndSimilarIncome = new InterestReceivableAndSimilarIncome();
        interestReceivableAndSimilarIncome.setCurrentAmount(CURRENT_INTEREST_RECEIVABLE_AND_SIMILAR_INCOME);

        TotalProfitOrLossBeforeTax totalProfitOrLossBeforeTax = new TotalProfitOrLossBeforeTax();
        totalProfitOrLossBeforeTax.setCurrentAmount(CURRENT_TOTAL_PROFIT_OR_LOSS_BEFORE_TAX);

        profitOrLossBeforeTax.setInterestPayableAndSimilarCharges(interestPayableAndSimilarCharges);
        profitOrLossBeforeTax.setInterestReceivableAndSimilarIncome(interestReceivableAndSimilarIncome);
        profitOrLossBeforeTax.setTotalProfitOrLossBeforeTax(totalProfitOrLossBeforeTax);

        profitAndLoss.setProfitOrLossBeforeTax(profitOrLossBeforeTax);

        transformer.addCurrentPeriodToApiModel(profitAndLoss, currentPeriodProfitAndLoss);

        assertEquals(CURRENT_INTEREST_RECEIVABLE_AND_SIMILAR_INCOME, currentPeriodProfitAndLoss.getProfitOrLossBeforeTax().
                getInterestReceivableAndSimilarIncome());
        assertEquals(CURRENT_INTEREST_PAYABLE_AND_SIMILAR_CHARGES, currentPeriodProfitAndLoss.getProfitOrLossBeforeTax().
                getInterestPayableAndSimilarCharges());
        assertEquals(CURRENT_TOTAL_PROFIT_OR_LOSS_BEFORE_TAX, currentPeriodProfitAndLoss.getProfitOrLossBeforeTax().
                getTotalProfitOrLossBeforeTax());

    }

    @Test
    @DisplayName("Add previous period to api model")
    void addPreviousPeriodToApiModel() {

        ProfitAndLossApi previousPeriodProfitAndLoss = new ProfitAndLossApi();

        ProfitAndLoss profitAndLoss = new ProfitAndLoss();

        uk.gov.companieshouse.web.accounts.model.profitandloss.profitorlossbeforetax.ProfitOrLossBeforeTax
                profitOrLossBeforeTax =
                new uk.gov.companieshouse.web.accounts.model.profitandloss.profitorlossbeforetax.ProfitOrLossBeforeTax();

        InterestPayableAndSimilarCharges interestPayableAndSimilarCharges = new InterestPayableAndSimilarCharges();
        interestPayableAndSimilarCharges.setPreviousAmount(PREVIOUS_INTEREST_PAYABLE_AND_SIMILAR_CHARGES);

        InterestReceivableAndSimilarIncome interestReceivableAndSimilarIncome = new InterestReceivableAndSimilarIncome();
        interestReceivableAndSimilarIncome.setPreviousAmount(PREVIOUS_INTEREST_RECEIVABLE_AND_SIMILAR_INCOME);

        TotalProfitOrLossBeforeTax totalProfitOrLossBeforeTax = new TotalProfitOrLossBeforeTax();
        totalProfitOrLossBeforeTax.setPreviousAmount(PREVIOUS_TOTAL_PROFIT_OR_LOSS_BEFORE_TAX);

        profitOrLossBeforeTax.setInterestPayableAndSimilarCharges(interestPayableAndSimilarCharges);
        profitOrLossBeforeTax.setInterestReceivableAndSimilarIncome(interestReceivableAndSimilarIncome);
        profitOrLossBeforeTax.setTotalProfitOrLossBeforeTax(totalProfitOrLossBeforeTax);

        profitAndLoss.setProfitOrLossBeforeTax(profitOrLossBeforeTax);

        transformer.addPreviousPeriodToApiModel(profitAndLoss, previousPeriodProfitAndLoss);

        assertEquals(PREVIOUS_INTEREST_RECEIVABLE_AND_SIMILAR_INCOME, previousPeriodProfitAndLoss.getProfitOrLossBeforeTax().
                getInterestReceivableAndSimilarIncome());
        assertEquals(PREVIOUS_INTEREST_PAYABLE_AND_SIMILAR_CHARGES, previousPeriodProfitAndLoss.getProfitOrLossBeforeTax().
                getInterestPayableAndSimilarCharges());
        assertEquals(PREVIOUS_TOTAL_PROFIT_OR_LOSS_BEFORE_TAX, previousPeriodProfitAndLoss.getProfitOrLossBeforeTax().
                getTotalProfitOrLossBeforeTax());

    }

    @Test
    @DisplayName("Add current period to api model without profit or loss before tax to map")
    void addCurrentPeriodToApiModelWithoutProfitOrLossBeforeTaxToMap() {

        ProfitAndLossApi currentPeriodProfitAndLoss = new ProfitAndLossApi();

        ProfitAndLoss profitAndLoss = new ProfitAndLoss();

        uk.gov.companieshouse.web.accounts.model.profitandloss.profitorlossbeforetax.ProfitOrLossBeforeTax
                profitOrLossBeforeTaxWeb =
                new uk.gov.companieshouse.web.accounts.model.profitandloss.profitorlossbeforetax.ProfitOrLossBeforeTax();

        profitOrLossBeforeTaxWeb.setInterestReceivableAndSimilarIncome(new InterestReceivableAndSimilarIncome());
        profitOrLossBeforeTaxWeb.setInterestPayableAndSimilarCharges(new InterestPayableAndSimilarCharges());
        profitOrLossBeforeTaxWeb.setTotalProfitOrLossBeforeTax(new TotalProfitOrLossBeforeTax());


        profitAndLoss.setProfitOrLossBeforeTax(profitOrLossBeforeTaxWeb);

        transformer.addPreviousPeriodToApiModel(profitAndLoss, currentPeriodProfitAndLoss);

        assertNull(currentPeriodProfitAndLoss.getProfitOrLossBeforeTax());
    }

    @Test
    @DisplayName("Add previous period to api model without operating profit and loss to map")
    void addPreviousPeriodToApiModelWithoutOperatingProfitAndLossToMap() {

        ProfitAndLossApi previousPeriodProfitAndLoss = new ProfitAndLossApi();

        ProfitAndLoss profitAndLoss = new ProfitAndLoss();

        uk.gov.companieshouse.web.accounts.model.profitandloss.profitorlossbeforetax.ProfitOrLossBeforeTax
                profitOrLossBeforeTaxWeb =
                new uk.gov.companieshouse.web.accounts.model.profitandloss.profitorlossbeforetax.ProfitOrLossBeforeTax();

        profitOrLossBeforeTaxWeb.setInterestReceivableAndSimilarIncome(new InterestReceivableAndSimilarIncome());
        profitOrLossBeforeTaxWeb.setInterestPayableAndSimilarCharges(new InterestPayableAndSimilarCharges());
        profitOrLossBeforeTaxWeb.setTotalProfitOrLossBeforeTax(new TotalProfitOrLossBeforeTax());

        profitAndLoss.setProfitOrLossBeforeTax(profitOrLossBeforeTaxWeb);

        transformer.addPreviousPeriodToApiModel(profitAndLoss, previousPeriodProfitAndLoss);

        assertNull(previousPeriodProfitAndLoss.getProfitOrLossBeforeTax());
    }

}
