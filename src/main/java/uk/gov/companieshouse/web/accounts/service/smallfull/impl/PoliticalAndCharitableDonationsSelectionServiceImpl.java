package uk.gov.companieshouse.web.accounts.service.smallfull.impl;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.companieshouse.api.model.accounts.directorsreport.StatementsApi;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.directorsreport.PoliticalAndCharitableDonationsSelection;
import uk.gov.companieshouse.web.accounts.service.smallfull.PoliticalAndCharitableDonationsSelectionService;
import uk.gov.companieshouse.web.accounts.service.smallfull.DirectorsReportStatementsService;

@Service
public class PoliticalAndCharitableDonationsSelectionServiceImpl implements PoliticalAndCharitableDonationsSelectionService {

    @Autowired
    private DirectorsReportStatementsService directorsReportStatementsService;

    @Override
    public PoliticalAndCharitableDonationsSelection getPoliticalAndCharitableDonationsSelection(String transactionId,
            String companyAccountsId) throws ServiceException {

        PoliticalAndCharitableDonationsSelection politicalAndCharitableDonations = new PoliticalAndCharitableDonationsSelection();

        if (Optional.ofNullable(directorsReportStatementsService.getDirectorsReportStatements(transactionId, companyAccountsId))
                .map(StatementsApi::getPoliticalAndCharitableDonations)
                .isPresent()) {

            politicalAndCharitableDonations.setHasPoliticalAndCharitableDonations(true);
        }

        return politicalAndCharitableDonations;
    }

    @Override
    public void submitPoliticalAndCharitableDonationsSelection(String transactionId, String companyAccountsId,
            PoliticalAndCharitableDonationsSelection politicalAndCharitableDonations) throws ServiceException {

        if (Boolean.FALSE.equals(politicalAndCharitableDonations.getHasPoliticalAndCharitableDonations())) {

            StatementsApi statementsApi =
                    directorsReportStatementsService
                            .getDirectorsReportStatements(transactionId, companyAccountsId);

            if (statementsApi != null) {

                if (hasOtherStatements(statementsApi)) {
                    statementsApi.setPoliticalAndCharitableDonations(null);
                    directorsReportStatementsService.updateDirectorsReportStatements(transactionId, companyAccountsId, statementsApi);
                } else {
                    directorsReportStatementsService.deleteDirectorsReportStatements(transactionId, companyAccountsId);
                }
            }
        }
    }

    private boolean hasOtherStatements(StatementsApi statementsApi) {

        return Stream.of(statementsApi.getAdditionalInformation(),
                         statementsApi.getCompanyPolicyOnDisabledEmployees(),
                         statementsApi.getPrincipalActivities())
                .anyMatch(Objects::nonNull);
    }
}
