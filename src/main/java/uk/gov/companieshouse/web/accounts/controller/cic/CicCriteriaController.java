package uk.gov.companieshouse.web.accounts.controller.cic;

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
import uk.gov.companieshouse.web.accounts.model.cic.CicCriteria;

import javax.validation.Valid;

@Controller
@NextController(CicCriteriaController.class)
@PreviousController(CicFileFullAccountsController.class)
@RequestMapping("/accounts/cic/criteria")
public class CicCriteriaController extends BaseController {

    private static final String NO_CRITERIA_URL_LINK = "/accounts/cic/cant-file-online-yet";

    @Override
    protected String getTemplateName() {
        return "cic/cicCriteria";
    }

    @GetMapping
    public String getCicCriteria(Model model) {

        model.addAttribute("criteria", new CicCriteria());

        addBackPageAttributeToModel(model);

        return getTemplateName();
    }

    @PostMapping
    public String postCicCriteria(@ModelAttribute("criteria") @Valid CicCriteria criteria,
                                  BindingResult bindingResult, Model model, RedirectAttributes attributes) {

        if (bindingResult.hasErrors()) {
            return getTemplateName();
        }

        if(criteria.getIsCriteriaMet()) {

            attributes.addAttribute("forward", "/accounts/company/{companyNumber}/cic/details");
            return UrlBasedViewResolver.REDIRECT_URL_PREFIX + "/company-lookup/search";
        }

        attributes.addAttribute("backLink", "/accounts/cic/criteria");
        return UrlBasedViewResolver.REDIRECT_URL_PREFIX + NO_CRITERIA_URL_LINK;
    }
}