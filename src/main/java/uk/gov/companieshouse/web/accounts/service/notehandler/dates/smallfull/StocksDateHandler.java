package uk.gov.companieshouse.web.accounts.service.notehandler.dates.smallfull;

import org.springframework.beans.factory.annotation.Autowired;
import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.model.accounts.smallfull.SmallFullApi;
import uk.gov.companieshouse.web.accounts.enumeration.NoteType;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.stocks.StocksNote;
import uk.gov.companieshouse.web.accounts.service.notehandler.dates.DateHandler;
import uk.gov.companieshouse.web.accounts.service.smallfull.SmallFullService;

public class StocksDateHandler implements DateHandler<StocksNote> {

    @Autowired
    private SmallFullService smallFullService;

    @Override
    public void addDates(ApiClient apiClient, String transactionId, String companyAccountsId, StocksNote note) throws ServiceException {

        SmallFullApi smallFullApi = smallFullService.getSmallFullAccounts(apiClient, transactionId, companyAccountsId);

        note.setBalanceSheetHeadings(smallFullService.getBalanceSheetHeadings(smallFullApi));
    }

    @Override
    public NoteType getNoteType() {
        return NoteType.SMALL_FULL_STOCKS;
    }
}
