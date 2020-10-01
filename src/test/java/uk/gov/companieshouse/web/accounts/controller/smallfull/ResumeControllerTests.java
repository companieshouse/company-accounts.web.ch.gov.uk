package uk.gov.companieshouse.web.accounts.controller.smallfull;

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
import uk.gov.companieshouse.web.accounts.service.smallfull.ResumeService;
import uk.gov.companieshouse.web.accounts.service.navigation.NavigatorService;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ResumeControllerTests {

    private static final String COMPANY_NUMBER = "companyNumber";

    private static final String TRANSACTION_ID = "transactionId";

    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";

    private static final String MOCK_REDIRECT = "mockRedirect";

    private static final String RESUME_PATH = "/company/" + COMPANY_NUMBER +
            "/transaction/" + TRANSACTION_ID +
            "/company-accounts/" + COMPANY_ACCOUNTS_ID +
            "/small-full/resume";

    @Mock
    ResumeService resumeService;

    @Mock
    NavigatorService navigatorService;

    @InjectMocks
    private ResumeController controller;

    private MockMvc mockMvc;

    @BeforeEach
    private void setup() {

        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    @DisplayName("Get resume redirect success path")
    void getRequestSuccess() throws Exception {

        when(resumeService.getResumeRedirect(COMPANY_NUMBER, TRANSACTION_ID, COMPANY_ACCOUNTS_ID)).thenReturn(UrlBasedViewResolver.REDIRECT_URL_PREFIX + MOCK_REDIRECT);

        this.mockMvc.perform(get(RESUME_PATH))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name(UrlBasedViewResolver.REDIRECT_URL_PREFIX + MOCK_REDIRECT));

        verify(resumeService, times(1)).getResumeRedirect(COMPANY_NUMBER, TRANSACTION_ID, COMPANY_ACCOUNTS_ID);
    }
}
