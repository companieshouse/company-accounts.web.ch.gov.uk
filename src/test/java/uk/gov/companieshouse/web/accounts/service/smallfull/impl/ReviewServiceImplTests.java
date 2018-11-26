package uk.gov.companieshouse.web.accounts.service.smallfull.impl;

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
import uk.gov.companieshouse.web.accounts.service.smallfull.BalanceSheetService;
import uk.gov.companieshouse.web.accounts.service.smallfull.BasisOfPreparationService;
import uk.gov.companieshouse.web.accounts.service.smallfull.StatementsService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

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

    @InjectMocks
    private ReviewServiceImpl reviewService = new ReviewServiceImpl();

    @Test
    @DisplayName("Get Review - Success Path")
    void getReview() throws ServiceException {

        BalanceSheet mockBalanceSheet = new BalanceSheet();
        when(balanceSheetService.getBalanceSheet(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, COMPANY_NUMBER)).thenReturn(mockBalanceSheet);

        Statements mockStatements = new Statements();
        when(statementsService.getBalanceSheetStatements(TRANSACTION_ID, COMPANY_ACCOUNTS_ID)).thenReturn(mockStatements);

        BasisOfPreparation mockBasisOfPreparation = new BasisOfPreparation();
        when(basisOfPreparationService.getBasisOfPreparation(TRANSACTION_ID, COMPANY_ACCOUNTS_ID)).thenReturn(mockBasisOfPreparation);

        Review review = reviewService.getReview(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, COMPANY_NUMBER);

        assertNotNull(review);
        assertEquals(mockBalanceSheet, review.getBalanceSheet());
        assertEquals(mockStatements, review.getStatements());
        assertEquals(mockBasisOfPreparation, review.getBasisOfPreparation());
    }
}
