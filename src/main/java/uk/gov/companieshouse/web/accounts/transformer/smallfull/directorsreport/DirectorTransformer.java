package uk.gov.companieshouse.web.accounts.transformer.smallfull.directorsreport;

import uk.gov.companieshouse.api.model.accounts.directorsreport.DirectorApi;
import uk.gov.companieshouse.web.accounts.model.directorsreport.DirectorToAdd;

public interface DirectorTransformer {

    DirectorApi getDirectorApi(DirectorToAdd director);
}
