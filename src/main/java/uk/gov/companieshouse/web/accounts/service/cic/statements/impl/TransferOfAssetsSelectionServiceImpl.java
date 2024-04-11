package uk.gov.companieshouse.web.accounts.service.cic.statements.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.companieshouse.api.model.accounts.cic.statements.CicStatementsApi;
import uk.gov.companieshouse.web.accounts.enumeration.DefaultCicStatements;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.cic.statements.TransferOfAssetsSelection;
import uk.gov.companieshouse.web.accounts.service.cic.statements.CicStatementsService;
import uk.gov.companieshouse.web.accounts.service.cic.statements.TransferOfAssetsSelectionService;

@Service
public class TransferOfAssetsSelectionServiceImpl implements
        TransferOfAssetsSelectionService {
    @Autowired
    private CicStatementsService cicStatementsService;

    @Override
    public TransferOfAssetsSelection getTransferOfAssetsSelection(
            String transactionId, String companyAccountsId) throws ServiceException {
        TransferOfAssetsSelection selection = new TransferOfAssetsSelection();

        CicStatementsApi statements =
                cicStatementsService.getCicStatementsApi(transactionId, companyAccountsId);

        if (!statements.getReportStatements()
                .getTransferOfAssets()
                        .equals(DefaultCicStatements.TRANSFER_OF_ASSETS.getDefaultStatement())) {
            selection.setHasProvidedTransferOfAssets(true);
        }

        return selection;
    }

    @Override
    public void submitTransferOfAssetsSelection(String transactionId, String companyAccountsId,
            TransferOfAssetsSelection selection)
            throws ServiceException {
        if (Boolean.FALSE.equals(selection.getHasProvidedTransferOfAssets())) {
            CicStatementsApi statements =
                    cicStatementsService.getCicStatementsApi(transactionId, companyAccountsId);

            if (!statements.getReportStatements()
                    .getTransferOfAssets()
                            .equals(DefaultCicStatements.TRANSFER_OF_ASSETS.getDefaultStatement())) {
                statements.getReportStatements().setTransferOfAssets(
                        DefaultCicStatements.TRANSFER_OF_ASSETS.getDefaultStatement());

                cicStatementsService.updateCicStatementsApi(transactionId, companyAccountsId, statements);
            }
        }
    }
}
