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
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.accountingpolicies.IntangibleAmortisationPolicy;
import uk.gov.companieshouse.web.accounts.service.smallfull.IntangibleAmortisationPolicyService;
import uk.gov.companieshouse.web.accounts.util.Navigator;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

@Controller
@NextController(ValuationInformationPolicyController.class)
@PreviousController(TangibleDepreciationPolicyController.class)
@RequestMapping("/company/{companyNumber}/transaction/{transactionId}/company-accounts/{companyAccountsId}/small-full/intangible-fixed-assets-amortisation")
public class IntangibleAmortisationPolicyController extends BaseController {

    private static final String INTANGIBLE_AMORTISATION_POLICY = "intangibleAmortisationPolicy";

    @Autowired
    private IntangibleAmortisationPolicyService intangibleAmortisationPolicyService;

    @GetMapping
    public String getIntangibleAmortisationPolicy(@PathVariable String companyNumber,
                                                  @PathVariable String transactionId,
                                                  @PathVariable String companyAccountsId,
                                                  Model model,
                                                  HttpServletRequest request) {

        addBackPageAttributeToModel(model, companyNumber, transactionId, companyAccountsId);

        try {
            model.addAttribute(INTANGIBLE_AMORTISATION_POLICY,
                    intangibleAmortisationPolicyService.getIntangibleAmortisationPolicy(transactionId, companyAccountsId));
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

        if (bindingResult.hasErrors()) {
            return getTemplateName();
        }

        try {
            List<ValidationError> validationErrors = intangibleAmortisationPolicyService
                    .submitIntangibleAmortisationPolicy(transactionId, companyAccountsId, intangibleAmortisationPolicy);

            if (!validationErrors.isEmpty()) {
                bindValidationErrors(bindingResult, validationErrors);
                return getTemplateName();
            }
        } catch (ServiceException e) {

            LOGGER.errorRequest(request, e.getMessage(), e);
            return ERROR_VIEW;
        }

        return Navigator.getNextControllerRedirect(this.getClass(), companyNumber, transactionId, companyAccountsId);
    }

    @Override
    protected String getTemplateName() {
        return "smallfull/intangibleAmortisationPolicy";
    }
}
