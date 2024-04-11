package uk.gov.companieshouse.web.accounts.controller.smallfull;

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
import uk.gov.companieshouse.web.accounts.model.smallfull.CurrentAssets;
import uk.gov.companieshouse.web.accounts.model.smallfull.Stocks;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.stocks.StocksNote;
import uk.gov.companieshouse.web.accounts.service.NoteService;
import uk.gov.companieshouse.web.accounts.service.smallfull.BalanceSheetService;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.List;

@Controller
@NextController(DebtorsController.class)
@PreviousController(FixedAssetsInvestmentsController.class)
@RequestMapping("/company/{companyNumber}/transaction/{transactionId}/company-accounts/{companyAccountsId}/small-full/stocks")
public class StocksController extends BaseController implements ConditionalController {
    @Autowired
    private BalanceSheetService balanceSheetService;

    @Autowired
    private NoteService<StocksNote> noteService;

    @Override
    protected String getTemplateName() {
        return "smallfull/stocks";
    }

    @GetMapping
    public String getStocks(@PathVariable String companyNumber,
                            @PathVariable String transactionId,
                            @PathVariable String companyAccountsId,
                            Model model, HttpServletRequest request) {
        addBackPageAttributeToModel(model, companyNumber, transactionId, companyAccountsId);

        try {
            model.addAttribute("stocksNote", noteService.get(transactionId, companyAccountsId, NoteType.SMALL_FULL_STOCKS));
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
                    noteService.submit(transactionId, companyAccountsId, stocksNote, NoteType.SMALL_FULL_STOCKS);

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

        return hasStocks(balanceSheet);
    }

    private boolean hasStocks(BalanceSheet balanceSheet) {
        Long currentStocks = Optional.of(balanceSheet)
                .map(BalanceSheet::getCurrentAssets)
                .map(CurrentAssets::getStocks)
                .map(Stocks::getCurrentAmount)
                .orElse(0L);

        Long previousStocks = Optional.of(balanceSheet)
                .map(BalanceSheet::getCurrentAssets)
                .map(CurrentAssets::getStocks)
                .map(Stocks::getPreviousAmount)
                .orElse(0L);

        return !(currentStocks.equals(0L) && previousStocks.equals(0L));
    }
}
