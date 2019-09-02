package uk.gov.companieshouse.web.accounts.controller.smallfull;

import static org.junit.jupiter.api.Assertions.assertFalse;
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
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.intangible.IntangibleAssets;
import uk.gov.companieshouse.web.accounts.service.navigation.NavigatorService;
import uk.gov.companieshouse.web.accounts.service.smallfull.IntangibleAssetsNoteService;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
public class IntangibleAssetsNoteControllerTest {

    private MockMvc mockMvc;

    @Mock
    private IntangibleAssetsNoteService intangibleAssetsNoteService;

    @Mock
    private IntangibleAssets intangibleAssets;

    @Mock
    private NavigatorService navigatorService;

    @InjectMocks
    private IntangibleAssetsNoteController intangibleAssetsNoteController;

    @Mock
    private List<ValidationError> validationErrors;

    private static final String COMPANY_NUMBER = "companyNumber";

    private static final String TRANSACTION_ID = "transactionId";

    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";

    private static final String INTANGIBLE_VIEW = "smallfull/intangibleAssetsNote";

    private static final String INTANGIBLE_PATH = "/company/" + COMPANY_NUMBER +
        "/transaction/" + TRANSACTION_ID +
        "/company-accounts/" + COMPANY_ACCOUNTS_ID +
        "/small-full/note/intangible-assets";

    private static final String MODEL_ATTR_BACK_PAGE = "backButton";

    private static final String MODEL_ATTR_TEMPLATE = "templateName";

    private static final String MODEL_ATTR_ASSET = "intangibleAssets";

    private static final String MOCK_CONTROLLER_PATH_NEXT = "nextControllerPath";

    private static final String MOCK_CONTROLLER_PATH_PREVIOUS = "previousControllerPath";

    private static final String ERROR_VIEW = "error";

    private void setUpMockMvc() {
        when(navigatorService.getPreviousControllerPath(any(), ArgumentMatchers.<String>any())).thenReturn(MOCK_CONTROLLER_PATH_PREVIOUS);
        mockMvc = MockMvcBuilders.standaloneSetup(intangibleAssetsNoteController).build();
    }

    @Test
    @DisplayName("Get intangible asset note view - success path")
    void getRequestSuccess() throws Exception {

        setUpMockMvc();

        when(intangibleAssetsNoteService
            .getIntangibleAssets(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, COMPANY_NUMBER))
            .thenReturn(intangibleAssets);

        mockMvc.perform(get(INTANGIBLE_PATH))
            .andExpect(status().isOk())
            .andExpect(view().name(INTANGIBLE_VIEW))
            .andExpect(model().attributeExists(MODEL_ATTR_BACK_PAGE))
            .andExpect(model().attributeExists(MODEL_ATTR_TEMPLATE))
            .andExpect(model().attributeExists(MODEL_ATTR_ASSET));
    }

    @Test
    @DisplayName("Get intangible asset note - service exception")
    void getRequestServiceException() throws Exception {

        setUpMockMvc();

        doThrow(ServiceException.class)
            .when(intangibleAssetsNoteService)
            .getIntangibleAssets(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, COMPANY_NUMBER);

        mockMvc.perform(get(INTANGIBLE_PATH))
            .andExpect(status().isOk())
            .andExpect(view().name(ERROR_VIEW));
    }

    @Test
    @DisplayName("Post intangible asset note - success path")
    void postRequestSuccess() throws Exception {

        setUpMockMvc();

        when(navigatorService.getNextControllerRedirect(any(), ArgumentMatchers.<String>any())).thenReturn(MOCK_CONTROLLER_PATH_NEXT);

        when(
            intangibleAssetsNoteService.postIntangibleAssets(eq(TRANSACTION_ID), eq(COMPANY_ACCOUNTS_ID), any(
                IntangibleAssets.class), eq(COMPANY_NUMBER)))
            .thenReturn(validationErrors);

        when(validationErrors.isEmpty()).thenReturn(true);

        mockMvc.perform(post(INTANGIBLE_PATH))
            .andExpect(status().isOk())
            .andExpect(view().name(MOCK_CONTROLLER_PATH_NEXT));
    }

    @Test
    @DisplayName("Post intangible asset note - validation errors")
    void postRequestValidationErrors() throws Exception {

        setUpMockMvc();

        when(
            intangibleAssetsNoteService.postIntangibleAssets(eq(TRANSACTION_ID), eq(COMPANY_ACCOUNTS_ID), any(
                IntangibleAssets.class), eq(COMPANY_NUMBER)))
            .thenReturn(validationErrors);

        when(validationErrors.isEmpty()).thenReturn(false);

        mockMvc.perform(post(INTANGIBLE_PATH))
            .andExpect(status().isOk())
            .andExpect(view().name(INTANGIBLE_VIEW))
            .andExpect(model().attributeExists(MODEL_ATTR_BACK_PAGE));
    }

    @Test
    @DisplayName("Post intangible asset note - service exception")
    void postRequestServiceException() throws Exception {

        setUpMockMvc();

        doThrow(ServiceException.class)
            .when(intangibleAssetsNoteService)
            .postIntangibleAssets(eq(TRANSACTION_ID), eq(COMPANY_ACCOUNTS_ID), any(
                IntangibleAssets.class), eq(COMPANY_NUMBER));

        mockMvc.perform(post(INTANGIBLE_PATH))
            .andExpect(status().isOk())
            .andExpect(view().name(ERROR_VIEW));
    }

    @Test
    @DisplayName("Post intangible assets note - binding errors")
    void postRequestBindingErrors() throws Exception {

        setUpMockMvc();

        mockMvc.perform(post(INTANGIBLE_PATH)
        .param("cost.atPeriodStart.goodwill", "abc"))
            .andExpect(status().isOk())
            .andExpect(view().name(INTANGIBLE_VIEW))
            .andExpect(model().attributeExists(MODEL_ATTR_BACK_PAGE));
    }

    @Test
    @DisplayName("Will render")
    void willRender() throws ServiceException {
        assertFalse(intangibleAssetsNoteController.willRender(COMPANY_NUMBER, TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }
}
