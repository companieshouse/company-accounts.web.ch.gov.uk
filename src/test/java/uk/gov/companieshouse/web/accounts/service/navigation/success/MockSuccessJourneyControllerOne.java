package uk.gov.companieshouse.web.accounts.service.navigation.success;

import org.springframework.web.bind.annotation.RequestMapping;
import uk.gov.companieshouse.web.accounts.annotation.NextController;
import uk.gov.companieshouse.web.accounts.controller.BaseController;

/**
 * Mock controller class for success scenario testing of navigation.
 */
@NextController(MockSuccessJourneyControllerTwo.class)
@RequestMapping("/mock-success-journey-controller-one")
public class MockSuccessJourneyControllerOne extends BaseController {

    @Override
    protected String getTemplateName() {
        return null;
    }
}
