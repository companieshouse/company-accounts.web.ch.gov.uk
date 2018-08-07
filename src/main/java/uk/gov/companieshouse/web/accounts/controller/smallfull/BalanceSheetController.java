package uk.gov.companieshouse.web.accounts.controller.smallfull;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import uk.gov.companieshouse.web.accounts.model.smallfull.BalanceSheet;
import uk.gov.companieshouse.web.accounts.service.smallfull.BalanceSheetService;

@Controller
public class BalanceSheetController {

    private static final String BALANCE_SHEET_PATH = "/company/{companyNumber}/transaction/{transactionId}/company-accounts/{companyAccountsId}/small-full/balance-sheet";

    private static final String SMALL_FULL_BALANCE_SHEET = "smallfull/balanceSheet";

    @Autowired
    private BalanceSheetService balanceSheetService;

    @RequestMapping(value = BALANCE_SHEET_PATH, method = RequestMethod.GET)
    public String getBalanceSheet(@PathVariable String transactionId,
                                  @PathVariable String companyAccountsId,
                                  Model model) {

        model.addAttribute(balanceSheetService.getBalanceSheet(transactionId, companyAccountsId));

        return SMALL_FULL_BALANCE_SHEET;
    }

    @RequestMapping(value = BALANCE_SHEET_PATH, method = RequestMethod.POST)
    public String postBalanceSheet(@PathVariable String companyNumber,
                                   @PathVariable String transactionId,
                                   @PathVariable String companyAccountsId,
                                   @ModelAttribute("balanceSheet") @Valid BalanceSheet balanceSheet,
                                   BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return SMALL_FULL_BALANCE_SHEET;
        }

        // The next page will be returned once created
        return SMALL_FULL_BALANCE_SHEET;
    }
}