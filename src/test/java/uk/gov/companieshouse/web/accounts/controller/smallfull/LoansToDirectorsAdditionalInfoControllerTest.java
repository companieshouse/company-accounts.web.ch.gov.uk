package uk.gov.companieshouse.web.accounts.controller.smallfull;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.UrlBasedViewResolver;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.loanstodirectors.LoansToDirectorsAdditionalInfo;
import uk.gov.companieshouse.web.accounts.model.state.CompanyAccountsDataState;
import uk.gov.companieshouse.web.accounts.service.navigation.NavigatorService;
import uk.gov.companieshouse.web.accounts.service.smallfull.LoansToDirectorsAdditionalInfoService;
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
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class LoansToDirectorsAdditionalInfoControllerTest {
    @Captor
    private ArgumentCaptor<String[]> captor = ArgumentCaptor.forClass(String[].class);

    private static final String COMPANY_NUMBER = "companyNumber";

    private static final String TRANSACTION_ID = "transactionId";

    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";

    private static final String ADDITIONAL_INFORMATION_PATH = "/company/" + COMPANY_NUMBER +
                                                                "/transaction/" + TRANSACTION_ID +
                                                                "/company-accounts/" + COMPANY_ACCOUNTS_ID +
                                                                "/small-full/notes/add-or-remove-loans/additional-information";

    private static final String ADDITIONAL_INFORMATION_MODEL_ATTR = "loansToDirectorsAdditionalInfo";

    private static final String TEMPLATE_NAME_MODEL_ATTR = "templateName";

    private static final String ADDITIONAL_INFORMATION_VIEW = "smallfull/loansToDirectorsAdditionalInfo";

    private static final String ERROR_VIEW = "error";

    private static final String ADDITIONAL_INFORMATION_DETAILS = "additionalInfoDetails";

    private static final String MOCK_CONTROLLER_PATH = UrlBasedViewResolver.REDIRECT_URL_PREFIX + "mockControllerPath";

    private static final String COMPANY_ACCOUNTS_DATA_STATE = "companyAccountsDataState";

    private MockMvc mockMvc;

    @Mock
    private HttpServletRequest request;

    @Mock
    private LoansToDirectorsAdditionalInfoService additionalInformationService;

    @Mock
    private NavigatorService navigatorService;

    @Mock
    private LoansToDirectorsAdditionalInfo additionalInformation;

    @Mock
    private List<ValidationError> validationErrors;

    @Mock
    private MockHttpSession session;

    @Mock
    private CompanyAccountsDataState companyAccountsDataState;

    @InjectMocks
    private LoansToDirectorsAdditionalInfoController controller;

    @BeforeEach
    public void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    @DisplayName("Get request - success")
    void getAdditionalInformationSuccess() throws Exception {
        when(additionalInformationService.getAdditionalInformation(TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(additionalInformation);

        this.mockMvc.perform(get(ADDITIONAL_INFORMATION_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name(ADDITIONAL_INFORMATION_VIEW))
                .andExpect(model().attributeExists(ADDITIONAL_INFORMATION_MODEL_ATTR))
                .andExpect(model().attributeExists(TEMPLATE_NAME_MODEL_ATTR));
    }

    @Test
    @DisplayName("Get request - service exception")
    void getAdditionalInformationThrowsServiceException() throws Exception {
        when(additionalInformationService.getAdditionalInformation(TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenThrow(ServiceException.class);

        this.mockMvc.perform(get(ADDITIONAL_INFORMATION_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name(ERROR_VIEW));
    }

    @Test
    @DisplayName("Post request - success")
    void postAdditionalInformationSuccess() throws Exception {
        when(additionalInformationService
                .getAdditionalInformation(eq(TRANSACTION_ID), eq(COMPANY_ACCOUNTS_ID)))
                .thenReturn(additionalInformation);

        when(additionalInformationService
                .createAdditionalInformation(eq(TRANSACTION_ID), eq(COMPANY_ACCOUNTS_ID), any(LoansToDirectorsAdditionalInfo.class)))
                .thenReturn(validationErrors);

        when(validationErrors.isEmpty()).thenReturn(true);

        when(navigatorService.getNextControllerRedirect(any(), captor.capture())).thenReturn(MOCK_CONTROLLER_PATH);

        this.mockMvc.perform(post(ADDITIONAL_INFORMATION_PATH)
                .param(ADDITIONAL_INFORMATION_DETAILS, ADDITIONAL_INFORMATION_DETAILS))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name(MOCK_CONTROLLER_PATH));
    }

    @Test
    @DisplayName("Post request - validation errors")
    void postAdditionalInformationWithValidationErrors() throws Exception {
        when(additionalInformationService
                .getAdditionalInformation(eq(TRANSACTION_ID), eq(COMPANY_ACCOUNTS_ID)))
                .thenReturn(additionalInformation);

        when(additionalInformationService
                .createAdditionalInformation(eq(TRANSACTION_ID), eq(COMPANY_ACCOUNTS_ID), any(LoansToDirectorsAdditionalInfo.class)))
                .thenReturn(validationErrors);

        when(validationErrors.isEmpty()).thenReturn(false);

        this.mockMvc.perform(post(ADDITIONAL_INFORMATION_PATH)
                .param(ADDITIONAL_INFORMATION_DETAILS, ADDITIONAL_INFORMATION_DETAILS))
                .andExpect(status().isOk())
                .andExpect(view().name(ADDITIONAL_INFORMATION_VIEW));

        verify(navigatorService, never()).getNextControllerRedirect(any(), captor.capture());
    }

    @Test
    @DisplayName("Post request - service exception")
    void postAdditionalInformationThrowsServiceException() throws Exception {
        when(additionalInformationService
                .getAdditionalInformation(eq(TRANSACTION_ID), eq(COMPANY_ACCOUNTS_ID)))
                .thenReturn(additionalInformation);

        when(additionalInformationService
                .createAdditionalInformation(eq(TRANSACTION_ID), eq(COMPANY_ACCOUNTS_ID), any(LoansToDirectorsAdditionalInfo.class)))
                .thenThrow(ServiceException.class);

        this.mockMvc.perform(post(ADDITIONAL_INFORMATION_PATH)
                .param(ADDITIONAL_INFORMATION_DETAILS, ADDITIONAL_INFORMATION_DETAILS))
                .andExpect(status().isOk())
                .andExpect(view().name(ERROR_VIEW));
    }

    @Test
    @DisplayName("Post request update - success")
    void postAdditionalInformationUpdateSuccess() throws Exception {
        LoansToDirectorsAdditionalInfo populatedAdditionalInformation = new LoansToDirectorsAdditionalInfo();
        
        populatedAdditionalInformation.setAdditionalInfoDetails(ADDITIONAL_INFORMATION_DETAILS);

        when(additionalInformationService
                .getAdditionalInformation(eq(TRANSACTION_ID), eq(COMPANY_ACCOUNTS_ID)))
                .thenReturn(populatedAdditionalInformation);

        when(additionalInformationService
                .updateAdditionalInformation(eq(TRANSACTION_ID), eq(COMPANY_ACCOUNTS_ID), any(LoansToDirectorsAdditionalInfo.class)))
                .thenReturn(validationErrors);

        when(validationErrors.isEmpty()).thenReturn(true);

        when(navigatorService.getNextControllerRedirect(any(), captor.capture())).thenReturn(MOCK_CONTROLLER_PATH);

        this.mockMvc.perform(post(ADDITIONAL_INFORMATION_PATH)
                .param(ADDITIONAL_INFORMATION_DETAILS, ADDITIONAL_INFORMATION_DETAILS))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name(MOCK_CONTROLLER_PATH));
    }

    @Test
    @DisplayName("Post request update - validation errors")
    void postAdditionalInformationUpdateWithValidationErrors() throws Exception {
        LoansToDirectorsAdditionalInfo populatedAdditionalInformation = new LoansToDirectorsAdditionalInfo();
        
        populatedAdditionalInformation.setAdditionalInfoDetails(ADDITIONAL_INFORMATION_DETAILS);

        when(additionalInformationService
                .getAdditionalInformation(eq(TRANSACTION_ID), eq(COMPANY_ACCOUNTS_ID)))
                .thenReturn(populatedAdditionalInformation);

        when(additionalInformationService
                .updateAdditionalInformation(eq(TRANSACTION_ID), eq(COMPANY_ACCOUNTS_ID), any(LoansToDirectorsAdditionalInfo.class)))
                .thenReturn(validationErrors);

        when(validationErrors.isEmpty()).thenReturn(false);

        this.mockMvc.perform(post(ADDITIONAL_INFORMATION_PATH)
                .param(ADDITIONAL_INFORMATION_DETAILS, ADDITIONAL_INFORMATION_DETAILS))
                .andExpect(status().isOk())
                .andExpect(view().name(ADDITIONAL_INFORMATION_VIEW));

        verify(navigatorService, never()).getNextControllerRedirect(any(), captor.capture());
    }

    @Test
    @DisplayName("Post request update - service exception")
    void postAdditionalInformationUpdateThrowsServiceException() throws Exception {
        LoansToDirectorsAdditionalInfo populatedAdditionalInformation = new LoansToDirectorsAdditionalInfo();
        
        populatedAdditionalInformation.setAdditionalInfoDetails(ADDITIONAL_INFORMATION_DETAILS);

        when(additionalInformationService
                .getAdditionalInformation(eq(TRANSACTION_ID), eq(COMPANY_ACCOUNTS_ID)))
                .thenReturn(populatedAdditionalInformation);

        when(additionalInformationService
                .updateAdditionalInformation(eq(TRANSACTION_ID), eq(COMPANY_ACCOUNTS_ID), any(LoansToDirectorsAdditionalInfo.class)))
                .thenThrow(ServiceException.class);

        this.mockMvc.perform(post(ADDITIONAL_INFORMATION_PATH)
                .param(ADDITIONAL_INFORMATION_DETAILS, ADDITIONAL_INFORMATION_DETAILS))
                .andExpect(status().isOk())
                .andExpect(view().name(ERROR_VIEW));
    }

    @Test
    @DisplayName("Post request - binding result errors")
    void postAdditionalInformationBindingResultErrors() throws Exception {
        this.mockMvc.perform(post(ADDITIONAL_INFORMATION_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name(ADDITIONAL_INFORMATION_VIEW));
    }

    @Test
    @DisplayName("Will render if loans to directors additional info not selected - false")
    void willRenderFalse() throws ServiceException {
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute(COMPANY_ACCOUNTS_DATA_STATE)).thenReturn(companyAccountsDataState);
        when(companyAccountsDataState.getHasIncludedLoansToDirectorsAdditionalInfo()).thenReturn(true);

        assertTrue(controller.willRender(COMPANY_NUMBER, TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }

    @Test
    @DisplayName("Will render if loans to directors additional info selected - true")
    void willRenderTrue() throws ServiceException {
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute(COMPANY_ACCOUNTS_DATA_STATE)).thenReturn(companyAccountsDataState);
        when(companyAccountsDataState.getHasIncludedLoansToDirectorsAdditionalInfo()).thenReturn(false);

        assertFalse(controller.willRender(COMPANY_NUMBER, TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }
}
