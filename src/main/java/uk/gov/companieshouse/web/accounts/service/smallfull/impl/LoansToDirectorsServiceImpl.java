package uk.gov.companieshouse.web.accounts.service.smallfull.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriTemplate;
import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.error.ApiErrorResponseException;
import uk.gov.companieshouse.api.handler.exception.URIValidationException;

import uk.gov.companieshouse.api.model.accounts.smallfull.loanstodirectors.LoansToDirectorsApi;
import uk.gov.companieshouse.web.accounts.api.ApiClientService;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.service.smallfull.LoansToDirectorsService;
import uk.gov.companieshouse.web.accounts.service.smallfull.SmallFullService;
import uk.gov.companieshouse.web.accounts.validation.helper.ServiceExceptionHandler;

@Service
public class LoansToDirectorsServiceImpl implements LoansToDirectorsService {

    @Autowired
    private ApiClientService apiClientService;

    @Autowired
    private SmallFullService smallFullService;

    @Autowired
    private ServiceExceptionHandler serviceExceptionHandler;

    private static final UriTemplate LOANS_TO_DIRECTORS_URI =
            new UriTemplate("/transactions/{transactionId}/company-accounts/{companyAccountsId}/small-full/notes/loans-to-directors");

    private static final String RESOURCE_NAME = "loans to directors";

    @Override
    public void createLoansToDirectors(String transactionId, String companyAccountsId) throws ServiceException {

        ApiClient apiClient = apiClientService.getApiClient();

        LoansToDirectorsApi loansToDirectors = getLoansToDirectors(apiClient, transactionId, companyAccountsId);


        String uri = LOANS_TO_DIRECTORS_URI.expand(transactionId, companyAccountsId).toString();

        try {
            apiClient.smallFull().loansToDirectors().create(uri, new LoansToDirectorsApi())
                    .execute();
        } catch (ApiErrorResponseException e) {
            serviceExceptionHandler.handleSubmissionException(e, RESOURCE_NAME);
        } catch (URIValidationException e) {
            serviceExceptionHandler.handleURIValidationException(e, RESOURCE_NAME);
        }
    }

    @Override
    public LoansToDirectorsApi getLoansToDirectors(ApiClient apiClient, String transactionId,
            String companyAccountsId) throws ServiceException {

        String uri = LOANS_TO_DIRECTORS_URI.expand(transactionId, companyAccountsId).toString();

        try {
            return apiClient.smallFull().loansToDirectors().get(uri).execute().getData();
        } catch (ApiErrorResponseException e) {
            serviceExceptionHandler.handleRetrievalException(e, RESOURCE_NAME);
        } catch (URIValidationException e) {
            serviceExceptionHandler.handleURIValidationException(e, RESOURCE_NAME);
        }

        return null;
    }

    @Override
    public void deleteLoansToDirectors(String transactionId, String companyAccountsId)
            throws ServiceException {

        ApiClient apiClient = apiClientService.getApiClient();

        if (StringUtils.isNotBlank(
                smallFullService.getSmallFullAccounts(apiClient, transactionId, companyAccountsId)
                        .getLinks().getLoansToDirectors())) {

            String uri = LOANS_TO_DIRECTORS_URI.expand(transactionId, companyAccountsId).toString();

            try {
                apiClient.smallFull().loansToDirectors().delete(uri).execute();
            } catch (ApiErrorResponseException e) {
                serviceExceptionHandler.handleDeletionException(e, RESOURCE_NAME);
            } catch (URIValidationException e) {
                serviceExceptionHandler.handleURIValidationException(e, RESOURCE_NAME);
            }
        }
    }
}
