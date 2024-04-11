package uk.gov.companieshouse.web.accounts.controller.cic;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.UrlBasedViewResolver;
import org.springframework.web.util.UriTemplate;
import uk.gov.companieshouse.web.accounts.annotation.NextController;
import uk.gov.companieshouse.web.accounts.annotation.PreviousController;
import uk.gov.companieshouse.web.accounts.controller.BaseController;
import uk.gov.companieshouse.web.accounts.model.cic.CicCriteria;

import jakarta.validation.Valid;
import java.util.Optional;

@Controller
@NextController(CicCriteriaController.class)
@PreviousController(CicFileFullAccountsController.class)
@RequestMapping({"/accounts/cic/criteria", "/accounts/cic/{companyNumber}/criteria"})
public class CicCriteriaController extends BaseController {
    private static final String NO_CRITERIA_URL_LINK = "/accounts/cic/cant-file-online-yet";
    private static final UriTemplate NO_CRITERIA_COMP_NUM_LINK =
            new UriTemplate("/accounts/cic/{companyNumber}/cant-file-online-yet");

    private static final UriTemplate YES_CRITERIA_MET_COMP_NUM_LINK =
            new UriTemplate("/company/{companyNumber}/cic/steps-to-complete");

    @Override
    protected String getTemplateName() {
        return "cic/cicCriteria";
    }

    @GetMapping
    public String getCicCriteria(@PathVariable Optional<String> companyNumber, Model model) {
        model.addAttribute("criteria", new CicCriteria());

        if(companyNumber.isPresent()) {
            addBackPageAttributeToModel(model, companyNumber.get());
        } else {
            addBackPageAttributeToModel(model);
        }

        return getTemplateName();
    }

    @PostMapping
    public String postCicCriteria(@PathVariable Optional<String> companyNumber, @ModelAttribute("criteria") @Valid CicCriteria criteria,
                                  BindingResult bindingResult, Model model, RedirectAttributes attributes) {
        if (bindingResult.hasErrors()) {
            if(companyNumber.isPresent()) {
                addBackPageAttributeToModel(model, companyNumber.get());
            } else {
                addBackPageAttributeToModel(model);
            }

            return getTemplateName();
        }

        if(Boolean.TRUE.equals(criteria.getIsCriteriaMet())) {
            if(companyNumber.isPresent()) {
                return UrlBasedViewResolver.REDIRECT_URL_PREFIX +
                        YES_CRITERIA_MET_COMP_NUM_LINK.expand(companyNumber.get()).toString();
            } else {
                attributes.addAttribute("forward", "/accounts/company/{companyNumber}/cic/details");
                return UrlBasedViewResolver.REDIRECT_URL_PREFIX + "/company-lookup/search";
            }
        } else {
            attributes.addAttribute("accountType", "full");

            if(companyNumber.isPresent()) {
                attributes.addAttribute("backLink", "/accounts/cic/"+companyNumber.get()+"/criteria");

                return UrlBasedViewResolver.REDIRECT_URL_PREFIX +
                        NO_CRITERIA_COMP_NUM_LINK.expand(companyNumber.get()).toString();
            } else {
                attributes.addAttribute("backLink", "/accounts/cic/criteria");
                return UrlBasedViewResolver.REDIRECT_URL_PREFIX + NO_CRITERIA_URL_LINK;
            }
        }
    }
}