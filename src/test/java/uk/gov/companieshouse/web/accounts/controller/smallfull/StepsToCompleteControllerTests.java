package uk.gov.companieshouse.web.accounts.controller.smallfull;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.google.api.client.util.DateTime;
import java.util.Date;
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
import uk.gov.companieshouse.api.model.company.account.NextAccountsApi;
import uk.gov.companieshouse.web.accounts.service.company.CompanyService;
import uk.gov.companieshouse.web.accounts.service.companyaccounts.CompanyAccountsService;
import uk.gov.companieshouse.web.accounts.service.transaction.TransactionService;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class StepsToCompleteControllerTests {

    private MockMvc mockMvc;

    @Mock
    private TransactionService transactionService;

    @Mock
    private CompanyService companyService;

    @Mock
    private CompanyAccountsService companyAccountsService;

    @InjectMocks
    private StepsToCompleteController controller;

    private static final String COMPANY_NUMBER = "companyNumber";

    private static final String TRANSACTION_ID = "transactionId";

    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";

    private static final String STEPS_TO_COMPLETE_PATH = "/company/" + COMPANY_NUMBER +
                                                            "/small-full/steps-to-complete";

    private static final String BALANCE_SHEET_PATH = "/company/" + COMPANY_NUMBER +
                                                        "/transaction/" + TRANSACTION_ID +
                                                        "/company-accounts/" + COMPANY_ACCOUNTS_ID +
                                                        "/small-full/balance-sheet";

    private static final String STEPS_TO_COMPLETE_VIEW = "smallfull/stepsToComplete";

    @BeforeEach
    private void setup() {

        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    @DisplayName("Get steps to complete view success path")
    void getRequestSuccess() throws Exception {

        this.mockMvc.perform(get(STEPS_TO_COMPLETE_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name(STEPS_TO_COMPLETE_VIEW));
    }

    @Test
    @DisplayName("Post balance sheet success path")
    void postRequestSuccess() throws Exception {

        when(transactionService.createTransaction(COMPANY_NUMBER)).thenReturn(TRANSACTION_ID);

        DateTime periodEndOn = new DateTime(new Date());

        NextAccountsApi nextAccounts = new NextAccountsApi();
        nextAccounts.setPeriodEndOn(periodEndOn);

        CompanyAccountApi companyAccount = new CompanyAccountApi();
        companyAccount.setNextAccounts(nextAccounts);

        CompanyProfileApi companyProfile = new CompanyProfileApi();
        companyProfile.setAccounts(companyAccount);

        when(companyService.getCompanyProfile(COMPANY_NUMBER)).thenReturn(companyProfile);

        when(companyAccountsService.createCompanyAccounts(TRANSACTION_ID, periodEndOn)).thenReturn(COMPANY_ACCOUNTS_ID);

        this.mockMvc.perform(post(STEPS_TO_COMPLETE_PATH))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name(UrlBasedViewResolver.REDIRECT_URL_PREFIX + BALANCE_SHEET_PATH));

        verify(transactionService, times(1)).createTransaction(COMPANY_NUMBER);

        verify(companyService, times(1)).getCompanyProfile(COMPANY_NUMBER);

        verify(companyAccountsService, times(1)).createCompanyAccounts(TRANSACTION_ID, periodEndOn);
    }

}
