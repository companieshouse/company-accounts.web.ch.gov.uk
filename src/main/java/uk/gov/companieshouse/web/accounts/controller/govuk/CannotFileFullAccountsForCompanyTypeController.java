package uk.gov.companieshouse.web.accounts.controller.govuk;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import uk.gov.companieshouse.web.accounts.controller.BaseController;

@Controller
@RequestMapping("/accounts/cannot-file-full-accounts-for-company-type")
public class CannotFileFullAccountsForCompanyTypeController extends BaseController {

    @Override
    protected String getTemplateName() {
        return "smallfull/cannotFileFullAccountsForCompanyType";
    }

    @GetMapping
    public String getStopPage(Model model) {
        model.addAttribute("backButton", "/company-lookup/search?forward=%2Faccounts%2Fcompany%2F%7BcompanyNumber%7D%2Fdetails");

        return getTemplateName();
    }
}

