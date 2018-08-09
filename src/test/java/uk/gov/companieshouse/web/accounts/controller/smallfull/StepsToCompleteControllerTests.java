package uk.gov.companieshouse.web.accounts.controller.smallfull;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
import uk.gov.companieshouse.web.accounts.service.transaction.TransactionService;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class StepsToCompleteControllerTests {

    private MockMvc mockMvc;

    @Mock
    TransactionService transactionService;

    @InjectMocks
    StepsToCompleteController controller;

    private static final String COMPANY_NUMBER = "companyNumber";

    private static final String STEPS_TO_COMPLETE_PATH = "/company/" + COMPANY_NUMBER +
                                                            "/small-full/steps-to-complete";

    private static final String STEPS_TO_COMPLETE_VIEW = "smallfull/stepsToComplete";

    @BeforeEach
    private void setup() {

        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    @DisplayName("Get steps to complete view success path")
    void getRequestSuccess() throws Exception {

        this.mockMvc.perform(get(STEPS_TO_COMPLETE_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name(STEPS_TO_COMPLETE_VIEW));
    }

    @Test
    @DisplayName("Post balance sheet success path")
    void postRequestSuccess() throws Exception {

        this.mockMvc.perform(post(STEPS_TO_COMPLETE_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name(STEPS_TO_COMPLETE_VIEW));

        verify(transactionService, times(1)).createTransaction(COMPANY_NUMBER);
    }

}
