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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.web.servlet.view.UrlBasedViewResolver.REDIRECT_URL_PREFIX;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CicSelectAccountTypeControllerTest {

    private MockMvc mockMvc;

    private static final String CIC_SELECT_ACCOUNT_TYPE_VIEW_PATH = "/accounts/cic/select-account-type";
    private static final String CIC_SELECT_ACCOUNT_TYPE_VIEW_NAME = "accountselector/selectAccountType";

    private static final String CIC_CANT_FILE_ONLINE_YET_VIEW_PATH = REDIRECT_URL_PREFIX
            + "/accounts/cic/cant-file-online-yet";
    private static final String CIC_FILE_FULL_ACCOUNTS_VIEW_PATH = REDIRECT_URL_PREFIX
            + "/accounts/cic/full-accounts-criteria";

    private static final String TEMPLATE_NAME_MODEL_ATTR = "templateName";
    private static final String TEMPLATE_ACCOUNT_TYPE_MODEL_ATTR = "typeOfAccounts";

    @Mock
    private NavigatorService service;

    @InjectMocks
    private CicSelectAccountTypeController controller;

    @BeforeEach
    void setUpBeforeEach() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    @DisplayName("Get cic select account type view success path")
    void getCicSelectAccountTypeRequest() throws Exception {

        mockMvc.perform(get(CIC_SELECT_ACCOUNT_TYPE_VIEW_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name(CIC_SELECT_ACCOUNT_TYPE_VIEW_NAME))
                .andExpect(model().attributeExists(TEMPLATE_ACCOUNT_TYPE_MODEL_ATTR))
                .andExpect(model().attributeExists(TEMPLATE_NAME_MODEL_ATTR));
    }

    @Test
    @DisplayName("Post cic select account type Binding Error")
    void postCicSelectAccountBindingError() throws Exception {

        mockMvc.perform(post(CIC_SELECT_ACCOUNT_TYPE_VIEW_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name(CIC_SELECT_ACCOUNT_TYPE_VIEW_NAME));
    }

    @Test
    @DisplayName("Post cic select account selection made - micro-entity")
    void postCicSelectAccountSelectionMadeMicro() throws Exception {

        mockMvc.perform(post(CIC_SELECT_ACCOUNT_TYPE_VIEW_PATH).
                param("selectedAccountTypeName", "micro-entity"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name(CIC_CANT_FILE_ONLINE_YET_VIEW_PATH));
    }

    @Test
    @DisplayName("Post cic select account selection made - abridged")
    void postCicSelectAccountSelectionMadeAbridged() throws Exception {

        mockMvc.perform(post(CIC_SELECT_ACCOUNT_TYPE_VIEW_PATH).
                param("selectedAccountTypeName", "abridged"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name(CIC_CANT_FILE_ONLINE_YET_VIEW_PATH));
    }

    @Test
    @DisplayName("Post cic select account selection made - dormant")
    void postCicSelectAccountSelectionMadeDormant() throws Exception {

        mockMvc.perform(post(CIC_SELECT_ACCOUNT_TYPE_VIEW_PATH).
                param("selectedAccountTypeName", "dormant"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name(CIC_CANT_FILE_ONLINE_YET_VIEW_PATH));
    }

    @Test
    @DisplayName("Post cic select account selection made - full")
    void postCicSelectAccountSelectionMadeFull() throws Exception {

        mockMvc.perform(post(CIC_SELECT_ACCOUNT_TYPE_VIEW_PATH).
                param("selectedAccountTypeName", "full"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name(CIC_FILE_FULL_ACCOUNTS_VIEW_PATH));
    }
}
