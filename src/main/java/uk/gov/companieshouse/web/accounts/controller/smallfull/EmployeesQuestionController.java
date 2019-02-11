package uk.gov.companieshouse.web.accounts.controller.smallfull;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
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
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.employees.Employees;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.employees.EmployeesQuestion;
import uk.gov.companieshouse.web.accounts.model.state.CompanyAccountsDataState;
import uk.gov.companieshouse.web.accounts.service.smallfull.EmployeesService;

@Controller
@NextController(EmployeesController.class)
@PreviousController(CreditorsWithinOneYearController.class)
@RequestMapping("/company/{companyNumber}/transaction/{transactionId}/company-accounts/{companyAccountsId}/small-full/employees-question")
public class EmployeesQuestionController extends BaseController {

    private static final String EMPLOYEES_QUESTION = "employeesQuestion";

    @Autowired
    private EmployeesService employeesService;

    @GetMapping
    public String getEmployeesQuestion(@PathVariable String companyNumber,
                                                  @PathVariable String transactionId,
                                                  @PathVariable String companyAccountsId,
                                                  Model model,
                                                  HttpServletRequest request) {

        addBackPageAttributeToModel(model, companyNumber, transactionId, companyAccountsId);

        EmployeesQuestion employeesQuestion = new EmployeesQuestion();
        try {
            Employees employees =
                    employeesService.getEmployees(transactionId, companyAccountsId, companyNumber);

            if (!employeesNoteProvided(employees)) {
                setIsEmployeesIncluded(request, employeesQuestion);
            } else {
                employeesQuestion.setHasSelectedEmployeesNote(true);
            }

            model.addAttribute(EMPLOYEES_QUESTION, employeesQuestion);
        } catch (ServiceException e) {

            LOGGER.errorRequest(request, e.getMessage(), e);
            return ERROR_VIEW;
        }

        return getTemplateName();
    }
    
    private boolean employeesNoteProvided(Employees employees) {
      return employees != null && 
        (employees.getAverageNumberOfEmployees() != null 
        || employees.getDetails() != null);
    }

    @PostMapping
    public String submitEmployeesQuestion(@PathVariable String companyNumber,
                                                     @PathVariable String transactionId,
                                                     @PathVariable String companyAccountsId,
                                                     @ModelAttribute(EMPLOYEES_QUESTION) @Valid EmployeesQuestion employeesQuestion,
                                                     BindingResult bindingResult,
                                                     Model model,
                                                     HttpServletRequest request) {

        addBackPageAttributeToModel(model, companyNumber, transactionId, companyAccountsId);

        if (bindingResult.hasErrors()) {
            return getTemplateName();
        }

        cacheIsEmployeesIncluded(request, employeesQuestion);

        return navigatorService.getNextControllerRedirect(this.getClass(), companyNumber, transactionId, companyAccountsId);
    }

    @Override
    protected String getTemplateName() {
        return "smallfull/employeesQuestion";
    }

    /**
     * Sets the hasSelectedEmployeesNote Boolean according to the cached state
     * @param request The request
     * @param employeesQuestion The employeesQuestion model on which to set the boolean
     */
    private void setIsEmployeesIncluded(HttpServletRequest request, EmployeesQuestion employeesQuestion) {

        CompanyAccountsDataState companyAccountsDataState = getStateFromRequest(request);
        employeesQuestion.setHasSelectedEmployeesNote(companyAccountsDataState.getHasSelectedEmployeesNote());
    }

    /**
     * Cache the hasSelectedEmployeesNote Boolean within the client's state
     * @param request The request
     * @param employeesQuestion The employeesQuestion model for which to cache data
     */
    private void  cacheIsEmployeesIncluded(HttpServletRequest request, EmployeesQuestion employeesQuestion) {

        CompanyAccountsDataState companyAccountsDataState = getStateFromRequest(request);
        companyAccountsDataState.setHasSelectedEmployeesNote(employeesQuestion.getHasSelectedEmployeesNote());

        updateStateOnRequest(request, companyAccountsDataState);
    }
}
