package uk.gov.companieshouse.web.accounts.service.smallfull.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.companieshouse.api.model.accounts.smallfull.loanstodirectors.AdditionalInformationApi;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.loanstodirectors.AdditionalInformationSelection;
import uk.gov.companieshouse.web.accounts.service.smallfull.AdditionalInformationSelectionService;
import uk.gov.companieshouse.web.accounts.service.smallfull.LoansToDirectorsAdditionalInformationService;

import java.util.Optional;

@Service
public class LoansToDirectorsAdditionalInformationSelectionServiceImpl implements AdditionalInformationSelectionService<AdditionalInformationSelection> {

    @Autowired
    private LoansToDirectorsAdditionalInformationService additionalInformationService;

    @Override
    public AdditionalInformationSelection getAdditionalInformationSelection(String transactionId,
                                                                            String companyAccountsId) throws ServiceException {

        AdditionalInformationSelection additionalInformationSelection = new AdditionalInformationSelection();
        if (Optional.ofNullable(additionalInformationService.getAdditionalInformation(transactionId, companyAccountsId))
                .map(AdditionalInformationApi::getDetails)
                .isPresent()) {

            additionalInformationSelection.setHasAdditionalInformation(true);
        }

        return additionalInformationSelection;
    }

    @Override
    public void submitAdditionalInformationSelection(String transactionId, String companyAccountsId,
                                                     AdditionalInformationSelection additionalInformationSelection) throws ServiceException {

        if (!additionalInformationSelection.getHasAdditionalInformation()) {

            AdditionalInformationApi additionalInformationApi =
                    additionalInformationService.getAdditionalInformation(transactionId, companyAccountsId);

            if (additionalInformationApi != null)

                if (StringUtils.isNotBlank(additionalInformationApi.getDetails())) {

                    additionalInformationApi.setDetails(null);
                    additionalInformationService.updateAdditionalInformation(transactionId, companyAccountsId, additionalInformationApi);
                } else {
                    additionalInformationService.deleteAdditionalInformation(transactionId, companyAccountsId);
                }
        }
    }
}
