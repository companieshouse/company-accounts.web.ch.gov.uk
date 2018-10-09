package uk.gov.companieshouse.web.accounts.transformer.smallfull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.api.model.accounts.smallfull.CurrentPeriodApi;
import uk.gov.companieshouse.web.accounts.model.smallfull.BalanceSheet;
import uk.gov.companieshouse.web.accounts.model.smallfull.Review;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.impl.ReviewTransformerImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReviewTransformerTests {

    @Mock
    private BalanceSheetTransformer balanceSheetTransformer;

    @InjectMocks
    private ReviewTransformer transformer = new ReviewTransformerImpl();

    @Test
    @DisplayName("Get Review - Assert Balance Sheet is correct")
    void getBalanceSheetCalledUpShareCapital() {

        CurrentPeriodApi currentPeriodApi = new CurrentPeriodApi();

        BalanceSheet balanceSheet = new BalanceSheet();

        when(balanceSheetTransformer.getBalanceSheet(currentPeriodApi)).thenReturn(balanceSheet);

        Review review = transformer.getReview(currentPeriodApi);

        assertNotNull(review);
        assertNotNull(review.getBalanceSheet());
        assertEquals(balanceSheet, review.getBalanceSheet());
    }
}
