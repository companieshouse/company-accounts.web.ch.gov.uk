package uk.gov.companieshouse.web.accounts.service.smallfull.impl;

import java.util.Objects;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.companieshouse.api.model.accounts.directorsreport.StatementsApi;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.directorsreport.PrincipalActivitiesSelection;
import uk.gov.companieshouse.web.accounts.service.smallfull.PrincipalActivitiesSelectionService;
import uk.gov.companieshouse.web.accounts.service.smallfull.DirectorsReportStatementsService;

@Service
public class PrincipalActivitiesSelectionServiceImpl implements PrincipalActivitiesSelectionService {

    @Autowired
    private DirectorsReportStatementsService directorsReportStatementsService;

    @Override
    public PrincipalActivitiesSelection getPrincipalActivitiesSelection(String transactionId,
            String companyAccountsId) throws ServiceException {

        PrincipalActivitiesSelection principalActivitiesSelection = new PrincipalActivitiesSelection();

        if (directorsReportStatementsService.getDirectorsReportStatements(transactionId, companyAccountsId) != null) {
            principalActivitiesSelection.setHasPrincipalActivities(true);
        }

        return principalActivitiesSelection;
    }

    @Override
    public void submitPrincipalActivities(String transactionId, String companyAccountsId,
            PrincipalActivitiesSelection principalActivitiesSelection) throws ServiceException {

        if (!principalActivitiesSelection.getHasPrincipalActivities()) {

            StatementsApi statementsApi =
                    directorsReportStatementsService
                            .getDirectorsReportStatements(transactionId, companyAccountsId);

            if (statementsApi != null) {

                if (hasOtherStatements(statementsApi)) {
                    statementsApi.setPrincipalActivities(null);
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
                         statementsApi.getPoliticalAndCharitableDonations())
                .anyMatch(Objects::nonNull);
    }
}
