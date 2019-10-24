package uk.gov.companieshouse.web.accounts.service.smallfull.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.error.ApiErrorResponseException;
import uk.gov.companieshouse.api.handler.exception.URIValidationException;
import uk.gov.companieshouse.api.handler.smallfull.SmallFullResourceHandler;
import uk.gov.companieshouse.api.handler.smallfull.currentperiodprofitandloss.CurrentPeriodProfitAndLossResourceHandler;
import uk.gov.companieshouse.api.handler.smallfull.currentperiodprofitandloss.request.CurrentPeriodProfitAndLossGet;
import uk.gov.companieshouse.api.handler.smallfull.previousperiodprofitandloss.PreviousPeriodProfitAndLossResourceHandler;
import uk.gov.companieshouse.api.handler.smallfull.previousperiodprofitandloss.request.PreviousPeriodProfitAndLossGet;
import uk.gov.companieshouse.api.model.ApiResponse;
import uk.gov.companieshouse.api.model.accounts.profitandloss.ProfitAndLossApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.CurrentPeriodApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.PreviousPeriodApi;
import uk.gov.companieshouse.api.model.company.CompanyProfileApi;
import uk.gov.companieshouse.web.accounts.api.ApiClientService;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.profitandloss.ProfitAndLoss;
import uk.gov.companieshouse.web.accounts.model.smallfull.BalanceSheetHeadings;
import uk.gov.companieshouse.web.accounts.service.company.CompanyService;
import uk.gov.companieshouse.web.accounts.service.smallfull.CurrentPeriodService;
import uk.gov.companieshouse.web.accounts.service.smallfull.PreviousPeriodService;
import uk.gov.companieshouse.web.accounts.service.smallfull.ProfitAndLossService;
import uk.gov.companieshouse.web.accounts.transformer.profitandloss.ProfitAndLossTransformer;
import uk.gov.companieshouse.web.accounts.validation.helper.ServiceExceptionHandler;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class ProfitAndLossServiceImplTests {

    @Mock
    private ProfitAndLoss profitAndLoss;

    @Mock
    private ApiClient apiClient;

    @Mock
    private SmallFullResourceHandler smallFullResourceHandler;

    @Mock
    private CurrentPeriodProfitAndLossResourceHandler currentPeriodProfitAndLossResourceHandler;

    @Mock
    private CurrentPeriodProfitAndLossGet currentPeriodProfitAndLossGet;

    @Mock
    private ApiResponse<ProfitAndLossApi> currentPeriodApiResponseWithData;

    @Mock
    private PreviousPeriodProfitAndLossResourceHandler previousPeriodProfitAndLossResourceHandler;

    @Mock
    private PreviousPeriodProfitAndLossGet previousPeriodProfitAndLossGet;

    @Mock
    private ApiResponse<ProfitAndLossApi> previousPeriodApiResponseWithData;

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
    private BalanceSheetHeadings balanceSheetHeadings;

    @Mock
    private ProfitAndLossApi currentPeriodProfitAndLoss;

    @Mock
    private ProfitAndLossApi previousPeriodProfitAndLoss;

    @Mock
    private ProfitAndLossTransformer profitAndLossTransformer;

    @Mock
    private ApiErrorResponseException apiErrorResponseException;

    @Mock
    private URIValidationException uriValidationException;

    @Mock
    private ServiceException serviceException;

    @Mock
    private ServiceExceptionHandler serviceExceptionHandler;

    @InjectMocks
    private ProfitAndLossService profitAndLossService = new ProfitAndLossServiceImpl();

    private static final String TRANSACTION_ID = "transactionId";

    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";

    private static final String COMPANY_NUMBER = "companyNumber";

    private static final String CURRENT_PERIOD_URI = "/transactions/" + TRANSACTION_ID +
                                                    "/company-accounts/" + COMPANY_ACCOUNTS_ID +
                                                    "/small-full/current-period/profit-and-loss";

    private static final String PREVIOUS_PERIOD_URI = "/transactions/" + TRANSACTION_ID +
                                                    "/company-accounts/" + COMPANY_ACCOUNTS_ID +
                                                    "/small-full/previous-period/profit-and-loss";

    private static final String CURRENT_PERIOD_RESOURCE = "current period profit and loss";
    private static final String PREVIOUS_PERIOD_RESOURCE = "previous period profit and loss";

    @BeforeEach
    private void init() {

        when(apiClientService.getApiClient()).thenReturn(apiClient);

        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);
    }

    @Test
    @DisplayName("Get profit and loss - single year filer - success")
    void getProfitAndLossSingleYearFilerSuccess()
            throws ServiceException, ApiErrorResponseException, URIValidationException {

        when(smallFullResourceHandler.currentPeriodProfitAndLoss())
                .thenReturn(currentPeriodProfitAndLossResourceHandler);

        when(currentPeriodProfitAndLossResourceHandler.get(CURRENT_PERIOD_URI))
                .thenReturn(currentPeriodProfitAndLossGet);

        when(currentPeriodProfitAndLossGet.execute()).thenReturn(currentPeriodApiResponseWithData);

        when(currentPeriodApiResponseWithData.getData()).thenReturn(currentPeriodProfitAndLoss);

        when(companyService.getCompanyProfile(COMPANY_NUMBER)).thenReturn(companyProfile);

        when(companyService.isMultiYearFiler(companyProfile)).thenReturn(false);

        when(profitAndLossTransformer.getProfitAndLoss(currentPeriodProfitAndLoss, null))
                .thenReturn(profitAndLoss);

        when(companyService.getBalanceSheetHeadings(companyProfile)).thenReturn(balanceSheetHeadings);

        ProfitAndLoss returnedProfitAndLoss =
                profitAndLossService.getProfitAndLoss(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, COMPANY_NUMBER);

        assertEquals(profitAndLoss, returnedProfitAndLoss);

        verify(profitAndLoss).setBalanceSheetHeadings(balanceSheetHeadings);
    }

    @Test
    @DisplayName("Get profit and loss - multi year filer - success")
    void getProfitAndLossMultiYearFilerSuccess()
            throws ServiceException, ApiErrorResponseException, URIValidationException {

        when(smallFullResourceHandler.currentPeriodProfitAndLoss())
                .thenReturn(currentPeriodProfitAndLossResourceHandler);

        when(currentPeriodProfitAndLossResourceHandler.get(CURRENT_PERIOD_URI))
                .thenReturn(currentPeriodProfitAndLossGet);

        when(currentPeriodProfitAndLossGet.execute()).thenReturn(currentPeriodApiResponseWithData);

        when(currentPeriodApiResponseWithData.getData()).thenReturn(currentPeriodProfitAndLoss);

        when(companyService.getCompanyProfile(COMPANY_NUMBER)).thenReturn(companyProfile);

        when(companyService.isMultiYearFiler(companyProfile)).thenReturn(true);

        when(smallFullResourceHandler.previousPeriodProfitAndLoss())
                .thenReturn(previousPeriodProfitAndLossResourceHandler);

        when(previousPeriodProfitAndLossResourceHandler.get(PREVIOUS_PERIOD_URI))
                .thenReturn(previousPeriodProfitAndLossGet);

        when(previousPeriodProfitAndLossGet.execute()).thenReturn(previousPeriodApiResponseWithData);

        when(previousPeriodApiResponseWithData.getData()).thenReturn(previousPeriodProfitAndLoss);

        when(profitAndLossTransformer.getProfitAndLoss(currentPeriodProfitAndLoss, previousPeriodProfitAndLoss))
                .thenReturn(profitAndLoss);

        when(companyService.getBalanceSheetHeadings(companyProfile)).thenReturn(balanceSheetHeadings);

        ProfitAndLoss returnedProfitAndLoss =
                profitAndLossService.getProfitAndLoss(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, COMPANY_NUMBER);

        assertEquals(profitAndLoss, returnedProfitAndLoss);

        verify(profitAndLoss).setBalanceSheetHeadings(balanceSheetHeadings);
    }

    @Test
    @DisplayName("Get profit and loss - not found")
    void getProfitAndLossNotFound()
            throws ServiceException, ApiErrorResponseException, URIValidationException {

        when(smallFullResourceHandler.currentPeriodProfitAndLoss())
                .thenReturn(currentPeriodProfitAndLossResourceHandler);

        when(currentPeriodProfitAndLossResourceHandler.get(CURRENT_PERIOD_URI))
                .thenReturn(currentPeriodProfitAndLossGet);

        when(currentPeriodProfitAndLossGet.execute()).thenThrow(apiErrorResponseException);

        doNothing().when(serviceExceptionHandler)
                .handleRetrievalException(apiErrorResponseException, CURRENT_PERIOD_RESOURCE);

        when(companyService.getCompanyProfile(COMPANY_NUMBER)).thenReturn(companyProfile);

        when(companyService.isMultiYearFiler(companyProfile)).thenReturn(false);

        when(profitAndLossTransformer.getProfitAndLoss(null, null))
                .thenReturn(profitAndLoss);

        when(companyService.getBalanceSheetHeadings(companyProfile)).thenReturn(balanceSheetHeadings);

        ProfitAndLoss returnedProfitAndLoss =
                profitAndLossService.getProfitAndLoss(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, COMPANY_NUMBER);

        assertEquals(profitAndLoss, returnedProfitAndLoss);

        verify(profitAndLoss).setBalanceSheetHeadings(balanceSheetHeadings);
    }

    @Test
    @DisplayName("Get profit and loss - uri validation exception")
    void getProfitAndLossURIValidationException()
            throws ServiceException, ApiErrorResponseException, URIValidationException {

        when(smallFullResourceHandler.currentPeriodProfitAndLoss())
                .thenReturn(currentPeriodProfitAndLossResourceHandler);

        when(currentPeriodProfitAndLossResourceHandler.get(CURRENT_PERIOD_URI))
                .thenReturn(currentPeriodProfitAndLossGet);

        when(currentPeriodProfitAndLossGet.execute()).thenThrow(uriValidationException);

        doThrow(serviceException).when(serviceExceptionHandler)
                .handleURIValidationException(uriValidationException, CURRENT_PERIOD_RESOURCE);

        assertThrows(ServiceException.class, () ->
                profitAndLossService.getProfitAndLoss(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, COMPANY_NUMBER));
    }
}