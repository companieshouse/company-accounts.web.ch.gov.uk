package uk.gov.companieshouse.web.accounts.controller.cic;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import uk.gov.companieshouse.web.accounts.annotation.NextController;
import uk.gov.companieshouse.web.accounts.annotation.PreviousController;
import uk.gov.companieshouse.web.accounts.controller.BaseController;
import uk.gov.companieshouse.web.accounts.controller.BranchController;
import uk.gov.companieshouse.web.accounts.controller.smallfull.StepsToCompleteController;

@Controller
@PreviousController(CicApprovalController.class)
@NextController(StepsToCompleteController.class)
@RequestMapping("/company/{companyNumber}/transaction/{transactionId}/company-accounts/{companyAccountsId}/cic/accounts-start")
public class AccountStartController extends BaseController implements BranchController {
    private static final String TRANSACTION_ID = "transaction_id";
    private static final String COMPANY_ACCOUNTS_ID = "company_accounts_id";

    @Override
    protected String getTemplateName() {
        return "cic/cicAccountStart";
    }

    @GetMapping
    public String getApproval(@PathVariable String companyNumber,
        @PathVariable String transactionId,
        @PathVariable String companyAccountsId,
        Model model) {
        addBackPageAttributeToModel(model, companyNumber, transactionId, companyAccountsId);

        model.addAttribute(TRANSACTION_ID, transactionId);
        model.addAttribute(COMPANY_ACCOUNTS_ID, companyAccountsId);

        return getTemplateName();
    }

    @PostMapping
    public String postApproval(@PathVariable String companyNumber,
        @PathVariable String transactionId,
        @PathVariable String companyAccountsId,
        Model model) {
        addBackPageAttributeToModel(model, companyNumber, transactionId, companyAccountsId);

        return navigatorService.getNextControllerRedirect(this.getClass(), companyNumber, transactionId, companyAccountsId);

    }

    @Override
    public boolean shouldBranch(String ... pathVars) {
        return pathVars.length == 3;
    }
}
