package uk.gov.companieshouse.web.accounts.service.smallfull.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.companieshouse.web.accounts.enumeration.NoteType;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.profitandloss.ProfitAndLoss;
import uk.gov.companieshouse.web.accounts.model.smallfull.BalanceSheet;
import uk.gov.companieshouse.web.accounts.model.smallfull.Review;
import uk.gov.companieshouse.web.accounts.model.smallfull.Statements;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.accountingpolicies.BasisOfPreparation;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.accountingpolicies.IntangibleAmortisationPolicy;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.accountingpolicies.OtherAccountingPolicy;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.accountingpolicies.TangibleDepreciationPolicy;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.accountingpolicies.TurnoverPolicy;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.accountingpolicies.ValuationInformationPolicy;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.creditorsafteroneyear.CreditorsAfterOneYear;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.creditorswithinoneyear.CreditorsWithinOneYear;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.currentassetsinvestments.CurrentAssetsInvestments;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.debtors.Debtors;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.employees.Employees;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.fixedassetsinvestments.FixedAssetsInvestments;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.intangible.IntangibleAssets;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.offbalancesheetarrangements.OffBalanceSheetArrangements;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.stocks.StocksNote;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.tangible.TangibleAssets;
import uk.gov.companieshouse.web.accounts.service.NoteService;
import uk.gov.companieshouse.web.accounts.service.smallfull.BalanceSheetService;
import uk.gov.companieshouse.web.accounts.service.smallfull.BasisOfPreparationService;
import uk.gov.companieshouse.web.accounts.service.smallfull.CreditorsAfterOneYearService;
import uk.gov.companieshouse.web.accounts.service.smallfull.CurrentAssetsInvestmentsService;
import uk.gov.companieshouse.web.accounts.service.smallfull.EmployeesService;
import uk.gov.companieshouse.web.accounts.service.smallfull.FixedAssetsInvestmentsService;
import uk.gov.companieshouse.web.accounts.service.smallfull.IntangibleAmortisationPolicyService;
import uk.gov.companieshouse.web.accounts.service.smallfull.IntangibleAssetsNoteService;
import uk.gov.companieshouse.web.accounts.service.smallfull.OtherAccountingPolicyService;
import uk.gov.companieshouse.web.accounts.service.smallfull.ProfitAndLossService;
import uk.gov.companieshouse.web.accounts.service.smallfull.ReviewService;
import uk.gov.companieshouse.web.accounts.service.smallfull.StatementsService;
import uk.gov.companieshouse.web.accounts.service.smallfull.TangibleAssetsNoteService;
import uk.gov.companieshouse.web.accounts.service.smallfull.TangibleDepreciationPolicyService;
import uk.gov.companieshouse.web.accounts.service.smallfull.TurnoverPolicyService;
import uk.gov.companieshouse.web.accounts.service.smallfull.ValuationInformationPolicyService;

@Service
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    private ProfitAndLossService profitAndLossService;

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
    private NoteService<CreditorsWithinOneYear> creditorsWithinOneYearService;

    @Autowired
    private CreditorsAfterOneYearService creditorsAfterOneYearService;

    @Autowired
    private NoteService<Debtors> debtorsService;

    @Autowired
    private EmployeesService employeesService;

    @Autowired
    private NoteService<StocksNote> stocksService;

    @Autowired
    private TangibleAssetsNoteService tangibleAssetsNoteService;

    @Autowired
    private IntangibleAssetsNoteService intangibleAssetsNoteService;
    
    @Autowired
    private FixedAssetsInvestmentsService fixedAssetsInvestmentsService;

    @Autowired
    private CurrentAssetsInvestmentsService currentAssetsInvestmentsService;

    @Autowired
    private NoteService<OffBalanceSheetArrangements> offBalanceSheetArrangementsService;

    public Review getReview(String transactionId, String companyAccountsId, String companyNumber) throws ServiceException {

        ProfitAndLoss profitAndLoss = profitAndLossService.getProfitAndLoss(transactionId, companyAccountsId, companyNumber);

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
        
        CreditorsWithinOneYear creditorsWithinOneYear = creditorsWithinOneYearService.get(transactionId, companyAccountsId, NoteType.SMALL_FULL_CREDITORS_WITHIN_ONE_YEAR);

        CreditorsAfterOneYear creditorsAfterOneYear = creditorsAfterOneYearService.getCreditorsAfterOneYear(transactionId, companyAccountsId, companyNumber);

        Debtors debtors = debtorsService.get(transactionId, companyAccountsId, NoteType.SMALL_FULL_DEBTORS);

        Employees employees = employeesService.getEmployees(transactionId, companyAccountsId, companyNumber);

        TangibleAssets tangibleAssets =
                tangibleAssetsNoteService.getTangibleAssets(transactionId, companyAccountsId, companyNumber);

        IntangibleAssets intangibleAssets =
                intangibleAssetsNoteService.getIntangibleAssets(transactionId, companyAccountsId, companyNumber);
        
        StocksNote stocks = stocksService.get(transactionId,companyAccountsId, NoteType.SMALL_FULL_STOCKS);

        FixedAssetsInvestments fixedAssetsInvestments = fixedAssetsInvestmentsService.getFixedAssetsInvestments(transactionId, companyAccountsId, companyNumber);

        CurrentAssetsInvestments currentAssetsInvestments =
                currentAssetsInvestmentsService.getCurrentAssetsInvestments(transactionId, companyAccountsId, companyNumber);

        OffBalanceSheetArrangements offBalanceSheetArrangements =
                offBalanceSheetArrangementsService.get(transactionId, companyAccountsId, NoteType.SMALL_FULL_OFF_BALANCE_SHEET_ARRANGEMENTS);

        Review review = new Review();
        review.setProfitAndLoss(profitAndLoss);
        review.setBalanceSheet(balanceSheet);
        review.setStatements(statements);
        review.setBasisOfPreparation(basisOfPreparation);
        review.setTurnoverPolicy(turnoverPolicy);
        review.setTangibleDepreciationPolicy(tangibleDepreciationPolicy);
        review.setIntangibleAmortisationPolicy(intangibleAmortisationPolicy);
        review.setValuationInformationPolicy(valuationInformationPolicy);
        review.setOtherAccountingPolicy(otherAccountingPolicy);
        review.setCreditorsWithinOneYear(creditorsWithinOneYear);
        review.setCreditorsAfterOneYear(creditorsAfterOneYear);
        review.setDebtors(debtors);
        review.setEmployees(employees);
        review.setTangibleAssets(tangibleAssets);
        review.setIntangibleAssets(intangibleAssets);
        review.setStocks(stocks);
        review.setFixedAssetsInvestments(fixedAssetsInvestments);
        review.setCurrentAssetsInvestments(currentAssetsInvestments);
        review.setOffBalanceSheetArrangements(offBalanceSheetArrangements);

        return review;
    }
}
