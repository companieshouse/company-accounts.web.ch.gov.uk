package uk.gov.companieshouse.web.accounts.controller.govuk;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.UrlBasedViewResolver;
import uk.gov.companieshouse.web.accounts.annotation.NextController;
import uk.gov.companieshouse.web.accounts.annotation.PreviousController;
import uk.gov.companieshouse.web.accounts.controller.BaseController;
import uk.gov.companieshouse.web.accounts.controller.govuk.smallfull.GovukFullAccountsCriteriaController;
import uk.gov.companieshouse.web.accounts.model.smallfull.Criteria;

@Controller
@NextController(GovukCriteriaController.class)
@PreviousController(GovukFullAccountsCriteriaController.class)
@RequestMapping("/accounts/criteria")
public class GovukCriteriaController extends BaseController {
    @Override
    protected String getTemplateName() {
        return "smallfull/criteria";
    }

    @GetMapping
    public String getGovUkCriteria(Model model) {
        model.addAttribute("criteria", new Criteria());
        addBackPageAttributeToModel(model);

        return getTemplateName();
    }

    @PostMapping
    public String postGovUkCriteria(@ModelAttribute("criteria") @Valid Criteria criteria,
        BindingResult bindingResult, Model model, RedirectAttributes attributes) {
        addBackPageAttributeToModel(model);

        if (bindingResult.hasErrors()) {
            return getTemplateName();
        }

        if (criteria.getIsCriteriaMet().equalsIgnoreCase("noAlternativeFilingMethod")) {
            return UrlBasedViewResolver.REDIRECT_URL_PREFIX
                + "/accounts/alternative-filing-options";
        } else if (criteria.getIsCriteriaMet().equalsIgnoreCase("noOtherAccounts")) {
            return UrlBasedViewResolver.REDIRECT_URL_PREFIX + "/accounts/select-account-type";
        }
        attributes.addAttribute("forward", "/accounts/company/{companyNumber}/details");
        return UrlBasedViewResolver.REDIRECT_URL_PREFIX + "/company-lookup/search";
    }
}