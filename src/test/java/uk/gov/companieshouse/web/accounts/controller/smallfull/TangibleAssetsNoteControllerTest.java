package uk.gov.companieshouse.web.accounts.controller.smallfull;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.LocalDate;
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
import org.springframework.web.servlet.view.UrlBasedViewResolver;
import uk.gov.companieshouse.api.model.company.CompanyProfileApi;
import uk.gov.companieshouse.api.model.company.account.CompanyAccountApi;
import uk.gov.companieshouse.api.model.company.account.LastAccountsApi;
import uk.gov.companieshouse.api.model.company.account.NextAccountsApi;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.tangible.TangibleAssets;
import uk.gov.companieshouse.web.accounts.service.company.CompanyService;
import uk.gov.companieshouse.web.accounts.service.smallfull.TangibleAssetsNoteService;
import uk.gov.companieshouse.web.accounts.service.transaction.TransactionService;
import uk.gov.companieshouse.web.accounts.util.Navigator;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TangibleAssetsNoteControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TangibleAssetsNoteService tangibleAssetsNoteService;

    @Mock
    private TransactionService transactionService;

    @Mock
    private CompanyService companyService;

    @Mock
    private CompanyProfileApi companyProfile;

    @Mock
    private CompanyAccountApi companyAccountApi;

    @Mock
    private LastAccountsApi lastAccountsApi;

    @Mock
    private NextAccountsApi nextAccountsApi;

    @Mock
    private TangibleAssets tangibleAssets;

    @Mock
    private Navigator navigator;

    @InjectMocks
    private TangibleAssetsNoteController tangibleAssetsNoteController;

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

    @BeforeEach
    private void setUp() throws Exception {
        when(navigator
            .getPreviousControllerPath(tangibleAssetsNoteController.getClass(), COMPANY_NUMBER,
                TRANSACTION_ID, COMPANY_ACCOUNTS_ID)).thenReturn(MOCK_CONTROLLER_PATH_PREVIOUS);

        this.mockMvc = MockMvcBuilders.standaloneSetup(tangibleAssetsNoteController).build();

    }

    @Test
    @DisplayName("Get tangible asset note view success path")
    void getRequestSuccess() throws Exception {
        when(companyService.getCompanyProfile(COMPANY_NUMBER)).thenReturn(companyProfile);
        when(companyProfile.getAccounts()).thenReturn(companyAccountApi);
        when(companyAccountApi.getLastAccounts()).thenReturn(lastAccountsApi);
        when(companyAccountApi.getNextAccounts()).thenReturn(nextAccountsApi);
        when(lastAccountsApi.getPeriodEndOn()).thenReturn(LocalDate.parse("2016-01-01"));
        when(nextAccountsApi.getPeriodStartOn()).thenReturn(LocalDate.parse("2017-01-01"));
        when(nextAccountsApi.getPeriodEndOn()).thenReturn(LocalDate.parse("2018-01-01"));

        when(tangibleAssetsNoteService
            .getTangibleAssets(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, COMPANY_NUMBER))
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
        when(navigator
            .getNextControllerRedirect(tangibleAssetsNoteController.getClass(), COMPANY_NUMBER,
                TRANSACTION_ID, COMPANY_ACCOUNTS_ID)).thenReturn(MOCK_CONTROLLER_PATH_NEXT);

        when(
            tangibleAssetsNoteService.postTangibleAssets(anyString(), anyString(), any(
                TangibleAssets.class), anyString()))
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
            .getTangibleAssets(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, COMPANY_NUMBER);

        this.mockMvc.perform(get(TANGIBLE_PATH))
            .andExpect(status().isOk())
            .andExpect(view().name(ERROR_VIEW));
    }

    @Test
    @DisplayName("Post tangible asset note  service exception")
    void postRequestServiceFailure() throws Exception {

        doThrow(ServiceException.class)
            .when(tangibleAssetsNoteService)
            .postTangibleAssets(anyString(), anyString(), any(
                TangibleAssets.class), anyString());

        this.mockMvc.perform(post(TANGIBLE_PATH))
            .andExpect(status().isOk())
            .andExpect(view().name(ERROR_VIEW));
    }
}