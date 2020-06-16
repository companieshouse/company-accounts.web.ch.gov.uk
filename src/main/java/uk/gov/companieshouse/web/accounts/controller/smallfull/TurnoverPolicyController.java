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
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.accountingpolicies.TurnoverPolicy;
import uk.gov.companieshouse.web.accounts.model.state.CompanyAccountsDataState;
import uk.gov.companieshouse.web.accounts.service.NoteService;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;
import uk.gov.companieshouse.web.accounts.validation.smallfull.impl.TurnoverPolicyValidator;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Controller
@NextController(TangibleDepreciationPolicyController.class)
@PreviousController(BasisOfPreparationController.class)
@RequestMapping("/company/{companyNumber}/transaction/{transactionId}/company-accounts/{companyAccountsId}/small-full/turnover-policy")
public class TurnoverPolicyController extends BaseController {

    @Autowired
    private NoteService<AccountingPolicies> noteService;

    @Autowired
    private TurnoverPolicyValidator turnoverPolicyValidator;

    @GetMapping
    public String getTurnoverPolicy(@PathVariable String companyNumber,
        @PathVariable String transactionId,
        @PathVariable String companyAccountsId,
        Model model,
        HttpServletRequest request) {

        addBackPageAttributeToModel(model, companyNumber, transactionId, companyAccountsId);

        try {
            TurnoverPolicy turnoverPolicy =
                    noteService.get(transactionId, companyAccountsId, NoteType.SMALL_FULL_ACCOUNTING_POLICIES)
                        .getTurnoverPolicy();

            if (turnoverPolicy.getIsIncludeTurnoverSelected() == null) {
                setIsPolicyIncluded(request, turnoverPolicy);
            }

            model.addAttribute("turnoverPolicy", turnoverPolicy);

        } catch (ServiceException e) {
            LOGGER.errorRequest(request, e.getMessage(), e);
            return ERROR_VIEW;
        }

        return getTemplateName();
    }

    @PostMapping
    public String postTurnoverPolicy(@PathVariable String companyNumber,
        @PathVariable String transactionId,
        @PathVariable String companyAccountsId,
        @ModelAttribute("turnoverPolicy") @Valid TurnoverPolicy turnoverPolicy,
        BindingResult bindingResult,
        Model model,
        HttpServletRequest request) {

        addBackPageAttributeToModel(model, companyNumber, transactionId, companyAccountsId);

        if (bindingResult.hasErrors()) {
            return getTemplateName();
        }

       /* List<ValidationError> validationErrors = turnoverPolicyValidator.validateTurnoverPolicy(turnoverPolicy);

        if (!validationErrors.isEmpty()) {
            bindValidationErrors(bindingResult, validationErrors);
            return getTemplateName();
        }*/

        try {
            AccountingPolicies accountingPolicies = noteService.get(transactionId, companyAccountsId,
                    NoteType.SMALL_FULL_ACCOUNTING_POLICIES);

            accountingPolicies.setTurnoverPolicy(turnoverPolicy);

           List<ValidationError> validationErrors = noteService
                .submit(transactionId, companyAccountsId, accountingPolicies, NoteType.SMALL_FULL_ACCOUNTING_POLICIES);

            if (!validationErrors.isEmpty()) {
                bindValidationErrors(bindingResult, validationErrors);
                return getTemplateName();
            }

        } catch (ServiceException e) {
            LOGGER.errorRequest(request, e.getMessage(), e);
            return ERROR_VIEW;
        }

        cacheIsPolicyIncluded(request, turnoverPolicy);

        return navigatorService.getNextControllerRedirect(this.getClass(), companyNumber, transactionId,
            companyAccountsId);
    }

    @Override
    protected String getTemplateName() {
        return "smallfull/turnoverPolicy";
    }

    /**
     * Sets the 'include turnover policy' boolean according to the cached state
     * @param request The request
     * @param turnoverPolicy The turnover policy model on which to set the boolean
     */
    private void setIsPolicyIncluded(HttpServletRequest request, TurnoverPolicy turnoverPolicy) {

        CompanyAccountsDataState companyAccountsDataState = getStateFromRequest(request);
        turnoverPolicy.setIsIncludeTurnoverSelected(
                companyAccountsDataState.getAccountingPolicies().getHasProvidedTurnoverPolicy());
    }

    /**
     * Cache the 'include turnover policy' boolean within the client's state
     * @param request The request
     * @param turnoverPolicy The turnover policy for which to cache data
     */
    private void  cacheIsPolicyIncluded(HttpServletRequest request, TurnoverPolicy turnoverPolicy) {

        CompanyAccountsDataState companyAccountsDataState = getStateFromRequest(request);
        companyAccountsDataState.getAccountingPolicies().setHasProvidedTurnoverPolicy(turnoverPolicy.getIsIncludeTurnoverSelected());

        updateStateOnRequest(request, companyAccountsDataState);
    }
}
