package uk.gov.companieshouse.web.accounts.controller.govuk;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.UrlBasedViewResolver;
import org.springframework.web.util.UriTemplate;
import uk.gov.companieshouse.web.accounts.controller.BaseController;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.service.company.CompanyService;
import uk.gov.companieshouse.web.accounts.service.company.OverseasCompanyNumberService;

@Controller
@RequestMapping("/accounts/company/{companyNumber}/details")
public class GovukCompanyDetailController extends BaseController {

    private final CompanyService companyService;
    private final OverseasCompanyNumberService overseasCompanyNumberService;

    public GovukCompanyDetailController(CompanyService companyService,
                                        OverseasCompanyNumberService overseasCompanyNumberService) {
        this.companyService = companyService;
        this.overseasCompanyNumberService = overseasCompanyNumberService;
    }

    private static final UriTemplate SMALL_FULL_STEPS_TO_COMPLETE =
            new UriTemplate("/company/{companyNumber}/small-full/steps-to-complete");

    private static final UriTemplate FILE_ACCOUNTS_DIFFERENTLY =
        new UriTemplate("/company/{companyNumber}/file-these-accounts-differently");

    private static final UriTemplate CIC_STEPS_TO_COMPLETE =
            new UriTemplate("/company/{companyNumber}/cic/steps-to-complete");

    private static final String TEMPLATE_HEADING = "Confirm company details";

    private static final boolean SHOW_CONTINUE = true;
    private static final String MODEL_ATTR_SHOW_CONTINUE = "showContinue";


    @Override
    protected String getTemplateName() {
        return "company/companyDetail";
    }

    @GetMapping
    public String getCompanyDetail(@PathVariable String companyNumber, Model model, HttpServletRequest request) {

        try {
            model.addAttribute("companyDetail", companyService.getCompanyDetail(companyNumber));
        } catch (ServiceException e) {
            LOGGER.errorRequest(request, e.getMessage(), e);
            return ERROR_VIEW;
        }

        model.addAttribute(MODEL_ATTR_SHOW_CONTINUE, SHOW_CONTINUE);
        model.addAttribute("templateHeading", TEMPLATE_HEADING);

        model.addAttribute("backButton", "/company-lookup/search?forward=%2Faccounts%2Fcompany%2F%7BcompanyNumber%7D%2Fdetails");

        return getTemplateName();
    }

    @PostMapping
    public String postCompanyDetail(@PathVariable String companyNumber, HttpServletRequest request) {

        try {
            if (overseasCompanyNumberService.isOverseasCompany(companyNumber)) {
                return UrlBasedViewResolver.REDIRECT_URL_PREFIX +
                    FILE_ACCOUNTS_DIFFERENTLY.expand(companyNumber);
            }

            if (BooleanUtils.isTrue(companyService.getCompanyProfile(companyNumber)
                    .isCommunityInterestCompany())) {

                return UrlBasedViewResolver.REDIRECT_URL_PREFIX +
                    CIC_STEPS_TO_COMPLETE.expand(companyNumber);
            } else {

                return UrlBasedViewResolver.REDIRECT_URL_PREFIX +
                    SMALL_FULL_STEPS_TO_COMPLETE.expand(companyNumber);
            }
        } catch (ServiceException e) {
            LOGGER.errorRequest(request, e.getMessage(), e);
            return ERROR_VIEW;
        }
    }
}

