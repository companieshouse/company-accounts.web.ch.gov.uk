package uk.gov.companieshouse.web.accounts.controller.smallfull;

import javax.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.UrlBasedViewResolver;
import uk.gov.companieshouse.web.accounts.annotation.NextController;
import uk.gov.companieshouse.web.accounts.controller.BaseController;
import uk.gov.companieshouse.web.accounts.model.smallfull.Criteria;

@Controller
@NextController(StepsToCompleteController.class)
@RequestMapping("/company/{companyNumber}/small-full/criteria")
public class CriteriaController extends BaseController {

    @GetMapping
    public String getCriteria(Model model) {

        model.addAttribute("criteria", new Criteria());

        return getTemplateName();
    }

    @Override
    protected String getTemplateName() {
        return "smallfull/criteria";
    }

    @PostMapping
    public String postCriteria(@PathVariable String companyNumber,
                               @ModelAttribute("criteria") @Valid Criteria criteria,
                               BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return getTemplateName();
        }

        if (criteria.getIsCriteriaMet().equalsIgnoreCase("noAlternativeFilingMethod")) {

            return UrlBasedViewResolver.REDIRECT_URL_PREFIX + "/company/" + companyNumber
                    + "/submit-abridged-accounts/alternative-filing-options";
        } else if (criteria.getIsCriteriaMet().equalsIgnoreCase("noOtherAccounts")) {

            return UrlBasedViewResolver.REDIRECT_URL_PREFIX + "/company/" + companyNumber
                    + "/select-account-type";
        }

        return navigatorService.getNextControllerRedirect(this.getClass(), companyNumber);
    }
}
