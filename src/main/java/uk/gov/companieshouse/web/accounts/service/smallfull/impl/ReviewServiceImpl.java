package uk.gov.companieshouse.web.accounts.service.smallfull.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.smallfull.BalanceSheet;
import uk.gov.companieshouse.web.accounts.model.smallfull.Review;
import uk.gov.companieshouse.web.accounts.model.smallfull.Statements;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.tangible.TangibleAssets;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.accountingpolicies.BasisOfPreparation;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.accountingpolicies.IntangibleAmortisationPolicy;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.accountingpolicies.OtherAccountingPolicy;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.accountingpolicies.TangibleDepreciationPolicy;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.accountingpolicies.TurnoverPolicy;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.accountingpolicies.ValuationInformationPolicy;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.creditorswithinoneyear.CreditorsWithinOneYear;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.debtors.Debtors;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.stocks.StocksNote;
import uk.gov.companieshouse.web.accounts.service.smallfull.BalanceSheetService;
import uk.gov.companieshouse.web.accounts.service.smallfull.BasisOfPreparationService;
import uk.gov.companieshouse.web.accounts.service.smallfull.CreditorsWithinOneYearService;
import uk.gov.companieshouse.web.accounts.service.smallfull.DebtorsService;
import uk.gov.companieshouse.web.accounts.service.smallfull.IntangibleAmortisationPolicyService;
import uk.gov.companieshouse.web.accounts.service.smallfull.OtherAccountingPolicyService;
import uk.gov.companieshouse.web.accounts.service.smallfull.ReviewService;
import uk.gov.companieshouse.web.accounts.service.smallfull.StatementsService;
import uk.gov.companieshouse.web.accounts.service.smallfull.TangibleAssetsNoteService;
import uk.gov.companieshouse.web.accounts.service.smallfull.StocksService;
import uk.gov.companieshouse.web.accounts.service.smallfull.TangibleDepreciationPolicyService;
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
    private TangibleDepreciationPolicyService tangibleDepreciationPolicyService;

    @Autowired
    private IntangibleAmortisationPolicyService intangibleAmortisationPolicyService;

    @Autowired
    private ValuationInformationPolicyService valuationInformationPolicyService;

    @Autowired
    private OtherAccountingPolicyService otherAccountingPolicyService;
    
    @Autowired
    private CreditorsWithinOneYearService creditorsWithinOneYearService;

    @Autowired
    private DebtorsService debtorsService;

    @Autowired
    private StocksService stocksService;

    @Autowired
    private TangibleAssetsNoteService tangibleAssetsNoteService;

    public Review getReview(String transactionId, String companyAccountsId, String companyNumber) throws ServiceException {

        BalanceSheet balanceSheet = balanceSheetService.getBalanceSheet(transactionId, companyAccountsId, companyNumber);

        Statements statements = statementsService.getBalanceSheetStatements(transactionId, companyAccountsId);

        BasisOfPreparation basisOfPreparation = basisOfPreparationService.getBasisOfPreparation(transactionId, companyAccountsId);

        TurnoverPolicy turnoverPolicy = turnoverPolicyService.getTurnOverPolicy(transactionId, companyAccountsId);

        TangibleDepreciationPolicy tangibleDepreciationPolicy = tangibleDepreciationPolicyService.getTangibleDepreciationPolicy(transactionId, companyAccountsId);

        IntangibleAmortisationPolicy intangibleAmortisationPolicy =
                intangibleAmortisationPolicyService.getIntangibleAmortisationPolicy(transactionId, companyAccountsId);

        ValuationInformationPolicy valuationInformationPolicy =
                valuationInformationPolicyService.getValuationInformationPolicy(transactionId, companyAccountsId);

        OtherAccountingPolicy otherAccountingPolicy =
                otherAccountingPolicyService.getOtherAccountingPolicy(transactionId, companyAccountsId);
        
        CreditorsWithinOneYear creditorsWithinOneYear = creditorsWithinOneYearService.getCreditorsWithinOneYear(transactionId, companyAccountsId, companyNumber);

        Debtors debtors = debtorsService.getDebtors(transactionId, companyAccountsId, companyNumber);

        StocksNote stocks = stocksService.getStocks(transactionId,companyAccountsId, companyNumber);

        TangibleAssets tangibleAssets =
                tangibleAssetsNoteService.getTangibleAssets(transactionId, companyAccountsId, companyNumber);

        Review review = new Review();
        review.setBalanceSheet(balanceSheet);
        review.setStatements(statements);
        review.setBasisOfPreparation(basisOfPreparation);
        review.setTurnoverPolicy(turnoverPolicy);
        review.setTangibleDepreciationPolicy(tangibleDepreciationPolicy);
        review.setIntangibleAmortisationPolicy(intangibleAmortisationPolicy);
        review.setValuationInformationPolicy(valuationInformationPolicy);
        review.setOtherAccountingPolicy(otherAccountingPolicy);
        review.setCreditorsWithinOneYear(creditorsWithinOneYear);
        review.setDebtors(debtors);
        review.setTangibleAssets(tangibleAssets);
        review.setStocks(stocks);

        return review;
    }
}
