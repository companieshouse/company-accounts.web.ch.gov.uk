package uk.gov.companieshouse.web.accounts.service.smallfull.impl;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.companieshouse.api.model.accounts.directorsreport.StatementsApi;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.directorsreport.PrincipalActivitiesSelection;
import uk.gov.companieshouse.web.accounts.service.smallfull.DirectorsReportStatementsService;
import uk.gov.companieshouse.web.accounts.service.smallfull.PrincipalActivitiesSelectionService;

@Service
public class PrincipalActivitiesSelectionServiceImpl implements PrincipalActivitiesSelectionService {

    @Autowired
    private DirectorsReportStatementsService directorsReportStatementsService;

    @Override
    public PrincipalActivitiesSelection getPrincipalActivitiesSelection(String transactionId,
            String companyAccountsId) throws ServiceException {

        PrincipalActivitiesSelection principalActivitiesSelection = new PrincipalActivitiesSelection();

        if (Optional.ofNullable(directorsReportStatementsService.getDirectorsReportStatements(transactionId, companyAccountsId))
                .map(StatementsApi::getPrincipalActivities)
                .isPresent()) {

            principalActivitiesSelection.setHasPrincipalActivities(true);
        }

        return principalActivitiesSelection;
    }

    @Override
    public void submitPrincipalActivitiesSelection(String transactionId, String companyAccountsId,
            PrincipalActivitiesSelection principalActivitiesSelection) throws ServiceException {

        if (Boolean.FALSE.equals(principalActivitiesSelection.getHasPrincipalActivities())) {

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
