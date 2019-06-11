package uk.gov.companieshouse.web.accounts.controller.cic;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import uk.gov.companieshouse.web.accounts.annotation.NextController;
import uk.gov.companieshouse.web.accounts.controller.BaseController;
import uk.gov.companieshouse.web.accounts.model.smallfull.Criteria;

@Controller
@NextController(CicCriteriaController.class)
@RequestMapping("/accounts/cic/criteria")
public class CicCriteriaController extends BaseController {

    @Override
    protected String getTemplateName() {
        return "cic/cicCriteria";
    }

    @GetMapping
    public String getCicCriteria(Model model) {

        model.addAttribute("criteria", new Criteria());
        addBackPageAttributeToModel(model);

        return getTemplateName();
    }
}