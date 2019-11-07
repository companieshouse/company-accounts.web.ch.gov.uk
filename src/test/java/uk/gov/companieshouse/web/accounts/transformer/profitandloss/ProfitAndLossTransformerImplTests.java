package uk.gov.companieshouse.web.accounts.transformer.profitandloss;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.api.model.accounts.profitandloss.ProfitAndLossApi;
import uk.gov.companieshouse.web.accounts.model.profitandloss.ProfitAndLoss;
import uk.gov.companieshouse.web.accounts.transformer.profitandloss.impl.GrossProfitAndLossTransformer;
import uk.gov.companieshouse.web.accounts.transformer.profitandloss.impl.OperatingProfitAndLossTransformer;
import uk.gov.companieshouse.web.accounts.transformer.profitandloss.impl.ProfitAndLossTransformerImpl;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ProfitAndLossTransformerImplTests {

    @Mock
    private GrossProfitAndLossTransformer grossProfitAndLossTransformer;

    @Mock
    OperatingProfitAndLossTransformer operatingProfitAndLossTransformer;

    @InjectMocks
    private ProfitAndLossTransformer transformer = new ProfitAndLossTransformerImpl();

    @Test
    @DisplayName("Get profit and loss - periods not null")
    void getProfitAndLossPeriodsNotNull() {

        ProfitAndLossApi currentPeriodProfitAndLoss = new ProfitAndLossApi();
        ProfitAndLossApi previousPeriodProfitAndLoss = new ProfitAndLossApi();

        assertNotNull(transformer.getProfitAndLoss(currentPeriodProfitAndLoss, previousPeriodProfitAndLoss));

        verify(grossProfitAndLossTransformer)
                .addCurrentPeriodToWebModel(any(ProfitAndLoss.class), eq(currentPeriodProfitAndLoss));
        verify(grossProfitAndLossTransformer)
                .addPreviousPeriodToWebModel(any(ProfitAndLoss.class), eq(previousPeriodProfitAndLoss));

        verify(operatingProfitAndLossTransformer)
                .addCurrentPeriodToWebModel(any(ProfitAndLoss.class), eq(currentPeriodProfitAndLoss));
        verify(operatingProfitAndLossTransformer)
                .addPreviousPeriodToWebModel(any(ProfitAndLoss.class), eq(previousPeriodProfitAndLoss));
    }

    @Test
    @DisplayName("Get profit and loss - periods null")
    void getProfitAndLossPeriodsNull() {

        assertNotNull(transformer.getProfitAndLoss(null, null));

        verify(grossProfitAndLossTransformer, never())
                .addCurrentPeriodToWebModel(any(ProfitAndLoss.class), any(ProfitAndLossApi.class));
        verify(grossProfitAndLossTransformer, never())
                .addPreviousPeriodToWebModel(any(ProfitAndLoss.class), any(ProfitAndLossApi.class));

        verify(operatingProfitAndLossTransformer, never())
                .addCurrentPeriodToWebModel(any(ProfitAndLoss.class), any(ProfitAndLossApi.class));
        verify(operatingProfitAndLossTransformer, never())
                .addPreviousPeriodToWebModel(any(ProfitAndLoss.class), any(ProfitAndLossApi.class));
    }

    @Test
    @DisplayName("Get current period")
    void getCurrentPeriod() {

        ProfitAndLoss profitAndLoss = new ProfitAndLoss();

        assertNotNull(transformer.getCurrentPeriodProfitAndLoss(profitAndLoss));

        verify(grossProfitAndLossTransformer).addCurrentPeriodToApiModel(eq(profitAndLoss), any(ProfitAndLossApi.class));
        verify(operatingProfitAndLossTransformer).addCurrentPeriodToApiModel(eq(profitAndLoss), any(ProfitAndLossApi.class));
    }

    @Test
    @DisplayName("Get previous period")
    void getPreviousPeriod() {

        ProfitAndLoss profitAndLoss = new ProfitAndLoss();

        assertNotNull(transformer.getPreviousPeriodProfitAndLoss(profitAndLoss));

        verify(grossProfitAndLossTransformer).addPreviousPeriodToApiModel(eq(profitAndLoss), any(ProfitAndLossApi.class));
        verify(operatingProfitAndLossTransformer).addPreviousPeriodToApiModel(eq(profitAndLoss), any(ProfitAndLossApi.class));
    }
}

