package uk.gov.companieshouse.web.accounts.controller.cic;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CicCantFileOnlineYetControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private CicCantFileOnlineYetController controller;

    @BeforeEach
    public void setup() {

        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    private static final String CIC_CANT_FILE_ONLINE_YET_VIEW_PATH = "/accounts/cic/cant-file-online-yet?backLink=/test&accountType=test";
    private static final String TEMPLATE_NAME_MODEL_ATTR = "templateName";
    private static final String TEMPLATE_ACCOUNT_TYPE_MODEL_ATTR = "accountType";


    @Test
    @DisplayName("Get Cic cant file online yet view success path")
    void getCicCantFindOnlineYetRequest() throws Exception {

        mockMvc.perform(get(CIC_CANT_FILE_ONLINE_YET_VIEW_PATH))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists(TEMPLATE_NAME_MODEL_ATTR))
                .andExpect(model().attributeExists(TEMPLATE_ACCOUNT_TYPE_MODEL_ATTR));
    }
}
