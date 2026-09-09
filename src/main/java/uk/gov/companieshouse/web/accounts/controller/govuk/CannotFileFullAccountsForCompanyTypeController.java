package uk.gov.companieshouse.web.accounts.controller.govuk;

import org.springframework.stereotype.Controller;
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
    public String getStopPage() {
        return getTemplateName();
    }
}

