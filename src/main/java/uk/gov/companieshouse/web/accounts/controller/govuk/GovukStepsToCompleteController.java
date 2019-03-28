package uk.gov.companieshouse.web.accounts.controller.govuk;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import uk.gov.companieshouse.web.accounts.annotation.PreviousController;
import uk.gov.companieshouse.web.accounts.controller.BaseController;

@Controller
@PreviousController(GovukCriteriaController.class)
@RequestMapping("/accounts/steps-to-complete")
public class GovukStepsToCompleteController extends BaseController {

    @Override
    protected String getTemplateName() {
        return "smallfull/stepsToComplete";
    }

    @GetMapping
    public String getStepsToComplete(Model model) {

        addBackPageAttributeToModel(model);

        return getTemplateName();
    }
}
