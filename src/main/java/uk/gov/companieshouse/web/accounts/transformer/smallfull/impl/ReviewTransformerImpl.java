package uk.gov.companieshouse.web.accounts.transformer.smallfull.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.api.model.accounts.smallfull.CurrentPeriodApi;
import uk.gov.companieshouse.web.accounts.model.smallfull.BalanceSheet;
import uk.gov.companieshouse.web.accounts.model.smallfull.Review;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.BalanceSheetTransformer;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.ReviewTransformer;

@Component
public class ReviewTransformerImpl implements ReviewTransformer {

    @Autowired
    BalanceSheetTransformer balanceSheetTransformer;

    public Review getReview(CurrentPeriodApi currentPeriodApi) {

        BalanceSheet balanceSheet = balanceSheetTransformer.getBalanceSheet(currentPeriodApi);

        Review review = new Review();
        review.setBalanceSheet(balanceSheet);

        return review;
    }
}
