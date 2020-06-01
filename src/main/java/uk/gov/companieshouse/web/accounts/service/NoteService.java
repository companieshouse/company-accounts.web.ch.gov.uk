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
import uk.gov.companieshouse.web.accounts.service.notehandler.NoteResourceHandler;
import uk.gov.companieshouse.web.accounts.service.notehandler.NoteResourceHandlerFactory;
import uk.gov.companieshouse.web.accounts.transformer.NoteTransformerFactory;
import uk.gov.companieshouse.web.accounts.util.ValidationContext;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

@Service
public class NoteService<N extends Note> {

    @Autowired
    private ApiClientService apiClientService;

    @Autowired
    private NoteResourceHandlerFactory<ApiResource> noteResourceHandlerFactory;

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
     * @throws ServiceException on retrieval error
     */
    public Optional<N> get(String transactionId, String companyAccountsId, NoteType noteType) throws ServiceException {

        ApiClient apiClient = apiClientService.getApiClient();

        NoteResourceHandler<ApiResource> noteResourceHandler = noteResourceHandlerFactory.getNoteResourceHandler(noteType);

        String uri = noteResourceHandler.getUri(transactionId, companyAccountsId);

        try {
            ApiResource apiResource = noteResourceHandler.get(apiClient, uri).execute().getData();
            return Optional.of(noteTransformerFactory.getNoteTransformer(noteType).toWeb(apiResource));

        } catch (ApiErrorResponseException e) {
            if (e.getStatusCode() == 404) {
                return Optional.empty();
            }
            throw new ServiceException("Error fetching resource of type " + noteType.toString(), e);
        } catch (URIValidationException e) {
            throw new ServiceException("Invalid URI for resource of type " + noteType.toString(), e);
        }
    }

    /**
     * Submit a resource
     * @param transactionId the transaction identifier
     * @param companyAccountsId the company accounts identifier
     * @param note the note to submit
     * @param noteType the type of note to fetch
     * @return a {@link List} of {@link ValidationError}s, or an empty list if none are present
     * @throws ServiceException on submission error
     */
    public List<ValidationError> submit(String transactionId, String companyAccountsId, N note, NoteType noteType) throws ServiceException {

        ApiClient apiClient = apiClientService.getApiClient();

        NoteResourceHandler<ApiResource> noteResourceHandler = noteResourceHandlerFactory.getNoteResourceHandler(noteType);

        String uri = noteResourceHandler.getUri(transactionId, companyAccountsId);

        ApiResource apiResource = noteTransformerFactory.getNoteTransformer(noteType).toApi(note);

        boolean parentResourceExists = noteResourceHandler
                .parentResourceExists(apiClient, transactionId, companyAccountsId);

        try {
            ApiResponse apiResponse;
            if (parentResourceExists) {
                apiResponse = noteResourceHandler.update(apiClient, uri, apiResource).execute();
            } else {
                apiResponse = noteResourceHandler.create(apiClient, uri, apiResource).execute();
            }
            if (apiResponse.hasErrors()) {
                return validationContext.getValidationErrors(apiResponse.getErrors());
            }
        } catch (ApiErrorResponseException e) {
            throw new ServiceException("Error submitting resource of type " + noteType.toString(), e);
        } catch (URIValidationException e) {
            throw new ServiceException("Invalid URI for resource of type " + noteType.toString(), e);
        }

        return new ArrayList<>();
    }

    /**
     * Delete a resource if it exists
     * @param transactionId the transaction identifier
     * @param companyAccountsId the company accounts identifier
     * @param noteType the type of note to fetch
     * @throws ServiceException on deletion error
     */
    public void delete(String transactionId, String companyAccountsId, NoteType noteType) throws ServiceException {

        ApiClient apiClient = apiClientService.getApiClient();

        NoteResourceHandler<ApiResource> noteResourceHandler = noteResourceHandlerFactory.getNoteResourceHandler(noteType);

        String uri = noteResourceHandler.getUri(transactionId, companyAccountsId);

        boolean parentResourceExists = noteResourceHandler
                .parentResourceExists(apiClient, transactionId, companyAccountsId);

        if (parentResourceExists) {

            try {
                noteResourceHandler.delete(apiClient, uri).execute();
            } catch (ApiErrorResponseException e) {
                throw new ServiceException("Error deleting resource of type " + noteType.toString(), e);
            } catch (URIValidationException e) {
                throw new ServiceException("Invalid URI for resource of type " + noteType.toString(), e);
            }
        }
    }
}
