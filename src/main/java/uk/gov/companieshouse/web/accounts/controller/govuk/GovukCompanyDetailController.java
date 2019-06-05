package uk.gov.companieshouse.web.accounts.controller.govuk;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import uk.gov.companieshouse.web.accounts.annotation.NextController;
import uk.gov.companieshouse.web.accounts.controller.BaseController;
import uk.gov.companieshouse.web.accounts.controller.smallfull.StepsToCompleteController;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.service.company.CompanyService;

import javax.servlet.http.HttpServletRequest;

@Controller
@NextController(StepsToCompleteController.class)
@RequestMapping("/accounts/company/{companyNumber}/details")
public class GovukCompanyDetailController extends BaseController {

    @Autowired
    private CompanyService companyService;

    private static final String TEMPLATE_HEADING = "Confirm company details";

    @Override
    protected String getTemplateName() {
        return "company/companyDetail";
    }

    @GetMapping
    public String getCompanyDetail(@PathVariable String companyNumber, Model model, HttpServletRequest request) {

        try {
            model.addAttribute("companyDetail", companyService.getCompanyDetail(companyNumber));
            model.addAttribute("templateHeading", TEMPLATE_HEADING);
        } catch (ServiceException e) {
            LOGGER.errorRequest(request, e.getMessage(), e);
            return ERROR_VIEW;
        }

        return getTemplateName();
    }

    @PostMapping
    public String postCompanyDetail(@PathVariable String companyNumber) {

        return navigatorService.getNextControllerRedirect(this.getClass(), companyNumber);
    }
}