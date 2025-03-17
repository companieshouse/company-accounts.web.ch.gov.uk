package uk.gov.companieshouse.web.accounts.controller.accountselector;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.UrlBasedViewResolver;
import org.springframework.web.util.UriTemplate;
import uk.gov.companieshouse.web.accounts.annotation.PreviousController;
import uk.gov.companieshouse.web.accounts.controller.BaseController;
import uk.gov.companieshouse.web.accounts.model.accounts.TypeOfAccounts;
import uk.gov.companieshouse.web.accounts.util.AccountType;

@Controller
@PreviousController(SelectAccountTypeController.class)
@RequestMapping("/company/{companyNumber}/select-account-type")
public class SelectAccountTypeController extends BaseController {

    private static final String TEMPLATE_NAME = "templateName";

    private static final String TYPE_OF_ACCOUNTS = "typeOfAccounts";

    private static final UriTemplate MICRO_ENTITY_ACCOUNTS_URI =
            new UriTemplate("/company/{companyNumber}/micro-entity/criteria");

    private static final UriTemplate DORMANT_ACCOUNTS_URI =
            new UriTemplate("/company/{companyNumber}/dormant/criteria");

    private static final UriTemplate FULL_ACCOUNTS_URI =
            new UriTemplate("/company/{companyNumber}/small-full/criteria");

    private static final UriTemplate ABRIDGED_ACCOUNTS_URI =
            new UriTemplate("/company/{companyNumber}/submit-abridged-accounts/criteria");

    private static final UriTemplate PACKAGE_ACCOUNTS_URI =
            new UriTemplate("/accounts-filing/company/{companyNumber}");

    @Value("${package-accounts.enabled}")
    private String packageAccountsEnabled;

    @Value("${dormant-accounts.enabled}")
    private String dormantAccountsEnabled;

    @Value("${micro-accounts.enabled}")
    private String microEntityAccountsEnabled;

    @Value("${abridged-accounts.enabled}")
    private String abridgedAccountsEnabled;

    @GetMapping
    public String getTypeOfAccounts(Model model) {

        model.addAttribute(TYPE_OF_ACCOUNTS, new TypeOfAccounts());
        enableAccountsAttributesToModel(model);

        return getTemplateName();
    }

    @PostMapping
    public String postTypeOfAccounts(
            @PathVariable String companyNumber,
            @ModelAttribute(TYPE_OF_ACCOUNTS) @Valid TypeOfAccounts typeOfAccounts,
            BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            enableAccountsAttributesToModel(model);
            return getTemplateName();
        }

        redirectAttributes.addFlashAttribute(TYPE_OF_ACCOUNTS, typeOfAccounts);
        redirectAttributes.addFlashAttribute(TEMPLATE_NAME, getTemplateName());

        return getReDirectPageURL(companyNumber, typeOfAccounts.getSelectedAccountTypeName());
    }

    private String getReDirectPageURL(String companyNumber, String selectedAccount) {

        if (AccountType.MICRO_ACCOUNT.equalsIgnoreCase(selectedAccount)) {
            return UrlBasedViewResolver.REDIRECT_URL_PREFIX + MICRO_ENTITY_ACCOUNTS_URI
                    .expand(companyNumber).toString();
        }

        if (AccountType.ABRIDGED_ACCOUNT.equalsIgnoreCase(selectedAccount)) {
            return UrlBasedViewResolver.REDIRECT_URL_PREFIX + ABRIDGED_ACCOUNTS_URI
                    .expand(companyNumber).toString();
        }

        if (AccountType.FULL_ACCOUNT.equalsIgnoreCase(selectedAccount)) {
            return UrlBasedViewResolver.REDIRECT_URL_PREFIX + FULL_ACCOUNTS_URI
                    .expand(companyNumber).toString();
        }
        if (AccountType.DORMANT_ACCOUNT.equalsIgnoreCase(selectedAccount)) {
            return UrlBasedViewResolver.REDIRECT_URL_PREFIX + DORMANT_ACCOUNTS_URI
                    .expand(companyNumber).toString();
        }
        if (AccountType.PACKAGE_ACCOUNT.equalsIgnoreCase(selectedAccount)) {
            return UrlBasedViewResolver.REDIRECT_URL_PREFIX + PACKAGE_ACCOUNTS_URI.expand(companyNumber);
        }

        return getTemplateName();
    }

    @Override
    protected String getTemplateName() {
        return "accountselector/selectAccountType";
    }

    private void enableAccountsAttributesToModel(Model model) {
        model.addAttribute(AccountType.PACKAGE_ACCOUNT.getModelAttribute(), packageAccountsEnabled);
        model.addAttribute(AccountType.DORMANT_ACCOUNT.getModelAttribute(), dormantAccountsEnabled);
        model.addAttribute(AccountType.MICRO_ACCOUNT.getModelAttribute(), microEntityAccountsEnabled);
        model.addAttribute(AccountType.ABRIDGED_ACCOUNT.getModelAttribute(), abridgedAccountsEnabled);
    }
}
