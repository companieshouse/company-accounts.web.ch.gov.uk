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
import uk.gov.companieshouse.web.accounts.api.ApiClientService;
import uk.gov.companieshouse.web.accounts.controller.BaseController;
import uk.gov.companieshouse.web.accounts.controller.ConditionalController;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.loanstodirectors.AdditionalInformation;
import uk.gov.companieshouse.web.accounts.model.loanstodirectors.AdditionalInformationSelection;
import uk.gov.companieshouse.web.accounts.model.state.CompanyAccountsDataState;
import uk.gov.companieshouse.web.accounts.model.state.DirectorsReportStatements;
import uk.gov.companieshouse.web.accounts.service.smallfull.AdditionalInformationSelectionService;
import uk.gov.companieshouse.web.accounts.service.smallfull.LoansToDirectorsService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Optional;

@Controller
@NextController(OffBalanceSheetArrangementsQuestionController.class)
@PreviousController(AddOrRemoveLoansController.class)
@RequestMapping("/company/{companyNumber}/transaction/{transactionId}/company-accounts/{companyAccountsId}/small-full/note/add-or-remove-loans/additional-information-question")
public class LoansToDirectorsAdditionalInformationSelectionController extends BaseController implements ConditionalController {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private AdditionalInformationSelectionService<AdditionalInformationSelection> selectionService;

    @Autowired
    private LoansToDirectorsService loansToDirectorsService;

    @Autowired
    private ApiClientService apiClientService;

    private static final String ADDITIONAL_INFORMATION_SELECTION = "additionalInformationSelection";

    @Override
    protected String getTemplateName() { return "smallfull/loansToDirectorsAdditionalInformationSelection"; }

    @GetMapping
    public String getAdditionalInformationSelection(@PathVariable String companyNumber,
                                                    @PathVariable String transactionId,
                                                    @PathVariable String companyAccountsId,
                                                    Model model) {

        addBackPageAttributeToModel(model, companyNumber, transactionId, companyAccountsId);

        try {
            AdditionalInformationSelection selection =
                    selectionService.getAdditionalInformationSelection(transactionId, companyAccountsId);

            if (selection.getHasAdditionalInformation() == null) {
                setHasProvidedAdditionalInformation(request, selection);
            }

            model.addAttribute(ADDITIONAL_INFORMATION_SELECTION, selection);
        } catch (ServiceException e) {

            LOGGER.errorRequest(request, e.getMessage(), e);
            return ERROR_VIEW;
        }

        return getTemplateName();
    }

    @PostMapping
    public String submitAdditionalInformationSelection(@PathVariable String companyNumber,
                                                       @PathVariable String transactionId,
                                                       @PathVariable String companyAccountsId,
                                                       @ModelAttribute(ADDITIONAL_INFORMATION_SELECTION) @Valid AdditionalInformationSelection selection,
                                                       BindingResult bindingResult,
                                                       Model model) {

        addBackPageAttributeToModel(model, companyNumber, transactionId, companyAccountsId);

        if (bindingResult.hasErrors()) {
            return getTemplateName();
        }

        try {
            selectionService.submitAdditionalInformationSelection(transactionId, companyAccountsId, selection);

        } catch (ServiceException e) {

            LOGGER.errorRequest(request, e.getMessage(), e);
            return ERROR_VIEW;
        }

        cacheHasProvidedAdditionalInformation(request, selection);

        return navigatorService.getNextControllerRedirect(this.getClass(), companyNumber, transactionId, companyAccountsId);

    }

    private void setHasProvidedAdditionalInformation(HttpServletRequest request, AdditionalInformationSelection selection) {

        CompanyAccountsDataState companyAccountsDataState = getStateFromRequest(request);
        selection.setHasAdditionalInformation(
                Optional.of(companyAccountsDataState)
                        .map(CompanyAccountsDataState::getLoansToDirectorsAdditionalInformation)
                        .map(AdditionalInformation::getHasProvidedAdditionalInformation)
                        .orElse(null));
    }

    private void cacheHasProvidedAdditionalInformation(HttpServletRequest request, AdditionalInformationSelection selection) {

        CompanyAccountsDataState companyAccountsDataState = getStateFromRequest(request);

        if (companyAccountsDataState.getLoansToDirectorsAdditionalInformation() == null) {
            companyAccountsDataState.setLoansToDirectorsAdditionalInformation(new AdditionalInformation());

        }

        companyAccountsDataState.getLoansToDirectorsAdditionalInformation().setHasProvidedAdditionalInformation(
                selection.getHasAdditionalInformation());

        updateStateOnRequest(request, companyAccountsDataState);
    }

    @Override
    public boolean willRender(String companyNumber, String transactionId, String companyAccountsId) throws ServiceException {
        return loansToDirectorsService.getLoansToDirectors(apiClientService.getApiClient(), transactionId, companyAccountsId) != null;
    }
}
