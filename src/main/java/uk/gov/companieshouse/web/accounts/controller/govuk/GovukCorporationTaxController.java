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
import uk.gov.companieshouse.web.accounts.annotation.NextController;
import uk.gov.companieshouse.web.accounts.controller.BaseController;
import uk.gov.companieshouse.web.accounts.model.corporationtax.CorporationTax;

import javax.validation.Valid;

@Controller
@NextController(GovukSelectAccountTypeController.class)
@RequestMapping("/accounts/corporation-tax")
public class GovukCorporationTaxController extends BaseController {

    @Value("${gov-uk-file-your-accounts.uri}")
    private String govUkFullAccountsUrl;

    @GetMapping
    public String getCorporationTax(Model model) {

        model.addAttribute("corporationTax", new CorporationTax());
        model.addAttribute("backButton", govUkFullAccountsUrl);

        return getTemplateName();
    }

    @PostMapping
    public String postCorporationTax(@ModelAttribute("corporationTax") @Valid CorporationTax corporationTax,
                                     BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return getTemplateName();
        }

        if (corporationTax.getFileChoice()) {
            return UrlBasedViewResolver.REDIRECT_URL_PREFIX +
                "https://www.gov.uk/file-your-company-accounts-and-tax-return";
        }

        return navigatorService.getNextControllerRedirect(this.getClass());
    }

    @Override
    protected String getTemplateName() {

        return "corporationtax/corporationTax";
    }
}
