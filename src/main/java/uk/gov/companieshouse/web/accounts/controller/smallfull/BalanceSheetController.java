package uk.gov.companieshouse.web.accounts.controller.smallfull;

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
import uk.gov.companieshouse.api.error.ApiErrorResponseException;
import uk.gov.companieshouse.web.accounts.annotation.NextController;
import uk.gov.companieshouse.web.accounts.controller.BaseController;
import uk.gov.companieshouse.web.accounts.model.smallfull.BalanceSheet;
import uk.gov.companieshouse.web.accounts.service.smallfull.BalanceSheetService;
import uk.gov.companieshouse.web.accounts.util.Navigator;

@Controller
@NextController(ApprovalController.class)
@RequestMapping("/company/{companyNumber}/transaction/{transactionId}/company-accounts/{companyAccountsId}/small-full/balance-sheet")
public class BalanceSheetController extends BaseController {

    private static final String SMALL_FULL_BALANCE_SHEET = "smallfull/balanceSheet";

    @Autowired
    private BalanceSheetService balanceSheetService;

    @GetMapping
    public String getBalanceSheet(@PathVariable String transactionId,
                                  @PathVariable String companyAccountsId,
                                  Model model) {

        try {
            model.addAttribute("balanceSheet", balanceSheetService.getBalanceSheet(transactionId, companyAccountsId));
        } catch (ApiErrorResponseException e) {
            // TODO: handle ApiErrorResponseExceptions (SFA-594)
            LOGGER.error(e);
            model.addAttribute("balanceSheet", new BalanceSheet());
        }

        return SMALL_FULL_BALANCE_SHEET;
    }

    @PostMapping
    public String postBalanceSheet(@PathVariable String companyNumber,
                                   @PathVariable String transactionId,
                                   @PathVariable String companyAccountsId,
                                   @ModelAttribute("balanceSheet") @Valid BalanceSheet balanceSheet,
                                   BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return SMALL_FULL_BALANCE_SHEET;
        }

        try {
            balanceSheetService.postBalanceSheet(transactionId, companyAccountsId, balanceSheet);
        } catch (ApiErrorResponseException e) {
            LOGGER.error(e);
            return SMALL_FULL_BALANCE_SHEET;
        }

        return Navigator.getNextControllerRedirect(this.getClass(), companyNumber, transactionId, companyAccountsId);
    }
}