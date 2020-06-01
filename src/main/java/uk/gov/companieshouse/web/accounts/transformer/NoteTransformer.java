package uk.gov.companieshouse.web.accounts.transformer;

import uk.gov.companieshouse.api.model.common.ApiResource;
import uk.gov.companieshouse.web.accounts.enumeration.NoteType;
import uk.gov.companieshouse.web.accounts.model.Note;

public interface NoteTransformer<W extends Note, A extends ApiResource> {

    W toWeb(A apiResource);

    A toApi(W webResource);

    NoteType getNoteType();
}
