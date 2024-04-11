package uk.gov.companieshouse.web.accounts.service.smallfull.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.companieshouse.api.model.accounts.directorsreport.StatementsApi;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.directorsreport.AdditionalInformation;
import uk.gov.companieshouse.web.accounts.service.smallfull.AdditionalInformationService;
import uk.gov.companieshouse.web.accounts.service.smallfull.DirectorsReportStatementsService;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.directorsreport.DirectorsReportStatementsTransformer;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

@Service
public class AdditionalInformationServiceImpl implements AdditionalInformationService {
    @Autowired
    private DirectorsReportStatementsService directorsReportStatementsService;

    @Autowired
    private DirectorsReportStatementsTransformer directorsReportStatementsTransformer;

    @Override
    public AdditionalInformation getAdditionalInformation(String transactionId,
            String companyAccountsId) throws ServiceException {
        StatementsApi statementsApi =
                directorsReportStatementsService.getDirectorsReportStatements(transactionId, companyAccountsId);

        return directorsReportStatementsTransformer.getAdditionalInformation(statementsApi);
    }

    @Override
    public List<ValidationError> submitAdditionalInformation(String transactionId,
            String companyAccountsId, AdditionalInformation additionalInformation)
            throws ServiceException {
        StatementsApi statementsApi =
                directorsReportStatementsService.getDirectorsReportStatements(transactionId, companyAccountsId);

        if (statementsApi == null) {
            statementsApi = new StatementsApi();
            directorsReportStatementsTransformer.setAdditionalInformation(statementsApi, additionalInformation);
            return directorsReportStatementsService
                    .createDirectorsReportStatements(transactionId, companyAccountsId, statementsApi);
        } else {
            directorsReportStatementsTransformer.setAdditionalInformation(statementsApi, additionalInformation);
            return directorsReportStatementsService
                    .updateDirectorsReportStatements(transactionId, companyAccountsId, statementsApi);
        }
    }
}
