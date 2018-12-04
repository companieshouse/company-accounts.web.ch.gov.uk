package uk.gov.companieshouse.web.accounts.service.smallfull.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.smallfull.BalanceSheet;
import uk.gov.companieshouse.web.accounts.model.smallfull.Review;
import uk.gov.companieshouse.web.accounts.model.smallfull.Statements;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.accountingpolicies.BasisOfPreparation;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.accountingpolicies.TurnoverPolicy;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.accountingpolicies.ValuationInformationPolicy;
import uk.gov.companieshouse.web.accounts.service.smallfull.BalanceSheetService;
import uk.gov.companieshouse.web.accounts.service.smallfull.BasisOfPreparationService;
import uk.gov.companieshouse.web.accounts.service.smallfull.ReviewService;
import uk.gov.companieshouse.web.accounts.service.smallfull.StatementsService;
import uk.gov.companieshouse.web.accounts.service.smallfull.TurnoverPolicyService;
import uk.gov.companieshouse.web.accounts.service.smallfull.ValuationInformationPolicyService;

@Service
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    private BalanceSheetService balanceSheetService;

    @Autowired
    private StatementsService statementsService;

    @Autowired
    private BasisOfPreparationService basisOfPreparationService;

    @Autowired
    private TurnoverPolicyService turnoverPolicyService;

    @Autowired
    private ValuationInformationPolicyService valuationInformationPolicyService;

    public Review getReview(String transactionId, String companyAccountsId, String companyNumber) throws ServiceException {

        BalanceSheet balanceSheet = balanceSheetService.getBalanceSheet(transactionId, companyAccountsId, companyNumber);

        Statements statements = statementsService.getBalanceSheetStatements(transactionId, companyAccountsId);

        BasisOfPreparation basisOfPreparation = basisOfPreparationService.getBasisOfPreparation(transactionId, companyAccountsId);

        TurnoverPolicy turnoverPolicy = turnoverPolicyService.getTurnOverPolicy(transactionId, companyAccountsId);

        ValuationInformationPolicy valuationInformationPolicy =
                valuationInformationPolicyService.getValuationInformationPolicy(transactionId, companyAccountsId);

        Review review = new Review();
        review.setBalanceSheet(balanceSheet);
        review.setStatements(statements);
        review.setBasisOfPreparation(basisOfPreparation);
        review.setTurnoverPolicy(turnoverPolicy);
        review.setValuationInformationPolicy(valuationInformationPolicy);

        return review;
    }
}
