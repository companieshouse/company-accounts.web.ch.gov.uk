package uk.gov.companieshouse.web.accounts.controller.smallfull;

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
import uk.gov.companieshouse.web.accounts.enumeration.NoteType;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.employees.Employees;
import uk.gov.companieshouse.web.accounts.service.NoteService;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.List;

@Controller
@NextController(IntangibleAssetsNoteController.class)
@PreviousController(OtherAccountingPolicyController.class)
@RequestMapping("/company/{companyNumber}/transaction/{transactionId}/company-accounts" +
        "/{companyAccountsId}/small-full/employees")
public class EmployeesController extends BaseController {

    @Autowired
    private NoteService<Employees> employeesService;

    @Override
    protected String getTemplateName() {
        return "smallfull/employees";
    }

    @GetMapping
    public String getEmployees(
            @PathVariable String companyNumber,
            @PathVariable String transactionId, @PathVariable String companyAccountsId,
            Model model, HttpServletRequest request) {

        addBackPageAttributeToModel(model, companyNumber, transactionId, companyAccountsId);

        try {
            Employees employees =
                    employeesService.get(transactionId, companyAccountsId,
                            NoteType.SMALL_FULL_EMPLOYEES);

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
            BindingResult bindingResult, Model model, HttpServletRequest request) {

        addBackPageAttributeToModel(model, companyNumber, transactionId, companyAccountsId);

        if (bindingResult.hasErrors()) {
            return getTemplateName();
        }

        try {
            List<ValidationError> validationErrors =
                    employeesService.submit(transactionId, companyAccountsId,
                            employees, NoteType.SMALL_FULL_EMPLOYEES);

            if ((! validationErrors.isEmpty())) {
                bindValidationErrors(bindingResult, validationErrors);
                return getTemplateName();
            }
        } catch (ServiceException e) {
            LOGGER.errorRequest(request, e.getMessage(), e);
            return ERROR_VIEW;
        }

        return navigatorService.getNextControllerRedirect(this.getClass(), companyNumber,
                transactionId,
                companyAccountsId);
    }

}
