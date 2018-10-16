package uk.gov.companieshouse.web.accounts.controller.smallfull;

import javax.servlet.http.HttpServletRequest;
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
import uk.gov.companieshouse.web.accounts.annotation.NextController;
import uk.gov.companieshouse.web.accounts.annotation.PreviousController;
import uk.gov.companieshouse.web.accounts.controller.BaseController;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.service.smallfull.BalanceSheetService;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;
import uk.gov.companieshouse.web.accounts.model.smallfull.BalanceSheet;
import uk.gov.companieshouse.web.accounts.util.Navigator;

import java.util.List;

@Controller
@NextController(ApprovalController.class)
@PreviousController(StepsToCompleteController.class)
@RequestMapping("/company/{companyNumber}/transaction/{transactionId}/company-accounts/{companyAccountsId}/small-full/balance-sheet")
public class BalanceSheetController extends BaseController {

    @Autowired
    private BalanceSheetService balanceSheetService;

    @Override
    protected String getTemplateName() {
        return "smallfull/balanceSheet";
    }

    @GetMapping
    public String getBalanceSheet(@PathVariable String companyNumber,
                                  @PathVariable String transactionId,
                                  @PathVariable String companyAccountsId,
                                  Model model,
                                  HttpServletRequest request) {

        addBackPageAttributeToModel(model, companyNumber, transactionId, companyAccountsId);

        try {
            model.addAttribute("balanceSheet", balanceSheetService.getBalanceSheet(transactionId, companyAccountsId, companyNumber));
        } catch (ServiceException e) {

            LOGGER.errorRequest(request, e.getMessage(), e);
            return ERROR_VIEW;
        }

        return getTemplateName();
    }

    @PostMapping
    public String postBalanceSheet(@PathVariable String companyNumber,
                                   @PathVariable String transactionId,
                                   @PathVariable String companyAccountsId,
                                   @ModelAttribute("balanceSheet") @Valid BalanceSheet balanceSheet,
                                   BindingResult bindingResult,
                                   HttpServletRequest request) {

        if(bindingResult.hasErrors()) {
            return getTemplateName();
        }

        try {
            List<ValidationError> validationErrors = balanceSheetService.postBalanceSheet(transactionId, companyAccountsId, balanceSheet, companyNumber);

            if (!validationErrors.isEmpty()) {
                bindValidationErrors(bindingResult, validationErrors);
                return getTemplateName();
            }
        } catch (ServiceException e) {

            LOGGER.errorRequest(request, e.getMessage(), e);
            return ERROR_VIEW;
        }

        return Navigator.getNextControllerRedirect(this.getClass(), companyNumber, transactionId, companyAccountsId);
    }
}