package uk.gov.companieshouse.web.accounts.service.cic;

import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.cic.CicReview;

public interface CicReviewService {
    CicReview getReview(String transactionId, String companyAccountsId) throws ServiceException;

    void acceptStatements(String transactionId, String companyAccountsId) throws ServiceException;
}