package uk.gov.companieshouse.web.accounts.controller.cic;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
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
import uk.gov.companieshouse.web.accounts.model.cic.statements.TransferOfAssetsSelection;
import uk.gov.companieshouse.web.accounts.model.state.CompanyAccountsDataState;
import uk.gov.companieshouse.web.accounts.service.cic.statements.TransferOfAssetsSelectionService;

@Controller
@NextController(TransferOfAssetsController.class)
@PreviousController(DirectorsRemunerationController.class)
@RequestMapping("/company/{companyNumber}/transaction/{transactionId}/company-accounts/{companyAccountsId}/cic/transfer-of-assets-selection")
public class TransferOfAssetsSelectionController extends BaseController {
    @Autowired
    private TransferOfAssetsSelectionService selectionService;

    private static final String TRANSFER_OF_ASSETS_SELECTION_ATTR = "transferOfAssetsSelection";

    @Override
    protected String getTemplateName() {
        return "cic/transferOfAssetsSelection";
    }

    @GetMapping
    public String getTransferOfAssetsSelection(
            @PathVariable String companyNumber,
            @PathVariable String transactionId,
            @PathVariable String companyAccountsId,
            Model model,
            HttpServletRequest request) {
        addBackPageAttributeToModel(model, companyNumber, transactionId, companyAccountsId);

        try {
            TransferOfAssetsSelection selection =
                    selectionService.getTransferOfAssetsSelection(transactionId, companyAccountsId);

            if (selection.getHasProvidedTransferOfAssets() == null) {
                setHasProvidedTransferOfAssets(request, selection);
            }

            model.addAttribute(TRANSFER_OF_ASSETS_SELECTION_ATTR, selection);

        } catch (ServiceException e) {
            LOGGER.errorRequest(request, e.getMessage(), e);
            return ERROR_VIEW;
        }

        return getTemplateName();
    }

    @PostMapping
    public String submitTransferOfAssetsSelection(
            @PathVariable String companyNumber,
            @PathVariable String transactionId,
            @PathVariable String companyAccountsId,
            @ModelAttribute(TRANSFER_OF_ASSETS_SELECTION_ATTR) @Valid TransferOfAssetsSelection selection,
            BindingResult bindingResult,
            Model model,
            HttpServletRequest request) {
        addBackPageAttributeToModel(model, companyNumber, transactionId, companyAccountsId);

        if (bindingResult.hasErrors()) {
            return getTemplateName();
        }

        try {
            selectionService.submitTransferOfAssetsSelection(transactionId, companyAccountsId, selection);

        } catch (ServiceException e) {
            LOGGER.errorRequest(request, e.getMessage(), e);
            return ERROR_VIEW;
        }

        cacheHasProvidedTransferOfAssets(request, selection);

        return navigatorService.getNextControllerRedirect(this.getClass(), companyNumber, transactionId, companyAccountsId);
    }

    private void setHasProvidedTransferOfAssets(HttpServletRequest request, TransferOfAssetsSelection selection) {
        CompanyAccountsDataState companyAccountsDataState = getStateFromRequest(request);
        selection.setHasProvidedTransferOfAssets(
                companyAccountsDataState.getCicStatements().getHasProvidedTransferOfAssets());
    }

    private void cacheHasProvidedTransferOfAssets(HttpServletRequest request, TransferOfAssetsSelection selection) {
        CompanyAccountsDataState companyAccountsDataState = getStateFromRequest(request);

        companyAccountsDataState.getCicStatements().setHasProvidedTransferOfAssets(
                selection.getHasProvidedTransferOfAssets());

        updateStateOnRequest(request, companyAccountsDataState);
    }
}
