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
import org.springframework.web.servlet.view.UrlBasedViewResolver;
import uk.gov.companieshouse.web.accounts.service.navigation.NavigatorService;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CicFileFullAccountsControllerTest {

    private MockMvc mockMvc;

    @Mock
    NavigatorService service;

    @InjectMocks
    private CicFileFullAccountsController controller;

    @BeforeEach
    private void setup() {

        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    private static final String CIC_FILE_FULL_ACCOUNTS_VIEW_PATH = "/accounts/cic/full-accounts-criteria";
    private static final String CIC_FILE_FULL_ACCOUNTS_VIEW = "govuk/smallfull/criteria";

    private static final String TEMPLATE_NAME_MODEL_ATTR = "templateName";

    private static final String CIC_MOCK_CONTROLLER_PATH = UrlBasedViewResolver.REDIRECT_URL_PREFIX + "mockControllerPath";

    @Test
    @DisplayName("Get cic file full accounts view success path")
    void getCicFileFullAccountsRequest() throws Exception{

        mockMvc.perform(get(CIC_FILE_FULL_ACCOUNTS_VIEW_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name(CIC_FILE_FULL_ACCOUNTS_VIEW))
                .andExpect(model().attributeExists(TEMPLATE_NAME_MODEL_ATTR));
    }

    @Test
    @DisplayName("Post cic file full accounts")
    void postCicFileFullAccounts() throws Exception {

        when(service.getNextControllerRedirect(controller.getClass()))
                .thenReturn(CIC_MOCK_CONTROLLER_PATH);

        mockMvc.perform(post(CIC_FILE_FULL_ACCOUNTS_VIEW_PATH))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name(CIC_MOCK_CONTROLLER_PATH));
    }
}