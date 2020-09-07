package uk.gov.companieshouse.web.accounts.transformer.smallfull.loanstodirectors;

import org.springframework.stereotype.Component;
import uk.gov.companieshouse.api.model.accounts.smallfull.loanstodirectors.AdditionalInformationApi;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.loanstodirectors.LoansToDirectorsAdditionalInfo;

@Component
public class LoanToDirectorsAdditionalInfoTransformer {

    public AdditionalInformationApi mapLoansToDirectorsAdditonalInfoToApi(LoansToDirectorsAdditionalInfo additionalInformation) {

        AdditionalInformationApi additionalInformationApi = new AdditionalInformationApi();

        if(additionalInformation.getAdditionalInfoDetails() != null) {
            additionalInformationApi.setDetails(additionalInformation.getAdditionalInfoDetails());
        }
        return additionalInformationApi;
    }

    public LoansToDirectorsAdditionalInfo mapLoansToDirectorsAdditionalInfoToWeb(AdditionalInformationApi additionalInformationApi) {

        LoansToDirectorsAdditionalInfo additionalInformation = new LoansToDirectorsAdditionalInfo();

        if(additionalInformationApi.getDetails() != null) {
            additionalInformation.setAdditionalInfoDetails(additionalInformationApi.getDetails());
        }
        return additionalInformation;
    }
}
