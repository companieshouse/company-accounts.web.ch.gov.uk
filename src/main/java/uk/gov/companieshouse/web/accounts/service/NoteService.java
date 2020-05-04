package uk.gov.companieshouse.web.accounts.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.error.ApiErrorResponseException;
import uk.gov.companieshouse.api.handler.exception.URIValidationException;
import uk.gov.companieshouse.api.model.ApiResponse;
import uk.gov.companieshouse.api.model.common.ApiResource;
import uk.gov.companieshouse.web.accounts.api.ApiClientService;
import uk.gov.companieshouse.web.accounts.enumeration.NoteType;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.Note;
import uk.gov.companieshouse.web.accounts.temp.NoteHelper;
import uk.gov.companieshouse.web.accounts.temp.NoteHelperFactory;
import uk.gov.companieshouse.web.accounts.transformer.NoteTransformerFactory;
import uk.gov.companieshouse.web.accounts.util.ValidationContext;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

@Service
public class NoteService<N extends Note> {

    @Autowired
    private ApiClientService apiClientService;

    @Autowired
    private NoteHelperFactory<ApiResource> noteHelperFactory;

    @Autowired
    private NoteTransformerFactory<N, ApiResource> noteTransformerFactory;

    @Autowired
    private ValidationContext validationContext;

    /**
     * Get a resource
     * @param transactionId the transaction identifier
     * @param companyAccountsId the company accounts identifier
     * @param noteType the type of note to fetch
     * @return an optional containing resource data, or an empty optional if no data is found
     * @throws ServiceException
     */
    public Optional<N> get(String transactionId, String companyAccountsId, NoteType noteType) throws ServiceException {

        ApiClient apiClient = apiClientService.getApiClient();

        NoteHelper<ApiResource> noteHelper = noteHelperFactory.getNoteHelper(noteType);

        String uri = noteHelper.getUri(transactionId, companyAccountsId);

        try {
            ApiResource apiResource = noteHelper.get(apiClient, uri).execute().getData();
            return Optional.of(noteTransformerFactory.getNoteTransformer(noteType).toWeb(apiResource));

        } catch (ApiErrorResponseException e) {
            if (e.getStatusCode() == 404) {
                return Optional.empty();
            }
            throw new ServiceException("Error fetching resource", e);
        } catch (URIValidationException e) {
            throw new ServiceException("Invalid URI", e);
        }
    }

    public List<ValidationError> submit(String transactionId, String companyAccountsId, N note, NoteType noteType) throws ServiceException {

        ApiClient apiClient = apiClientService.getApiClient();

        NoteHelper<ApiResource> noteHelper = noteHelperFactory.getNoteHelper(noteType);

        String uri = noteHelper.getUri(transactionId, companyAccountsId);

        ApiResource apiResource = noteTransformerFactory.getNoteTransformer(noteType).toApi(note);

        boolean parentResourceExists = noteHelper.parentResourceExists(apiClient, transactionId, companyAccountsId);

        try {
            ApiResponse apiResponse;
            if (parentResourceExists) {
                apiResponse = noteHelper.update(apiClient, uri, apiResource).execute();
            } else {
                apiResponse = noteHelper.create(apiClient, uri, apiResource).execute();
            }
            if (apiResponse.hasErrors()) {
                return validationContext.getValidationErrors(apiResponse.getErrors());
            }
        } catch (ApiErrorResponseException e) {
            throw new ServiceException("Error submitting resource", e);
        } catch (URIValidationException e) {
            throw new ServiceException("Invalid URI", e);
        }

        return new ArrayList<>();
    }

    public void delete(String transactionId, String companyAccountsId, NoteType noteType) throws ServiceException {

        ApiClient apiClient = apiClientService.getApiClient();

        NoteHelper<ApiResource> noteHelper = noteHelperFactory.getNoteHelper(noteType);

        String uri = noteHelper.getUri(transactionId, companyAccountsId);

        boolean parentResourceExists = noteHelper.parentResourceExists(apiClient, transactionId, companyAccountsId);

        if (parentResourceExists) {

            try {
                noteHelper.delete(apiClient, uri).execute();
            } catch (ApiErrorResponseException e) {
                throw new ServiceException("Error deleting resource", e);
            } catch (URIValidationException e) {
                throw new ServiceException("Invalid URI", e);
            }
        }
    }
}
