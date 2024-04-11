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
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.offbalancesheetarrangements.OffBalanceSheetArrangements;
import uk.gov.companieshouse.web.accounts.model.state.CompanyAccountsDataState;
import uk.gov.companieshouse.web.accounts.service.NoteService;
import uk.gov.companieshouse.web.accounts.service.navigation.NavigatorService;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

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
@TestInstance(Lifecycle.PER_CLASS)
class OffBalanceSheetArrangementsControllerTest {
    @Captor
    private ArgumentCaptor<String[]> captor = ArgumentCaptor.forClass(String[].class);

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

    @Mock
    private List<ValidationError> validationErrors;

    @InjectMocks
    private OffBalanceSheetArrangementsController controller;

    private static final String COMPANY_NUMBER = "companyNumber";

    private static final String TRANSACTION_ID = "transactionId";

    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";

    private static final String OFF_BALANCE_SHEET_ARRANGEMENTS_PATH = "/company/" + COMPANY_NUMBER +
                                                                                "/transaction/" + TRANSACTION_ID +
                                                                                "/company-accounts/" + COMPANY_ACCOUNTS_ID +
                                                                                "/small-full/off-balance-sheet-arrangements";

    private static final String TEMPLATE_NAME_MODEL_ATTR = "templateName";

    private static final String OFF_BALANCE_SHEET_ARRANGEMENTS_MODEL_ATTR = "offBalanceSheetArrangements";

    private static final String OFF_BALANCE_SHEET_ARRANGEMENTS_VIEW = "smallfull/offBalanceSheetArrangements";

    private static final String ERROR_VIEW = "error";

    private static final String COMPANY_ACCOUNTS_STATE = "companyAccountsDataState";

    private static final String MOCK_CONTROLLER_PATH = UrlBasedViewResolver.REDIRECT_URL_PREFIX + "mockControllerPath";

    private static final String OFF_BALANCE_SHEET_ARRANGEMENTS_DETAILS = "offBalanceSheetArrangementsDetails";

