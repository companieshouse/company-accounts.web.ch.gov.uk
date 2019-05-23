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
import org.springframework.web.servlet.view.UrlBasedViewResolver;
import org.springframework.web.util.UriTemplate;
import uk.gov.companieshouse.web.accounts.annotation.NextController;
import uk.gov.companieshouse.web.accounts.annotation.PreviousController;
import uk.gov.companieshouse.web.accounts.controller.BaseController;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.smallfull.Approval;
import uk.gov.companieshouse.web.accounts.service.cic.CicApprovalService;
import uk.gov.companieshouse.web.accounts.service.smallfull.ApprovalService;
import uk.gov.companieshouse.web.accounts.service.transaction.TransactionService;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

    @Controller
    @PreviousController(CicReviewController.class)
    @NextController(CompanyActivitiesAndImpactController.class)
    @RequestMapping("/company/{companyNumber}/transaction/{transactionId}/company-accounts/{companyAccountsId}/cic/approval")
    public class CicApprovalController extends BaseController {


        private static final String APPROVAL = "approval";
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
