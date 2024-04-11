package uk.gov.companieshouse.web.accounts.service.notehandler.dates.smallfull;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.model.accounts.smallfull.AccountingPeriodApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.SmallFullApi;
import uk.gov.companieshouse.web.accounts.enumeration.NoteType;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.intangible.IntangibleAssets;
import uk.gov.companieshouse.web.accounts.service.notehandler.dates.DateHandler;
import uk.gov.companieshouse.web.accounts.service.smallfull.SmallFullService;

@Component
public class IntangibleAssetsDateHandler implements DateHandler<IntangibleAssets> {
    @Autowired
    private SmallFullService smallFullService;

    @Override
    public void addDates(ApiClient apiClient, String transactionId, String companyAccountsId,
                    IntangibleAssets note) throws ServiceException {
        SmallFullApi smallFullApi = smallFullService.getSmallFullAccounts(apiClient, transactionId,
                        companyAccountsId);
        AccountingPeriodApi nextAccounts = smallFullApi.getNextAccounts();
        note.setNextAccountsPeriodStartOn(
                        nextAccounts.getPeriodStartOn());
        note.setNextAccountsPeriodEndOn(
                        nextAccounts.getPeriodEndOn());
        note.setLastAccountsPeriodEndOn(
                        Optional.of(smallFullApi).map(SmallFullApi::getLastAccounts)
                                        .map(AccountingPeriodApi::getPeriodEndOn).orElse(null));
    }

    @Override
    public NoteType getNoteType() {
        return NoteType.SMALL_FULL_INTANGIBLE_ASSETS;
    }
}
