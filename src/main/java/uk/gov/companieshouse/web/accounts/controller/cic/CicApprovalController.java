package uk.gov.companieshouse.web.accounts.controller.cic;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import uk.gov.companieshouse.web.accounts.annotation.NextController;
import uk.gov.companieshouse.web.accounts.annotation.PreviousController;
import uk.gov.companieshouse.web.accounts.controller.BaseController;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.cic.CicApproval;
import uk.gov.companieshouse.web.accounts.service.cic.CicApprovalService;
import uk.gov.companieshouse.web.accounts.service.transaction.TransactionService;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

@Controller
@PreviousController(CicReviewController.class)
@NextController(AccountStartController.class)
@RequestMapping("/company/{companyNumber}/transaction/{transactionId}/company-accounts/{companyAccountsId}/cic/approval")
public class CicApprovalController extends BaseController {


    private static final String APPROVAL = "cicApproval";
    private static final String TRANSACTION_ID = "transaction_id";
    private static final String COMPANY_ACCOUNTS_ID = "company_accounts_id";

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private CicApprovalService cicApprovalService;

    @Override
    protected String getTemplateName() {
        return "cic/cicApproval";
    }

    @GetMapping
    public String getApproval(@PathVariable String companyNumber,
        @PathVariable String transactionId,
        @PathVariable String companyAccountsId,
        Model model) {

        addBackPageAttributeToModel(model, companyNumber, transactionId, companyAccountsId);

        model.addAttribute(APPROVAL, new CicApproval());
        model.addAttribute(TRANSACTION_ID, transactionId);
        model.addAttribute(COMPANY_ACCOUNTS_ID, companyAccountsId);

        return getTemplateName();
    }

    @PostMapping
    public String postApproval(@PathVariable String companyNumber,
        @PathVariable String transactionId,
        @PathVariable String companyAccountsId,
        @ModelAttribute(APPROVAL) @Valid CicApproval cicApproval,
        BindingResult bindingResult,
        Model model,
        HttpServletRequest request) {

        addBackPageAttributeToModel(model, companyNumber, transactionId, companyAccountsId);

        List<ValidationError> validationErrors = cicApprovalService
            .validateCicApprovalDate(cicApproval);
        if (!validationErrors.isEmpty()) {
            bindValidationErrors(bindingResult, validationErrors);
            return getTemplateName();
        }

        try {
            validationErrors.addAll(cicApprovalService
                .submitCicApproval(transactionId, companyAccountsId, cicApproval));
            if (!validationErrors.isEmpty()) {
                bindValidationErrors(bindingResult, validationErrors);
                return getTemplateName();
            }

        } catch (ServiceException e) {

            LOGGER.errorRequest(request, e.getMessage(), e);
            return ERROR_VIEW;
        }

        return navigatorService
            .getNextControllerRedirect(this.getClass(), companyNumber, transactionId,
                companyAccountsId);

    }
}
