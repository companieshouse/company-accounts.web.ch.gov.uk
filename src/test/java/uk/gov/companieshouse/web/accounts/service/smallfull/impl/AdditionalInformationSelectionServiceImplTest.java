package uk.gov.companieshouse.web.accounts.service.smallfull.impl;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.api.model.accounts.directorsreport.StatementsApi;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.directorsreport.AdditionalInformationSelection;
import uk.gov.companieshouse.web.accounts.service.smallfull.AdditionalInformationSelectionService;
import uk.gov.companieshouse.web.accounts.service.smallfull.DirectorsReportStatementsService;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AdditionalInformationSelectionServiceImplTest {

    @Mock
    private DirectorsReportStatementsService directorsReportStatementsService;

    @InjectMocks
    private AdditionalInformationSelectionService additionalInformationSelectionService = new AdditionalInformationSelectionServiceImpl();

    @Mock
    private AdditionalInformationSelection additionalInformationSelection;

    @Mock
    private StatementsApi statementsApi;

    private static final String TRANSACTION_ID = "transactionId";

    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";

    private static final String ADDITIONAL_INFORMATION = "additionalInformation";

    private static final String PRINCIPAL_ACTIVITIES = "principalActivities";

    @Test
    @DisplayName("Get additional information selection - no existing statements")
    void getAdditionalInformationSelectionNoExistingStatements() throws ServiceException {

        when(directorsReportStatementsService.getDirectorsReportStatements(TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(null);

        AdditionalInformationSelection returned =
                additionalInformationSelectionService.getAdditionalInformationSelection(TRANSACTION_ID, COMPANY_ACCOUNTS_ID);

        assertNotNull(returned);
        assertNull(returned.getHasAdditionalInformation());
    }

    @Test
    @DisplayName("Get additional information selection - statements do not include additional information")
    void getAdditionalInformationSelectionStatementsDoNotIncludeAdditionalInformation() throws ServiceException {

        when(directorsReportStatementsService.getDirectorsReportStatements(TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(statementsApi);

        when(statementsApi.getAdditionalInformation()).thenReturn(null);

        AdditionalInformationSelection returned =
                additionalInformationSelectionService.getAdditionalInformationSelection(TRANSACTION_ID, COMPANY_ACCOUNTS_ID);

        assertNotNull(returned);
        assertNull(returned.getHasAdditionalInformation());
    }

    @Test
    @DisplayName("Get additional information selection - statements include additional information")
    void getAdditionalInformationSelectionStatementsIncludeAdditionalInformation() throws ServiceException {

        when(directorsReportStatementsService.getDirectorsReportStatements(TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(statementsApi);

        when(statementsApi.getAdditionalInformation()).thenReturn(ADDITIONAL_INFORMATION);

        AdditionalInformationSelection returned =
                additionalInformationSelectionService.getAdditionalInformationSelection(TRANSACTION_ID, COMPANY_ACCOUNTS_ID);

        assertNotNull(returned);
        assertTrue(returned.getHasAdditionalInformation());
    }

    @Test
    @DisplayName("Submit additional information selection - has additional information")
    void submitAdditionalInformationSelectionHasAdditionalInformation() throws ServiceException {

        when(additionalInformationSelection.getHasAdditionalInformation()).thenReturn(true);

        assertAll(() ->
                additionalInformationSelectionService
                        .submitAdditionalInformationSelection(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, additionalInformationSelection));

        verify(directorsReportStatementsService, never()).getDirectorsReportStatements(TRANSACTION_ID, COMPANY_ACCOUNTS_ID);
    }

    @Test
    @DisplayName("Submit additional information selection - no existing statements")
    void submitAdditionalInformationSelectionNoExistingStatements() throws ServiceException {

        when(additionalInformationSelection.getHasAdditionalInformation()).thenReturn(false);

        when(directorsReportStatementsService.getDirectorsReportStatements(TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(null);

        assertAll(() ->
                additionalInformationSelectionService
                        .submitAdditionalInformationSelection(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, additionalInformationSelection));
    }

    @Test
    @DisplayName("Submit additional information selection - has other statements")
    void submitAdditionalInformationSelectionHasOtherStatements() throws ServiceException {

        when(additionalInformationSelection.getHasAdditionalInformation()).thenReturn(false);

        when(directorsReportStatementsService.getDirectorsReportStatements(TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(statementsApi);

        when(statementsApi.getPrincipalActivities()).thenReturn(PRINCIPAL_ACTIVITIES);

        assertAll(() ->
                additionalInformationSelectionService
                        .submitAdditionalInformationSelection(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, additionalInformationSelection));

        verify(statementsApi).setAdditionalInformation(null);

        verify(directorsReportStatementsService).updateDirectorsReportStatements(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, statementsApi);
    }

    @Test
    @DisplayName("Submit additional information selection - has no other statements")
    void submitAdditionalInformationSelectionHasNoOtherStatements() throws ServiceException {

        when(additionalInformationSelection.getHasAdditionalInformation()).thenReturn(false);

        when(directorsReportStatementsService.getDirectorsReportStatements(TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(statementsApi);

        assertAll(() ->
                additionalInformationSelectionService
                        .submitAdditionalInformationSelection(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, additionalInformationSelection));

        verify(directorsReportStatementsService).deleteDirectorsReportStatements(TRANSACTION_ID, COMPANY_ACCOUNTS_ID);
    }
}
