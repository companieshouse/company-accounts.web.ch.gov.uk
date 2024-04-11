package uk.gov.companieshouse.web.accounts.controller.smallfull;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.UrlBasedViewResolver;
import uk.gov.companieshouse.web.accounts.enumeration.NoteType;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.financialcommitments.FinancialCommitments;
import uk.gov.companieshouse.web.accounts.model.state.CompanyAccountsDataState;
import uk.gov.companieshouse.web.accounts.service.NoteService;
import uk.gov.companieshouse.web.accounts.service.navigation.NavigatorService;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FinancialCommitmentsControllerTest {

    private MockMvc mockMvc;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpSession httpSession;

    @Mock
    private NoteService<FinancialCommitments> noteService;

    @Mock
    private CompanyAccountsDataState companyAccountsDataState;

    @Mock
    private FinancialCommitments financialCommitments;

    @Mock
    private NavigatorService navigatorService;

    @Mock
    private List<ValidationError> validationErrors;

    @InjectMocks
    private FinancialCommitmentsController controller;

    private static final String COMPANY_NUMBER = "companyNumber";

    private static final String TRANSACTION_ID = "transactionId";

    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";

    private static final String FINANCIAL_COMMITMENTS_PATH = "/company/" + COMPANY_NUMBER +
            "/transaction/" + TRANSACTION_ID +
            "/company-accounts/" + COMPANY_ACCOUNTS_ID +
            "/small-full/financial-commitments";

    private static final String TEMPLATE_NAME_MODEL_ATTR = "templateName";

    private static final String FINANCIAL_COMMITMENTS_MODEL_ATTR = "financialCommitments";

    private static final String FINANCIAL_COMMITMENTS_VIEW = "smallfull/financialCommitments";

    private static final String ERROR_VIEW = "error";

    private static final String COMPANY_ACCOUNTS_STATE = "companyAccountsDataState";

    private static final String MOCK_CONTROLLER_PATH = UrlBasedViewResolver.REDIRECT_URL_PREFIX + "mockControllerPath";

    private static final String FINANCIAL_COMMITMENTS_DETAILS = "financialCommitmentsDetails";

    @BeforeEach
    private void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    @DisplayName("Get financial commitments - success")
    void getFinancialCommitmentsSuccess() throws Exception {

        when(noteService.get(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, NoteType.SMALL_FULL_FINANCIAL_COMMITMENTS))
                .thenReturn(financialCommitments);

        mockMvc.perform(get(FINANCIAL_COMMITMENTS_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name(FINANCIAL_COMMITMENTS_VIEW))
                .andExpect(model().attributeExists(TEMPLATE_NAME_MODEL_ATTR))
                .andExpect(model().attributeExists(FINANCIAL_COMMITMENTS_MODEL_ATTR));
    }

    @Test
    @DisplayName("Get financial commitments - ServiceException")
    void getFinancialCommitmentsThrowsServiceException() throws Exception {

        when(noteService.get(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, NoteType.SMALL_FULL_FINANCIAL_COMMITMENTS))
                .thenThrow(ServiceException.class);

        mockMvc.perform(get(FINANCIAL_COMMITMENTS_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name(ERROR_VIEW));
    }

    @Test
    @DisplayName("Submit financial commitments - success")
    void submitFinancialCommitmentsSuccess() throws Exception {

        when(noteService.submit(
                eq(TRANSACTION_ID), eq(COMPANY_ACCOUNTS_ID), any(FinancialCommitments.class), eq(NoteType.SMALL_FULL_FINANCIAL_COMMITMENTS)))
                .thenReturn(validationErrors);

        when(validationErrors.isEmpty()).thenReturn(true);

        when(navigatorService.getNextControllerRedirect(any(), ArgumentMatchers.<String>any())).thenReturn(MOCK_CONTROLLER_PATH);

        mockMvc.perform(post(FINANCIAL_COMMITMENTS_PATH)
                .param(FINANCIAL_COMMITMENTS_DETAILS, FINANCIAL_COMMITMENTS_DETAILS))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name(MOCK_CONTROLLER_PATH));
    }

    @Test
    @DisplayName("Submit financial commitments - ServiceException")
    void submitFinancialCommitmentsThrowsServiceException() throws Exception {

        when(noteService.submit(
                eq(TRANSACTION_ID), eq(COMPANY_ACCOUNTS_ID), any(FinancialCommitments.class), eq(NoteType.SMALL_FULL_FINANCIAL_COMMITMENTS)))
                .thenThrow(ServiceException.class);

        mockMvc.perform(post(FINANCIAL_COMMITMENTS_PATH)
                .param(FINANCIAL_COMMITMENTS_DETAILS, FINANCIAL_COMMITMENTS_DETAILS))
                .andExpect(status().isOk())
                .andExpect(view().name(ERROR_VIEW));
    }

    @Test
    @DisplayName("Submit financial commitments - validation errors")
    void submitFinancialCommitmentsWithValidationErrors() throws Exception {

        when(noteService.submit(
                eq(TRANSACTION_ID), eq(COMPANY_ACCOUNTS_ID), any(FinancialCommitments.class), eq(NoteType.SMALL_FULL_FINANCIAL_COMMITMENTS)))
                .thenReturn(validationErrors);

        when(validationErrors.isEmpty()).thenReturn(false);

        mockMvc.perform(post(FINANCIAL_COMMITMENTS_PATH)
                .param(FINANCIAL_COMMITMENTS_DETAILS, FINANCIAL_COMMITMENTS_DETAILS))
                .andExpect(status().isOk())
                .andExpect(view().name(FINANCIAL_COMMITMENTS_VIEW));

        verify(navigatorService, never()).getNextControllerRedirect(any(), ArgumentMatchers.<String>any());
    }

    @Test
    @DisplayName("Submit financial commitments - binding result errors")
    void submitFinancialCommitmentsWithBindingResultErrors() throws Exception {

        mockMvc.perform(post(FINANCIAL_COMMITMENTS_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name(FINANCIAL_COMMITMENTS_VIEW));

        verify(noteService, never())
                .submit(eq(TRANSACTION_ID), eq(COMPANY_ACCOUNTS_ID), any(FinancialCommitments.class), eq(NoteType.SMALL_FULL_FINANCIAL_COMMITMENTS));
    }

    @Test
    @DisplayName("Will render - true")
    void willRenderTrue() throws ServiceException {

        when(request.getSession()).thenReturn(httpSession);
        when(httpSession.getAttribute(COMPANY_ACCOUNTS_STATE)).thenReturn(companyAccountsDataState);
        when(companyAccountsDataState.getHasIncludedFinancialCommitments()).thenReturn(true);
        assertTrue(controller.willRender(COMPANY_NUMBER, TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }

    @Test
    @DisplayName("Will render - false")
    void willRenderFalse() throws ServiceException {

        when(request.getSession()).thenReturn(httpSession);
        when(httpSession.getAttribute(COMPANY_ACCOUNTS_STATE)).thenReturn(companyAccountsDataState);
        when(companyAccountsDataState.getHasIncludedFinancialCommitments()).thenReturn(false);
        assertFalse(controller.willRender(COMPANY_NUMBER, TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }
}