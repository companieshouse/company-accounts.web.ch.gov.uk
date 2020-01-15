package uk.gov.companieshouse.web.accounts.controller.smallfull;

import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
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
import uk.gov.companieshouse.web.accounts.model.directorsreport.PrincipalActivities;
import uk.gov.companieshouse.web.accounts.model.state.CompanyAccountsDataState;
import uk.gov.companieshouse.web.accounts.model.state.DirectorsReportStatements;
import uk.gov.companieshouse.web.accounts.service.smallfull.PrincipalActivitiesService;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

@Controller
@NextController(DirectorsReportApprovalController.class)
@PreviousController(PrincipalActivitiesSelectionController.class)
@RequestMapping("/company/{companyNumber}/transaction/{transactionId}/company-accounts/{companyAccountsId}/small-full/directors-report/principal-activities")
public class PrincipalActivitiesController extends BaseController implements ConditionalController {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private PrincipalActivitiesService principalActivitiesService;

    private static final String PRINCIPAL_ACTIVITIES = "principalActivities";

    @Override
    protected String getTemplateName() {
        return "smallfull/principalActivities";
    }

    @GetMapping
    public String getPrincipalActivities(@PathVariable String companyNumber,
                                         @PathVariable String transactionId,
                                         @PathVariable String companyAccountsId,
                                         Model model,
                                         HttpServletRequest request) {

        addBackPageAttributeToModel(model, companyNumber, transactionId, companyAccountsId);

        try {
            PrincipalActivities principalActivities =
                    principalActivitiesService.getPrincipalActivities(transactionId, companyAccountsId);

            model.addAttribute(PRINCIPAL_ACTIVITIES, principalActivities);

        } catch (ServiceException e) {

            LOGGER.errorRequest(request, e.getMessage(), e);
            return ERROR_VIEW;
        }

        return getTemplateName();
    }

    @PostMapping
    public String submitPrincipalActivities(@PathVariable String companyNumber,
                                            @PathVariable String transactionId,
                                            @PathVariable String companyAccountsId,
                                            @ModelAttribute(PRINCIPAL_ACTIVITIES) PrincipalActivities principalActivities,
                                            BindingResult bindingResult,
                                            Model model,
                                            HttpServletRequest request) {

        addBackPageAttributeToModel(model, companyNumber, transactionId, companyAccountsId);

        try {
            List<ValidationError> validationErrors =
                    principalActivitiesService.submitPrincipalActivities(transactionId, companyAccountsId, principalActivities);

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
                    .map(DirectorsReportStatements::getHasProvidedPrincipalActivities)
                    .orElse(false);
    }
}
