package uk.gov.companieshouse.web.accounts.service.smallfull.impl;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.api.model.accounts.smallfull.loanstodirectors.AdditionalInformationApi;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.loanstodirectors.AdditionalInformationSelection;
import uk.gov.companieshouse.web.accounts.service.smallfull.AdditionalInformationSelectionService;
import uk.gov.companieshouse.web.accounts.service.smallfull.LoansToDirectorsAdditionalInformationService;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class LoansToDirectorsAdditionalInformationSelectionServiceImplTest {

    @Mock
    private LoansToDirectorsAdditionalInformationService additionalInformationService;

    @InjectMocks
    private AdditionalInformationSelectionService<AdditionalInformationSelection> additionalInformationSelectionService = new LoansToDirectorsAdditionalInformationSelectionServiceImpl();

    @Mock
    private AdditionalInformationSelection additionalInformationSelection;

    @Mock
    private AdditionalInformationApi additionalInformationApi;

    private static final String TRANSACTION_ID = "transactionId";

    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";

    private static final String ADDITIONAL_INFORMATION = "additionalInformation";

    @Test
    @DisplayName("Get additional information selection - no existing information")
    void getAdditionalInformationSelectionNoExistingInformation() throws ServiceException {

        when(additionalInformationService.getAdditionalInformation(TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(null);

        AdditionalInformationSelection returned =
                additionalInformationSelectionService.getAdditionalInformationSelection(TRANSACTION_ID, COMPANY_ACCOUNTS_ID);

        assertNotNull(returned);
        assertNull(returned.getHasAdditionalInformation());
    }

    @Test
    @DisplayName("Get additional information selection - include additional information")
    void getAdditionalInformationSelectionIncludeAdditionalInformation() throws ServiceException {

        when(additionalInformationService.getAdditionalInformation(TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(additionalInformationApi);

        when(additionalInformationApi.getDetails()).thenReturn(ADDITIONAL_INFORMATION);

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

        verify(additionalInformationService, never()).getAdditionalInformation(TRANSACTION_ID, COMPANY_ACCOUNTS_ID);
    }

    @Test
    @DisplayName("Submit additional information selection - no existing additional information")
    void submitAdditionalInformationSelectionNoExistingAdditionalInformation() throws ServiceException {

        when(additionalInformationSelection.getHasAdditionalInformation()).thenReturn(false);

        when(additionalInformationService.getAdditionalInformation(TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(null);

        assertAll(() ->
                additionalInformationSelectionService
                        .submitAdditionalInformationSelection(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, additionalInformationSelection));
    }

    @Test
    @DisplayName("Submit additional information selection - has additional information details")
    void submitAdditionalInformationSelectionHasDetails() throws ServiceException {

        when(additionalInformationSelection.getHasAdditionalInformation()).thenReturn(false);

        when(additionalInformationService.getAdditionalInformation(TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(additionalInformationApi);

        when(additionalInformationApi.getDetails()).thenReturn(ADDITIONAL_INFORMATION);

        assertAll(() ->
                additionalInformationSelectionService
                        .submitAdditionalInformationSelection(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, additionalInformationSelection));

        verify(additionalInformationApi).setDetails(null);

        verify(additionalInformationService).updateAdditionalInformation(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, additionalInformationApi);
    }

    @Test
    @DisplayName("Submit additional information selection - has no additional information details")
    void submitAdditionalInformationSelectionHasNoDetails() throws ServiceException {

        when(additionalInformationSelection.getHasAdditionalInformation()).thenReturn(false);

        when(additionalInformationService.getAdditionalInformation(TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(additionalInformationApi);

        when(additionalInformationApi.getDetails()).thenReturn(null);

        assertAll(() ->
                additionalInformationSelectionService
                        .submitAdditionalInformationSelection(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, additionalInformationSelection));

        verify(additionalInformationService).deleteAdditionalInformation(TRANSACTION_ID, COMPANY_ACCOUNTS_ID);
    }
}