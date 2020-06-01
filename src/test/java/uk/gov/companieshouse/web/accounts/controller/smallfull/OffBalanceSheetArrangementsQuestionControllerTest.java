package uk.gov.companieshouse.web.accounts.controller.smallfull;

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

import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
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
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.offbalancesheetarrangements.OffBalanceSheetArrangements;
import uk.gov.companieshouse.web.accounts.model.state.CompanyAccountsDataState;
import uk.gov.companieshouse.web.accounts.service.NoteService;
import uk.gov.companieshouse.web.accounts.service.navigation.NavigatorService;

@ExtendWith(MockitoExtension.class)
@TestInstance(Lifecycle.PER_CLASS)
public class OffBalanceSheetArrangementsQuestionControllerTest {

    private MockMvc mockMvc;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpSession httpSession;

    @Mock
    private NoteService<OffBalanceSheetArrangements> noteService;

    @Mock
    private CompanyAccountsDataState companyAccountsDataState;

    @Mock
    private OffBalanceSheetArrangements offBalanceSheetArrangements;

    @Mock
    private NavigatorService navigatorService;

    @InjectMocks
    private OffBalanceSheetArrangementsQuestionController controller;

    private static final String COMPANY_NUMBER = "companyNumber";

    private static final String TRANSACTION_ID = "transactionId";

    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";

    private static final String OFF_BALANCE_SHEET_ARRANGEMENTS_QUESTION_PATH = "/company/" + COMPANY_NUMBER +
                                                                                "/transaction/" + TRANSACTION_ID +
                                                                                "/company-accounts/" + COMPANY_ACCOUNTS_ID +
                                                                                "/small-full/off-balance-sheet-arrangements-question";

    private static final String TEMPLATE_NAME_MODEL_ATTR = "templateName";

    private static final String OFF_BALANCE_SHEET_ARRANGEMENTS_QUESTION_MODEL_ATTR = "offBalanceSheetArrangementsQuestion";

    private static final String OFF_BALANCE_SHEET_ARRANGEMENTS_QUESTION_VIEW = "smallfull/offBalanceSheetArrangementsQuestion";

    private static final String ERROR_VIEW = "error";

    private static final String HAS_INCLUDED_OFF_BALANCE_SHEET_ARRANGEMENTS = "hasIncludedOffBalanceSheetArrangements";

    private static final String COMPANY_ACCOUNTS_STATE = "companyAccountsDataState";

    private static final String MOCK_CONTROLLER_PATH = UrlBasedViewResolver.REDIRECT_URL_PREFIX + "mockControllerPath";

    private static final String ARRANGEMENTS = "arrangements";

