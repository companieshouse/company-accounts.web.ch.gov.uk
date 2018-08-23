package uk.gov.companieshouse.web.accounts.service.company.impl;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.error.ApiErrorResponseException;
import uk.gov.companieshouse.api.handler.company.CompanyResourceHandler;
import uk.gov.companieshouse.api.model.company.CompanyProfileApi;
import uk.gov.companieshouse.web.accounts.api.ApiClientService;
import uk.gov.companieshouse.web.accounts.service.company.CompanyService;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CompanyServiceImplTests {

    @Mock
    private ApiClient apiClient;

    @Mock
    private ApiClientService apiClientService;

    @Mock
    private CompanyResourceHandler companyResourceHandler;

    @InjectMocks
    private CompanyService companyService = new CompanyServiceImpl();

    private static final String COMPANY_NUMBER = "companyNumber";

    @BeforeEach
    private void init() {

        when(apiClientService.getApiClient()).thenReturn(apiClient);

        when(apiClient.company(COMPANY_NUMBER)).thenReturn(companyResourceHandler);
    }

    @Test
    @DisplayName("Get Company Profile - Success Path")
    void getCompanyProfileSuccess() throws ApiErrorResponseException {

        when(companyResourceHandler.get()).thenReturn(new CompanyProfileApi());

        CompanyProfileApi companyProfile = companyService.getCompanyProfile(COMPANY_NUMBER);

        assertNotNull(companyProfile);
    }

    @Test
    @DisplayName("Get Company Profile - Throws ApiErrorResponseException")
    void getBalanceSheetThrowsApiErrorResponseException() throws ApiErrorResponseException {

        when(companyResourceHandler.get()).thenThrow(ApiErrorResponseException.class);

        assertThrows(ApiErrorResponseException.class, () ->
                companyService.getCompanyProfile(COMPANY_NUMBER));
    }
}
