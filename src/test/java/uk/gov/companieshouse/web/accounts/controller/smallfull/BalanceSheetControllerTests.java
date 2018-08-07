package uk.gov.companieshouse.web.accounts.controller.smallfull;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import uk.gov.companieshouse.web.accounts.model.smallfull.BalanceSheet;
import uk.gov.companieshouse.web.accounts.service.smallfull.BalanceSheetService;

public class BalanceSheetControllerTests {

    private MockMvc mockMvc;

    @Mock
    BalanceSheetService balanceSheetService;

    @InjectMocks
    BalanceSheetController controller;

    private static final String COMPANY_NUMBER = "companyNumber";

    private static final String TRANSACTION_ID = "transactionId";

    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";

    private static final String BALANCE_SHEET_PATH = "/company/" + COMPANY_NUMBER +
                                                     "/transaction/" + TRANSACTION_ID +
                                                     "/company-accounts/" + COMPANY_ACCOUNTS_ID +
                                                     "/small-full/balance-sheet";

    @BeforeEach
    private void setup() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void getRequestSuccess() throws Exception {

        when(balanceSheetService.getBalanceSheet(TRANSACTION_ID, COMPANY_ACCOUNTS_ID)).thenReturn(new BalanceSheet());

        this.mockMvc.perform(get(BALANCE_SHEET_PATH))
                    .andExpect(status().isOk())
                    .andExpect(view().name("smallfull/balanceSheet"))
                    .andExpect(model().attributeExists("balanceSheet"));
    }

    @Test
    void postRequestSuccess() throws Exception {

        this.mockMvc.perform(post(BALANCE_SHEET_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name("smallfull/balanceSheet"))
                .andExpect(model().attributeExists("balanceSheet"));
    }

}
