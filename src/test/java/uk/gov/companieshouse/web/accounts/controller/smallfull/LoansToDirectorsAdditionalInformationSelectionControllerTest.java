package uk.gov.companieshouse.web.accounts.controller.smallfull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.UrlBasedViewResolver;
import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.model.accounts.smallfull.loanstodirectors.AdditionalInformationApi;
import uk.gov.companieshouse.web.accounts.api.ApiClientService;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.loanstodirectors.AdditionalInformation;
import uk.gov.companieshouse.web.accounts.model.loanstodirectors.AdditionalInformationSelection;
import uk.gov.companieshouse.web.accounts.model.state.CompanyAccountsDataState;
import uk.gov.companieshouse.web.accounts.service.navigation.NavigatorService;
import uk.gov.companieshouse.web.accounts.service.smallfull.impl.LoansToDirectorsAdditionalInformationSelectionServiceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
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
class LoansToDirectorsAdditionalInformationSelectionControllerTest {

    private MockMvc mockMvc;

    @Mock
    private HttpServletRequest request;

    @Mock
    private LoansToDirectorsAdditionalInformationSelectionServiceImpl additionalInformationSelectionService;

    @Mock
    private NavigatorService navigatorService;

    @Mock
    private AdditionalInformationApi additionalInformationApi;

    @Mock
    private ApiClientService apiClientService;

    @Mock
    private ApiClient apiClient;

    @Mock
    private AdditionalInformationSelection additionalInformationSelection;

    @Mock
    private HttpSession httpSession;

    @Mock
    private CompanyAccountsDataState companyAccountsDataState;

    @Mock
    private AdditionalInformation additionalInformation;

    @InjectMocks
    private LoansToDirectorsAdditionalInformationSelectionController controller;

    private static final String COMPANY_NUMBER = "companyNumber";

    private static final String TRANSACTION_ID = "transactionId";

    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";

    private static final String ADDITIONAL_INFORMATION_SELECTION_PATH = "/company/" + COMPANY_NUMBER +
            "/transaction/" + TRANSACTION_ID +
            "/company-accounts/" + COMPANY_ACCOUNTS_ID +
            "/small-full/note/add-or-remove-loans/additional-information-question";

    private static final String ADDITIONAL_INFORMATION_SELECTION_MODEL_ATTR = "additionalInformationSelection";

    private static final String TEMPLATE_NAME_MODEL_ATTR = "templateName";

    private static final String ADDITIONAL_INFORMATION_SELECTION_VIEW = "smallfull/loansToDirectorsAdditionalInformationSelection";

    private static final String ERROR_VIEW = "error";

    private static final String HAS_ADDITIONAL_INFORMATION = "hasAdditionalInformation";

    private static final String MOCK_CONTROLLER_PATH = UrlBasedViewResolver.REDIRECT_URL_PREFIX + "mockControllerPath";

    private static final String COMPANY_ACCOUNTS_DATA_STATE = "companyAccountsDataState";

