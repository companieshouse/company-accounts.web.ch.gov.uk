package uk.gov.companieshouse.web.accounts.api.impl;

import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.sdk.manager.ApiClientManager;
import uk.gov.companieshouse.web.accounts.api.ApiClientService;

public class ApiClientServiceImpl implements ApiClientService {

    @Override
    public ApiClient getApiClient() {
        return ApiClientManager.getSDK();
    }
}
