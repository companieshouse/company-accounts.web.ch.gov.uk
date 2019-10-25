package uk.gov.companieshouse.web.accounts.service.company.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.accountsdates.AccountsDatesHelper;
import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.error.ApiErrorResponseException;
import uk.gov.companieshouse.api.handler.company.CompanyResourceHandler;
import uk.gov.companieshouse.api.handler.company.request.CompanyGet;
import uk.gov.companieshouse.api.handler.exception.URIValidationException;
import uk.gov.companieshouse.api.model.ApiResponse;
import uk.gov.companieshouse.api.model.company.CompanyProfileApi;
import uk.gov.companieshouse.api.model.company.account.CompanyAccountApi;
import uk.gov.companieshouse.api.model.company.account.LastAccountsApi;
import uk.gov.companieshouse.api.model.company.account.NextAccountsApi;
import uk.gov.companieshouse.web.accounts.api.ApiClientService;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.company.CompanyDetail;
import uk.gov.companieshouse.web.accounts.model.smallfull.BalanceSheetHeadings;
import uk.gov.companieshouse.web.accounts.service.company.CompanyService;
import uk.gov.companieshouse.web.accounts.transformer.company.CompanyDetailTransformer;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CompanyServiceImplTests {

    @Mock
    private ApiClient apiClient;

    @Mock
    private ApiClientService apiClientService;

    @Mock
    private CompanyResourceHandler companyResourceHandler;

    @Mock
    private CompanyGet companyGet;

    @Mock
    private ApiResponse<CompanyProfileApi> responseWithData;

    @Mock
    private CompanyProfileApi companyProfile;

    @Mock
    private CompanyDetail companyDetail;

    @Mock
    private CompanyDetailTransformer companyDetailTransformer;

    @Mock
    private AccountsDatesHelper accountsDatesHelper;

    @Mock
    private CompanyAccountApi accounts;

    @Mock
    private LastAccountsApi lastAccounts;

    @Mock
    private NextAccountsApi nextAccounts;

    @InjectMocks
    private CompanyService companyService = new CompanyServiceImpl();

    private static final String COMPANY_NUMBER = "companyNumber";

    private static final String COMPANY_URI = "/company/" + COMPANY_NUMBER;

    private static final LocalDate LAST_ACCOUNTS_PERIOD_START = LocalDate.of(2018, 1, 1);

    private static final LocalDate LAST_ACCOUNTS_PERIOD_END = LocalDate.of(2018, 12, 31);

    private static final LocalDate NEXT_ACCOUNTS_PERIOD_START = LocalDate.of(2019, 1, 1);

    private static final LocalDate NEXT_ACCOUNTS_PERIOD_END = LocalDate.of(2019, 12, 31);

    private static final String LAST_ACCOUNTS_HEADING = "2018";

    private static final String NEXT_ACCOUNTS_HEADING = "2019";

    private void initApiClient() {

        when(apiClientService.getApiClient()).thenReturn(apiClient);

        when(apiClient.company()).thenReturn(companyResourceHandler);

        when(companyResourceHandler.get(COMPANY_URI)).thenReturn(companyGet);
    }

    @Test
    @DisplayName("Get Company Profile - Success Path")
    void getCompanyProfileSuccess() throws ServiceException, ApiErrorResponseException, URIValidationException {

        initApiClient();

        when(companyGet.execute()).thenReturn(responseWithData);

        when(responseWithData.getData()).thenReturn(companyProfile);

        CompanyProfileApi returnedCompanyProfile = companyService.getCompanyProfile(COMPANY_NUMBER);

        assertEquals(companyProfile, returnedCompanyProfile);
    }

    @Test
    @DisplayName("Get Company Profile - Throws ApiErrorResponseException")
    void getCompanyProfileThrowsApiErrorResponseException() throws ApiErrorResponseException, URIValidationException {

        initApiClient();

        when(companyGet.execute()).thenThrow(ApiErrorResponseException.class);

        assertThrows(ServiceException.class, () ->
                companyService.getCompanyProfile(COMPANY_NUMBER));
    }

    @Test
    @DisplayName("Get Company Profile - Throws URIValidationException")
    void getCompanyProfileThrowsURIValidationException() throws ApiErrorResponseException, URIValidationException {

        initApiClient();

        when(companyGet.execute()).thenThrow(URIValidationException.class);

        assertThrows(ServiceException.class, () ->
                companyService.getCompanyProfile(COMPANY_NUMBER));
    }

    @Test
    @DisplayName("Get Company Details - Success Path")
    void getCompanyDetailSuccess() throws ServiceException, ApiErrorResponseException, URIValidationException {

        initApiClient();

        when(companyGet.execute()).thenReturn(responseWithData);

        when(responseWithData.getData()).thenReturn(companyProfile);

        when(companyDetailTransformer.getCompanyDetail(companyProfile)).thenReturn(companyDetail);

        CompanyDetail returnedCompanyDetail = companyService.getCompanyDetail(COMPANY_NUMBER);

        assertEquals(companyDetail, returnedCompanyDetail);
    }

    @Test
    @DisplayName("Is Multi Year Filer - No Last Accounts")
    void isMultiYearFilerNoLastAccounts() {

        when(companyProfile.getAccounts()).thenReturn(accounts);

        when(accounts.getLastAccounts()).thenReturn(null);

        assertFalse(companyService.isMultiYearFiler(companyProfile));
    }

    @Test
    @DisplayName("Is Multi Year Filer - No Last Accounts Period End")
    void isMultiYearFilerNoLastAccountsPeriodEnd() {

        when(companyProfile.getAccounts()).thenReturn(accounts);

        when(accounts.getLastAccounts()).thenReturn(lastAccounts);

        when(lastAccounts.getPeriodEndOn()).thenReturn(null);

        assertFalse(companyService.isMultiYearFiler(companyProfile));
    }

    @Test
    @DisplayName("Is Multi Year Filer - Has Last Accounts Period End")
    void isMultiYearFilerHasLastAccountsPeriodEnd() {

        when(companyProfile.getAccounts()).thenReturn(accounts);

        when(accounts.getLastAccounts()).thenReturn(lastAccounts);

        when(lastAccounts.getPeriodEndOn()).thenReturn(LocalDate.now());

        assertTrue(companyService.isMultiYearFiler(companyProfile));
    }

    @Test
    @DisplayName("Get Balance Sheet Headings - Single Year Filer")
    void getBalanceSheetHeadingsSingleYearFiler() {

        when(companyProfile.getAccounts()).thenReturn(accounts);

        when(accounts.getLastAccounts()).thenReturn(null);

        when(accounts.getNextAccounts()).thenReturn(nextAccounts);

        when(nextAccounts.getPeriodStartOn()).thenReturn(NEXT_ACCOUNTS_PERIOD_START);

        when(nextAccounts.getPeriodEndOn()).thenReturn(NEXT_ACCOUNTS_PERIOD_END);

        when(accountsDatesHelper.generateBalanceSheetHeading(
                NEXT_ACCOUNTS_PERIOD_START, NEXT_ACCOUNTS_PERIOD_END, false))
                        .thenReturn(NEXT_ACCOUNTS_HEADING);

        BalanceSheetHeadings balanceSheetHeadings =
                companyService.getBalanceSheetHeadings(companyProfile);

        assertNotNull(balanceSheetHeadings);
        assertEquals(NEXT_ACCOUNTS_HEADING, balanceSheetHeadings.getCurrentPeriodHeading());
        assertNull(balanceSheetHeadings.getPreviousPeriodHeading());
    }

    @Test
    @DisplayName("Get Balance Sheet Headings - Multi Year Filer")
    void getBalanceSheetHeadingsMultiYearFiler() {

        when(companyProfile.getAccounts()).thenReturn(accounts);

        when(accounts.getLastAccounts()).thenReturn(lastAccounts);

        when(lastAccounts.getPeriodStartOn()).thenReturn(LAST_ACCOUNTS_PERIOD_START);

        when(lastAccounts.getPeriodEndOn()).thenReturn(LAST_ACCOUNTS_PERIOD_END);

        when(accounts.getNextAccounts()).thenReturn(nextAccounts);

        when(nextAccounts.getPeriodStartOn()).thenReturn(NEXT_ACCOUNTS_PERIOD_START);

        when(nextAccounts.getPeriodEndOn()).thenReturn(NEXT_ACCOUNTS_PERIOD_END);

        when(accountsDatesHelper.isSameYear(LAST_ACCOUNTS_PERIOD_END, NEXT_ACCOUNTS_PERIOD_END))
                .thenReturn(true);

        when(accountsDatesHelper.generateBalanceSheetHeading(
                any(LocalDate.class), any(LocalDate.class), eq(true)))
                        .thenReturn(LAST_ACCOUNTS_HEADING)
                                .thenReturn(NEXT_ACCOUNTS_HEADING);

        BalanceSheetHeadings balanceSheetHeadings =
                companyService.getBalanceSheetHeadings(companyProfile);

        assertNotNull(balanceSheetHeadings);
        assertEquals(NEXT_ACCOUNTS_HEADING, balanceSheetHeadings.getCurrentPeriodHeading());
        assertEquals(LAST_ACCOUNTS_HEADING, balanceSheetHeadings.getPreviousPeriodHeading());
    }
}
