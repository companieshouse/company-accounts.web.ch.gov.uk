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
import uk.gov.companieshouse.web.accounts.model.directorsreport.PoliticalAndCharitableDonationsSelection;
import uk.gov.companieshouse.web.accounts.model.state.CompanyAccountsDataState;
import uk.gov.companieshouse.web.accounts.model.state.DirectorsReportStatements;
import uk.gov.companieshouse.web.accounts.service.smallfull.DirectorsReportService;
import uk.gov.companieshouse.web.accounts.service.smallfull.PoliticalAndCharitableDonationsSelectionService;

@Controller
@NextController(PoliticalAndCharitableDonationsController.class)
@PreviousController(PrincipalActivitiesController.class)
@RequestMapping("/company/{companyNumber}/transaction/{transactionId}/company-accounts/{companyAccountsId}/small-full/directors-report/political-and-charitable-donations-question")
public class PoliticalAndCharitableDonationsSelectionController extends BaseController implements ConditionalController {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private DirectorsReportService directorsReportService;

    @Autowired
    private ApiClientService apiClientService;

    @Autowired
    private PoliticalAndCharitableDonationsSelectionService selectionService;

    private static final String POLITICAL_AND_CHARITABLE_DONATIONS_SELECTION = "politicalAndCharitableDonationsSelection";

    @Override
    protected String getTemplateName() { return "smallfull/politicalAndCharitableDonationsSelection"; }

    @GetMapping
    public String getPoliticalAndCharitableDonationsSelection(@PathVariable String companyNumber,
                                                  @PathVariable String transactionId,
                                                  @PathVariable String companyAccountsId,
                                                  Model model) {

        addBackPageAttributeToModel(model, companyNumber, transactionId, companyAccountsId);

        try {
            PoliticalAndCharitableDonationsSelection selection =
                    selectionService.getPoliticalAndCharitableDonationsSelection(transactionId, companyAccountsId);

            if (selection.getHasPoliticalAndCharitableDonations() == null) {
                setHasProvidedPoliticalAndCharitableDonations(request, selection);

            }

            model.addAttribute(POLITICAL_AND_CHARITABLE_DONATIONS_SELECTION, selection);

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
                                                     @ModelAttribute(POLITICAL_AND_CHARITABLE_DONATIONS_SELECTION)
                                                         @Valid PoliticalAndCharitableDonationsSelection selection,
                                                     BindingResult bindingResult,
                                                     Model model) {

        addBackPageAttributeToModel(model, companyNumber, transactionId, companyAccountsId);

        if (bindingResult.hasErrors()) {
            return getTemplateName();
        }

        try {
            selectionService.submitPoliticalAndCharitableDonationsSelection(transactionId, companyAccountsId, selection);

        } catch (ServiceException e) {

            LOGGER.errorRequest(request, e.getMessage(), e);
            return ERROR_VIEW;
        }

        cacheHasProvidedPoliticalAndCharitableDonations(request, selection);

        return navigatorService.getNextControllerRedirect(this.getClass(), companyNumber, transactionId, companyAccountsId);
    }

    private void setHasProvidedPoliticalAndCharitableDonations(HttpServletRequest request, PoliticalAndCharitableDonationsSelection selection) {

        CompanyAccountsDataState companyAccountsDataState = getStateFromRequest(request);
        selection.setHasPoliticalAndCharitableDonations(
                Optional.of(companyAccountsDataState)
                        .map(CompanyAccountsDataState::getDirectorsReportStatements)
                        .map(DirectorsReportStatements::getHasProvidedPoliticalAndCharitableDonations)
                        .orElse(null));
    }

    private void cacheHasProvidedPoliticalAndCharitableDonations(HttpServletRequest request, PoliticalAndCharitableDonationsSelection selection) {

        CompanyAccountsDataState companyAccountsDataState = getStateFromRequest(request);

        if (companyAccountsDataState.getDirectorsReportStatements() == null) {
            companyAccountsDataState.setDirectorsReportStatements(new DirectorsReportStatements());
        }

        companyAccountsDataState.getDirectorsReportStatements().setHasProvidedPoliticalAndCharitableDonations(
                selection.getHasPoliticalAndCharitableDonations());

        updateStateOnRequest(request, companyAccountsDataState);
    }

    @Override
    public boolean willRender(String companyNumber, String transactionId, String companyAccountsId)
            throws ServiceException {

        return directorsReportService.getDirectorsReport(apiClientService.getApiClient(), transactionId, companyAccountsId) != null;
    }

}
