package uk.gov.companieshouse.web.accounts.service.smallfull.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import uk.gov.companieshouse.api.model.accounts.smallfull.SmallFullApi;
import uk.gov.companieshouse.web.accounts.api.ApiClientService;
import uk.gov.companieshouse.web.accounts.enumeration.NoteType;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.profitandloss.ProfitAndLoss;
import uk.gov.companieshouse.web.accounts.model.smallfull.BalanceSheet;
import uk.gov.companieshouse.web.accounts.model.smallfull.Review;
import uk.gov.companieshouse.web.accounts.model.smallfull.Statements;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.accountingpolicies.AccountingPolicies;
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
import uk.gov.companieshouse.web.accounts.service.smallfull.CreditorsAfterOneYearService;
import uk.gov.companieshouse.web.accounts.service.smallfull.FixedAssetsInvestmentsService;
import uk.gov.companieshouse.web.accounts.service.smallfull.ProfitAndLossService;
import uk.gov.companieshouse.web.accounts.service.smallfull.ReviewService;
import uk.gov.companieshouse.web.accounts.service.smallfull.SmallFullService;
import uk.gov.companieshouse.web.accounts.service.smallfull.StatementsService;

@Service
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    private ProfitAndLossService profitAndLossService;

    @Autowired
    private BalanceSheetService balanceSheetService;

    @Autowired
    private StatementsService statementsService;
    
    @Autowired
    private NoteService<CreditorsWithinOneYear> creditorsWithinOneYearService;

    @Autowired
    private CreditorsAfterOneYearService creditorsAfterOneYearService;

    @Autowired
    private NoteService<Debtors> debtorsService;

    @Autowired
    private NoteService<Employees> employeesService;

    @Autowired
    private NoteService<StocksNote> stocksService;

    @Autowired
    private NoteService<TangibleAssets> tangibleAssetsNoteService;

    @Autowired
    private NoteService<IntangibleAssets> intangibleAssetsNoteService;

    @Autowired
    private FixedAssetsInvestmentsService fixedAssetsInvestmentsService;

    @Autowired
    private NoteService<CurrentAssetsInvestments> currentAssetsInvestmentsService;

    @Autowired
    private NoteService<OffBalanceSheetArrangements> offBalanceSheetArrangementsService;

    @Autowired
    private NoteService<AccountingPolicies> accountingPoliciesNoteService;

    @Autowired
    private SmallFullService smallFullService;

    @Autowired
    private ApiClientService apiClientService;

    public Review getReview(String transactionId, String companyAccountsId, String companyNumber) throws ServiceException {

        ProfitAndLoss profitAndLoss = profitAndLossService.getProfitAndLoss(transactionId, companyAccountsId, companyNumber);

        BalanceSheet balanceSheet = balanceSheetService.getBalanceSheet(transactionId, companyAccountsId, companyNumber);

        Statements statements = statementsService.getBalanceSheetStatements(transactionId, companyAccountsId);

        BasisOfPreparation basisOfPreparation = accountingPoliciesNoteService.get(transactionId, companyAccountsId, NoteType.SMALL_FULL_ACCOUNTING_POLICIES)
                .getBasisOfPreparation();

        TurnoverPolicy turnoverPolicy = accountingPoliciesNoteService.get(transactionId, companyAccountsId, NoteType.SMALL_FULL_ACCOUNTING_POLICIES)
                .getTurnoverPolicy();

        TangibleDepreciationPolicy tangibleDepreciationPolicy = accountingPoliciesNoteService.get(transactionId, companyAccountsId, NoteType.SMALL_FULL_ACCOUNTING_POLICIES)
                .getTangibleDepreciationPolicy();

        IntangibleAmortisationPolicy intangibleAmortisationPolicy = accountingPoliciesNoteService.get(transactionId, companyAccountsId, NoteType.SMALL_FULL_ACCOUNTING_POLICIES)
                .getIntangibleAmortisationPolicy();

        ValuationInformationPolicy valuationInformationPolicy = accountingPoliciesNoteService.get(transactionId, companyAccountsId, NoteType.SMALL_FULL_ACCOUNTING_POLICIES)
                .getValuationInformationPolicy();

        OtherAccountingPolicy otherAccountingPolicy = accountingPoliciesNoteService.get(transactionId, companyAccountsId, NoteType.SMALL_FULL_ACCOUNTING_POLICIES)
                .getOtherAccountingPolicy();
        
        CreditorsWithinOneYear creditorsWithinOneYear = creditorsWithinOneYearService.get(transactionId, companyAccountsId, NoteType.SMALL_FULL_CREDITORS_WITHIN_ONE_YEAR);

        CreditorsAfterOneYear creditorsAfterOneYear = creditorsAfterOneYearService.getCreditorsAfterOneYear(transactionId, companyAccountsId, companyNumber);

        Debtors debtors = debtorsService.get(transactionId, companyAccountsId, NoteType.SMALL_FULL_DEBTORS);

        Employees employees = employeesService.get(transactionId, companyAccountsId, NoteType.SMALL_FULL_EMPLOYEES);

        TangibleAssets tangibleAssets =
                tangibleAssetsNoteService.get(transactionId, companyAccountsId, NoteType.SMALL_FULL_TANGIBLE_ASSETS);

        IntangibleAssets intangibleAssets =
                intangibleAssetsNoteService.get(transactionId, companyAccountsId, NoteType.SMALL_FULL_INTANGIBLE_ASSETS);
        
        StocksNote stocks = stocksService.get(transactionId,companyAccountsId, NoteType.SMALL_FULL_STOCKS);

        FixedAssetsInvestments fixedAssetsInvestments = fixedAssetsInvestmentsService.getFixedAssetsInvestments(transactionId, companyAccountsId, companyNumber);

        CurrentAssetsInvestments currentAssetsInvestments =
                currentAssetsInvestmentsService.get(transactionId, companyAccountsId, NoteType.SMALL_FULL_CURRENT_ASSETS_INVESTMENTS);

        OffBalanceSheetArrangements offBalanceSheetArrangements =
                offBalanceSheetArrangementsService.get(transactionId, companyAccountsId, NoteType.SMALL_FULL_OFF_BALANCE_SHEET_ARRANGEMENTS);

        SmallFullApi smallFullApi = smallFullService.getSmallFullAccounts(apiClientService.getApiClient(), transactionId, companyAccountsId);

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
        review.setPeriodStartOn(smallFullApi.getNextAccounts().getPeriodStartOn());
        review.setPeriodEndOn(smallFullApi.getNextAccounts().getPeriodEndOn());

        return review;
    }
}