    @BeforeEach
    public void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    @DisplayName("Get off balance sheet arrangements - success")
    void getOffBalanceSheetArrangementsSuccess() throws Exception {
        when(noteService.get(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, NoteType.SMALL_FULL_OFF_BALANCE_SHEET_ARRANGEMENTS))
                .thenReturn(offBalanceSheetArrangements);

        mockMvc.perform(get(OFF_BALANCE_SHEET_ARRANGEMENTS_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name(OFF_BALANCE_SHEET_ARRANGEMENTS_VIEW))
                .andExpect(model().attributeExists(TEMPLATE_NAME_MODEL_ATTR))
                .andExpect(model().attributeExists(OFF_BALANCE_SHEET_ARRANGEMENTS_MODEL_ATTR));
    }

    @Test
    @DisplayName("Get off balance sheet arrangements - ServiceException")
    void getOffBalanceSheetArrangementsThrowsServiceException() throws Exception {
        when(noteService.get(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, NoteType.SMALL_FULL_OFF_BALANCE_SHEET_ARRANGEMENTS))
                .thenThrow(ServiceException.class);

        mockMvc.perform(get(OFF_BALANCE_SHEET_ARRANGEMENTS_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name(ERROR_VIEW));
    }

    @Test
    @DisplayName("Submit off balance sheet arrangements - success")
    void submitOffBalanceSheetArrangementsSuccess() throws Exception {
        when(noteService.submit(
                eq(TRANSACTION_ID), eq(COMPANY_ACCOUNTS_ID), any(OffBalanceSheetArrangements.class), eq(NoteType.SMALL_FULL_OFF_BALANCE_SHEET_ARRANGEMENTS)))
                        .thenReturn(validationErrors);

        when(validationErrors.isEmpty()).thenReturn(true);

        when(navigatorService.getNextControllerRedirect(any(), captor.capture())).thenReturn(MOCK_CONTROLLER_PATH);

        mockMvc.perform(post(OFF_BALANCE_SHEET_ARRANGEMENTS_PATH)
                .param(OFF_BALANCE_SHEET_ARRANGEMENTS_DETAILS, OFF_BALANCE_SHEET_ARRANGEMENTS_DETAILS))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name(MOCK_CONTROLLER_PATH));
    }

    @Test
    @DisplayName("Submit off balance sheet arrangements - validation errors")
    void submitOffBalanceSheetArrangementsWithValidationErrors() throws Exception {
        when(noteService.submit(
                eq(TRANSACTION_ID), eq(COMPANY_ACCOUNTS_ID), any(OffBalanceSheetArrangements.class), eq(NoteType.SMALL_FULL_OFF_BALANCE_SHEET_ARRANGEMENTS)))
                        .thenReturn(validationErrors);

        when(validationErrors.isEmpty()).thenReturn(false);

        mockMvc.perform(post(OFF_BALANCE_SHEET_ARRANGEMENTS_PATH)
                .param(OFF_BALANCE_SHEET_ARRANGEMENTS_DETAILS, OFF_BALANCE_SHEET_ARRANGEMENTS_DETAILS))
                .andExpect(status().isOk())
                .andExpect(view().name(OFF_BALANCE_SHEET_ARRANGEMENTS_VIEW));

        verify(navigatorService, never()).getNextControllerRedirect(any(), captor.capture());
    }

    @Test
    @DisplayName("Submit off balance sheet arrangements - ServiceException")
    void submitOffBalanceSheetArrangementsThrowsServiceException() throws Exception {
        when(noteService.submit(
                eq(TRANSACTION_ID), eq(COMPANY_ACCOUNTS_ID), any(OffBalanceSheetArrangements.class), eq(NoteType.SMALL_FULL_OFF_BALANCE_SHEET_ARRANGEMENTS)))
                        .thenThrow(ServiceException.class);

        mockMvc.perform(post(OFF_BALANCE_SHEET_ARRANGEMENTS_PATH)
                .param(OFF_BALANCE_SHEET_ARRANGEMENTS_DETAILS, OFF_BALANCE_SHEET_ARRANGEMENTS_DETAILS))
                .andExpect(status().isOk())
                .andExpect(view().name(ERROR_VIEW));
    }

    @Test
    @DisplayName("Submit off balance sheet arrangements - binding result errors")
    void submitOffBalanceSheetArrangementsWithBindingResultErrors() throws Exception {
        mockMvc.perform(post(OFF_BALANCE_SHEET_ARRANGEMENTS_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name(OFF_BALANCE_SHEET_ARRANGEMENTS_VIEW));

        verify(noteService, never())
                .submit(eq(TRANSACTION_ID), eq(COMPANY_ACCOUNTS_ID), any(OffBalanceSheetArrangements.class), eq(NoteType.SMALL_FULL_OFF_BALANCE_SHEET_ARRANGEMENTS));
    }

    @Test
    @DisplayName("Will render - true")
    void willRenderTrue() throws ServiceException {
        when(request.getSession()).thenReturn(httpSession);
        when(httpSession.getAttribute(COMPANY_ACCOUNTS_STATE)).thenReturn(companyAccountsDataState);
        when(companyAccountsDataState.getHasIncludedOffBalanceSheetArrangements()).thenReturn(true);
        assertTrue(controller.willRender(COMPANY_NUMBER, TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }

    @Test
    @DisplayName("Will render - false")
    void willRenderFalse() throws ServiceException {
        when(request.getSession()).thenReturn(httpSession);
        when(httpSession.getAttribute(COMPANY_ACCOUNTS_STATE)).thenReturn(companyAccountsDataState);
        when(companyAccountsDataState.getHasIncludedOffBalanceSheetArrangements()).thenReturn(false);
        assertFalse(controller.willRender(COMPANY_NUMBER, TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }
}
