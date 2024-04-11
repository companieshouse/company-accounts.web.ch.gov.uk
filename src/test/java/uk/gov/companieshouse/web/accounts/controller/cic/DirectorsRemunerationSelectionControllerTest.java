package uk.gov.companieshouse.web.accounts.controller.cic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.UrlBasedViewResolver;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.cic.statements.DirectorsRemunerationSelection;
import uk.gov.companieshouse.web.accounts.model.state.CicStatements;
import uk.gov.companieshouse.web.accounts.model.state.CompanyAccountsDataState;
import uk.gov.companieshouse.web.accounts.service.cic.statements.DirectorsRemunerationSelectionService;
import uk.gov.companieshouse.web.accounts.service.navigation.NavigatorService;

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

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DirectorsRemunerationSelectionControllerTest {
    @Captor
    private ArgumentCaptor<String[]> captor = ArgumentCaptor.forClass(String[].class);

    private MockMvc mockMvc;

    @Mock
    private DirectorsRemunerationSelectionService selectionService;

    @Mock
    private NavigatorService navigatorService;

    @Mock
    private DirectorsRemunerationSelection selection;

    @Mock
    private CompanyAccountsDataState companyAccountsDataState;

    @Mock
    private CicStatements cicStatements;

    @InjectMocks
    private DirectorsRemunerationSelectionController selectionController;

    private static final String COMPANY_NUMBER = "companyNumber";
    private static final String TRANSACTION_ID = "transactionId";
    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";

    private static final String DIRECTORS_REMUNERATION_SELECTION_PATH = "/company/" + COMPANY_NUMBER +
                                                                                "/transaction/" + TRANSACTION_ID +
                                                                                "/company-accounts/" + COMPANY_ACCOUNTS_ID +
                                                                                "/cic/directors-remuneration-selection";

    private static final String TEMPLATE_NAME_MODEL_ATTR = "templateName";

    private static final String DIRECTORS_REMUNERATION_SELECTION_MODEL_ATTR = "directorsRemunerationSelection";

    private static final String DIRECTORS_REMUNERATION_SELECTION_VIEW = "cic/directorsRemunerationSelection";

    private static final String COMPANY_ACCOUNTS_STATE = "companyAccountsDataState";

    private static final boolean HAS_PROVIDED_DIRECTORS_REMUNERATION = false;

    private static final String ERROR_VIEW = "error";

    private static final String MOCK_CONTROLLER_PATH = UrlBasedViewResolver.REDIRECT_URL_PREFIX + "mockControllerPath";

    @BeforeEach
    public void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(selectionController).build();
    }

    @Test
    @DisplayName("Get directors remuneration selection - success path")
    void getDirectorsRemunerationSelectionSuccess() throws Exception {
        when(selectionService.getDirectorsRemunerationSelection(TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(selection);

        when(selection.getHasProvidedDirectorsRemuneration())
                .thenReturn(true);

        this.mockMvc.perform(get(DIRECTORS_REMUNERATION_SELECTION_PATH))
            .andExpect(status().isOk())
            .andExpect(view().name(DIRECTORS_REMUNERATION_SELECTION_VIEW))
            .andExpect(model().attributeExists(TEMPLATE_NAME_MODEL_ATTR))
            .andExpect(model().attributeExists(DIRECTORS_REMUNERATION_SELECTION_MODEL_ATTR));

        verify(selection, never()).setHasProvidedDirectorsRemuneration(anyBoolean());
    }

    @Test
    @DisplayName("Get directors remuneration selection - derived from cache")
    void getDirectorsRemunerationSelectionDerivedFromCache() throws Exception {
        when(selectionService.getDirectorsRemunerationSelection(TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(selection);

        when(selection.getHasProvidedDirectorsRemuneration())
                .thenReturn(null);

        MockHttpSession session = new MockHttpSession();
        session.setAttribute(COMPANY_ACCOUNTS_STATE, companyAccountsDataState);

        when(companyAccountsDataState.getCicStatements()).thenReturn(cicStatements);

        when(cicStatements.getHasProvidedDirectorsRemuneration())
                .thenReturn(HAS_PROVIDED_DIRECTORS_REMUNERATION);

        this.mockMvc.perform(get(DIRECTORS_REMUNERATION_SELECTION_PATH).session(session))
                .andExpect(status().isOk())
                .andExpect(view().name(DIRECTORS_REMUNERATION_SELECTION_VIEW))
                .andExpect(model().attributeExists(TEMPLATE_NAME_MODEL_ATTR))
                .andExpect(model().attributeExists(DIRECTORS_REMUNERATION_SELECTION_MODEL_ATTR));

        verify(selection).setHasProvidedDirectorsRemuneration(
                HAS_PROVIDED_DIRECTORS_REMUNERATION);
    }

    @Test
    @DisplayName("Get directors remuneration selection - service exception")
    void getDirectorsRemunerationSelectionServiceException() throws Exception {
        when(selectionService.getDirectorsRemunerationSelection(TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenThrow(ServiceException.class);

        this.mockMvc.perform(get(DIRECTORS_REMUNERATION_SELECTION_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name(ERROR_VIEW));
    }

    @Test
    @DisplayName("Post directors remuneration selection - success path")
    void postDirectorsRemunerationSelectionSuccess() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute(COMPANY_ACCOUNTS_STATE, companyAccountsDataState);

        when(companyAccountsDataState.getCicStatements()).thenReturn(cicStatements);

        when(navigatorService.getNextControllerRedirect(any(), captor.capture())).thenReturn(MOCK_CONTROLLER_PATH);

        this.mockMvc.perform(createPostRequestWithParam(HAS_PROVIDED_DIRECTORS_REMUNERATION).session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name(MOCK_CONTROLLER_PATH));

        verify(selectionService).submitDirectorsRemunerationSelection(
                eq(TRANSACTION_ID), eq(COMPANY_ACCOUNTS_ID), any(DirectorsRemunerationSelection.class));

        verify(cicStatements).setHasProvidedDirectorsRemuneration(HAS_PROVIDED_DIRECTORS_REMUNERATION);
    }

    @Test
    @DisplayName("Post directors remuneration selection - binding errors")
    void postDirectorsRemunerationSelectionBindingErrors() throws Exception {
        this.mockMvc.perform(createPostRequestWithParam(null))
                .andExpect(status().isOk())
                .andExpect(view().name(DIRECTORS_REMUNERATION_SELECTION_VIEW));

        verify(selectionService, never()).submitDirectorsRemunerationSelection(
                eq(TRANSACTION_ID), eq(COMPANY_ACCOUNTS_ID), any(DirectorsRemunerationSelection.class));
    }

    @Test
    @DisplayName("Post directors remuneration selection - service exception")
    void postDirectorsRemunerationSelectionServiceException() throws Exception {
        doThrow(ServiceException.class)
                .when(selectionService).submitDirectorsRemunerationSelection(
                        eq(TRANSACTION_ID), eq(COMPANY_ACCOUNTS_ID), any(DirectorsRemunerationSelection.class));

        this.mockMvc.perform(createPostRequestWithParam(HAS_PROVIDED_DIRECTORS_REMUNERATION))
                .andExpect(status().isOk())
                .andExpect(view().name(ERROR_VIEW));
    }

    private MockHttpServletRequestBuilder createPostRequestWithParam(Boolean hasProvidedDirectorsRemuneration) {
        String beanElement = "hasProvidedDirectorsRemuneration";
        String data = hasProvidedDirectorsRemuneration == null ? null : "0";

        return post(DIRECTORS_REMUNERATION_SELECTION_PATH).param(beanElement, data);
    }
}
