package uk.gov.companieshouse.web.accounts.controller.smallfull;

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
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.accountingpolicies.OtherAccountingPolicy;
import uk.gov.companieshouse.web.accounts.model.state.CompanyAccountsDataState;
import uk.gov.companieshouse.web.accounts.service.NoteService;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;
import uk.gov.companieshouse.web.accounts.validation.smallfull.RadioAndTextValidator;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.List;

@Controller
@NextController(EmployeesController.class)
@PreviousController(ValuationInformationPolicyController.class)
@RequestMapping("/company/{companyNumber}/transaction/{transactionId}/company-accounts/{companyAccountsId}/small-full/other-accounting-policies")
public class OtherAccountingPolicyController extends BaseController {
    private static final String OTHER_ACCOUNTING_POLICY_FIELD_PATH =
            "otherAccountingPolicyDetails";

    private static final String INVALID_STRING_SIZE_ERROR_MESSAGE =
            "validation.length.minInvalid.accounting_policies.other_accounting_policy";

    @Autowired
    private NoteService<AccountingPolicies> noteService;

    @Autowired
    private RadioAndTextValidator radioAndTextValidator;

    @GetMapping
    public String getOtherAccountingPolicy(@PathVariable String companyNumber,
                                           @PathVariable String transactionId,
                                           @PathVariable String companyAccountsId,
                                           Model model,
                                           HttpServletRequest request) {
        addBackPageAttributeToModel(model, companyNumber, transactionId, companyAccountsId);

        try {
            OtherAccountingPolicy otherAccountingPolicy =
                    noteService.get(transactionId, companyAccountsId, NoteType.SMALL_FULL_ACCOUNTING_POLICIES)
                    .getOtherAccountingPolicy();

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

        radioAndTextValidator.validate(otherAccountingPolicy.getHasOtherAccountingPolicySelected(), otherAccountingPolicy.getOtherAccountingPolicyDetails(), bindingResult, INVALID_STRING_SIZE_ERROR_MESSAGE, OTHER_ACCOUNTING_POLICY_FIELD_PATH);

        if (bindingResult.hasErrors()) {
            return getTemplateName();
        }

        try {
            AccountingPolicies accountingPolicies = noteService.get(transactionId, companyAccountsId,
                    NoteType.SMALL_FULL_ACCOUNTING_POLICIES);

            accountingPolicies.setOtherAccountingPolicy(otherAccountingPolicy);

            List<ValidationError> validationErrors =
                noteService.submit(transactionId, companyAccountsId, accountingPolicies, NoteType.SMALL_FULL_ACCOUNTING_POLICIES);

            if (!validationErrors.isEmpty()) {
                bindValidationErrors(bindingResult, validationErrors);
                return getTemplateName();
            }
        } catch (ServiceException e) {
            LOGGER.errorRequest(request, e.getMessage(), e);
            return ERROR_VIEW;
        }

        cacheIsPolicyIncluded(request, otherAccountingPolicy);

        return navigatorService.getNextControllerRedirect(this.getClass(), companyNumber, transactionId,
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