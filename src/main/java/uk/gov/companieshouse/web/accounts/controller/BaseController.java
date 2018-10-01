package uk.gov.companieshouse.web.accounts.controller;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import uk.gov.companieshouse.logging.Logger;
import uk.gov.companieshouse.logging.LoggerFactory;
import uk.gov.companieshouse.web.accounts.CompanyAccountsWebApplication;
import uk.gov.companieshouse.web.accounts.util.Navigator;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public abstract class BaseController {

    protected static final Logger LOGGER = LoggerFactory
            .getLogger(CompanyAccountsWebApplication.APPLICATION_NAME_SPACE);

    protected static final String ERROR_VIEW = "error";

    protected BaseController() {
    }

    /**
     * Returns an array of error message arguments that apply to a validation
     * error, and guarantees that any 'lower' and 'upper' error message
     * arguments are ordered at indexes 0 and 1 respectively, for use in
     * validation messages (note the order of keys in a JSON object is not
     * guaranteed).
     *
     * @param errorArgs the map of error arguments
     * @return an array of message argument values
     */
    private static Object[] getValidationArgs(Map<String, String> errorArgs) {

        if (errorArgs == null) {
            return new Object[]{};
        }

        if (errorArgs.get("lower") != null && errorArgs.get("upper") != null) {
            ArrayList<String> args = new ArrayList<>();
            args.add(errorArgs.get("lower"));
            args.add(errorArgs.get("upper"));
            return args.toArray();
        } else {
            return errorArgs.values().toArray();
        }
    }

    protected void addBackPageAttributeToModel(Model model, String... pathVars) {

        model.addAttribute("backButton", Navigator.getPreviousControllerPath(this.getClass(), pathVars));
    }

    /**
     * Binds one or more API validation errors to model object fields.
     *
     * @param bindingResult the binding result object
     * @param errors        the list of validation errors generated from an
     *                      API request
     */
    protected void bindValidationErrors(BindingResult bindingResult, List<ValidationError> errors) {
        Collections.sort(errors,
                Comparator.comparing(ValidationError::getFieldPath)
                        .thenComparing(ValidationError::getMessageKey));

        errors.forEach(error ->
                bindingResult.rejectValue(error.getFieldPath(),
                        error.getMessageKey(),
                        getValidationArgs(error.getMessageArguments()),
                        null)
        );
    }
}
