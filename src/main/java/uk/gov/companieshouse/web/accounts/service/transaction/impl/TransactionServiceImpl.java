package uk.gov.companieshouse.web.accounts.service.transaction.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriTemplate;
import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.error.ApiErrorResponseException;
import uk.gov.companieshouse.api.handler.exception.URIValidationException;
import uk.gov.companieshouse.api.model.transaction.Transaction;
import uk.gov.companieshouse.api.model.transaction.TransactionStatus;
import uk.gov.companieshouse.web.accounts.api.ApiClientService;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.service.transaction.TransactionService;

import java.util.HashMap;
import java.util.Map;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    ApiClientService apiClientService;

    private static final String RESUME_LINK = "resume";

    private static final UriTemplate TRANSACTIONS_URI = new UriTemplate("/transactions/{transactionId}");

    /**
     * {@inheritDoc}
     */
    @Override
    public String createTransaction(String companyNumber) throws ServiceException {

        Transaction transaction = new Transaction();
        transaction.setCompanyNumber(companyNumber);

        transaction.setDescription("Small Full Accounts");

        ApiClient apiClient = apiClientService.getApiClient();

        try {
            transaction = apiClient.transactions().create("/transactions", transaction).execute();
        } catch (ApiErrorResponseException e) {
            
            throw new ServiceException("Error creating transaction", e);
        } catch (URIValidationException e) {

            throw new ServiceException("Invalid URI for transactions resource", e);
        }

        return transaction.getId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void closeTransaction(String transactionId) throws ServiceException {

        String uri = TRANSACTIONS_URI.expand(transactionId).toString();

        try {
            Transaction transaction = apiClientService.getApiClient().transactions().get(uri).execute();
            transaction.setStatus(TransactionStatus.CLOSED);
            apiClientService.getApiClient().transactions().update(uri, transaction).execute();
        } catch (ApiErrorResponseException e) {

            throw new ServiceException("Error closing transaction", e);
        } catch (URIValidationException e) {

            throw new ServiceException("Invalid URI for transactions resource", e);
        }
    }

    @Override
    public void createResumeLink(String companyNumber, String transactionId, String companyAccountsId) throws ServiceException {

        String uri = TRANSACTIONS_URI.expand(transactionId).toString();

        String resumeLink = "/company/" + companyNumber + "/transaction/" + transactionId + "/company-accounts/" + companyAccountsId + "/small-full/resume";

        Map<String, String> links = new HashMap<>();
        links.put(RESUME_LINK, resumeLink);

        Transaction transaction = new Transaction();
        transaction.setLinks(links);

        try {
            apiClientService.getApiClient().transactions().update(uri, transaction).execute();

        } catch (ApiErrorResponseException e) {

            throw new ServiceException("Error updating transaction", e);
        } catch (URIValidationException e) {

            throw new ServiceException("Invalid URI for updating transactions resource", e);
        }
    }
}
