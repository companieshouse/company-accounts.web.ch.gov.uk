package uk.gov.companieshouse.web.accounts.controller.smallfull;

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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ApprovalControllerTests {

    private MockMvc mockMvc;

    @InjectMocks
    private ApprovalController approvalController;

    private static final String COMPANY_NUMBER = "companyNumber";

    private static final String APPROVAL_VIEW = "smallfull/approval";

    private static final String APPROVAL_PATH = "/company/" + COMPANY_NUMBER +
            "/small-full/approval";

    @BeforeEach
    private void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(approvalController).build();
    }

    @Test
    @DisplayName("Get approval view success path")
    void getRequestSuccess() throws Exception {
        checkPathStatus(APPROVAL_PATH, APPROVAL_VIEW);
    }

    @Test
    @DisplayName("Post approval success path")
    void postRequestSuccess() throws Exception {
        checkPathStatus(APPROVAL_PATH, APPROVAL_VIEW);
    }

    private void checkPathStatus(String path, String view) throws Exception {
        this.mockMvc.perform(get(path))
                .andExpect(status().isOk())
                .andExpect(view().name(view));
    }
}
