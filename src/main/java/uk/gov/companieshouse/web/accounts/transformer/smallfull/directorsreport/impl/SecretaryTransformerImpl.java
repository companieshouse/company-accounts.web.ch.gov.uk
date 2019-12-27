package uk.gov.companieshouse.web.accounts.transformer.smallfull.directorsreport.impl;

import org.springframework.stereotype.Component;
import uk.gov.companieshouse.api.model.accounts.directorsreport.SecretaryApi;
import uk.gov.companieshouse.web.accounts.model.directorsreport.AddOrRemoveDirectors;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.directorsreport.SecretaryTransformer;

@Component
public class SecretaryTransformerImpl implements SecretaryTransformer {

    @Override
    public SecretaryApi getSecretaryApi(AddOrRemoveDirectors addOrRemoveDirectors) {

        SecretaryApi secretaryApi = new SecretaryApi();
        secretaryApi.setName(addOrRemoveDirectors.getSecretary());

        return secretaryApi;

    }
}
