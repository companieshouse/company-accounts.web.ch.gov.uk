package uk.gov.companieshouse.web.accounts.controller.govuk;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

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
import uk.gov.companieshouse.web.accounts.service.navigation.NavigatorService;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GovukSelectAccountTypeControllerTest {

    private static final String MOCK_CONTROLLER_PATH =
            UrlBasedViewResolver.REDIRECT_URL_PREFIX + "mockControllerPath";

    private static final String SELECT_ACCOUNT_TYPE_VIEW = "accountselector/selectAccountType";

    private static final String TEMPLATE_NAME_MODEL_ATTR = "templateName";

    private static final String TYPE_OF_ACCOUNTS_ATTR = "typeOfAccounts";

    private static final String SELECT_ACCOUNT_TYPE_PATH =
            "/accounts/select-account-type";

    private static final String MICRO_ENTITY_ACCOUNTS_URI =
            UrlBasedViewResolver.REDIRECT_URL_PREFIX +
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
    void setUpBeforeEach() {
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
    @DisplayName("Post select account type for micro-entity account, success path")
    void postRequestForMicroEntitySuccess() throws Exception {

        performPostRequestAndValidateResponse(
                "micro-entity",
                status().is3xxRedirection()
        );
    }

    @Test
    @DisplayName("Post select account type for abridged account, success path")
    void postRequestForAbridgedAccountSuccess() throws Exception {

        performPostRequestAndValidateResponse(
                "abridged",
                status().is3xxRedirection()
        );
    }

    @Test
    @DisplayName("Post select account type for dormant account, success path")
    void postRequestForDormantAccountSuccess() throws Exception {

        performPostRequestAndValidateResponse(
                "dormant",
                status().is3xxRedirection()
        );
    }

    @Test
    @DisplayName("Post select account type for package accounts, success path")
    void postRequestForPackageAccountsSuccess() throws Exception {

        performPostRequestAndValidateResponse(
                "package",
                status().is3xxRedirection()
        );
    }

    @Test
    @DisplayName("Post criteria with binding result errors")
    void postRequestBindingResultErrors() throws Exception {

        performPostRequestAndValidateResponseNoAttributes(
                null,
                status().isOk()
        );
    }

    private void performPostRequestAndValidateResponse(
            String beanElementValue,
            ResultMatcher expectedStatus) throws Exception {

        this.mockMvc.perform(
                        post(SELECT_ACCOUNT_TYPE_PATH).param("selectedAccountTypeName", beanElementValue))
                .andExpect(expectedStatus)
                .andExpect(flash().attributeExists(TEMPLATE_NAME_MODEL_ATTR))
                .andExpect(flash().attributeExists(TYPE_OF_ACCOUNTS_ATTR));
    }

    private void performPostRequestAndValidateResponseNoAttributes(
            String beanElementValue,
            ResultMatcher expectedStatus) throws Exception {

        this.mockMvc.perform(
                        post(SELECT_ACCOUNT_TYPE_PATH).param("selectedAccountTypeName", beanElementValue))
                .andExpect(expectedStatus);
    }
}

