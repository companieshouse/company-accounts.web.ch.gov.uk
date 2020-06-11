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
import uk.gov.companieshouse.web.accounts.enumeration.NoteType;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.accountingpolicies.AccountingPolicies;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.accountingpolicies.TangibleDepreciationPolicy;
import uk.gov.companieshouse.web.accounts.model.state.CompanyAccountsDataState;
import uk.gov.companieshouse.web.accounts.service.NoteService;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

@Controller
@NextController(IntangibleAmortisationPolicyController.class)
@PreviousController(TurnoverPolicyController.class)
@RequestMapping("/company/{companyNumber}/transaction/{transactionId}/company-accounts/{companyAccountsId}/small-full/tangible-depreciation-policy")
public class TangibleDepreciationPolicyController extends BaseController {

    @Autowired
    private NoteService<AccountingPolicies> noteService;

    @GetMapping
    public String getTangibleDepreciationPolicy(@PathVariable String companyNumber,
        @PathVariable String transactionId,
        @PathVariable String companyAccountsId,
        Model model,
        HttpServletRequest request) {

        addBackPageAttributeToModel(model, companyNumber, transactionId, companyAccountsId);

        try {
            TangibleDepreciationPolicy tangibleDepreciationPolicy =
                    noteService.get(transactionId, companyAccountsId, NoteType.SMALL_FULL_ACCOUNTING_POLICIES)
                            .getTangibleDepreciationPolicy();

            if (tangibleDepreciationPolicy.getHasTangibleDepreciationPolicySelected() == null) {
                setIsPolicyIncluded(request, tangibleDepreciationPolicy);
            }

            model.addAttribute("tangibleDepreciationPolicy", tangibleDepreciationPolicy);
        } catch (ServiceException e) {

            LOGGER.errorRequest(request, e.getMessage(), e);
            return ERROR_VIEW;
        }

        return getTemplateName();
    }

    @PostMapping
    public String postTangibleDepreciationPolicy(@PathVariable String companyNumber,
        @PathVariable String transactionId,
        @PathVariable String companyAccountsId,
        @ModelAttribute("tangibleDepreciationPolicy") @Valid TangibleDepreciationPolicy tangiblePolicy,
        BindingResult bindingResult,
        Model model,
        HttpServletRequest request) {

        addBackPageAttributeToModel(model, companyNumber, transactionId, companyAccountsId);

        if (bindingResult.hasErrors()) {
            return getTemplateName();
        }

        try {
            AccountingPolicies accountingPolicies = noteService.get(transactionId, companyAccountsId,
                    NoteType.SMALL_FULL_ACCOUNTING_POLICIES);

            accountingPolicies.setTangibleDepreciationPolicy(tangiblePolicy);

            List<ValidationError> validationErrors =
                noteService.submit(transactionId, companyAccountsId, accountingPolicies,
                        NoteType.SMALL_FULL_ACCOUNTING_POLICIES);

            if (!validationErrors.isEmpty()) {
                bindValidationErrors(bindingResult, validationErrors);
                return getTemplateName();
            }
        } catch (ServiceException e) {

            LOGGER.errorRequest(request, e.getMessage(), e);
            return ERROR_VIEW;
        }

        cacheIsPolicyIncluded(request, tangiblePolicy);

        return navigatorService.getNextControllerRedirect(this.getClass(), companyNumber, transactionId,
            companyAccountsId);
    }

    @Override
    protected String getTemplateName() {
        return "smallfull/tangibleDepreciationPolicy";
    }

    /**
     * Sets the 'include tangible depreciation policy' boolean according to the cached state
     * @param request The request
     * @param tangibleDepreciationPolicy The tangible depreciation policy model on which to set the boolean
     */
    private void setIsPolicyIncluded(HttpServletRequest request, TangibleDepreciationPolicy tangibleDepreciationPolicy) {

        CompanyAccountsDataState companyAccountsDataState = getStateFromRequest(request);
        tangibleDepreciationPolicy.setHasTangibleDepreciationPolicySelected(
                companyAccountsDataState.getAccountingPolicies().getHasProvidedTangiblePolicy());
    }

    /**
     * Cache the 'include tangible depreciation policy' boolean within the client's state
     * @param request The request
     * @param tangibleDepreciationPolicy The tangible depreciation policy for which to cache data
     */
    private void  cacheIsPolicyIncluded(HttpServletRequest request, TangibleDepreciationPolicy tangibleDepreciationPolicy) {

        CompanyAccountsDataState companyAccountsDataState = getStateFromRequest(request);
        companyAccountsDataState.getAccountingPolicies().setHasProvidedTangiblePolicy(
                tangibleDepreciationPolicy.getHasTangibleDepreciationPolicySelected());

        updateStateOnRequest(request, companyAccountsDataState);
    }
}
