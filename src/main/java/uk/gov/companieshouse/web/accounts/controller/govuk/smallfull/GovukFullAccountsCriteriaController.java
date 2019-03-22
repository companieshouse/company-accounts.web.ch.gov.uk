package uk.gov.companieshouse.web.accounts.controller.govuk.smallfull;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.UrlBasedViewResolver;
import uk.gov.companieshouse.web.accounts.controller.BaseController;

@Controller
@RequestMapping("/accounts/full-accounts-criteria")
public class GovukFullAccountsCriteriaController extends BaseController{

    @Value("${chs.url}")
    private String chsUrl;

    @GetMapping
    public String getCriteria(Model model){
        model.addAttribute("hideUserBar" , true);
        return getTemplateName();
    }

    @PostMapping
    public String postFullAccountsCriteria(){
        return UrlBasedViewResolver.REDIRECT_URL_PREFIX + chsUrl;
    }

    @Override
    protected String getTemplateName() {
        return "govuk/smallfull/criteria";
    }
}