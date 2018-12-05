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
import uk.gov.companieshouse.web.accounts.annotation.NextController;
import uk.gov.companieshouse.web.accounts.annotation.PreviousController;
import uk.gov.companieshouse.web.accounts.controller.BaseController;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.accountingpolicies.OtherAccountingPolicy;
import uk.gov.companieshouse.web.accounts.service.smallfull.OtherAccountingPolicyService;
import uk.gov.companieshouse.web.accounts.util.Navigator;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

@Controller
@NextController(ReviewController.class)
@PreviousController(TurnoverPolicyController.class)
@RequestMapping("/company/{companyNumber}/transaction/{transactionId}/company-accounts/{companyAccountsId}/small-full/other-accounting-policies")
public class OtherAccountingPolicyController extends BaseController {

    @Autowired
    private OtherAccountingPolicyService otherAccountingPolicyService;

    @GetMapping
    public String getOtherAccountingPolicy(@PathVariable String companyNumber,
        @PathVariable String transactionId, @PathVariable String companyAccountsId,
        Model model, HttpServletRequest request) {

        addBackPageAttributeToModel(model, companyNumber, transactionId, companyAccountsId);

        try {
            model.addAttribute("otherAccountingPolicy",
                otherAccountingPolicyService
                    .getOtherAccountingPolicy(transactionId, companyAccountsId));
        } catch (ServiceException e) {
            LOGGER.errorRequest(request, e.getMessage(), e);
            return ERROR_VIEW;
        }
        return getTemplateName();
    }

    @PostMapping
    public String postOtherAccountingPolicy(@PathVariable String companyNumber,
        @PathVariable String transactionId, @PathVariable String companyAccountsId,
        @ModelAttribute("otherAccountingPolicy") @Valid OtherAccountingPolicy otherAccountingPolicy,
        BindingResult bindingResult, Model model, HttpServletRequest request) {

        addBackPageAttributeToModel(model, companyNumber, transactionId, companyAccountsId);

        if (bindingResult.hasErrors()) {
            return getTemplateName();
        }

        try {
            List<ValidationError> validationErrors =
                otherAccountingPolicyService
                    .submitOtherAccountingPolicy(transactionId, companyAccountsId,
                        otherAccountingPolicy);
            if (!validationErrors.isEmpty()) {
                bindValidationErrors(bindingResult, validationErrors);
                return getTemplateName();
            }
        } catch (ServiceException e) {
            LOGGER.errorRequest(request, e.getMessage(), e);
            return ERROR_VIEW;
        }

        return Navigator.getNextControllerRedirect(this.getClass(), companyNumber, transactionId,
            companyAccountsId);
    }

    @Override
    protected String getTemplateName() {
        return "smallfull/otherAccountingPolicy";
    }
}