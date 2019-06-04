package uk.gov.companieshouse.web.accounts.controller.cic;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.UrlBasedViewResolver;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.cic.statements.TransferOfAssetsSelection;
import uk.gov.companieshouse.web.accounts.model.state.CicStatements;
import uk.gov.companieshouse.web.accounts.model.state.CompanyAccountsDataState;
import uk.gov.companieshouse.web.accounts.service.cic.statements.TransferOfAssetsSelectionService;
import uk.gov.companieshouse.web.accounts.service.navigation.NavigatorService;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TransferOfAssetsSelectionControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TransferOfAssetsSelectionService selectionService;

    @Mock
    private NavigatorService navigatorService;

    @Mock
    private TransferOfAssetsSelection selection;

    @Mock
    private CompanyAccountsDataState companyAccountsDataState;

    @Mock
    private CicStatements cicStatements;

    @InjectMocks
    private TransferOfAssetsSelectionController selectionController;

    private static final String COMPANY_NUMBER = "companyNumber";
    private static final String TRANSACTION_ID = "transactionId";
    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";

    private static final String TRANSFER_OF_ASSETS_SELECTION_PATH = "/company/" + COMPANY_NUMBER +
                                                                                "/transaction/" + TRANSACTION_ID +
                                                                                "/company-accounts/" + COMPANY_ACCOUNTS_ID +
                                                                                "/cic/transfer-of-assets-selection";

    private static final String TEMPLATE_NAME_MODEL_ATTR = "templateName";

    private static final String TRANSFER_OF_ASSETS_SELECTION_MODEL_ATTR = "transferOfAssetsSelection";

    private static final String TRANSFER_OF_ASSETS_SELECTION_VIEW = "cic/transferOfAssetsSelection";

    private static final String COMPANY_ACCOUNTS_STATE = "companyAccountsDataState";

    private static final boolean HAS_PROVIDED_TRANSFER_OF_ASSETS = false;

    private static final String ERROR_VIEW = "error";

    private static final String MOCK_CONTROLLER_PATH = UrlBasedViewResolver.REDIRECT_URL_PREFIX + "mockControllerPath";

    @BeforeEach
    private void setup() {

        this.mockMvc = MockMvcBuilders.standaloneSetup(selectionController).build();
    }

    @Test
    @DisplayName("Get transfer of assets selection - success path")
    void getTransferOfAssetsSelectionSuccess() throws Exception {

        when(selectionService.getTransferOfAssetsSelection(TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(selection);

        when(selection.getHasProvidedTransferOfAssets())
                .thenReturn(true);

        this.mockMvc.perform(get(TRANSFER_OF_ASSETS_SELECTION_PATH))
            .andExpect(status().isOk())
            .andExpect(view().name(TRANSFER_OF_ASSETS_SELECTION_VIEW))
            .andExpect(model().attributeExists(TEMPLATE_NAME_MODEL_ATTR))
            .andExpect(model().attributeExists(TRANSFER_OF_ASSETS_SELECTION_MODEL_ATTR));

        verify(selection, never()).setHasProvidedTransferOfAssets(anyBoolean());
    }

    @Test
    @DisplayName("Get transfer of assets selection - derived from cache")
    void getTransferOfAssetsSelectionDerivedFromCache() throws Exception {

        when(selectionService.getTransferOfAssetsSelection(TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(selection);

        when(selection.getHasProvidedTransferOfAssets())
                .thenReturn(null);

        MockHttpSession session = new MockHttpSession();
        session.setAttribute(COMPANY_ACCOUNTS_STATE, companyAccountsDataState);

        when(companyAccountsDataState.getCicStatements()).thenReturn(cicStatements);

        when(cicStatements.getHasProvidedTransferOfAssets())
                .thenReturn(HAS_PROVIDED_TRANSFER_OF_ASSETS);

        this.mockMvc.perform(get(TRANSFER_OF_ASSETS_SELECTION_PATH).session(session))
                .andExpect(status().isOk())
                .andExpect(view().name(TRANSFER_OF_ASSETS_SELECTION_VIEW))
                .andExpect(model().attributeExists(TEMPLATE_NAME_MODEL_ATTR))
                .andExpect(model().attributeExists(TRANSFER_OF_ASSETS_SELECTION_MODEL_ATTR));

        verify(selection).setHasProvidedTransferOfAssets(
                HAS_PROVIDED_TRANSFER_OF_ASSETS);
    }

    @Test
    @DisplayName("Get transfer of assets selection - service exception")
    void getTransferOfAssetsSelectionServiceException() throws Exception {

        when(selectionService.getTransferOfAssetsSelection(TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenThrow(ServiceException.class);

        this.mockMvc.perform(get(TRANSFER_OF_ASSETS_SELECTION_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name(ERROR_VIEW));
    }

    @Test
    @DisplayName("Post transfer of assets selection - success path")
    void postTransferOfAssetsSelectionSuccess() throws Exception {

        MockHttpSession session = new MockHttpSession();
        session.setAttribute(COMPANY_ACCOUNTS_STATE, companyAccountsDataState);

        when(companyAccountsDataState.getCicStatements()).thenReturn(cicStatements);

        when(navigatorService.getNextControllerRedirect(any(), ArgumentMatchers.<String>any())).thenReturn(MOCK_CONTROLLER_PATH);

        this.mockMvc.perform(createPostRequestWithParam(HAS_PROVIDED_TRANSFER_OF_ASSETS).session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name(MOCK_CONTROLLER_PATH));

        verify(selectionService).submitTransferOfAssetsSelection(
                eq(TRANSACTION_ID), eq(COMPANY_ACCOUNTS_ID), any(TransferOfAssetsSelection.class));

        verify(cicStatements).setHasProvidedTransferOfAssets(HAS_PROVIDED_TRANSFER_OF_ASSETS);
    }

    @Test
    @DisplayName("Post transfer of assets selection - binding errors")
    void postTransferOfAssetsSelectionBindingErrors() throws Exception {

        this.mockMvc.perform(createPostRequestWithParam(null))
                .andExpect(status().isOk())
                .andExpect(view().name(TRANSFER_OF_ASSETS_SELECTION_VIEW));

        verify(selectionService, never()).submitTransferOfAssetsSelection(
                eq(TRANSACTION_ID), eq(COMPANY_ACCOUNTS_ID), any(TransferOfAssetsSelection.class));
    }

    @Test
    @DisplayName("Post transfer of assets selection - service exception")
    void postTransferOfAssetsSelectionServiceException() throws Exception {

        doThrow(ServiceException.class)
                .when(selectionService).submitTransferOfAssetsSelection(
                        eq(TRANSACTION_ID), eq(COMPANY_ACCOUNTS_ID), any(TransferOfAssetsSelection.class));

        this.mockMvc.perform(createPostRequestWithParam(HAS_PROVIDED_TRANSFER_OF_ASSETS))
                .andExpect(status().isOk())
                .andExpect(view().name(ERROR_VIEW));
    }

    private MockHttpServletRequestBuilder createPostRequestWithParam(Boolean hasProvidedTransferOfAssets) {

        String beanElement = "hasProvidedTransferOfAssets";
        String data = hasProvidedTransferOfAssets == null ? null : "0";

        return post(TRANSFER_OF_ASSETS_SELECTION_PATH).param(beanElement, data);
    }
}
