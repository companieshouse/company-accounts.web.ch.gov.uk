package uk.gov.companieshouse.web.accounts.util.navigator.failure;

import org.springframework.web.bind.annotation.RequestMapping;
import uk.gov.companieshouse.web.accounts.annotation.NextController;
import uk.gov.companieshouse.web.accounts.annotation.PreviousController;
import uk.gov.companieshouse.web.accounts.controller.BaseController;
import uk.gov.companieshouse.web.accounts.util.navigator.NavigatorTests;

/**
 * Mock controller class.
 *
 * @see NavigatorTests
 */
@NextController(MockControllerSix.class)
@PreviousController(MockControllerFour.class)
@RequestMapping("/mock-controller-five")
public class MockControllerFive extends BaseController {

    @Override
    protected String getTemplateName() {
        return null;
    }
}
