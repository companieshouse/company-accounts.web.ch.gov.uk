package uk.gov.companieshouse.web.accounts.controller.govuk;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
import uk.gov.companieshouse.web.accounts.service.navigation.NavigatorService;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GovukFileTheseAccountsDifferentlyControllerTest {

    private MockMvc mockMvc;

    @Mock
    private NavigatorService navigatorService;

    @InjectMocks
    private FileTheseAccountsDifferentlyController controller;

    private static final String COMPANY_NUMBER = "12345678";
    private static final String PATH = "/company/" + COMPANY_NUMBER + "/file-these-accounts-differently";
    private static final String VIEW = "smallfull/fileTheseAccountsDifferently";
    private static final String TEMPLATE_NAME_MODEL_ATTR = "templateName";
    private static final String BACK_PAGE_MODEL_ATTR = "backButton";
    private static final String MOCK_CONTROLLER_PATH = "mockControllerPath";

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    @DisplayName("Get file these accounts differently view success path")
    void getRequestSuccess() throws Exception {

        when(navigatorService.getPreviousControllerPath(any(), any()))
                .thenReturn(MOCK_CONTROLLER_PATH);

        this.mockMvc.perform(get(PATH))
                .andExpect(status().isOk())
                .andExpect(view().name(VIEW))
                .andExpect(model().attributeExists(BACK_PAGE_MODEL_ATTR))
                .andExpect(model().attributeExists(TEMPLATE_NAME_MODEL_ATTR));
    }
}

