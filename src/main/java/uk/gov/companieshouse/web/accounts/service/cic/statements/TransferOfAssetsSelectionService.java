package uk.gov.companieshouse.web.accounts.service.cic.statements;

import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.cic.statements.TransferOfAssetsSelection;

public interface TransferOfAssetsSelectionService {

    TransferOfAssetsSelection getTransferOfAssetsSelection(
            String transactionId, String companyAccountsId) throws ServiceException;

    void submitTransferOfAssetsSelection(String transactionId, String companyAccountsId,
            TransferOfAssetsSelection selection) throws ServiceException;
}
