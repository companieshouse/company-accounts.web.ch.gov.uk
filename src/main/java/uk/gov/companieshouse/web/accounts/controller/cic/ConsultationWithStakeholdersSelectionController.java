package uk.gov.companieshouse.web.accounts.controller.cic;

import java.util.Optional;
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
import uk.gov.companieshouse.web.accounts.controller.smallfull.StepsToCompleteController;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.cic.statements.ConsultationWithStakeholdersSelection;
import uk.gov.companieshouse.web.accounts.model.state.CicStatements;
import uk.gov.companieshouse.web.accounts.model.state.CompanyAccountsDataState;
import uk.gov.companieshouse.web.accounts.service.cic.statements.ConsultationWithStakeholdersSelectionService;

@Controller
@NextController(StepsToCompleteController.class)
@PreviousController(CompanyActivitiesAndImpactController.class)
@RequestMapping("/company/{companyNumber}/transaction/{transactionId}/company-accounts/{companyAccountsId}/cic/consultation-with-stakeholders-selection")
public class ConsultationWithStakeholdersSelectionController extends BaseController {

    @Autowired
    private ConsultationWithStakeholdersSelectionService selectionService;

    private static final String CONSULTATION_WITH_STAKEHOLDERS_SELECTION_ATTR = "consultationWithStakeholdersSelection";

    @Override
    protected String getTemplateName() {
        return "cic/consultationWithStakeholdersSelection";
    }

    @GetMapping
    public String getConsultationWithStakeholdersSelection(
            @PathVariable String companyNumber,
            @PathVariable String transactionId,
            @PathVariable String companyAccountsId,
            Model model,
            HttpServletRequest request) {

        addBackPageAttributeToModel(model, companyNumber, transactionId, companyAccountsId);

        try {
            ConsultationWithStakeholdersSelection selection =
                    selectionService.getConsultationWithStakeholdersSelection(transactionId, companyAccountsId);

            if (selection.getHasProvidedConsultationWithStakeholders() == null) {
                setHasProvidedConsultationWithStakeholders(request, selection);
            }

            model.addAttribute(CONSULTATION_WITH_STAKEHOLDERS_SELECTION_ATTR, selection);

        } catch (ServiceException e) {

            LOGGER.errorRequest(request, e.getMessage(), e);
            return ERROR_VIEW;
        }

        return getTemplateName();
    }

    @PostMapping
    public String submitConsultationWithStakeholdersSelection(
            @PathVariable String companyNumber,
            @PathVariable String transactionId,
            @PathVariable String companyAccountsId,
            @ModelAttribute(CONSULTATION_WITH_STAKEHOLDERS_SELECTION_ATTR) @Valid ConsultationWithStakeholdersSelection selection,
            BindingResult bindingResult,
            Model model,
            HttpServletRequest request) {

        addBackPageAttributeToModel(model, companyNumber, transactionId, companyAccountsId);

        if (bindingResult.hasErrors()) {
            return getTemplateName();
        }

        try {
            selectionService.submitConsultationWithStakeholdersSelection(transactionId, companyAccountsId, selection);

        } catch (ServiceException e) {

            LOGGER.errorRequest(request, e.getMessage(), e);
            return ERROR_VIEW;
        }

        cacheHasProvidedConsultationWithStakeholders(request, selection);

        return navigatorService.getNextControllerRedirect(this.getClass(), companyNumber, transactionId, companyAccountsId);
    }

    private void setHasProvidedConsultationWithStakeholders(HttpServletRequest request, ConsultationWithStakeholdersSelection selection) {

        CompanyAccountsDataState companyAccountsDataState = getStateFromRequest(request);
        selection.setHasProvidedConsultationWithStakeholders(
                Optional.of(companyAccountsDataState)
                        .map(CompanyAccountsDataState::getCicStatements)
                        .map(CicStatements::getHasProvidedConsultationWithStakeholders)
                        .orElse(null));
    }

    private void cacheHasProvidedConsultationWithStakeholders(HttpServletRequest request, ConsultationWithStakeholdersSelection selection) {

        CompanyAccountsDataState companyAccountsDataState = getStateFromRequest(request);

        if (companyAccountsDataState.getCicStatements() == null) {
            companyAccountsDataState.setCicStatements(new CicStatements());
        }

        companyAccountsDataState.getCicStatements().setHasProvidedConsultationWithStakeholders(
                selection.getHasProvidedConsultationWithStakeholders());

        updateStateOnRequest(request, companyAccountsDataState);
    }
}
