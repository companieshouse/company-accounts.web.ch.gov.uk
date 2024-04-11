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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CicFileOnPaperControllerTest {
    private MockMvc mockMvc;

    @Mock
    private NavigatorService service;

    @InjectMocks
    private CicFileOnPaperController controller;

    @BeforeEach
    public void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    private static final String CIC_FILE_PAPER_URL = "/accounts/cic/cics-file-paper?backLink=/test&accountType=test";
    private static final String TEMPLATE_NAME_MODEL_ATTR = "templateName";

    @Test
    @DisplayName("Get CIC file on paper view success path")
    void getRequestSuccess() throws Exception {
        mockMvc.perform(get(CIC_FILE_PAPER_URL))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists(TEMPLATE_NAME_MODEL_ATTR));
    }
}
