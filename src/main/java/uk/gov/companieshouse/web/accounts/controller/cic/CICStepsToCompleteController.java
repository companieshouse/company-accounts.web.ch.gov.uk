package uk.gov.companieshouse.web.accounts.controller.cic;

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
import uk.gov.companieshouse.web.accounts.controller.smallfull.StepsToCompleteController;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.service.cic.CicReportService;
import uk.gov.companieshouse.web.accounts.service.companyaccounts.CompanyAccountsService;
import uk.gov.companieshouse.web.accounts.service.transaction.TransactionService;

import javax.servlet.http.HttpServletRequest;

@Controller
@PreviousController(StepsToCompleteController.class)
@NextController(StepsToCompleteController.class)
@RequestMapping("/company/{companyNumber}/cic/steps-to-complete")
public class CICStepsToCompleteController extends BaseController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private CompanyAccountsService companyAccountsService;

    @Autowired
    private CicReportService cicReportService;

    @Override
    protected String getTemplateName() {
        return "cic/stepsToComplete";
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

            transactionService.createResumeLink(companyNumber, transactionId, companyAccountsId);

            cicReportService.createCicReport(transactionId, companyAccountsId);

            return navigatorService.getNextControllerRedirect(this.getClass(), companyNumber, transactionId, companyAccountsId);

        } catch (ServiceException e) {

            LOGGER.errorRequest(request, e.getMessage(), e);
            return ERROR_VIEW;
        }
    }
}
