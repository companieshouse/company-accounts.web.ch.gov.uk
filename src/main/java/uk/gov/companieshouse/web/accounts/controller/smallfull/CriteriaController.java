package uk.gov.companieshouse.web.accounts.controller.smallfull;

import javax.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import uk.gov.companieshouse.web.accounts.annotation.NextController;
import uk.gov.companieshouse.web.accounts.controller.BaseController;
import uk.gov.companieshouse.web.accounts.model.smallfull.Criteria;
import uk.gov.companieshouse.web.accounts.util.Navigator;

@Controller
@NextController(StepsToCompleteController.class)
@RequestMapping("/company/{companyNumber}/small-full/criteria")
public class CriteriaController extends BaseController {

    private static final String TEMPLATE = "smallfull/criteria";

    @GetMapping
    public String getCriteria(Model model) {

        Criteria criteria = new Criteria();
        model.addAttribute("criteria", criteria);

        return TEMPLATE;
    }

    @PostMapping
    public String postCriteria(@PathVariable String companyNumber,
                               @ModelAttribute("criteria") @Valid Criteria criteria,
                               BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return TEMPLATE;
        }

        if (!criteria.getIsCriteriaMet().equalsIgnoreCase("yes")) {
            // TODO: Temporarily return the criteria page until a 'criteria not met' page is created
            return TEMPLATE;
        }

        return Navigator.getNextControllerRedirect(this.getClass(), companyNumber);
    }
}
