package uk.gov.companieshouse.web.accounts.transformer.smallfull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import uk.gov.companieshouse.api.model.accounts.smallfull.currentassetsinvestments.CurrentAssetsInvestmentsApi;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.CurrentAssetsInvestments;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.impl.CurrentAssetsInvestmentsTransformerImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class CurrentAssetsInvestmentsTransformerImplTests {

    private static final String TEST_DETAILS = "test details";

    private CurrentAssetsInvestmentsTransformer transformer = new CurrentAssetsInvestmentsTransformerImpl();

    @Test
    @DisplayName("Transform api model to web model")
    void transformCurrentAssetsInvestmentsApiToWeb() {
        CurrentAssetsInvestmentsApi currentAssetsInvestmentsApi = new CurrentAssetsInvestmentsApi();
        currentAssetsInvestmentsApi.setDetails(TEST_DETAILS);

        CurrentAssetsInvestments currentAssetsInvestments =
            transformer.getCurrentAssetsInvestments(currentAssetsInvestmentsApi);

        assertNotNull(currentAssetsInvestments);
        assertNotNull(currentAssetsInvestments.getDetails());
        assertEquals(TEST_DETAILS, currentAssetsInvestments.getDetails());
    }

    @Test
    @DisplayName("Transform api model to web model when api model is null")
    void transformNullCurrentAssetsInvestmentsApiToWeb() {
        CurrentAssetsInvestments currentAssetsInvestments =
            transformer.getCurrentAssetsInvestments(null);

        assertNotNull(currentAssetsInvestments);
        assertNull(currentAssetsInvestments.getDetails());
    }

    @Test
    @DisplayName("Transform web model to api model")
    void transformCurrentAssetsInvestmentsWebToApi() {
        CurrentAssetsInvestments currentAssetsInvestments = new CurrentAssetsInvestments();
        currentAssetsInvestments.setDetails(TEST_DETAILS);

        CurrentAssetsInvestmentsApi currentAssetsInvestmentsApi =
            transformer.getCurrentAssetsInvestmentsApi(currentAssetsInvestments);

        assertNotNull(currentAssetsInvestmentsApi);
        assertNotNull(currentAssetsInvestmentsApi.getDetails());
        assertEquals(TEST_DETAILS, currentAssetsInvestmentsApi.getDetails());
    }
}
