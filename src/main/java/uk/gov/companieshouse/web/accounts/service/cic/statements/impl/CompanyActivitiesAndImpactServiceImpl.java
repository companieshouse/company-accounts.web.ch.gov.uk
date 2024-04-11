package uk.gov.companieshouse.web.accounts.service.cic.statements.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.companieshouse.api.model.accounts.cic.statements.CicStatementsApi;
import uk.gov.companieshouse.api.model.accounts.cic.statements.ReportStatementsApi;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.cic.statements.CompanyActivitiesAndImpact;
import uk.gov.companieshouse.web.accounts.service.cic.statements.CicStatementsService;
import uk.gov.companieshouse.web.accounts.service.cic.statements.CompanyActivitiesAndImpactService;
import uk.gov.companieshouse.web.accounts.transformer.cic.CicStatementsTransformer;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

@Service
public class CompanyActivitiesAndImpactServiceImpl implements CompanyActivitiesAndImpactService {
    @Autowired
    private CicStatementsService cicStatementsService;

    @Autowired
    private CicStatementsTransformer cicStatementsTransformer;

    /**
     * {@inheritDoc}
     */
    @Override
    public CompanyActivitiesAndImpact getCompanyActivitiesAndImpact(String transactionId,
            String companyAccountsId) throws ServiceException {
        CicStatementsApi cicStatementsApi =
                cicStatementsService.getCicStatementsApi(transactionId, companyAccountsId);

        return cicStatementsTransformer.getCompanyActivitiesAndImpact(cicStatementsApi);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ValidationError> submitCompanyActivitiesAndImpact(String transactionId,
            String companyAccountsId, CompanyActivitiesAndImpact companyActivitiesAndImpact)
            throws ServiceException {
        CicStatementsApi cicStatementsApi =
                cicStatementsService.getCicStatementsApi(transactionId, companyAccountsId);

        if (cicStatementsApi == null) {
            cicStatementsApi = createNewCicStatementsApi();
            cicStatementsTransformer.setCompanyActivitiesAndImpact(companyActivitiesAndImpact, cicStatementsApi);
            return cicStatementsService.createCicStatementsApi(transactionId, companyAccountsId, cicStatementsApi);

        } else {
            cicStatementsTransformer.setCompanyActivitiesAndImpact(companyActivitiesAndImpact, cicStatementsApi);
            return cicStatementsService.updateCicStatementsApi(transactionId, companyAccountsId, cicStatementsApi);
        }
    }

    private CicStatementsApi createNewCicStatementsApi() {
        CicStatementsApi cicStatementsApi = new CicStatementsApi();
        cicStatementsApi.setReportStatements(new ReportStatementsApi());
        cicStatementsApi.setHasCompletedReportStatements(false);
        return cicStatementsApi;
    }
}
