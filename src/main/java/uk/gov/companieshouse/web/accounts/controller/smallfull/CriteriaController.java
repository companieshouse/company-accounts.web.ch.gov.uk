package uk.gov.companieshouse.web.accounts.controller.smallfull;

import jakarta.validation.Valid;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.UrlBasedViewResolver;
import org.springframework.web.util.UriTemplate;
import uk.gov.companieshouse.web.accounts.annotation.NextController;
import uk.gov.companieshouse.web.accounts.annotation.PreviousController;
import uk.gov.companieshouse.web.accounts.controller.BaseController;
import uk.gov.companieshouse.web.accounts.controller.accountselector.SelectAccountTypeController;
import uk.gov.companieshouse.web.accounts.model.smallfull.Criteria;

@Controller
@NextController(StepsToCompleteController.class)
@PreviousController(SelectAccountTypeController.class)
@RequestMapping("/company/{companyNumber}/small-full/criteria")
public class CriteriaController extends BaseController {

    @Value("${overseas.company.prefixes}")
    private String overseasCompanyPrefixes;

    private static final UriTemplate FILE_ACCOUNTS_DIFFERENTLY =
        new UriTemplate("/company/{companyNumber}/file-these-accounts-differently");

    @GetMapping
    public String getCriteria(@PathVariable String companyNumber, Model model) {

        addBackPageAttributeToModel(model, companyNumber);
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
                               BindingResult bindingResult, Model model) {
        if (isOverseasCompany(companyNumber)) {
            return UrlBasedViewResolver.REDIRECT_URL_PREFIX +
                FILE_ACCOUNTS_DIFFERENTLY.expand(companyNumber).toString();
        }

        addBackPageAttributeToModel(model, companyNumber);

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

    private boolean isOverseasCompany(String companyNumber) {

        String companyNumberUpperCase = companyNumber.toUpperCase();
        List<String> prefixes = Arrays.asList(overseasCompanyPrefixes.split(","));

        return prefixes.stream()
            .anyMatch(prefix -> companyNumberUpperCase.startsWith(prefix.trim()));
    }
}
