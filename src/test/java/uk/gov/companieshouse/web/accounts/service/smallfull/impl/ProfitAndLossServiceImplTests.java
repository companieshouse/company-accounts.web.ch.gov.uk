package uk.gov.companieshouse.web.accounts.service.smallfull.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.error.ApiError;
import uk.gov.companieshouse.api.error.ApiErrorResponseException;
import uk.gov.companieshouse.api.handler.exception.URIValidationException;
import uk.gov.companieshouse.api.handler.smallfull.SmallFullResourceHandler;
import uk.gov.companieshouse.api.handler.smallfull.currentperiodprofitandloss.CurrentPeriodProfitAndLossResourceHandler;
import uk.gov.companieshouse.api.handler.smallfull.currentperiodprofitandloss.request.CurrentPeriodProfitAndLossCreate;
import uk.gov.companieshouse.api.handler.smallfull.currentperiodprofitandloss.request.CurrentPeriodProfitAndLossGet;
import uk.gov.companieshouse.api.handler.smallfull.currentperiodprofitandloss.request.CurrentPeriodProfitAndLossUpdate;
import uk.gov.companieshouse.api.handler.smallfull.previousperiodprofitandloss.PreviousPeriodProfitAndLossResourceHandler;
import uk.gov.companieshouse.api.handler.smallfull.previousperiodprofitandloss.request.PreviousPeriodProfitAndLossCreate;
import uk.gov.companieshouse.api.handler.smallfull.previousperiodprofitandloss.request.PreviousPeriodProfitAndLossGet;
import uk.gov.companieshouse.api.handler.smallfull.previousperiodprofitandloss.request.PreviousPeriodProfitAndLossUpdate;
import uk.gov.companieshouse.api.model.ApiResponse;
import uk.gov.companieshouse.api.model.accounts.profitandloss.ProfitAndLossApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.CurrentPeriodApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.CurrentPeriodLinks;
import uk.gov.companieshouse.api.model.accounts.smallfull.PreviousPeriodApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.PreviousPeriodLinks;
import uk.gov.companieshouse.api.model.accounts.smallfull.SmallFullApi;
import uk.gov.companieshouse.api.model.company.CompanyProfileApi;
import uk.gov.companieshouse.web.accounts.api.ApiClientService;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.profitandloss.ProfitAndLoss;
import uk.gov.companieshouse.web.accounts.model.smallfull.BalanceSheetHeadings;
import uk.gov.companieshouse.web.accounts.service.company.CompanyService;
import uk.gov.companieshouse.web.accounts.service.smallfull.CurrentPeriodService;
import uk.gov.companieshouse.web.accounts.service.smallfull.PreviousPeriodService;
import uk.gov.companieshouse.web.accounts.service.smallfull.ProfitAndLossService;
import uk.gov.companieshouse.web.accounts.service.smallfull.SmallFullService;
import uk.gov.companieshouse.web.accounts.transformer.profitandloss.ProfitAndLossTransformer;
import uk.gov.companieshouse.web.accounts.util.ValidationContext;
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
    private CurrentPeriodProfitAndLossCreate currentPeriodProfitAndLossCreate;

    @Mock
    private CurrentPeriodProfitAndLossUpdate currentPeriodProfitAndLossUpdate;

    @Mock
    private ApiResponse<ProfitAndLossApi> currentPeriodApiResponseWithData;

    @Mock
    private ApiResponse<Void> currentPeriodApiResponseNoData;

    @Mock
    private PreviousPeriodProfitAndLossResourceHandler previousPeriodProfitAndLossResourceHandler;

    @Mock
    private PreviousPeriodProfitAndLossGet previousPeriodProfitAndLossGet;

    @Mock
    private PreviousPeriodProfitAndLossCreate previousPeriodProfitAndLossCreate;

    @Mock
    private PreviousPeriodProfitAndLossUpdate previousPeriodProfitAndLossUpdate;

    @Mock
    private ApiResponse<ProfitAndLossApi> previousPeriodApiResponseWithData;

    @Mock
    private ApiResponse<Void> previousPeriodApiResponseNoData;

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
    private CurrentPeriodLinks currentPeriodLinks;

    @Mock
    private PreviousPeriodApi previousPeriod;

    @Mock
    private PreviousPeriodLinks previousPeriodLinks;

    @Mock
    private BalanceSheetHeadings balanceSheetHeadings;

    @Mock
    private ProfitAndLossApi currentPeriodProfitAndLoss;

    @Mock
    private ProfitAndLossApi previousPeriodProfitAndLoss;

    @Mock
    private SmallFullService smallFullService;

    @Mock
    private SmallFullApi smallFullApi;

    @Mock
    private List<ApiError> apiErrors;

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

    @Mock
    private ValidationContext validationContext;

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

    private static final String CURRENT_PERIOD_PROFIT_AND_LOSS_LINK = "currentPeriodProfitAndLossLink";
    private static final String PREVIOUS_PERIOD_PROFIT_AND_LOSS_LINK = "previousPeriodProfitAndLossLink";

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

        when(smallFullService.getSmallFullAccounts(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID)).thenReturn(smallFullApi);
        when(smallFullService.getBalanceSheetHeadings(smallFullApi)).thenReturn(balanceSheetHeadings);

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

        when(smallFullService.getSmallFullAccounts(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID)).thenReturn(smallFullApi);
        when(smallFullService.getBalanceSheetHeadings(smallFullApi)).thenReturn(balanceSheetHeadings);

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

        when(smallFullService.getSmallFullAccounts(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID)).thenReturn(smallFullApi);
        when(smallFullService.getBalanceSheetHeadings(smallFullApi)).thenReturn(balanceSheetHeadings);

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

    @Test
    @DisplayName("Create profit and loss - single year filer - success")
    void createProfitAndLossSingleYearFilerSuccess()
            throws ServiceException, ApiErrorResponseException, URIValidationException {

        when(profitAndLossTransformer.getCurrentPeriodProfitAndLoss(profitAndLoss))
                .thenReturn(currentPeriodProfitAndLoss);

        when(currentPeriodService.getCurrentPeriod(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(currentPeriod);

        when(currentPeriod.getLinks()).thenReturn(currentPeriodLinks);

        when(currentPeriodLinks.getProfitAndLoss()).thenReturn(null);

        when(smallFullResourceHandler.currentPeriodProfitAndLoss())
                .thenReturn(currentPeriodProfitAndLossResourceHandler);

        when(currentPeriodProfitAndLossResourceHandler.create(CURRENT_PERIOD_URI, currentPeriodProfitAndLoss))
                .thenReturn(currentPeriodProfitAndLossCreate);

        when(currentPeriodProfitAndLossCreate.execute()).thenReturn(currentPeriodApiResponseWithData);

        when(currentPeriodApiResponseWithData.hasErrors()).thenReturn(false);

        when(companyService.getCompanyProfile(COMPANY_NUMBER)).thenReturn(companyProfile);

        when(companyService.isMultiYearFiler(companyProfile)).thenReturn(false);

        assertEquals(0,
                profitAndLossService
                        .submitProfitAndLoss(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, COMPANY_NUMBER, profitAndLoss)
                                .size());
    }

    @Test
    @DisplayName("Create profit and loss - multi year filer - success")
    void createProfitAndLossMultiYearFilerSuccess()
            throws ServiceException, ApiErrorResponseException, URIValidationException {

        when(profitAndLossTransformer.getCurrentPeriodProfitAndLoss(profitAndLoss))
                .thenReturn(currentPeriodProfitAndLoss);

        when(currentPeriodService.getCurrentPeriod(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(currentPeriod);

        when(currentPeriod.getLinks()).thenReturn(currentPeriodLinks);

        when(currentPeriodLinks.getProfitAndLoss()).thenReturn(null);

        when(smallFullResourceHandler.currentPeriodProfitAndLoss())
                .thenReturn(currentPeriodProfitAndLossResourceHandler);

        when(currentPeriodProfitAndLossResourceHandler.create(CURRENT_PERIOD_URI, currentPeriodProfitAndLoss))
                .thenReturn(currentPeriodProfitAndLossCreate);

        when(currentPeriodProfitAndLossCreate.execute()).thenReturn(currentPeriodApiResponseWithData);

        when(currentPeriodApiResponseWithData.hasErrors()).thenReturn(false);

        when(companyService.getCompanyProfile(COMPANY_NUMBER)).thenReturn(companyProfile);

        when(companyService.isMultiYearFiler(companyProfile)).thenReturn(true);

        when(profitAndLossTransformer.getPreviousPeriodProfitAndLoss(profitAndLoss))
                .thenReturn(previousPeriodProfitAndLoss);

        when(previousPeriodService.getPreviousPeriod(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(previousPeriod);

        when(previousPeriod.getLinks()).thenReturn(previousPeriodLinks);

        when(previousPeriodLinks.getProfitAndLoss()).thenReturn(null);

        when(smallFullResourceHandler.previousPeriodProfitAndLoss())
                .thenReturn(previousPeriodProfitAndLossResourceHandler);

        when(previousPeriodProfitAndLossResourceHandler.create(PREVIOUS_PERIOD_URI, previousPeriodProfitAndLoss))
                .thenReturn(previousPeriodProfitAndLossCreate);

        when(previousPeriodProfitAndLossCreate.execute()).thenReturn(previousPeriodApiResponseWithData);

        when(previousPeriodApiResponseWithData.hasErrors()).thenReturn(false);

        assertEquals(0,
                profitAndLossService
                        .submitProfitAndLoss(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, COMPANY_NUMBER, profitAndLoss)
                                .size());
    }

    @Test
    @DisplayName("Update profit and loss - single year filer - success")
    void updateProfitAndLossSingleYearFilerSuccess()
            throws ServiceException, ApiErrorResponseException, URIValidationException {

        when(profitAndLossTransformer.getCurrentPeriodProfitAndLoss(profitAndLoss))
                .thenReturn(currentPeriodProfitAndLoss);

        when(currentPeriodService.getCurrentPeriod(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(currentPeriod);

        when(currentPeriod.getLinks()).thenReturn(currentPeriodLinks);

        when(currentPeriodLinks.getProfitAndLoss()).thenReturn(CURRENT_PERIOD_PROFIT_AND_LOSS_LINK);

        when(smallFullResourceHandler.currentPeriodProfitAndLoss())
                .thenReturn(currentPeriodProfitAndLossResourceHandler);

        when(currentPeriodProfitAndLossResourceHandler.update(CURRENT_PERIOD_URI, currentPeriodProfitAndLoss))
                .thenReturn(currentPeriodProfitAndLossUpdate);

        when(currentPeriodProfitAndLossUpdate.execute()).thenReturn(currentPeriodApiResponseNoData);

        when(currentPeriodApiResponseNoData.hasErrors()).thenReturn(false);

        when(companyService.getCompanyProfile(COMPANY_NUMBER)).thenReturn(companyProfile);

        when(companyService.isMultiYearFiler(companyProfile)).thenReturn(false);

        assertEquals(0,
                profitAndLossService
                        .submitProfitAndLoss(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, COMPANY_NUMBER, profitAndLoss)
                                .size());
    }

    @Test
    @DisplayName("Update profit and loss - multi year filer - success")
    void updateProfitAndLossMultiYearFilerSuccess()
            throws ServiceException, ApiErrorResponseException, URIValidationException {

        when(profitAndLossTransformer.getCurrentPeriodProfitAndLoss(profitAndLoss))
                .thenReturn(currentPeriodProfitAndLoss);

        when(currentPeriodService.getCurrentPeriod(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(currentPeriod);

        when(currentPeriod.getLinks()).thenReturn(currentPeriodLinks);

        when(currentPeriodLinks.getProfitAndLoss()).thenReturn(CURRENT_PERIOD_PROFIT_AND_LOSS_LINK);

        when(smallFullResourceHandler.currentPeriodProfitAndLoss())
                .thenReturn(currentPeriodProfitAndLossResourceHandler);

        when(currentPeriodProfitAndLossResourceHandler.update(CURRENT_PERIOD_URI, currentPeriodProfitAndLoss))
                .thenReturn(currentPeriodProfitAndLossUpdate);

        when(currentPeriodProfitAndLossUpdate.execute()).thenReturn(currentPeriodApiResponseNoData);

        when(currentPeriodApiResponseNoData.hasErrors()).thenReturn(false);

        when(companyService.getCompanyProfile(COMPANY_NUMBER)).thenReturn(companyProfile);

        when(companyService.isMultiYearFiler(companyProfile)).thenReturn(true);

        when(profitAndLossTransformer.getPreviousPeriodProfitAndLoss(profitAndLoss))
                .thenReturn(previousPeriodProfitAndLoss);

        when(previousPeriodService.getPreviousPeriod(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(previousPeriod);

        when(previousPeriod.getLinks()).thenReturn(previousPeriodLinks);

        when(previousPeriodLinks.getProfitAndLoss()).thenReturn(PREVIOUS_PERIOD_PROFIT_AND_LOSS_LINK);

        when(smallFullResourceHandler.previousPeriodProfitAndLoss())
                .thenReturn(previousPeriodProfitAndLossResourceHandler);

        when(previousPeriodProfitAndLossResourceHandler.update(PREVIOUS_PERIOD_URI, previousPeriodProfitAndLoss))
                .thenReturn(previousPeriodProfitAndLossUpdate);

        when(previousPeriodProfitAndLossUpdate.execute()).thenReturn(previousPeriodApiResponseNoData);

        when(previousPeriodApiResponseNoData.hasErrors()).thenReturn(false);

        assertEquals(0,
                profitAndLossService
                        .submitProfitAndLoss(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, COMPANY_NUMBER, profitAndLoss)
                                .size());
    }

    @Test
    @DisplayName("Create profit and loss - multi year filer - current period validation errors")
    void createProfitAndLossMultiYearFilerCurrentPeriodValidationErrors()
            throws ServiceException, ApiErrorResponseException, URIValidationException {

        when(profitAndLossTransformer.getCurrentPeriodProfitAndLoss(profitAndLoss))
                .thenReturn(currentPeriodProfitAndLoss);

        when(currentPeriodService.getCurrentPeriod(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(currentPeriod);

        when(currentPeriod.getLinks()).thenReturn(currentPeriodLinks);

        when(currentPeriodLinks.getProfitAndLoss()).thenReturn(null);

        when(smallFullResourceHandler.currentPeriodProfitAndLoss())
                .thenReturn(currentPeriodProfitAndLossResourceHandler);

        when(currentPeriodProfitAndLossResourceHandler.create(CURRENT_PERIOD_URI, currentPeriodProfitAndLoss))
                .thenReturn(currentPeriodProfitAndLossCreate);

        when(currentPeriodProfitAndLossCreate.execute()).thenReturn(currentPeriodApiResponseWithData);

        when(currentPeriodApiResponseWithData.hasErrors()).thenReturn(true);

        when(currentPeriodApiResponseWithData.getErrors()).thenReturn(apiErrors);

        when(validationContext.getValidationErrors(apiErrors)).thenReturn(new ArrayList<>());

        when(companyService.getCompanyProfile(COMPANY_NUMBER)).thenReturn(companyProfile);

        when(companyService.isMultiYearFiler(companyProfile)).thenReturn(true);

        when(profitAndLossTransformer.getPreviousPeriodProfitAndLoss(profitAndLoss))
                .thenReturn(previousPeriodProfitAndLoss);

        when(previousPeriodService.getPreviousPeriod(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(previousPeriod);

        when(previousPeriod.getLinks()).thenReturn(previousPeriodLinks);

        when(previousPeriodLinks.getProfitAndLoss()).thenReturn(null);

        when(smallFullResourceHandler.previousPeriodProfitAndLoss())
                .thenReturn(previousPeriodProfitAndLossResourceHandler);

        when(previousPeriodProfitAndLossResourceHandler.create(PREVIOUS_PERIOD_URI, previousPeriodProfitAndLoss))
                .thenReturn(previousPeriodProfitAndLossCreate);

        when(previousPeriodProfitAndLossCreate.execute()).thenReturn(previousPeriodApiResponseWithData);

        when(previousPeriodApiResponseWithData.hasErrors()).thenReturn(false);

        assertNotNull(profitAndLossService
                .submitProfitAndLoss(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, COMPANY_NUMBER, profitAndLoss));
    }

    @Test
    @DisplayName("Create profit and loss - multi year filer - previous period validation errors")
    void createProfitAndLossMultiYearFilerPreviousPeriodValidationErrors()
            throws ServiceException, ApiErrorResponseException, URIValidationException {

        when(profitAndLossTransformer.getCurrentPeriodProfitAndLoss(profitAndLoss))
                .thenReturn(currentPeriodProfitAndLoss);

        when(currentPeriodService.getCurrentPeriod(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(currentPeriod);

        when(currentPeriod.getLinks()).thenReturn(currentPeriodLinks);

        when(currentPeriodLinks.getProfitAndLoss()).thenReturn(null);

        when(smallFullResourceHandler.currentPeriodProfitAndLoss())
                .thenReturn(currentPeriodProfitAndLossResourceHandler);

        when(currentPeriodProfitAndLossResourceHandler.create(CURRENT_PERIOD_URI, currentPeriodProfitAndLoss))
                .thenReturn(currentPeriodProfitAndLossCreate);

        when(currentPeriodProfitAndLossCreate.execute()).thenReturn(currentPeriodApiResponseWithData);

        when(currentPeriodApiResponseWithData.hasErrors()).thenReturn(false);

        when(companyService.getCompanyProfile(COMPANY_NUMBER)).thenReturn(companyProfile);

        when(companyService.isMultiYearFiler(companyProfile)).thenReturn(true);

        when(profitAndLossTransformer.getPreviousPeriodProfitAndLoss(profitAndLoss))
                .thenReturn(previousPeriodProfitAndLoss);

        when(previousPeriodService.getPreviousPeriod(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(previousPeriod);

        when(previousPeriod.getLinks()).thenReturn(previousPeriodLinks);

        when(previousPeriodLinks.getProfitAndLoss()).thenReturn(null);

        when(smallFullResourceHandler.previousPeriodProfitAndLoss())
                .thenReturn(previousPeriodProfitAndLossResourceHandler);

        when(previousPeriodProfitAndLossResourceHandler.create(PREVIOUS_PERIOD_URI, previousPeriodProfitAndLoss))
                .thenReturn(previousPeriodProfitAndLossCreate);

        when(previousPeriodProfitAndLossCreate.execute()).thenReturn(previousPeriodApiResponseWithData);

        when(previousPeriodApiResponseWithData.hasErrors()).thenReturn(true);

        when(previousPeriodApiResponseWithData.getErrors()).thenReturn(apiErrors);

        when(validationContext.getValidationErrors(apiErrors)).thenReturn(new ArrayList<>());

        assertNotNull(profitAndLossService
                .submitProfitAndLoss(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, COMPANY_NUMBER, profitAndLoss));
    }
}
