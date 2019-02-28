package uk.gov.companieshouse.web.accounts.controller.smallfull;

import org.apache.commons.lang.StringUtils;
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
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.employees.Employees;
import uk.gov.companieshouse.web.accounts.model.state.CompanyAccountsDataState;
import uk.gov.companieshouse.web.accounts.service.smallfull.EmployeesService;
import uk.gov.companieshouse.web.accounts.validation.EmployeesValidator;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Controller
@NextController(ReviewController.class)
@PreviousController(EmployeesQuestionController.class)
@RequestMapping("/company/{companyNumber}/transaction/{transactionId}/company-accounts/{companyAccountsId}/small-full/employees")
public class EmployeesController extends BaseController implements
    ConditionalController {

    @Autowired
    private EmployeesService employeesService;

    @Autowired
    private HttpServletRequest request;

    @Override
    protected String getTemplateName() {
        return "smallfull/employees";
    }

    @GetMapping
    public String getEmployees(
        @PathVariable String companyNumber,
        @PathVariable String transactionId, @PathVariable String companyAccountsId, Model model) {

        addBackPageAttributeToModel(model, companyNumber, transactionId, companyAccountsId);

        try {
            Employees employees =
                employeesService.getEmployees(transactionId, companyAccountsId,
                    companyNumber);

            model.addAttribute("employees", employees);
        } catch (ServiceException e) {
            LOGGER.errorRequest(request, e.getMessage(), e);
            return ERROR_VIEW;
        }
        return getTemplateName();
    }

    @PostMapping
    public String postEmployees(
        @PathVariable String companyNumber,
        @PathVariable String transactionId,
        @PathVariable String companyAccountsId,
        @ModelAttribute("employees") @Valid Employees employees,
        BindingResult bindingResult, Model model) {

        addBackPageAttributeToModel(model, companyNumber, transactionId, companyAccountsId);

        EmployeesValidator employeesValidator = new EmployeesValidator();
        employeesValidator.validate(employees, bindingResult);
        
        if (bindingResult.hasErrors()) {
            return getTemplateName();
        }

        try {
            List<ValidationError> validationErrors =
                employeesService.submitEmployees(transactionId, companyAccountsId,
                    employees, companyNumber);
            
            if ((!validationErrors.isEmpty())) {
                bindValidationErrors(bindingResult, validationErrors);
                return getTemplateName();
            }
        } catch (ServiceException e) {
            LOGGER.errorRequest(request, e.getMessage(), e);
            return ERROR_VIEW;
        }

        return navigatorService.getNextControllerRedirect(this.getClass(), companyNumber, transactionId,
            companyAccountsId);
    }

    @Override
    public boolean willRender(String companyNumber, String transactionId, String companyAccountsId)
        throws ServiceException {

        CompanyAccountsDataState companyAccountsDataState = getStateFromRequest(request);
        return companyAccountsDataState.getHasSelectedEmployeesNote();
    }
}
