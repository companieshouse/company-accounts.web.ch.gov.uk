package uk.gov.companieshouse.web.accounts.service.cic.statements.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.api.model.accounts.cic.statements.CicStatementsApi;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.cic.statements.CompanyActivitiesAndImpact;
import uk.gov.companieshouse.web.accounts.service.cic.statements.CicStatementsService;
import uk.gov.companieshouse.web.accounts.service.cic.statements.CompanyActivitiesAndImpactService;
import uk.gov.companieshouse.web.accounts.transformer.cic.CicStatementsTransformer;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CompanyActivitiesAndImpactServiceImplTest {
    @Mock
    private CicStatementsService cicStatementsService;

    @Mock
    private CicStatementsTransformer cicStatementsTransformer;

    @Mock
    private CicStatementsApi cicStatementsApi;

    @Mock
    private CompanyActivitiesAndImpact companyActivitiesAndImpact;

    @Mock
    private List<ValidationError> validationErrors;

    @InjectMocks
    private CompanyActivitiesAndImpactService companyActivitiesAndImpactService =
            new CompanyActivitiesAndImpactServiceImpl();

    private static final String TRANSACTION_ID = "transactionId";

    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";

    @Test
    @DisplayName("Get company activities and impact")
    void getCompanyActivitiesAndImpact() throws ServiceException {
        when(cicStatementsService.getCicStatementsApi(TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(cicStatementsApi);

        when(cicStatementsTransformer.getCompanyActivitiesAndImpact(cicStatementsApi))
                .thenReturn(companyActivitiesAndImpact);

        CompanyActivitiesAndImpact returnedCompanyActivitiesAndImpact =
                companyActivitiesAndImpactService
                        .getCompanyActivitiesAndImpact(TRANSACTION_ID, COMPANY_ACCOUNTS_ID);

        assertEquals(companyActivitiesAndImpact, returnedCompanyActivitiesAndImpact);
    }

    @Test
    @DisplayName("Submit company activities and impact - not found")
    void submitCompanyActivitiesAndImpactNotFound() throws ServiceException {
        when(cicStatementsService.getCicStatementsApi(TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(null);

        when(cicStatementsService.createCicStatementsApi(
                eq(TRANSACTION_ID), eq(COMPANY_ACCOUNTS_ID), any(CicStatementsApi.class)))
                        .thenReturn(validationErrors);

        List<ValidationError> returnedValidationErrors =
                companyActivitiesAndImpactService.submitCompanyActivitiesAndImpact(
                        TRANSACTION_ID, COMPANY_ACCOUNTS_ID, companyActivitiesAndImpact);

        assertEquals(validationErrors, returnedValidationErrors);

        verify(cicStatementsTransformer)
                .setCompanyActivitiesAndImpact(eq(companyActivitiesAndImpact), any(CicStatementsApi.class));
    }

    @Test
    @DisplayName("Submit company activities and impact - update existing")
    void submitCompanyActivitiesAndImpactUpdateExisting() throws ServiceException {
        when(cicStatementsService.getCicStatementsApi(TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(cicStatementsApi);

        when(cicStatementsService.updateCicStatementsApi(
                eq(TRANSACTION_ID), eq(COMPANY_ACCOUNTS_ID), any(CicStatementsApi.class)))
                        .thenReturn(validationErrors);

        List<ValidationError> returnedValidationErrors =
                companyActivitiesAndImpactService.submitCompanyActivitiesAndImpact(
                        TRANSACTION_ID, COMPANY_ACCOUNTS_ID, companyActivitiesAndImpact);

        assertEquals(validationErrors, returnedValidationErrors);

        verify(cicStatementsTransformer)
                .setCompanyActivitiesAndImpact(companyActivitiesAndImpact, cicStatementsApi);
    }
}
