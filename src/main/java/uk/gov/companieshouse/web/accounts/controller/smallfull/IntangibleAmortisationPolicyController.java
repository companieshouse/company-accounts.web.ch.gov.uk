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
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.accountingpolicies.IntangibleAmortisationPolicy;
import uk.gov.companieshouse.web.accounts.model.state.CompanyAccountsDataState;
import uk.gov.companieshouse.web.accounts.service.NoteService;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;
import uk.gov.companieshouse.web.accounts.validation.smallfull.RadioAndTextValidator;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Controller
@NextController(ValuationInformationPolicyController.class)
@PreviousController(TangibleDepreciationPolicyController.class)
@RequestMapping("/company/{companyNumber}/transaction/{transactionId}/company-accounts/{companyAccountsId}/small-full/intangible-fixed-assets-amortisation")
public class IntangibleAmortisationPolicyController extends BaseController {

    private static final String INTANGIBLE_AMORTISATION_POLICY = "intangibleAmortisationPolicy";

    private static final String INTANGIBLE_AMORTISATION_POLICY_FIELD_PATH =
            "intangibleAmortisationPolicyDetails";

    private static final String INVALID_STRING_SIZE_ERROR_MESSAGE =
            "validation.length.minInvalid.accounting_policies.intangible_fixed_assets_amortisation_policy";


    @Autowired
    private NoteService<AccountingPolicies> noteService;

    @Autowired
    private RadioAndTextValidator radioAndTextValidator;

    @GetMapping
    public String getIntangibleAmortisationPolicy(@PathVariable String companyNumber,
                                                  @PathVariable String transactionId,
                                                  @PathVariable String companyAccountsId,
                                                  Model model,
                                                  HttpServletRequest request) {

        addBackPageAttributeToModel(model, companyNumber, transactionId, companyAccountsId);

        try {
            IntangibleAmortisationPolicy intangibleAmortisationPolicy =
                    noteService.get(transactionId, companyAccountsId, NoteType.SMALL_FULL_ACCOUNTING_POLICIES)
                            .getIntangibleAmortisationPolicy();

            if (intangibleAmortisationPolicy.getIncludeIntangibleAmortisationPolicy() == null) {
                setIsPolicyIncluded(request, intangibleAmortisationPolicy);
            }

            model.addAttribute(INTANGIBLE_AMORTISATION_POLICY, intangibleAmortisationPolicy);
        } catch (ServiceException e) {

            LOGGER.errorRequest(request, e.getMessage(), e);
            return ERROR_VIEW;
        }

        return getTemplateName();
    }

    @PostMapping
    public String submitIntangibleAmortisationPolicy(@PathVariable String companyNumber,
                                                     @PathVariable String transactionId,
                                                     @PathVariable String companyAccountsId,
                                                     @ModelAttribute(INTANGIBLE_AMORTISATION_POLICY) @Valid IntangibleAmortisationPolicy intangibleAmortisationPolicy,
                                                     BindingResult bindingResult,
                                                     Model model,
                                                     HttpServletRequest request) {

        addBackPageAttributeToModel(model, companyNumber, transactionId, companyAccountsId);

        radioAndTextValidator.validate(intangibleAmortisationPolicy.getIncludeIntangibleAmortisationPolicy(), intangibleAmortisationPolicy.getIntangibleAmortisationPolicyDetails(), bindingResult, INVALID_STRING_SIZE_ERROR_MESSAGE, INTANGIBLE_AMORTISATION_POLICY_FIELD_PATH);

        if (bindingResult.hasErrors()) {
            return getTemplateName();
        }

        try {

            AccountingPolicies accountingPolicies = noteService.get(transactionId, companyAccountsId,
                    NoteType.SMALL_FULL_ACCOUNTING_POLICIES);

            accountingPolicies.setIntangibleAmortisationPolicy(intangibleAmortisationPolicy);

            List<ValidationError>  validationErrors = noteService
                    .submit(transactionId, companyAccountsId, accountingPolicies, NoteType.SMALL_FULL_ACCOUNTING_POLICIES);

            if (!validationErrors.isEmpty()) {
                bindValidationErrors(bindingResult, validationErrors);
                return getTemplateName();
            }
        } catch (ServiceException e) {

            LOGGER.errorRequest(request, e.getMessage(), e);
            return ERROR_VIEW;
        }

        cacheIsPolicyIncluded(request, intangibleAmortisationPolicy);

        return navigatorService.getNextControllerRedirect(this.getClass(), companyNumber, transactionId, companyAccountsId);
    }

    @Override
    protected String getTemplateName() {
        return "smallfull/intangibleAmortisationPolicy";
    }

    /**
     * Sets the 'include intangible amortisation policy' boolean according to the cached state
     * @param request The request
     * @param intangibleAmortisationPolicy The intangible amortisation policy model on which to set the boolean
     */
    private void setIsPolicyIncluded(HttpServletRequest request, IntangibleAmortisationPolicy intangibleAmortisationPolicy) {

        CompanyAccountsDataState companyAccountsDataState = getStateFromRequest(request);
        intangibleAmortisationPolicy.setIncludeIntangibleAmortisationPolicy(
                companyAccountsDataState.getAccountingPolicies().getHasProvidedIntangiblePolicy());
    }

    /**
     * Cache the 'include intangible amortisation policy' boolean within the client's state
     * @param request The request
     * @param intangibleAmortisationPolicy The intangible amortisation policy for which to cache data
     */
    private void  cacheIsPolicyIncluded(HttpServletRequest request, IntangibleAmortisationPolicy intangibleAmortisationPolicy) {

        CompanyAccountsDataState companyAccountsDataState = getStateFromRequest(request);
        companyAccountsDataState.getAccountingPolicies().setHasProvidedIntangiblePolicy(
                intangibleAmortisationPolicy.getIncludeIntangibleAmortisationPolicy());

        updateStateOnRequest(request, companyAccountsDataState);
    }
}
