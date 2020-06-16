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
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.accountingpolicies.ValuationInformationPolicy;
import uk.gov.companieshouse.web.accounts.model.state.CompanyAccountsDataState;
import uk.gov.companieshouse.web.accounts.service.NoteService;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;
import uk.gov.companieshouse.web.accounts.validation.smallfull.impl.ValuationInformationPolicyValidator;

@Controller
@NextController(OtherAccountingPolicyController.class)
@PreviousController(IntangibleAmortisationPolicyController.class)
@RequestMapping("/company/{companyNumber}/transaction/{transactionId}/company-accounts/{companyAccountsId}/small-full/valuation-information")
public class ValuationInformationPolicyController extends BaseController {

    @Autowired
    private NoteService<AccountingPolicies> noteService;

    @Autowired
    private ValuationInformationPolicyValidator valuationInformationPolicyValidator;

    private static final String VALUATION_INFORMATION_POLICY = "valuationInformationPolicy";

    @GetMapping
    public String getValuationInformationPolicy(@PathVariable String companyNumber,
                                                @PathVariable String transactionId,
                                                @PathVariable String companyAccountsId,
                                                Model model,
                                                HttpServletRequest request) {

        addBackPageAttributeToModel(model, companyNumber, transactionId, companyAccountsId);

        try {
            ValuationInformationPolicy valuationInformationPolicy =
                    noteService.get(transactionId, companyAccountsId, NoteType.SMALL_FULL_ACCOUNTING_POLICIES)
                        .getValuationInformationPolicy();

            if (valuationInformationPolicy.getIncludeValuationInformationPolicy() == null) {
                setIsPolicyIncluded(request, valuationInformationPolicy);
            }

            model.addAttribute(VALUATION_INFORMATION_POLICY, valuationInformationPolicy);
        } catch (ServiceException e) {

            LOGGER.errorRequest(request, e.getMessage(), e);
            return ERROR_VIEW;
        }

        return getTemplateName();
    }

    @PostMapping
    public String submitValuationInformationPolicy(@PathVariable String companyNumber,
                                                   @PathVariable String transactionId,
                                                   @PathVariable String companyAccountsId,
                                                   @ModelAttribute(VALUATION_INFORMATION_POLICY) @Valid ValuationInformationPolicy valuationInformationPolicy,
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

            accountingPolicies.setValuationInformationPolicy(valuationInformationPolicy);

            List<ValidationError> validationErrors = valuationInformationPolicyValidator.validateValuationInformationPolicy(valuationInformationPolicy);

            if (!validationErrors.isEmpty()) {
                bindValidationErrors(bindingResult, validationErrors);
                return getTemplateName();
            }

            validationErrors =
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

        cacheIsPolicyIncluded(request, valuationInformationPolicy);

        return navigatorService.getNextControllerRedirect(this.getClass(), companyNumber, transactionId, companyAccountsId);
    }

    @Override
    protected String getTemplateName() {
        return "smallfull/valuationInformationPolicy";
    }

    /**
     * Sets the 'include valuation information policy' boolean according to the cached state
     * @param request The request
     * @param valuationInformationPolicy The valuation information policy model on which to set the boolean
     */
    private void setIsPolicyIncluded(HttpServletRequest request, ValuationInformationPolicy valuationInformationPolicy) {

        CompanyAccountsDataState companyAccountsDataState = getStateFromRequest(request);
        valuationInformationPolicy.setIncludeValuationInformationPolicy(
                companyAccountsDataState.getAccountingPolicies().getHasProvidedValuationInformationPolicy());
    }

    /**
     * Cache the 'include valuation information policy' boolean within the client's state
     * @param request The request
     * @param valuationInformationPolicy The intangible amortisation policy for which to cache data
     */
    private void  cacheIsPolicyIncluded(HttpServletRequest request, ValuationInformationPolicy valuationInformationPolicy) {

        CompanyAccountsDataState companyAccountsDataState = getStateFromRequest(request);
        companyAccountsDataState.getAccountingPolicies().setHasProvidedValuationInformationPolicy(
                valuationInformationPolicy.getIncludeValuationInformationPolicy());

        updateStateOnRequest(request, companyAccountsDataState);
    }
}
