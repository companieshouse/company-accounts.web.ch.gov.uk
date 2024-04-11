package uk.gov.companieshouse.web.accounts.controller.accountselector;

import jakarta.validation.Valid;
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
import uk.gov.companieshouse.web.accounts.annotation.PreviousController;
import uk.gov.companieshouse.web.accounts.controller.BaseController;
import uk.gov.companieshouse.web.accounts.model.accounts.TypeOfAccounts;

@Controller
@PreviousController(SelectAccountTypeController.class)
@RequestMapping("/company/{companyNumber}/select-account-type")
public class SelectAccountTypeController extends BaseController {
    private static final UriTemplate MICRO_ENTITY_ACCOUNTS_URI =
        new UriTemplate("/company/{companyNumber}/micro-entity/criteria");

    private static final UriTemplate DORMANT_ACCOUNTS_URI =
        new UriTemplate("/company/{companyNumber}/dormant/criteria");

    private static final UriTemplate FULL_ACCOUNTS_URI =
        new UriTemplate("/company/{companyNumber}/small-full/criteria");

    private static final UriTemplate ABRIDGED_ACCOUNTS_URI =
        new UriTemplate("/company/{companyNumber}/submit-abridged-accounts/criteria");

    @GetMapping
    public String getTypeOfAccounts(Model model) {
        model.addAttribute("typeOfAccounts", new TypeOfAccounts());

        return getTemplateName();
    }

    @PostMapping
    public String postTypeOfAccounts(
        @PathVariable String companyNumber,
        @ModelAttribute("typeOfAccounts") @Valid TypeOfAccounts typeOfAccounts,
        BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return getTemplateName();
        }

        return getReDirectPageURL(companyNumber, typeOfAccounts.getSelectedAccountTypeName());
    }

    private String getReDirectPageURL(String companyNumber, String selectedAccount) {
        if ("micro-entity".equalsIgnoreCase(selectedAccount)) {
            return UrlBasedViewResolver.REDIRECT_URL_PREFIX + MICRO_ENTITY_ACCOUNTS_URI
                .expand(companyNumber).toString();
        }

        if ("abridged".equalsIgnoreCase(selectedAccount)) {
            return UrlBasedViewResolver.REDIRECT_URL_PREFIX + ABRIDGED_ACCOUNTS_URI
                .expand(companyNumber).toString();
        }

        if ("full".equalsIgnoreCase(selectedAccount)) {
            return UrlBasedViewResolver.REDIRECT_URL_PREFIX + FULL_ACCOUNTS_URI
                .expand(companyNumber).toString();
        }
        if ("dormant".equalsIgnoreCase(selectedAccount)) {
            return UrlBasedViewResolver.REDIRECT_URL_PREFIX + DORMANT_ACCOUNTS_URI
                .expand(companyNumber).toString();
        }

        return getTemplateName();
    }

    @Override
    protected String getTemplateName() {
        return "accountselector/selectAccountType";
    }
}
