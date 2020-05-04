package uk.gov.companieshouse.web.accounts.transformer.smallfull;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import uk.gov.companieshouse.api.model.accounts.smallfull.offBalanceSheet.OffBalanceSheetApi;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.offbalancesheetarrangements.OffBalanceSheetArrangements;
import uk.gov.companieshouse.web.accounts.transformer.NoteTransformer;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.impl.OffBalanceSheetArrangementsTransformerImpl;

public class OffBalanceSheetArrangementsTransformerImplTest {

    private static final String DETAILS = "details";

    private NoteTransformer<OffBalanceSheetArrangements, OffBalanceSheetApi> transformer = new OffBalanceSheetArrangementsTransformerImpl();

    @Test
    @DisplayName("Get off balance sheet arrangements - null api resource")
    void getOffBalanceSheetArrangementsForNullApiResource() {

        OffBalanceSheetArrangements arrangements = transformer.toWeb(null);

        assertNotNull(arrangements);
        assertNull(arrangements.getOffBalanceSheetArrangementsDetails());
    }

    @Test
    @DisplayName("Get off balance sheet arrangements - existing api resource")
    void getOffBalanceSheetArrangementsForExistingApiResource() {

        OffBalanceSheetApi offBalanceSheetApi = new OffBalanceSheetApi();
        offBalanceSheetApi.setDetails(DETAILS);

        OffBalanceSheetArrangements arrangements = transformer.toWeb(offBalanceSheetApi);

        assertNotNull(arrangements);
        assertEquals(DETAILS, arrangements.getOffBalanceSheetArrangementsDetails());
    }

    @Test
    @DisplayName("Get off balance sheet arrangements api")
    void getOffBalanceSheetArrangementsApi() {

        OffBalanceSheetArrangements arrangements = new OffBalanceSheetArrangements();
        arrangements.setOffBalanceSheetArrangementsDetails(DETAILS);

        OffBalanceSheetApi offBalanceSheetApi = transformer.toApi(arrangements);

        assertNotNull(offBalanceSheetApi);
        assertEquals(DETAILS, offBalanceSheetApi.getDetails());
    }
}
