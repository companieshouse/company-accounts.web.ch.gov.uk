package uk.gov.companieshouse.web.accounts.controller.govuk;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import uk.gov.companieshouse.web.accounts.annotation.PreviousController;
import uk.gov.companieshouse.web.accounts.controller.BaseController;

@Controller
@PreviousController(GovukCriteriaController.class)
@RequestMapping("/accounts/alternative-filing-options")
public class GovukAlternativeFilingOptionsController extends BaseController {

    @Override
    protected String getTemplateName() {
        return "smallfull/alternativeFilingOptions";
    }

    @GetMapping
    public String showAlternativeFilingOptionsPage(Model model) {

        model.addAttribute("hideUserBar", true);
        addBackPageAttributeToModel(model);

        return getTemplateName();
    }
}
