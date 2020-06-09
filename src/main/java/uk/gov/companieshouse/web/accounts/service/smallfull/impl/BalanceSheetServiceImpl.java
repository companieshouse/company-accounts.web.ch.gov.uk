package uk.gov.companieshouse.web.accounts.service.smallfull.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.model.accounts.smallfull.CurrentPeriodApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.PreviousPeriodApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.SmallFullApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.SmallFullLinks;
import uk.gov.companieshouse.api.model.company.CompanyProfileApi;
import uk.gov.companieshouse.web.accounts.api.ApiClientService;
import uk.gov.companieshouse.web.accounts.enumeration.NoteType;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.smallfull.BalanceSheet;
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
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.creditorswithinoneyear.CreditorsWithinOneYear;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.stocks.StocksNote;
import uk.gov.companieshouse.web.accounts.service.NoteService;
import uk.gov.companieshouse.web.accounts.service.company.CompanyService;
import uk.gov.companieshouse.web.accounts.service.smallfull.BalanceSheetService;
import uk.gov.companieshouse.web.accounts.service.smallfull.CreditorsAfterOneYearService;
import uk.gov.companieshouse.web.accounts.service.smallfull.CurrentPeriodService;
import uk.gov.companieshouse.web.accounts.service.smallfull.FixedAssetsInvestmentsService;
import uk.gov.companieshouse.web.accounts.service.smallfull.PreviousPeriodService;
import uk.gov.companieshouse.web.accounts.service.smallfull.SmallFullService;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.BalanceSheetTransformer;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

