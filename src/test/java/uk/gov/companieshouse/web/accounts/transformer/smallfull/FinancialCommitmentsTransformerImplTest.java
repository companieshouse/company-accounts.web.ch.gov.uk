package uk.gov.companieshouse.web.accounts.transformer.smallfull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import uk.gov.companieshouse.api.handler.smallfull.financialcommitments.FinancialCommitmentsApi;
import uk.gov.companieshouse.web.accounts.enumeration.NoteType;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.financialcommitments.FinancialCommitments;
import uk.gov.companieshouse.web.accounts.transformer.NoteTransformer;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.impl.FinancialCommitmentsTransformerImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class FinancialCommitmentsTransformerImplTest {

    private static final String DETAILS = "details";

    private NoteTransformer<FinancialCommitments, FinancialCommitmentsApi> transformer = new FinancialCommitmentsTransformerImpl();

    @Test
    @DisplayName("Get financial commitments")
    void getFinancialCommitments() {

        FinancialCommitmentsApi financialCommitmentsApi = new FinancialCommitmentsApi();
        financialCommitmentsApi.setDetails(DETAILS);

        FinancialCommitments commitments = transformer.toWeb(financialCommitmentsApi);

        assertNotNull(commitments);
        assertEquals(DETAILS, commitments.getFinancialCommitmentsDetails());
    }

    @Test
    @DisplayName("Get financial commitments api")
    void getFinancialCommitmentsApi() {

        FinancialCommitments commitments = new FinancialCommitments();
        commitments.setFinancialCommitmentsDetails(DETAILS);

        FinancialCommitmentsApi financialCommitmentsApi = transformer.toApi(commitments);

        assertNotNull(financialCommitmentsApi);
        assertEquals(DETAILS, financialCommitmentsApi.getDetails());
    }

    @Test
    @DisplayName("Get note type")
    void getNoteType() {

        assertEquals(NoteType.SMALL_FULL_FINANCIAL_COMMITMENTS,
                        transformer.getNoteType());
    }
}
