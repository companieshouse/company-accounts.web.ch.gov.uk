package uk.gov.companieshouse.web.accounts.service.navigation.failure;

import org.springframework.web.bind.annotation.RequestMapping;
import uk.gov.companieshouse.web.accounts.annotation.PreviousController;
import uk.gov.companieshouse.web.accounts.controller.BaseController;
import uk.gov.companieshouse.web.accounts.controller.ConditionalController;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.service.navigation.NavigatorServiceTests;

/**
 * Mock conditional controller class for testing exception handling.
 *
 * @see NavigatorServiceTests
 * @see uk.gov.companieshouse.web.accounts.exception.NavigationException
 */
@RequestMapping("/mock-controller-eight")
@PreviousController(MockControllerSeven.class)
public class MockControllerEight extends BaseController implements ConditionalController {
    @Override
    protected String getTemplateName() {
        return null;
    }

    @Override
    public boolean willRender(String companyNumber, String transactionId, String companyAccountsId) throws ServiceException {
        throw new ServiceException("Test exception", null);
    }
}
