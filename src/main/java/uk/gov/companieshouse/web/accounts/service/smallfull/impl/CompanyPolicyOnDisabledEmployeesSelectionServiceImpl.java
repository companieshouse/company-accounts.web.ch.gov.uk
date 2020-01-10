package uk.gov.companieshouse.web.accounts.service.smallfull.impl;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.companieshouse.api.model.accounts.directorsreport.StatementsApi;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.directorsreport.CompanyPolicyOnDisabledEmployeesSelection;
import uk.gov.companieshouse.web.accounts.service.smallfull.CompanyPolicyOnDisabledEmployeesSelectionService;
import uk.gov.companieshouse.web.accounts.service.smallfull.DirectorsReportStatementsService;

@Service
public class CompanyPolicyOnDisabledEmployeesSelectionServiceImpl implements CompanyPolicyOnDisabledEmployeesSelectionService {

    @Autowired
    private DirectorsReportStatementsService directorsReportStatementsService;

    @Override
    public CompanyPolicyOnDisabledEmployeesSelection getCompanyPolicyOnDisabledEmployeesSelection(String transactionId,
            String companyAccountsId) throws ServiceException {

        CompanyPolicyOnDisabledEmployeesSelection companyPolicyOnDisabledEmployees = new CompanyPolicyOnDisabledEmployeesSelection();

        if (Optional.ofNullable(directorsReportStatementsService.getDirectorsReportStatements(transactionId, companyAccountsId))
                .map(StatementsApi::getCompanyPolicyOnDisabledEmployees)
                .isPresent()) {

            companyPolicyOnDisabledEmployees.setHasCompanyPolicyOnDisabledEmployees(true);
        }

        return companyPolicyOnDisabledEmployees;
    }

    @Override
    public void submitCompanyPolicyOnDisabledEmployeesSelection(String transactionId, String companyAccountsId,
            CompanyPolicyOnDisabledEmployeesSelection companyPolicyOnDisabledEmployees) throws ServiceException {

        if (!companyPolicyOnDisabledEmployees.getHasCompanyPolicyOnDisabledEmployees()) {

            StatementsApi statementsApi =
                    directorsReportStatementsService
                            .getDirectorsReportStatements(transactionId, companyAccountsId);

            if (statementsApi != null) {

                if (hasOtherStatements(statementsApi)) {
                    statementsApi.setCompanyPolicyOnDisabledEmployees(null);
                    directorsReportStatementsService.updateDirectorsReportStatements(transactionId, companyAccountsId, statementsApi);
                } else {
                    directorsReportStatementsService.deleteDirectorsReportStatements(transactionId, companyAccountsId);
                }
            }
        }
    }

    private boolean hasOtherStatements(StatementsApi statementsApi) {

        return Stream.of(statementsApi.getAdditionalInformation(),
                         statementsApi.getPoliticalAndCharitableDonations(),
                         statementsApi.getPrincipalActivities())
                .anyMatch(Objects::nonNull);
    }
}
