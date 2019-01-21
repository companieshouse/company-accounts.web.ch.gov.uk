package uk.gov.companieshouse.web.accounts.service.smallfull;

import java.util.List;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.tangible.TangibleAssets;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

public interface TangibleAssetsNoteService {

    TangibleAssets getTangibleAssets(String transactionId, String companyAccountsId,
        String companyNumber)
        throws ServiceException;

    List<ValidationError> postTangibleAssets(String transactionId, String companyAccountsId,
        TangibleAssets tangibleAssets, String companyNumber) throws ServiceException;

}