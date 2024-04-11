package uk.gov.companieshouse.web.accounts.controller;

import org.apache.commons.lang.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import uk.gov.companieshouse.logging.Logger;
import uk.gov.companieshouse.logging.LoggerFactory;
import uk.gov.companieshouse.web.accounts.CompanyAccountsWebApplication;
import uk.gov.companieshouse.web.accounts.model.state.CompanyAccountsDataState;
import uk.gov.companieshouse.web.accounts.service.navigation.NavigatorService;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public abstract class BaseController {
    @Autowired
    protected NavigatorService navigatorService;

    protected static final Logger LOGGER = LoggerFactory
            .getLogger(CompanyAccountsWebApplication.APPLICATION_NAME_SPACE);

    protected static final String ERROR_VIEW = "error";

    protected static final String SUMMARY_FALSE_PARAMETER = "?summary=false";

    private static final String COMPANY_ACCOUNTS_DATA_STATE = "companyAccountsDataState";

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

    @ModelAttribute("templateName")
    protected abstract String getTemplateName();

    protected void addBackPageAttributeToModel(Model model, String... pathVars) {
        model.addAttribute("backButton", navigatorService.getPreviousControllerPath(this.getClass(), pathVars));
    }

    /**
     * Binds one or more API validation errors to model object fields.
     *
     * @param bindingResult the binding result object
     * @param errors        the list of validation errors generated from an
     *                      API request
     */
    protected void bindValidationErrors(BindingResult bindingResult, List<ValidationError> errors) {
        errors.sort(Comparator.comparing(ValidationError::getFieldPath).thenComparing(ValidationError::getMessageKey));

        errors.forEach(error -> bindingResult.rejectValue(error.getFieldPath(),
                error.getMessageKey(),
                getValidationArgs(error.getMessageArguments()),
                null)
        );
    }

    /**
     * Retrieve the client's {@link CompanyAccountsDataState} from the request session
     *
     * @param request The request
     * @return the client's {@link CompanyAccountsDataState}
     */
    protected CompanyAccountsDataState getStateFromRequest(HttpServletRequest request) {
        return (CompanyAccountsDataState) request.getSession().getAttribute(
                COMPANY_ACCOUNTS_DATA_STATE);
    }

    /**
     * Update the client's {@link CompanyAccountsDataState} on the request session
     *
     * @param request                  The request
     * @param companyAccountsDataState The client's {@link CompanyAccountsDataState}
     */
    protected void updateStateOnRequest(HttpServletRequest request, CompanyAccountsDataState companyAccountsDataState) {
        request.getSession().setAttribute(COMPANY_ACCOUNTS_DATA_STATE, companyAccountsDataState);
    }

    @ModelAttribute("metadata")
    public String addMetadata(HttpServletRequest request) {
        String metadata = "";
        if (isCic(request)) {
            metadata += "cic";
        }

        return metadata;
    }

    private boolean isCic(HttpServletRequest request) {
        CompanyAccountsDataState companyAccountsDataState = getStateFromRequest(request);

        String url = request.getRequestURL().toString();

        return url.contains("cic")
                || (companyAccountsDataState != null && BooleanUtils.isTrue(companyAccountsDataState.getIsCic()));
    }
}
