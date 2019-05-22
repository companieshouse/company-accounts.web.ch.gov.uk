package uk.gov.companieshouse.web.accounts.service.cic.statements.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.companieshouse.api.model.accounts.cic.statements.CicStatementsApi;
import uk.gov.companieshouse.api.model.accounts.cic.statements.ReportStatementsApi;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.cic.statements.ConsultationWithStakeholders;
import uk.gov.companieshouse.web.accounts.service.cic.statements.CicStatementsService;
import uk.gov.companieshouse.web.accounts.service.cic.statements.ConsultationWithStakeholdersService;
import uk.gov.companieshouse.web.accounts.transformer.cic.CicStatementsTransformer;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

@Service
public class ConsultationWithStakeholdersServiceImpl implements
    ConsultationWithStakeholdersService {

    @Autowired
    private CicStatementsService cicStatementsService;

    @Autowired
    private CicStatementsTransformer cicStatementsTransformer;

    /**
     * {@inheritDoc}
     */
    @Override
    public ConsultationWithStakeholders getConsultationWithStakeholders(String transactionId,
        String companyAccountsId) throws ServiceException {

        CicStatementsApi cicStatementsApi =
            cicStatementsService.getCicStatementsApi(transactionId, companyAccountsId);

        return cicStatementsTransformer.getConsultationWithStakeholders(cicStatementsApi);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ValidationError> submitConsultationWithStakeholders(String transactionId,
        String companyAccountsId, ConsultationWithStakeholders consultationWithStakeholders)
        throws ServiceException {

        CicStatementsApi cicStatementsApi =
            cicStatementsService.getCicStatementsApi(transactionId, companyAccountsId);

        cicStatementsTransformer
            .setConsultationWithStakeholders(consultationWithStakeholders, cicStatementsApi);
        return cicStatementsService
            .updateCicStatementsApi(transactionId, companyAccountsId, cicStatementsApi);

    }
}
