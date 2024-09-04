package uk.gov.companieshouse.web.accounts.controller.smallfull;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

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
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.state.CompanyAccountsDataState;
import uk.gov.companieshouse.web.accounts.service.navigation.NavigatorService;
import uk.gov.companieshouse.web.accounts.service.smallfull.ProfitAndLossService;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ProfitAndLossQuestionControllerTest {

    private static final String COMPANY_NUMBER = "companyNumber";
    private static final String TRANSACTION_ID = "transactionId";
    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";
    private static final String PROFIT_AND_LOSS_QUESTION_PATH = "/company/" + COMPANY_NUMBER +
            "/transaction/" + TRANSACTION_ID +
            "/company-accounts/" + COMPANY_ACCOUNTS_ID +
            "/small-full/profit-and-loss-question";
    private static final String PROFIT_AND_LOSS_QUESTION_MODEL_ATTR = "profitAndLossQuestion";
    private static final String TEMPLATE_NAME_MODEL_ATTR = "templateName";
    private static final String PROFIT_AND_LOSS_QUESTION_VIEW = "smallfull/profitAndLossQuestion";
    private static final String ERROR_VIEW = "error";
    private static final String PROFIT_AND_LOSS_SELECTION = "hasIncludedProfitAndLoss";
    private static final String MOCK_CONTROLLER_PATH =
            UrlBasedViewResolver.REDIRECT_URL_PREFIX + "mockControllerPath";
    private static final String COMPANY_ACCOUNTS_DATA_STATE = "companyAccountsDataState";
    private MockMvc mockMvc;
    @Mock
    private ProfitAndLossService profitAndLossService;
    @Mock
    private NavigatorService navigatorService;
    @InjectMocks
    private ProfitAndLossQuestionController controller;

    @BeforeEach
    public void setup() {

        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    @DisplayName("Get profit and loss")
    void getRequest() throws Exception {

        this.mockMvc.perform(get(PROFIT_AND_LOSS_QUESTION_PATH)
                        .sessionAttr(COMPANY_ACCOUNTS_DATA_STATE, new CompanyAccountsDataState()))
                .andExpect(status().isOk())
                .andExpect(view().name(PROFIT_AND_LOSS_QUESTION_VIEW))
                .andExpect(model().attributeExists(PROFIT_AND_LOSS_QUESTION_MODEL_ATTR))
                .andExpect(model().attributeExists(TEMPLATE_NAME_MODEL_ATTR));
    }

    @Test
    @DisplayName("Post profit and loss - has not included profit and loss")
    void postRequestHasNotIncludedProfitAndLoss() throws Exception {

        when(navigatorService.getNextControllerRedirect(
                any(Class.class),
                anyString(),
                anyString(),
                anyString()))
                .thenReturn(MOCK_CONTROLLER_PATH);

        this.mockMvc.perform(post(PROFIT_AND_LOSS_QUESTION_PATH)
                        .param(PROFIT_AND_LOSS_SELECTION, "0")
                        .sessionAttr(COMPANY_ACCOUNTS_DATA_STATE, new CompanyAccountsDataState()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name(MOCK_CONTROLLER_PATH));

        verify(profitAndLossService).deleteProfitAndLoss(TRANSACTION_ID, COMPANY_ACCOUNTS_ID,
                COMPANY_NUMBER);
    }

    @Test
    @DisplayName("Post profit and loss - has included profit and loss")
    void postRequestHasIncludedProfitAndLoss() throws Exception {

        when(navigatorService.getNextControllerRedirect(
                any(Class.class),
                anyString(),
                anyString(),
                anyString()))
                .thenReturn(MOCK_CONTROLLER_PATH);

        this.mockMvc.perform(post(PROFIT_AND_LOSS_QUESTION_PATH)
                        .param(PROFIT_AND_LOSS_SELECTION, "1")
                        .sessionAttr(COMPANY_ACCOUNTS_DATA_STATE, new CompanyAccountsDataState()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name(MOCK_CONTROLLER_PATH));

        verify(profitAndLossService, never()).deleteProfitAndLoss(TRANSACTION_ID,
                COMPANY_ACCOUNTS_ID, COMPANY_NUMBER);
    }

    @Test
    @DisplayName("Post profit and loss - service exception")
    void postRequestServiceException() throws Exception {

        doThrow(ServiceException.class)
                .when(profitAndLossService)
                .deleteProfitAndLoss(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, COMPANY_NUMBER);

        this.mockMvc.perform(post(PROFIT_AND_LOSS_QUESTION_PATH)
                        .param(PROFIT_AND_LOSS_SELECTION, "0"))
                .andExpect(status().isOk())
                .andExpect(view().name(ERROR_VIEW));

        verify(navigatorService, never()).getNextControllerRedirect(any(),
                ArgumentMatchers.<String>any());
    }

    @Test
    @DisplayName("Post profit and loss - binding result errors")
    void postRequestBindingResultErrors() throws Exception {

        this.mockMvc.perform(post(PROFIT_AND_LOSS_QUESTION_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name(PROFIT_AND_LOSS_QUESTION_VIEW));
    }
}
