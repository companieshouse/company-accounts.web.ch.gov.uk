package uk.gov.companieshouse.web.accounts.service.smallfull.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.util.UriTemplate;
import uk.gov.companieshouse.accountsdates.AccountsDatesHelper;
import uk.gov.companieshouse.accountsdates.impl.AccountsDatesHelperImpl;
import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.error.ApiErrorResponseException;
import uk.gov.companieshouse.api.handler.exception.URIValidationException;
import uk.gov.companieshouse.api.model.ApiResponse;
import uk.gov.companieshouse.api.model.accounts.smallfull.CurrentPeriodApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.PreviousPeriodApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.SmallFullApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.SmallFullLinks;
import uk.gov.companieshouse.api.model.company.CompanyProfileApi;
import uk.gov.companieshouse.api.model.company.account.LastAccountsApi;
import uk.gov.companieshouse.api.model.company.account.NextAccountsApi;
import uk.gov.companieshouse.web.accounts.api.ApiClientService;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.smallfull.BalanceSheet;
import uk.gov.companieshouse.web.accounts.model.smallfull.BalanceSheetHeadings;
import uk.gov.companieshouse.web.accounts.model.smallfull.CreditorsAfterOneYear;
import uk.gov.companieshouse.web.accounts.model.smallfull.CreditorsDueWithinOneYear;
import uk.gov.companieshouse.web.accounts.model.smallfull.CurrentAssets;
import uk.gov.companieshouse.web.accounts.model.smallfull.CurrentAssetsInvestments;
import uk.gov.companieshouse.web.accounts.model.smallfull.Debtors;
import uk.gov.companieshouse.web.accounts.model.smallfull.FixedAssets;
import uk.gov.companieshouse.web.accounts.model.smallfull.FixedInvestments;
import uk.gov.companieshouse.web.accounts.model.smallfull.IntangibleAssets;
import uk.gov.companieshouse.web.accounts.model.smallfull.OtherLiabilitiesOrAssets;
import uk.gov.companieshouse.web.accounts.model.smallfull.Stocks;
import uk.gov.companieshouse.web.accounts.model.smallfull.TangibleAssets;
import uk.gov.companieshouse.web.accounts.service.company.CompanyService;
import uk.gov.companieshouse.web.accounts.service.smallfull.BalanceSheetService;
import uk.gov.companieshouse.web.accounts.service.smallfull.CreditorsAfterOneYearService;
import uk.gov.companieshouse.web.accounts.service.smallfull.CreditorsWithinOneYearService;
import uk.gov.companieshouse.web.accounts.service.smallfull.CurrentAssetsInvestmentsService;
import uk.gov.companieshouse.web.accounts.service.smallfull.DebtorsService;
import uk.gov.companieshouse.web.accounts.service.smallfull.FixedAssetsInvestmentsService;
import uk.gov.companieshouse.web.accounts.service.smallfull.IntangibleAssetsNoteService;
import uk.gov.companieshouse.web.accounts.service.smallfull.SmallFullService;
import uk.gov.companieshouse.web.accounts.service.smallfull.StocksService;
import uk.gov.companieshouse.web.accounts.service.smallfull.TangibleAssetsNoteService;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.BalanceSheetTransformer;
import uk.gov.companieshouse.web.accounts.util.ValidationContext;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;
import uk.gov.companieshouse.web.accounts.validation.helper.ServiceExceptionHandler;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class BalanceSheetServiceImpl implements BalanceSheetService {

    private static final String SMALL_FULL_URI =
            "/transactions/{transactionId}/company-accounts/{companyAccountsId}/small-full";

    private static final UriTemplate CURRENT_PERIOD_URI =
            new UriTemplate(SMALL_FULL_URI + "/current-period");

    private static final UriTemplate PREVIOUS_PERIOD_URI =
            new UriTemplate(SMALL_FULL_URI + "/previous-period");

    @Autowired
    private BalanceSheetTransformer transformer;

    @Autowired
    private ApiClientService apiClientService;

    @Autowired
    private ServiceExceptionHandler serviceExceptionHandler;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private DebtorsService debtorsService;

    @Autowired
    private CreditorsWithinOneYearService creditorsWithinOneYearService;

    @Autowired
    private CreditorsAfterOneYearService creditorsAfterOneYearService;

    @Autowired
    private StocksService stocksService;

    @Autowired
    private TangibleAssetsNoteService tangibleAssetsNoteService;

    @Autowired
    private IntangibleAssetsNoteService intangibleAssetsNoteService;

    @Autowired
    private FixedAssetsInvestmentsService fixedAssetsInvestmentsService;

    @Autowired
    private CurrentAssetsInvestmentsService currentAssetsInvestmentsService;

    @Autowired
    private SmallFullService smallFullService;

    @Autowired
    private ValidationContext validationContext;

    private BalanceSheet cachedBalanceSheet;

    private AccountsDatesHelper accountsDatesHelper = new AccountsDatesHelperImpl();

    private static final String CURRENT_PERIOD_RESOURCE = "current period";
    private static final String PREVIOUS_PERIOD_RESOURCE = "previous period";

    private static List<String> lbgTypes = Arrays.asList(
        "private-limited-guarant-nsc",
        "private-limited-guarant-nsc-limited-exemption"
    );

    @Override
    public BalanceSheet getBalanceSheet(String transactionId, String companyAccountsId,
                                        String companyNumber)
            throws ServiceException {

        if (cachedBalanceSheet != null) {
            return cachedBalanceSheet;
        }

        ApiClient apiClient = apiClientService.getApiClient();

        CurrentPeriodApi currentPeriodApi = getCurrentPeriod(apiClient,
                transactionId, companyAccountsId);
        PreviousPeriodApi previousPeriodApi = null;

        CompanyProfileApi companyProfileApi = getCompanyProfile(companyNumber);

        if (isMultipleYearFiler(companyProfileApi)) {
            previousPeriodApi = getPreviousPeriod(apiClient, transactionId,
                    companyAccountsId);
        }

        BalanceSheetHeadings balanceSheetHeadings = getBalanceSheetHeadings(companyProfileApi);

        BalanceSheet balanceSheet = transformer.getBalanceSheet(currentPeriodApi,
                previousPeriodApi);

        balanceSheet.setLbg(lbgTypes.contains(companyProfileApi.getType()));
        balanceSheet.setBalanceSheetHeadings(balanceSheetHeadings);

        cachedBalanceSheet = balanceSheet;

        return balanceSheet;
    }

    private CurrentPeriodApi getCurrentPeriod(ApiClient apiClient,
                                              String transactionId, String companyAccountsId) throws ServiceException {

        try {
            return apiClient.smallFull().currentPeriod()
                    .get(CURRENT_PERIOD_URI.expand(transactionId,
                            companyAccountsId).toString()).execute().getData();
        } catch (ApiErrorResponseException e) {
            serviceExceptionHandler.handleRetrievalException(e, CURRENT_PERIOD_RESOURCE);
        } catch (URIValidationException e) {
            serviceExceptionHandler.handleURIValidationException(e, CURRENT_PERIOD_RESOURCE);
        }

        return null;
    }

    private PreviousPeriodApi getPreviousPeriod(ApiClient apiClient,
                                                String transactionId,
                                                String companyAccountsId)
            throws ServiceException {

        try {
            return apiClient.smallFull().previousPeriod()
                    .get(PREVIOUS_PERIOD_URI.expand(transactionId,
                            companyAccountsId).toString()).execute().getData();
        } catch (ApiErrorResponseException e) {
            serviceExceptionHandler.handleRetrievalException(e, PREVIOUS_PERIOD_RESOURCE);
        } catch (URIValidationException e) {
            serviceExceptionHandler.handleURIValidationException(e, PREVIOUS_PERIOD_RESOURCE);
        }

        return null;
    }

    @Override
    public List<ValidationError> postBalanceSheet(String transactionId,
                                                  String companyAccountsId,
                                                  BalanceSheet balanceSheet,
                                                  String companyNumber)
            throws ServiceException {

        invalidateRequestScopedCache();

        ApiClient apiClient = apiClientService.getApiClient();

        List<ValidationError> validationErrors = new ArrayList<>();

        SmallFullApi smallFullApi =
                smallFullService.getSmallFullAccounts(apiClient, transactionId, companyAccountsId);

        CompanyProfileApi companyProfileApi = getCompanyProfile(companyNumber);
        if (isMultipleYearFiler(companyProfileApi)) {
            PreviousPeriodApi previousPeriodApi = transformer.getPreviousPeriod(balanceSheet);

            String previousPeriodUri = PREVIOUS_PERIOD_URI.expand(transactionId,
                    companyAccountsId).toString();
            createPreviousPeriod(apiClient, smallFullApi, previousPeriodUri, previousPeriodApi,
                    validationErrors);
        }

        CurrentPeriodApi currentPeriod = transformer.getCurrentPeriod(balanceSheet);

        String currentPeriodUri =
                CURRENT_PERIOD_URI.expand(transactionId, companyAccountsId).toString();
        createCurrentPeriod(apiClient, smallFullApi, currentPeriodUri, currentPeriod,
                validationErrors);

        checkConditionalNotes(balanceSheet, smallFullApi.getLinks(),
                transactionId, companyAccountsId);

        return validationErrors;

    }

    private void createPreviousPeriod(ApiClient apiClient, SmallFullApi smallFullApi,
                                      String previousPeriodUri, PreviousPeriodApi previousPeriodApi,
                                      List<ValidationError> validationErrors)
            throws ServiceException {
        boolean isCreated = hasPreviousPeriod(smallFullApi.getLinks());
        try {
            ApiResponse apiResponse;
            if (!isCreated) {
                apiResponse = apiClient.smallFull().previousPeriod().create(previousPeriodUri,
                        previousPeriodApi).execute();
            } else {
                apiResponse = apiClient.smallFull().previousPeriod().update(previousPeriodUri,
                        previousPeriodApi).execute();
            }

            if (apiResponse.hasErrors()) {
                validationErrors.addAll(validationContext.getValidationErrors(apiResponse.getErrors()));
            }
        } catch (ApiErrorResponseException e) {
            serviceExceptionHandler.handleSubmissionException(e, PREVIOUS_PERIOD_RESOURCE);
        } catch (URIValidationException e) {
            serviceExceptionHandler.handleURIValidationException(e, PREVIOUS_PERIOD_RESOURCE);
        }
    }

    private void createCurrentPeriod(ApiClient apiClient, SmallFullApi smallFullApi,
                                     String currentPeriodUri, CurrentPeriodApi currentPeriod,
                                     List<ValidationError> validationErrors)
            throws ServiceException {

        boolean isCreated = hasCurrentPeriod(smallFullApi.getLinks());
        try {
            ApiResponse apiResponse;
            if (!isCreated) {
                apiResponse = apiClient.smallFull().currentPeriod().create(currentPeriodUri, currentPeriod).execute();
            } else {
                apiResponse = apiClient.smallFull().currentPeriod().update(currentPeriodUri, currentPeriod).execute();
            }

            if (apiResponse.hasErrors()) {
                validationErrors.addAll(validationContext.getValidationErrors(apiResponse.getErrors()));
            }
        } catch (ApiErrorResponseException e) {
            serviceExceptionHandler.handleSubmissionException(e, CURRENT_PERIOD_RESOURCE);
        } catch (URIValidationException e) {
            serviceExceptionHandler.handleURIValidationException(e, CURRENT_PERIOD_RESOURCE);
        }
    }

    private BalanceSheetHeadings getBalanceSheetHeadings(CompanyProfileApi companyProfile) {
        boolean isSameYear = isSameYearFiler(companyProfile);
        BalanceSheetHeadings balanceSheetHeadings = new BalanceSheetHeadings();
        balanceSheetHeadings.setPreviousPeriodHeading(getPreviousPeriodHeading(companyProfile,
                isSameYear));
        balanceSheetHeadings.setCurrentPeriodHeading(getCurrentPeriodHeading(companyProfile,
                isSameYear));
        return balanceSheetHeadings;
    }

    private String getCurrentPeriodHeading(CompanyProfileApi companyProfile, boolean isSameYear) {
        NextAccountsApi nextAccountsApi = companyProfile.getAccounts().getNextAccounts();
        LocalDate currentPeriodEndOn = nextAccountsApi.getPeriodEndOn();
        LocalDate currentPeriodStartOn = nextAccountsApi.getPeriodStartOn();

        return accountsDatesHelper.generateBalanceSheetHeading(currentPeriodStartOn,
                currentPeriodEndOn, isSameYear);
    }

    private String getPreviousPeriodHeading(CompanyProfileApi companyProfile, boolean isSameYear) {
        LastAccountsApi lastAccountsApi = companyProfile.getAccounts().getLastAccounts();
        if (isMultipleYearFiler(companyProfile)) {
            LocalDate previousPeriodStartOn = lastAccountsApi.getPeriodStartOn();
            LocalDate previousPeriodEndOn = lastAccountsApi.getPeriodEndOn();

            return accountsDatesHelper.generateBalanceSheetHeading(previousPeriodStartOn,
                    previousPeriodEndOn, isSameYear);
        }
        return null;
    }

    private boolean isSameYearFiler(CompanyProfileApi companyProfile) {
        if (isMultipleYearFiler(companyProfile)) {
            LastAccountsApi lastAccountsApi = companyProfile.getAccounts().getLastAccounts();
            LocalDate previousPeriodEndOn = lastAccountsApi.getPeriodEndOn();

            NextAccountsApi nextAccountsApi = companyProfile.getAccounts().getNextAccounts();
            LocalDate currentPeriodEndOn = nextAccountsApi.getPeriodEndOn();

            return accountsDatesHelper.isSameYear(previousPeriodEndOn, currentPeriodEndOn);
        }
        return false;
    }

    private CompanyProfileApi getCompanyProfile(String companyNumber) throws ServiceException {
        return companyService.getCompanyProfile(companyNumber);
    }

    private boolean isMultipleYearFiler(CompanyProfileApi companyProfile) {
        LastAccountsApi lastAccountsApi = companyProfile.getAccounts().getLastAccounts();
        return lastAccountsApi != null && lastAccountsApi.getPeriodEndOn() != null;
    }

    private boolean hasCurrentPeriod(SmallFullLinks smallFullLinks) {
        return smallFullLinks.getCurrentPeriod() != null;
    }

    private boolean hasPreviousPeriod(SmallFullLinks smallFullLinks) {
        return smallFullLinks.getPreviousPeriod() != null;
    }

    /**
     * Checks whether a conditional note exists when there is no balance sheet value for it
     * If there is, the note is then deleted
     *
     * @param balanceSheet      the populated balance sheet
     * @param smallFullLinks    the links used to determine if notes are present
     * @param transactionId     The id of the CHS transaction
     * @param companyAccountsId The company accounts identifier
     * @throws ServiceException if there's an error on submission
     */
    private void checkConditionalNotes(BalanceSheet balanceSheet, SmallFullLinks smallFullLinks,
                                       String transactionId, String companyAccountsId) throws ServiceException {

        if ((isDebtorsCurrentAmountNullOrZero(balanceSheet)
                && isDebtorsPreviousAmountNullOrZero(balanceSheet))
                && smallFullLinks.getDebtorsNote() != null) {

            debtorsService.deleteDebtors(transactionId, companyAccountsId);
        }

        if ((isCreditorsWithinOneYearCurrentAmountNullOrZero(balanceSheet)
                && isCreditorsWithinOneYearPreviousAmountNullOrZero(balanceSheet))
                && smallFullLinks.getCreditorsWithinOneYearNote() != null) {

            creditorsWithinOneYearService
                    .deleteCreditorsWithinOneYear(transactionId, companyAccountsId);
        }

        if ((isCreditorsAfterOneYearCurrentAmountNullOrZero(balanceSheet)
                && isCreditorsAfterOneYearPreviousAmountNullOrZero(balanceSheet))
                && smallFullLinks.getCreditorsAfterMoreThanOneYearNote() != null) {

            creditorsAfterOneYearService.deleteCreditorsAfterOneYear(transactionId, companyAccountsId);
        }

        if ((isTangibleAssetsCurrentAmountNullOrZero(balanceSheet)
                && isTangibleAssetsPreviousAmountNullOrZero(balanceSheet))
                && smallFullLinks.getTangibleAssetsNote() != null) {

            tangibleAssetsNoteService.deleteTangibleAssets(transactionId, companyAccountsId);
        }

        if ((isIntangibleAssetsCurrentAmountNullOrZero(balanceSheet)
                && isIntangibleAssetsPreviousAmountNullOrZero(balanceSheet))
                && smallFullLinks.getIntangibleAssetsNote() != null) {

            intangibleAssetsNoteService.deleteIntangibleAssets(transactionId, companyAccountsId);
        }

        if ((isFixedInvestmentsCurrentAmountNullOrZero(balanceSheet)
            && isFixedInvestmentsPreviousAmountNullOrZero(balanceSheet))
            && smallFullLinks.getFixedAssetsInvestmentsNote() != null) {

            fixedAssetsInvestmentsService.deleteFixedAssetsInvestments(transactionId, companyAccountsId);
        }

        if ((isStocksCurrentAmountNullOrZero(balanceSheet)
                && isStocksPreviousAmountNullOrZero(balanceSheet))
                && smallFullLinks.getStocksNote() != null) {

            stocksService.deleteStocks(transactionId, companyAccountsId);
        }

        if ((isCurrentAssetsInvestmentsCurrentAmountNullOrZero(balanceSheet)
                && isCurrentAssetsInvestmentsPreviousAmountNullOrZero(balanceSheet)
                && smallFullLinks.getCurrentAssetsInvestmentsNote() != null)) {

            currentAssetsInvestmentsService.deleteCurrentAssetsInvestments(transactionId, companyAccountsId);
        }
    }

    private boolean isDebtorsCurrentAmountNullOrZero(BalanceSheet balanceSheet) {
        return Optional.of(balanceSheet)
                .map(BalanceSheet::getCurrentAssets)
                .map(CurrentAssets::getDebtors)
                .map(Debtors::getCurrentAmount)
                .orElse(0L).equals(0L);
    }

    private boolean isDebtorsPreviousAmountNullOrZero(BalanceSheet balanceSheet) {
        return Optional.of(balanceSheet)
                .map(BalanceSheet::getCurrentAssets)
                .map(CurrentAssets::getDebtors)
                .map(Debtors::getPreviousAmount)
                .orElse(0L).equals(0L);
    }

    private boolean isCreditorsWithinOneYearCurrentAmountNullOrZero(BalanceSheet balanceSheet) {
        return Optional.of(balanceSheet)
                .map(BalanceSheet::getOtherLiabilitiesOrAssets)
                .map(OtherLiabilitiesOrAssets::getCreditorsDueWithinOneYear)
                .map(CreditorsDueWithinOneYear::getCurrentAmount)
                .orElse(0L).equals(0L);
    }

    private boolean isCreditorsWithinOneYearPreviousAmountNullOrZero(BalanceSheet balanceSheet) {
        return Optional.of(balanceSheet)
                .map(BalanceSheet::getOtherLiabilitiesOrAssets)
                .map(OtherLiabilitiesOrAssets::getCreditorsDueWithinOneYear)
                .map(CreditorsDueWithinOneYear::getPreviousAmount)
                .orElse(0L).equals(0L);
    }

    private boolean isCreditorsAfterOneYearCurrentAmountNullOrZero(BalanceSheet balanceSheet) {
        return Optional.of(balanceSheet)
                .map(BalanceSheet::getOtherLiabilitiesOrAssets)
                .map(OtherLiabilitiesOrAssets::getCreditorsAfterOneYear)
                .map(CreditorsAfterOneYear::getCurrentAmount)
                .orElse(0L).equals(0L);
    }

    private boolean isCreditorsAfterOneYearPreviousAmountNullOrZero(BalanceSheet balanceSheet) {
        return Optional.of(balanceSheet)
                .map(BalanceSheet::getOtherLiabilitiesOrAssets)
                .map(OtherLiabilitiesOrAssets::getCreditorsAfterOneYear)
                .map(CreditorsAfterOneYear::getPreviousAmount)
                .orElse(0L).equals(0L);
    }

    private boolean isTangibleAssetsCurrentAmountNullOrZero(BalanceSheet balanceSheet) {
        return Optional.of(balanceSheet)
                .map(BalanceSheet::getFixedAssets)
                .map(FixedAssets::getTangibleAssets)
                .map(TangibleAssets::getCurrentAmount)
                .orElse(0L).equals(0L);
    }

    private boolean isTangibleAssetsPreviousAmountNullOrZero(BalanceSheet balanceSheet) {
        return Optional.of(balanceSheet)
                .map(BalanceSheet::getFixedAssets)
                .map(FixedAssets::getTangibleAssets)
                .map(TangibleAssets::getPreviousAmount)
                .orElse(0L).equals(0L);
    }

    private boolean isIntangibleAssetsCurrentAmountNullOrZero(BalanceSheet balanceSheet) {
        return Optional.of(balanceSheet)
                .map(BalanceSheet::getFixedAssets)
                .map(FixedAssets::getIntangibleAssets)
                .map(IntangibleAssets::getCurrentAmount)
                .orElse(0L).equals(0L);
    }

    private boolean isIntangibleAssetsPreviousAmountNullOrZero(BalanceSheet balanceSheet) {
        return Optional.of(balanceSheet)
                .map(BalanceSheet::getFixedAssets)
                .map(FixedAssets::getIntangibleAssets)
                .map(IntangibleAssets::getPreviousAmount)
                .orElse(0L).equals(0L);
    }

    private boolean isFixedInvestmentsCurrentAmountNullOrZero(BalanceSheet balanceSheet) {
        return Optional.of(balanceSheet)
            .map(BalanceSheet::getFixedAssets)
            .map(FixedAssets::getInvestments)
            .map(FixedInvestments::getCurrentAmount)
            .orElse(0L).equals(0L);
    }

    private boolean isFixedInvestmentsPreviousAmountNullOrZero(BalanceSheet balanceSheet) {
        return Optional.of(balanceSheet)
            .map(BalanceSheet::getFixedAssets)
            .map(FixedAssets::getInvestments)
            .map(FixedInvestments::getPreviousAmount)
            .orElse(0L).equals(0L);
    }

    private boolean isStocksCurrentAmountNullOrZero(BalanceSheet balanceSheet) {
        return Optional.of(balanceSheet)
                .map(BalanceSheet::getCurrentAssets)
                .map(CurrentAssets::getStocks)
                .map(Stocks::getCurrentAmount)
                .orElse(0L).equals(0L);
    }

    private boolean isStocksPreviousAmountNullOrZero(BalanceSheet balanceSheet) {
        return Optional.of(balanceSheet)
                .map(BalanceSheet::getCurrentAssets)
                .map(CurrentAssets::getStocks)
                .map(Stocks::getPreviousAmount)
                .orElse(0L).equals(0L);
    }

    private boolean isCurrentAssetsInvestmentsCurrentAmountNullOrZero(BalanceSheet balanceSheet) {
        return Optional.of(balanceSheet)
                .map(BalanceSheet::getCurrentAssets)
                .map(CurrentAssets::getInvestments)
                .map(CurrentAssetsInvestments::getCurrentAmount)
                .orElse(0L).equals(0L);
    }

    private boolean isCurrentAssetsInvestmentsPreviousAmountNullOrZero(BalanceSheet balanceSheet) {
        return Optional.of(balanceSheet)
                .map(BalanceSheet::getCurrentAssets)
                .map(CurrentAssets::getInvestments)
                .map(CurrentAssetsInvestments::getPreviousAmount)
                .orElse(0L).equals(0L);
    }

    private void invalidateRequestScopedCache() {
        cachedBalanceSheet = null;
    }
}
