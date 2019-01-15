package uk.gov.companieshouse.web.accounts.util.navigator;

import uk.gov.companieshouse.web.accounts.controller.BaseController;

/**
 * Mock controller class for testing missing navigation annotations {@code NextController}
 * and {@code PreviousController}.
 */
public class MockControllerThree extends BaseController {

    @Override
    protected String getTemplateName() {
        return null;
    }
}
