package uk.gov.companieshouse.web.accounts.controller.cic;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import uk.gov.companieshouse.web.accounts.annotation.NextController;
import uk.gov.companieshouse.web.accounts.controller.BaseController;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.company.CompanyDetail;
import uk.gov.companieshouse.web.accounts.service.company.CompanyService;

@Controller
@NextController(CICStepsToCompleteController.class)
@RequestMapping("/accounts/company/{companyNumber}/cic/details")
public class CicCompanyDetailController extends BaseController {

    @Autowired
    private CompanyService companyService;

    @Override
    protected String getTemplateName() {
        return "company/companyDetail";
    }

    private static final String TEMPLATE_HEADING_IS_CIC = "Confirm company details";

    private static final String TEMPLATE_HEADING_ACC_NOT_DUE = "This CIC’s annual accounts are not due yet";
    private static final String TEMPLATE_LINK_ACC_TEXT = "File CIC report and accounts for a different CIC";
    private static final String TEMPLATE_LINK_ACC_URL = "/company-lookup/search?forward=%2Faccounts%2Fcompany%2F%7BcompanyNumber%7D%2Fcic%2Fdetails";
    private static final String TEMPLATE_EVENT_FILE_DIFF_CIC = "File for a different CIC link";

    private static final String TEMPLATE_HEADING_NOT_CIC = "This is not a Community Interest Company";
    private static final String TEMPLATE_LINK_NOT_CIC_TEXT = "File accounts for other company types";
    private static final String TEMPLATE_LINK_NOT_CIC_URL = "https://www.gov.uk/file-your-company-annual-accounts";
    private static final String TEMPLATE_EVENT_NOT_CIC = "Other company types link";

    private static final String MODEL_ATTR_HEADING = "templateHeading";
    private static final String MODEL_ATTR_LINK_TEXT = "templateLinkText";
    private static final String MODEL_ATTR_LINK_URL = "templateLinkUrl";
    private static final String MODEL_ATTR_EVENT = "eventLink";

    private static final boolean SHOW_CONTINUE = true;
    private static final String MODEL_ATTR_SHOW_CONTINUE = "showContinue";

    @GetMapping
    public String getCompanyDetail(@PathVariable String companyNumber, Model model, HttpServletRequest request) {

        CompanyDetail companyDetail;

        try {
            companyDetail = companyService.getCompanyDetail(companyNumber);
        }
        catch (ServiceException e) {
            LOGGER.errorRequest(request, e.getMessage(), e);
            return ERROR_VIEW;
        }

        model.addAttribute("companyDetail", companyDetail);

        if(Boolean.TRUE.equals(companyDetail.getIsCic())) {

            LocalDate accountsNextMadeUpTo = companyDetail.getAccountsNextMadeUpTo();
            if(accountsNextMadeUpTo != null && accountsNextMadeUpTo.isBefore(LocalDate.now())) {
                model.addAttribute(MODEL_ATTR_HEADING, TEMPLATE_HEADING_IS_CIC);

                model.addAttribute(MODEL_ATTR_SHOW_CONTINUE, SHOW_CONTINUE);
            } else {
                model.addAttribute(MODEL_ATTR_HEADING, TEMPLATE_HEADING_ACC_NOT_DUE);
                model.addAttribute(MODEL_ATTR_LINK_TEXT, TEMPLATE_LINK_ACC_TEXT);
                model.addAttribute(MODEL_ATTR_LINK_URL, TEMPLATE_LINK_ACC_URL);
                model.addAttribute(MODEL_ATTR_EVENT, TEMPLATE_EVENT_FILE_DIFF_CIC);
            }
        }
        else {
            model.addAttribute(MODEL_ATTR_HEADING, TEMPLATE_HEADING_NOT_CIC);
            model.addAttribute(MODEL_ATTR_LINK_TEXT, TEMPLATE_LINK_NOT_CIC_TEXT);
            model.addAttribute(MODEL_ATTR_LINK_URL, TEMPLATE_LINK_NOT_CIC_URL);
            model.addAttribute(MODEL_ATTR_EVENT, TEMPLATE_EVENT_NOT_CIC);
        }

        model.addAttribute("backButton", TEMPLATE_LINK_ACC_URL);

        return getTemplateName();
    }

    @PostMapping
    public String postCompanyDetails(@PathVariable String companyNumber) {

        return navigatorService.getNextControllerRedirect(this.getClass(), companyNumber);
    }
}