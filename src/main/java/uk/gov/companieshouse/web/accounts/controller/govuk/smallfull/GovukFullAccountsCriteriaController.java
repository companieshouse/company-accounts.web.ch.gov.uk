package uk.gov.companieshouse.web.accounts.controller.govuk.smallfull;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import uk.gov.companieshouse.web.accounts.annotation.NextController;
import uk.gov.companieshouse.web.accounts.annotation.PreviousController;
import uk.gov.companieshouse.web.accounts.controller.BaseController;
import uk.gov.companieshouse.web.accounts.controller.govuk.GovukCriteriaController;
import uk.gov.companieshouse.web.accounts.controller.govuk.GovukSelectAccountTypeController;

@Controller
@NextController(GovukCriteriaController.class)
@PreviousController(GovukSelectAccountTypeController.class)
@RequestMapping("/accounts/full-accounts-criteria")
public class GovukFullAccountsCriteriaController extends BaseController{
    @Override
    protected String getTemplateName() {
        return "govuk/smallfull/criteria";
    }

    @GetMapping
    public String getCriteria(Model model){
        addBackPageAttributeToModel(model);

        return getTemplateName();
    }

    @PostMapping
    public String postFullAccountsCriteria(){
        return navigatorService.getNextControllerRedirect(this.getClass());
    }
}