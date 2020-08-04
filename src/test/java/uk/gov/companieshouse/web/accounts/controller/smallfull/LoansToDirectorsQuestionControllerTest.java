package uk.gov.companieshouse.web.accounts.controller.smallfull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.UrlBasedViewResolver;
import uk.gov.companieshouse.web.accounts.model.state.CompanyAccountsDataState;
import uk.gov.companieshouse.web.accounts.service.navigation.NavigatorService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class LoansToDirectorsQuestionControllerTest {

    private MockMvc mockMvc;

    @Mock
    private NavigatorService navigatorService;

    @InjectMocks
    private LoansToDirectorsQuestionController controller;

    private static final String COMPANY_NUMBER = "companyNumber";

    private static final String TRANSACTION_ID = "transactionId";

    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";

    private static final String LOANS_TO_DIRECTORS_QUESTION_PATH = "/company/" + COMPANY_NUMBER +
                                                                "/transaction/" + TRANSACTION_ID +
                                                                "/company-accounts/" + COMPANY_ACCOUNTS_ID +
                                                                "/small-full/notes/loans-to-directors-question";

    private static final String LOANS_TO_DIRECTORS_QUESTION_MODEL_ATTR = "loansToDirectorsQuestion";

    private static final String TEMPLATE_NAME_MODEL_ATTR = "templateName";

    private static final String LOANS_TO_DIRECTORS_QUESTION_VIEW = "smallfull/loansToDirectorsQuestion";

    private static final String LOANS_TO_DIRECTORS_SELECTION = "hasIncludedLoansToDirectors";

    private static final String MOCK_CONTROLLER_PATH = UrlBasedViewResolver.REDIRECT_URL_PREFIX + "mockControllerPath";

    private static final String COMPANY_ACCOUNTS_DATA_STATE = "companyAccountsDataState";
    
    @BeforeEach
    private void setup() {

        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    @DisplayName("Get loans to directors question")
    void getRequest() throws Exception {
        
        this.mockMvc.perform(get(LOANS_TO_DIRECTORS_QUESTION_PATH)
            .sessionAttr(COMPANY_ACCOUNTS_DATA_STATE, new CompanyAccountsDataState()))
            .andExpect(status().isOk())
            .andExpect(view().name(LOANS_TO_DIRECTORS_QUESTION_VIEW))
            .andExpect(model().attributeExists(LOANS_TO_DIRECTORS_QUESTION_MODEL_ATTR))
            .andExpect(model().attributeExists(TEMPLATE_NAME_MODEL_ATTR));
    }

    @Test
    @DisplayName("Post loans to directors - has not included loans to directors")
    void postRequestHasNotIncludedLoansToDirectors() throws Exception {

        when(navigatorService.getNextControllerRedirect(any(), ArgumentMatchers.<String>any())).thenReturn(MOCK_CONTROLLER_PATH);
        
        this.mockMvc.perform(post(LOANS_TO_DIRECTORS_QUESTION_PATH)
            .param(LOANS_TO_DIRECTORS_SELECTION, "0")
            .sessionAttr(COMPANY_ACCOUNTS_DATA_STATE, new CompanyAccountsDataState()))
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name(MOCK_CONTROLLER_PATH));
    }

    @Test
    @DisplayName("Post loans to directors - has included loans to directors")
    void postRequestHasIncludedLoansToDirectors() throws Exception {

        when(navigatorService.getNextControllerRedirect(any(), ArgumentMatchers.<String>any())).thenReturn(MOCK_CONTROLLER_PATH);

        this.mockMvc.perform(post(LOANS_TO_DIRECTORS_QUESTION_PATH)
                .param(LOANS_TO_DIRECTORS_SELECTION, "1")
                .sessionAttr(COMPANY_ACCOUNTS_DATA_STATE, new CompanyAccountsDataState()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name(MOCK_CONTROLLER_PATH));
    }

    @Test
    @DisplayName("Post loans to directors - binding result errors")
    void postRequestBindingResultErrors() throws Exception {

        this.mockMvc.perform(post(LOANS_TO_DIRECTORS_QUESTION_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name(LOANS_TO_DIRECTORS_QUESTION_VIEW));
    }
}
