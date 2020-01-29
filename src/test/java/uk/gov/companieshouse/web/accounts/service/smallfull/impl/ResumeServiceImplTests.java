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
import uk.gov.companieshouse.api.model.ApiResponse;
import uk.gov.companieshouse.api.model.accounts.directorsreport.DirectorsReportApi;
import uk.gov.companieshouse.api.model.accounts.profitandloss.ProfitAndLossApi;
import uk.gov.companieshouse.api.model.company.CompanyProfileApi;
import uk.gov.companieshouse.web.accounts.api.ApiClientService;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.profitandloss.ProfitAndLoss;
import uk.gov.companieshouse.web.accounts.service.company.CompanyService;
import uk.gov.companieshouse.web.accounts.service.smallfull.DirectorsReportService;
import uk.gov.companieshouse.web.accounts.service.smallfull.ProfitAndLossService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ResumeServiceImplTests {

    @Mock
    private ApiClientService apiClientService;

    @Mock
    private CompanyService companyService;

    @Mock
    private ApiResponse<CompanyProfileApi> responseWithData;

    @Mock
    private CompanyProfileApi companyProfileApi;

    @Mock
    private DirectorsReportService directorsReportService;

    @Mock
    private ProfitAndLossService profitAndLossService;

    @Mock
    private DirectorsReportApi directorsReportApi;

    @Mock
    private ProfitAndLoss profitAndLoss;

    @InjectMocks
    private ResumeServiceImpl resumeService = new ResumeServiceImpl();

    private static final String COMPANY_NUMBER = "companyNumber";

    private static final String TRANSACTION_ID = "transactionId";

    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";

    @BeforeEach
    void setUp() throws ApiErrorResponseException, URIValidationException, ServiceException {
        when(companyService.getCompanyProfile(COMPANY_NUMBER)).thenReturn(companyProfileApi);
    }
    
    @Test
    @DisplayName("Get resume redirect success path for non-cic company")
    void getResumeRedirect() throws ServiceException {

        when(companyProfileApi.isCommunityInterestCompany()).thenReturn(false);
        when(directorsReportService.getDirectorsReport(apiClientService.getApiClient(), TRANSACTION_ID, COMPANY_ACCOUNTS_ID)).thenReturn(null);

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

    @Test
    @DisplayName("Get resume redirect success path for company with Directors Report")
    void getResumeRedirectDirectorsReport() throws ServiceException {

        when(companyProfileApi.isCommunityInterestCompany()).thenReturn(false);
        when(directorsReportService.getDirectorsReport(apiClientService.getApiClient(), TRANSACTION_ID, COMPANY_ACCOUNTS_ID)).thenReturn(directorsReportApi);

        String redirect = resumeService.getResumeRedirect(COMPANY_NUMBER, TRANSACTION_ID, COMPANY_ACCOUNTS_ID);

        String expectedRedirect = UrlBasedViewResolver.REDIRECT_URL_PREFIX +
                "/company/" + COMPANY_NUMBER +
                "/transaction/" + TRANSACTION_ID +
                "/company-accounts/" + COMPANY_ACCOUNTS_ID +
                "/small-full/add-or-remove-directors";

        assertEquals(expectedRedirect, redirect);

    }

    @Test
    @DisplayName("Get resume redirect success path for company with Profit and Loss")
    void getResumeRedirectProfitAndLoss() throws ServiceException {

        when(companyProfileApi.isCommunityInterestCompany()).thenReturn(false);
        when(profitAndLossService.getProfitAndLoss(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, COMPANY_NUMBER)).thenReturn(profitAndLoss);

        String redirect = resumeService.getResumeRedirect(COMPANY_NUMBER, TRANSACTION_ID, COMPANY_ACCOUNTS_ID);

        String expectedRedirect = UrlBasedViewResolver.REDIRECT_URL_PREFIX +
                "/company/" + COMPANY_NUMBER +
                "/transaction/" + TRANSACTION_ID +
                "/company-accounts/" + COMPANY_ACCOUNTS_ID +
                "/small-full/profit-and-loss";

        assertEquals(expectedRedirect, redirect);

    }
}