    @BeforeEach
    private void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    @DisplayName("Get off balance sheet arrangements question - has arrangements")
    void getOffBalanceSheetArrangementsQuestionHasArrangements() throws Exception {

        when(noteService.get(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, NoteType.SMALL_FULL_OFF_BALANCE_SHEET_ARRANGEMENTS))
                .thenReturn(Optional.of(offBalanceSheetArrangements));

        when(offBalanceSheetArrangements.getOffBalanceSheetArrangementsDetails()).thenReturn(ARRANGEMENTS);

        mockMvc.perform(get(OFF_BALANCE_SHEET_ARRANGEMENTS_QUESTION_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name(OFF_BALANCE_SHEET_ARRANGEMENTS_QUESTION_VIEW))
                .andExpect(model().attributeExists(TEMPLATE_NAME_MODEL_ATTR))
                .andExpect(model().attributeExists(OFF_BALANCE_SHEET_ARRANGEMENTS_QUESTION_MODEL_ATTR))
                .andExpect(model().attribute(OFF_BALANCE_SHEET_ARRANGEMENTS_QUESTION_MODEL_ATTR, hasProperty(
                        HAS_INCLUDED_OFF_BALANCE_SHEET_ARRANGEMENTS, is(true))));

        verify(request, never()).getSession();
    }

    @Test
    @DisplayName("Get off balance sheet arrangements question - does not have arrangements")
    void getOffBalanceSheetArrangementsQuestionDoesNotHaveArrangements() throws Exception {

        when(noteService.get(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, NoteType.SMALL_FULL_OFF_BALANCE_SHEET_ARRANGEMENTS))
                .thenReturn(Optional.of(offBalanceSheetArrangements));

        when(offBalanceSheetArrangements.getOffBalanceSheetArrangementsDetails()).thenReturn(null);

        when(request.getSession()).thenReturn(httpSession);

        when(httpSession.getAttribute(COMPANY_ACCOUNTS_STATE)).thenReturn(companyAccountsDataState);

        when(companyAccountsDataState.getHasIncludedOffBalanceSheetArrangements()).thenReturn(false);

        mockMvc.perform(get(OFF_BALANCE_SHEET_ARRANGEMENTS_QUESTION_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name(OFF_BALANCE_SHEET_ARRANGEMENTS_QUESTION_VIEW))
                .andExpect(model().attributeExists(TEMPLATE_NAME_MODEL_ATTR))
                .andExpect(model().attributeExists(OFF_BALANCE_SHEET_ARRANGEMENTS_QUESTION_MODEL_ATTR))
                .andExpect(model().attribute(OFF_BALANCE_SHEET_ARRANGEMENTS_QUESTION_MODEL_ATTR, hasProperty(
                        HAS_INCLUDED_OFF_BALANCE_SHEET_ARRANGEMENTS, is(false))));
    }

    @Test
    @DisplayName("Get off balance sheet arrangements question - ServiceException")
    void getOffBalanceSheetArrangementsQuestionThrowsServiceException() throws Exception {

        when(noteService.get(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, NoteType.SMALL_FULL_OFF_BALANCE_SHEET_ARRANGEMENTS))
                .thenThrow(ServiceException.class);

        mockMvc.perform(get(OFF_BALANCE_SHEET_ARRANGEMENTS_QUESTION_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name(ERROR_VIEW));
    }

    @Test
    @DisplayName("Submit off balance sheet arrangements question - has arrangements")
    void submitOffBalanceSheetArrangementsQuestionHasArrangements() throws Exception {

        when(request.getSession()).thenReturn(httpSession);

        when(httpSession.getAttribute(COMPANY_ACCOUNTS_STATE)).thenReturn(companyAccountsDataState);

        when(navigatorService.getNextControllerRedirect(any(), ArgumentMatchers.<String>any())).thenReturn(MOCK_CONTROLLER_PATH);

        mockMvc.perform(post(OFF_BALANCE_SHEET_ARRANGEMENTS_QUESTION_PATH)
                .param(HAS_INCLUDED_OFF_BALANCE_SHEET_ARRANGEMENTS, "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name(MOCK_CONTROLLER_PATH));

        verify(noteService, never()).delete(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, NoteType.SMALL_FULL_OFF_BALANCE_SHEET_ARRANGEMENTS);

        verify(companyAccountsDataState).setHasIncludedOffBalanceSheetArrangements(true);
    }

    @Test
    @DisplayName("Submit off balance sheet arrangements question - does not have arrangements")
    void submitOffBalanceSheetArrangementsQuestionDoesNotHaveArrangements() throws Exception {

        when(request.getSession()).thenReturn(httpSession);

        when(httpSession.getAttribute(COMPANY_ACCOUNTS_STATE)).thenReturn(companyAccountsDataState);

        when(navigatorService.getNextControllerRedirect(any(), ArgumentMatchers.<String>any())).thenReturn(MOCK_CONTROLLER_PATH);

        mockMvc.perform(post(OFF_BALANCE_SHEET_ARRANGEMENTS_QUESTION_PATH)
                .param(HAS_INCLUDED_OFF_BALANCE_SHEET_ARRANGEMENTS, "0"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name(MOCK_CONTROLLER_PATH));

        verify(noteService).delete(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, NoteType.SMALL_FULL_OFF_BALANCE_SHEET_ARRANGEMENTS);

        verify(companyAccountsDataState).setHasIncludedOffBalanceSheetArrangements(false);
    }

    @Test
    @DisplayName("Submit off balance sheet arrangements question - ServiceException")
    void submitOffBalanceSheetArrangementsQuestionThrowsServiceException() throws Exception {

        doThrow(ServiceException.class)
                .when(noteService)
                        .delete(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, NoteType.SMALL_FULL_OFF_BALANCE_SHEET_ARRANGEMENTS);

        mockMvc.perform(post(OFF_BALANCE_SHEET_ARRANGEMENTS_QUESTION_PATH)
                .param(HAS_INCLUDED_OFF_BALANCE_SHEET_ARRANGEMENTS, "0"))
                .andExpect(status().isOk())
                .andExpect(view().name(ERROR_VIEW));
    }

    @Test
    @DisplayName("Submit off balance sheet arrangements question - binding result errors")
    void submitOffBalanceSheetArrangementsQuestionWithBindingResultErrors() throws Exception {

        mockMvc.perform(post(OFF_BALANCE_SHEET_ARRANGEMENTS_QUESTION_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name(OFF_BALANCE_SHEET_ARRANGEMENTS_QUESTION_VIEW));
    }
}
