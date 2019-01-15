package uk.gov.companieshouse.web.accounts.util.navigator;

import org.springframework.web.bind.annotation.RequestMapping;
import uk.gov.companieshouse.web.accounts.annotation.PreviousController;
import uk.gov.companieshouse.web.accounts.controller.BaseController;

/**
 * Mock controller class.
 *
 * @see NavigatorTests
 */
@PreviousController(MockControllerFour.class)
@RequestMapping("/mock-controller-five")
public class MockControllerFive extends BaseController {

    @Override
    protected String getTemplateName() {
        return null;
    }
}
