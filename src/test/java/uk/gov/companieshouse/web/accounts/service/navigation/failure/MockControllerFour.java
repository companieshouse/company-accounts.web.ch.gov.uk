package uk.gov.companieshouse.web.accounts.service.navigation.failure;

import uk.gov.companieshouse.web.accounts.annotation.NextController;
import uk.gov.companieshouse.web.accounts.annotation.PreviousController;
import uk.gov.companieshouse.web.accounts.controller.BaseController;
import uk.gov.companieshouse.web.accounts.controller.ConditionalController;
import uk.gov.companieshouse.web.accounts.service.navigation.NavigatorServiceTests;

/**
 * Mock conditional controller class for testing missing expected number of
 * path variables.
 *
 * @see NavigatorServiceTests
 */
@NextController(MockControllerFive.class)
@PreviousController(MockControllerThree.class)
public class MockControllerFour extends BaseController implements ConditionalController {

    @Override
    protected String getTemplateName() {
        return null;
    }

    @Override
    public boolean willRender(String companyNumber, String transactionId, String companyAccountsId) {
        return false;
    }
}
