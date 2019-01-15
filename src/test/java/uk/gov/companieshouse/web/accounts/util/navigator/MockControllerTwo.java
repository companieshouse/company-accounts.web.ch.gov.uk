package uk.gov.companieshouse.web.accounts.util.navigator;

import uk.gov.companieshouse.web.accounts.annotation.NextController;
import uk.gov.companieshouse.web.accounts.annotation.PreviousController;
import uk.gov.companieshouse.web.accounts.controller.BaseController;

/**
 * Mock controller class for testing missing navigation annotation {@code RequestMapping}
 * when attempting to obtain the next controller in the journey.
 *
 * @see NavigatorTests
 */
@NextController(MockControllerThree.class)
@PreviousController(MockControllerOne.class)
public class MockControllerTwo extends BaseController {

    @Override
    protected String getTemplateName() {
        return null;
    }
}
