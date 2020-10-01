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
import uk.gov.companieshouse.web.accounts.model.cic.statements.ConsultationWithStakeholdersSelection;
import uk.gov.companieshouse.web.accounts.model.state.CicStatements;
import uk.gov.companieshouse.web.accounts.model.state.CompanyAccountsDataState;
import uk.gov.companieshouse.web.accounts.service.cic.statements.ConsultationWithStakeholdersSelectionService;
import uk.gov.companieshouse.web.accounts.service.navigation.NavigatorService;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ConsultationWithStakeholdersSelectionControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ConsultationWithStakeholdersSelectionService selectionService;

    @Mock
    private NavigatorService navigatorService;

    @Mock
    private ConsultationWithStakeholdersSelection selection;

    @Mock
    private CompanyAccountsDataState companyAccountsDataState;

    @Mock
    private CicStatements cicStatements;

    @InjectMocks
    private ConsultationWithStakeholdersSelectionController selectionController;

    private static final String COMPANY_NUMBER = "companyNumber";
    private static final String TRANSACTION_ID = "transactionId";
    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";

    private static final String CONSULTATION_WITH_STAKEHOLDERS_SELECTION_PATH = "/company/" + COMPANY_NUMBER +
                                                                                "/transaction/" + TRANSACTION_ID +
                                                                                "/company-accounts/" + COMPANY_ACCOUNTS_ID +
                                                                                "/cic/consultation-with-stakeholders-selection";

    private static final String TEMPLATE_NAME_MODEL_ATTR = "templateName";

    private static final String CONSULTATION_WITH_STAKEHOLDERS_SELECTION_MODEL_ATTR = "consultationWithStakeholdersSelection";

    private static final String CONSULTATION_WITH_STAKEHOLDERS_SELECTION_VIEW = "cic/consultationWithStakeholdersSelection";

    private static final String COMPANY_ACCOUNTS_STATE = "companyAccountsDataState";

    private static final boolean HAS_PROVIDED_CONSULTATION_WITH_STAKEHOLDERS = false;

    private static final String ERROR_VIEW = "error";

    private static final String MOCK_CONTROLLER_PATH = UrlBasedViewResolver.REDIRECT_URL_PREFIX + "mockControllerPath";

    @BeforeEach
    private void setup() {

        this.mockMvc = MockMvcBuilders.standaloneSetup(selectionController).build();
    }

    @Test
    @DisplayName("Get consultation with stakeholders selection - success path")
    void getConsultationWithStakeHoldersSelectionSuccess() throws Exception {

        when(selectionService.getConsultationWithStakeholdersSelection(TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(selection);

        when(selection.getHasProvidedConsultationWithStakeholders())
                .thenReturn(true);

        this.mockMvc.perform(get(CONSULTATION_WITH_STAKEHOLDERS_SELECTION_PATH))
            .andExpect(status().isOk())
            .andExpect(view().name(CONSULTATION_WITH_STAKEHOLDERS_SELECTION_VIEW))
            .andExpect(model().attributeExists(TEMPLATE_NAME_MODEL_ATTR))
            .andExpect(model().attributeExists(CONSULTATION_WITH_STAKEHOLDERS_SELECTION_MODEL_ATTR));

        verify(selection, never()).setHasProvidedConsultationWithStakeholders(anyBoolean());
    }

    @Test
    @DisplayName("Get consultation with stakeholders selection - derived from cache")
    void getConsultationWithStakeHoldersSelectionDerivedFromCache() throws Exception {

        when(selectionService.getConsultationWithStakeholdersSelection(TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(selection);

        when(selection.getHasProvidedConsultationWithStakeholders())
                .thenReturn(null);

        MockHttpSession session = new MockHttpSession();
        session.setAttribute(COMPANY_ACCOUNTS_STATE, companyAccountsDataState);

        when(companyAccountsDataState.getCicStatements()).thenReturn(cicStatements);

        when(cicStatements.getHasProvidedConsultationWithStakeholders())
                .thenReturn(HAS_PROVIDED_CONSULTATION_WITH_STAKEHOLDERS);

        this.mockMvc.perform(get(CONSULTATION_WITH_STAKEHOLDERS_SELECTION_PATH).session(session))
                .andExpect(status().isOk())
                .andExpect(view().name(CONSULTATION_WITH_STAKEHOLDERS_SELECTION_VIEW))
                .andExpect(model().attributeExists(TEMPLATE_NAME_MODEL_ATTR))
                .andExpect(model().attributeExists(CONSULTATION_WITH_STAKEHOLDERS_SELECTION_MODEL_ATTR));

        verify(selection).setHasProvidedConsultationWithStakeholders(
                HAS_PROVIDED_CONSULTATION_WITH_STAKEHOLDERS);
    }

    @Test
    @DisplayName("Get consultation with stakeholders selection - service exception")
    void getConsultationWithStakeHoldersSelectionServiceException() throws Exception {

        when(selectionService.getConsultationWithStakeholdersSelection(TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenThrow(ServiceException.class);

        this.mockMvc.perform(get(CONSULTATION_WITH_STAKEHOLDERS_SELECTION_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name(ERROR_VIEW));
    }

    @Test
    @DisplayName("Post consultation with stakeholders selection - success path")
    void postConsultationWithStakeHoldersSelectionSuccess() throws Exception {

        MockHttpSession session = new MockHttpSession();
        session.setAttribute(COMPANY_ACCOUNTS_STATE, companyAccountsDataState);

        when(companyAccountsDataState.getCicStatements()).thenReturn(cicStatements);

        when(navigatorService.getNextControllerRedirect(any(), ArgumentMatchers.<String>any())).thenReturn(MOCK_CONTROLLER_PATH);

        this.mockMvc.perform(createPostRequestWithParam(HAS_PROVIDED_CONSULTATION_WITH_STAKEHOLDERS).session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name(MOCK_CONTROLLER_PATH));

        verify(selectionService).submitConsultationWithStakeholdersSelection(
                eq(TRANSACTION_ID), eq(COMPANY_ACCOUNTS_ID), any(ConsultationWithStakeholdersSelection.class));

        verify(cicStatements).setHasProvidedConsultationWithStakeholders(HAS_PROVIDED_CONSULTATION_WITH_STAKEHOLDERS);
    }

    @Test
    @DisplayName("Post consultation with stakeholders selection - binding errors")
    void postConsultationWithStakeHoldersSelectionBindingErrors() throws Exception {

        this.mockMvc.perform(createPostRequestWithParam(null))
                .andExpect(status().isOk())
                .andExpect(view().name(CONSULTATION_WITH_STAKEHOLDERS_SELECTION_VIEW));

        verify(selectionService, never()).submitConsultationWithStakeholdersSelection(
                eq(TRANSACTION_ID), eq(COMPANY_ACCOUNTS_ID), any(ConsultationWithStakeholdersSelection.class));
    }

    @Test
    @DisplayName("Post consultation with stakeholders selection - service exception")
    void postConsultationWithStakeHoldersSelectionServiceException() throws Exception {

        doThrow(ServiceException.class)
                .when(selectionService).submitConsultationWithStakeholdersSelection(
                        eq(TRANSACTION_ID), eq(COMPANY_ACCOUNTS_ID), any(ConsultationWithStakeholdersSelection.class));

        this.mockMvc.perform(createPostRequestWithParam(HAS_PROVIDED_CONSULTATION_WITH_STAKEHOLDERS))
                .andExpect(status().isOk())
                .andExpect(view().name(ERROR_VIEW));
    }

    private MockHttpServletRequestBuilder createPostRequestWithParam(Boolean hasProvidedConsultationWithStakeholders) {

        String beanElement = "hasProvidedConsultationWithStakeholders";
        String data = hasProvidedConsultationWithStakeholders == null ? null : "0";

        return post(CONSULTATION_WITH_STAKEHOLDERS_SELECTION_PATH).param(beanElement, data);
    }
}
