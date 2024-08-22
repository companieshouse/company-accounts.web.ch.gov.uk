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
import uk.gov.companieshouse.web.accounts.model.directorsreport.PrincipalActivitiesSelection;
import uk.gov.companieshouse.web.accounts.model.state.CompanyAccountsDataState;
import uk.gov.companieshouse.web.accounts.model.state.DirectorsReportStatements;
import uk.gov.companieshouse.web.accounts.service.smallfull.DirectorsReportService;
import uk.gov.companieshouse.web.accounts.service.smallfull.PrincipalActivitiesSelectionService;

@Controller
@NextController(PrincipalActivitiesController.class)
@PreviousController(AddOrRemoveDirectorsController.class)
@RequestMapping("/company/{companyNumber}/transaction/{transactionId}/company-accounts/{companyAccountsId}/small-full/directors-report/principal-activities-question")
public class PrincipalActivitiesSelectionController extends BaseController implements ConditionalController {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private PrincipalActivitiesSelectionService selectionService;

    @Autowired
    private DirectorsReportService directorsReportService;

    @Autowired
    private ApiClientService apiClientService;

    private static final String PRINCIPAL_ACTIVITIES_SELECTION = "principalActivitiesSelection";

    @Override
    protected String getTemplateName() {
        return "smallfull/principalActivitiesSelection";
    }

    @GetMapping
    public String getPrincipalActivitiesSelection(@PathVariable String companyNumber,
                                                  @PathVariable String transactionId,
                                                  @PathVariable String companyAccountsId,
                                                  Model model) {

        addBackPageAttributeToModel(model, companyNumber, transactionId, companyAccountsId);

        try {
            PrincipalActivitiesSelection selection =
                    selectionService.getPrincipalActivitiesSelection(transactionId, companyAccountsId);

            if (selection.getHasPrincipalActivities() == null) {
                setHasProvidedPrincipalActivities(request, selection);
            }

            model.addAttribute(PRINCIPAL_ACTIVITIES_SELECTION, selection);

        } catch (ServiceException e) {

            LOGGER.errorRequest(request, e.getMessage(), e);
            return ERROR_VIEW;
        }

        return getTemplateName();
    }

    @PostMapping
    public String submitPrincipalActivitiesSelection(@PathVariable String companyNumber,
                                                     @PathVariable String transactionId,
                                                     @PathVariable String companyAccountsId,
                                                     @ModelAttribute(PRINCIPAL_ACTIVITIES_SELECTION) @Valid PrincipalActivitiesSelection selection,
                                                     BindingResult bindingResult,
                                                     Model model) {

        addBackPageAttributeToModel(model, companyNumber, transactionId, companyAccountsId);

        if (bindingResult.hasErrors()) {
            return getTemplateName();
        }

        try {
            selectionService.submitPrincipalActivitiesSelection(transactionId, companyAccountsId, selection);

        } catch (ServiceException e) {

            LOGGER.errorRequest(request, e.getMessage(), e);
            return ERROR_VIEW;
        }

        cacheHasProvidedPrincipalActivities(request, selection);

        return navigatorService.getNextControllerRedirect(this.getClass(), companyNumber, transactionId, companyAccountsId);
    }

    private void setHasProvidedPrincipalActivities(HttpServletRequest request, PrincipalActivitiesSelection selection) {

        CompanyAccountsDataState companyAccountsDataState = getStateFromRequest(request);
        selection.setHasPrincipalActivities(
                Optional.of(companyAccountsDataState)
                        .map(CompanyAccountsDataState::getDirectorsReportStatements)
                        .map(DirectorsReportStatements::getHasProvidedPrincipalActivities)
                        .orElse(null));
    }

    private void cacheHasProvidedPrincipalActivities(HttpServletRequest request, PrincipalActivitiesSelection selection) {

        CompanyAccountsDataState companyAccountsDataState = getStateFromRequest(request);

        if (companyAccountsDataState.getDirectorsReportStatements() == null) {
            companyAccountsDataState.setDirectorsReportStatements(new DirectorsReportStatements());
        }
        companyAccountsDataState.getDirectorsReportStatements().setHasProvidedPrincipalActivities(
                selection.getHasPrincipalActivities());

        updateStateOnRequest(request, companyAccountsDataState);
    }

    @Override
    public boolean willRender(String companyNumber, String transactionId, String companyAccountsId)
            throws ServiceException {

        return directorsReportService.getDirectorsReport(apiClientService.getApiClient(), transactionId, companyAccountsId) != null;
    }
}
