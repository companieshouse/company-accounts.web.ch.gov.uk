package uk.gov.companieshouse.web.accounts.service.smallfull.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.environment.EnvironmentReader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ResumeServiceImplTests {

    @Mock
    EnvironmentReader environmentReader;

    @InjectMocks
    private ResumeServiceImpl resumeService = new ResumeServiceImpl();

    private static final String CHS_ENV_VAR = "CHS_URL";

    private static final String MOCK_CHS_ENV_VAR = "mockChsEnv";

    private static final String COMPANY_NUMBER = "company_number";

    private static final String TRANSACTION_ID = "transaction_id";

    private static final String COMPANY_ACCOUNTS_ID = "company_accounts_id";
    
    @Test
    @DisplayName("Get resume redirect success path")
    void getResumeRedirect() {

        when(environmentReader.getMandatoryString(CHS_ENV_VAR)).thenReturn(MOCK_CHS_ENV_VAR);

        String redirect = resumeService.getResumeRedirect(COMPANY_NUMBER, TRANSACTION_ID, COMPANY_ACCOUNTS_ID);

        String expectedRedirect = "redirect:" + MOCK_CHS_ENV_VAR +
                "/company/" + COMPANY_NUMBER +
                "/transaction/" + TRANSACTION_ID +
                "/company-accounts/" + COMPANY_ACCOUNTS_ID +
                "/small-full/balance-sheet";

        assertEquals(redirect, expectedRedirect);

        verify(environmentReader, times(1)).getMandatoryString(CHS_ENV_VAR);
    }
}
