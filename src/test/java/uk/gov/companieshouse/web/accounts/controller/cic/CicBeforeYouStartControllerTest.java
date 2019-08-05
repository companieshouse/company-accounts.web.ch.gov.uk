package uk.gov.companieshouse.web.accounts.controller.cic;

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
public class CicBeforeYouStartControllerTest {

    private MockMvc mockMvc;

    @Mock
    private NavigatorService navigatorService;

    @InjectMocks
    private CicBeforeYouStartController controller;

    private static final String MOCK_CONTROLLER_PATH =
        UrlBasedViewResolver.REDIRECT_URL_PREFIX + "mockControllerPath";

    private static final String CIC_BEFORE_YOU_START_VIEW_PATH = "/accounts/cic/before-you-start";
    private static final String CIC_BEFORE_YOU_START_VIEW = "cic/cicBeforeYouStart";

    private static final String TEMPLATE_NAME_MODEL_ATTR = "templateName";

    @BeforeEach
    private void setup() {

        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    @DisplayName("Get cic before you start")
    void getCicBeforeYouStartRequest() throws Exception{

        mockMvc.perform(get(CIC_BEFORE_YOU_START_VIEW_PATH))
            .andExpect(status().isOk())
            .andExpect(view().name(CIC_BEFORE_YOU_START_VIEW))
            .andExpect(model().attributeExists(TEMPLATE_NAME_MODEL_ATTR));
    }

    @Test
    @DisplayName("Post cic before you start")
    void postCicBeforeYouStart() throws Exception {

        when(navigatorService.getNextControllerRedirect(controller.getClass()))
            .thenReturn(MOCK_CONTROLLER_PATH);

        mockMvc.perform(post(CIC_BEFORE_YOU_START_VIEW_PATH))
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name(MOCK_CONTROLLER_PATH));
    }
}
