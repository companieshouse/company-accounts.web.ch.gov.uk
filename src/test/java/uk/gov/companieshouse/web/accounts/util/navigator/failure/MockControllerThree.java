package uk.gov.companieshouse.web.accounts.util.navigator.failure;

import uk.gov.companieshouse.web.accounts.controller.BaseController;
import uk.gov.companieshouse.web.accounts.util.navigator.NavigatorTests;

/**
 * Mock controller class for testing missing navigation annotations {@code NextController}
 * and {@code PreviousController}.
 *
 * @see NavigatorTests
 */
public class MockControllerThree extends BaseController {

    @Override
    protected String getTemplateName() {
        return null;
    }
}
