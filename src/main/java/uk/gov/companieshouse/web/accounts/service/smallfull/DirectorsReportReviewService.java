package uk.gov.companieshouse.web.accounts.service.smallfull;

import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.directorsreport.DirectorsReportReview;

public interface DirectorsReportReviewService {
    DirectorsReportReview getReview(String transactionId, String companyAccountsId) throws ServiceException;
}
