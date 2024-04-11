package uk.gov.companieshouse.web.accounts.service.smallfull.impl;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.companieshouse.api.model.accounts.directorsreport.StatementsApi;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.directorsreport.AdditionalInformationSelection;
import uk.gov.companieshouse.web.accounts.service.smallfull.AdditionalInformationSelectionService;
import uk.gov.companieshouse.web.accounts.service.smallfull.DirectorsReportStatementsService;

@Service
public class AdditionalInformationSelectionServiceImpl implements AdditionalInformationSelectionService {
    @Autowired
    private DirectorsReportStatementsService directorsReportStatementsService;

    @Override
    public AdditionalInformationSelection getAdditionalInformationSelection(String transactionId,
            String companyAccountsId) throws ServiceException {
        AdditionalInformationSelection additionalInformationSelection = new AdditionalInformationSelection();

        if (Optional.ofNullable(directorsReportStatementsService.getDirectorsReportStatements(transactionId, companyAccountsId))
                .map(StatementsApi::getAdditionalInformation)
                .isPresent()) {
            additionalInformationSelection.setHasAdditionalInformation(true);
        }

        return additionalInformationSelection;
    }

    @Override
    public void submitAdditionalInformationSelection(String transactionId, String companyAccountsId,
            AdditionalInformationSelection additionalInformationSelection) throws ServiceException {
        if (Boolean.FALSE.equals(additionalInformationSelection.getHasAdditionalInformation())) {
            StatementsApi statementsApi =
                    directorsReportStatementsService
                            .getDirectorsReportStatements(transactionId, companyAccountsId);

            if (statementsApi != null) {
                if (hasOtherStatements(statementsApi)) {
                    statementsApi.setAdditionalInformation(null);
                    directorsReportStatementsService.updateDirectorsReportStatements(transactionId, companyAccountsId, statementsApi);
                } else {
                    directorsReportStatementsService.deleteDirectorsReportStatements(transactionId, companyAccountsId);
                }
            }
        }
    }

    private boolean hasOtherStatements(StatementsApi statementsApi) {
        return Stream.of(statementsApi.getCompanyPolicyOnDisabledEmployees(),
                         statementsApi.getPoliticalAndCharitableDonations(),
                         statementsApi.getPrincipalActivities())
                .anyMatch(Objects::nonNull);
    }
}
