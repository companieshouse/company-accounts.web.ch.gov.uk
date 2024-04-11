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
import uk.gov.companieshouse.web.accounts.model.directorsreport.PoliticalAndCharitableDonations;
import uk.gov.companieshouse.web.accounts.service.smallfull.PoliticalAndCharitableDonationsService;
import uk.gov.companieshouse.web.accounts.service.smallfull.DirectorsReportStatementsService;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.directorsreport.DirectorsReportStatementsTransformer;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PoliticalAndCharitableDonationsServiceImplTest {
    @Mock
    private DirectorsReportStatementsService directorsReportStatementsService;

    @Mock
    private DirectorsReportStatementsTransformer directorsReportStatementsTransformer;

    @InjectMocks
    private PoliticalAndCharitableDonationsService politicalAndCharitableDonationsService = new PoliticalAndCharitableDonationsServiceImpl();

    @Mock
    private PoliticalAndCharitableDonations politicalAndCharitableDonations;

    @Mock
    private StatementsApi statementsApi;

    @Mock
    private List<ValidationError> validationErrors;

    private static final String TRANSACTION_ID = "transactionId";

    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";

    @Test
    @DisplayName("Get political and charitable donations")
    void getPoliticalAndCharitableDonations() throws ServiceException {
        when(directorsReportStatementsService.getDirectorsReportStatements(TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(statementsApi);

        when(directorsReportStatementsTransformer.getPoliticalAndCharitableDonations(statementsApi))
                .thenReturn(politicalAndCharitableDonations);

        PoliticalAndCharitableDonations returned =
                politicalAndCharitableDonationsService.getPoliticalAndCharitableDonations(TRANSACTION_ID, COMPANY_ACCOUNTS_ID);

        assertEquals(politicalAndCharitableDonations, returned);
    }

    @Test
    @DisplayName("Submit political and charitable donations - create")
    void submitPoliticalAndCharitableDonationsCreate() throws ServiceException {
        when(directorsReportStatementsService.getDirectorsReportStatements(TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(null);

        when(directorsReportStatementsService.createDirectorsReportStatements(eq(TRANSACTION_ID), eq(COMPANY_ACCOUNTS_ID), any(StatementsApi.class)))
                .thenReturn(validationErrors);

        List<ValidationError> returned =
                politicalAndCharitableDonationsService
                        .submitPoliticalAndCharitableDonations(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, politicalAndCharitableDonations);

        assertEquals(validationErrors, returned);

        verify(directorsReportStatementsTransformer).setPoliticalAndCharitableDonations(any(StatementsApi.class), eq(politicalAndCharitableDonations));
    }

    @Test
    @DisplayName("Submit political and charitable donations - update")
    void submitPoliticalAndCharitableDonationsUpdate() throws ServiceException {
        when(directorsReportStatementsService.getDirectorsReportStatements(TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(statementsApi);

        when(directorsReportStatementsService.updateDirectorsReportStatements(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, statementsApi))
                .thenReturn(validationErrors);

        List<ValidationError> returned =
                politicalAndCharitableDonationsService
                        .submitPoliticalAndCharitableDonations(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, politicalAndCharitableDonations);

        assertEquals(validationErrors, returned);

        verify(directorsReportStatementsTransformer).setPoliticalAndCharitableDonations(statementsApi, politicalAndCharitableDonations);
    }
}
