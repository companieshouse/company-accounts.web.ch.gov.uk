package uk.gov.companieshouse.web.accounts.service.smallfull.impl;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriTemplate;
import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.error.ApiError;
import uk.gov.companieshouse.api.error.ApiErrorResponseException;
import uk.gov.companieshouse.api.handler.exception.URIValidationException;
import uk.gov.companieshouse.api.model.ApiResponse;
import uk.gov.companieshouse.api.model.accounts.profitandloss.ProfitAndLossApi;
import uk.gov.companieshouse.api.model.company.CompanyProfileApi;
import uk.gov.companieshouse.web.accounts.api.ApiClientService;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.profitandloss.ProfitAndLoss;
import uk.gov.companieshouse.web.accounts.service.company.CompanyService;
import uk.gov.companieshouse.web.accounts.service.smallfull.CurrentPeriodService;
import uk.gov.companieshouse.web.accounts.service.smallfull.PreviousPeriodService;
import uk.gov.companieshouse.web.accounts.service.smallfull.ProfitAndLossService;
import uk.gov.companieshouse.web.accounts.transformer.profitandloss.ProfitAndLossTransformer;
import uk.gov.companieshouse.web.accounts.util.ValidationContext;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;
import uk.gov.companieshouse.web.accounts.validation.helper.ServiceExceptionHandler;

@Service
public class ProfitAndLossServiceImpl implements ProfitAndLossService {

    private static final UriTemplate CURRENT_PERIOD_URI =
            new UriTemplate("/transactions/{transactionId}/company-accounts/{companyAccountsId}/small-full/current-period/profit-and-loss");

    private static final UriTemplate PREVIOUS_PERIOD_URI =
            new UriTemplate("/transactions/{transactionId}/company-accounts/{companyAccountsId}/small-full/previous-period/profit-and-loss");

    @Autowired
    private ApiClientService apiClientService;

    @Autowired
    private ServiceExceptionHandler serviceExceptionHandler;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private CurrentPeriodService currentPeriodService;

    @Autowired
    private PreviousPeriodService previousPeriodService;

    @Autowired
    private ProfitAndLossTransformer profitAndLossTransformer;

    @Autowired
    private ValidationContext validationContext;

    private static final String CURRENT_PERIOD_RESOURCE = "current period profit and loss";
    private static final String PREVIOUS_PERIOD_RESOURCE = "previous period profit and loss";

    private static final String PERIOD_AGNOSTIC_JSON_PATH_PREFIX = "$";
    private static final String CURRENT_PERIOD_JSON_PATH_PREFIX = "$.current_period";
    private static final String PREVIOUS_PERIOD_JSON_PATH_PREFIX = "$.previous_period";

    @Override
    public ProfitAndLoss getProfitAndLoss(String transactionId, String companyAccountsId,
            String companyNumber) throws ServiceException {

        ApiClient apiClient = apiClientService.getApiClient();

        ProfitAndLossApi currentPeriod =
                getProfitAndLoss(apiClient, transactionId, companyAccountsId, true);

        ProfitAndLossApi previousPeriod = null;

        CompanyProfileApi companyProfile = companyService.getCompanyProfile(companyNumber);
        if (companyService.isMultiYearFiler(companyProfile)) {
            previousPeriod =
                    getProfitAndLoss(apiClient, transactionId, companyAccountsId, false);
        }

        ProfitAndLoss profitAndLoss =
                profitAndLossTransformer.getProfitAndLoss(currentPeriod, previousPeriod);

        profitAndLoss.setBalanceSheetHeadings(companyService.getBalanceSheetHeadings(companyProfile));

        return profitAndLoss;
    }

    @Override
    public List<ValidationError> submitProfitAndLoss(String transactionId, String companyAccountsId,
            String companyNumber, ProfitAndLoss profitAndLoss) throws ServiceException {

        ApiClient apiClient = apiClientService.getApiClient();

        List<ValidationError> validationErrors = new ArrayList<>();

        ProfitAndLossApi currentPeriodProfitAndLoss =
                profitAndLossTransformer.getCurrentPeriodProfitAndLoss(profitAndLoss);

        submitCurrentPeriodProfitAndLoss(apiClient, transactionId, companyAccountsId,
                currentPeriodProfitAndLoss, validationErrors);

        CompanyProfileApi companyProfile = companyService.getCompanyProfile(companyNumber);
        if (companyService.isMultiYearFiler(companyProfile)) {

            ProfitAndLossApi previousPeriodProfitAndLoss =
                    profitAndLossTransformer.getPreviousPeriodProfitAndLoss(profitAndLoss);

            submitPreviousPeriodProfitAndLoss(apiClient, transactionId, companyAccountsId,
                    previousPeriodProfitAndLoss, validationErrors);
        }

        return validationErrors;
    }

