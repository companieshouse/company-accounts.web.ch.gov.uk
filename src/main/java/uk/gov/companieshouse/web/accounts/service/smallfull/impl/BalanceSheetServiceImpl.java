package uk.gov.companieshouse.web.accounts.service.smallfull.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriTemplate;
import uk.gov.companieshouse.accountsdates.AccountsDatesHelper;
import uk.gov.companieshouse.accountsdates.impl.AccountsDatesHelperImpl;
import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.error.ApiErrorResponseException;
import uk.gov.companieshouse.api.handler.exception.URIValidationException;
import uk.gov.companieshouse.api.model.accounts.smallfull.CurrentPeriodApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.PreviousPeriodApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.SmallFullApi;
import uk.gov.companieshouse.api.model.company.CompanyProfileApi;
import uk.gov.companieshouse.api.model.company.account.LastAccountsApi;
import uk.gov.companieshouse.api.model.company.account.NextAccountsApi;
import uk.gov.companieshouse.web.accounts.api.ApiClientService;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.links.SmallFullLinkType;
import uk.gov.companieshouse.web.accounts.model.smallfull.BalanceSheet;
import uk.gov.companieshouse.web.accounts.model.smallfull.BalanceSheetHeadings;
import uk.gov.companieshouse.web.accounts.service.company.CompanyService;
import uk.gov.companieshouse.web.accounts.service.smallfull.BalanceSheetService;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.BalanceSheetTransformer;
import uk.gov.companieshouse.web.accounts.util.ValidationContext;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

@Service
public class BalanceSheetServiceImpl implements BalanceSheetService {

    @Autowired
    private BalanceSheetTransformer transformer;

    @Autowired
    private ApiClientService apiClientService;
    
    @Autowired
    private ValidationContext validationContext;

    @Autowired
    private CompanyService companyService;

    private AccountsDatesHelper accountsDatesHelper = new AccountsDatesHelperImpl();

    private static final UriTemplate SMALL_FULL_URI =
            new UriTemplate("/transactions/{transactionId}/company-accounts/{companyAccountsId}/small-full");

    private static final UriTemplate CURRENT_PERIOD_URI = new UriTemplate(SMALL_FULL_URI.toString() + "/current-period");
    private static final UriTemplate PREVIOUS_PERIOD_URI = new UriTemplate(SMALL_FULL_URI.toString() + "/previous-period");

    @Override
    public BalanceSheet getBalanceSheet(String transactionId, String companyAccountsId, String companyNumber)
            throws ServiceException {

        ApiClient apiClient = apiClientService.getApiClient();

        CurrentPeriodApi currentPeriodApi = getCurrentPeriod(apiClient, CURRENT_PERIOD_URI, transactionId, companyAccountsId);
        PreviousPeriodApi previousPeriodApi = null;

        CompanyProfileApi companyProfileApi = getCompanyProfile(companyNumber);

        if (isMultipleYearFiler(companyProfileApi)) {
            previousPeriodApi = getPreviousPeriod(apiClient, PREVIOUS_PERIOD_URI, transactionId, companyAccountsId);
        }

        BalanceSheetHeadings balanceSheetHeadings = getBalanceSheetHeadings(companyProfileApi);

        BalanceSheet balanceSheet = transformer.getBalanceSheet(currentPeriodApi, previousPeriodApi);

        balanceSheet.setBalanceSheetHeadings(balanceSheetHeadings);

        return balanceSheet;
    }

    private CurrentPeriodApi getCurrentPeriod(ApiClient apiClient, UriTemplate uri, String transactionId, String companyAccountsId) throws ServiceException {

        try {
            return apiClient.smallFull().currentPeriod().get(uri.expand(transactionId, companyAccountsId).toString()).execute();
        } catch (ApiErrorResponseException e) {

            if (e.getStatusCode() == HttpStatus.NOT_FOUND.value()) {
                return null;
            }

            throw new ServiceException("Error retrieving current period resource", e);
        } catch (URIValidationException e) {

            throw new ServiceException("Invalid URI for current period resource", e);
        }
    }

    private PreviousPeriodApi getPreviousPeriod(ApiClient apiClient, UriTemplate uri, String transactionId, String companyAccountsId) throws ServiceException {

        try {
            return apiClient.smallFull().previousPeriod().get(uri.expand(transactionId, companyAccountsId).toString()).execute();
        } catch (ApiErrorResponseException e) {

            if (e.getStatusCode() == HttpStatus.NOT_FOUND.value()) {
                return null;
            }

            throw new ServiceException("Error retrieving previous period resource", e);
        } catch (URIValidationException e) {

            throw new ServiceException("Invalid URI for previous period resource", e);
        }
    }

    @Override
    public List<ValidationError> postBalanceSheet(String transactionId, String companyAccountsId, BalanceSheet balanceSheet, String companyNumber)
            throws ServiceException {
        ApiClient apiClient = apiClientService.getApiClient();

        List<ValidationError> validationErrors = new ArrayList<>();


        String smallFullUri = SMALL_FULL_URI.expand(transactionId, companyAccountsId).toString();
        SmallFullApi smallFullApi = getSmallFullData(apiClient, smallFullUri);

        CompanyProfileApi companyProfileApi = getCompanyProfile(companyNumber);
        if (isMultipleYearFiler(companyProfileApi)) {
            PreviousPeriodApi previousPeriodApi = transformer.getPreviousPeriod(balanceSheet);

            String previousPeriodUri = PREVIOUS_PERIOD_URI.expand(transactionId, companyAccountsId).toString();
            createPreviousPeriod(apiClient, smallFullApi, previousPeriodUri, previousPeriodApi, validationErrors);
        }

        CurrentPeriodApi currentPeriod = transformer.getCurrentPeriod(balanceSheet);

        String currentPeriodUri = CURRENT_PERIOD_URI.expand(transactionId, companyAccountsId).toString();
        createCurrentPeriod(apiClient, smallFullApi, currentPeriodUri, currentPeriod, validationErrors);

        return validationErrors;

    }

