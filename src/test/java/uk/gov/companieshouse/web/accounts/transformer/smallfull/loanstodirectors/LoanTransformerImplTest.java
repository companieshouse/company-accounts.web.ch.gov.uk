package uk.gov.companieshouse.web.accounts.transformer.smallfull.loanstodirectors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.api.model.accounts.smallfull.loanstodirectors.LoanApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.loanstodirectors.LoanLinks;
import uk.gov.companieshouse.web.accounts.model.loanstodirectors.Loan;
import uk.gov.companieshouse.web.accounts.model.loanstodirectors.LoanToAdd;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class LoanTransformerImplTest {
    @Mock
    private BreakdownTransformer breakdownTransformer;

    @Mock
    private LoanToAdd loanToAdd;

    @InjectMocks
    private LoanTransformer loanTransformer = new LoanTransformerImpl();

    private static final String NAME = "name";

    private static final String DESCRIPTION = "description";

    private static final String LOAN_ID = "loanId";

    private static final String LOAN_SELF_LINK =
            "/transactions/transactionId/company-accounts/companyAccountsId/small-full/notes/loans-to-directors/loans/" + LOAN_ID;

    @Test
    @DisplayName("Get loan API - no breakdown")
    void getDirectorApiNoDates() {
        LoanToAdd loanToAdd = new LoanToAdd();
        loanToAdd.setDirectorName(NAME);
        loanToAdd.setDescription(DESCRIPTION);

        LoanApi loanApi = loanTransformer.getLoanApi(loanToAdd);

        assertNotNull(loanApi);
        assertEquals(NAME, loanApi.getDirectorName());
        assertEquals(DESCRIPTION, loanApi.getDescription());
        assertNull(loanApi.getBreakdown());
    }

    @Test
    @DisplayName("Get all loans")
    void getAllLoans() {
        LoanApi loanApi = new LoanApi();
        loanApi.setDirectorName(NAME);
        loanApi.setDescription(DESCRIPTION);
        loanApi.setBreakdown(breakdownTransformer.mapLoanBreakdownToApi(loanToAdd));

        LoanLinks loanLinks = new LoanLinks();
        loanLinks.setSelf(LOAN_SELF_LINK);

        loanApi.setLinks(loanLinks);

        Loan[] allLoans = loanTransformer.getAllLoans(new LoanApi[]{loanApi});

        assertEquals(1, allLoans.length);
        assertEquals(NAME, allLoans[0].getDirectorName());
        assertEquals(DESCRIPTION, allLoans[0].getDescription());
        assertEquals(loanToAdd.getBreakdown(), allLoans[0].getBreakdown());
        assertEquals(LOAN_ID, allLoans[0].getId());
    }
}