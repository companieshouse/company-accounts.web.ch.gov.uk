package uk.gov.companieshouse.web.accounts.service.cic.statements.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.companieshouse.api.model.accounts.cic.statements.CicStatementsApi;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.cic.statements.DirectorsRemuneration;
import uk.gov.companieshouse.web.accounts.service.cic.statements.CicStatementsService;
import uk.gov.companieshouse.web.accounts.service.cic.statements.DirectorsRemunerationService;
import uk.gov.companieshouse.web.accounts.transformer.cic.CicStatementsTransformer;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

@Service
public class DirectorsRemunerationServiceImpl implements
    DirectorsRemunerationService {
    @Autowired
    private CicStatementsService cicStatementsService;

    @Autowired
    private CicStatementsTransformer cicStatementsTransformer;

    /**
     * {@inheritDoc}
     */
    @Override
    public DirectorsRemuneration getDirectorsRemuneration(String transactionId,
        String companyAccountsId) throws ServiceException {
        CicStatementsApi cicStatementsApi =
            cicStatementsService.getCicStatementsApi(transactionId, companyAccountsId);

        return cicStatementsTransformer.getDirectorsRemuneration(cicStatementsApi);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ValidationError> submitDirectorsRemuneration(String transactionId,
        String companyAccountsId, DirectorsRemuneration directorsRemuneration)
        throws ServiceException {
        CicStatementsApi cicStatementsApi =
            cicStatementsService.getCicStatementsApi(transactionId, companyAccountsId);

        cicStatementsTransformer
            .setDirectorsRemuneration(directorsRemuneration, cicStatementsApi);
        return cicStatementsService
            .updateCicStatementsApi(transactionId, companyAccountsId, cicStatementsApi);

    }

}