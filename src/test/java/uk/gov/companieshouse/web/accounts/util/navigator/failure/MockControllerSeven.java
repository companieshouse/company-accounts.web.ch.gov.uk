package uk.gov.companieshouse.web.accounts.util.navigator.failure;

import uk.gov.companieshouse.web.accounts.annotation.PreviousController;
import uk.gov.companieshouse.web.accounts.controller.BaseController;
import uk.gov.companieshouse.web.accounts.util.navigator.NavigatorTests;

/**
 * Mock controller class for testing missing {@code RequestMapping} value
 * when searching backwards in the controller chain.
 *
 * @see NavigatorTests
 */
@PreviousController(MockControllerSix.class)
public class MockControllerSeven extends BaseController {

    @Override
    protected String getTemplateName() {
        return null;
    }
}

