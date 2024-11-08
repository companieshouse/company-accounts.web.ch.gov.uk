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
import uk.gov.companieshouse.web.accounts.exception.NavigationException;
import uk.gov.companieshouse.web.accounts.model.accounts.TypeOfAccounts;

@Controller
@NextController(GovukFullAccountsCriteriaController.class)
@PreviousController(GovukCorporationTaxController.class)
@RequestMapping("/accounts/select-account-type")
public class GovukSelectAccountTypeController extends BaseController {

    @Value("${dormant-accounts.uri}")
    private String dormantAccountsUri;

    @Value("${micro-entity-accounts.uri}")
    private String microEntityAccountsUri;

    @Value("${abridged-accounts.uri}")
    private String abridgedAccountsUri;

    @Value("${package-accounts.uri}")
    private String packageAccountsUri;

    @GetMapping
    public String getTypeOfAccounts(Model model) {

        model.addAttribute("typeOfAccounts", new TypeOfAccounts());
        addBackPageAttributeToModel(model);

        return getTemplateName();
    }

    @PostMapping
    public String postTypeOfAccounts(
            @ModelAttribute("typeOfAccounts") @Valid TypeOfAccounts typeOfAccounts,
            BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {

        addBackPageAttributeToModel(model);

        if (bindingResult.hasErrors()) {
            return getTemplateName();
        }

        try {
            redirectAttributes.addFlashAttribute("backButton", model.getAttribute("backButton"));
            redirectAttributes.addFlashAttribute("typeOfAccounts", typeOfAccounts);
            redirectAttributes.addFlashAttribute("templateName", getTemplateName());
        } catch (NullPointerException e){
            LOGGER.error("model.getAttribute(\"backButton\") was null :\n" + e.getMessage(), e);
            return ERROR_VIEW;
        }

        return getReDirectPageURL(typeOfAccounts.getSelectedAccountTypeName());
    }

    private String getReDirectPageURL(String selectedAccount) {

        if ("micro-entity".equalsIgnoreCase(selectedAccount)) {
            return UrlBasedViewResolver.REDIRECT_URL_PREFIX + microEntityAccountsUri;
        }

        if ("abridged".equalsIgnoreCase(selectedAccount)) {
            return UrlBasedViewResolver.REDIRECT_URL_PREFIX + abridgedAccountsUri;
        }

        if ("full".equalsIgnoreCase(selectedAccount)) {
            return navigatorService.getNextControllerRedirect(this.getClass());
        }

        if ("dormant".equalsIgnoreCase(selectedAccount)) {
            return UrlBasedViewResolver.REDIRECT_URL_PREFIX + dormantAccountsUri;
        }

        if ("package".equalsIgnoreCase(selectedAccount)) {
            return UrlBasedViewResolver.REDIRECT_URL_PREFIX + packageAccountsUri;
        }

        throw new NavigationException("Failed to redirect to another webpage");
    }

    @Override
    protected String getTemplateName() {
        return "accountselector/selectAccountType";
    }
}
