package uk.gov.companieshouse.web.accounts.controller.cic;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import uk.gov.companieshouse.web.accounts.annotation.NextController;
import uk.gov.companieshouse.web.accounts.annotation.PreviousController;
import uk.gov.companieshouse.web.accounts.controller.BaseController;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.cic.statements.CompanyActivitiesAndImpact;
import uk.gov.companieshouse.web.accounts.model.state.CompanyAccountsDataState;
import uk.gov.companieshouse.web.accounts.service.cic.statements.CompanyActivitiesAndImpactService;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

@Controller
@NextController(ConsultationWithStakeholdersSelectionController.class)
@PreviousController(CICStepsToCompleteController.class)
@RequestMapping("/company/{companyNumber}/transaction/{transactionId}/company-accounts/{companyAccountsId}/cic/company-activity")
public class CompanyActivitiesAndImpactController extends BaseController {

    @Autowired
    private CompanyActivitiesAndImpactService companyActivitiesAndImpactService;

    @Override
    protected String getTemplateName() {
        return "cic/companyActivitiesAndImpact";
    }

    @GetMapping
    public String getCompanyActivitiesAndImpact(@PathVariable String companyNumber,
        @PathVariable String transactionId,
        @PathVariable String companyAccountsId,
        Model model,
        HttpServletRequest request) {

        try {
            model.addAttribute("companyActivitiesAndImpact", companyActivitiesAndImpactService.getCompanyActivitiesAndImpact(transactionId, companyAccountsId));
        } catch (ServiceException e) {

            LOGGER.errorRequest(request, e.getMessage(), e);
            return ERROR_VIEW;
        }

        return getTemplateName();
    }

    @PostMapping
    public String postCompanyActivitiesAndImpact(@PathVariable String companyNumber,
        @PathVariable String transactionId,
        @PathVariable String companyAccountsId,
        @ModelAttribute("companyActivitiesAndImpact") @Valid CompanyActivitiesAndImpact companyActivitiesAndImpact,
        BindingResult bindingResult,
        HttpServletRequest request) {

        if (bindingResult.hasErrors()) {
            return getTemplateName();
        }

        try {
            List<ValidationError> validationErrors = companyActivitiesAndImpactService.submitCompanyActivitiesAndImpact(transactionId, companyAccountsId, companyActivitiesAndImpact);

            if (!validationErrors.isEmpty()) {
                bindValidationErrors(bindingResult, validationErrors);
                return getTemplateName();
            }
        } catch (ServiceException e) {

            LOGGER.errorRequest(request, e.getMessage(), e);
            return ERROR_VIEW;
        }

        cacheIsCicCompany(request);
        return navigatorService.getNextControllerRedirect(this.getClass(), companyNumber, transactionId, companyAccountsId);
    }

    private void  cacheIsCicCompany(HttpServletRequest request) {

        CompanyAccountsDataState companyAccountsDataState = getStateFromRequest(request);
        companyAccountsDataState.setIsCic(true);
        updateStateOnRequest(request, companyAccountsDataState);
    }
}
