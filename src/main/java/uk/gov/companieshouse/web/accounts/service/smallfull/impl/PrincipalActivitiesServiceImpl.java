package uk.gov.companieshouse.web.accounts.service.smallfull.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.companieshouse.api.model.accounts.directorsreport.StatementsApi;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.directorsreport.PrincipalActivities;
import uk.gov.companieshouse.web.accounts.service.smallfull.PrincipalActivitiesService;
import uk.gov.companieshouse.web.accounts.service.smallfull.DirectorsReportStatementsService;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.directorsreport.DirectorsReportStatementsTransformer;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

@Service
public class PrincipalActivitiesServiceImpl implements PrincipalActivitiesService {

    @Autowired
    private DirectorsReportStatementsService directorsReportStatementsService;

    @Autowired
    private DirectorsReportStatementsTransformer directorsReportStatementsTransformer;

    @Override
    public PrincipalActivities getPrincipalActivities(String transactionId,
            String companyAccountsId) throws ServiceException {

        StatementsApi statementsApi =
                directorsReportStatementsService.getDirectorsReportStatements(transactionId, companyAccountsId);

        return directorsReportStatementsTransformer.getPrincipalActivities(statementsApi);
    }

    @Override
    public List<ValidationError> submitPrincipalActivities(String transactionId,
            String companyAccountsId, PrincipalActivities principalActivities)
            throws ServiceException {

        StatementsApi statementsApi =
                directorsReportStatementsService.getDirectorsReportStatements(transactionId, companyAccountsId);

        if (statementsApi == null) {

            statementsApi = new StatementsApi();
            directorsReportStatementsTransformer.setPrincipalActivities(statementsApi, principalActivities);
            return directorsReportStatementsService
                    .createDirectorsReportStatements(transactionId, companyAccountsId, statementsApi);
        } else {

            directorsReportStatementsTransformer.setPrincipalActivities(statementsApi, principalActivities);
            return directorsReportStatementsService
                    .updateDirectorsReportStatements(transactionId, companyAccountsId, statementsApi);
        }
    }
}
