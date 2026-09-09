package uk.gov.companieshouse.web.accounts.controller.govuk;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CannotFileFullAccountsForCompanyTypeControllerTest {

    private MockMvc mockMvc;

    private static final String PATH = "/accounts/cannot-file-full-accounts-for-company-type";
    private static final String VIEW = "smallfull/cannotFileFullAccountsForCompanyType";
    private static final String TEMPLATE_NAME_MODEL_ATTR = "templateName";

    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(new CannotFileFullAccountsForCompanyTypeController()).build();
    }

    @Test
    @DisplayName("Get cannot file full accounts for company type view success path")
    void getRequestSuccess() throws Exception {

        this.mockMvc.perform(get(PATH))
            .andExpect(status().isOk())
            .andExpect(view().name(VIEW))
            .andExpect(model().attributeExists(TEMPLATE_NAME_MODEL_ATTR));
    }
}

