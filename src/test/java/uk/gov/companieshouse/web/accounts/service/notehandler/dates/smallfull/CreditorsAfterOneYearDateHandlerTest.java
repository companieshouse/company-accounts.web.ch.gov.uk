package uk.gov.companieshouse.web.accounts.service.notehandler.dates.smallfull;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.model.accounts.smallfull.SmallFullApi;
import uk.gov.companieshouse.web.accounts.enumeration.NoteType;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.smallfull.BalanceSheetHeadings;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.creditorsafteroneyear.CreditorsAfterOneYear;
import uk.gov.companieshouse.web.accounts.service.notehandler.dates.DateHandler;
import uk.gov.companieshouse.web.accounts.service.smallfull.SmallFullService;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)

public class CreditorsAfterOneYearDateHandlerTest {

    private static final String TRANSACTION_ID = "transactionId";

    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";

    @Mock
    private ApiClient apiClient;

    @Mock
    private SmallFullService smallFullService;

    @Mock
    private SmallFullApi smallFullApi;

    @Mock
    private CreditorsAfterOneYear creditorsAfterOneYear;

    @Mock
    private BalanceSheetHeadings balanceSheetHeadings;

    private DateHandler<CreditorsAfterOneYear> creditorsAfterOneYearDateHandler;

    @BeforeEach
    void before() {
        creditorsAfterOneYearDateHandler = new CreditorsAfterOneYearDateHandler(smallFullService);
    }

    @Test
    @DisplayName("Add dates")
    void addDates() throws ServiceException {

        when(smallFullService.getSmallFullAccounts(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(smallFullApi);

        when(smallFullService.getBalanceSheetHeadings(smallFullApi)).thenReturn(balanceSheetHeadings);

        assertAll(() -> creditorsAfterOneYearDateHandler.addDates(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID, creditorsAfterOneYear));

        verify(creditorsAfterOneYear).setBalanceSheetHeadings(balanceSheetHeadings);
    }

    @Test
    @DisplayName("Get note type")
    void getNoteType() {

        assertEquals(NoteType.SMALL_FULL_CREDITORS_AFTER_ONE_YEAR, creditorsAfterOneYearDateHandler.getNoteType());
    }
}
