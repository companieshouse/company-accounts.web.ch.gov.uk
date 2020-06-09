package uk.gov.companieshouse.web.accounts.service.smallfull.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
import uk.gov.companieshouse.web.accounts.service.smallfull.FixedAssetsInvestmentsService;
import uk.gov.companieshouse.web.accounts.service.smallfull.IntangibleAmortisationPolicyService;
import uk.gov.companieshouse.web.accounts.service.smallfull.OtherAccountingPolicyService;
import uk.gov.companieshouse.web.accounts.service.smallfull.ProfitAndLossService;
import uk.gov.companieshouse.web.accounts.service.smallfull.StatementsService;
import uk.gov.companieshouse.web.accounts.service.smallfull.TangibleDepreciationPolicyService;
import uk.gov.companieshouse.web.accounts.service.smallfull.TurnoverPolicyService;
import uk.gov.companieshouse.web.accounts.service.smallfull.ValuationInformationPolicyService;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ReviewServiceImplTests {

    private static final String TRANSACTION_ID = "transactionId";

    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";

    private static final String COMPANY_NUMBER = "companyNumber";

    @Mock
    private ProfitAndLossService profitAndLossService;

    @Mock
    private BalanceSheetService balanceSheetService;

    @Mock
    private StatementsService statementsService;

    @Mock
    private BasisOfPreparationService basisOfPreparationService;

    @Mock
    private TurnoverPolicyService turnoverPolicyService;

    @Mock
    private TangibleDepreciationPolicyService tangibleDepreciationPolicyService;

    @Mock
    private IntangibleAmortisationPolicyService intangibleAmortisationPolicyService;

    @Mock
    private ValuationInformationPolicyService valuationInformationPolicyService;

    @Mock
    private OtherAccountingPolicyService otherAccountingPolicyService;
    
    @Mock
    private NoteService<CreditorsWithinOneYear> creditorsWithinOneYearService;

    @Mock
    private CreditorsAfterOneYearService creditorsAfterOneYearService;

    @Mock
    private NoteService<Debtors> debtorsService;

    @Mock
    private NoteService<Employees> employeesService;

    @Mock
    private NoteService<StocksNote> stocksService;

    @Mock
    private NoteService<TangibleAssets> tangibleAssetsNoteService;

    @Mock
    private NoteService<IntangibleAssets> intangibleAssetsNoteService;

    @Mock
    private FixedAssetsInvestmentsService fixedAssetsInvestmentsService;

    @Mock
    private NoteService<CurrentAssetsInvestments> currentAssetsInvestmentsService;

    @Mock
    private NoteService<OffBalanceSheetArrangements> offBalanceSheetArrangementsService;

    @InjectMocks
    private ReviewServiceImpl reviewService = new ReviewServiceImpl();

    @Test
    @DisplayName("Get Review - Success Path")
    void getReview() throws ServiceException {

        ProfitAndLoss mockProfitAndLoss = new ProfitAndLoss();
        when(profitAndLossService.getProfitAndLoss(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, COMPANY_NUMBER))
                .thenReturn(mockProfitAndLoss);

        BalanceSheet mockBalanceSheet = new BalanceSheet();
        when(balanceSheetService.getBalanceSheet(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, COMPANY_NUMBER))
            .thenReturn(mockBalanceSheet);

        Statements mockStatements = new Statements();
        when(statementsService.getBalanceSheetStatements(TRANSACTION_ID, COMPANY_ACCOUNTS_ID)).thenReturn(mockStatements);

        BasisOfPreparation mockBasisOfPreparation = new BasisOfPreparation();
        when(basisOfPreparationService.getBasisOfPreparation(TRANSACTION_ID, COMPANY_ACCOUNTS_ID)).thenReturn(mockBasisOfPreparation);

        TurnoverPolicy mockTurnoverPolicy = new TurnoverPolicy();
        when(turnoverPolicyService.getTurnOverPolicy(TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
            .thenReturn(mockTurnoverPolicy);

        TangibleDepreciationPolicy mockTangibleDepreciationPolicy = new TangibleDepreciationPolicy();
        when(tangibleDepreciationPolicyService.getTangibleDepreciationPolicy(TRANSACTION_ID, COMPANY_ACCOUNTS_ID)).thenReturn(mockTangibleDepreciationPolicy);

        IntangibleAmortisationPolicy mockIntangibleAmortisationPolicy = new IntangibleAmortisationPolicy();
        when(intangibleAmortisationPolicyService.getIntangibleAmortisationPolicy(TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(mockIntangibleAmortisationPolicy);

        ValuationInformationPolicy valuationInformationPolicy = new ValuationInformationPolicy();
        when(valuationInformationPolicyService.getValuationInformationPolicy(TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
            .thenReturn(valuationInformationPolicy);

        OtherAccountingPolicy mockOtherAccounting = new OtherAccountingPolicy();
        when(otherAccountingPolicyService.getOtherAccountingPolicy(TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
            .thenReturn(mockOtherAccounting);
        
        CreditorsWithinOneYear mockCreditorsWithinOneYear = new CreditorsWithinOneYear();
        when(creditorsWithinOneYearService.get(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, NoteType.SMALL_FULL_CREDITORS_WITHIN_ONE_YEAR))
            .thenReturn(mockCreditorsWithinOneYear);

        CreditorsAfterOneYear mockCreditorsAfterOneYear = new CreditorsAfterOneYear();
        when(creditorsAfterOneYearService.getCreditorsAfterOneYear(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, COMPANY_NUMBER))
            .thenReturn(mockCreditorsAfterOneYear);

        Debtors mockDebtors = new Debtors();
        when(debtorsService.get(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, NoteType.SMALL_FULL_DEBTORS)).thenReturn(mockDebtors);

        Employees mockEmployees = new Employees();
        when(employeesService.get(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, NoteType.SMALL_FULL_EMPLOYEES)).thenReturn(mockEmployees);

        StocksNote mockStocks = new StocksNote();
        when(stocksService.get(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, NoteType.SMALL_FULL_STOCKS)).thenReturn(mockStocks);

        TangibleAssets mockTangibleAssets = new TangibleAssets();
        when(tangibleAssetsNoteService.get(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, NoteType.SMALL_FULL_TANGIBLE_ASSETS))
                .thenReturn(mockTangibleAssets);

        IntangibleAssets mockIntangibleAssets = new IntangibleAssets();
        when(intangibleAssetsNoteService.get(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, NoteType.SMALL_FULL_INTANGIBLE))
                .thenReturn(mockIntangibleAssets);
        
        FixedAssetsInvestments mockFixedAssetsInvestments = new FixedAssetsInvestments();
        when(fixedAssetsInvestmentsService.getFixedAssetsInvestments(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, COMPANY_NUMBER))
                .thenReturn(mockFixedAssetsInvestments);

        CurrentAssetsInvestments mockCurrentAssetsInvestments = new CurrentAssetsInvestments();
        when(currentAssetsInvestmentsService.get(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, NoteType.SMALL_FULL_CURRENT_ASSETS_INVESTMENTS))
                .thenReturn(mockCurrentAssetsInvestments);

        OffBalanceSheetArrangements mockOffBalanceSheetArrangements = new OffBalanceSheetArrangements();
        when(offBalanceSheetArrangementsService.get(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, NoteType.SMALL_FULL_OFF_BALANCE_SHEET_ARRANGEMENTS))
                .thenReturn(mockOffBalanceSheetArrangements);

        Review review = reviewService.getReview(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, COMPANY_NUMBER);

        assertNotNull(review);
        assertEquals(mockProfitAndLoss, review.getProfitAndLoss());
        assertEquals(mockBalanceSheet, review.getBalanceSheet());
        assertEquals(mockStatements, review.getStatements());
        assertEquals(mockBasisOfPreparation, review.getBasisOfPreparation());
        assertEquals(mockTurnoverPolicy, review.getTurnoverPolicy());
        assertEquals(mockTangibleDepreciationPolicy, review.getTangibleDepreciationPolicy());
        assertEquals(mockIntangibleAmortisationPolicy, review.getIntangibleAmortisationPolicy());
        assertEquals(valuationInformationPolicy, review.getValuationInformationPolicy());
        assertEquals(mockOtherAccounting, review.getOtherAccountingPolicy());
        assertEquals(mockCreditorsWithinOneYear, review.getCreditorsWithinOneYear());
        assertEquals(mockCreditorsAfterOneYear, review.getCreditorsAfterOneYear());
        assertEquals(mockTangibleAssets, review.getTangibleAssets());
        assertEquals(mockIntangibleAssets, review.getIntangibleAssets());
        assertEquals(mockEmployees, review.getEmployees());
        assertEquals(mockDebtors, review.getDebtors());
        assertEquals(mockStocks, review.getStocks());
        assertEquals(mockFixedAssetsInvestments, review.getFixedAssetsInvestments());
        assertEquals(mockCurrentAssetsInvestments, review.getCurrentAssetsInvestments());
        assertEquals(mockOffBalanceSheetArrangements, review.getOffBalanceSheetArrangements());
    }
}
