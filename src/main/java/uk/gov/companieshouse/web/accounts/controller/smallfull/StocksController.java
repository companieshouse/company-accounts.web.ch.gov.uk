package uk.gov.companieshouse.web.accounts.controller.smallfull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import uk.gov.companieshouse.web.accounts.annotation.NextController;
import uk.gov.companieshouse.web.accounts.annotation.PreviousController;
import uk.gov.companieshouse.web.accounts.controller.BaseController;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.stocks.StocksNote;
import uk.gov.companieshouse.web.accounts.service.smallfull.BalanceSheetService;
import uk.gov.companieshouse.web.accounts.service.smallfull.StocksService;

import javax.servlet.http.HttpServletRequest;

@Controller
@NextController(DebtorsController.class)
@PreviousController(OtherAccountingPolicyController.class)
@RequestMapping("/company/{companyNumber}/transaction/{transactionId}/company-accounts/{companyAccountsId}/small" +
    "-full/stocks")
public class StocksController extends BaseController {

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

    @Override
    protected String getTemplateName() {
        return "smallfull/stocks";
    }
}
