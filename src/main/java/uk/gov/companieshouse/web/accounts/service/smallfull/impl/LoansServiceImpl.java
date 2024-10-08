package uk.gov.companieshouse.web.accounts.service.smallfull.impl;

import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriTemplate;
import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.error.ApiErrorResponseException;
import uk.gov.companieshouse.api.handler.exception.URIValidationException;
import uk.gov.companieshouse.api.model.ApiResponse;
import uk.gov.companieshouse.api.model.accounts.smallfull.loanstodirectors.LoanApi;
import uk.gov.companieshouse.web.accounts.api.ApiClientService;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.loanstodirectors.AddOrRemoveLoans;
import uk.gov.companieshouse.web.accounts.model.loanstodirectors.Loan;
import uk.gov.companieshouse.web.accounts.model.loanstodirectors.LoanToAdd;
import uk.gov.companieshouse.web.accounts.service.smallfull.LoanService;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.loanstodirectors.LoanTransformer;
import uk.gov.companieshouse.web.accounts.util.ValidationContext;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;
import uk.gov.companieshouse.web.accounts.validation.helper.ServiceExceptionHandler;
import uk.gov.companieshouse.web.accounts.validation.smallfull.LoanValidator;

@Service
public class LoansServiceImpl implements LoanService {

    private static final String PREFER_NOT_TO_SAY = "Prefer not to say";

    private static final UriTemplate LOANS_URI =
            new UriTemplate("/transactions/{transactionId}/company-accounts/{companyAccountsId}/small-full/notes/loans-to-directors/loans");

    private static final UriTemplate LOAN_URI_WITH_ID =
            new UriTemplate("/transactions/{transactionId}/company-accounts/{companyAccountsId}/small-full/notes/loans-to-directors/loans/{loanId}");

    private static final String RESOURCE_NAME = "loans";

    @Autowired
    private ApiClientService apiClientService;

    @Autowired
    private LoanTransformer loanTransformer;

    @Autowired
    private ServiceExceptionHandler serviceExceptionHandler;

    @Autowired
    private ValidationContext validationContext;
 
    @Autowired
    private LoanValidator loanValidator;

    @Override
    public Loan[] getAllLoans(String transactionId, String companyAccountsId) throws ServiceException {

        ApiClient apiClient = apiClientService.getApiClient();

        String uri = LOANS_URI.expand(transactionId, companyAccountsId).toString();

        try {
            LoanApi[] loans = Arrays.stream(apiClient.smallFull().loansToDirectors().loans().getAll(uri).execute().getData())
                    .map(loan -> {
                        if(StringUtils.isBlank(loan.getDirectorName())) {
                            loan.setDirectorName("Not provided");
                        }
                        return loan;
                    }).toArray(LoanApi[]::new);

            return loanTransformer.getAllLoans(loans);
        } catch (ApiErrorResponseException e) {
            serviceExceptionHandler.handleRetrievalException(e, RESOURCE_NAME);
        } catch (URIValidationException e) {
            serviceExceptionHandler.handleURIValidationException(e, RESOURCE_NAME);
        }

        return new Loan[0];

    }

    @Override
    public List<ValidationError> createLoan(String transactionId, String companyAccountsId, AddOrRemoveLoans addOrRemoveLoans) throws ServiceException {

        List<ValidationError> validationErrors;

        boolean directorReportPresent = addOrRemoveLoans.getValidDirectorNames() != null && !addOrRemoveLoans.getValidDirectorNames().isEmpty();

        validationErrors = loanValidator.validateLoanToAdd(addOrRemoveLoans.getLoanToAdd(), addOrRemoveLoans.getIsMultiYearFiler(), directorReportPresent);

        if(!validationErrors.isEmpty()) {
            return validationErrors;
        }

        String directorName = addOrRemoveLoans.getLoanToAdd().getDirectorName();

        if(StringUtils.isBlank(directorName) || directorName.equals(PREFER_NOT_TO_SAY)) {
            addOrRemoveLoans.getLoanToAdd().setDirectorName(null);
        }

        ApiClient apiClient = apiClientService.getApiClient();

        String uri = LOANS_URI.expand(transactionId, companyAccountsId).toString();

        LoanApi loanApi = loanTransformer.getLoanApi(addOrRemoveLoans.getLoanToAdd());

        try {
            ApiResponse<LoanApi> apiResponse = apiClient.smallFull().loansToDirectors().loans().create(uri, loanApi).execute();

            if (apiResponse.hasErrors()) {
                validationErrors.addAll(validationContext.getValidationErrors(apiResponse.getErrors()));
            }
        } catch (ApiErrorResponseException e) {
            serviceExceptionHandler.handleSubmissionException(e, RESOURCE_NAME);
        } catch (URIValidationException e) {
            serviceExceptionHandler.handleURIValidationException(e, RESOURCE_NAME);
        }

        return validationErrors;
    }

    @Override
    public void deleteLoan(String transactionId, String companyAccountsId, String loanId) throws ServiceException {

        ApiClient apiClient = apiClientService.getApiClient();

        String uri = LOAN_URI_WITH_ID.expand(transactionId, companyAccountsId, loanId).toString();

        try {
            apiClient.smallFull().loansToDirectors().loans().delete(uri).execute();
        } catch (ApiErrorResponseException e) {
            serviceExceptionHandler.handleDeletionException(e, RESOURCE_NAME);
        } catch (URIValidationException e) {
            serviceExceptionHandler.handleURIValidationException(e, RESOURCE_NAME);
        }
    }

    @Override
    public List<ValidationError> submitAddOrRemoveLoans(String transactionId, String companyAccountsId, AddOrRemoveLoans addOrRemoveLoans) throws ServiceException {

        LoanToAdd loanToAdd = addOrRemoveLoans.getLoanToAdd();

        List<ValidationError> validationErrors;

        if(addOrRemoveLoans.getValidDirectorNames() != null && addOrRemoveLoans.getValidDirectorNames().size() == 1) {
            validationErrors = loanValidator.validateAtLeastOneLoan(addOrRemoveLoans, true);
        } else {
            validationErrors = loanValidator.validateAtLeastOneLoan(addOrRemoveLoans, false);

        }

        boolean isEmptyResource;
        if (validationErrors.isEmpty()) {
            if(addOrRemoveLoans.getValidDirectorNames() != null && addOrRemoveLoans.getValidDirectorNames().size() == 1) {
                isEmptyResource = loanValidator.isSingleDirectorEmptyResource(loanToAdd, addOrRemoveLoans.getIsMultiYearFiler());
            } else {
                isEmptyResource = loanValidator.isEmptyResource(loanToAdd, addOrRemoveLoans.getIsMultiYearFiler());
            }

            if (!isEmptyResource) {
                validationErrors = createLoan(transactionId, companyAccountsId, addOrRemoveLoans);
            }
        }

        return validationErrors;
    }
}
