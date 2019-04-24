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
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.currentassetsinvestments.CurrentAssetsInvestments;
import uk.gov.companieshouse.web.accounts.service.navigation.NavigatorService;
import uk.gov.companieshouse.web.accounts.service.smallfull.CurrentAssetsInvestmentsService;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CurrentAssetsInvestmentsControllerTests {

    private MockMvc mockMvc;

    @Mock
    private NavigatorService mockNavigatorService;

    @Mock
    private CurrentAssetsInvestmentsService mockCurrentAssetsInvestmentsService;

    @InjectMocks
    private CurrentAssetsInvestmentsController controller;

    private static final String TRANSACTION_ID = "transactionId";

    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";

    private static final String COMPANY_NUMBER = "companyNumber";

    private static final String SMALL_FULL_FIXED_ASSETS_INVESTMENTS_PATH = "/company/" + COMPANY_NUMBER +
        "/transaction/" + TRANSACTION_ID +
        "/company-accounts/" + COMPANY_ACCOUNTS_ID +
        "/small-full/current-assets-investments";

    private static final String CURRENT_ASSETS_INVESTMENTS_VIEW = "smallfull/currentAssetsInvestments";

    private static final String CURRENT_ASSETS_INVESTMENTS_MODEL_ATTR = "currentAssetsInvestments";

    private static final String BACK_BUTTON_MODEL_ATTR = "backButton";

    private static final String TEMPLATE_NAME_MODEL_ATTR = "templateName";

    private static final String MOCK_CONTROLLER_PATH = UrlBasedViewResolver.REDIRECT_URL_PREFIX + "mockControllerPath";

    private static final String ERROR_VIEW = "error";

    private static final String TEST_PATH = "currentAssetsInvestmentsDetails";


    @BeforeEach
    private void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    @DisplayName("Get currentAssetsInvestments view success path")
    void getRequestSuccess() throws Exception {

        when(mockNavigatorService.getPreviousControllerPath(any(), any()))
            .thenReturn(MOCK_CONTROLLER_PATH);
        when(mockCurrentAssetsInvestmentsService.getCurrentAssetsInvestments(
            TRANSACTION_ID, COMPANY_ACCOUNTS_ID, COMPANY_NUMBER))
            .thenReturn(new CurrentAssetsInvestments());

        this.mockMvc.perform(get(SMALL_FULL_FIXED_ASSETS_INVESTMENTS_PATH))
            .andExpect(status().isOk())
            .andExpect(view().name(CURRENT_ASSETS_INVESTMENTS_VIEW))
            .andExpect(model().attributeExists(CURRENT_ASSETS_INVESTMENTS_MODEL_ATTR))
            .andExpect(model().attributeExists(BACK_BUTTON_MODEL_ATTR))
            .andExpect(model().attributeExists(TEMPLATE_NAME_MODEL_ATTR));

        verify(mockCurrentAssetsInvestmentsService, times(1))
            .getCurrentAssetsInvestments(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, COMPANY_NUMBER);
    }

    @Test
    @DisplayName("Get currentAssetsInvestments view failure path due to error on currentAssetsInvestments retrieval")
    void getRequestFailureInGetFixedAssetsInvestments() throws Exception {

        when(mockCurrentAssetsInvestmentsService.getCurrentAssetsInvestments(
            TRANSACTION_ID, COMPANY_ACCOUNTS_ID, COMPANY_NUMBER))
            .thenThrow(ServiceException.class);

        this.mockMvc.perform(get(SMALL_FULL_FIXED_ASSETS_INVESTMENTS_PATH))
            .andExpect(status().isOk())
            .andExpect(view().name(ERROR_VIEW))
            .andExpect(model().attributeExists(TEMPLATE_NAME_MODEL_ATTR));
    }

    @Test
    @DisplayName("Post currentAssestInvestments success path")
    void postRequestSuccess() throws Exception {

        when(mockNavigatorService.getNextControllerRedirect(any(), ArgumentMatchers.<String>any()))
            .thenReturn(MOCK_CONTROLLER_PATH);
        when(mockCurrentAssetsInvestmentsService.submitCurrentAssetsInvestments(
            anyString(), anyString(), any(CurrentAssetsInvestments.class), anyString()))
            .thenReturn(new ArrayList<>());

        this.mockMvc.perform(post(SMALL_FULL_FIXED_ASSETS_INVESTMENTS_PATH))
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name(MOCK_CONTROLLER_PATH));
    }

    @Test
    @DisplayName("Post currentAssetsInvestments failure path")
    void postRequestFailure() throws Exception {

        doThrow(ServiceException.class)
            .when(mockCurrentAssetsInvestmentsService).submitCurrentAssetsInvestments(
                anyString(), anyString(), any(CurrentAssetsInvestments.class), anyString());

        this.mockMvc.perform(post(SMALL_FULL_FIXED_ASSETS_INVESTMENTS_PATH))
            .andExpect(status().isOk())
            .andExpect(view().name(ERROR_VIEW))
            .andExpect(model().attributeExists(TEMPLATE_NAME_MODEL_ATTR));
    }

    @Test
    @DisplayName("Post currentAssetsInvestments failure path with API validation errors")
    void postRequestFailureWithApiValidationErrors() throws Exception {

        ValidationError validationError = new ValidationError();
        validationError.setFieldPath(TEST_PATH);
        validationError.setMessageKey("invalid_character");

        List<ValidationError> errors = new ArrayList<>();
        errors.add(validationError);

        when(mockCurrentAssetsInvestmentsService.submitCurrentAssetsInvestments(
            anyString(), anyString(), any(CurrentAssetsInvestments.class), anyString()))
            .thenReturn(errors);

        this.mockMvc.perform(post(SMALL_FULL_FIXED_ASSETS_INVESTMENTS_PATH))
            .andExpect(status().isOk())
            .andExpect(view().name(CURRENT_ASSETS_INVESTMENTS_VIEW));
    }
}
