package uk.gov.companieshouse.web.accounts.service.cic.statements.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.companieshouse.api.model.accounts.cic.statements.CicStatementsApi;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.cic.statements.TransferOfAssets;
import uk.gov.companieshouse.web.accounts.service.cic.statements.CicStatementsService;
import uk.gov.companieshouse.web.accounts.service.cic.statements.TransferOfAssetsService;
import uk.gov.companieshouse.web.accounts.transformer.cic.CicStatementsTransformer;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

@Service
public class TransferOfAssetsServiceImpl implements
    TransferOfAssetsService {
    @Autowired
    private CicStatementsService cicStatementsService;

    @Autowired
    private CicStatementsTransformer cicStatementsTransformer;

    /**
     * {@inheritDoc}
     */
    @Override
    public TransferOfAssets getTransferOfAssets(String transactionId,
        String companyAccountsId) throws ServiceException {
        CicStatementsApi cicStatementsApi =
            cicStatementsService.getCicStatementsApi(transactionId, companyAccountsId);

        return cicStatementsTransformer.getTransferOfAssets(cicStatementsApi);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ValidationError> submitTransferOfAssets(String transactionId,
        String companyAccountsId, TransferOfAssets transferOfAssets)
        throws ServiceException {
        CicStatementsApi cicStatementsApi =
            cicStatementsService.getCicStatementsApi(transactionId, companyAccountsId);

        cicStatementsTransformer
            .setTransferOfAssets(transferOfAssets, cicStatementsApi);
        return cicStatementsService
            .updateCicStatementsApi(transactionId, companyAccountsId, cicStatementsApi);

    }

}
