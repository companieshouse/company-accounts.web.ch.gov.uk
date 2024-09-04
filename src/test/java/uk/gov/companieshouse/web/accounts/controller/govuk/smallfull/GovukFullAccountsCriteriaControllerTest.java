package uk.gov.companieshouse.web.accounts.controller.govuk.smallfull;

import static org.mockito.ArgumentMatchers.any;
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
class GovukFullAccountsCriteriaControllerTest {

    private MockMvc mockMvc;

    @Mock
    private NavigatorService navigatorService;

    @InjectMocks
    private GovukFullAccountsCriteriaController controller;

    private static final String VIEW = "govuk/smallfull/criteria";
    private static final String PATH = "/accounts/full-accounts-criteria";
    private static final String TEMPLATE_NAME_MODEL_ATTR = "templateName";
    private static final String BACK_PAGE_MODEL_ATTR = "backButton";
    private static final String CHS_HOME_PATH = UrlBasedViewResolver.REDIRECT_URL_PREFIX + "/";
    private static final String MOCK_CONTROLLER_PATH =
            UrlBasedViewResolver.REDIRECT_URL_PREFIX + "mockControllerPath";

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    @DisplayName("Get full accounts criteria view success path")
    void getRequestSuccess() throws Exception {

        when(navigatorService.getPreviousControllerPath(
                any(Class.class)))
                .thenReturn(MOCK_CONTROLLER_PATH);

        this.mockMvc.perform(get(PATH))
                .andExpect(status().isOk())
                .andExpect(view().name(VIEW))
                .andExpect(model().attributeExists(BACK_PAGE_MODEL_ATTR))
                .andExpect(model().attributeExists(TEMPLATE_NAME_MODEL_ATTR));
    }

    @Test
    @DisplayName("Post corporation tax with criteria met")
    void postRequestCriteriaMet() throws Exception {

        when(navigatorService.getNextControllerRedirect(
                any(Class.class)))
                .thenReturn(MOCK_CONTROLLER_PATH);

        this.mockMvc.perform(post(PATH))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name(MOCK_CONTROLLER_PATH));
    }
}
