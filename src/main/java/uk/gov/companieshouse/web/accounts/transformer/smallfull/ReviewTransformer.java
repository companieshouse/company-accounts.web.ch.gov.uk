package uk.gov.companieshouse.web.accounts.transformer.smallfull;

import uk.gov.companieshouse.api.model.accounts.smallfull.CurrentPeriodApi;
import uk.gov.companieshouse.web.accounts.model.smallfull.Review;

public interface ReviewTransformer {

    Review getReview(CurrentPeriodApi currentPeriodApi);
}
