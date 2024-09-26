package uk.gov.companieshouse.web.accounts.controller.smallfull;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
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
import uk.gov.companieshouse.web.accounts.controller.ConditionalController;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.directorsreport.CompanyPolicyOnDisabledEmployees;
import uk.gov.companieshouse.web.accounts.model.state.CompanyAccountsDataState;
import uk.gov.companieshouse.web.accounts.model.state.DirectorsReportStatements;
import uk.gov.companieshouse.web.accounts.service.smallfull.CompanyPolicyOnDisabledEmployeesService;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

@Controller
@NextController(DirectorsReportAdditionalInformationSelectionController.class)
@PreviousController(CompanyPolicyOnDisabledEmployeesSelectionController.class)
@RequestMapping("/company/{companyNumber}/transaction/{transactionId}/company-accounts/{companyAccountsId}/small-full/directors-report/company-policy-on-disabled-employees")
public class CompanyPolicyOnDisabledEmployeesController extends BaseController implements ConditionalController {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private CompanyPolicyOnDisabledEmployeesService companyPolicyOnDisabledEmployeesService;

    private static final String COMPANY_POLICY_ON_DISABLED_EMPLOYEES = "companyPolicyOnDisabledEmployees";

    @Override
    protected String getTemplateName() {
        return "smallfull/companyPolicyOnDisabledEmployees";
    }

    @GetMapping
    public String getCompanyPolicyOnDisabledEmployees(@PathVariable String companyNumber,
                                                      @PathVariable String transactionId,
                                                      @PathVariable String companyAccountsId,
                                                      Model model) {

        addBackPageAttributeToModel(model, companyNumber, transactionId, companyAccountsId);

        try {
            CompanyPolicyOnDisabledEmployees companyPolicyOnDisabledEmployees =
                    companyPolicyOnDisabledEmployeesService.getCompanyPolicyOnDisabledEmployees(transactionId, companyAccountsId);

            model.addAttribute(COMPANY_POLICY_ON_DISABLED_EMPLOYEES, companyPolicyOnDisabledEmployees);

        } catch (ServiceException e) {

            LOGGER.errorRequest(request, e.getMessage(), e);
            return ERROR_VIEW;
        }

        return getTemplateName();
    }

    @PostMapping
    public String submitCompanyPolicyOnDisabledEmployees(@PathVariable String companyNumber,
                                                         @PathVariable String transactionId,
                                                         @PathVariable String companyAccountsId,
                                                         @ModelAttribute(COMPANY_POLICY_ON_DISABLED_EMPLOYEES) @Valid CompanyPolicyOnDisabledEmployees companyPolicyOnDisabledEmployees,
                                                         BindingResult bindingResult,
                                                         Model model) {

        addBackPageAttributeToModel(model, companyNumber, transactionId, companyAccountsId);

        if (bindingResult.hasErrors()) {
            return getTemplateName();
        }

        try {
            List<ValidationError> validationErrors =
                    companyPolicyOnDisabledEmployeesService.submitCompanyPolicyOnDisabledEmployees(transactionId, companyAccountsId, companyPolicyOnDisabledEmployees);

            if (!validationErrors.isEmpty()) {
                bindValidationErrors(bindingResult, validationErrors);
                return getTemplateName();
            }

        } catch (ServiceException e) {

            LOGGER.errorRequest(request, e.getMessage(), e);
            return ERROR_VIEW;
        }

        return navigatorService.getNextControllerRedirect(this.getClass(), companyNumber, transactionId, companyAccountsId);
    }

    @Override
    public boolean willRender(String companyNumber, String transactionId, String companyAccountsId)
            throws ServiceException {

        CompanyAccountsDataState companyAccountsDataState = getStateFromRequest(request);
        return Optional.ofNullable(companyAccountsDataState)
                    .map(CompanyAccountsDataState::getDirectorsReportStatements)
                    .map(DirectorsReportStatements::getHasProvidedCompanyPolicyOnDisabledEmployees)
                    .orElse(false);
    }
}
