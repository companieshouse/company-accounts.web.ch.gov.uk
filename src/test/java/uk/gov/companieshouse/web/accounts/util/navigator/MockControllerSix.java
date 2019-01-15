package uk.gov.companieshouse.web.accounts.util.navigator;

import org.springframework.web.bind.annotation.RequestMapping;
import uk.gov.companieshouse.web.accounts.annotation.PreviousController;
import uk.gov.companieshouse.web.accounts.controller.BaseController;

/**
 * Mock controller class for testing missing {@code RequestMapping} value
 * when searching forwards or backwards in the controller chain.
 *
 * @see NavigatorTests
 */
@PreviousController(MockControllerFive.class)
@RequestMapping()
public class MockControllerSix extends BaseController {

    @Override
    protected String getTemplateName() {
        return null;
    }
}

