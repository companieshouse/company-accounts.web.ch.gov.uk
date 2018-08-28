package uk.gov.companieshouse.web.accounts.controller.smallfull;

import com.google.api.client.util.DateTime;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import uk.gov.companieshouse.api.error.ApiErrorResponseException;
import uk.gov.companieshouse.api.model.accounts.CompanyAccountsApi;
import uk.gov.companieshouse.api.model.company.CompanyProfileApi;
import uk.gov.companieshouse.api.model.transaction.Transaction;
import uk.gov.companieshouse.web.accounts.annotation.NextController;
import uk.gov.companieshouse.web.accounts.controller.BaseController;
import uk.gov.companieshouse.web.accounts.exception.MissingAnnotationException;
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
    public String postStepsToComplete(@PathVariable String companyNumber) {

        try {
            String transactionId = transactionService.createTransaction(companyNumber);

            CompanyProfileApi companyProfile = companyService.getCompanyProfile(companyNumber);
            DateTime periodEndOn = companyProfile.getAccounts().getNextAccounts().getPeriodEndOn();

            String companyAccountsId = companyAccountsService.createCompanyAccounts(transactionId, periodEndOn);

            return Navigator.getNextControllerRedirect(this.getClass(), companyNumber, transactionId, companyAccountsId);

        } catch (ApiErrorResponseException e) {
            // TODO: handle ApiErrorResponseExceptions (SFA-594)
            LOGGER.error(e);
            return "error";
        }
    }
}
