package uk.gov.companieshouse.web.accounts.transformer.smallfull.directorsreport.impl;

import uk.gov.companieshouse.api.model.accounts.directorsreport.SecretaryApi;
import uk.gov.companieshouse.web.accounts.model.directorsreport.AddOrRemoveDirectors;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.directorsreport.SecretaryTransformer;

public class SecretaryTranformerImpl implements SecretaryTransformer {

    @Override
    public SecretaryApi getSecretaryApi(AddOrRemoveDirectors addOrRemoveDirectors) {

        SecretaryApi secretaryApi = new SecretaryApi();
        secretaryApi.setName(addOrRemoveDirectors.getSecretary());

        return secretaryApi;

    }
}
