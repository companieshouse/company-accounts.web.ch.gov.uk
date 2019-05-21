package uk.gov.companieshouse.web.accounts.service.cic.statements.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.companieshouse.api.model.accounts.cic.statements.CicStatementsApi;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.cic.statements.TransferOfAssetsSelection;
import uk.gov.companieshouse.web.accounts.service.cic.statements.CicStatementsService;
import uk.gov.companieshouse.web.accounts.service.cic.statements.TransferOfAssetsSelectionService;

@Service
public class TransferOfAssetsSelectionServiceImpl implements
        TransferOfAssetsSelectionService {

    @Autowired
    private CicStatementsService cicStatementsService;

    private static final String DEFAULT_TRANSFER_OF_ASSETS_STATEMENT =
            "No transfer of assets other than for full consideration";

    @Override
    public TransferOfAssetsSelection getTransferOfAssetsSelection(
            String transactionId, String companyAccountsId) throws ServiceException {

        TransferOfAssetsSelection selection = new TransferOfAssetsSelection();

        CicStatementsApi statements =
                cicStatementsService.getCicStatementsApi(transactionId, companyAccountsId);

        if (!statements.getReportStatements()
                .getTransferOfAssets()
                        .equals(DEFAULT_TRANSFER_OF_ASSETS_STATEMENT)) {

            selection.setHasProvidedTransferOfAssets(true);
        }

        return selection;
    }

    @Override
    public void submitTransferOfAssetsSelection(String transactionId, String companyAccountsId,
            TransferOfAssetsSelection selection)
            throws ServiceException {

        if (!selection.getHasProvidedTransferOfAssets()) {

            CicStatementsApi statements =
                    cicStatementsService.getCicStatementsApi(transactionId, companyAccountsId);

            if (!statements.getReportStatements()
                    .getTransferOfAssets()
                    .equals(DEFAULT_TRANSFER_OF_ASSETS_STATEMENT)) {

                statements.getReportStatements().setTransferOfAssets(
                        DEFAULT_TRANSFER_OF_ASSETS_STATEMENT);

                cicStatementsService.updateCicStatementsApi(transactionId, companyAccountsId, statements);
            }
        }
    }
}
