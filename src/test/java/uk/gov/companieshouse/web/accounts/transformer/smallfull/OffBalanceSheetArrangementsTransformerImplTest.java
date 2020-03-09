package uk.gov.companieshouse.web.accounts.transformer.smallfull;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import uk.gov.companieshouse.api.model.accounts.smallfull.offBalanceSheet.OffBalanceSheetApi;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.offbalancesheetarrangements.OffBalanceSheetArrangements;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.impl.OffBalanceSheetArrangementsTransformerImpl;

public class OffBalanceSheetArrangementsTransformerImplTest {

    private static final String DETAILS = "details";

    private OffBalanceSheetArrangementsTransformer transformer = new OffBalanceSheetArrangementsTransformerImpl();

    @Test
    @DisplayName("Get off balance sheet arrangements - null api resource")
    void getOffBalanceSheetArrangementsForNullApiResource() {

        OffBalanceSheetArrangements arrangements = transformer.getOffBalanceSheetArrangements(null);

        assertNotNull(arrangements);
        assertNull(arrangements.getDetails());
    }

    @Test
    @DisplayName("Get off balance sheet arrangements - existing api resource")
    void getOffBalanceSheetArrangementsForExistingApiResource() {

        OffBalanceSheetApi offBalanceSheetApi = new OffBalanceSheetApi();
        offBalanceSheetApi.setDetails(DETAILS);

        OffBalanceSheetArrangements arrangements = transformer.getOffBalanceSheetArrangements(offBalanceSheetApi);

        assertNotNull(arrangements);
        assertEquals(DETAILS, arrangements.getDetails());
    }

    @Test
    @DisplayName("Get off balance sheet arrangements api")
    void getOffBalanceSheetArrangementsApi() {

        OffBalanceSheetArrangements arrangements = new OffBalanceSheetArrangements();
        arrangements.setDetails(DETAILS);

        OffBalanceSheetApi offBalanceSheetApi = transformer.getOffBalanceSheetArrangementsApi(arrangements);

        assertNotNull(offBalanceSheetApi);
        assertEquals(DETAILS, offBalanceSheetApi.getDetails());
    }
}
