package uk.gov.companieshouse.web.accounts.controller.govuk;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.UrlBasedViewResolver;
import org.springframework.web.util.UriTemplate;
import uk.gov.companieshouse.web.accounts.annotation.PreviousController;
import uk.gov.companieshouse.web.accounts.controller.BaseController;
import uk.gov.companieshouse.web.accounts.model.accounts.TypeOfAccounts;

import javax.validation.Valid;

@Controller
@PreviousController(GovukCorporationTaxController.class)
@RequestMapping("/accounts/select-account-type")
public class GovukSelectAccountTypeController extends BaseController {

    @Value("${dormant-accounts.uri}")
    private String dormantAccountsUri;

    @Value("${micro-entity-accounts.uri}")
    private String microEntityAccountsUri;

    @Value("${abridged-accounts.uri}")
    private String abridgedAccountsUri;

    private static final UriTemplate FULL_ACCOUNTS_URI =
            new UriTemplate("/accounts/full-accounts-criteria");

    @GetMapping
    public String getTypeOfAccounts(Model model) {

        model.addAttribute("typeOfAccounts", new TypeOfAccounts());
        model.addAttribute("hideUserBar", true);

        return getTemplateName();
    }

    @PostMapping
    public String postTypeOfAccounts(
        @ModelAttribute("typeOfAccounts") @Valid TypeOfAccounts typeOfAccounts,
        BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            return getTemplateName();
        }

        return getReDirectPageURL(typeOfAccounts.getSelectedAccountTypeName());
    }

    private String getReDirectPageURL(String selectedAccount) {

        if ("micros".equalsIgnoreCase(selectedAccount)) {
            return UrlBasedViewResolver.REDIRECT_URL_PREFIX + microEntityAccountsUri;
        }

        if ("abridged".equalsIgnoreCase(selectedAccount)) {
            return UrlBasedViewResolver.REDIRECT_URL_PREFIX + abridgedAccountsUri;
        }

        if ("full".equalsIgnoreCase(selectedAccount)) {
            return UrlBasedViewResolver.REDIRECT_URL_PREFIX + FULL_ACCOUNTS_URI.toString();
        }

        if ("dormant".equalsIgnoreCase(selectedAccount)) {
            return UrlBasedViewResolver.REDIRECT_URL_PREFIX + dormantAccountsUri;
        }

        throw new RuntimeException("Failed to redirect to another webpage");
    }

    @Override
    protected String getTemplateName() {
        return "accountselector/selectAccountType";
    }
}
