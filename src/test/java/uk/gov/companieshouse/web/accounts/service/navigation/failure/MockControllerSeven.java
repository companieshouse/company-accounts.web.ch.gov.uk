package uk.gov.companieshouse.web.accounts.service.navigation.failure;

import uk.gov.companieshouse.web.accounts.annotation.NextController;
import uk.gov.companieshouse.web.accounts.annotation.PreviousController;
import uk.gov.companieshouse.web.accounts.controller.BaseController;
import uk.gov.companieshouse.web.accounts.service.navigation.NavigatorServiceTests;

/**
 * Mock controller class for testing missing {@code RequestMapping} value
 * when searching backwards in the controller chain.
 *
 * @see NavigatorServiceTests
 */
@NextController(MockControllerEight.class)
@PreviousController(MockControllerSix.class)
public class MockControllerSeven extends BaseController {
    @Override
    protected String getTemplateName() {
        return null;
    }
}

