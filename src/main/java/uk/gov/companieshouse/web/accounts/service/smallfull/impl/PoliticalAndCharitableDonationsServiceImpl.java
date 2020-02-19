package uk.gov.companieshouse.web.accounts.service.smallfull.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.companieshouse.api.model.accounts.directorsreport.StatementsApi;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.directorsreport.PoliticalAndCharitableDonations;
import uk.gov.companieshouse.web.accounts.service.smallfull.PoliticalAndCharitableDonationsService;
import uk.gov.companieshouse.web.accounts.service.smallfull.DirectorsReportStatementsService;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.directorsreport.DirectorsReportStatementsTransformer;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

@Service
public class PoliticalAndCharitableDonationsServiceImpl implements PoliticalAndCharitableDonationsService {

    @Autowired
    private DirectorsReportStatementsService directorsReportStatementsService;

    @Autowired
    private DirectorsReportStatementsTransformer directorsReportStatementsTransformer;

    @Override
    public PoliticalAndCharitableDonations getPoliticalAndCharitableDonations(String transactionId,
            String companyAccountsId) throws ServiceException {

        StatementsApi statementsApi =
                directorsReportStatementsService.getDirectorsReportStatements(transactionId, companyAccountsId);

        return directorsReportStatementsTransformer.getPoliticalAndCharitableDonations(statementsApi);
    }

    @Override
    public List<ValidationError> submitPoliticalAndCharitableDonations(String transactionId,
            String companyAccountsId, PoliticalAndCharitableDonations politicalAndCharitableDonations)
            throws ServiceException {

        StatementsApi statementsApi =
                directorsReportStatementsService.getDirectorsReportStatements(transactionId, companyAccountsId);

        if (statementsApi == null) {

            statementsApi = new StatementsApi();
            directorsReportStatementsTransformer.setPoliticalAndCharitableDonations(statementsApi, politicalAndCharitableDonations);
            return directorsReportStatementsService
                    .createDirectorsReportStatements(transactionId, companyAccountsId, statementsApi);
        } else {

            directorsReportStatementsTransformer.setPoliticalAndCharitableDonations(statementsApi, politicalAndCharitableDonations);
            return directorsReportStatementsService
                    .updateDirectorsReportStatements(transactionId, companyAccountsId, statementsApi);
        }
    }
}