    @Override
    public void deleteProfitAndLoss(String transactionId, String companyAccountsId,
            String companyNumber) throws ServiceException {

        ApiClient apiClient = apiClientService.getApiClient();

        if (StringUtils.isNotBlank(
                currentPeriodService.getCurrentPeriod(
                        apiClient, transactionId, companyAccountsId).getLinks()
                                .getProfitAndLoss())) {

            try {
                apiClient.smallFull().currentPeriodProfitAndLoss()
                        .delete(CURRENT_PERIOD_URI.expand(transactionId, companyAccountsId)
                                .toString()).execute();
            } catch (ApiErrorResponseException e) {
                serviceExceptionHandler.handleDeletionException(e, CURRENT_PERIOD_RESOURCE);
            } catch (URIValidationException e) {
                serviceExceptionHandler.handleURIValidationException(e, CURRENT_PERIOD_RESOURCE);
            }
        }

        if (StringUtils.isNotBlank(
                previousPeriodService.getPreviousPeriod(
                        apiClient, transactionId, companyAccountsId).getLinks()
                                .getProfitAndLoss())){

            try {
                apiClient.smallFull().previousPeriodProfitAndLoss()
                        .delete(PREVIOUS_PERIOD_URI.expand(transactionId, companyAccountsId)
                                .toString()).execute();
            } catch (ApiErrorResponseException e) {
                serviceExceptionHandler.handleDeletionException(e, PREVIOUS_PERIOD_RESOURCE);
            } catch (URIValidationException e) {
                serviceExceptionHandler.handleURIValidationException(e, PREVIOUS_PERIOD_RESOURCE);
            }
        }
    }

    private ProfitAndLossApi getProfitAndLoss(ApiClient apiClient,
                                              String transactionId,
                                              String companyAccountsId,
                                              boolean isCurrentPeriod) throws ServiceException {

        String uri = (isCurrentPeriod ? CURRENT_PERIOD_URI : PREVIOUS_PERIOD_URI).expand(transactionId, companyAccountsId).toString();

        try {
            if (isCurrentPeriod) {
                return apiClient.smallFull().currentPeriodProfitAndLoss().get(uri).execute().getData();
            } else {
                return apiClient.smallFull().previousPeriodProfitAndLoss().get(uri).execute().getData();
            }
        } catch (ApiErrorResponseException e) {
            serviceExceptionHandler.handleRetrievalException(e,
                    isCurrentPeriod ? CURRENT_PERIOD_RESOURCE : PREVIOUS_PERIOD_RESOURCE);
        } catch (URIValidationException e) {
            serviceExceptionHandler.handleURIValidationException(e,
                    isCurrentPeriod ? CURRENT_PERIOD_RESOURCE : PREVIOUS_PERIOD_RESOURCE);
        }

        return null;
    }

    private void submitCurrentPeriodProfitAndLoss(ApiClient apiClient,
                                     String transactionId,
                                     String companyAccountsId,
                                     ProfitAndLossApi profitAndLoss,
                                     List<ValidationError> validationErrors) throws ServiceException {

        String uri = CURRENT_PERIOD_URI.expand(transactionId, companyAccountsId).toString();

        boolean resourceExists = StringUtils.isNotEmpty(
                currentPeriodService.getCurrentPeriod(apiClient, transactionId, companyAccountsId)
                        .getLinks().getProfitAndLoss());

        try {
            ApiResponse apiResponse;
                if (resourceExists) {
                    apiResponse = apiClient.smallFull().currentPeriodProfitAndLoss()
                            .update(uri, profitAndLoss).execute();
                } else {
                    apiResponse = apiClient.smallFull().currentPeriodProfitAndLoss()
                            .create(uri, profitAndLoss).execute();
                }


            if (apiResponse.hasErrors()) {
                List<ApiError> apiErrors = apiResponse.getErrors();
                apiErrors.forEach(apiError ->
                        apiError.setLocation(
                                apiError.getLocation()
                                        .replace(PERIOD_AGNOSTIC_JSON_PATH_PREFIX,
                                                CURRENT_PERIOD_JSON_PATH_PREFIX)));
                validationErrors.addAll(validationContext.getValidationErrors(apiErrors));
            }
        } catch (ApiErrorResponseException e) {
            serviceExceptionHandler.handleSubmissionException(e, CURRENT_PERIOD_RESOURCE);
        } catch (URIValidationException e) {
            serviceExceptionHandler.handleURIValidationException(e, CURRENT_PERIOD_RESOURCE);
        }
    }

    private void submitPreviousPeriodProfitAndLoss(ApiClient apiClient,
                                                   String transactionId,
                                                   String companyAccountsId,
                                                   ProfitAndLossApi profitAndLoss,
                                                   List<ValidationError> validationErrors) throws ServiceException {

        String uri = PREVIOUS_PERIOD_URI.expand(transactionId, companyAccountsId).toString();

        boolean resourceExists = StringUtils.isNotEmpty(
                previousPeriodService.getPreviousPeriod(apiClient, transactionId, companyAccountsId)
                        .getLinks().getProfitAndLoss());

        try {
            ApiResponse apiResponse;
            if (resourceExists) {
                apiResponse = apiClient.smallFull().previousPeriodProfitAndLoss()
                        .update(uri, profitAndLoss).execute();
            } else {
                apiResponse = apiClient.smallFull().previousPeriodProfitAndLoss()
                        .create(uri, profitAndLoss).execute();
            }


            if (apiResponse.hasErrors()) {
                List<ApiError> apiErrors = apiResponse.getErrors();
                apiErrors.forEach(apiError ->
                        apiError.setLocation(
                                apiError.getLocation()
                                        .replace(PERIOD_AGNOSTIC_JSON_PATH_PREFIX,
                                                PREVIOUS_PERIOD_JSON_PATH_PREFIX)));
                validationErrors.addAll(validationContext.getValidationErrors(apiErrors));
            }
        } catch (ApiErrorResponseException e) {
            serviceExceptionHandler.handleSubmissionException(e, PREVIOUS_PERIOD_RESOURCE);
        } catch (URIValidationException e) {
            serviceExceptionHandler.handleURIValidationException(e, PREVIOUS_PERIOD_RESOURCE);
        }
    }
}
