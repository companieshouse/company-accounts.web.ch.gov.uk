package uk.gov.companieshouse.web.accounts.controller.corporationtax;

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
import uk.gov.companieshouse.web.accounts.annotation.NextController;
import uk.gov.companieshouse.web.accounts.controller.BaseController;
import uk.gov.companieshouse.web.accounts.controller.accountselector.SelectAccountTypeController;
import uk.gov.companieshouse.web.accounts.model.corporationtax.CorporationTax;

@Controller
@NextController(SelectAccountTypeController.class)
@RequestMapping("/company/{companyNumber}/corporation-tax")
public class CorporationTaxController extends BaseController {

    @GetMapping
    public String getCorporationTax(Model model) {

        model.addAttribute("corporationTax", new CorporationTax());

        return getTemplateName();
    }

    @PostMapping
    public String postCorporationTax(@PathVariable String companyNumber,
                                     @ModelAttribute("corporationTax") @Valid CorporationTax corporationTax,
                                     BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return getTemplateName();
        }

        if (Boolean.TRUE.equals(corporationTax.getFileChoice())) {
            return UrlBasedViewResolver.REDIRECT_URL_PREFIX +
                    "https://www.gov.uk/file-your-company-accounts-and-tax-return";
        }

        return navigatorService.getNextControllerRedirect(this.getClass(), companyNumber);
    }

    @Override
    protected String getTemplateName() {

        return "corporationtax/corporationTax";
    }
}
