package uk.gov.companieshouse.web.accounts.controller.fullaccounts;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.UrlBasedViewResolver;
import uk.gov.companieshouse.web.accounts.controller.BaseController;

@Controller
@RequestMapping("/accounts/full-accounts-criteria")
public class FullAccountsCriteriaController extends BaseController{

    @GetMapping
    public String getCriteria(){
        return getTemplateName();
    }

    @Override
    protected String getTemplateName() {
        return "fullaccounts/criteria";
    }

    @PostMapping
    public String postFullAccountsCriteria(){
        return UrlBasedViewResolver.REDIRECT_URL_PREFIX + "/";
    }


}

