package uk.gov.companieshouse.web.accounts.controller.smallfull;

import com.google.api.client.util.DateTime;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import uk.gov.companieshouse.api.model.company.CompanyProfileApi;
import uk.gov.companieshouse.web.accounts.annotation.NextController;
import uk.gov.companieshouse.web.accounts.controller.BaseController;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.service.company.CompanyService;
import uk.gov.companieshouse.web.accounts.service.companyaccounts.CompanyAccountsService;
import uk.gov.companieshouse.web.accounts.service.transaction.TransactionService;
import uk.gov.companieshouse.web.accounts.util.Navigator;

@Controller
@NextController(BalanceSheetController.class)
@RequestMapping("/company/{companyNumber}/small-full/steps-to-complete")
public class StepsToCompleteController extends BaseController {

    private static final String TEMPLATE = "smallfull/stepsToComplete";

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private CompanyAccountsService companyAccountsService;

    @GetMapping
    public String getStepsToComplete() {

        return TEMPLATE;
    }

    @PostMapping
    public String postStepsToComplete(@PathVariable String companyNumber,
                                      HttpServletRequest request) {

        try {
            String transactionId = transactionService.createTransaction(companyNumber);

            CompanyProfileApi companyProfile = companyService.getCompanyProfile(companyNumber);
            DateTime periodEndOn = companyProfile.getAccounts().getNextAccounts().getPeriodEndOn();

            String companyAccountsId = companyAccountsService.createCompanyAccounts(transactionId, periodEndOn);

            companyAccountsService.createSmallFullAccounts(transactionId, companyAccountsId);

            return Navigator.getNextControllerRedirect(this.getClass(), companyNumber, transactionId, companyAccountsId);

        } catch (ServiceException e) {

            LOGGER.errorRequest(request, e.getMessage(), e);
            return ERROR_VIEW;
        }
    }
}
