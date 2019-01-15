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
import uk.gov.companieshouse.web.accounts.model.state.CompanyAccountsDataState;
import uk.gov.companieshouse.web.accounts.service.smallfull.OtherAccountingPolicyService;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

@Controller
@NextController(DebtorsController.class)
@PreviousController(ValuationInformationPolicyController.class)
@RequestMapping("/company/{companyNumber}/transaction/{transactionId}/company-accounts/{companyAccountsId}/small-full/other-accounting-policies")
public class OtherAccountingPolicyController extends BaseController {

    @Autowired
    private OtherAccountingPolicyService otherAccountingPolicyService;

    @GetMapping
    public String getOtherAccountingPolicy(@PathVariable String companyNumber,
                                           @PathVariable String transactionId,
                                           @PathVariable String companyAccountsId,
                                           Model model,
                                           HttpServletRequest request) {

        addBackPageAttributeToModel(model, companyNumber, transactionId, companyAccountsId);

        try {
            OtherAccountingPolicy otherAccountingPolicy =
                    otherAccountingPolicyService
                            .getOtherAccountingPolicy(transactionId, companyAccountsId);

            if (otherAccountingPolicy.getHasOtherAccountingPolicySelected() == null) {
                setIsPolicyIncluded(request, otherAccountingPolicy);
            }

            model.addAttribute("otherAccountingPolicy", otherAccountingPolicy);
        } catch (ServiceException e) {
            LOGGER.errorRequest(request, e.getMessage(), e);
            return ERROR_VIEW;
        }
        return getTemplateName();
    }

    @PostMapping
    public String postOtherAccountingPolicy(@PathVariable String companyNumber,
                                            @PathVariable String transactionId,
                                            @PathVariable String companyAccountsId,
                                            @ModelAttribute("otherAccountingPolicy") @Valid OtherAccountingPolicy otherAccountingPolicy,
                                            BindingResult bindingResult,
                                            Model model,
                                            HttpServletRequest request) {

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

        cacheIsPolicyIncluded(request, otherAccountingPolicy);

        return navigator.getNextControllerRedirect(this.getClass(), companyNumber, transactionId,
            companyAccountsId);
    }

    @Override
    protected String getTemplateName() {
        return "smallfull/otherAccountingPolicy";
    }

    /**
     * Sets the 'include other accounting policy' boolean according to the cached state
     * @param request The request
     * @param otherAccountingPolicy The other accounting policy model on which to set the boolean
     */
    private void setIsPolicyIncluded(HttpServletRequest request, OtherAccountingPolicy otherAccountingPolicy) {

        CompanyAccountsDataState companyAccountsDataState = getStateFromRequest(request);
        otherAccountingPolicy.setHasOtherAccountingPolicySelected(
                companyAccountsDataState.getAccountingPolicies().getHasProvidedOtherAccountingPolicy());
    }

    /**
     * Cache the 'include other accounting policy' boolean within the client's state
     * @param request The request
     * @param otherAccountingPolicy The other accounting policy for which to cache data
     */
    private void  cacheIsPolicyIncluded(HttpServletRequest request, OtherAccountingPolicy otherAccountingPolicy) {

        CompanyAccountsDataState companyAccountsDataState = getStateFromRequest(request);
        companyAccountsDataState.getAccountingPolicies().setHasProvidedOtherAccountingPolicy(
                otherAccountingPolicy.getHasOtherAccountingPolicySelected());

        updateStateOnRequest(request, companyAccountsDataState);
    }
}