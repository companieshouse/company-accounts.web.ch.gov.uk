package uk.gov.companieshouse.web.accounts.controller.smallfull;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.UrlBasedViewResolver;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.profitandloss.ProfitAndLoss;
import uk.gov.companieshouse.web.accounts.model.state.CompanyAccountsDataState;
import uk.gov.companieshouse.web.accounts.service.navigation.NavigatorService;
import uk.gov.companieshouse.web.accounts.service.smallfull.ProfitAndLossService;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ProfitAndLossControllerTests {

    private MockMvc mockMvc;

    @Mock
    private ProfitAndLossService profitAndLossService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private NavigatorService navigatorService;

    @InjectMocks
    private ProfitAndLossController controller;

    @Mock
    private List<ValidationError> validationErrors;

    @Mock
    private MockHttpSession session;

    @Mock
    private CompanyAccountsDataState companyAccountsDataState;

    private static final String COMPANY_NUMBER = "companyNumber";

    private static final String TRANSACTION_ID = "transactionId";

    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";

    private static final String PROFIT_AND_LOSS_PATH = "/company/" + COMPANY_NUMBER +
                                                     "/transaction/" + TRANSACTION_ID +
                                                     "/company-accounts/" + COMPANY_ACCOUNTS_ID +
                                                     "/small-full/profit-and-loss";

    private static final String PROFIT_AND_LOSS_MODEL_ATTR = "profitAndLoss";

    private static final String TEMPLATE_NAME_MODEL_ATTR = "templateName";

    private static final String PROFIT_AND_LOSS_VIEW = "smallfull/profitAndLoss";

    private static final String ERROR_VIEW = "error";

    private static final String MOCK_CONTROLLER_PATH = UrlBasedViewResolver.REDIRECT_URL_PREFIX + "mockControllerPath";

    private static final String COMPANY_ACCOUNTS_DATA_STATE = "companyAccountsDataState";

    @BeforeEach
    private void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    @DisplayName("Get profit and loss - success")
    void getRequestSuccess() throws Exception {

        when(profitAndLossService.getProfitAndLoss(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, COMPANY_NUMBER))
                .thenReturn(new ProfitAndLoss());

        this.mockMvc.perform(get(PROFIT_AND_LOSS_PATH))
                    .andExpect(status().isOk())
                    .andExpect(view().name(PROFIT_AND_LOSS_VIEW))
                    .andExpect(model().attributeExists(PROFIT_AND_LOSS_MODEL_ATTR))
                    .andExpect(model().attributeExists(TEMPLATE_NAME_MODEL_ATTR));
    }

    @Test
    @DisplayName("Get profit and loss - service exception")
    void getRequestServiceException() throws Exception {

        doThrow(ServiceException.class)
                .when(profitAndLossService)
                        .getProfitAndLoss(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, COMPANY_NUMBER);

        this.mockMvc.perform(get(PROFIT_AND_LOSS_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name(ERROR_VIEW));
    }

    @Test
    @DisplayName("Post profit and loss - success")
    void postRequestSuccess() throws Exception {

        when(profitAndLossService.submitProfitAndLoss(
                eq(TRANSACTION_ID), eq(COMPANY_ACCOUNTS_ID), eq(COMPANY_NUMBER), any(ProfitAndLoss.class)))
                        .thenReturn(validationErrors);

        when(validationErrors.isEmpty()).thenReturn(true);

        when(navigatorService.getNextControllerRedirect(any(), ArgumentMatchers.<String>any()))
                .thenReturn(MOCK_CONTROLLER_PATH);

        this.mockMvc.perform(post(PROFIT_AND_LOSS_PATH))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name(MOCK_CONTROLLER_PATH));
    }

    @Test
    @DisplayName("Post profit and loss - service exception")
    void postRequestFailure() throws Exception {

        doThrow(ServiceException.class)
                .when(profitAndLossService).submitProfitAndLoss(
                        eq(TRANSACTION_ID), eq(COMPANY_ACCOUNTS_ID), eq(COMPANY_NUMBER), any(ProfitAndLoss.class));

        this.mockMvc.perform(post(PROFIT_AND_LOSS_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name(ERROR_VIEW));
    }

    @Test
    @DisplayName("Post profit and loss - validation errors")
    void postRequestFailureWithApiValidationErrors() throws Exception {

        when(profitAndLossService.submitProfitAndLoss(
                eq(TRANSACTION_ID), eq(COMPANY_ACCOUNTS_ID), eq(COMPANY_NUMBER), any(ProfitAndLoss.class)))
                        .thenReturn(validationErrors);

        when(validationErrors.isEmpty()).thenReturn(false);

        this.mockMvc.perform(post(PROFIT_AND_LOSS_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name(PROFIT_AND_LOSS_VIEW));
    }
    
    @Test
    @DisplayName("Post profit and loss - binding result errors")
    void postRequestBindingResultErrors() throws Exception {

        String beanElement = "grossProfitOrLoss.turnover.currentAmount";
        // Mock non-numeric input to trigger binding result errors
        String invalidData = "test";

        this.mockMvc.perform(post(PROFIT_AND_LOSS_PATH)
                .param(beanElement, invalidData))
                .andExpect(status().isOk())
                .andExpect(view().name(PROFIT_AND_LOSS_VIEW))
                .andExpect(model().attributeExists(TEMPLATE_NAME_MODEL_ATTR));
    }

    @Test
    @DisplayName("Will render - false")
    void willRenderFalse() throws ServiceException {

        when(request.getSession()).thenReturn(session);
        when(session.getAttribute(COMPANY_ACCOUNTS_DATA_STATE)).thenReturn(companyAccountsDataState);
        when(companyAccountsDataState.getHasIncludedProfitAndLoss()).thenReturn(false);

        assertFalse(controller.willRender(COMPANY_NUMBER, TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }

    @Test
    @DisplayName("Will render - true")
    void willRenderTrue() throws ServiceException {

        when(request.getSession()).thenReturn(session);
        when(session.getAttribute(COMPANY_ACCOUNTS_DATA_STATE)).thenReturn(companyAccountsDataState);
        when(companyAccountsDataState.getHasIncludedProfitAndLoss()).thenReturn(true);

        assertTrue(controller.willRender(COMPANY_NUMBER, TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }
}
