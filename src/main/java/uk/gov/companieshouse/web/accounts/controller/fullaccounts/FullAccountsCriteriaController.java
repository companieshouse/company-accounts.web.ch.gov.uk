package uk.gov.companieshouse.web.accounts.controller.fullaccounts;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import uk.gov.companieshouse.web.accounts.controller.BaseController;

@Controller
@RequestMapping("/accounts/full-accounts-criteria")
public class FullAccountsCriteriaController extends BaseController{

    @Override
    protected String getTemplateName() {
        return "fullaccounts/criteria";
    }

    @GetMapping
    public String getCriteria(){
        return getTemplateName();
    }
}

