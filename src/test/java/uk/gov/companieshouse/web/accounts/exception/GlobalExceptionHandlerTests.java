package uk.gov.companieshouse.web.accounts.exception;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;
import uk.gov.companieshouse.web.accounts.controller.smallfull.StepsToCompleteController;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GlobalExceptionHandlerTests {

    private MockMvc mockMvc;

    @Mock
    private StepsToCompleteController controller;

    private static final String REQUEST_PATH = "/company/123456/small-full/steps-to-complete";

    private static final String ERROR_VIEW = "error";

    @BeforeEach
    private void setup() {

        when(controller.getStepsToComplete(anyString(), any(Model.class))).thenThrow(new RuntimeException());

        this.mockMvc = MockMvcBuilders.standaloneSetup(controller)
                            .setControllerAdvice(new GlobalExceptionHandler())
                            .build();
    }

    @Test
    @DisplayName("Global Exception Handler Test - Assert runtime exceptions are caught")
    public void testGlobalExceptionHandlerError() throws Exception {

        mockMvc.perform(get(REQUEST_PATH))
                .andExpect(status().is(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                .andExpect(view().name(ERROR_VIEW));
    }

}
