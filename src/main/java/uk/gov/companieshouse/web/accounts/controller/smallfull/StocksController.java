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
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.stocks.StocksNote;
import uk.gov.companieshouse.web.accounts.service.smallfull.BalanceSheetService;
import uk.gov.companieshouse.web.accounts.service.smallfull.StocksService;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Controller
@NextController(DebtorsController.class)
@PreviousController(TangibleAssetsNoteController.class)
@RequestMapping("/company/{companyNumber}/transaction/{transactionId}/company-accounts/{companyAccountsId}/small-full/stocks")
public class StocksController extends BaseController implements ConditionalController {

    @Autowired
    private BalanceSheetService balanceSheetService;

    @Autowired
    private StocksService stocksService;

    @GetMapping
    public String getStocks(@PathVariable String companyNumber,
                            @PathVariable String transactionId,
                            @PathVariable String companyAccountsId,
                            Model model, HttpServletRequest request) {

        addBackPageAttributeToModel(model, companyNumber, transactionId, companyAccountsId);

        try {
            StocksNote stocksNote = stocksService.getStocks(transactionId, companyAccountsId, companyNumber);
            model.addAttribute("stocksNote", stocksNote);
        } catch (ServiceException se) {
            LOGGER.errorRequest(request, se.getMessage(), se);
            return ERROR_VIEW;
        }

        return getTemplateName();
    }

    @PostMapping
    public String postStocks(@PathVariable String companyNumber,
                              @PathVariable String transactionId,
                              @PathVariable String companyAccountsId,
                              @ModelAttribute("stocksNote") @Valid StocksNote stocksNote,
                              BindingResult bindingResult,
                              Model model,
                              HttpServletRequest request) {

        addBackPageAttributeToModel(model, companyNumber, transactionId, companyAccountsId);

        if (bindingResult.hasErrors()) {
            return getTemplateName();
        }

        try {
            List<ValidationError> validationErrors =
                stocksService.submitStocks(transactionId, companyAccountsId, stocksNote, companyNumber);

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

    private boolean shouldStocksNoteRender(BalanceSheet balanceSheet) {
        if (balanceSheet.getCurrentAssets() != null && balanceSheet.getCurrentAssets().getStocks() != null) {

            Long previousAmount = balanceSheet.getCurrentAssets().getStocks().getPreviousAmount();
            Long currentAmount = balanceSheet.getCurrentAssets().getStocks().getCurrentAmount();

            return valuePresent(previousAmount) || valuePresent(currentAmount);

        }
        return false;
    }

    private boolean valuePresent(Long value) {
        return value != null && value != 0;
    }

    @Override
    protected String getTemplateName() {
        return "smallfull/stocks";
    }

    @Override
    public boolean willRender(String companyNumber, String transactionId, String companyAccountsId) {

        try {
            BalanceSheet balanceSheet = balanceSheetService.getBalanceSheet(
                transactionId, companyAccountsId, companyNumber);
            return shouldStocksNoteRender(balanceSheet);
        } catch (ServiceException e) {
            return false;
        }
    }
}
