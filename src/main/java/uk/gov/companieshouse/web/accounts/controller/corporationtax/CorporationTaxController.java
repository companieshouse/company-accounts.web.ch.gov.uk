package uk.gov.companieshouse.web.accounts.controller.corporationtax;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import uk.gov.companieshouse.web.accounts.annotation.NextController;
import uk.gov.companieshouse.web.accounts.controller.BaseController;
import uk.gov.companieshouse.web.accounts.controller.smallfull.CriteriaController;
import uk.gov.companieshouse.web.accounts.model.corporationtax.CorporationTax;

import javax.validation.Valid;

@Controller
@NextController(CriteriaController.class)
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

        if (corporationTax.getFileChoice().equals(true)) {
            return "redirect:" + "https://www.gov.uk/file-your-company-accounts-and-tax-return";
        }

        return navigatorService.getNextControllerRedirect(this.getClass(), companyNumber);
    }

    @Override
    protected String getTemplateName() {

        return "corporationtax/corporationTax";
    }
}
