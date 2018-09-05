package uk.gov.companieshouse.web.accounts.api;

import uk.gov.companieshouse.api.ApiClient;

/**
 * The {@code ApiClientService} interface provides an abstraction that can be
 * used when testing {@code SessionHandler} static methods, without imposing
 * the use of a test framework that supports mocking of static methods.
 */
public interface ApiClientService {

    ApiClient getApiClient();
}
