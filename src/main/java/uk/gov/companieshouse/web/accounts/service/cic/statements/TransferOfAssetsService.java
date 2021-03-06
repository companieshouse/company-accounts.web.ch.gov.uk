package uk.gov.companieshouse.web.accounts.service.cic.statements;

import java.util.List;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.cic.statements.TransferOfAssets;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

public interface TransferOfAssetsService {

    /**
     * Retrieves the transfer of assets
     *
     * @param transactionId The id of the CHS transaction
     * @param companyAccountsId The company accounts identifier
     * @return the transfer of assets
     * @throws ServiceException if there's an error while fetching the resource
     */
    TransferOfAssets getTransferOfAssets(String transactionId,
        String companyAccountsId)
        throws ServiceException;

    /**
     * Submits the transfer of assets
     *
     * @param transactionId The id of the CHS transaction
     * @param companyAccountsId The company accounts identifier
     * @return a list of validation errors, or an empty list if none are present
     * @throws ServiceException if there's an error while submitting the resource
     */
    List<ValidationError> submitTransferOfAssets(String transactionId,
        String companyAccountsId,
        TransferOfAssets transferOfAssets) throws ServiceException;
}
