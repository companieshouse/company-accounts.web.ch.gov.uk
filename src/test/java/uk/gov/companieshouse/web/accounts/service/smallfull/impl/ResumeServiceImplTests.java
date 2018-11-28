package uk.gov.companieshouse.web.accounts.service.smallfull.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.environment.EnvironmentReader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ResumeServiceImplTests {

    @Mock
    EnvironmentReader environmentReader;

    @InjectMocks
    private ResumeServiceImpl resumeService = new ResumeServiceImpl();

    private static final String MOCK_CHS_ENV = "mockChsEnv";

    @Test
    @DisplayName("Get resume redirect success path")
    void getResumeRedirect() {

        String companyNumber = "company_number";
        String transactionId = "transaction_id";
        String companyAccountsId = "company_accounts_id";

        when(environmentReader.getMandatoryString("CHS_URL")).thenReturn(MOCK_CHS_ENV);

        String redirect = resumeService.getResumeRedirect(companyNumber, transactionId, companyAccountsId);

        String expectedRedirect = "redirect:" + MOCK_CHS_ENV + "/company/" + companyNumber + "/transaction/" + transactionId + "/company-accounts/" + companyAccountsId + "/small-full/balance-sheet";

        assertEquals(redirect, expectedRedirect);
    }
}
