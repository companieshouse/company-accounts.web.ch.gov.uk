package uk.gov.companieshouse.web.accounts.service.navigation.success;

import org.springframework.web.bind.annotation.RequestMapping;
import uk.gov.companieshouse.web.accounts.annotation.NextController;
import uk.gov.companieshouse.web.accounts.annotation.PreviousController;
import uk.gov.companieshouse.web.accounts.controller.BaseController;
import uk.gov.companieshouse.web.accounts.controller.ConditionalController;

/**
 * Mock controller class for success scenario testing of navigation.
 */
@NextController(MockSuccessJourneyControllerThree.class)
@PreviousController(MockSuccessJourneyControllerOne.class)
@RequestMapping("/mock-success-journey-controller-two/{companyNumber}/{transactionId}/{companyAccountsId}")
public class MockSuccessJourneyControllerTwo extends BaseController implements ConditionalController {

    @Override
    protected String getTemplateName() {
        return null;
    }

    @Override
    public boolean willRender(String companyNumber, String transactionId, String companyAccountsId) {
        return false;
    }
}
