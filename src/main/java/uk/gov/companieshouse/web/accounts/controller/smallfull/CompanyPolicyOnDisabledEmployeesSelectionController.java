package uk.gov.companieshouse.web.accounts.controller.smallfull;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
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
import uk.gov.companieshouse.web.accounts.api.ApiClientService;
import uk.gov.companieshouse.web.accounts.controller.BaseController;
import uk.gov.companieshouse.web.accounts.controller.ConditionalController;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.directorsreport.CompanyPolicyOnDisabledEmployeesSelection;
import uk.gov.companieshouse.web.accounts.model.state.CompanyAccountsDataState;
import uk.gov.companieshouse.web.accounts.model.state.DirectorsReportStatements;
import uk.gov.companieshouse.web.accounts.service.smallfull.CompanyPolicyOnDisabledEmployeesSelectionService;
import uk.gov.companieshouse.web.accounts.service.smallfull.DirectorsReportService;

@Controller
@NextController(CompanyPolicyOnDisabledEmployeesController.class)
@PreviousController(PoliticalAndCharitableDonationsController.class)
@RequestMapping("/company/{companyNumber}/transaction/{transactionId}/company-accounts/{companyAccountsId}/small-full/directors-report/company-policy-on-disabled-employees-question")
public class CompanyPolicyOnDisabledEmployeesSelectionController extends BaseController implements ConditionalController {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private CompanyPolicyOnDisabledEmployeesSelectionService selectionService;

    @Autowired
    private DirectorsReportService directorsReportService;

    @Autowired
    private ApiClientService apiClientService;

    private static final String COMPANY_POLICY_ON_DISABLED_EMPLOYEES_SELECTION = "companyPolicyOnDisabledEmployeesSelection";

    @Override
    protected String getTemplateName() {
        return "smallfull/companyPolicyOnDisabledEmployeesSelection";
    }

    @GetMapping
    public String getCompanyPolicyOnDisabledEmployeesSelection(@PathVariable String companyNumber,
                                                               @PathVariable String transactionId,
                                                               @PathVariable String companyAccountsId,
                                                               Model model) {

        addBackPageAttributeToModel(model, companyNumber, transactionId, companyAccountsId);

        try {
            CompanyPolicyOnDisabledEmployeesSelection selection =
                    selectionService.getCompanyPolicyOnDisabledEmployeesSelection(transactionId, companyAccountsId);

            if (selection.getHasCompanyPolicyOnDisabledEmployees() == null) {
                setHasProvidedCompanyPolicyOnDisabledEmployees(request, selection);
            }

            model.addAttribute(COMPANY_POLICY_ON_DISABLED_EMPLOYEES_SELECTION, selection);

        } catch (ServiceException e) {

            LOGGER.errorRequest(request, e.getMessage(), e);
            return ERROR_VIEW;
        }

        return getTemplateName();
    }

    @PostMapping
    public String submitCompanyPolicyOnDisabledEmployeesSelection(@PathVariable String companyNumber,
                                                                  @PathVariable String transactionId,
                                                                  @PathVariable String companyAccountsId,
                                                                  @ModelAttribute(COMPANY_POLICY_ON_DISABLED_EMPLOYEES_SELECTION) @Valid CompanyPolicyOnDisabledEmployeesSelection selection,
                                                                  BindingResult bindingResult,
                                                                  Model model) {

        addBackPageAttributeToModel(model, companyNumber, transactionId, companyAccountsId);

        if (bindingResult.hasErrors()) {
            return getTemplateName();
        }

        try {
            selectionService.submitCompanyPolicyOnDisabledEmployeesSelection(transactionId, companyAccountsId, selection);

        } catch (ServiceException e) {

            LOGGER.errorRequest(request, e.getMessage(), e);
            return ERROR_VIEW;
        }

        cacheHasProvidedCompanyPolicyOnDisabledEmployees(request, selection);

        return navigatorService.getNextControllerRedirect(this.getClass(), companyNumber, transactionId, companyAccountsId);
    }

    private void setHasProvidedCompanyPolicyOnDisabledEmployees(HttpServletRequest request, CompanyPolicyOnDisabledEmployeesSelection selection) {

        CompanyAccountsDataState companyAccountsDataState = getStateFromRequest(request);
        selection.setHasCompanyPolicyOnDisabledEmployees(
                Optional.of(companyAccountsDataState)
                        .map(CompanyAccountsDataState::getDirectorsReportStatements)
                        .map(DirectorsReportStatements::getHasProvidedCompanyPolicyOnDisabledEmployees)
                        .orElse(null));
    }

    private void cacheHasProvidedCompanyPolicyOnDisabledEmployees(HttpServletRequest request, CompanyPolicyOnDisabledEmployeesSelection selection) {

        CompanyAccountsDataState companyAccountsDataState = getStateFromRequest(request);

        if (companyAccountsDataState.getDirectorsReportStatements() == null) {
            companyAccountsDataState.setDirectorsReportStatements(new DirectorsReportStatements());
        }
        
        companyAccountsDataState.getDirectorsReportStatements().setHasProvidedCompanyPolicyOnDisabledEmployees(
                selection.getHasCompanyPolicyOnDisabledEmployees());

        updateStateOnRequest(request, companyAccountsDataState);
    }

    @Override
    public boolean willRender(String companyNumber, String transactionId, String companyAccountsId)
            throws ServiceException {

        return directorsReportService.getDirectorsReport(apiClientService.getApiClient(), transactionId, companyAccountsId) != null;
    }
}
