package uk.gov.companieshouse.web.accounts.controller.govuk;

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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.UrlBasedViewResolver;;
import uk.gov.companieshouse.web.accounts.service.navigation.NavigatorService;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GovukCriteriaControllerTest {

    private MockMvc mockMvc;

    @Mock
    private NavigatorService navigatorService;

    @InjectMocks
    private GovukCriteriaController controller;

    private static final String CRITERIA_PATH = "/accounts/criteria";
    private static final String CRITERIA_MODEL_ATTR = "criteria";
    private static final String TEMPLATE_NAME_MODEL_ATTR = "templateName";
    private static final String CRITERIA_VIEW = "smallfull/criteria";
    private static final String ALTERNATIVE_FILING_PATH = "redirect:/accounts/alternative-filing-options";
    private static final String OTHER_FILING_PATH = "redirect:/accounts/select-account-type";
    private static final String MOCK_CONTROLLER_PATH = UrlBasedViewResolver.REDIRECT_URL_PREFIX + "/company-lookup/search";
    private static final String FORWARD_PATH = "/accounts/company/{companyNumber}/details";

    @BeforeEach
    private void setup() {

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
    @DisplayName("Post criteria with criteria met")
    void postRequestCriteriaMet() throws Exception {

        String beanElement = "isCriteriaMet";
        String criteriaMet = "yes";

        this.mockMvc.perform(post(CRITERIA_PATH)
                .param(beanElement, criteriaMet))
                .andExpect(MockMvcResultMatchers.model()
                    .attribute("forward", FORWARD_PATH))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name(MOCK_CONTROLLER_PATH));
    }

    @Test
    @DisplayName("Post corporation tax with alternative filing selected")
    void postRequestAlternativeFiling() throws Exception {

        String beanElement = "isCriteriaMet";
        String criteriaMet = "noAlternativeFilingMethod";

        this.mockMvc.perform(post(CRITERIA_PATH)
                .param(beanElement, criteriaMet))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name(ALTERNATIVE_FILING_PATH));
    }

    @Test
    @DisplayName("Post corporation tax with other filing selected")
    void postRequestOtherFilings() throws Exception {

        String beanElement = "isCriteriaMet";
        String criteriaMet = "noOtherAccounts";

        this.mockMvc.perform(post(CRITERIA_PATH)
                .param(beanElement, criteriaMet))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name(OTHER_FILING_PATH));
    }

    @Test
    @DisplayName("Post corporation tax with binding result errors")
    void postRequestBindingResultErrors() throws Exception {

        String beanElement = "fileChoice";
        String criteriaMet = null;

        this.mockMvc.perform(post(CRITERIA_PATH)
                .param(beanElement, criteriaMet))
                .andExpect(status().isOk())
                .andExpect(view().name(CRITERIA_VIEW));
    }
}
