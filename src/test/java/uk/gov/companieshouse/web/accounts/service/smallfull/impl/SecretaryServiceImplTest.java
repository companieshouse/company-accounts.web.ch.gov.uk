package uk.gov.companieshouse.web.accounts.service.smallfull.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.error.ApiErrorResponseException;
import uk.gov.companieshouse.api.handler.exception.URIValidationException;
import uk.gov.companieshouse.api.handler.smallfull.SmallFullResourceHandler;
import uk.gov.companieshouse.api.handler.smallfull.directorsreport.DirectorsReportResourceHandler;
import uk.gov.companieshouse.api.handler.smallfull.directorsreport.secretary.SecretaryResourceHandler;
import uk.gov.companieshouse.api.handler.smallfull.directorsreport.secretary.request.SecretaryDelete;
import uk.gov.companieshouse.api.handler.smallfull.directorsreport.secretary.request.SecretaryGet;
import uk.gov.companieshouse.api.handler.smallfull.directorsreport.secretary.request.SecretaryUpdate;
import uk.gov.companieshouse.api.model.ApiResponse;
import uk.gov.companieshouse.api.model.accounts.directorsreport.SecretaryApi;
import uk.gov.companieshouse.web.accounts.api.ApiClientService;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.directorsreport.AddOrRemoveDirectors;
import uk.gov.companieshouse.web.accounts.service.smallfull.SecretaryService;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.directorsreport.SecretaryTransformer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SecretaryServiceImplTest {

    @Mock
    private ApiClientService apiClientService;

    @Mock
    private ApiClient apiClient;

    @Mock
    private SecretaryApi secretaryApi;

    @Mock
    private AddOrRemoveDirectors addOrRemoveDirectors;

    @Mock
    private SecretaryTransformer secretaryTransformer;

    @Mock
    private SecretaryGet secretaryGet;

    @Mock
    private SecretaryDelete secretaryDelete;

    @Mock
    private SecretaryUpdate secretaryUpdate;

    @Mock
    private ApiResponse<SecretaryApi> responseWithData;

    @Mock
    private SmallFullResourceHandler smallFullResourceHandler;

    @Mock
    private DirectorsReportResourceHandler directorsReportResourceHandler;

    @Mock
    private SecretaryResourceHandler secretaryResourceHandler;

    private static final String TRANSACTION_ID = "transactionId";

    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";

    private static final String SECRETARY_URI = "/transactions/" + TRANSACTION_ID + "/company-accounts/" +
            COMPANY_ACCOUNTS_ID + "/small-full/directors-report/secretary";

    @InjectMocks
    private SecretaryService secretaryService = new SecretaryServiceImpl();

    @Test
    @DisplayName("Get Secretary success")
    void getSecretarySuccess() throws ServiceException, ApiErrorResponseException, URIValidationException {


        when(apiClientService.getApiClient()).thenReturn(apiClient);
        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);
        when(smallFullResourceHandler.directorsReport()).thenReturn(directorsReportResourceHandler);
        when(directorsReportResourceHandler.secretary()).thenReturn(secretaryResourceHandler);

        when(secretaryResourceHandler.get(SECRETARY_URI)).thenReturn(secretaryGet);
        when(secretaryGet.execute()).thenReturn(responseWithData);

        SecretaryApi secretary = new SecretaryApi();
        when(responseWithData.getData()).thenReturn(secretary);

        AddOrRemoveDirectors newSecretary = new AddOrRemoveDirectors();
        when(secretaryTransformer.getSecretary(secretary)).thenReturn(newSecretary);

        String result = secretaryService.getSecretary(TRANSACTION_ID, COMPANY_ACCOUNTS_ID);
        
    }
}
