package uk.gov.companieshouse.web.accounts.controller.smallfull;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
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

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@ExtendWith(MockitoExtension.class)
@TestInstance(Lifecycle.PER_CLASS)
class FinancialCommitmentsQuestionControllerTest {
    @Captor
    private ArgumentCaptor<String[]> captor = ArgumentCaptor.forClass(String[].class);

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

    @InjectMocks
    private FinancialCommitmentsQuestionController controller;

    private static final String COMPANY_NUMBER = "companyNumber";

    private static final String TRANSACTION_ID = "transactionId";

    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";

    private static final String FINANCIAL_COMMITMENTS_QUESTION_PATH = "/company/" + COMPANY_NUMBER +
                                                                                "/transaction/" + TRANSACTION_ID +
                                                                                "/company-accounts/" + COMPANY_ACCOUNTS_ID +
                                                                                "/small-full/financial-commitments-question";

    private static final String TEMPLATE_NAME_MODEL_ATTR = "templateName";

    private static final String FINANCIAL_COMMITMENTS_QUESTION_MODEL_ATTR = "financialCommitmentsQuestion";

    private static final String FINANCIAL_COMMITMENTS_QUESTION_VIEW = "smallfull/financialCommitmentsQuestion";

    private static final String ERROR_VIEW = "error";

    private static final String HAS_INCLUDED_FINANCIAL_COMMITMENTS = "hasIncludedFinancialCommitments";

    private static final String COMPANY_ACCOUNTS_STATE = "companyAccountsDataState";

    private static final String MOCK_CONTROLLER_PATH = UrlBasedViewResolver.REDIRECT_URL_PREFIX + "mockControllerPath";

    private static final String COMMITMENTS = "commitments";

    @BeforeEach
    public void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    @DisplayName("Get financial commitments question - has commitments")
    void getFinancialCommitmentsQuestionHasCommitments() throws Exception {
        when(noteService.get(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, NoteType.SMALL_FULL_FINANCIAL_COMMITMENTS))
                .thenReturn(financialCommitments);

        when(financialCommitments.getFinancialCommitmentsDetails()).thenReturn(COMMITMENTS);

        mockMvc.perform(get(FINANCIAL_COMMITMENTS_QUESTION_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name(FINANCIAL_COMMITMENTS_QUESTION_VIEW))
                .andExpect(model().attributeExists(TEMPLATE_NAME_MODEL_ATTR))
                .andExpect(model().attributeExists(FINANCIAL_COMMITMENTS_QUESTION_MODEL_ATTR))
                .andExpect(model().attribute(FINANCIAL_COMMITMENTS_QUESTION_MODEL_ATTR, hasProperty(
                        HAS_INCLUDED_FINANCIAL_COMMITMENTS, is(true))));

        verify(request, never()).getSession();
        verify(request, never()).getSession();
    }

    @Test
    @DisplayName("Get financial commitments question - does not have commitments")
    void getFinancialCommitmentsQuestionDoesNotHaveCommitments() throws Exception {
        when(noteService.get(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, NoteType.SMALL_FULL_FINANCIAL_COMMITMENTS))
                .thenReturn(financialCommitments);

        when(financialCommitments.getFinancialCommitmentsDetails()).thenReturn(null);

        when(request.getSession()).thenReturn(httpSession);

        when(httpSession.getAttribute(COMPANY_ACCOUNTS_STATE)).thenReturn(companyAccountsDataState);

        when(companyAccountsDataState.getHasIncludedFinancialCommitments()).thenReturn(false);

        mockMvc.perform(get(FINANCIAL_COMMITMENTS_QUESTION_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name(FINANCIAL_COMMITMENTS_QUESTION_VIEW))
                .andExpect(model().attributeExists(TEMPLATE_NAME_MODEL_ATTR))
                .andExpect(model().attributeExists(FINANCIAL_COMMITMENTS_QUESTION_MODEL_ATTR))
                .andExpect(model().attribute(FINANCIAL_COMMITMENTS_QUESTION_MODEL_ATTR, hasProperty(
                        HAS_INCLUDED_FINANCIAL_COMMITMENTS, is(false))));
    }

    @Test
    @DisplayName("Get financial commitments question - ServiceException")
    void getFinancialCommitmentsQuestionThrowsServiceException() throws Exception {
        when(noteService.get(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, NoteType.SMALL_FULL_FINANCIAL_COMMITMENTS))
                .thenThrow(ServiceException.class);

        mockMvc.perform(get(FINANCIAL_COMMITMENTS_QUESTION_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name(ERROR_VIEW));
    }

    @Test
    @DisplayName("Submit financial commitments question - has commitments")
    void submitFinancialCommitmentsQuestionHasCommitments() throws Exception {
        when(request.getSession()).thenReturn(httpSession);

        when(httpSession.getAttribute(COMPANY_ACCOUNTS_STATE)).thenReturn(companyAccountsDataState);

        when(navigatorService.getNextControllerRedirect(any(), captor.capture())).thenReturn(MOCK_CONTROLLER_PATH);

        mockMvc.perform(post(FINANCIAL_COMMITMENTS_QUESTION_PATH)
                .param(HAS_INCLUDED_FINANCIAL_COMMITMENTS, "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name(MOCK_CONTROLLER_PATH));

        verify(noteService, never()).delete(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, NoteType.SMALL_FULL_FINANCIAL_COMMITMENTS);

        verify(companyAccountsDataState).setHasIncludedFinancialCommitments(true);
    }

    @Test
    @DisplayName("Submit financial commitments question - does not have commitments")
    void submitFinancialCommitmentsQuestionDoesNotHaveCommitments() throws Exception {
        when(request.getSession()).thenReturn(httpSession);

        when(httpSession.getAttribute(COMPANY_ACCOUNTS_STATE)).thenReturn(companyAccountsDataState);

        when(navigatorService.getNextControllerRedirect(any(), captor.capture())).thenReturn(MOCK_CONTROLLER_PATH);

        mockMvc.perform(post(FINANCIAL_COMMITMENTS_QUESTION_PATH)
                .param(HAS_INCLUDED_FINANCIAL_COMMITMENTS, "0"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name(MOCK_CONTROLLER_PATH));

        verify(noteService).delete(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, NoteType.SMALL_FULL_FINANCIAL_COMMITMENTS);

        verify(companyAccountsDataState).setHasIncludedFinancialCommitments(false);
    }

    @Test
    @DisplayName("Submit financial commitments question - ServiceException")
    void submitFinancialCommitmentsQuestionThrowsServiceException() throws Exception {
        doThrow(ServiceException.class)
                .when(noteService)
                        .delete(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, NoteType.SMALL_FULL_FINANCIAL_COMMITMENTS);

        mockMvc.perform(post(FINANCIAL_COMMITMENTS_QUESTION_PATH)
                .param(HAS_INCLUDED_FINANCIAL_COMMITMENTS, "0"))
                .andExpect(status().isOk())
                .andExpect(view().name(ERROR_VIEW));
    }

    @Test
    @DisplayName("Submit financial commitments question - binding result errors")
    void submitFinancialCommitmentsQuestionWithBindingResultErrors() throws Exception {
        mockMvc.perform(post(FINANCIAL_COMMITMENTS_QUESTION_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name(FINANCIAL_COMMITMENTS_QUESTION_VIEW));
    }
}
