package uk.gov.companieshouse.web.accounts.controller.cic;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import uk.gov.companieshouse.web.accounts.annotation.NextController;
import uk.gov.companieshouse.web.accounts.annotation.PreviousController;
import uk.gov.companieshouse.web.accounts.controller.BaseController;

@Controller
@RequestMapping("/accounts/cic/full-accounts-criteria")
@NextController(CicCriteriaController.class)
@PreviousController(CicSelectAccountTypeController.class)
public class CicFileFullAccountsController extends BaseController {

    @Override
    protected String getTemplateName() {
        return "govuk/smallfull/criteria";
    }

    @GetMapping
    public String getCicFullAccountsCriteria(Model model) {

        addBackPageAttributeToModel(model);

        return getTemplateName();
    }

    @PostMapping
    public String postCicFullAccountsCriteria() {

        return navigatorService.getNextControllerRedirect(this.getClass());
    }
}
