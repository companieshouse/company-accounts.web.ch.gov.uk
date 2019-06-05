package uk.gov.companieshouse.web.accounts.service.company.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
import uk.gov.companieshouse.api.handler.company.request.CompanyGet;
import uk.gov.companieshouse.api.handler.exception.URIValidationException;
import uk.gov.companieshouse.api.model.ApiResponse;
import uk.gov.companieshouse.api.model.company.CompanyProfileApi;
import uk.gov.companieshouse.web.accounts.api.ApiClientService;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.company.CompanyDetail;
import uk.gov.companieshouse.web.accounts.service.company.CompanyService;
import uk.gov.companieshouse.web.accounts.transformer.company.CompanyDetailTransformer;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CompanyServiceImplTests {

    @Mock
    private ApiClient apiClient;

    @Mock
    private ApiClientService apiClientService;

    @Mock
    private CompanyResourceHandler companyResourceHandler;

    @Mock
    private CompanyGet companyGet;

    @Mock
    private ApiResponse<CompanyProfileApi> responseWithData;

    @Mock
    private CompanyProfileApi companyProfile;

    @Mock
    private CompanyDetail companyDetail;

    @Mock
    private CompanyDetailTransformer companyDetailTransformer;

    @InjectMocks
    private CompanyService companyService = new CompanyServiceImpl();

    private static final String COMPANY_NUMBER = "companyNumber";

    private static final String COMPANY_URI = "/company/" + COMPANY_NUMBER;

    @BeforeEach
    private void init() {

        when(apiClientService.getApiClient()).thenReturn(apiClient);

        when(apiClient.company()).thenReturn(companyResourceHandler);

        when(companyResourceHandler.get(COMPANY_URI)).thenReturn(companyGet);
    }

    @Test
    @DisplayName("Get Company Profile - Success Path")
    void getCompanyProfileSuccess() throws ServiceException, ApiErrorResponseException, URIValidationException {

        when(companyGet.execute()).thenReturn(responseWithData);

        when(responseWithData.getData()).thenReturn(companyProfile);

        CompanyProfileApi returnedCompanyProfile = companyService.getCompanyProfile(COMPANY_NUMBER);

        assertEquals(companyProfile, returnedCompanyProfile);
    }

    @Test
    @DisplayName("Get Company Profile - Throws ApiErrorResponseException")
    void getCompanyProfileThrowsApiErrorResponseException() throws ApiErrorResponseException, URIValidationException {

        when(companyGet.execute()).thenThrow(ApiErrorResponseException.class);

        assertThrows(ServiceException.class, () ->
                companyService.getCompanyProfile(COMPANY_NUMBER));
    }

    @Test
    @DisplayName("Get Company Profile - Throws URIValidationException")
    void getCompanyProfileThrowsURIValidationException() throws ApiErrorResponseException, URIValidationException {

        when(companyGet.execute()).thenThrow(URIValidationException.class);

        assertThrows(ServiceException.class, () ->
                companyService.getCompanyProfile(COMPANY_NUMBER));
    }

    @Test
    @DisplayName("Get Company Details - Success Path")
    void getCompanyDetailSuccess() throws ServiceException, ApiErrorResponseException, URIValidationException {

        when(companyGet.execute()).thenReturn(responseWithData);

        when(responseWithData.getData()).thenReturn(companyProfile);

        when(companyDetailTransformer.getCompanyDetail(companyProfile)).thenReturn(companyDetail);

        CompanyDetail returnedCompanyDetail = companyService.getCompanyDetail(COMPANY_NUMBER);

        assertEquals(companyDetail, returnedCompanyDetail);
    }
}
