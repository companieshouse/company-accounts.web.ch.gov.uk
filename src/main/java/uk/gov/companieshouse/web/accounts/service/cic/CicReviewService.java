package uk.gov.companieshouse.web.accounts.service.cic;

import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.cic.CicReview;

public interface CicReviewService {

    CicReview getReview(String transactionId, String companyAccountsId, String companyNumber) throws ServiceException;
}