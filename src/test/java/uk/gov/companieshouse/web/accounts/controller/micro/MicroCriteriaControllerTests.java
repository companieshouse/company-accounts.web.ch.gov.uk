package uk.gov.companieshouse.web.accounts.controller.micro;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.UrlBasedViewResolver;
import uk.gov.companieshouse.web.accounts.service.navigation.NavigatorService;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MicroCriteriaControllerTests {

    private static final String COMPANY_NUMBER = "companyNumber";
    private static final String MICRO_CRITERIA_PATH = "/company/" + COMPANY_NUMBER +
            "/micro-entity/criteria";
    private static final String BACK_BUTTON_MODEL_ATTR = "backButton";
    private static final String MICRO_ENTITY_ACCOUNTS_URI_MODEL_ATTR = "microEntityAccountsUri";
    private static final String MICRO_ENTITY_ACCOUNTS_URI_MODEL_VALUE = "http://test.entity.accounts.uri";
    private static final String TEMPLATE_NAME_MODEL_ATTR = "templateName";
    private static final String CRITERIA_VIEW = "micro/criteria";
    private static final String MOCK_CONTROLLER_PATH =
            UrlBasedViewResolver.REDIRECT_URL_PREFIX + "mockControllerPath";

    private MockMvc mockMvc;

    @Mock
    private NavigatorService mockNavigatorService;
    @InjectMocks
    private MicroCriteriaController controller;

    @Test
    @DisplayName("Get micro criteria view success path")
    void getRequestSuccess() throws Exception {
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

        ReflectionTestUtils
                .setField(controller, MICRO_ENTITY_ACCOUNTS_URI_MODEL_ATTR,
                        MICRO_ENTITY_ACCOUNTS_URI_MODEL_VALUE);

        when(mockNavigatorService.getPreviousControllerPath(any(), any()))
                .thenReturn(MOCK_CONTROLLER_PATH);

        this.mockMvc.perform(get(MICRO_CRITERIA_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name(CRITERIA_VIEW))
                .andExpect(model().attributeExists(BACK_BUTTON_MODEL_ATTR))
                .andExpect(model().attributeExists(MICRO_ENTITY_ACCOUNTS_URI_MODEL_ATTR))
                .andExpect(model().attributeExists(TEMPLATE_NAME_MODEL_ATTR));
    }
}
