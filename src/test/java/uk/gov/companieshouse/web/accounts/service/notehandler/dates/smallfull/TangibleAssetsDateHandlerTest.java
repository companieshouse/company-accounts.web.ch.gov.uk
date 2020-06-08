package uk.gov.companieshouse.web.accounts.service.notehandler.dates.smallfull;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.model.accounts.smallfull.AccountingPeriodApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.SmallFullApi;
import uk.gov.companieshouse.web.accounts.enumeration.NoteType;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.smallfull.BalanceSheetHeadings;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.tangible.TangibleAssets;
import uk.gov.companieshouse.web.accounts.service.notehandler.dates.DateHandler;
import uk.gov.companieshouse.web.accounts.service.smallfull.SmallFullService;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TangibleAssetsDateHandlerTest {

    @Mock
    private ApiClient apiClient;

    @Mock
    private SmallFullService smallFullService;

    @Mock
    private SmallFullApi smallFullApi;

    @Mock
    private TangibleAssets tangibleAssets;

    @Mock
    private BalanceSheetHeadings balanceSheetHeadings;

    @InjectMocks
    private DateHandler<TangibleAssets> tangibleAssetsDateHandler = new TangibleAssetsDateHandler();

    private static final String TRANSACTION_ID = "transactionId";

    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";

    @Test
    @DisplayName("Add dates")
    void addDates() throws ServiceException {

        when(smallFullService.getSmallFullAccounts(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(smallFullApi);

        AccountingPeriodApi accountPeriodApi = new AccountingPeriodApi();
        accountPeriodApi.setPeriodStartOn(LocalDate.now().minusYears(1));
        accountPeriodApi.setPeriodEndOn(LocalDate.now());
        
        when(smallFullService.getBalanceSheetHeadings(smallFullApi)).thenReturn(balanceSheetHeadings);
        when(smallFullApi.getNextAccounts()).thenReturn(accountPeriodApi);

        assertAll(() -> tangibleAssetsDateHandler.addDates(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID, tangibleAssets));

        verify(tangibleAssets).setBalanceSheetHeadings(balanceSheetHeadings);
    }

    @Test
    @DisplayName("Get note type")
    void getNoteType() {

        assertEquals(NoteType.TANGIBLE_ASSETS, tangibleAssetsDateHandler.getNoteType());
    }
}
