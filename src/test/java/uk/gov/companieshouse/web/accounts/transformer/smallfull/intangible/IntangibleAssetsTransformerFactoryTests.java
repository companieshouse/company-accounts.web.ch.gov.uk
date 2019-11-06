package uk.gov.companieshouse.web.accounts.transformer.smallfull.intangible;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.web.accounts.enumeration.IntangibleAssetsResource;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.intangible.impl.IntangibleAssetsGoodwillTransformerImpl;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.intangible.impl.IntangibleAssetsOtherIntangibleAssetsTransformerImpl;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.intangible.impl.IntangibleAssetsTotalTransformerImpl;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class IntangibleAssetsTransformerFactoryTests {

    private IntangibleAssetsTransformerFactory factory = new IntangibleAssetsTransformerFactory();

    @Test
    @DisplayName("Asserts the factory returns a goodwill transformer when requesting "
            + "with the appropriate resource type")
    void getGoodwillTransformer() {

        IntangibleAssetsResourceTransformer transformer = factory.getResourceTransformer(
                IntangibleAssetsResource.GOODWILL);

        assertTrue(transformer instanceof IntangibleAssetsGoodwillTransformerImpl);
    }

    @Test
    @DisplayName("Asserts the factory returns an other intangible assets transformer when requesting "
            + "with the appropriate resource type")
    void getOtherIntangibleAssetsTransformer() {

        IntangibleAssetsResourceTransformer transformer = factory.getResourceTransformer(
                IntangibleAssetsResource.OTHER_INTANGIBLE_ASSETS);

        assertTrue(transformer instanceof IntangibleAssetsOtherIntangibleAssetsTransformerImpl);
    }

    @Test
    @DisplayName("Asserts the factory returns a total transformer when requesting "
            + "with the appropriate resource type")
    void getTotalTransformer() {

        IntangibleAssetsResourceTransformer transformer = factory.getResourceTransformer(
                IntangibleAssetsResource.TOTAL);

        assertTrue(transformer instanceof IntangibleAssetsTotalTransformerImpl);
    }
}
