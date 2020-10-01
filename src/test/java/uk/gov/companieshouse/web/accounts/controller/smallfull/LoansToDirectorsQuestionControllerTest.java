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
import uk.gov.companieshouse.web.accounts.api.impl.ApiClientServiceImpl;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.state.CompanyAccountsDataState;
import uk.gov.companieshouse.web.accounts.service.navigation.NavigatorService;
import uk.gov.companieshouse.web.accounts.service.smallfull.impl.LoansServiceImpl;
import uk.gov.companieshouse.web.accounts.service.smallfull.impl.LoansToDirectorsServiceImpl;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class LoansToDirectorsQuestionControllerTest {

    private MockMvc mockMvc;

    @Mock
    private NavigatorService navigatorService;

    @Mock
    private LoansServiceImpl loansService;

    @Mock
    private LoansToDirectorsServiceImpl loansToDirectorsService;

    @Mock
    private ApiClientServiceImpl apiClientService;

    @Mock
    private ApiClient apiClient;

    @Mock
    private LoansToDirectorsApi loansToDirectorsApi;

    @Mock
    private LoansToDirectorsLinks loansToDirectorsLinks;

    @InjectMocks
    private LoansToDirectorsQuestionController controller;

    private static final String COMPANY_NUMBER = "companyNumber";

    private static final String TRANSACTION_ID = "transactionId";

    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";

    private static final String LOANS_TO_DIRECTORS_QUESTION_PATH = "/company/" + COMPANY_NUMBER +
                                                                "/transaction/" + TRANSACTION_ID +
                                                                "/company-accounts/" + COMPANY_ACCOUNTS_ID +
                                                                "/small-full/notes/loans-to-directors-question";

    private static final String LOANS_TO_DIRECTORS_QUESTION_MODEL_ATTR = "loansToDirectorsQuestion";

    private static final String TEMPLATE_NAME_MODEL_ATTR = "templateName";

    private static final String LOANS_TO_DIRECTORS_QUESTION_VIEW = "smallfull/loansToDirectorsQuestion";

    private static final String LOANS_TO_DIRECTORS_SELECTION = "hasIncludedLoansToDirectors";

    private static final String MOCK_CONTROLLER_PATH = UrlBasedViewResolver.REDIRECT_URL_PREFIX + "mockControllerPath";

    private static final String COMPANY_ACCOUNTS_DATA_STATE = "companyAccountsDataState";

    private static final String ERROR_VIEW = "error";

    private static final String ADDITIONAL_INFORMATION = "additionalInformation";

    @BeforeEach
    private void setup() {

        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    @DisplayName("Get loans to directors question without loans")
    void getRequestEmptyLoans() throws Exception {

        when(apiClientService.getApiClient()).thenReturn(apiClient);

        when(loansToDirectorsService.getLoansToDirectors(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID)).thenReturn(loansToDirectorsApi);

        this.mockMvc.perform(get(LOANS_TO_DIRECTORS_QUESTION_PATH)
            .sessionAttr(COMPANY_ACCOUNTS_DATA_STATE, new CompanyAccountsDataState()))
            .andExpect(status().isOk())
            .andExpect(view().name(LOANS_TO_DIRECTORS_QUESTION_VIEW))
            .andExpect(model().attributeExists(LOANS_TO_DIRECTORS_QUESTION_MODEL_ATTR))
            .andExpect(model().attributeExists(TEMPLATE_NAME_MODEL_ATTR));
    }


    @Test
    @DisplayName("Get loans to directors question with loans")
    void getRequestWithLoans() throws Exception {

        when(apiClientService.getApiClient()).thenReturn(apiClient);

        when(loansToDirectorsService.getLoansToDirectors(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID)).thenReturn(loansToDirectorsApi);
        when(loansToDirectorsApi.getLoans()).thenReturn(getLoans());

        this.mockMvc.perform(get(LOANS_TO_DIRECTORS_QUESTION_PATH)
                .sessionAttr(COMPANY_ACCOUNTS_DATA_STATE, new CompanyAccountsDataState()))
                .andExpect(status().isOk())
                .andExpect(view().name(LOANS_TO_DIRECTORS_QUESTION_VIEW))
                .andExpect(model().attributeExists(LOANS_TO_DIRECTORS_QUESTION_MODEL_ATTR))
                .andExpect(model().attributeExists(TEMPLATE_NAME_MODEL_ATTR));
    }

    @Test
    @DisplayName("Get loans to directors question - Throws exception")
    void getRequestThrowsException() throws Exception {

        when(apiClientService.getApiClient()).thenReturn(apiClient);

        when(loansToDirectorsService.getLoansToDirectors(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID)).thenThrow(ServiceException.class);

        this.mockMvc.perform(get(LOANS_TO_DIRECTORS_QUESTION_PATH)
                .sessionAttr(COMPANY_ACCOUNTS_DATA_STATE, new CompanyAccountsDataState()))
                .andExpect(status().isOk())
                .andExpect(view().name(ERROR_VIEW));
    }

    @Test
    @DisplayName("Post loans to directors - has not included loans to directors")
    void postRequestHasNotIncludedLoansToDirectors() throws Exception {

        when(apiClientService.getApiClient()).thenReturn(apiClient);

        when(loansToDirectorsService.getLoansToDirectors(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID)).thenReturn(loansToDirectorsApi);

        when(navigatorService.getNextControllerRedirect(any(), ArgumentMatchers.<String>any())).thenReturn(MOCK_CONTROLLER_PATH);

        when(loansToDirectorsApi.getLinks()).thenReturn(loansToDirectorsLinks);
        when(loansToDirectorsLinks.getAdditionalInformation()).thenReturn(ADDITIONAL_INFORMATION);
        
        this.mockMvc.perform(post(LOANS_TO_DIRECTORS_QUESTION_PATH)
            .param(LOANS_TO_DIRECTORS_SELECTION, "0")
            .sessionAttr(COMPANY_ACCOUNTS_DATA_STATE, new CompanyAccountsDataState()))
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name(MOCK_CONTROLLER_PATH));
    }

    @Test
    @DisplayName("Post loans to directors - has not included loans to directors, has add info")
    void postRequestHasNotLoansToDirectorsWithAddInfo() throws Exception {

        when(apiClientService.getApiClient()).thenReturn(apiClient);

        when(loansToDirectorsService.getLoansToDirectors(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID)).thenReturn(loansToDirectorsApi);

        when(navigatorService.getNextControllerRedirect(any(), ArgumentMatchers.<String>any())).thenReturn(MOCK_CONTROLLER_PATH);

        when(loansToDirectorsApi.getLinks()).thenReturn(loansToDirectorsLinks);
        when(loansToDirectorsLinks.getAdditionalInformation()).thenReturn(ADDITIONAL_INFORMATION);
        when(loansToDirectorsApi.getLoans()).thenReturn(getLoans());

        this.mockMvc.perform(post(LOANS_TO_DIRECTORS_QUESTION_PATH)
                .param(LOANS_TO_DIRECTORS_SELECTION, "0")
                .sessionAttr(COMPANY_ACCOUNTS_DATA_STATE, new CompanyAccountsDataState()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name(MOCK_CONTROLLER_PATH));
    }

    @Test
    @DisplayName("Post loans to directors - has not included loans to directors and no additional info")
    void postRequestHasNotIncludedLoansToDirectorsNoAddInfo() throws Exception {

        when(apiClientService.getApiClient()).thenReturn(apiClient);

        when(loansToDirectorsService.getLoansToDirectors(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID)).thenReturn(loansToDirectorsApi);

        when(navigatorService.getNextControllerRedirect(any(), ArgumentMatchers.<String>any())).thenReturn(MOCK_CONTROLLER_PATH);

        when(loansToDirectorsApi.getLinks()).thenReturn(loansToDirectorsLinks);
        when(loansToDirectorsLinks.getAdditionalInformation()).thenReturn(null);

        this.mockMvc.perform(post(LOANS_TO_DIRECTORS_QUESTION_PATH)
                .param(LOANS_TO_DIRECTORS_SELECTION, "0")
                .sessionAttr(COMPANY_ACCOUNTS_DATA_STATE, new CompanyAccountsDataState()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name(MOCK_CONTROLLER_PATH));
    }

    @Test
    @DisplayName("Post loans to directors - has included loans to directors")
    void postRequestHasIncludedLoansToDirectors() throws Exception {

        when(apiClientService.getApiClient()).thenReturn(apiClient);

        when(loansToDirectorsService.getLoansToDirectors(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID)).thenReturn(null);

        when(navigatorService.getNextControllerRedirect(any(), ArgumentMatchers.<String>any())).thenReturn(MOCK_CONTROLLER_PATH);

        this.mockMvc.perform(post(LOANS_TO_DIRECTORS_QUESTION_PATH)
                .param(LOANS_TO_DIRECTORS_SELECTION, "1")
                .sessionAttr(COMPANY_ACCOUNTS_DATA_STATE, new CompanyAccountsDataState()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name(MOCK_CONTROLLER_PATH));
    }

    @Test
    @DisplayName("Post loans to directors - throws exception")
    void postRequestThrowsException() throws Exception {

        when(apiClientService.getApiClient()).thenReturn(apiClient);

        when(loansToDirectorsService.getLoansToDirectors(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID)).thenThrow(ServiceException.class);

        this.mockMvc.perform(post(LOANS_TO_DIRECTORS_QUESTION_PATH)
                .param(LOANS_TO_DIRECTORS_SELECTION, "1")
                .sessionAttr(COMPANY_ACCOUNTS_DATA_STATE, new CompanyAccountsDataState()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name(ERROR_VIEW));
    }

    @Test
    @DisplayName("Post loans to directors - binding result errors")
    void postRequestBindingResultErrors() throws Exception {

        this.mockMvc.perform(post(LOANS_TO_DIRECTORS_QUESTION_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name(LOANS_TO_DIRECTORS_QUESTION_VIEW));
    }

    private Map<String, String> getLoans() {
        Map<String, String> loans = new HashMap<String, String>();
        loans.put("id", "loans");

        return loans;
    }
}
