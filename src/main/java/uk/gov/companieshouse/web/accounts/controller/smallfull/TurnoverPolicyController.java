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
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.accountingpolicies.TurnoverPolicy;
import uk.gov.companieshouse.web.accounts.model.state.State;
import uk.gov.companieshouse.web.accounts.service.smallfull.TurnoverPolicyService;
import uk.gov.companieshouse.web.accounts.util.Navigator;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

@Controller
@NextController(ReviewController.class)
@PreviousController(BasisOfPreparationController.class)
@RequestMapping("/company/{companyNumber}/transaction/{transactionId}/company-accounts/{companyAccountsId}/small-full/turnover-policy")
public class TurnoverPolicyController extends BaseController {

    @Autowired
    private TurnoverPolicyService turnoverPolicyService;

    @GetMapping
    public String getTurnoverPolicy(@PathVariable String companyNumber,
        @PathVariable String transactionId,
        @PathVariable String companyAccountsId,
        Model model,
        HttpServletRequest request) {

        addBackPageAttributeToModel(model, companyNumber, transactionId, companyAccountsId);

        try {
            TurnoverPolicy turnoverPolicy =
                    turnoverPolicyService.getTurnOverPolicy(transactionId, companyAccountsId);

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

        try {
            List<ValidationError> validationErrors = turnoverPolicyService
                .postTurnoverPolicy(transactionId, companyAccountsId, turnoverPolicy);

            if (!validationErrors.isEmpty()) {
                bindValidationErrors(bindingResult, validationErrors);
                return getTemplateName();
            }

        } catch (ServiceException e) {
            LOGGER.errorRequest(request, e.getMessage(), e);
            return ERROR_VIEW;
        }

        cacheIsPolicyIncluded(request, turnoverPolicy);

        return Navigator.getNextControllerRedirect(this.getClass(), companyNumber, transactionId,
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

        State state = getStateFromRequest(request);
        turnoverPolicy.setIsIncludeTurnoverSelected(state.getAccountingPolicies().getHasProvidedTurnoverPolicy());
    }

    /**
     * Cache the 'include turnover policy' boolean within the client's state
     * @param request The request
     * @param turnoverPolicy The turnover policy for which to cache data
     */
    private void  cacheIsPolicyIncluded(HttpServletRequest request, TurnoverPolicy turnoverPolicy) {

        State state = getStateFromRequest(request);
        state.getAccountingPolicies().setHasProvidedTurnoverPolicy(turnoverPolicy.getIsIncludeTurnoverSelected());

        updateStateOnRequest(request, state);
    }

}
