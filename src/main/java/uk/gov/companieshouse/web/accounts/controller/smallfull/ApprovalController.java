package uk.gov.companieshouse.web.accounts.controller.smallfull;

import java.util.List;
import java.util.Optional;
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
import uk.gov.companieshouse.web.accounts.service.payment.PaymentService;
import uk.gov.companieshouse.web.accounts.service.smallfull.ApprovalService;
import uk.gov.companieshouse.web.accounts.service.transaction.TransactionService;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

@Controller
@PreviousController(ReviewController.class)
@RequestMapping("/company/{companyNumber}/transaction/{transactionId}/company-accounts/{companyAccountsId}/small-full/approval")
public class ApprovalController extends BaseController {

    private static final UriTemplate CONFIRMATION_REDIRECT = new UriTemplate("/transaction/{transactionId}/confirmation");
    private static final UriTemplate RESUME_URI = new UriTemplate("/company/{companyNumber}/transaction/{transactionId}/company-accounts/{companyAccountsId}/pay-filing-fee");


    private static final String APPROVAL = "approval";
    private static final String TRANSACTION_ID = "transaction_id";
    private static final String COMPANY_ACCOUNTS_ID = "company_accounts_id";
    private static final String IS_PAYABLE_TRANSACTION = "isPayableTransaction";

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private ApprovalService approvalService;

    @Autowired
    private PaymentService paymentService;

    @Override
    protected String getTemplateName() {
        return "smallfull/approval";
    }

    @GetMapping
    public String getApproval(@PathVariable String companyNumber,
                              @PathVariable String transactionId,
                              @PathVariable String companyAccountsId,
                              Model model,
                              HttpServletRequest request) {

        addBackPageAttributeToModel(model, companyNumber, transactionId, companyAccountsId);

        try {
            model.addAttribute(IS_PAYABLE_TRANSACTION,
                    transactionService.isPayableTransaction(transactionId, companyAccountsId));
        } catch (ServiceException e) {

            LOGGER.errorRequest(request, e.getMessage(), e);
            return ERROR_VIEW;
        }

        model.addAttribute(APPROVAL, new Approval());
        model.addAttribute(TRANSACTION_ID, transactionId);
        model.addAttribute(COMPANY_ACCOUNTS_ID, companyAccountsId);

        return getTemplateName();
    }

    @PostMapping
    public String postApproval(@PathVariable String companyNumber,
                               @PathVariable String transactionId,
                               @PathVariable String companyAccountsId,
                               @ModelAttribute(APPROVAL) @Valid Approval approval,
                               BindingResult bindingResult,
                               Model model,
                               HttpServletRequest request) {

        addBackPageAttributeToModel(model, companyNumber, transactionId, companyAccountsId);

        try {
            List<ValidationError> validationErrors = approvalService.validateApprovalDate(approval);
            if (!validationErrors.isEmpty()) {
                model.addAttribute(IS_PAYABLE_TRANSACTION,
                        transactionService.isPayableTransaction(transactionId, companyAccountsId));
                bindValidationErrors(bindingResult, validationErrors);
                return getTemplateName();
            }

            validationErrors.addAll(approvalService.submitApproval(transactionId, companyAccountsId, approval));
            if (!validationErrors.isEmpty()) {
                model.addAttribute(IS_PAYABLE_TRANSACTION,
                        transactionService.isPayableTransaction(transactionId, companyAccountsId));
                bindValidationErrors(bindingResult, validationErrors);
                return getTemplateName();
            }

            boolean paymentRequired = transactionService.closeTransaction(transactionId);
            if (paymentRequired) {

                transactionService.updateResumeLink(transactionId, RESUME_URI.expand(companyNumber, transactionId, companyAccountsId).toString());

                return UrlBasedViewResolver.REDIRECT_URL_PREFIX +
                        paymentService.createPaymentSessionForTransaction(transactionId);
            }
        } catch (ServiceException e) {

            LOGGER.errorRequest(request, e.getMessage(), e);
            return ERROR_VIEW;
        }

        return UrlBasedViewResolver.REDIRECT_URL_PREFIX +
                CONFIRMATION_REDIRECT.expand(transactionId).toString();
    }
}
