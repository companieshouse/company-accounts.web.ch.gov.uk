package uk.gov.companieshouse.web.accounts.transformer.smallfull;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.api.model.accounts.smallfull.currentassetsinvestments.CurrentAssetsInvestmentsApi;
import uk.gov.companieshouse.web.accounts.enumeration.NoteType;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.currentassetsinvestments.CurrentAssetsInvestments;
import uk.gov.companieshouse.web.accounts.transformer.NoteTransformer;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.impl.CurrentAssetsInvestmentsTransformerImpl;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CurrentAssetsInvestmentsTransformerImplTests {

    private static final String TEST_DETAILS = "test details";

    private NoteTransformer<CurrentAssetsInvestments, CurrentAssetsInvestmentsApi> transformer = new CurrentAssetsInvestmentsTransformerImpl();

    @Test
    @DisplayName("Transform api model to web model")
    void transformCurrentAssetsInvestmentsApiToWeb() {
        CurrentAssetsInvestmentsApi currentAssetsInvestmentsApi = new CurrentAssetsInvestmentsApi();
        currentAssetsInvestmentsApi.setDetails(TEST_DETAILS);

        CurrentAssetsInvestments currentAssetsInvestments =
                transformer.toWeb(currentAssetsInvestmentsApi);

        assertNotNull(currentAssetsInvestments);
        assertNotNull(currentAssetsInvestments.getCurrentAssetsInvestmentsDetails());
        assertEquals(TEST_DETAILS, currentAssetsInvestments.getCurrentAssetsInvestmentsDetails());
    }

    @Test
    @DisplayName("Transform api model to web model when api model is null")
    void transformNullCurrentAssetsInvestmentsApiToWeb() {
        CurrentAssetsInvestments currentAssetsInvestments =
                transformer.toWeb(null);

        assertNotNull(currentAssetsInvestments);
        assertNull(currentAssetsInvestments.getCurrentAssetsInvestmentsDetails());
    }

    @Test
    @DisplayName("Transform web model to api model")
    void transformCurrentAssetsInvestmentsWebToApi() {
        CurrentAssetsInvestments currentAssetsInvestments = new CurrentAssetsInvestments();
        currentAssetsInvestments.setCurrentAssetsInvestmentsDetails(TEST_DETAILS);

        CurrentAssetsInvestmentsApi currentAssetsInvestmentsApi =
                transformer.toApi(currentAssetsInvestments);

        assertNotNull(currentAssetsInvestmentsApi);
        assertNotNull(currentAssetsInvestmentsApi.getDetails());
        assertEquals(TEST_DETAILS, currentAssetsInvestmentsApi.getDetails());
    }

    @Test
    @DisplayName("Get note type")
    void getNoteType() {

        assertEquals(NoteType.SMALL_FULL_CURRENT_ASSETS_INVESTMENTS, transformer.getNoteType());
    }
}
