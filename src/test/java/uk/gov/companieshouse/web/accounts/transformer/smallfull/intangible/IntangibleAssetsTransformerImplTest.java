package uk.gov.companieshouse.web.accounts.transformer.smallfull.intangible;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.api.model.accounts.smallfull.intangible.IntangibleApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.intangible.IntangibleAssetsResource;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.intangible.IntangibleAssets;
import uk.gov.companieshouse.web.accounts.transformer.NoteTransformer;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.intangible.impl.IntangibleAssetsTransformerImpl;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class IntangibleAssetsTransformerImplTest {

    @Mock
    private IntangibleAssetsTransformerFactory factory;

    @Mock
    private IntangibleAssetsResourceTransformer resourceTransformer;

    @InjectMocks
    private NoteTransformer<IntangibleAssets, IntangibleApi> transformer = new IntangibleAssetsTransformerImpl();

    @Test
    @DisplayName("Asserts that the goodwill transformer is called when its api resource is not null")
    void toWebGoodwillTransformerCalled() {

        IntangibleApi intangibleApi = new IntangibleApi();
        IntangibleAssetsResource goodwill = new IntangibleAssetsResource();
        intangibleApi.setGoodwill(goodwill);

        when(factory.getResourceTransformer(
                uk.gov.companieshouse.web.accounts.enumeration.IntangibleAssetsResource.GOODWILL))
                .thenReturn(resourceTransformer);

        IntangibleAssets intangibleAssets = transformer.toWeb(intangibleApi);

        assertNotNull(intangibleAssets);

        verify(resourceTransformer, times(1))
                .mapIntangibleAssetsResourceToWebModel(intangibleAssets, goodwill);
    }

    @Test
    @DisplayName("Asserts that the otherIntangibleAssets transformer is called when its api resource is not null")
    void toWebOtherIntangibleAssetsTransformerCalled() {

        IntangibleApi intangibleApi = new IntangibleApi();
        IntangibleAssetsResource otherIntangibleAssets = new IntangibleAssetsResource();
        intangibleApi.setOtherIntangibleAssets(otherIntangibleAssets);

        when(factory.getResourceTransformer(
                uk.gov.companieshouse.web.accounts.enumeration.IntangibleAssetsResource.OTHER_INTANGIBLE_ASSETS))
                .thenReturn(resourceTransformer);

        IntangibleAssets intangibleAssets = transformer.toWeb(intangibleApi);

        assertNotNull(intangibleAssets);

        verify(resourceTransformer, times(1))
                .mapIntangibleAssetsResourceToWebModel(intangibleAssets, otherIntangibleAssets);
    }

    @Test
    @DisplayName("Asserts that the total transformer is called when its api resource is not null")
    void toWebTotalTransformerCalled() {

        IntangibleApi intangibleApi = new IntangibleApi();
        IntangibleAssetsResource total = new IntangibleAssetsResource();
        intangibleApi.setTotal(total);

        when(factory.getResourceTransformer(
                uk.gov.companieshouse.web.accounts.enumeration.IntangibleAssetsResource.TOTAL))
                .thenReturn(resourceTransformer);

        IntangibleAssets intangibleAssets = transformer.toWeb(intangibleApi);

        assertNotNull(intangibleAssets);

        verify(resourceTransformer, times(1))
                .mapIntangibleAssetsResourceToWebModel(intangibleAssets, total);
    }

    @Test
    @DisplayName("Tests that all resource transformers are called when the web model has resources to map")
    void toApiForWithResourcesToMapInWebModel() {

        IntangibleAssets intangibleAssets = new IntangibleAssets();

        when(factory.getResourceTransformer(any(
                uk.gov.companieshouse.web.accounts.enumeration.IntangibleAssetsResource.class)))
                .thenReturn(resourceTransformer);

        when(resourceTransformer.hasIntangibleAssetsToMapToApiResource(intangibleAssets))
                .thenReturn(true);

        IntangibleApi intangibleApi = transformer.toApi(intangibleAssets);

        assertNotNull(intangibleApi);

        verify(resourceTransformer, times(3))
                .mapIntangibleAssetsToApiResource(intangibleAssets, intangibleApi);
    }

    @Test
    @DisplayName("Tests that no resource transformers are called when the web model doesn't have resources to map")
    void toApiForWithoutResourcesToMapInWebModel() {

        IntangibleAssets intangibleAssets = new IntangibleAssets();

        when(factory.getResourceTransformer(any(
                uk.gov.companieshouse.web.accounts.enumeration.IntangibleAssetsResource.class)))
                .thenReturn(resourceTransformer);

        when(resourceTransformer.hasIntangibleAssetsToMapToApiResource(intangibleAssets))
                .thenReturn(false);

        IntangibleApi intangibleApi = transformer.toApi(intangibleAssets);

        assertNotNull(intangibleApi);

        verify(resourceTransformer, never())
                .mapIntangibleAssetsToApiResource(intangibleAssets, intangibleApi);
    }

    @Test
    @DisplayName("Tests that a null api object is transformed into an empty web object")
    void toWebApiForNullApiModel() {
        IntangibleAssets intangibleApi = transformer.toWeb(null);

        assertNotNull(intangibleApi);
        assertNull(intangibleApi.getAdditionalInformation());
        assertNull(intangibleApi.getAmortisation());
        assertNull(intangibleApi.getCost());
        assertNull(intangibleApi.getLastAccountsPeriodEndOn());
        assertNull(intangibleApi.getNetBookValue());
        assertNull(intangibleApi.getNextAccountsPeriodEndOn());
        assertNull(intangibleApi.getNextAccountsPeriodStartOn());

    }
}
