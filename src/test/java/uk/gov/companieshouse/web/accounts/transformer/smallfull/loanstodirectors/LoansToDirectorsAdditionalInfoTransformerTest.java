package uk.gov.companieshouse.web.accounts.transformer.smallfull.loanstodirectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.api.model.accounts.smallfull.loanstodirectors.AdditionalInformationApi;
import uk.gov.companieshouse.web.accounts.model.loanstodirectors.LoanToAdd;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.loanstodirectors.LoansToDirectorsAdditionalInfo;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class LoansToDirectorsAdditionalInfoTransformerTest {

    @Mock
    private LoanToAdd loanToAdd;

    @InjectMocks
    private LoansToDirectorsAdditionalInfoTransformer loansToDirectorsAdditionalInfoTransformer = new LoansToDirectorsAdditionalInfoTransformer();

    private static final String ADDITIONAL_INFORMATION_DETAILS = "additionl information details";

    @Test
    @DisplayName("Get loans to directors additioal information API")
    void getDirectorApiNoDates() {

        LoansToDirectorsAdditionalInfo loansToDirectorsAdditionalInfo = new LoansToDirectorsAdditionalInfo();
        loansToDirectorsAdditionalInfo.setAdditionalInfoDetails(ADDITIONAL_INFORMATION_DETAILS);

        AdditionalInformationApi additionalInforApi = loansToDirectorsAdditionalInfoTransformer.mapLoansToDirectorsAdditonalInfoToApi(loansToDirectorsAdditionalInfo);

        assertNotNull(additionalInforApi);
        assertEquals(ADDITIONAL_INFORMATION_DETAILS, additionalInforApi.getDetails());
    }

    @Test
    @DisplayName("Get all loans")
    void getAllLoans() {

        AdditionalInformationApi additionalInformationApi = new AdditionalInformationApi();
        additionalInformationApi.setDetails(ADDITIONAL_INFORMATION_DETAILS);

        LoansToDirectorsAdditionalInfo loansToDirectorsAdditionalInfo = loansToDirectorsAdditionalInfoTransformer.mapLoansToDirectorsAdditionalInfoToWeb(additionalInformationApi);

        assertNotNull(loansToDirectorsAdditionalInfo);
        assertEquals(ADDITIONAL_INFORMATION_DETAILS, loansToDirectorsAdditionalInfo.getAdditionalInfoDetails());
    }
}