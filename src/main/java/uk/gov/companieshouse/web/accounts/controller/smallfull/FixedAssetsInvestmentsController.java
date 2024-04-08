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
import uk.gov.companieshouse.web.accounts.enumeration.NoteType;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.smallfull.BalanceSheet;
import uk.gov.companieshouse.web.accounts.model.smallfull.FixedAssets;
import uk.gov.companieshouse.web.accounts.model.smallfull.FixedInvestments;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.fixedassetsinvestments.FixedAssetsInvestments;
import uk.gov.companieshouse.web.accounts.service.NoteService;
import uk.gov.companieshouse.web.accounts.service.smallfull.BalanceSheetService;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@NextController(StocksController.class)
@PreviousController(TangibleAssetsNoteController.class)
@RequestMapping("/company/{companyNumber}/transaction/{transactionId}/company-accounts/{companyAccountsId}/small-full/fixed-assets-investments")
public class FixedAssetsInvestmentsController extends BaseController implements ConditionalController {
    
    @Autowired
    private NoteService<FixedAssetsInvestments> fixedAssetsInvestmentsService;

    @Autowired
    private BalanceSheetService balanceSheetService;

    @Override
    protected String getTemplateName() {
        return "smallfull/fixedAssetsInvestments";
    }

    @GetMapping
    public String getFixedAssetsInvestments(@PathVariable String companyNumber,
                            @PathVariable String transactionId,
                            @PathVariable String companyAccountsId,
                            Model model, HttpServletRequest request) {

        addBackPageAttributeToModel(model, companyNumber, transactionId, companyAccountsId);

        try {
            FixedAssetsInvestments fixedAssetsInvestments = fixedAssetsInvestmentsService.get(transactionId, companyAccountsId, NoteType.SMALL_FULL_FIXED_ASSETS_INVESTMENT);
            model.addAttribute("fixedAssetsInvestments", fixedAssetsInvestments);
        } catch (ServiceException se) {
            LOGGER.errorRequest(request, se.getMessage(), se);
            return ERROR_VIEW;
        }

        return getTemplateName();
    }

    @PostMapping
    public String postFixedAssetsInvestments(@PathVariable String companyNumber,
                              @PathVariable String transactionId,
                              @PathVariable String companyAccountsId,
                              @ModelAttribute("fixedAssetsInvestments") @Valid FixedAssetsInvestments fixedAssetsInvestments,
                              BindingResult bindingResult,
                              Model model,
                              HttpServletRequest request) {

        
        addBackPageAttributeToModel(model, companyNumber, transactionId, companyAccountsId);

        if (bindingResult.hasErrors()) {
            return getTemplateName();
        }
        
        try {
            List<ValidationError> validationErrors =
                fixedAssetsInvestmentsService.submit(transactionId, companyAccountsId, fixedAssetsInvestments, NoteType.SMALL_FULL_FIXED_ASSETS_INVESTMENT);

            if (! validationErrors.isEmpty()) {
                bindValidationErrors(bindingResult, validationErrors);
                return getTemplateName();
            }
        } catch (ServiceException e) {
            LOGGER.errorRequest(request, e.getMessage(), e);
            return ERROR_VIEW;
        }

        return navigatorService.getNextControllerRedirect(
            this.getClass(), companyNumber, transactionId, companyAccountsId);
    }

    @Override
    public boolean willRender(String companyNumber, String transactionId, String companyAccountsId)
            throws ServiceException {

        BalanceSheet balanceSheet =
            balanceSheetService.getBalanceSheet(
                transactionId, companyAccountsId, companyNumber);

        return hasFixedInvestments(balanceSheet);
    }

    private boolean hasFixedInvestments(BalanceSheet balanceSheet) {

        Long currentInvestments = Optional.of(balanceSheet)
            .map(BalanceSheet::getFixedAssets)
            .map(FixedAssets::getInvestments)
            .map(FixedInvestments::getCurrentAmount)
            .orElse(0L);

        Long previousInvestments = Optional.of(balanceSheet)
            .map(BalanceSheet::getFixedAssets)
            .map(FixedAssets::getInvestments)
            .map(FixedInvestments::getPreviousAmount)
            .orElse(0L);

        return !(currentInvestments.equals(0L) && previousInvestments.equals(0L));
    }
}
