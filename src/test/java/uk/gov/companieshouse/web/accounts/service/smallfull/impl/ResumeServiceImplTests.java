package uk.gov.companieshouse.web.accounts.service.smallfull.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.servlet.view.UrlBasedViewResolver;
import uk.gov.companieshouse.api.error.ApiErrorResponseException;
import uk.gov.companieshouse.api.handler.exception.URIValidationException;
import uk.gov.companieshouse.api.model.ApiResponse;
import uk.gov.companieshouse.api.model.accounts.directorsreport.DirectorsReportApi;
import uk.gov.companieshouse.api.model.company.CompanyProfileApi;
import uk.gov.companieshouse.web.accounts.api.ApiClientService;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.profitandloss.ProfitAndLoss;
import uk.gov.companieshouse.web.accounts.model.profitandloss.profitorlossforfinancialyear.ProfitOrLossForFinancialYear;
import uk.gov.companieshouse.web.accounts.model.profitandloss.profitorlossforfinancialyear.items.TotalProfitOrLossForFinancialYear;
import uk.gov.companieshouse.web.accounts.service.company.CompanyService;
import uk.gov.companieshouse.web.accounts.service.smallfull.DirectorsReportService;
import uk.gov.companieshouse.web.accounts.service.smallfull.ProfitAndLossService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ResumeServiceImplTests {

    @Mock
    private ApiClientService apiClientService;

    @Mock
    private CompanyService companyService;

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

    @Mock
    private ProfitOrLossForFinancialYear profitOrLossForFinancialYear;

    @Mock
    private TotalProfitOrLossForFinancialYear totalProfitOrLossForFinancialYear;

    @InjectMocks
    private ResumeServiceImpl resumeService = new ResumeServiceImpl();

    private static final String COMPANY_NUMBER = "companyNumber";

    private static final String TRANSACTION_ID = "transactionId";

    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";

    private static final Long CURRENT_AMOUNT = 111L;

    @BeforeEach
    void setUp() throws ApiErrorResponseException, URIValidationException, ServiceException {
        when(companyService.getCompanyProfile(COMPANY_NUMBER)).thenReturn(companyProfileApi);
    }
    
    @Test
    @DisplayName("Get resume redirect success path for non-cic company")
    void getResumeRedirect() throws ServiceException {

        when(companyProfileApi.isCommunityInterestCompany()).thenReturn(false);
        when(directorsReportService.getDirectorsReport(apiClientService.getApiClient(), TRANSACTION_ID, COMPANY_ACCOUNTS_ID)).thenReturn(null);

        when(profitAndLossService.getProfitAndLoss(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, COMPANY_NUMBER)).thenReturn(profitAndLoss);
        when(profitAndLoss.getProfitOrLossForFinancialYear()).thenReturn(profitOrLossForFinancialYear);
        when(profitOrLossForFinancialYear.getTotalProfitOrLossForFinancialYear()).thenReturn(totalProfitOrLossForFinancialYear);
        when(totalProfitOrLossForFinancialYear.getCurrentAmount()).thenReturn(null);

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
        when(profitAndLoss.getProfitOrLossForFinancialYear()).thenReturn(profitOrLossForFinancialYear);
        when(profitOrLossForFinancialYear.getTotalProfitOrLossForFinancialYear()).thenReturn(totalProfitOrLossForFinancialYear);
        when(totalProfitOrLossForFinancialYear.getCurrentAmount()).thenReturn(CURRENT_AMOUNT);


        String redirect = resumeService.getResumeRedirect(COMPANY_NUMBER, TRANSACTION_ID, COMPANY_ACCOUNTS_ID);

        String expectedRedirect = UrlBasedViewResolver.REDIRECT_URL_PREFIX +
                "/company/" + COMPANY_NUMBER +
                "/transaction/" + TRANSACTION_ID +
                "/company-accounts/" + COMPANY_ACCOUNTS_ID +
                "/small-full/profit-and-loss";

        assertEquals(expectedRedirect, redirect);

    }
}
