package uk.gov.companieshouse.web.accounts.service.smallfull;

import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.smallfull.Review;

public interface ReviewService {
    Review getReview(String transactionId, String companyAccountsId, String companyNumber) throws ServiceException;
}
