package uk.gov.companieshouse.web.accounts.service.notehandler.dates;

import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.web.accounts.enumeration.NoteType;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.Note;

public interface DateHandler<N extends Note> {

    void addDates(ApiClient apiClient, String transactionId, String companyAccountsId, N note) throws ServiceException;

    NoteType getNoteType();
}
