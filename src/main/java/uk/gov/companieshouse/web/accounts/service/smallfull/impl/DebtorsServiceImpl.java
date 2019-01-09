package uk.gov.companieshouse.web.accounts.service.smallfull.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriTemplate;
import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.error.ApiErrorResponseException;
import uk.gov.companieshouse.api.handler.exception.URIValidationException;
import uk.gov.companieshouse.api.model.accounts.smallfull.Debtors.DebtorsApi;
import uk.gov.companieshouse.web.accounts.api.ApiClientService;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.Debtors;
import uk.gov.companieshouse.web.accounts.service.smallfull.DebtorsService;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.DebtorsTransformer;
import uk.gov.companieshouse.web.accounts.util.ValidationContext;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

import java.util.ArrayList;
import java.util.List;

@Service
public class DebtorsServiceImpl implements DebtorsService {

    @Autowired
    private ApiClientService apiClientService;

    @Autowired
    private ValidationContext validationContext;

    @Autowired
    private DebtorsTransformer transformer;

    private static final UriTemplate DEBTORS_URI =
        new UriTemplate("/transactions/{transactionId}/company-accounts/{companyAccountsId}/small-full/notes/debtors");

    private static final String INVALID_URI_MESSAGE = "Invalid URI for debtors resource";

    @Override
    public Debtors getDebtors(String transactionId, String companyAccountsId) throws ServiceException {
        DebtorsApi debtorsApi = getDebtorsApi(transactionId, companyAccountsId);
        return transformer.getDebtors(debtorsApi);
    }

    @Override
    public List<ValidationError> submitDebtors(String transactionId, String companyAccountsId,
            Debtors debtors, String companyNumber) throws ServiceException {

        ApiClient apiClient = apiClientService.getApiClient();
        String uri = DEBTORS_URI.expand(transactionId, companyAccountsId).toString();
        DebtorsApi debtorsApi = getDebtorsApi(transactionId, companyAccountsId);
        if (debtorsApi == null) {
            debtorsApi = new DebtorsApi();
            transformer.setDebtors(debtors, debtorsApi);
        }

        try {
            apiClient.smallFull().debtors().create(uri, debtorsApi).execute();
        } catch (URIValidationException e) {
            throw new ServiceException(INVALID_URI_MESSAGE, e);
        } catch (ApiErrorResponseException e) {
            if (e.getStatusCode() == HttpStatus.BAD_REQUEST.value()) {
                List<ValidationError> validationErrors = validationContext.getValidationErrors(e);
                if (validationErrors.isEmpty()) {
                    throw new ServiceException("Bad request when creating debtors resource", e);
                }
                return validationErrors;
            }
            throw new ServiceException("Error creating debtors resource", e);
        }

        return new ArrayList<>();
    }


    private DebtorsApi getDebtorsApi(String transactionId, String companyAccountsId) throws ServiceException {
        ApiClient apiClient = apiClientService.getApiClient();

        String uri = DEBTORS_URI.expand(transactionId, companyAccountsId).toString();

        try {
            return apiClient.smallFull().debtors().get(uri).execute();

        } catch (ApiErrorResponseException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND.value()) {
                return null;
            }
            throw new ServiceException("Error when retrieving debtors", e);
        } catch (URIValidationException e) {
            throw new ServiceException("Invalid URI for debtors resource", e);
        }
    }
}
