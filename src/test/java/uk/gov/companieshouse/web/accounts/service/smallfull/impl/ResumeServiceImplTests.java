package uk.gov.companieshouse.web.accounts.service.smallfull.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class ResumeServiceImplTests {

    @InjectMocks
    private ResumeServiceImpl resumeService = new ResumeServiceImpl();

    private static final String COMPANY_NUMBER = "companyNumber";

    private static final String TRANSACTION_ID = "transactionId";

    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";
    
    @Test
    @DisplayName("Get resume redirect success path")
    void getResumeRedirect() {

        String redirect = resumeService.getResumeRedirect(COMPANY_NUMBER, TRANSACTION_ID, COMPANY_ACCOUNTS_ID);

        String expectedRedirect = UrlBasedViewResolver.REDIRECT_URL_PREFIX +
                "/company/" + COMPANY_NUMBER +
                "/transaction/" + TRANSACTION_ID +
                "/company-accounts/" + COMPANY_ACCOUNTS_ID +
                "/small-full/balance-sheet";

        assertEquals(redirect, expectedRedirect);
    }
}
