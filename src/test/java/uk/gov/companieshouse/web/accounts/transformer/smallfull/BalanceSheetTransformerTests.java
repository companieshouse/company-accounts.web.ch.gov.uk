package uk.gov.companieshouse.web.accounts.transformer.smallfull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.gov.companieshouse.web.accounts.model.smallfull.BalanceSheet;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class BalanceSheetTransformerTests {

    private BalanceSheetTransformer transformer;

    @BeforeEach
    private void init() {

        transformer = new BalanceSheetTransformer();
    }

    @Test
    void getBalanceSheetSuccess() {

        BalanceSheet balanceSheet = transformer.getBalanceSheet();

        assertNotNull(balanceSheet);
        assertNotNull(balanceSheet.getCalledUpShareCapitalNotPaid());
    }
}
