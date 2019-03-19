package uk.gov.companieshouse.web.accounts.controller.govuk;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.UrlBasedViewResolver;
import org.springframework.web.util.UriTemplate;
import uk.gov.companieshouse.web.accounts.service.navigation.NavigatorService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GovukSelectAccountTypeControllerTests {

    private static final String MOCK_CONTROLLER_PATH =
        UrlBasedViewResolver.REDIRECT_URL_PREFIX + "mockControllerPath";

    private static final String SELECT_ACCOUNT_TYPE_VIEW = "accountselector/selectAccountType";

    private static final String TEMPLATE_NAME_MODEL_ATTR = "templateName";

    private static final String TYPE_OF_ACCOUNTS_ATTR = "typeOfAccounts";

    private static final String SELECT_ACCOUNT_TYPE_PATH =
        "/accounts/select-account-type";

    private static final String MICRO_ENTITY_ACCOUNTS_URI = UrlBasedViewResolver.REDIRECT_URL_PREFIX +
        "https://beta.companieshouse.gov.uk/guides/accounts/chooser/micro-entity";

    private static final String DORMANT_ACCOUNTS_URI = UrlBasedViewResolver.REDIRECT_URL_PREFIX +
        "https://beta.companieshouse.gov.uk/guides/accounts/chooser/dormant";

    private static final String ABRIDGED_ACCOUNTS_URI = UrlBasedViewResolver.REDIRECT_URL_PREFIX +
        "https://beta.companieshouse.gov.uk/guides/accounts/chooser/abridged";

    private MockMvc mockMvc;

    @Mock
    private NavigatorService mockNavigatorService;

    @InjectMocks
    private GovukSelectAccountTypeController controller;

    @BeforeEach
    void setUpBeforeEAch() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    @DisplayName("Get select account type view, success path")
    void getRequestSuccess() throws Exception {

        this.mockMvc.perform(get(SELECT_ACCOUNT_TYPE_PATH))
            .andExpect(status().isOk())
            .andExpect(view().name(SELECT_ACCOUNT_TYPE_VIEW))
            .andExpect(model().attributeExists(TEMPLATE_NAME_MODEL_ATTR))
            .andExpect(model().attributeExists(TYPE_OF_ACCOUNTS_ATTR));
    }

    @Test
    @DisplayName("Post select account type for micros account, success path")
    void postRequestForMicrosSuccess() throws Exception {

        performPostRequestAndValidateResponse(
            "micros",
            status().is3xxRedirection(),
            getRedirectUrlForAccount(MICRO_ENTITY_ACCOUNTS_URI));
    }

    @Test
    @DisplayName("Post select account type for abridged account, success path")
    void postRequestForAbridgedAccountSuccess() throws Exception {

        performPostRequestAndValidateResponse(
            "abridged",
            status().is3xxRedirection(),
            getRedirectUrlForAccount(ABRIDGED_ACCOUNTS_URI));
    }

    @Test
    @DisplayName("Post select account type for dormant account, success path")
    void postRequestForDormantAccountSuccess() throws Exception {

        performPostRequestAndValidateResponse(
            "dormant",
            status().is3xxRedirection(),
            getRedirectUrlForAccount(DORMANT_ACCOUNTS_URI));
    }

    @Test
    @DisplayName("Post any other account selected will not be re-directed")
    void postRequestAnyOtherAccountSuccess() throws Exception {

        performPostRequestAndValidateResponse(
            "anyOtherAccount",
            status().isOk(),
            SELECT_ACCOUNT_TYPE_VIEW);
    }

    @Test
    @DisplayName("Post criteria with binding result errors")
    void postRequestBindingResultErrors() throws Exception {

        performPostRequestAndValidateResponse(
            null,
            status().isOk(),
            SELECT_ACCOUNT_TYPE_VIEW);
    }

    private void performPostRequestAndValidateResponse(
        String beanElementValue,
        ResultMatcher expectedStatus,
        String expectedViewName) throws Exception {

        this.mockMvc.perform(
            post(SELECT_ACCOUNT_TYPE_PATH).param("selectedAccountTypeName", beanElementValue))
            .andExpect(expectedStatus)
            .andExpect(model().attributeExists(TEMPLATE_NAME_MODEL_ATTR))
            .andExpect(model().attributeExists(TYPE_OF_ACCOUNTS_ATTR));
    }

    private String getRedirectUrlForAccount(String accountTypeUri) {
        return
            UrlBasedViewResolver.REDIRECT_URL_PREFIX + accountTypeUri;
    }
}

