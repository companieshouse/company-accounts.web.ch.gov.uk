package uk.gov.companieshouse.web.accounts.controller.smallfull;

import java.util.Optional;
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
@RequestMapping({"/company/{companyNumber}/small-full/steps-to-complete", "/company/{companyNumber}/transaction/{transactionId}/company-accounts/{companyAccountsId}/small-full/steps-to-complete"})
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
                                     @PathVariable Optional<String> transactionId,
                                     @PathVariable Optional<String> companyAccountsId,
                                     Model model) {

        addBackPageAttributeToModel(model, companyNumber);

        return getTemplateName();
    }

    @PostMapping
    public String postStepsToComplete(@PathVariable String companyNumber,
                                      @PathVariable Optional<String> transactionId,
                                      @PathVariable Optional<String> companyAccountsId,
                                      HttpServletRequest request) {

        boolean isExistingTransaction = false;
        String transactionID = "";
        String companyAccountsID = "";

        if (transactionId.isPresent() && companyAccountsId.isPresent()) {
            transactionID = transactionId.get();
            companyAccountsID = companyAccountsId.get();
            isExistingTransaction = true;
        }

        if (!isExistingTransaction) {
            try {
                transactionID = transactionService.createTransaction(companyNumber);
                companyAccountsID = companyAccountsService.createCompanyAccounts(transactionID);

            } catch (ServiceException e) {

                LOGGER.errorRequest(request, e.getMessage(), e);
                return ERROR_VIEW;
            }
        }

        try {

            smallFullService.createSmallFullAccounts(transactionID, companyAccountsID);
            statementsService.createBalanceSheetStatementsResource(transactionID, companyAccountsID);
            transactionService.createResumeLink(companyNumber, transactionID, companyAccountsID);

            return navigatorService.getNextControllerRedirect(this.getClass(), companyNumber, transactionID, companyAccountsID);
        } catch (ServiceException e) {

            LOGGER.errorRequest(request, e.getMessage(), e);
            return ERROR_VIEW;
        }
    }
}
