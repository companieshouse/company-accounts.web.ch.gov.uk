package uk.gov.companieshouse.web.accounts.controller.smallfull;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.UrlBasedViewResolver;
import uk.gov.companieshouse.web.accounts.service.navigation.NavigatorService;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CriteriaControllerTests {

    private static final String COMPANY_NUMBER = "companyNumber";
    private static final String CRITERIA_PATH = "/company/" + COMPANY_NUMBER +
            "/small-full/criteria";
    private static final String CRITERIA_MODEL_ATTR = "criteria";
    private static final String TEMPLATE_NAME_MODEL_ATTR = "templateName";
    private static final String CRITERIA_VIEW = "smallfull/criteria";
    private static final String ALTERNATIVE_FILING_PATH = "redirect:/company/" + COMPANY_NUMBER +
            "/submit-abridged-accounts/alternative-filing-options";
    private static final String OTHER_FILING_PATH = "redirect:/company/" + COMPANY_NUMBER +
            "/select-account-type";
    private static final String MOCK_CONTROLLER_PATH =
            UrlBasedViewResolver.REDIRECT_URL_PREFIX + "mockControllerPath";
    private MockMvc mockMvc;
    @Mock
    private NavigatorService navigatorService;
    @InjectMocks
    private CriteriaController controller;

    @BeforeEach
    public void setup() {

        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    @DisplayName("Get criteria view success path")
    void getRequestSuccess() throws Exception {

        this.mockMvc.perform(get(CRITERIA_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name(CRITERIA_VIEW))
                .andExpect(model().attributeExists(CRITERIA_MODEL_ATTR))
                .andExpect(model().attributeExists(TEMPLATE_NAME_MODEL_ATTR));
    }

    @Test
    @DisplayName("Post criteria with YES selection")
    void postRequestCriteriaMet() throws Exception {

        String beanElement = "isCriteriaMet";
        String criteriaMet = "yes";

        when(navigatorService.getNextControllerRedirect(any(Class.class),
                anyString()))
                .thenReturn(MOCK_CONTROLLER_PATH);

        this.mockMvc.perform(post(CRITERIA_PATH)
                        .param(beanElement, criteriaMet))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name(MOCK_CONTROLLER_PATH));
    }

    @Test
    @DisplayName("Post criteria with alternative filing method selection")
    void postRequestCriteriaNotMetAlternativeFiling() throws Exception {

        String beanElement = "isCriteriaMet";
        String criteriaNotMet = "noAlternativeFilingMethod";

        this.mockMvc.perform(post(CRITERIA_PATH)
                        .param(beanElement, criteriaNotMet))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name(ALTERNATIVE_FILING_PATH));
    }

    @Test
    @DisplayName("Post criteria with other accounts selection")
    void postRequestCriteriaNotMetOtherAccounts() throws Exception {

        String beanElement = "isCriteriaMet";
        String criteriaNotMet = "noOtherAccounts";

        this.mockMvc.perform(post(CRITERIA_PATH)
                        .param(beanElement, criteriaNotMet))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name(OTHER_FILING_PATH));
    }

    @Test
    @DisplayName("Post criteria with binding result errors")
    void postRequestBindingResultErrors() throws Exception {

        String beanElement = "isCriteriaMet";
        // Mock no selection to trigger binding result errors
        String invalidData = null;

        this.mockMvc.perform(post(CRITERIA_PATH)
                        .param(beanElement, invalidData))
                .andExpect(status().isOk())
                .andExpect(view().name(CRITERIA_VIEW));
    }
}
