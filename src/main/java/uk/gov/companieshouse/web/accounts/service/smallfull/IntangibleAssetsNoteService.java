package uk.gov.companieshouse.web.accounts.service.smallfull;

import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.intangible.IntangibleAssets;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

import java.util.List;

public interface IntangibleAssetsNoteService {

    IntangibleAssets getIntangibleAssets(String transactionId, String companyAccountsId,
                                         String companyNumber)
        throws ServiceException;

    List<ValidationError> postIntangibleAssets(String transactionId, String companyAccountsId, IntangibleAssets
            intangibleAssets, String companyNumber) throws ServiceException;

    void deleteIntangibleAssets(String transactionId, String companyAccountsId)
        throws ServiceException;
}
