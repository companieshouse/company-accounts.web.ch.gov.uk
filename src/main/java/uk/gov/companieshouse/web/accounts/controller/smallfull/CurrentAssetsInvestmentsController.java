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
import uk.gov.companieshouse.web.accounts.model.smallfull.BalanceSheet;
import uk.gov.companieshouse.web.accounts.model.smallfull.CurrentAssets;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.currentassetsinvestments.CurrentAssetsInvestments;
import uk.gov.companieshouse.web.accounts.service.smallfull.BalanceSheetService;
import uk.gov.companieshouse.web.accounts.service.smallfull.CurrentAssetsInvestmentsService;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@NextController(ReviewController.class)
@PreviousController(CreditorsAfterOneYearController.class)
@RequestMapping("/company/{companyNumber}/transaction/{transactionId}/company-accounts/{companyAccountsId}/small-full/current-assets-investments")
public class CurrentAssetsInvestmentsController extends BaseController implements ConditionalController {

    @Autowired
    private CurrentAssetsInvestmentsService currentAssetsInvestmentsService;

    @Autowired
    private BalanceSheetService balanceSheetService;

    @Override
    protected String getTemplateName() {
        return "smallfull/currentAssetsInvestments";
    }

    @GetMapping
    public String getCurrentAssetsInvestments(@PathVariable String companyNumber,
            @PathVariable String transactionId, @PathVariable String companyAccountsId, Model model,
            HttpServletRequest request) {

        addBackPageAttributeToModel(model, companyNumber, transactionId, companyAccountsId);

        try {
            CurrentAssetsInvestments currentAssetsInvestments =
                currentAssetsInvestmentsService.getCurrentAssetsInvestments(transactionId,
                    companyAccountsId, companyNumber);

            model.addAttribute("currentAssetsInvestments", currentAssetsInvestments);

        } catch (ServiceException e) {
            LOGGER.errorRequest(request, e.getMessage(), e);
            return ERROR_VIEW;
        }

        return getTemplateName();
    }

    @PostMapping
    public String postCurrentAssetsInvestments(@PathVariable String companyNumber,
            @PathVariable String transactionId, @PathVariable String companyAccountsId,
            @ModelAttribute("currentAssetsInvestments") @Valid CurrentAssetsInvestments currentAssetsInvestments,
            BindingResult bindingResult, Model model, HttpServletRequest request) {

        addBackPageAttributeToModel(model, companyNumber, transactionId, companyAccountsId);

        if (bindingResult.hasErrors()) {
            return getTemplateName();
        }

        try {
            List<ValidationError> validationErrors =
                currentAssetsInvestmentsService.submitCurrentAssetsInvestments(transactionId,
                    companyAccountsId, currentAssetsInvestments, companyNumber);

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

        return hasInvestments(balanceSheet);
    }

    private boolean hasInvestments(BalanceSheet balanceSheet) {

        Long currentInvestments = Optional.of(balanceSheet)
            .map(BalanceSheet::getCurrentAssets)
            .map(CurrentAssets::getInvestments)
            .map(uk.gov.companieshouse.web.accounts.model.smallfull.CurrentAssetsInvestments::getCurrentAmount)
            .orElse(0L);

        Long previousInvestments = Optional.of(balanceSheet)
            .map(BalanceSheet::getCurrentAssets)
            .map(CurrentAssets::getInvestments)
            .map(uk.gov.companieshouse.web.accounts.model.smallfull.CurrentAssetsInvestments::getPreviousAmount)
            .orElse(0L);

        return !(currentInvestments.equals(0L) && previousInvestments.equals(0L));

    }
}
