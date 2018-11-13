package uk.gov.companieshouse.web.accounts.transformer.smallfull;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.api.model.accounts.smallfull.BalanceSheetLegalStatementsApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.BalanceSheetStatementsApi;
import uk.gov.companieshouse.web.accounts.model.smallfull.Statements;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.impl.StatementsTransformerImpl;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class StatementsTransformerImplTests {

    private StatementsTransformer transformer = new StatementsTransformerImpl();

    private static final String AUDIT_NOT_REQUIRED = "auditNotRequired";

    private static final String DIRECTORS_RESPONSIBILITY = "directorsResponsibility";

    private static final String NO_PROFIT_AND_LOSS = "noProfitAndLoss";

    private static final String SECTION_477 = "section477";

    private static final String SMALL_COMPANIES_REGIME = "smallCompaniesRegime";

    @Test
    @DisplayName("Get statements")
    void getStatements() {

        BalanceSheetLegalStatementsApi legalStatementsApi = new BalanceSheetLegalStatementsApi();
        legalStatementsApi.setAuditNotRequiredByMembers(AUDIT_NOT_REQUIRED);
        legalStatementsApi.setDirectorsResponsibility(DIRECTORS_RESPONSIBILITY);
        legalStatementsApi.setNoProfitAndLoss(NO_PROFIT_AND_LOSS);
        legalStatementsApi.setSection477(SECTION_477);
        legalStatementsApi.setSmallCompaniesRegime(SMALL_COMPANIES_REGIME);

        BalanceSheetStatementsApi statementsApi = new BalanceSheetStatementsApi();
        statementsApi.setLegalStatements(legalStatementsApi);

        Statements statements = transformer.getBalanceSheetStatements(statementsApi);

        assertEquals(AUDIT_NOT_REQUIRED, statements.getAuditNotRequiredByMembers());
        assertEquals(DIRECTORS_RESPONSIBILITY, statements.getDirectorsResponsibility());
        assertEquals(NO_PROFIT_AND_LOSS, statements.getNoProfitAndLoss());
        assertEquals(SECTION_477, statements.getSection477());
        assertEquals(SMALL_COMPANIES_REGIME, statements.getSmallCompaniesRegime());
    }

}
