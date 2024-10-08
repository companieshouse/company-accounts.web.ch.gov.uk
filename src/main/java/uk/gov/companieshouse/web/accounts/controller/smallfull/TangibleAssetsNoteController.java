package uk.gov.companieshouse.web.accounts.controller.smallfull;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.List;
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
import uk.gov.companieshouse.web.accounts.controller.BaseController;
import uk.gov.companieshouse.web.accounts.controller.ConditionalController;
import uk.gov.companieshouse.web.accounts.enumeration.NoteType;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.smallfull.BalanceSheet;
import uk.gov.companieshouse.web.accounts.model.smallfull.FixedAssets;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.tangible.TangibleAssets;
import uk.gov.companieshouse.web.accounts.service.NoteService;
import uk.gov.companieshouse.web.accounts.service.smallfull.BalanceSheetService;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

@Controller
@NextController(FixedAssetsInvestmentsController.class)
@PreviousController(IntangibleAssetsNoteController.class)
@RequestMapping("/company/{companyNumber}/transaction/{transactionId}/company-accounts/{companyAccountsId}/small-full/note/tangible-assets")
public class TangibleAssetsNoteController extends BaseController implements ConditionalController {

    @Autowired
    private NoteService<TangibleAssets> noteService;

    @Autowired
    private BalanceSheetService balanceSheetService;

    @Override
    protected String getTemplateName() {
        return "smallfull/tangibleAssetsNote";
    }

    @GetMapping
    public String getTangibleAssetsNote(@PathVariable String companyNumber,
        @PathVariable String transactionId,
        @PathVariable String companyAccountsId,
        Model model,
        HttpServletRequest request) {

        addBackPageAttributeToModel(model, companyNumber, transactionId, companyAccountsId);

        try {
            TangibleAssets tangibleAssets = noteService
                .get(transactionId, companyAccountsId, NoteType.SMALL_FULL_TANGIBLE_ASSETS);

            model.addAttribute("tangibleAssets", tangibleAssets);

        } catch (ServiceException e) {
            LOGGER.errorRequest(request, e.getMessage(), e);
            return ERROR_VIEW;
        }

        return getTemplateName();
    }

    @PostMapping
    public String postTangibleAssets(@PathVariable String companyNumber,
        @PathVariable String transactionId,
        @PathVariable String companyAccountsId,
        @ModelAttribute("tangibleAssets") @Valid TangibleAssets tangibleAssets,
        BindingResult bindingResult,
        Model model,
        HttpServletRequest request) {

        addBackPageAttributeToModel(model, companyNumber, transactionId, companyAccountsId);

        if (bindingResult.hasErrors()) {
            return getTemplateName();
        }

        try {
            List<ValidationError> validationErrors = noteService
                .submit(transactionId, companyAccountsId, tangibleAssets,
                    NoteType.SMALL_FULL_TANGIBLE_ASSETS);
            if (!validationErrors.isEmpty()) {
                bindValidationErrors(bindingResult, validationErrors);
                return getTemplateName();
            }
        } catch (ServiceException e) {

            LOGGER.errorRequest(request, e.getMessage(), e);
            return ERROR_VIEW;
        }

        return navigatorService
            .getNextControllerRedirect(this.getClass(), companyNumber, transactionId,
                companyAccountsId);
    }

    @Override
    public boolean willRender(String companyNumber, String transactionId, String companyAccountsId)
            throws ServiceException {

        BalanceSheet balanceSheet =
                balanceSheetService.getBalanceSheet(
                        transactionId, companyAccountsId, companyNumber);

        return hasTangibleAssets(balanceSheet);
    }

    private boolean hasTangibleAssets(BalanceSheet balanceSheet) {

        Long currentTangible = Optional.of(balanceSheet)
                .map(BalanceSheet::getFixedAssets)
                .map(FixedAssets::getTangibleAssets)
                .map(uk.gov.companieshouse.web.accounts.model.smallfull.TangibleAssets::getCurrentAmount)
                .orElse(0L);

        Long previousTangible = Optional.of(balanceSheet)
                .map(BalanceSheet::getFixedAssets)
                .map(FixedAssets::getTangibleAssets)
                .map(uk.gov.companieshouse.web.accounts.model.smallfull.TangibleAssets::getPreviousAmount)
                .orElse(0L);

        return !(currentTangible.equals(0L) && previousTangible.equals(0L));
    }
}