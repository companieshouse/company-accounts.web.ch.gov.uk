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

import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.smallfull.BalanceSheet;
import uk.gov.companieshouse.web.accounts.model.smallfull.Review;
import uk.gov.companieshouse.web.accounts.model.smallfull.Statements;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.accountingpolicies.BasisOfPreparation;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.accountingpolicies.IntangibleAmortisationPolicy;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.accountingpolicies.OtherAccountingPolicy;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.accountingpolicies.TangibleDepreciationPolicy;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.accountingpolicies.TurnoverPolicy;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.accountingpolicies.ValuationInformationPolicy;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.creditorswithinoneyear.CreditorsWithinOneYear;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.debtors.Debtors;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.tangible.TangibleAssets;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.stocks.StocksNote;
import uk.gov.companieshouse.web.accounts.service.smallfull.BalanceSheetService;
import uk.gov.companieshouse.web.accounts.service.smallfull.BasisOfPreparationService;
import uk.gov.companieshouse.web.accounts.service.smallfull.CreditorsWithinOneYearService;
import uk.gov.companieshouse.web.accounts.service.smallfull.DebtorsService;
import uk.gov.companieshouse.web.accounts.service.smallfull.IntangibleAmortisationPolicyService;
import uk.gov.companieshouse.web.accounts.service.smallfull.OtherAccountingPolicyService;
import uk.gov.companieshouse.web.accounts.service.smallfull.StatementsService;
import uk.gov.companieshouse.web.accounts.service.smallfull.TangibleAssetsNoteService;
import uk.gov.companieshouse.web.accounts.service.smallfull.StocksService;
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
    private CreditorsWithinOneYearService creditorsWithinOneYearService;

    @Mock
    private DebtorsService debtorsService;

    @Mock
    private StocksService stocksService;

    @Mock
    private TangibleAssetsNoteService tangibleAssetsNoteService;

    @InjectMocks
    private ReviewServiceImpl reviewService = new ReviewServiceImpl();

    @Test
    @DisplayName("Get Review - Success Path")
    void getReview() throws ServiceException {

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
        when(creditorsWithinOneYearService.getCreditorsWithinOneYear(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, COMPANY_NUMBER))
            .thenReturn(mockCreditorsWithinOneYear);

        Debtors mockDebtors = new Debtors();
        when(debtorsService.getDebtors(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, COMPANY_NUMBER)).thenReturn(mockDebtors);

        StocksNote mockStocks = new StocksNote();
        when(stocksService.getStocks(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, COMPANY_NUMBER)).thenReturn(mockStocks);

        TangibleAssets mockTangibleAssets = new TangibleAssets();
        when(tangibleAssetsNoteService.getTangibleAssets(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, COMPANY_NUMBER))
                .thenReturn(mockTangibleAssets);

        Review review = reviewService.getReview(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, COMPANY_NUMBER);

        assertNotNull(review);
        assertEquals(mockBalanceSheet, review.getBalanceSheet());
        assertEquals(mockStatements, review.getStatements());
        assertEquals(mockBasisOfPreparation, review.getBasisOfPreparation());
        assertEquals(mockTurnoverPolicy, review.getTurnoverPolicy());
        assertEquals(mockTangibleDepreciationPolicy, review.getTangibleDepreciationPolicy());
        assertEquals(mockIntangibleAmortisationPolicy, review.getIntangibleAmortisationPolicy());
        assertEquals(valuationInformationPolicy, review.getValuationInformationPolicy());
        assertEquals(mockOtherAccounting, review.getOtherAccountingPolicy());
        assertEquals(mockCreditorsWithinOneYear, review.getCreditorsWithinOneYear());
        assertEquals(mockTangibleAssets, review.getTangibleAssets());
        assertEquals(mockDebtors, review.getDebtors());
        assertEquals(mockStocks, review.getStocks());
    }
}
