package uk.gov.companieshouse.web.accounts.transformer.smallfull.directorsreport.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.api.model.accounts.directorsreport.SecretaryApi;
import uk.gov.companieshouse.web.accounts.model.directorsreport.AddOrRemoveDirectors;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SecretaryTransformerImplTest {

    private static final String NAME = "name";
    @InjectMocks
    private SecretaryTransformerImpl secretaryTransformer;

    @Test
    @DisplayName("Get secretary API")
    void getSecretaryApi() {

        AddOrRemoveDirectors addOrRemoveDirectors = new AddOrRemoveDirectors();
        addOrRemoveDirectors.setSecretary(NAME);

        SecretaryApi secretaryApi = secretaryTransformer.getSecretaryApi(addOrRemoveDirectors);

        assertNotNull(secretaryApi);
        assertEquals(NAME, secretaryApi.getName());

    }
}
