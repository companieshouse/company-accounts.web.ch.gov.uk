package uk.gov.companieshouse.web.accounts.service.smallfull.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ResumeServiceImplTests {

    private ResumeServiceImpl resumeService = new ResumeServiceImpl();

    @Test
    @DisplayName("Get resume redirect success path")
    void getResumeRedirect() {

        String companyNumber = "company_number";
        String transactionId = "transaction_id";
        String companyAccountsId = "company_accounts_id";

        String redirect = resumeService.getResumeRedirect(companyNumber, transactionId, companyAccountsId);

        String expectedRedirect = "redirect:/company/" + companyNumber + "/transaction/" + transactionId + "/company-accounts/" + companyAccountsId + "/small-full/balance-sheet";

        assertEquals(redirect, expectedRedirect);
    }
}
