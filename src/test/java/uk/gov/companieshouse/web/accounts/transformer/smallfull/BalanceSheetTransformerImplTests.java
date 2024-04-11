package uk.gov.companieshouse.web.accounts.transformer.smallfull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.api.model.accounts.smallfull.BalanceSheetApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.CurrentAssetsApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.CurrentPeriodApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.FixedAssetsApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.MembersFundsApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.OtherLiabilitiesOrAssetsApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.PreviousPeriodApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.CapitalAndReservesApi;
import uk.gov.companieshouse.web.accounts.model.smallfull.BalanceSheet;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.impl.BalanceSheetTransformerImpl;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BalanceSheetTransformerImplTests {
    @Mock
    private Transformer calledUpShareCapitalNotPaidTransformer;

    @Mock
    private Transformer fixedAssetsTransformer;

    @Mock
    private Transformer currentAssetsTransformer;

    @Mock
    private Transformer otherLiabilitiesOrAssetsTransformer;

    @Mock
    private Transformer capitalAndReservesTransformer;

    @Mock
    private Transformer membersFundsTransformer;

    @InjectMocks
    private final BalanceSheetTransformer transformer = new BalanceSheetTransformerImpl();

    private static final Long CALLED_UP_SHARE_CAPITAL_NOT_PAID = 1L;

    @Test
    @DisplayName("Get balance sheet for current period")
    void getBalanceSheetForCurrentPeriod() {
        BalanceSheetApi balanceSheetApi = new BalanceSheetApi();
        balanceSheetApi.setCalledUpShareCapitalNotPaid(CALLED_UP_SHARE_CAPITAL_NOT_PAID);

        balanceSheetApi.setFixedAssets(new FixedAssetsApi());
        balanceSheetApi.setCurrentAssets(new CurrentAssetsApi());
        balanceSheetApi.setOtherLiabilitiesOrAssets(new OtherLiabilitiesOrAssetsApi());
        balanceSheetApi.setCapitalAndReserves(new CapitalAndReservesApi());
        balanceSheetApi.setMembersFunds(new MembersFundsApi());

        CurrentPeriodApi currentPeriodApi = new CurrentPeriodApi();
        currentPeriodApi.setBalanceSheet(balanceSheetApi);

        BalanceSheet balanceSheet = transformer.getBalanceSheet(currentPeriodApi, null);

        verifyCurrentPeriodApiToWebTransformersCalled();
        verifyPreviousPeriodApiToWebTransformersNotCalled();

        verifyCurrentPeriodWebToApiTransformersNotCalled();
        verifyPreviousPeriodWebToApiTransformersNotCalled();

        assertNotNull(balanceSheet);
    }

    @Test
    @DisplayName("Get balance sheet for both periods")
    void getBalanceSheetForBothPeriods() {
        BalanceSheetApi balanceSheetApi = new BalanceSheetApi();
        balanceSheetApi.setCalledUpShareCapitalNotPaid(CALLED_UP_SHARE_CAPITAL_NOT_PAID);

        balanceSheetApi.setFixedAssets(new FixedAssetsApi());
        balanceSheetApi.setCurrentAssets(new CurrentAssetsApi());
        balanceSheetApi.setOtherLiabilitiesOrAssets(new OtherLiabilitiesOrAssetsApi());
        balanceSheetApi.setCapitalAndReserves(new CapitalAndReservesApi());
        balanceSheetApi.setMembersFunds(new MembersFundsApi());

        CurrentPeriodApi currentPeriodApi = new CurrentPeriodApi();
        currentPeriodApi.setBalanceSheet(balanceSheetApi);

        PreviousPeriodApi previousPeriodApi = new PreviousPeriodApi();
        previousPeriodApi.setBalanceSheet(balanceSheetApi);

        BalanceSheet balanceSheet = transformer.getBalanceSheet(currentPeriodApi, previousPeriodApi);

        verifyCurrentPeriodApiToWebTransformersCalled();
        verifyPreviousPeriodApiToWebTransformersCalled();

        verifyCurrentPeriodWebToApiTransformersNotCalled();
        verifyPreviousPeriodWebToApiTransformersNotCalled();

        assertNotNull(balanceSheet);
    }

    @Test
    @DisplayName("Get current period for LBG company")
    void getCurrentPeriodLBG() {
        BalanceSheet balanceSheet = new BalanceSheet();
        balanceSheet.setLbg(true);

        BalanceSheetApi currentPeriodBalanceSheet = transformer.getCurrentPeriodBalanceSheet(balanceSheet);

        verifyCurrentPeriodWebToApiTransformersCalled(true);
        verifyPreviousPeriodWebToApiTransformersNotCalled();

        verifyCurrentPeriodApiToWebTransformersNotCalled();
        verifyPreviousPeriodApiToWebTransformersNotCalled();

        assertNotNull(currentPeriodBalanceSheet);
    }

    @Test
    @DisplayName("Get previous period for LBG company")
    void getPreviousPeriodLBG() {
        BalanceSheet balanceSheet = new BalanceSheet();
        balanceSheet.setLbg(true);

        BalanceSheetApi previousPeriodBalanceSheet = transformer.getPreviousPeriodBalanceSheet(balanceSheet);

        verifyCurrentPeriodWebToApiTransformersNotCalled();
        verifyPreviousPeriodWebToApiTransformersCalled(true);

        verifyCurrentPeriodApiToWebTransformersNotCalled();
        verifyPreviousPeriodApiToWebTransformersNotCalled();

        assertNotNull(previousPeriodBalanceSheet);
    }

    @Test
    @DisplayName("Get current period for LBS company")
    void getCurrentPeriodLBS() {
        BalanceSheet balanceSheet = new BalanceSheet();
        balanceSheet.setLbg(false);

        BalanceSheetApi currentPeriodBalanceSheet = transformer.getCurrentPeriodBalanceSheet(balanceSheet);

        verifyCurrentPeriodWebToApiTransformersCalled(false);
        verifyPreviousPeriodWebToApiTransformersNotCalled();

        verifyCurrentPeriodApiToWebTransformersNotCalled();
        verifyPreviousPeriodApiToWebTransformersNotCalled();

        assertNotNull(currentPeriodBalanceSheet);
    }

    @Test
    @DisplayName("Get previous period for LBS company")
    void getPreviousPeriodLBS() {
        BalanceSheet balanceSheet = new BalanceSheet();
        balanceSheet.setLbg(false);

        BalanceSheetApi previousPeriodBalanceSheet = transformer.getPreviousPeriodBalanceSheet(balanceSheet);

        verifyCurrentPeriodWebToApiTransformersNotCalled();
        verifyPreviousPeriodWebToApiTransformersCalled(false);

        verifyCurrentPeriodApiToWebTransformersNotCalled();
        verifyPreviousPeriodApiToWebTransformersNotCalled();

        assertNotNull(previousPeriodBalanceSheet);
    }

    private void verifyCurrentPeriodWebToApiTransformersCalled(boolean isLbg) {
        verify(fixedAssetsTransformer, times(1)).addCurrentPeriodToApiModel(any(BalanceSheetApi.class), any(BalanceSheet.class));
        verify(calledUpShareCapitalNotPaidTransformer, times(1)).addCurrentPeriodToApiModel(any(BalanceSheetApi.class), any(BalanceSheet.class));
        verify(currentAssetsTransformer, times(1)).addCurrentPeriodToApiModel(any(BalanceSheetApi.class), any(BalanceSheet.class));
        verify(otherLiabilitiesOrAssetsTransformer, times(1)).addCurrentPeriodToApiModel(any(BalanceSheetApi.class), any(BalanceSheet.class));
        if (isLbg) {
            verify(membersFundsTransformer, times(1))
                    .addCurrentPeriodToApiModel(any(BalanceSheetApi.class),
                            any(BalanceSheet.class));
        } else {
            verify(capitalAndReservesTransformer, times(1))
                    .addCurrentPeriodToApiModel(any(BalanceSheetApi.class),
                            any(BalanceSheet.class));
        }
    }

    private void verifyPreviousPeriodWebToApiTransformersCalled(boolean isLbg) {
        verify(fixedAssetsTransformer, times(1)).addPreviousPeriodToApiModel(any(BalanceSheetApi.class), any(BalanceSheet.class));
        verify(calledUpShareCapitalNotPaidTransformer, times(1)).addPreviousPeriodToApiModel(any(BalanceSheetApi.class), any(BalanceSheet.class));
        verify(currentAssetsTransformer, times(1)).addPreviousPeriodToApiModel(any(BalanceSheetApi.class), any(BalanceSheet.class));
        verify(otherLiabilitiesOrAssetsTransformer, times(1)).addPreviousPeriodToApiModel(any(BalanceSheetApi.class), any(BalanceSheet.class));
        if (isLbg) {
            verify(membersFundsTransformer, times(1))
                    .addPreviousPeriodToApiModel(any(BalanceSheetApi.class),
                            any(BalanceSheet.class));
        } else {
            verify(capitalAndReservesTransformer, times(1))
                    .addPreviousPeriodToApiModel(any(BalanceSheetApi.class),
                            any(BalanceSheet.class));
        }
    }

    private void verifyCurrentPeriodApiToWebTransformersCalled() {
        verify(fixedAssetsTransformer, times(1)).addCurrentPeriodToWebModel(any(BalanceSheet.class), any(BalanceSheetApi.class));
        verify(calledUpShareCapitalNotPaidTransformer, times(1)).addCurrentPeriodToWebModel(any(BalanceSheet.class), any(BalanceSheetApi.class));
        verify(currentAssetsTransformer, times(1)).addCurrentPeriodToWebModel(any(BalanceSheet.class), any(BalanceSheetApi.class));
        verify(otherLiabilitiesOrAssetsTransformer, times(1)).addCurrentPeriodToWebModel(any(BalanceSheet.class), any(BalanceSheetApi.class));
        verify(capitalAndReservesTransformer, times(1)).addCurrentPeriodToWebModel(any(BalanceSheet.class), any(BalanceSheetApi.class));
        verify(membersFundsTransformer, times(1)).addCurrentPeriodToWebModel(any(BalanceSheet.class), any(BalanceSheetApi.class));

    }

    private void verifyPreviousPeriodApiToWebTransformersCalled() {
        verify(fixedAssetsTransformer, times(1)).addPreviousPeriodToWebModel(any(BalanceSheet.class), any(BalanceSheetApi.class));
        verify(calledUpShareCapitalNotPaidTransformer, times(1)).addPreviousPeriodToWebModel(any(BalanceSheet.class), any(BalanceSheetApi.class));
        verify(currentAssetsTransformer, times(1)).addPreviousPeriodToWebModel(any(BalanceSheet.class), any(BalanceSheetApi.class));
        verify(otherLiabilitiesOrAssetsTransformer, times(1)).addPreviousPeriodToWebModel(any(BalanceSheet.class), any(BalanceSheetApi.class));
        verify(capitalAndReservesTransformer, times(1)).addPreviousPeriodToWebModel(any(BalanceSheet.class), any(BalanceSheetApi.class));
        verify(membersFundsTransformer, times(1)).addPreviousPeriodToWebModel(any(BalanceSheet.class), any(BalanceSheetApi.class));
    }

    private void verifyCurrentPeriodWebToApiTransformersNotCalled() {
        verify(fixedAssetsTransformer, never()).addCurrentPeriodToApiModel(any(BalanceSheetApi.class), any(BalanceSheet.class));
        verify(calledUpShareCapitalNotPaidTransformer, never()).addCurrentPeriodToApiModel(any(BalanceSheetApi.class), any(BalanceSheet.class));
        verify(currentAssetsTransformer, never()).addCurrentPeriodToApiModel(any(BalanceSheetApi.class), any(BalanceSheet.class));
        verify(otherLiabilitiesOrAssetsTransformer, never()).addCurrentPeriodToApiModel(any(BalanceSheetApi.class), any(BalanceSheet.class));
        verify(capitalAndReservesTransformer, never()).addCurrentPeriodToApiModel(any(BalanceSheetApi.class), any(BalanceSheet.class));
        verify(membersFundsTransformer, never()).addCurrentPeriodToApiModel(any(BalanceSheetApi.class), any(BalanceSheet.class));
    }

    private void verifyPreviousPeriodWebToApiTransformersNotCalled() {
        verify(fixedAssetsTransformer, never()).addPreviousPeriodToApiModel(any(BalanceSheetApi.class), any(BalanceSheet.class));
        verify(calledUpShareCapitalNotPaidTransformer, never()).addPreviousPeriodToApiModel(any(BalanceSheetApi.class), any(BalanceSheet.class));
        verify(currentAssetsTransformer, never()).addPreviousPeriodToApiModel(any(BalanceSheetApi.class), any(BalanceSheet.class));
        verify(otherLiabilitiesOrAssetsTransformer, never()).addPreviousPeriodToApiModel(any(BalanceSheetApi.class), any(BalanceSheet.class));
        verify(capitalAndReservesTransformer, never()).addPreviousPeriodToApiModel(any(BalanceSheetApi.class), any(BalanceSheet.class));
        verify(membersFundsTransformer, never()).addPreviousPeriodToApiModel(any(BalanceSheetApi.class), any(BalanceSheet.class));
    }

    private void verifyCurrentPeriodApiToWebTransformersNotCalled() {
        verify(fixedAssetsTransformer, never()).addCurrentPeriodToWebModel(any(BalanceSheet.class), any(BalanceSheetApi.class));
        verify(calledUpShareCapitalNotPaidTransformer, never()).addCurrentPeriodToWebModel(any(BalanceSheet.class), any(BalanceSheetApi.class));
        verify(currentAssetsTransformer, never()).addCurrentPeriodToWebModel(any(BalanceSheet.class), any(BalanceSheetApi.class));
        verify(otherLiabilitiesOrAssetsTransformer, never()).addCurrentPeriodToWebModel(any(BalanceSheet.class), any(BalanceSheetApi.class));
        verify(capitalAndReservesTransformer, never()).addCurrentPeriodToWebModel(any(BalanceSheet.class), any(BalanceSheetApi.class));
        verify(membersFundsTransformer, never()).addCurrentPeriodToWebModel(any(BalanceSheet.class), any(BalanceSheetApi.class));

    }

    private void verifyPreviousPeriodApiToWebTransformersNotCalled() {
        verify(fixedAssetsTransformer, never()).addPreviousPeriodToWebModel(any(BalanceSheet.class), any(BalanceSheetApi.class));
        verify(calledUpShareCapitalNotPaidTransformer, never()).addPreviousPeriodToWebModel(any(BalanceSheet.class), any(BalanceSheetApi.class));
        verify(currentAssetsTransformer, never()).addPreviousPeriodToWebModel(any(BalanceSheet.class), any(BalanceSheetApi.class));
        verify(otherLiabilitiesOrAssetsTransformer, never()).addPreviousPeriodToWebModel(any(BalanceSheet.class), any(BalanceSheetApi.class));
        verify(capitalAndReservesTransformer, never()).addPreviousPeriodToWebModel(any(BalanceSheet.class), any(BalanceSheetApi.class));
        verify(membersFundsTransformer, never()).addPreviousPeriodToWebModel(any(BalanceSheet.class), any(BalanceSheetApi.class));
    }
}
