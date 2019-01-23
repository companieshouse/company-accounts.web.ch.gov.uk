package uk.gov.companieshouse.web.accounts.service.navigation.failure;

import uk.gov.companieshouse.web.accounts.annotation.NextController;
import uk.gov.companieshouse.web.accounts.controller.BaseController;
import uk.gov.companieshouse.web.accounts.service.navigation.NavigatorServiceTests;

/**
 * Mock controller class for testing missing navigation annotation {@code RequestMapping}
 * when attempting to obtain the previous controller in the journey.
 *
 * @see NavigatorServiceTests
 */
@NextController(MockControllerTwo.class)
public class MockControllerOne extends BaseController {

    @Override
    protected String getTemplateName() {
        return null;
    }
}
