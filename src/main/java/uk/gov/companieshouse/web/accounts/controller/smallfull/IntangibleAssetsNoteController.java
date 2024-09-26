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
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.intangible.IntangibleAssets;
import uk.gov.companieshouse.web.accounts.service.NoteService;
import uk.gov.companieshouse.web.accounts.service.smallfull.BalanceSheetService;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

@Controller
@NextController(TangibleAssetsNoteController.class)
@PreviousController(EmployeesController.class)
@RequestMapping("/company/{companyNumber}/transaction/{transactionId}/company-accounts/{companyAccountsId}/small-full/note/intangible-assets")
public class IntangibleAssetsNoteController extends BaseController implements ConditionalController {

    private static final String INTANGIBLE_ASSETS_MODEL_ATTR = "intangibleAssets";

    @Autowired
    private NoteService<IntangibleAssets> intangibleAssetsNoteService;

    @Override
    protected String getTemplateName() { return "smallfull/intangibleAssetsNote"; }

    @Autowired
    private BalanceSheetService balanceSheetService;

    @GetMapping
    public String getIntangibleAssetsNote(@PathVariable String companyNumber,
        @PathVariable String transactionId,
        @PathVariable String companyAccountsId,
        Model model,
        HttpServletRequest request) {

        addBackPageAttributeToModel(model, companyNumber, transactionId, companyAccountsId);

        try {
            IntangibleAssets intangibleAssets = intangibleAssetsNoteService
                    .get(transactionId, companyAccountsId, NoteType.SMALL_FULL_INTANGIBLE_ASSETS);

            model.addAttribute(INTANGIBLE_ASSETS_MODEL_ATTR, intangibleAssets);

        } catch (ServiceException e) {
            LOGGER.errorRequest(request, e.getMessage(), e);
            return ERROR_VIEW;
        }

        return getTemplateName();
    }

    @PostMapping
    public String postIntangibleAssets(@PathVariable String companyNumber,
                                       @PathVariable String transactionId,
                                       @PathVariable String companyAccountsId,
                                       @ModelAttribute(INTANGIBLE_ASSETS_MODEL_ATTR) @Valid IntangibleAssets intangibleAssets,
                                       BindingResult bindingResult,
                                       Model model,
                                       HttpServletRequest request) {
        addBackPageAttributeToModel(model, companyNumber, transactionId, companyAccountsId);

        if (bindingResult.hasErrors()) {
            return getTemplateName();
        }

        try {
            List<ValidationError> validationErrors = intangibleAssetsNoteService
                    .submit(transactionId, companyAccountsId, intangibleAssets,
                            NoteType.SMALL_FULL_INTANGIBLE_ASSETS);
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
    public boolean willRender(String companyNumber, String transactionId, String companyAccountsId) throws ServiceException {
        BalanceSheet balanceSheet =
                balanceSheetService.getBalanceSheet(
                        transactionId, companyAccountsId, companyNumber);
        return hasIntangibleAssets(balanceSheet);
    }

    private boolean hasIntangibleAssets(BalanceSheet balanceSheet) {

        Long currentIntangible = Optional.of(balanceSheet)
                .map(BalanceSheet::getFixedAssets)
                .map(FixedAssets::getIntangibleAssets)
                .map(uk.gov.companieshouse.web.accounts.model.smallfull.IntangibleAssets::getCurrentAmount)
                .orElse(0L);

        Long previousIntangible = Optional.of(balanceSheet)
                .map(BalanceSheet::getFixedAssets)
                .map(FixedAssets::getIntangibleAssets)
                .map(uk.gov.companieshouse.web.accounts.model.smallfull.IntangibleAssets::getPreviousAmount)
                .orElse(0L);

        return !(currentIntangible.equals(0L) && previousIntangible.equals(0L));
    }

}
