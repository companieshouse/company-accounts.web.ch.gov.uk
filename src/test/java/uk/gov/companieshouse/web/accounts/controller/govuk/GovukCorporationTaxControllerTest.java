package uk.gov.companieshouse.web.accounts.controller.govuk;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.UrlBasedViewResolver;
import uk.gov.companieshouse.web.accounts.service.navigation.NavigatorService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GovukCorporationTaxControllerTest {

    private MockMvc mockMvc;

    @Mock
    private NavigatorService navigatorService;

    @InjectMocks
    private GovukCorporationTaxController controller;

    private static final String PATH = "/accounts/corporation-tax";
    private static final String MODEL_ATTR = "corporationTax";
    private static final String TEMPLATE_NAME_MODEL_ATTR = "templateName";
    private static final String VIEW = "corporationtax/corporationTax";
    private static final String MOCK_CONTROLLER_PATH = UrlBasedViewResolver.REDIRECT_URL_PREFIX + "mockControllerPath";
    private static final String TAX_EXTERNAL_PATH = UrlBasedViewResolver.REDIRECT_URL_PREFIX +
        "https://www.gov.uk/file-your-company-accounts-and-tax-return";

    @BeforeEach
    private void setup() {

        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    @DisplayName("Get corporation tax view success path")
    void getRequestSuccess() throws Exception {

        this.mockMvc.perform(get(PATH))
            .andExpect(status().isOk())
            .andExpect(view().name(VIEW))
            .andExpect(model().attributeExists(MODEL_ATTR))
            .andExpect(model().attributeExists(TEMPLATE_NAME_MODEL_ATTR));
    }

    @Test
    @DisplayName("Post corporation tax with criteria met")
    void postRequestCriteriaMet() throws Exception {

        String beanElement = "fileChoice";
        String criteriaMet = "true";

        this.mockMvc.perform(post(PATH)
            .param(beanElement, criteriaMet))
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name(TAX_EXTERNAL_PATH));
    }

    @Test
    @DisplayName("Post corporation tax with criteria not met")
    void postRequestCriteriaNotMet() throws Exception {

        String beanElement = "fileChoice";
        String criteriaMet = "false";

        when(navigatorService.getNextControllerRedirect(any(), ArgumentMatchers.<String>any()))
            .thenReturn(MOCK_CONTROLLER_PATH);

        this.mockMvc.perform(post(PATH)
            .param(beanElement, criteriaMet))
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name(MOCK_CONTROLLER_PATH));
    }

    @Test
    @DisplayName("Post corporation tax with binding result errors")
    void postRequestBindingResultErrors() throws Exception {

        String beanElement = "fileChoice";
        String criteriaMet = null;

        this.mockMvc.perform(post(PATH)
            .param(beanElement, criteriaMet))
            .andExpect(status().isOk())
            .andExpect(view().name(VIEW));
    }
}
