package uk.gov.companieshouse.web.accounts.controller.cic;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import uk.gov.companieshouse.web.accounts.annotation.NextController;
import uk.gov.companieshouse.web.accounts.annotation.PreviousController;
import uk.gov.companieshouse.web.accounts.controller.BaseController;

import java.util.Optional;

@Controller
@RequestMapping({"/accounts/cic/full-accounts-criteria" , "/accounts/cic/{companyNumber}/full-accounts-criteria"})
@NextController(CicCriteriaController.class)
@PreviousController(CicSelectAccountTypeController.class)
public class CicFileFullAccountsController extends BaseController {
    @Override
    protected String getTemplateName() {
        return "govuk/smallfull/criteria";
    }

    @GetMapping
    public String getCicFullAccountsCriteria(@PathVariable Optional<String> companyNumber, Model model) {
        if(companyNumber.isPresent()) {
            addBackPageAttributeToModel(model, companyNumber.get());
        } else {
            addBackPageAttributeToModel(model);
        }

        return getTemplateName();
    }

    @PostMapping
    public String postCicFullAccountsCriteria(@PathVariable Optional<String> companyNumber) {
        if (companyNumber.isPresent()) {
            return navigatorService.getNextControllerRedirect(this.getClass(), companyNumber.get());
        } else {
            return navigatorService.getNextControllerRedirect(this.getClass());
        }
    }
}
