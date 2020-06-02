package uk.gov.companieshouse.web.accounts.service.smallfull.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.model.accounts.smallfull.BalanceSheetApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.CurrentPeriodApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.PreviousPeriodApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.SmallFullApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.SmallFullLinks;
import uk.gov.companieshouse.api.model.company.CompanyProfileApi;
import uk.gov.companieshouse.web.accounts.api.ApiClientService;
import uk.gov.companieshouse.web.accounts.enumeration.NoteType;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.smallfull.BalanceSheet;
import uk.gov.companieshouse.web.accounts.model.smallfull.BalanceSheetHeadings;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.creditorswithinoneyear.CreditorsWithinOneYear;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.debtors.Debtors;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.stocks.StocksNote;
import uk.gov.companieshouse.web.accounts.service.NoteService;
import uk.gov.companieshouse.web.accounts.service.company.CompanyService;
import uk.gov.companieshouse.web.accounts.service.smallfull.BalanceSheetService;
import uk.gov.companieshouse.web.accounts.service.smallfull.CreditorsAfterOneYearService;
import uk.gov.companieshouse.web.accounts.service.smallfull.CurrentAssetsInvestmentsService;
import uk.gov.companieshouse.web.accounts.service.smallfull.CurrentPeriodService;
import uk.gov.companieshouse.web.accounts.service.smallfull.FixedAssetsInvestmentsService;
import uk.gov.companieshouse.web.accounts.service.smallfull.IntangibleAssetsNoteService;
import uk.gov.companieshouse.web.accounts.service.smallfull.PreviousPeriodService;
import uk.gov.companieshouse.web.accounts.service.smallfull.SmallFullService;
import uk.gov.companieshouse.web.accounts.service.smallfull.TangibleAssetsNoteService;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.BalanceSheetTransformer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class BalanceSheetServiceImplTests {

    @Mock
    private BalanceSheetTransformer transformer;

    @Mock
    private ApiClient apiClient;

    @Mock
    private CompanyService companyService;

    @Mock
    private CompanyProfileApi companyProfile;

    @Mock
    private ApiClientService apiClientService;

    @Mock
    private CurrentPeriodService currentPeriodService;

    @Mock
    private PreviousPeriodService previousPeriodService;

    @Mock
    private CurrentPeriodApi currentPeriod;

    @Mock
    private PreviousPeriodApi previousPeriod;

    @Mock
    private BalanceSheetApi currentPeriodBalanceSheet;

    @Mock
    private BalanceSheetApi previousPeriodBalanceSheet;

    @Mock
    private BalanceSheet balanceSheet;

    @Mock
    private BalanceSheetHeadings balanceSheetHeadings;

    @Mock
    private NoteService<Debtors> debtorsService;

    @Mock
    private NoteService<CreditorsWithinOneYear> creditorsWithinOneYearService;

    @Mock
    private CreditorsAfterOneYearService creditorsAfterOneYearService;

    @Mock
    private NoteService<StocksNote> stocksService;

    @Mock
    private TangibleAssetsNoteService tangibleAssetsNoteService;

    @Mock
    private IntangibleAssetsNoteService intangibleAssetsNoteService;

    @Mock
    private FixedAssetsInvestmentsService fixedAssetsInvestmentsService;

    @Mock
    private CurrentAssetsInvestmentsService currentAssetsInvestmentsService;

    @Mock
    private SmallFullService smallFullService;

    @Mock
    private SmallFullApi smallFull;

    @Mock
    private SmallFullLinks smallFullLinks;

    @InjectMocks
    private BalanceSheetService balanceSheetService = new BalanceSheetServiceImpl();

    private static final String TRANSACTION_ID = "transactionId";

    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";

    private static final String COMPANY_NUMBER = "companyNumber";

    private static final String CACHED_BALANCE_SHEET = "cachedBalanceSheet";

    private static final String LBG_TYPE = "private-limited-guarant-nsc";

    private static final String NON_LBG_TYPE = "plc";

    private static final String DEBTORS_LINK = "debtorsLink";

    private static final String CREDITORS_WITHIN_LINK = "creditorsWithinLink";

    private static final String CREDITORS_AFTER_LINK = "creditorsAfterLink";

    private static final String INTANGIBLE_LINK = "intangibleLink";

    private static final String TANGIBLE_LINK = "tangibleLink";

    private static final String FIXED_ASSETS_LINK = "fixedAssetsLink";

    private static final String STOCKS_LINK = "stocksLink";

    private static final String CURRENT_ASSETS_LINK = "currentAssetsLink";

    @Test
    @DisplayName("Get balance sheet - single year lbg filer")
    void getBalanceSheetSingleYearLBGFiler() throws ServiceException {

        ReflectionTestUtils.setField(balanceSheetService, CACHED_BALANCE_SHEET, null);

        when(apiClientService.getApiClient()).thenReturn(apiClient);

        when(currentPeriodService.getCurrentPeriod(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(currentPeriod);

        when(companyService.getCompanyProfile(COMPANY_NUMBER)).thenReturn(companyProfile);

        when(companyService.isMultiYearFiler(companyProfile)).thenReturn(false);

        when(transformer.getBalanceSheet(currentPeriod, null))
                .thenReturn(balanceSheet);

        when(companyProfile.getType()).thenReturn(LBG_TYPE);

        when(companyService.getBalanceSheetHeadings(companyProfile))
                .thenReturn(balanceSheetHeadings);

        BalanceSheet returnedBalanceSheet =
                balanceSheetService.getBalanceSheet(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, COMPANY_NUMBER);

        assertEquals(balanceSheet, returnedBalanceSheet);

        verify(balanceSheet).setLbg(true);

        verify(balanceSheet).setBalanceSheetHeadings(balanceSheetHeadings);
    }

    @Test
    @DisplayName("Get balance sheet - multi year non-lbg filer")
    void getBalanceSheetMultiYearNonLBGFiler() throws ServiceException {

        ReflectionTestUtils.setField(balanceSheetService, CACHED_BALANCE_SHEET, null);

        when(apiClientService.getApiClient()).thenReturn(apiClient);

        when(currentPeriodService.getCurrentPeriod(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(currentPeriod);

        when(companyService.getCompanyProfile(COMPANY_NUMBER)).thenReturn(companyProfile);

        when(companyService.isMultiYearFiler(companyProfile)).thenReturn(true);

        when(previousPeriodService.getPreviousPeriod(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(previousPeriod);

        when(transformer.getBalanceSheet(currentPeriod, previousPeriod))
                .thenReturn(balanceSheet);

        when(companyProfile.getType()).thenReturn(NON_LBG_TYPE);

        when(companyService.getBalanceSheetHeadings(companyProfile))
                .thenReturn(balanceSheetHeadings);

        BalanceSheet returnedBalanceSheet =
                balanceSheetService.getBalanceSheet(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, COMPANY_NUMBER);

        assertEquals(balanceSheet, returnedBalanceSheet);

        verify(balanceSheet).setLbg(false);

        verify(balanceSheet).setBalanceSheetHeadings(balanceSheetHeadings);
    }

    @Test
    @DisplayName("Get cached balance sheet")
    void getCachedBalanceSheet() throws ServiceException {

        ReflectionTestUtils.setField(balanceSheetService, CACHED_BALANCE_SHEET, balanceSheet);

        BalanceSheet returnedBalanceSheet =
                balanceSheetService.getBalanceSheet(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, COMPANY_NUMBER);

        assertEquals(balanceSheet, returnedBalanceSheet);

        verify(apiClientService, never()).getApiClient();

        verify(balanceSheet, never()).setLbg(anyBoolean());

        verify(balanceSheet, never()).setBalanceSheetHeadings(any(BalanceSheetHeadings.class));
    }

    @Test
    @DisplayName("Submit balance sheet - single year filer")
    void submitBalanceSheetSingleYearFiler() throws ServiceException {

        when(apiClientService.getApiClient()).thenReturn(apiClient);

        when(smallFullService.getSmallFullAccounts(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(smallFull);

        when(companyService.getCompanyProfile(COMPANY_NUMBER))
                .thenReturn(companyProfile);

        when(companyService.isMultiYearFiler(companyProfile)).thenReturn(false);

        when(currentPeriodService.getCurrentPeriod(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(currentPeriod);

        when(transformer.getCurrentPeriodBalanceSheet(balanceSheet)).thenReturn(currentPeriodBalanceSheet);

        when(smallFull.getLinks()).thenReturn(smallFullLinks);

        assertNotNull(
                balanceSheetService
                        .postBalanceSheet(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, balanceSheet, COMPANY_NUMBER));

        verify(previousPeriodService, never()).getPreviousPeriod(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID);

        verify(transformer, never()).getPreviousPeriodBalanceSheet(balanceSheet);

        verify(previousPeriodService, never())
                .submitPreviousPeriod(eq(apiClient), eq(smallFull), eq(TRANSACTION_ID),
                        eq(COMPANY_ACCOUNTS_ID), any(PreviousPeriodApi.class), anyList());

        verify(currentPeriod).setBalanceSheet(currentPeriodBalanceSheet);

        verify(currentPeriodService)
                .submitCurrentPeriod(eq(apiClient), eq(smallFull), eq(TRANSACTION_ID),
                        eq(COMPANY_ACCOUNTS_ID), eq(currentPeriod), anyList());
    }

    @Test
    @DisplayName("Submit balance sheet - single year filer - not found")
    void submitBalanceSheetSingleYearFilerNotFound() throws ServiceException {

        when(apiClientService.getApiClient()).thenReturn(apiClient);

        when(smallFullService.getSmallFullAccounts(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(smallFull);

        when(companyService.getCompanyProfile(COMPANY_NUMBER))
                .thenReturn(companyProfile);

        when(companyService.isMultiYearFiler(companyProfile)).thenReturn(false);

        when(currentPeriodService.getCurrentPeriod(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(null);

        when(transformer.getCurrentPeriodBalanceSheet(balanceSheet)).thenReturn(currentPeriodBalanceSheet);

        when(smallFull.getLinks()).thenReturn(smallFullLinks);

        assertNotNull(
                balanceSheetService
                        .postBalanceSheet(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, balanceSheet, COMPANY_NUMBER));

        verify(previousPeriodService, never()).getPreviousPeriod(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID);

        verify(transformer, never()).getPreviousPeriodBalanceSheet(balanceSheet);

        verify(previousPeriodService, never())
                .submitPreviousPeriod(eq(apiClient), eq(smallFull), eq(TRANSACTION_ID),
                        eq(COMPANY_ACCOUNTS_ID), any(PreviousPeriodApi.class), anyList());

        verify(currentPeriodService)
                .submitCurrentPeriod(eq(apiClient), eq(smallFull), eq(TRANSACTION_ID),
                        eq(COMPANY_ACCOUNTS_ID), any(CurrentPeriodApi.class), anyList());
    }

    @Test
    @DisplayName("Submit balance sheet - multi year filer")
    void submitBalanceSheetMultiYearFiler() throws ServiceException {

        when(apiClientService.getApiClient()).thenReturn(apiClient);

        when(smallFullService.getSmallFullAccounts(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(smallFull);

        when(companyService.getCompanyProfile(COMPANY_NUMBER))
                .thenReturn(companyProfile);

        when(companyService.isMultiYearFiler(companyProfile)).thenReturn(true);

        when(previousPeriodService.getPreviousPeriod(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(previousPeriod);

        when(transformer.getPreviousPeriodBalanceSheet(balanceSheet)).thenReturn(previousPeriodBalanceSheet);

        when(currentPeriodService.getCurrentPeriod(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(currentPeriod);

        when(transformer.getCurrentPeriodBalanceSheet(balanceSheet)).thenReturn(currentPeriodBalanceSheet);

        when(smallFull.getLinks()).thenReturn(smallFullLinks);

        assertNotNull(
                balanceSheetService
                        .postBalanceSheet(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, balanceSheet, COMPANY_NUMBER));

        verify(previousPeriod).setBalanceSheet(previousPeriodBalanceSheet);

        verify(previousPeriodService)
                .submitPreviousPeriod(eq(apiClient), eq(smallFull), eq(TRANSACTION_ID),
                        eq(COMPANY_ACCOUNTS_ID), eq(previousPeriod), anyList());

        verify(currentPeriod).setBalanceSheet(currentPeriodBalanceSheet);

        verify(currentPeriodService)
                .submitCurrentPeriod(eq(apiClient), eq(smallFull), eq(TRANSACTION_ID),
                        eq(COMPANY_ACCOUNTS_ID), eq(currentPeriod), anyList());
    }

    @Test
    @DisplayName("Submit balance sheet - multi year filer - not found")
    void submitBalanceSheetMultiYearFilerNotFound() throws ServiceException {

        when(apiClientService.getApiClient()).thenReturn(apiClient);

        when(smallFullService.getSmallFullAccounts(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(smallFull);

        when(companyService.getCompanyProfile(COMPANY_NUMBER))
                .thenReturn(companyProfile);

        when(companyService.isMultiYearFiler(companyProfile)).thenReturn(true);

        when(previousPeriodService.getPreviousPeriod(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(null);

        when(transformer.getPreviousPeriodBalanceSheet(balanceSheet)).thenReturn(previousPeriodBalanceSheet);

        when(currentPeriodService.getCurrentPeriod(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(null);

        when(transformer.getCurrentPeriodBalanceSheet(balanceSheet)).thenReturn(currentPeriodBalanceSheet);

        when(smallFull.getLinks()).thenReturn(smallFullLinks);

        assertNotNull(
                balanceSheetService
                        .postBalanceSheet(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, balanceSheet, COMPANY_NUMBER));

        verify(previousPeriodService)
                .submitPreviousPeriod(eq(apiClient), eq(smallFull), eq(TRANSACTION_ID),
                        eq(COMPANY_ACCOUNTS_ID), any(PreviousPeriodApi.class), anyList());

        verify(currentPeriodService)
                .submitCurrentPeriod(eq(apiClient), eq(smallFull), eq(TRANSACTION_ID),
                        eq(COMPANY_ACCOUNTS_ID), any(CurrentPeriodApi.class), anyList());
    }

    @Test
    @DisplayName("Submit balance sheet - delete all conditional notes")
    void submitBalanceSheetDeleteAllConditionalNotes() throws ServiceException {

        when(apiClientService.getApiClient()).thenReturn(apiClient);

        when(smallFullService.getSmallFullAccounts(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(smallFull);

        when(companyService.getCompanyProfile(COMPANY_NUMBER))
                .thenReturn(companyProfile);

        when(companyService.isMultiYearFiler(companyProfile)).thenReturn(true);

        when(previousPeriodService.getPreviousPeriod(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(previousPeriod);

        when(transformer.getPreviousPeriodBalanceSheet(balanceSheet)).thenReturn(previousPeriodBalanceSheet);

        when(currentPeriodService.getCurrentPeriod(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(currentPeriod);

        when(transformer.getCurrentPeriodBalanceSheet(balanceSheet)).thenReturn(currentPeriodBalanceSheet);

        when(smallFull.getLinks()).thenReturn(smallFullLinks);

        mockSmallFullLinks();

        assertNotNull(
                balanceSheetService
                        .postBalanceSheet(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, balanceSheet, COMPANY_NUMBER));

        verify(previousPeriod).setBalanceSheet(previousPeriodBalanceSheet);

        verify(previousPeriodService)
                .submitPreviousPeriod(eq(apiClient), eq(smallFull), eq(TRANSACTION_ID),
                        eq(COMPANY_ACCOUNTS_ID), eq(previousPeriod), anyList());

        verify(currentPeriod).setBalanceSheet(currentPeriodBalanceSheet);

        verify(currentPeriodService)
                .submitCurrentPeriod(eq(apiClient), eq(smallFull), eq(TRANSACTION_ID),
                        eq(COMPANY_ACCOUNTS_ID), eq(currentPeriod), anyList());

        assertAllConditionalNotesDeleted();
    }

    private void mockSmallFullLinks() {

        when(smallFullLinks.getDebtorsNote()).thenReturn(DEBTORS_LINK);
        when(smallFullLinks.getCreditorsWithinOneYearNote()).thenReturn(CREDITORS_WITHIN_LINK);
        when(smallFullLinks.getCreditorsAfterMoreThanOneYearNote()).thenReturn(CREDITORS_AFTER_LINK);
        when(smallFullLinks.getIntangibleAssetsNote()).thenReturn(INTANGIBLE_LINK);
        when(smallFullLinks.getTangibleAssetsNote()).thenReturn(TANGIBLE_LINK);
        when(smallFullLinks.getFixedAssetsInvestmentsNote()).thenReturn(FIXED_ASSETS_LINK);
        when(smallFullLinks.getStocksNote()).thenReturn(STOCKS_LINK);
        when(smallFullLinks.getCurrentAssetsInvestmentsNote()).thenReturn(CURRENT_ASSETS_LINK);
    }

    private void assertAllConditionalNotesDeleted() throws ServiceException {

        verify(creditorsWithinOneYearService).delete(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, NoteType.SMALL_FULL_CREDITORS_WITHIN_ONE_YEAR);
        verify(debtorsService).delete(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, NoteType.SMALL_FULL_DEBTORS);
        verify(creditorsAfterOneYearService).deleteCreditorsAfterOneYear(TRANSACTION_ID, COMPANY_ACCOUNTS_ID);
        verify(intangibleAssetsNoteService).deleteIntangibleAssets(TRANSACTION_ID, COMPANY_ACCOUNTS_ID);
        verify(tangibleAssetsNoteService).deleteTangibleAssets(TRANSACTION_ID, COMPANY_ACCOUNTS_ID);
        verify(fixedAssetsInvestmentsService).deleteFixedAssetsInvestments(TRANSACTION_ID, COMPANY_ACCOUNTS_ID);
        verify(stocksService).delete(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, NoteType.SMALL_FULL_STOCKS);
        verify(currentAssetsInvestmentsService).deleteCurrentAssetsInvestments(TRANSACTION_ID, COMPANY_ACCOUNTS_ID);
    }
}