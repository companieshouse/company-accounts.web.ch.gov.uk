package uk.gov.companieshouse.web.accounts.controller.fullaccounts;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class FullAccountsCriteriaControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private FullAccountsCriteriaController controller;

    private static final String VIEW = "fullaccounts/criteria";
    private static final String PATH = "/accounts/full-accounts-criteria";
    private static final String MODEL_ATTR = "hideUserBar";
    private static final String TEMPLATE_NAME_MODEL_ATTR = "templateName";
    private static final String CHS_HOME_PATH = UrlBasedViewResolver.REDIRECT_URL_PREFIX +
        "/";

    @BeforeEach
    private void setup() {

        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    @DisplayName("Get full accounts criteria view success path")
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

        ReflectionTestUtils.setField(controller, "chsUrl", "/");
        assertEquals("redirect:/", controller.postFullAccountsCriteria());
    }
}
