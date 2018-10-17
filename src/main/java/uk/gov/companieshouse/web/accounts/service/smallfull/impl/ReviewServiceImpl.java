package uk.gov.companieshouse.web.accounts.service.smallfull.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.smallfull.BalanceSheet;
import uk.gov.companieshouse.web.accounts.model.smallfull.Review;
import uk.gov.companieshouse.web.accounts.service.smallfull.BalanceSheetService;
import uk.gov.companieshouse.web.accounts.service.smallfull.ReviewService;

@Service
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    BalanceSheetService balanceSheetService;

    public Review getReview(String transactionId, String companyAccountsId, String companyNumber) throws ServiceException {

        BalanceSheet balanceSheet = balanceSheetService.getBalanceSheet(transactionId, companyAccountsId, companyNumber);

        Review review = new Review();
        review.setBalanceSheet(balanceSheet);

        return review;
    }
}
