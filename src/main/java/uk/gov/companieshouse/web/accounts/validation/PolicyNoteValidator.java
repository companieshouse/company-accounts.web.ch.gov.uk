package uk.gov.companieshouse.web.accounts.validation;

import uk.gov.companieshouse.web.accounts.enumeration.NoteType;
import uk.gov.companieshouse.web.accounts.model.Note;

import java.util.List;

public interface PolicyNoteValidator<N extends Note> {

     List<ValidationError> validateNote(N noteType);
}
