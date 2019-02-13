package uk.gov.companieshouse.web.accounts.controller.micro;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import uk.gov.companieshouse.web.accounts.annotation.PreviousController;
import uk.gov.companieshouse.web.accounts.controller.BaseController;

@Controller
@PreviousController(MicroCriteriaController.class)
@RequestMapping("/company/{companyNumber}/micro-entity/criteria")
public class MicroCriteriaController extends BaseController {

    @Value("${micro-entity-accounts.uri}")
    private String microEntityAccountsUri;

    @GetMapping
    public String getCriteria(@PathVariable String companyNumber, Model model) {
        addBackPageAttributeToModel(model, companyNumber);
        model.addAttribute("microEntityAccountsUri", microEntityAccountsUri);

        return getTemplateName();
    }

    @Override
    protected String getTemplateName() {
        return "micro/criteria";
    }
}
