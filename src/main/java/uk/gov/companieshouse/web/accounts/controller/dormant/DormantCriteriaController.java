package uk.gov.companieshouse.web.accounts.controller.dormant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import uk.gov.companieshouse.web.accounts.annotation.PreviousController;
import uk.gov.companieshouse.web.accounts.controller.BaseController;

@Controller
@PreviousController(DormantCriteriaController.class)
@RequestMapping("/company/{companyNumber}/dormant/criteria")
public class DormantCriteriaController extends BaseController {

    @Value("${dormant-accounts.uri}")
    private String dormantAccountsUri;

    @GetMapping
    public String getCriteria(@PathVariable String companyNumber, Model model) {
        addBackPageAttributeToModel(model, companyNumber);
        model.addAttribute("dormantAccountsUri", dormantAccountsUri);
        return getTemplateName();
    }

    @Override
    protected String getTemplateName() {
        return "dormant/criteria";
    }
}
