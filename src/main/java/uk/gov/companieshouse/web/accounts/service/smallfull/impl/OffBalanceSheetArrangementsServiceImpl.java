package uk.gov.companieshouse.web.accounts.service.smallfull.impl;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriTemplate;
import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.error.ApiErrorResponseException;
import uk.gov.companieshouse.api.handler.exception.URIValidationException;
import uk.gov.companieshouse.api.model.ApiResponse;
import uk.gov.companieshouse.api.model.accounts.smallfull.SmallFullApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.offBalanceSheet.OffBalanceSheetApi;
import uk.gov.companieshouse.web.accounts.api.ApiClientService;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.offbalancesheetarrangements.OffBalanceSheetArrangements;
import uk.gov.companieshouse.web.accounts.service.smallfull.OffBalanceSheetArrangementsService;
import uk.gov.companieshouse.web.accounts.service.smallfull.SmallFullService;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.OffBalanceSheetArrangementsTransformer;
import uk.gov.companieshouse.web.accounts.util.ValidationContext;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;
import uk.gov.companieshouse.web.accounts.validation.helper.ServiceExceptionHandler;

@Service
public class OffBalanceSheetArrangementsServiceImpl implements OffBalanceSheetArrangementsService {

    @Autowired
    private ApiClientService apiClientService;

    @Autowired
    private SmallFullService smallFullService;

    @Autowired
    private OffBalanceSheetArrangementsTransformer transformer;

    @Autowired
    private ServiceExceptionHandler serviceExceptionHandler;

    @Autowired
    private ValidationContext validationContext;

    private static final UriTemplate OFF_BALANCE_SHEET_ARRANGEMENTS_URI =
            new UriTemplate(
                    "/transactions/{transactionId}/company-accounts/{companyAccountsId}/small-full/notes/off-balance-sheet-arrangements");

    private static final String RESOURCE_NAME = "off balance sheet arrangements";

    @Override
    public OffBalanceSheetArrangements getOffBalanceSheetArrangements(String transactionId,
            String companyAccountsId) throws ServiceException {

        ApiClient apiClient = apiClientService.getApiClient();

        String uri = OFF_BALANCE_SHEET_ARRANGEMENTS_URI.expand(transactionId, companyAccountsId).toString();

        OffBalanceSheetApi offBalanceSheetApi = null;
        try {
            offBalanceSheetApi = apiClient.smallFull().offBalanceSheet().get(uri).execute().getData();
        } catch (ApiErrorResponseException e) {
            serviceExceptionHandler.handleRetrievalException(e, RESOURCE_NAME);
        } catch (URIValidationException e) {
            serviceExceptionHandler.handleURIValidationException(e, RESOURCE_NAME);
        }

        return transformer.getOffBalanceSheetArrangements(offBalanceSheetApi);
    }

    @Override
    public List<ValidationError> submitOffBalanceSheetArrangements(String transactionId,
            String companyAccountsId, OffBalanceSheetArrangements arrangements)
            throws ServiceException {

        ApiClient apiClient = apiClientService.getApiClient();

        String uri = OFF_BALANCE_SHEET_ARRANGEMENTS_URI.expand(transactionId, companyAccountsId).toString();

        SmallFullApi smallFullApi = smallFullService
                .getSmallFullAccounts(apiClient, transactionId, companyAccountsId);

        OffBalanceSheetApi offBalanceSheetApi = transformer.getOffBalanceSheetArrangementsApi(arrangements);

        boolean offBalanceSheetArrangementsResourceExists = StringUtils.isNotBlank(smallFullApi.getLinks().getOffBalanceSheetArrangements());

        try {
            ApiResponse apiResponse;
            if (!offBalanceSheetArrangementsResourceExists) {
                apiResponse = apiClient.smallFull().offBalanceSheet().create(uri, offBalanceSheetApi).execute();
            } else {
                apiResponse = apiClient.smallFull().offBalanceSheet().update(uri, offBalanceSheetApi).execute();
            }

            if (apiResponse.hasErrors()) {
                return validationContext.getValidationErrors(apiResponse.getErrors());
            }
        } catch (URIValidationException e) {
            serviceExceptionHandler.handleURIValidationException(e, RESOURCE_NAME);
        } catch (ApiErrorResponseException e) {
            serviceExceptionHandler.handleSubmissionException(e, RESOURCE_NAME);
        }

        return new ArrayList<>();
    }

    @Override
    public void deleteOffBalanceSheetArrangements(String transactionId, String companyAccountsId)
            throws ServiceException {

        ApiClient apiClient = apiClientService.getApiClient();

        SmallFullApi smallFullApi = smallFullService.getSmallFullAccounts(apiClient, transactionId, companyAccountsId);

        if (StringUtils.isNotBlank(smallFullApi.getLinks().getOffBalanceSheetArrangements())) {

            String uri = OFF_BALANCE_SHEET_ARRANGEMENTS_URI.expand(transactionId, companyAccountsId)
                    .toString();

            try {
                apiClient.smallFull().offBalanceSheet().delete(uri).execute();
            } catch (URIValidationException e) {
                serviceExceptionHandler.handleURIValidationException(e, RESOURCE_NAME);
            } catch (ApiErrorResponseException e) {
                serviceExceptionHandler.handleDeletionException(e, RESOURCE_NAME);
            }
        }
    }
}
