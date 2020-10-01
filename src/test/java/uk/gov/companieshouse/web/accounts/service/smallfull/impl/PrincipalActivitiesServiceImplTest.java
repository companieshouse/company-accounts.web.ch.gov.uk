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
import uk.gov.companieshouse.web.accounts.model.directorsreport.PrincipalActivities;
import uk.gov.companieshouse.web.accounts.service.smallfull.PrincipalActivitiesService;
import uk.gov.companieshouse.web.accounts.service.smallfull.DirectorsReportStatementsService;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.directorsreport.DirectorsReportStatementsTransformer;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PrincipalActivitiesServiceImplTest {

    @Mock
    private DirectorsReportStatementsService directorsReportStatementsService;

    @Mock
    private DirectorsReportStatementsTransformer directorsReportStatementsTransformer;

    @InjectMocks
    private PrincipalActivitiesService principalActivitiesService = new PrincipalActivitiesServiceImpl();

    @Mock
    private PrincipalActivities principalActivities;

    @Mock
    private StatementsApi statementsApi;

    @Mock
    private List<ValidationError> validationErrors;

    private static final String TRANSACTION_ID = "transactionId";

    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";

    @Test
    @DisplayName("Get principal activities")
    void getPrincipalActivities() throws ServiceException {

        when(directorsReportStatementsService.getDirectorsReportStatements(TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(statementsApi);

        when(directorsReportStatementsTransformer.getPrincipalActivities(statementsApi))
                .thenReturn(principalActivities);

        PrincipalActivities returned =
                principalActivitiesService.getPrincipalActivities(TRANSACTION_ID, COMPANY_ACCOUNTS_ID);

        assertEquals(principalActivities, returned);
    }

    @Test
    @DisplayName("Submit principal activities - create")
    void submitPrincipalActivitiesCreate() throws ServiceException {

        when(directorsReportStatementsService.getDirectorsReportStatements(TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(null);

        when(directorsReportStatementsService.createDirectorsReportStatements(eq(TRANSACTION_ID), eq(COMPANY_ACCOUNTS_ID), any(StatementsApi.class)))
                .thenReturn(validationErrors);

        List<ValidationError> returned = principalActivitiesService.submitPrincipalActivities(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, principalActivities);

        assertEquals(validationErrors, returned);

        verify(directorsReportStatementsTransformer).setPrincipalActivities(any(StatementsApi.class), eq(principalActivities));
    }

    @Test
    @DisplayName("Submit principal activities - update")
    void submitPrincipalActivitiesUpdate() throws ServiceException {

        when(directorsReportStatementsService.getDirectorsReportStatements(TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(statementsApi);

        when(directorsReportStatementsService.updateDirectorsReportStatements(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, statementsApi))
                .thenReturn(validationErrors);

        List<ValidationError> returned = principalActivitiesService.submitPrincipalActivities(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, principalActivities);

        assertEquals(validationErrors, returned);

        verify(directorsReportStatementsTransformer).setPrincipalActivities(statementsApi, principalActivities);
    }
}
