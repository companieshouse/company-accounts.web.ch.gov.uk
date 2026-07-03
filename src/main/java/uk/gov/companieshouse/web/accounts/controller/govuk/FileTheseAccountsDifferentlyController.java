package uk.gov.companieshouse.web.accounts.controller.govuk;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import uk.gov.companieshouse.web.accounts.annotation.PreviousController;
import uk.gov.companieshouse.web.accounts.controller.BaseController;
import uk.gov.companieshouse.web.accounts.controller.smallfull.CriteriaController;

@Controller
@PreviousController(CriteriaController.class)
@RequestMapping("/company/{companyNumber}/file-these-accounts-differently")
public class FileTheseAccountsDifferentlyController extends BaseController {

    @Override
    protected String getTemplateName() {
        return "smallfull/fileTheseAccountsDifferently";
    }

    @GetMapping
    public String getFileTheseAccountsDifferently(@PathVariable String companyNumber, Model model) {

        addBackPageAttributeToModel(model, companyNumber);

        return getTemplateName();
    }
}