    private SmallFullApi getSmallFullData(ApiClient apiClient, String smallFullUri)
            throws ServiceException {
        try {
            return apiClient.smallFull().get(smallFullUri).execute();
        } catch (ApiErrorResponseException e) {
            throw new ServiceException("Error retrieving small full data", e);
        } catch (URIValidationException e) {
            throw new ServiceException("Invalid URI for small full resource", e);
        }
    }

    private void createPreviousPeriod(ApiClient apiClient, SmallFullApi smallFullApi, String previousPeriodUri, PreviousPeriodApi previousPeriodApi, List<ValidationError> validationErrors)
            throws ServiceException {
        boolean isCreated = hasPreviousPeriod(smallFullApi.getLinks());
        try {
            if (!isCreated) {
                apiClient.smallFull().previousPeriod().create(previousPeriodUri, previousPeriodApi).execute();
            } else {
                apiClient.smallFull().previousPeriod().update(previousPeriodUri, previousPeriodApi).execute();
            }

        } catch (ApiErrorResponseException e) {
            if (e.getStatusCode() == HttpStatus.BAD_REQUEST.value()) {
                validationErrors.addAll(validationContext.getValidationErrors(e));
                if (validationErrors.isEmpty()) {
                    throw new ServiceException("Bad request when submitting previous period resource", e);
                }
            } else {
                throw new ServiceException("Bad request when submitting previous period resource", e);
            }
        } catch (URIValidationException e) {
            throw new ServiceException("Invalid URI for previous period resource", e);
        }
    }

    private void createCurrentPeriod(ApiClient apiClient, SmallFullApi smallFullApi, String currentPeriodUri, CurrentPeriodApi currentPeriod, List<ValidationError> validationErrors)
            throws ServiceException {
        boolean isCreated = hasCurrentPeriod(smallFullApi.getLinks());
        try {
            if (!isCreated) {
                apiClient.smallFull().currentPeriod().create(currentPeriodUri, currentPeriod).execute();
            } else {
                apiClient.smallFull().currentPeriod().update(currentPeriodUri, currentPeriod).execute();
            }

        } catch (ApiErrorResponseException e) {
            if (e.getStatusCode() == HttpStatus.BAD_REQUEST.value()) {
                validationErrors.addAll(validationContext.getValidationErrors(e));
                if (validationErrors.isEmpty()) {
                    throw new ServiceException("Bad request when submitting current period resource", e);
                }
            } else {
                throw new ServiceException("Bad request when submitting current period resource", e);
            }
        } catch (URIValidationException e) {
            throw new ServiceException("Invalid URI for current period resource", e);
        }
    }

    private BalanceSheetHeadings getBalanceSheetHeadings(CompanyProfileApi companyProfile) {
        boolean isSameYear = isSameYearFiler(companyProfile);
        BalanceSheetHeadings balanceSheetHeadings = new BalanceSheetHeadings();
        balanceSheetHeadings.setPreviousPeriodHeading(getPreviousPeriodHeading(companyProfile, isSameYear));
        balanceSheetHeadings.setCurrentPeriodHeading(getCurrentPeriodHeading(companyProfile, isSameYear));
        return balanceSheetHeadings;
    }

    private String getCurrentPeriodHeading(CompanyProfileApi companyProfile, boolean isSameYear) {
        NextAccountsApi nextAccountsApi = companyProfile.getAccounts().getNextAccounts();
        LocalDate currentPeriodEndOn = nextAccountsApi.getPeriodEndOn();
        LocalDate currentPeriodStartOn = nextAccountsApi.getPeriodStartOn();

        return accountsDatesHelper.generateBalanceSheetHeading(currentPeriodStartOn, currentPeriodEndOn, isSameYear);
    }

    private String getPreviousPeriodHeading(CompanyProfileApi companyProfile, boolean isSameYear) {
        LastAccountsApi lastAccountsApi = companyProfile.getAccounts().getLastAccounts();
        if (isMultipleYearFiler(companyProfile)) {
            LocalDate previousPeriodStartOn = lastAccountsApi.getPeriodStartOn();
            LocalDate previousPeriodEndOn = lastAccountsApi.getPeriodEndOn();

            return accountsDatesHelper.generateBalanceSheetHeading(previousPeriodStartOn, previousPeriodEndOn, isSameYear);
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

    private boolean hasCurrentPeriod(Map<String, String> links) {
        return isCreated(links, SmallFullLinkType.CURRENT_PERIOD);
    }

    private boolean hasPreviousPeriod(Map<String, String> links) {
        return isCreated(links, SmallFullLinkType.PREVIOUS_PERIOD);
    }

    private boolean isCreated(Map<String, String> links, SmallFullLinkType linkType) {
        return links.containsKey(linkType.getLink());
    }
}
