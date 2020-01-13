package uk.gov.companieshouse.web.accounts.service.smallfull.impl;

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
import uk.gov.companieshouse.api.model.accounts.directorsreport.StatementsApi;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.directorsreport.CompanyPolicyOnDisabledEmployees;
import uk.gov.companieshouse.web.accounts.service.smallfull.CompanyPolicyOnDisabledEmployeesService;
import uk.gov.companieshouse.web.accounts.service.smallfull.DirectorsReportStatementsService;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.directorsreport.DirectorsReportStatementsTransformer;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CompanyPolicyOnDisabledEmployeesServiceImplTest {

    @Mock
    private DirectorsReportStatementsService directorsReportStatementsService;

    @Mock
    private DirectorsReportStatementsTransformer directorsReportStatementsTransformer;

    @InjectMocks
    private CompanyPolicyOnDisabledEmployeesService companyPolicyOnDisabledEmployeesService = new CompanyPolicyOnDisabledEmployeesServiceImpl();

    @Mock
    private CompanyPolicyOnDisabledEmployees companyPolicyOnDisabledEmployees;

    @Mock
    private StatementsApi statementsApi;

    @Mock
    private List<ValidationError> validationErrors;

    private static final String TRANSACTION_ID = "transactionId";

    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";

    @Test
    @DisplayName("Get company policy on disabled employees")
    void getCompanyPolicyOnDisabledEmployees() throws ServiceException {

        when(directorsReportStatementsService.getDirectorsReportStatements(TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(statementsApi);

        when(directorsReportStatementsTransformer.getCompanyPolicyOnDisabledEmployees(statementsApi))
                .thenReturn(companyPolicyOnDisabledEmployees);

        CompanyPolicyOnDisabledEmployees returned =
                companyPolicyOnDisabledEmployeesService.getCompanyPolicyOnDisabledEmployees(TRANSACTION_ID, COMPANY_ACCOUNTS_ID);

        assertEquals(companyPolicyOnDisabledEmployees, returned);
    }

    @Test
    @DisplayName("Submit company policy on disabled employees - create")
    void submitCompanyPolicyOnDisabledEmployeesCreate() throws ServiceException {

        when(directorsReportStatementsService.getDirectorsReportStatements(TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(null);

        when(directorsReportStatementsService.createDirectorsReportStatements(eq(TRANSACTION_ID), eq(COMPANY_ACCOUNTS_ID), any(StatementsApi.class)))
                .thenReturn(validationErrors);

        List<ValidationError> returned =
                companyPolicyOnDisabledEmployeesService
                        .submitCompanyPolicyOnDisabledEmployees(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, companyPolicyOnDisabledEmployees);

        assertEquals(validationErrors, returned);

        verify(directorsReportStatementsTransformer).setCompanyPolicyOnDisabledEmployees(any(StatementsApi.class), eq(companyPolicyOnDisabledEmployees));
    }

    @Test
    @DisplayName("Submit company policy on disabled employees - update")
    void submitCompanyPolicyOnDisabledEmployeesUpdate() throws ServiceException {

        when(directorsReportStatementsService.getDirectorsReportStatements(TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(statementsApi);

        when(directorsReportStatementsService.updateDirectorsReportStatements(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, statementsApi))
                .thenReturn(validationErrors);

        List<ValidationError> returned =
                companyPolicyOnDisabledEmployeesService
                        .submitCompanyPolicyOnDisabledEmployees(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, companyPolicyOnDisabledEmployees);

        assertEquals(validationErrors, returned);

        verify(directorsReportStatementsTransformer).setCompanyPolicyOnDisabledEmployees(statementsApi, companyPolicyOnDisabledEmployees);
    }
}
