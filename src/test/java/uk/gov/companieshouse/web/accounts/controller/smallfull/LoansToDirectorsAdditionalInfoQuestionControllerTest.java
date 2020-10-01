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
import uk.gov.companieshouse.api.model.accounts.smallfull.loanstodirectors.LoansToDirectorsApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.loanstodirectors.LoansToDirectorsLinks;
import uk.gov.companieshouse.web.accounts.api.ApiClientService;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.state.CompanyAccountsDataState;
import uk.gov.companieshouse.web.accounts.service.navigation.NavigatorService;
import uk.gov.companieshouse.web.accounts.service.smallfull.LoansToDirectorsAdditionalInfoService;
import uk.gov.companieshouse.web.accounts.service.smallfull.LoansToDirectorsService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
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
class LoansToDirectorsAdditionalInfoQuestionControllerTest {

    private MockMvc mockMvc;

    @Mock
    private HttpServletRequest request;

    @Mock
    private NavigatorService navigatorService;

    @Mock
    private ApiClientService apiClientService;

    @Mock
    private ApiClient apiClient;

    @Mock
    private LoansToDirectorsService loansToDirectorsService;

    @Mock
    private LoansToDirectorsApi loansToDirectorsApi;

    @Mock
    private HttpSession httpSession;

    @Mock
    private CompanyAccountsDataState companyAccountsDataState;

    @Mock
    private LoansToDirectorsAdditionalInfoService loansToDirectorsAdditionalInfoService;

    @Mock
    private LoansToDirectorsLinks loansToDirectorsLinks;

    @InjectMocks
    private LoansToDirectorsAdditionalInfoQuestionController controller;

    private static final String COMPANY_NUMBER = "companyNumber";

    private static final String TRANSACTION_ID = "transactionId";

    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";

    private static final String ADDITIONAL_INFORMATION_SELECTION_PATH = "/company/" + COMPANY_NUMBER +
            "/transaction/" + TRANSACTION_ID +
            "/company-accounts/" + COMPANY_ACCOUNTS_ID +
            "/small-full/notes/add-or-remove-loans/additional-information-question";

    private static final String ADDITIONAL_INFORMATION_ANSWER_MODEL_ATTR = "loansToDirectorsAdditionalInfoQuestion";
    private static final String ADDITIONAL_INFORMATION_SELECTION = "hasIncludedLoansToDirectorsAdditionalInfo";

    private static final String TEMPLATE_NAME_MODEL_ATTR = "templateName";

    private static final String ADDITIONAL_INFORMATION_SELECTION_VIEW = "smallfull/loansToDirectorsAdditionalInfoQuestion";

    private static final String ERROR_VIEW = "error";

    private static final String MOCK_CONTROLLER_PATH = UrlBasedViewResolver.REDIRECT_URL_PREFIX + "mockControllerPath";

    private static final String COMPANY_ACCOUNTS_DATA_STATE = "companyAccountsDataState";

