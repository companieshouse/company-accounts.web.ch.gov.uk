package uk.gov.companieshouse.web.accounts.controller.cic;

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
import uk.gov.companieshouse.web.accounts.service.navigation.NavigatorService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.web.servlet.view.UrlBasedViewResolver.REDIRECT_URL_PREFIX;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CicCriteriaControllerTest {

    private MockMvc mockMvc;

    @Mock
    private NavigatorService service;

    @InjectMocks
    private CicCriteriaController controller;

    @BeforeEach
    private void setup() {

        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    private static final String CIC_CRITERIA_PATH = "/accounts/cic/criteria";

    private static final String CIC_CRITERIA_VIEW = "cic/cicCriteria";
    private static final String CIC_CANT_FILE_ONLINE_YET_VIEW_PATH = REDIRECT_URL_PREFIX
            + "/accounts/cic/cant-file-online-yet";
    private static final String CIC_SUCCESS_VIEW = REDIRECT_URL_PREFIX + "/company-lookup/search";

    private static final String CIC_CRITERIA_MODEL_ATTR = "criteria";
    private static final String TEMPLATE_NAME_MODEL_ATTR = "templateName";

    @Test
    @DisplayName("Get cic criteria view - success path")
    void getRequestSuccess() throws Exception {

        this.mockMvc.perform(get(CIC_CRITERIA_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name(CIC_CRITERIA_VIEW))
                .andExpect(model().attributeExists(CIC_CRITERIA_MODEL_ATTR))
                .andExpect(model().attributeExists(TEMPLATE_NAME_MODEL_ATTR));
    }

    @Test
    @DisplayName("Post cic criteria - criteria met")
    void postRequestCriteriaMet() throws Exception {

        mockMvc.perform(post(CIC_CRITERIA_PATH).
                param("isCriteriaMet", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name(CIC_SUCCESS_VIEW));
    }

    @Test
    @DisplayName("Post cic criteria - criteria not met")
    void postRequestCriteriaNotMet() throws Exception {

        mockMvc.perform(post(CIC_CRITERIA_PATH).
                param("isCriteriaMet", "0"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name(CIC_CANT_FILE_ONLINE_YET_VIEW_PATH));
    }

    @Test
    @DisplayName("Post cic criteria - binding error")
    void postRequestCriteriaBindingError() throws Exception {

        mockMvc.perform(post(CIC_CRITERIA_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name(CIC_CRITERIA_VIEW));
    }
}