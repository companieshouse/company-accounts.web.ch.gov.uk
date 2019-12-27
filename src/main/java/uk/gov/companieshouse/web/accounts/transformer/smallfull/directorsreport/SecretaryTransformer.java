package uk.gov.companieshouse.web.accounts.transformer.smallfull.directorsreport;

import uk.gov.companieshouse.api.model.accounts.directorsreport.SecretaryApi;
import uk.gov.companieshouse.web.accounts.model.directorsreport.AddOrRemoveDirectors;

public interface SecretaryTransformer {

    SecretaryApi getSecretaryApi(AddOrRemoveDirectors addOrRemoveDirectors);
}