    @BeforeEach
    private void setup() {

        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    @DisplayName("Get request - success - has additional information set from db")
    void getAdditionalInformationSelectionSuccessHasSelectionSetFromDB() throws Exception {

        when(additionalInformationSelectionService.getAdditionalInformationSelection(TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(additionalInformationSelection);

        when(additionalInformationSelection.getHasAdditionalInformation()).thenReturn(true);

        this.mockMvc.perform(get(ADDITIONAL_INFORMATION_SELECTION_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name(ADDITIONAL_INFORMATION_SELECTION_VIEW))
                .andExpect(model().attributeExists(ADDITIONAL_INFORMATION_SELECTION_MODEL_ATTR))
                .andExpect(model().attributeExists(TEMPLATE_NAME_MODEL_ATTR));

        // verify no interactions with the data state
        verify(request, never()).getSession();
    }

    @Test
    @DisplayName("Get request - success - has additional information derived from data state")
    void getAdditionalInformationSelectionSuccessHasSelectionDerivedFromDataState() throws Exception {

        when(additionalInformationSelectionService.getAdditionalInformationSelection(TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(additionalInformationSelection);

        when(additionalInformationSelection.getHasAdditionalInformation()).thenReturn(null);

        when(request.getSession()).thenReturn(httpSession);
        when(httpSession.getAttribute(COMPANY_ACCOUNTS_DATA_STATE)).thenReturn(companyAccountsDataState);
        when(companyAccountsDataState.getLoansToDirectorsAdditionalInformation()).thenReturn(additionalInformation);
        when(additionalInformation.getHasProvidedAdditionalInformation()).thenReturn(false);

        this.mockMvc.perform(get(ADDITIONAL_INFORMATION_SELECTION_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name(ADDITIONAL_INFORMATION_SELECTION_VIEW))
                .andExpect(model().attributeExists(ADDITIONAL_INFORMATION_SELECTION_MODEL_ATTR))
                .andExpect(model().attributeExists(TEMPLATE_NAME_MODEL_ATTR));

        verify(additionalInformationSelection).setHasAdditionalInformation(false);
    }

    @Test
    @DisplayName("Get request - service exception")
    void getAdditionalInformationSelectionThrowsServiceException() throws Exception {

        when(additionalInformationSelectionService.getAdditionalInformationSelection(TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenThrow(ServiceException.class);

        this.mockMvc.perform(get(ADDITIONAL_INFORMATION_SELECTION_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name(ERROR_VIEW));
    }

    @Test
    @DisplayName("Post request - success")
    void postAdditionalInformationSuccess() throws Exception {

        doNothing()
                .when(additionalInformationSelectionService)
                .submitAdditionalInformationSelection(eq(TRANSACTION_ID), eq(COMPANY_ACCOUNTS_ID), any(AdditionalInformationSelection.class));

        when(request.getSession()).thenReturn(httpSession);
        when(httpSession.getAttribute(COMPANY_ACCOUNTS_DATA_STATE)).thenReturn(companyAccountsDataState);
        when(companyAccountsDataState.getLoansToDirectorsAdditionalInformation()).thenReturn(additionalInformation);

        when(navigatorService.getNextControllerRedirect(any(), ArgumentMatchers.<String>any())).thenReturn(MOCK_CONTROLLER_PATH);

        this.mockMvc.perform(post(ADDITIONAL_INFORMATION_SELECTION_PATH)
                .param(HAS_ADDITIONAL_INFORMATION, "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name(MOCK_CONTROLLER_PATH));

        verify(additionalInformation).setHasProvidedAdditionalInformation(true);

        verify(httpSession).setAttribute(COMPANY_ACCOUNTS_DATA_STATE, companyAccountsDataState);
    }

    @Test
    @DisplayName("Post request - service exception")
    void postAdditionalInformationServiceException() throws Exception {

        doThrow(ServiceException.class)
                .when(additionalInformationSelectionService)
                .submitAdditionalInformationSelection(eq(TRANSACTION_ID), eq(COMPANY_ACCOUNTS_ID), any(AdditionalInformationSelection.class));

        this.mockMvc.perform(post(ADDITIONAL_INFORMATION_SELECTION_PATH)
                .param(HAS_ADDITIONAL_INFORMATION, "1"))
                .andExpect(status().isOk())
                .andExpect(view().name(ERROR_VIEW));
    }

    @Test
    @DisplayName("Post request - binding result errors")
    void postAdditionalInformationWithBindingResultErrors() throws Exception {

        this.mockMvc.perform(post(ADDITIONAL_INFORMATION_SELECTION_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name(ADDITIONAL_INFORMATION_SELECTION_VIEW));

        verify(additionalInformationSelectionService, never())
                .submitAdditionalInformationSelection(eq(TRANSACTION_ID), eq(COMPANY_ACCOUNTS_ID), any(AdditionalInformationSelection.class));
    }

    @Test
    @DisplayName("Will render - true")
    void willRenderTrue() throws ServiceException {
        assertTrue(controller.willRender(COMPANY_NUMBER, TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }
}