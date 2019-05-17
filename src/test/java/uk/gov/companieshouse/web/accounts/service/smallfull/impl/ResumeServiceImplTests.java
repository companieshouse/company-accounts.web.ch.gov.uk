package uk.gov.companieshouse.web.accounts.service.smallfull.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.servlet.view.UrlBasedViewResolver;
import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.error.ApiErrorResponseException;
import uk.gov.companieshouse.api.handler.company.CompanyResourceHandler;
import uk.gov.companieshouse.api.handler.company.request.CompanyGet;
import uk.gov.companieshouse.api.handler.exception.URIValidationException;
import uk.gov.companieshouse.api.model.company.CompanyProfileApi;
import uk.gov.companieshouse.web.accounts.api.ApiClientService;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ResumeServiceImplTests {

    @Mock
    private ApiClientService apiClientService;

    @Mock
    private ApiClient apiClient;

    @Mock
    private CompanyResourceHandler companyResourceHandler;

    @Mock
    private CompanyGet companyGet;

    @Mock
    private CompanyProfileApi companyProfileApi;

    @InjectMocks
    private ResumeServiceImpl resumeService = new ResumeServiceImpl();

    private static final String COMPANY_NUMBER = "companyNumber";

    private static final String TRANSACTION_ID = "transactionId";

    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";

    @BeforeEach
    void setUp() throws ApiErrorResponseException, URIValidationException {
        when(apiClientService.getApiClient()).thenReturn(apiClient);
        when(apiClient.company()).thenReturn(companyResourceHandler);
        when(companyResourceHandler.get(any(String.class))).thenReturn(companyGet);
        when(companyGet.execute()).thenReturn(companyProfileApi);
    }
    
    @Test
    @DisplayName("Get resume redirect success path for non-cic company")
    void getResumeRedirect() throws ServiceException {

        when(companyProfileApi.isCommunityInterestCompany()).thenReturn(false);

        String redirect = resumeService.getResumeRedirect(COMPANY_NUMBER, TRANSACTION_ID, COMPANY_ACCOUNTS_ID);

        String expectedRedirect = UrlBasedViewResolver.REDIRECT_URL_PREFIX +
                "/company/" + COMPANY_NUMBER +
                "/transaction/" + TRANSACTION_ID +
                "/company-accounts/" + COMPANY_ACCOUNTS_ID +
                "/small-full/balance-sheet";

        assertEquals(expectedRedirect, redirect);
    }

    @Test
    @DisplayName("Get resume redirect success path for cic company")
    void getResumeRedirectCIC() throws ServiceException {

        when(companyProfileApi.isCommunityInterestCompany()).thenReturn(true);

        String redirect = resumeService.getResumeRedirect(COMPANY_NUMBER, TRANSACTION_ID, COMPANY_ACCOUNTS_ID);

        String expectedRedirect = UrlBasedViewResolver.REDIRECT_URL_PREFIX +
                "/company/" + COMPANY_NUMBER +
                "/transaction/" + TRANSACTION_ID +
                "/company-accounts/" + COMPANY_ACCOUNTS_ID +
                "/cic/company-activity";

        assertEquals(expectedRedirect, redirect);
    }
}
