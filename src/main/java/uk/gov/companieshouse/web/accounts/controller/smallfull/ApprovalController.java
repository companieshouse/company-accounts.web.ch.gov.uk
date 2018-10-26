package uk.gov.companieshouse.web.accounts.controller.smallfull;

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
import org.springframework.web.servlet.view.UrlBasedViewResolver;
import org.springframework.web.util.UriTemplate;
import uk.gov.companieshouse.web.accounts.annotation.PreviousController;
import uk.gov.companieshouse.web.accounts.controller.BaseController;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.smallfull.Approval;
import uk.gov.companieshouse.web.accounts.service.smallfull.ApprovalService;
import uk.gov.companieshouse.web.accounts.service.transaction.TransactionService;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

@Controller
@PreviousController(ReviewController.class)
@RequestMapping("/company/{companyNumber}/transaction/{transactionId}/company-accounts/{companyAccountsId}/small-full/approval")
public class ApprovalController extends BaseController {

   private static final UriTemplate CONFIRMATION_REDIRECT = new UriTemplate("/transaction/{transactionId}/confirmation");

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private ApprovalService approvalService;

    @Override
    protected String getTemplateName() {
        return "smallfull/approval";
    }

    @GetMapping
    public String getApproval(@PathVariable String companyNumber,
                              @PathVariable String transactionId,
                              @PathVariable String companyAccountsId,
                              Model model) {

        addBackPageAttributeToModel(model, companyNumber, transactionId, companyAccountsId);

        model.addAttribute("approval", new Approval());

        return getTemplateName();
    }

    @PostMapping
    public String postApproval(@PathVariable String transactionId,
                               @PathVariable String companyAccountsId,
                               @ModelAttribute("approval") @Valid Approval approval,
                               BindingResult bindingResult,
                               HttpServletRequest request) {

        List<ValidationError> validationErrors = approvalService.validateApprovalDate(approval);
        if (!validationErrors.isEmpty()) {
            bindValidationErrors(bindingResult, validationErrors);
            return getTemplateName();
        }

        try {
            validationErrors.addAll(approvalService.submitApproval(transactionId, companyAccountsId, approval));
            if (!validationErrors.isEmpty()) {
                bindValidationErrors(bindingResult, validationErrors);
                return getTemplateName();
            }

            transactionService.closeTransaction(transactionId);
        } catch (ServiceException e) {

            LOGGER.errorRequest(request, e.getMessage(), e);
            return ERROR_VIEW;
        }

        return UrlBasedViewResolver.REDIRECT_URL_PREFIX +
                CONFIRMATION_REDIRECT.expand(transactionId).toString();
    }
}
