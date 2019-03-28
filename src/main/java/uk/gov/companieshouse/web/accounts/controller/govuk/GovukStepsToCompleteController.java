package uk.gov.companieshouse.web.accounts.controller.govuk;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.UrlBasedViewResolver;
import uk.gov.companieshouse.web.accounts.annotation.PreviousController;
import uk.gov.companieshouse.web.accounts.controller.BaseController;

@Controller
@RequestMapping("/accounts/stepsToComplete")
@PreviousController(GovukCriteriaController.class)
public class GovukStepsToCompleteController extends BaseController {

    @Value("${chs.url}")
    private String chsUrl;

    @Override
    protected String getTemplateName() {
        return "smallfull/stepsToComplete";
    }

    @GetMapping
    public String getStepsToComplete(Model model) {
        addBackPageAttributeToModel(model);
        return getTemplateName();
    }

    @PostMapping
    public String postFullAccountsCriteria(){
        return UrlBasedViewResolver.REDIRECT_URL_PREFIX + chsUrl;
    }
}
