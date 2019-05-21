package uk.gov.companieshouse.web.accounts.controller.cic;

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
import uk.gov.companieshouse.web.accounts.model.cic.statements.DirectorsRemunerationSelection;
import uk.gov.companieshouse.web.accounts.model.state.CompanyAccountsDataState;
import uk.gov.companieshouse.web.accounts.service.cic.statements.DirectorsRemunerationSelectionService;

@Controller
@NextController(StepsToCompleteController.class)
@PreviousController(ConsultationWithStakeholdersController.class)
@RequestMapping("/company/{companyNumber}/transaction/{transactionId}/company-accounts/{companyAccountsId}/cic/directors-remuneration-selection")
public class DirectorsRemunerationSelectionController extends BaseController {

    @Autowired
    private DirectorsRemunerationSelectionService selectionService;

    private static final String DIRECTORS_REMUNERATION_SELECTION_ATTR = "directorsRemunerationSelection";

    @Override
    protected String getTemplateName() {
        return "cic/directorsRemunerationSelection";
    }

    @GetMapping
    public String getDirectorsRemunerationSelection(
            @PathVariable String companyNumber,
            @PathVariable String transactionId,
            @PathVariable String companyAccountsId,
            Model model,
            HttpServletRequest request) {

        addBackPageAttributeToModel(model, companyNumber, transactionId, companyAccountsId);

        try {
            DirectorsRemunerationSelection selection =
                    selectionService.getDirectorsRemunerationSelection(transactionId, companyAccountsId);

            if (selection.getHasProvidedDirectorsRemuneration() == null) {
                setHasProvidedDirectorsRemuneration(request, selection);
            }

            model.addAttribute(DIRECTORS_REMUNERATION_SELECTION_ATTR, selection);

        } catch (ServiceException e) {

            LOGGER.errorRequest(request, e.getMessage(), e);
            return ERROR_VIEW;
        }

        return getTemplateName();
    }

    @PostMapping
    public String submitDirectorsRemunerationSelection(
            @PathVariable String companyNumber,
            @PathVariable String transactionId,
            @PathVariable String companyAccountsId,
            @ModelAttribute(DIRECTORS_REMUNERATION_SELECTION_ATTR) @Valid DirectorsRemunerationSelection selection,
            BindingResult bindingResult,
            Model model,
            HttpServletRequest request) {

        addBackPageAttributeToModel(model, companyNumber, transactionId, companyAccountsId);

        if (bindingResult.hasErrors()) {
            return getTemplateName();
        }

        try {
            selectionService.submitDirectorsRemunerationSelection(transactionId, companyAccountsId, selection);

        } catch (ServiceException e) {

            LOGGER.errorRequest(request, e.getMessage(), e);
            return ERROR_VIEW;
        }

        cacheHasProvidedDirectorsRemuneration(request, selection);

        return navigatorService.getNextControllerRedirect(this.getClass(), companyNumber, transactionId, companyAccountsId);
    }

    private void setHasProvidedDirectorsRemuneration(HttpServletRequest request, DirectorsRemunerationSelection selection) {

        CompanyAccountsDataState companyAccountsDataState = getStateFromRequest(request);
        selection.setHasProvidedDirectorsRemuneration(
                companyAccountsDataState.getCicStatements().getHasProvidedDirectorsRemuneration());
    }

    private void cacheHasProvidedDirectorsRemuneration(HttpServletRequest request, DirectorsRemunerationSelection selection) {

        CompanyAccountsDataState companyAccountsDataState = getStateFromRequest(request);

        companyAccountsDataState.getCicStatements().setHasProvidedDirectorsRemuneration(
                selection.getHasProvidedDirectorsRemuneration());

        updateStateOnRequest(request, companyAccountsDataState);
    }
}
