package uk.gov.companieshouse.web.accounts.controller.smallfull;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import uk.gov.companieshouse.web.accounts.enumeration.NoteType;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.tangible.TangibleAssets;
import uk.gov.companieshouse.web.accounts.service.NoteService;
import uk.gov.companieshouse.web.accounts.service.navigation.NavigatorService;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TangibleAssetsNoteControllerTest {

    private static final String COMPANY_NUMBER = "companyNumber";
    private static final String TRANSACTION_ID = "transactionId";
    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";
    private static final String TANGIBLE_VIEW = "smallfull/tangibleAssetsNote";
    private static final String TANGIBLE_PATH = "/company/" + COMPANY_NUMBER +
            "/transaction/" + TRANSACTION_ID +
            "/company-accounts/" + COMPANY_ACCOUNTS_ID +
            "/small-full/note/tangible-assets";
    private static final String MODEL_ATTR_BACK_PAGE = "backButton";
    private static final String MODEL_ATTR_TEMPLATE = "templateName";
    private static final String MODEL_ATTR_ASSET = "tangibleAssets";
    private static final String MOCK_CONTROLLER_PATH_NEXT = "nextControllerPath";
    private static final String MOCK_CONTROLLER_PATH_PREVIOUS = "previousControllerPath";
    private static final String ERROR_VIEW = "error";
    private MockMvc mockMvc;
    @Mock
    private NoteService<TangibleAssets> tangibleAssetsNoteService;
    @Mock
    private TangibleAssets tangibleAssets;
    @Mock
    private NavigatorService navigatorService;
    @InjectMocks
    private TangibleAssetsNoteController tangibleAssetsNoteController;

    @BeforeEach
    public void setUp() {
        when(navigatorService.getPreviousControllerPath(
                any(Class.class),
                anyString(),
                anyString(),
                anyString()))
                .thenReturn(MOCK_CONTROLLER_PATH_PREVIOUS);
        this.mockMvc = MockMvcBuilders.standaloneSetup(tangibleAssetsNoteController).build();

    }

    @Test
    @DisplayName("Get tangible asset note view success path")
    void getRequestSuccess() throws Exception {

        when(tangibleAssetsNoteService
                .get(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, NoteType.SMALL_FULL_TANGIBLE_ASSETS))
                .thenReturn(tangibleAssets);

        this.mockMvc.perform(get(TANGIBLE_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name(TANGIBLE_VIEW))
                .andExpect(model().attributeExists(MODEL_ATTR_BACK_PAGE))
                .andExpect(model().attributeExists(MODEL_ATTR_TEMPLATE))
                .andExpect(model().attributeExists(MODEL_ATTR_ASSET));
    }

    @Test
    @DisplayName("Post tangible asset note success path")
    void postRequestSuccess() throws Exception {
        when(navigatorService.getNextControllerRedirect(
                any(Class.class),
                anyString(),
                anyString(),
                anyString()))
                .thenReturn(MOCK_CONTROLLER_PATH_NEXT);

        when(
                tangibleAssetsNoteService.submit(anyString(), anyString(), any(
                        TangibleAssets.class), eq(NoteType.SMALL_FULL_TANGIBLE_ASSETS)))
                .thenReturn(new ArrayList<>());

        this.mockMvc.perform(post(TANGIBLE_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name(MOCK_CONTROLLER_PATH_NEXT));
    }

    @Test
    @DisplayName("Get tangible asset note  service exception")
    void getRequestServiceFailure() throws Exception {

        doThrow(ServiceException.class)
                .when(tangibleAssetsNoteService)
                .get(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, NoteType.SMALL_FULL_TANGIBLE_ASSETS);

        this.mockMvc.perform(get(TANGIBLE_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name(ERROR_VIEW));
    }

    @Test
    @DisplayName("Post tangible asset note  service exception")
    void postRequestServiceFailure() throws Exception {

        doThrow(ServiceException.class)
                .when(tangibleAssetsNoteService)
                .submit(anyString(), anyString(), any(
                        TangibleAssets.class), eq(NoteType.SMALL_FULL_TANGIBLE_ASSETS));

        this.mockMvc.perform(post(TANGIBLE_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name(ERROR_VIEW));
    }
}