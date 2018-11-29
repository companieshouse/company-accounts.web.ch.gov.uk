package uk.gov.companieshouse.web.accounts.service.smallfull.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.smallfull.BalanceSheet;
import uk.gov.companieshouse.web.accounts.model.smallfull.Review;
import uk.gov.companieshouse.web.accounts.model.smallfull.Statements;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.accountingpolicies.BasisOfPreparation;
import uk.gov.companieshouse.web.accounts.service.smallfull.BalanceSheetService;
import uk.gov.companieshouse.web.accounts.service.smallfull.BasisOfPreparationService;
import uk.gov.companieshouse.web.accounts.service.smallfull.ReviewService;
import uk.gov.companieshouse.web.accounts.service.smallfull.StatementsService;

@Service
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    private BalanceSheetService balanceSheetService;

    @Autowired
    private StatementsService statementsService;

    @Autowired
    private BasisOfPreparationService basisOfPreparationService;

    public Review getReview(String transactionId, String companyAccountsId, String companyNumber) throws ServiceException {

        BalanceSheet balanceSheet = balanceSheetService.getBalanceSheet(transactionId, companyAccountsId, companyNumber);

        Statements statements = statementsService.getBalanceSheetStatements(transactionId, companyAccountsId);

        BasisOfPreparation basisOfPreparation = basisOfPreparationService.getBasisOfPreparation(transactionId, companyAccountsId);

        Review review = new Review();
        review.setBalanceSheet(balanceSheet);
        review.setStatements(statements);
        review.setBasisOfPreparation(basisOfPreparation);

        return review;
    }
}
