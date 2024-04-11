package uk.gov.companieshouse.web.accounts.service.notehandler.dates.smallfull;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.model.accounts.smallfull.AccountingPeriodApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.SmallFullApi;
import uk.gov.companieshouse.web.accounts.enumeration.NoteType;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.tangible.TangibleAssets;
import uk.gov.companieshouse.web.accounts.service.notehandler.dates.DateHandler;
import uk.gov.companieshouse.web.accounts.service.smallfull.SmallFullService;

@Component
public class TangibleAssetsDateHandler implements DateHandler<TangibleAssets> {
    @Autowired
    private SmallFullService smallFullService;

    @Override
    public void addDates(ApiClient apiClient, String transactionId, String companyAccountsId, TangibleAssets note) throws ServiceException {
        SmallFullApi smallFullApi = smallFullService.getSmallFullAccounts(apiClient, transactionId, companyAccountsId);

        note.setLastAccountsPeriodEndOn(Optional.of(smallFullApi)
                .map(SmallFullApi::getLastAccounts)
                .map(AccountingPeriodApi::getPeriodEndOn)
                .orElse(null));
        note.setNextAccountsPeriodStartOn(smallFullApi.getNextAccounts().getPeriodStartOn());
        note.setNextAccountsPeriodEndOn(smallFullApi.getNextAccounts().getPeriodEndOn());
    }

    @Override
    public NoteType getNoteType() {
        return NoteType.SMALL_FULL_TANGIBLE_ASSETS;
    }
}