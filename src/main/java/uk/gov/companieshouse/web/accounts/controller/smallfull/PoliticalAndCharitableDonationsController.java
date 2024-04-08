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
import uk.gov.companieshouse.web.accounts.controller.ConditionalController;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.directorsreport.PoliticalAndCharitableDonations;
import uk.gov.companieshouse.web.accounts.model.state.CompanyAccountsDataState;
import uk.gov.companieshouse.web.accounts.model.state.DirectorsReportStatements;
import uk.gov.companieshouse.web.accounts.service.smallfull.PoliticalAndCharitableDonationsService;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@NextController(CompanyPolicyOnDisabledEmployeesSelectionController.class)
@PreviousController(PoliticalAndCharitableDonationsSelectionController.class)
@RequestMapping("/company/{companyNumber}/transaction/{transactionId}/company-accounts/{companyAccountsId}/small-full/directors-report/political-and-charitable-donations")
public class PoliticalAndCharitableDonationsController extends BaseController implements ConditionalController {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private PoliticalAndCharitableDonationsService politicalAndCharitableDonationsService;

    private static final String POLITICAL_AND_CHARITABLE_DONATIONS = "politicalAndCharitableDonations";

    @Override
    protected String getTemplateName() {
        return "smallfull/politicalAndCharitableDonations";
    }

    @GetMapping
    public String getPoliticalAndCharitableDonations(@PathVariable String companyNumber,
                                                     @PathVariable String transactionId,
                                                     @PathVariable String companyAccountsId,
                                                     Model model) {

        addBackPageAttributeToModel(model, companyNumber, transactionId, companyAccountsId);

        try {
            PoliticalAndCharitableDonations politicalAndCharitableDonations =
                    politicalAndCharitableDonationsService.getPoliticalAndCharitableDonations(transactionId, companyAccountsId);

            model.addAttribute(POLITICAL_AND_CHARITABLE_DONATIONS, politicalAndCharitableDonations);

        } catch (ServiceException e) {

            LOGGER.errorRequest(request, e.getMessage(), e);
            return ERROR_VIEW;
        }

        return getTemplateName();

    }

    @PostMapping
    public String submitPoliticalAndCharitableDonations(@PathVariable String companyNumber,
                                                        @PathVariable String transactionId,
                                                        @PathVariable String companyAccountsId,
                                                        @ModelAttribute(POLITICAL_AND_CHARITABLE_DONATIONS)
                                                        @Valid PoliticalAndCharitableDonations politicalAndCharitableDonations,
                                                        BindingResult bindingResult,
                                                        Model model) {

        addBackPageAttributeToModel(model, companyNumber, transactionId, companyAccountsId);

        if (bindingResult.hasErrors()) {
            return getTemplateName();
        }

        try {
            List<ValidationError> validationErrors =
                    politicalAndCharitableDonationsService.submitPoliticalAndCharitableDonations(transactionId, companyAccountsId,
                            politicalAndCharitableDonations);

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
                .map(DirectorsReportStatements::getHasProvidedPoliticalAndCharitableDonations)
                .orElse(false);
    }




}
