package uk.gov.companieshouse.web.accounts.controller.smallfull;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import uk.gov.companieshouse.web.accounts.annotation.NextController;
import uk.gov.companieshouse.web.accounts.annotation.PreviousController;
import uk.gov.companieshouse.web.accounts.controller.BaseController;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.service.companyaccounts.CompanyAccountsService;
import uk.gov.companieshouse.web.accounts.service.smallfull.SmallFullService;
import uk.gov.companieshouse.web.accounts.service.smallfull.StatementsService;
import uk.gov.companieshouse.web.accounts.service.transaction.TransactionService;

@Controller
@NextController(BalanceSheetController.class)
@PreviousController(CriteriaController.class)
@RequestMapping("/company/{companyNumber}/small-full/steps-to-complete")
public class StepsToCompleteController extends BaseController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private SmallFullService smallFullService;

    @Autowired
    private CompanyAccountsService companyAccountsService;

    @Autowired
    private StatementsService statementsService;

    @Override
    protected String getTemplateName() {
        return "smallfull/stepsToComplete";
    }

    @GetMapping
    public String getStepsToComplete(@PathVariable String companyNumber,
                                     Model model) {

        addBackPageAttributeToModel(model, companyNumber);

        return getTemplateName();
    }

    @PostMapping
    public String postStepsToComplete(@PathVariable String companyNumber,
                                      HttpServletRequest request) {

        try {
            String transactionId = transactionService.createTransaction(companyNumber);

            String companyAccountsId = companyAccountsService.createCompanyAccounts(transactionId);

            smallFullService.createSmallFullAccounts(transactionId, companyAccountsId);

            statementsService.createBalanceSheetStatementsResource(transactionId, companyAccountsId);

            transactionService.createResumeLink(companyNumber, transactionId, companyAccountsId);

            return navigatorService.getNextControllerRedirect(this.getClass(), companyNumber, transactionId, companyAccountsId);

        } catch (ServiceException e) {

            LOGGER.errorRequest(request, e.getMessage(), e);
            return ERROR_VIEW;
        }
    }
}
