package uk.gov.companieshouse.web.accounts.transformer.smallfull;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.api.model.accounts.smallfull.fixedassetsinvestments.FixedAssetsInvestmentsApi;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.fixedassetsinvestments.FixedAssetsInvestments;
import uk.gov.companieshouse.web.accounts.transformer.NoteTransformer;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.impl.FixedAssetsInvestmentsTransformerImpl;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FixedAssetsInvestmentsTransformerImplTests {

    private static final String TEST_DETAILS = "test details";

    private final NoteTransformer<FixedAssetsInvestments, FixedAssetsInvestmentsApi> transformer = new FixedAssetsInvestmentsTransformerImpl();

    @Test
    @DisplayName("Transform api model to web model")
    void transformFixedAssetsInvestmentsApiToWeb() {
        FixedAssetsInvestmentsApi fixedAssetsInvestmentsApi = new FixedAssetsInvestmentsApi();
        fixedAssetsInvestmentsApi.setDetails(TEST_DETAILS);

        FixedAssetsInvestments fixedAssetsInvestments = transformer.toWeb(
                fixedAssetsInvestmentsApi);

        assertNotNull(fixedAssetsInvestments);
        assertNotNull(fixedAssetsInvestments.getFixedAssetsDetails());
        assertEquals(TEST_DETAILS, fixedAssetsInvestments.getFixedAssetsDetails());
    }

    @Test
    @DisplayName("Transform api model to web model when api model is null")
    void transformNullFixedAssetsInvestmentsApiToWeb() {
        FixedAssetsInvestments fixedAssetsInvestments = transformer.toWeb(null);

        assertNotNull(fixedAssetsInvestments);
        assertNull(fixedAssetsInvestments.getFixedAssetsDetails());
    }

    @Test
    @DisplayName("Transform web model to api model")
    void transformFixedAssetsInvestmentsWebToApi() {
        FixedAssetsInvestments fixedAssetsInvestments = new FixedAssetsInvestments();
        fixedAssetsInvestments.setFixedAssetsDetails(TEST_DETAILS);

        FixedAssetsInvestmentsApi fixedAssetsInvestmentsApi = transformer.toApi(
                fixedAssetsInvestments);

        assertNotNull(fixedAssetsInvestmentsApi);
        assertNotNull(fixedAssetsInvestmentsApi.getDetails());
        assertEquals(TEST_DETAILS, fixedAssetsInvestmentsApi.getDetails());
    }
}