@Service
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class BalanceSheetServiceImpl implements BalanceSheetService {

    @Autowired
    private BalanceSheetTransformer transformer;

    @Autowired
    private ApiClientService apiClientService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private NoteService<uk.gov.companieshouse.web.accounts.model.smallfull.notes.debtors.Debtors> debtorsService;

    @Autowired
    private NoteService<CreditorsWithinOneYear> creditorsWithinOneYearService;

    @Autowired
    private CreditorsAfterOneYearService creditorsAfterOneYearService;

    @Autowired
    private NoteService<StocksNote> stocksService;

    @Autowired
    private NoteService<uk.gov.companieshouse.web.accounts.model.smallfull.notes.intangible.IntangibleAssets> intangibleAssetsNoteService;

    @Autowired
    private NoteService<uk.gov.companieshouse.web.accounts.model.smallfull.notes.tangible.TangibleAssets> tangibleAssetsNoteService;

    @Autowired
    private FixedAssetsInvestmentsService fixedAssetsInvestmentsService;

    @Autowired
    private NoteService<uk.gov.companieshouse.web.accounts.model.smallfull.notes.currentassetsinvestments.CurrentAssetsInvestments> currentAssetsInvestmentsService;

    @Autowired
    private SmallFullService smallFullService;

    @Autowired
    private CurrentPeriodService currentPeriodService;

    @Autowired
    private PreviousPeriodService previousPeriodService;

    private BalanceSheet cachedBalanceSheet;

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

        CurrentPeriodApi currentPeriodApi =
                currentPeriodService.getCurrentPeriod(apiClient, transactionId, companyAccountsId);

        PreviousPeriodApi previousPeriodApi = null;

        CompanyProfileApi companyProfileApi = companyService.getCompanyProfile(companyNumber);

        if (companyService.isMultiYearFiler(companyProfileApi)) {
            previousPeriodApi =
                    previousPeriodService.getPreviousPeriod(apiClient, transactionId, companyAccountsId);
        }

        BalanceSheet balanceSheet = transformer.getBalanceSheet(currentPeriodApi,
                previousPeriodApi);

        balanceSheet.setLbg(lbgTypes.contains(companyProfileApi.getType()));
        balanceSheet.setBalanceSheetHeadings(companyService.getBalanceSheetHeadings(companyProfileApi));

        cachedBalanceSheet = balanceSheet;

        return balanceSheet;
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

        CompanyProfileApi companyProfileApi = companyService.getCompanyProfile(companyNumber);
        
        if (companyService.isMultiYearFiler(companyProfileApi)) {

            PreviousPeriodApi previousPeriodApi =
                    previousPeriodService.getPreviousPeriod(apiClient, transactionId, companyAccountsId);

            if (previousPeriodApi == null) {
                previousPeriodApi = new PreviousPeriodApi();
            }

            previousPeriodApi.setBalanceSheet(transformer.getPreviousPeriodBalanceSheet(balanceSheet));

            previousPeriodService.submitPreviousPeriod(
                    apiClient, smallFullApi, transactionId, companyAccountsId, previousPeriodApi, validationErrors);
        }

        CurrentPeriodApi currentPeriod =
                currentPeriodService.getCurrentPeriod(apiClient, transactionId, companyAccountsId);

        if (currentPeriod == null) {
            currentPeriod = new CurrentPeriodApi();
        }

        currentPeriod.setBalanceSheet(transformer.getCurrentPeriodBalanceSheet(balanceSheet));

        currentPeriodService.submitCurrentPeriod(
                apiClient, smallFullApi, transactionId, companyAccountsId, currentPeriod, validationErrors);

        checkConditionalNotes(balanceSheet, smallFullApi.getLinks(),
                transactionId, companyAccountsId);

        return validationErrors;

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

            debtorsService.delete(transactionId, companyAccountsId, NoteType.SMALL_FULL_DEBTORS);
        }

        if ((isCreditorsWithinOneYearCurrentAmountNullOrZero(balanceSheet)
                && isCreditorsWithinOneYearPreviousAmountNullOrZero(balanceSheet))
                && smallFullLinks.getCreditorsWithinOneYearNote() != null) {

            creditorsWithinOneYearService
                    .delete(transactionId, companyAccountsId, NoteType.SMALL_FULL_CREDITORS_WITHIN_ONE_YEAR);
        }

        if ((isCreditorsAfterOneYearCurrentAmountNullOrZero(balanceSheet)
                && isCreditorsAfterOneYearPreviousAmountNullOrZero(balanceSheet))
                && smallFullLinks.getCreditorsAfterMoreThanOneYearNote() != null) {

            creditorsAfterOneYearService.deleteCreditorsAfterOneYear(transactionId, companyAccountsId);
        }

        if ((isIntangibleAssetsCurrentAmountNullOrZero(balanceSheet)
                && isIntangibleAssetsPreviousAmountNullOrZero(balanceSheet))
                && smallFullLinks.getIntangibleAssetsNote() != null) {

            intangibleAssetsNoteService.delete(transactionId, companyAccountsId, NoteType.SMALL_FULL_INTANGIBLE_ASSETS);
        }

        if ((isTangibleAssetsCurrentAmountNullOrZero(balanceSheet)
                && isTangibleAssetsPreviousAmountNullOrZero(balanceSheet))
                && smallFullLinks.getTangibleAssetsNote() != null) {

            tangibleAssetsNoteService.delete(transactionId, companyAccountsId, NoteType.SMALL_FULL_TANGIBLE_ASSETS);
        }

        if ((isFixedInvestmentsCurrentAmountNullOrZero(balanceSheet)
            && isFixedInvestmentsPreviousAmountNullOrZero(balanceSheet))
            && smallFullLinks.getFixedAssetsInvestmentsNote() != null) {

            fixedAssetsInvestmentsService.deleteFixedAssetsInvestments(transactionId, companyAccountsId);
        }

        if ((isStocksCurrentAmountNullOrZero(balanceSheet)
                && isStocksPreviousAmountNullOrZero(balanceSheet))
                && smallFullLinks.getStocksNote() != null) {

            stocksService.delete(transactionId, companyAccountsId, NoteType.SMALL_FULL_STOCKS);
        }

        if ((isCurrentAssetsInvestmentsCurrentAmountNullOrZero(balanceSheet)
                && isCurrentAssetsInvestmentsPreviousAmountNullOrZero(balanceSheet)
                && smallFullLinks.getCurrentAssetsInvestmentsNote() != null)) {

            currentAssetsInvestmentsService.delete(transactionId, companyAccountsId, NoteType.SMALL_FULL_CURRENT_ASSETS_INVESTMENTS);
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

    private boolean isIntangibleAssetsCurrentAmountNullOrZero(BalanceSheet balanceSheet) {
        return Optional.of(balanceSheet)
                .map(BalanceSheet::getFixedAssets)
                .map(FixedAssets::getIntangibleAssets)
                .map(IntangibleAssets::getCurrentAmount)
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
