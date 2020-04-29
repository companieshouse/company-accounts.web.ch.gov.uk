package uk.gov.companieshouse.web.accounts.service.smallfull.impl;

import uk.gov.companieshouse.api.model.common.ApiResource;

public interface NoteTransformer<T> {

     String getNoteType();

     ApiResource getApi(Note note);
     T getResource(ApiResource resource);
}
