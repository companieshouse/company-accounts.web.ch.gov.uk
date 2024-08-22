package uk.gov.companieshouse.web.accounts.transformer.smallfull;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.api.model.accounts.smallfull.offBalanceSheet.OffBalanceSheetApi;
import uk.gov.companieshouse.web.accounts.enumeration.NoteType;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.offbalancesheetarrangements.OffBalanceSheetArrangements;
import uk.gov.companieshouse.web.accounts.transformer.NoteTransformer;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.impl.OffBalanceSheetArrangementsTransformerImpl;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class OffBalanceSheetArrangementsTransformerImplTest {

    private static final String DETAILS = "details";

    private final NoteTransformer<OffBalanceSheetArrangements, OffBalanceSheetApi> transformer = new OffBalanceSheetArrangementsTransformerImpl();

    @Test
    @DisplayName("Get off balance sheet arrangements")
    void getOffBalanceSheetArrangements() {

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

    @Test
    @DisplayName("Get note type")
    void getNoteType() {

        assertEquals(NoteType.SMALL_FULL_OFF_BALANCE_SHEET_ARRANGEMENTS,
                transformer.getNoteType());
    }
}
