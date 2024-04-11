package uk.gov.companieshouse.web.accounts.service.notehandler.dates.smallfull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.model.accounts.smallfull.SmallFullApi;
import uk.gov.companieshouse.web.accounts.enumeration.NoteType;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.creditorswithinoneyear.CreditorsWithinOneYear;
import uk.gov.companieshouse.web.accounts.service.notehandler.dates.DateHandler;
import uk.gov.companieshouse.web.accounts.service.smallfull.SmallFullService;

@Component
public class CreditorsWithinOneYearDateHandler implements DateHandler<CreditorsWithinOneYear> {
    @Autowired
    private SmallFullService smallFullService;

    @Override
    public void addDates(ApiClient apiClient, String transactionId, String companyAccountsId, CreditorsWithinOneYear note) throws ServiceException {
        SmallFullApi smallFullApi = smallFullService.getSmallFullAccounts(apiClient, transactionId, companyAccountsId);

        note.setBalanceSheetHeadings(smallFullService.getBalanceSheetHeadings(smallFullApi));
    }

    @Override
    public NoteType getNoteType() {
        return NoteType.SMALL_FULL_CREDITORS_WITHIN_ONE_YEAR;
    }
}
