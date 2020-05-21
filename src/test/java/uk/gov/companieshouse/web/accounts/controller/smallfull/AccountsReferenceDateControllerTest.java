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
import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.model.accounts.smallfull.AccountingPeriodApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.SmallFullApi;
import uk.gov.companieshouse.api.model.company.CompanyProfileApi;
import uk.gov.companieshouse.api.model.company.account.CompanyAccountApi;
import uk.gov.companieshouse.api.model.company.account.NextAccountsApi;
import uk.gov.companieshouse.web.accounts.api.ApiClientService;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.state.CompanyAccountsDataState;
import uk.gov.companieshouse.web.accounts.service.company.impl.CompanyServiceImpl;
import uk.gov.companieshouse.web.accounts.service.navigation.NavigatorService;
import uk.gov.companieshouse.web.accounts.service.smallfull.impl.SmallFullServiceImpl;

import java.time.LocalDate;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasProperty;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@ExtendWith({MockitoExtension.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AccountsReferenceDateControllerTest {

    private MockMvc mockMvc;

    @Mock
    private NavigatorService navigatorService;

    @Mock
    private CompanyServiceImpl companyService;

    @Mock
    private SmallFullServiceImpl smallFullService;

    @Mock
    private ApiClientService apiClientService;

    @Mock
    private CompanyProfileApi companyProfile;

    @Mock
    private SmallFullApi smallFull;

    @Mock
    private ApiClient apiClient;

    @Mock
    private CompanyAccountApi accounts;

    @Mock
    private AccountingPeriodApi accountingPeriodApi;

    @Mock
    private NextAccountsApi nextAccounts;

    @InjectMocks
    private AccountsReferenceDateController controller;

    private static final LocalDate NEXT_ACCOUNTS_PERIOD_END = LocalDate.of(2019, 12, 31);
    private static final LocalDate NEXT_ACCOUNTS_PERIOD_END_DIFF = LocalDate.of(2019, 11, 1);

    private static final String COMPANY_NUMBER = "companyNumber";

    private static final String TRANSACTION_ID = "transactionId";

    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";

    private static final String ARD_PATH = "/company/" + COMPANY_NUMBER +
            "/transaction/" + TRANSACTION_ID +
            "/company-accounts/" + COMPANY_ACCOUNTS_ID +
            "/small-full/accounts-reference-date";

    private static final String ARD_MODEL_ATTR = "accountsReferenceDate";

    private static final String ARD_CHOSEN_DATE = "chosenDate";

    private static final String TEMPLATE_NAME_MODEL_ATTR = "templateName";

    private static final String ARD_VIEW = "smallfull/accountsReferenceDate";

    private static final String ERROR_VIEW = "error";

    private static final String MOCK_CONTROLLER_PATH = UrlBasedViewResolver.REDIRECT_URL_PREFIX + "mockControllerPath";

    private static final String COMPANY_ACCOUNTS_DATA_STATE = "companyAccountsDataState";

    @BeforeEach
    private void setup() {

        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    @DisplayName("Get ARD page")
    void getRequest() throws Exception {

        when(apiClientService.getApiClient()).thenReturn(apiClient);
        when(companyService.getCompanyProfile(COMPANY_NUMBER)).thenReturn(companyProfile);
        when(smallFullService.getSmallFullAccounts(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID)).thenReturn(smallFull);

        when(companyProfile.getAccounts()).thenReturn(accounts);

        when(accounts.getNextAccounts()).thenReturn(nextAccounts);

        when(nextAccounts.getPeriodEndOn()).thenReturn(NEXT_ACCOUNTS_PERIOD_END);

        when(smallFull.getNextAccounts()).thenReturn(accountingPeriodApi);
        when(accountingPeriodApi.getPeriodEndOn()).thenReturn(NEXT_ACCOUNTS_PERIOD_END);

        this.mockMvc.perform(get(ARD_PATH)
                .sessionAttr(COMPANY_ACCOUNTS_DATA_STATE, new CompanyAccountsDataState()))
                .andExpect(status().isOk())
                .andExpect(view().name(ARD_VIEW))
                .andExpect(model().attributeExists(ARD_MODEL_ATTR))
                .andExpect(model().attributeExists(TEMPLATE_NAME_MODEL_ATTR));
    }

    @Test
    @DisplayName("Get ARD page - with different dates")
    void getRequestWithDifferentDates() throws Exception {

        when(apiClientService.getApiClient()).thenReturn(apiClient);
        when(companyService.getCompanyProfile(COMPANY_NUMBER)).thenReturn(companyProfile);
        when(smallFullService.getSmallFullAccounts(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID)).thenReturn(smallFull);

        when(companyProfile.getAccounts()).thenReturn(accounts);

        when(accounts.getNextAccounts()).thenReturn(nextAccounts);

        when(nextAccounts.getPeriodEndOn()).thenReturn(NEXT_ACCOUNTS_PERIOD_END);

        when(smallFull.getNextAccounts()).thenReturn(accountingPeriodApi);
        when(accountingPeriodApi.getPeriodEndOn()).thenReturn(NEXT_ACCOUNTS_PERIOD_END_DIFF);

        this.mockMvc.perform(get(ARD_PATH)
                .sessionAttr(COMPANY_ACCOUNTS_DATA_STATE, new CompanyAccountsDataState()))
                .andExpect(status().isOk())
                .andExpect(view().name(ARD_VIEW))
                .andExpect(model().attributeExists(ARD_MODEL_ATTR))
                .andExpect(model().attributeExists(TEMPLATE_NAME_MODEL_ATTR));
    }

    @Test
    @DisplayName("Get ARD page - Throws exception")
    void getRequestThrowsException() throws Exception {

        when(apiClientService.getApiClient()).thenReturn(apiClient);

        when(companyService.getCompanyProfile(COMPANY_NUMBER)).thenThrow(ServiceException.class);

        this.mockMvc.perform(get(ARD_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name(ERROR_VIEW));
    }
}
