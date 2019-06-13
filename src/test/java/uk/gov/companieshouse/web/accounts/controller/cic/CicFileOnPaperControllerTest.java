package uk.gov.companieshouse.web.accounts.controller.cic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CicFileOnPaperControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private CicFileOnPaperController controller;

    @BeforeEach
    private void setup() {

        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    private static final String CIC_FILE_PAPER_URL = "/accounts/cic/cics-file-paper";
    private static final String TEMPLATE_NAME_MODEL_ATTR = "templateName";

    @Test
    @DisplayName("Get File cic on paper Request - Success Path")
    void getRequestSuccess() throws Exception {
        this.mockMvc.perform(get(CIC_FILE_PAPER_URL))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists(TEMPLATE_NAME_MODEL_ATTR));
    }
}