    @BeforeEach
    private void setup() {

        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    @DisplayName("Get request - success - has additional information set from db")
    void getAdditionalInformationSuccessHasSelectionSetFromDB() throws Exception {

        when(apiClientService.getApiClient()).thenReturn(apiClient);
        when(loansToDirectorsService.getLoansToDirectors(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID)).thenReturn(loansToDirectorsApi);
        when(loansToDirectorsApi.getLinks()).thenReturn(loansToDirectorsLinks);
        when(loansToDirectorsLinks.getAdditionalInformation()).thenReturn("text");

        this.mockMvc.perform(get(ADDITIONAL_INFORMATION_SELECTION_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name(ADDITIONAL_INFORMATION_SELECTION_VIEW))
                .andExpect(model().attributeExists(ADDITIONAL_INFORMATION_ANSWER_MODEL_ATTR))
                .andExpect(model().attributeExists(TEMPLATE_NAME_MODEL_ATTR));

        // verify no interactions with the data state
        verify(request, never()).getSession();
    }

    @Test
    @DisplayName("Get request - success - has no additional information set from db")
    void getAdditionalInformationSuccessHasSelectionLtdNull() throws Exception {

        when(apiClientService.getApiClient()).thenReturn(apiClient);
        when(loansToDirectorsService.getLoansToDirectors(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID)).thenReturn(null);
        when(request.getSession()).thenReturn(httpSession);
        when(httpSession.getAttribute(COMPANY_ACCOUNTS_DATA_STATE)).thenReturn(companyAccountsDataState);
        when(companyAccountsDataState.getHasIncludedLoansToDirectorsAdditionalInfo()).thenReturn(false);

        this.mockMvc.perform(get(ADDITIONAL_INFORMATION_SELECTION_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name(ADDITIONAL_INFORMATION_SELECTION_VIEW))
                .andExpect(model().attributeExists(ADDITIONAL_INFORMATION_ANSWER_MODEL_ATTR))
                .andExpect(model().attributeExists(TEMPLATE_NAME_MODEL_ATTR));
    }

    @Test
    @DisplayName("Get request - service exception")
    void getAdditionalInformationThrowsServiceException() throws Exception {

        when(apiClientService.getApiClient()).thenReturn(apiClient);
        when(loansToDirectorsService.getLoansToDirectors(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID)).thenThrow(ServiceException.class);


        this.mockMvc.perform(get(ADDITIONAL_INFORMATION_SELECTION_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name(ERROR_VIEW));
    }

    @Test
    @DisplayName("Post loans to directors additional info - has not included additional information but has loans and has previously filed additional info")
    void postRequestHasNotIncludedLtdAddInfoWithLoansAndPrevInfo() throws Exception {

        when(apiClientService.getApiClient()).thenReturn(apiClient);
        when(loansToDirectorsService.getLoansToDirectors(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID)).thenReturn(loansToDirectorsApi);
        when(navigatorService.getNextControllerRedirect(any(), ArgumentMatchers.<String>any())).thenReturn(MOCK_CONTROLLER_PATH);
        when(loansToDirectorsApi.getLoans()).thenReturn(getLoans());
        when(loansToDirectorsApi.getLinks()).thenReturn(loansToDirectorsLinks);
        when(loansToDirectorsLinks.getAdditionalInformation()).thenReturn("text");

        this.mockMvc.perform(post(ADDITIONAL_INFORMATION_SELECTION_PATH)
                .param(ADDITIONAL_INFORMATION_SELECTION, "0")
                .sessionAttr(COMPANY_ACCOUNTS_DATA_STATE, new CompanyAccountsDataState()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name(MOCK_CONTROLLER_PATH));

        verify(loansToDirectorsAdditionalInfoService).deleteAdditionalInformation(TRANSACTION_ID, COMPANY_ACCOUNTS_ID);
    }

    @Test
    @DisplayName("Post loans to directors additional info - has not included additional information but has loans and has not previously filed additional info")
    void postRequestHasNotIncludedLtdAddInfoWithLoansAndNoPrevInfo() throws Exception {

        when(apiClientService.getApiClient()).thenReturn(apiClient);
        when(loansToDirectorsService.getLoansToDirectors(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID)).thenReturn(loansToDirectorsApi);
        when(navigatorService.getNextControllerRedirect(any(), ArgumentMatchers.<String>any())).thenReturn(MOCK_CONTROLLER_PATH);
        when(loansToDirectorsApi.getLoans()).thenReturn(getLoans());
        when(loansToDirectorsApi.getLinks()).thenReturn(loansToDirectorsLinks);
        when(loansToDirectorsLinks.getAdditionalInformation()).thenReturn(null);

        this.mockMvc.perform(post(ADDITIONAL_INFORMATION_SELECTION_PATH)
                .param(ADDITIONAL_INFORMATION_SELECTION, "0")
                .sessionAttr(COMPANY_ACCOUNTS_DATA_STATE, new CompanyAccountsDataState()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name(MOCK_CONTROLLER_PATH));

        verify(loansToDirectorsAdditionalInfoService, never()).deleteAdditionalInformation(TRANSACTION_ID, COMPANY_ACCOUNTS_ID);
    }

    @Test
    @DisplayName("Post loans to directors additional info - has included additional information with null loans to directors resource")
    void postRequestHasIncludedLtdAddInfoWithNoLtd() throws Exception {

        when(apiClientService.getApiClient()).thenReturn(apiClient);

        when(loansToDirectorsService.getLoansToDirectors(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID)).thenReturn(null);

        when(navigatorService.getNextControllerRedirect(any(), ArgumentMatchers.<String>any())).thenReturn(MOCK_CONTROLLER_PATH);


        this.mockMvc.perform(post(ADDITIONAL_INFORMATION_SELECTION_PATH)
                .param(ADDITIONAL_INFORMATION_SELECTION, "1")
                .sessionAttr(COMPANY_ACCOUNTS_DATA_STATE, new CompanyAccountsDataState()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name(MOCK_CONTROLLER_PATH));

        verify(loansToDirectorsService).createLoansToDirectors(TRANSACTION_ID, COMPANY_ACCOUNTS_ID);
    }


    @Test
    @DisplayName("Post loans to directors additional info - has included additional information with loans to directors resource")
    void postRequestHasIncludedLtdAddInfoWithLtd() throws Exception {

        when(apiClientService.getApiClient()).thenReturn(apiClient);
        when(loansToDirectorsService.getLoansToDirectors(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID)).thenReturn(loansToDirectorsApi);
        when(navigatorService.getNextControllerRedirect(any(), ArgumentMatchers.<String>any())).thenReturn(MOCK_CONTROLLER_PATH);


        this.mockMvc.perform(post(ADDITIONAL_INFORMATION_SELECTION_PATH)
                .param(ADDITIONAL_INFORMATION_SELECTION, "1")
                .sessionAttr(COMPANY_ACCOUNTS_DATA_STATE, new CompanyAccountsDataState()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name(MOCK_CONTROLLER_PATH));

        verify(loansToDirectorsService, never()).createLoansToDirectors(TRANSACTION_ID, COMPANY_ACCOUNTS_ID);
    }

    @Test
    @DisplayName("Post loans to directors additional info - service exception thrown")
    void postRequestHasIncludedLoansToDirAddInfoException() throws Exception {

        when(apiClientService.getApiClient()).thenReturn(apiClient);
        when(loansToDirectorsService.getLoansToDirectors(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID)).thenThrow(ServiceException.class);

        this.mockMvc.perform(post(ADDITIONAL_INFORMATION_SELECTION_PATH)
                .param(ADDITIONAL_INFORMATION_SELECTION, "1")
                .sessionAttr(COMPANY_ACCOUNTS_DATA_STATE, new CompanyAccountsDataState()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name(ERROR_VIEW));
    }

    @Test
    @DisplayName("Post loans to directors additional info - binding result error")
    void postRequestHasIncludedLoansToDirAddInfoBindingError() throws Exception {

        this.mockMvc.perform(post(ADDITIONAL_INFORMATION_SELECTION_PATH)
                .sessionAttr(COMPANY_ACCOUNTS_DATA_STATE, new CompanyAccountsDataState()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name(ADDITIONAL_INFORMATION_SELECTION_VIEW));
    }

    @Test
    @DisplayName("Post loans to directors additional info - has not included additional information but has no loans ")
    void postRequestHasNotIncludedLoansToDirectorsAddInfoNoLoans() throws Exception {

        when(apiClientService.getApiClient()).thenReturn(apiClient);
        when(loansToDirectorsService.getLoansToDirectors(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID)).thenReturn(loansToDirectorsApi);
        when(navigatorService.getNextControllerRedirect(any(), ArgumentMatchers.<String>any())).thenReturn(MOCK_CONTROLLER_PATH);
        when(loansToDirectorsApi.getLoans()).thenReturn(null);

        this.mockMvc.perform(post(ADDITIONAL_INFORMATION_SELECTION_PATH)
                .param(ADDITIONAL_INFORMATION_SELECTION, "0")
                .sessionAttr(COMPANY_ACCOUNTS_DATA_STATE, new CompanyAccountsDataState()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name(MOCK_CONTROLLER_PATH));

        verify(loansToDirectorsService).deleteLoansToDirectors(TRANSACTION_ID, COMPANY_ACCOUNTS_ID);
    }

    private Map<String, String> getLoans() {
        Map<String, String> loans = new HashMap<String, String>();
        loans.put("id", "loans");

        return loans;
    }
} 
