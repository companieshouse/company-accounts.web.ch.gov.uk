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
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.debtors.Debtors;
import uk.gov.companieshouse.web.accounts.service.smallfull.BalanceSheetService;
import uk.gov.companieshouse.web.accounts.service.smallfull.DebtorsService;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Controller
@NextController(ReviewController.class)
@PreviousController(OtherAccountingPolicyController.class)
@RequestMapping("/company/{companyNumber}/transaction/{transactionId}/company-accounts" +
        "/{companyAccountsId}/small-full/debtors")
public class DebtorsController extends BaseController implements ConditionalController {

    @Autowired
    private DebtorsService debtorsService;

    @Autowired
    private BalanceSheetService balanceSheetService;

    @Override
    protected String getTemplateName() {
        return "smallfull/debtors";
    }

    @GetMapping
    public String getDebtors(@PathVariable String companyNumber,
            @PathVariable String transactionId,
            @PathVariable String companyAccountsId,
            Model model, HttpServletRequest request) throws ServiceException {

        addBackPageAttributeToModel(model, companyNumber, transactionId, companyAccountsId);

        try {
            Debtors debtors = debtorsService.getDebtors(transactionId, companyAccountsId,
                    companyNumber);

            model.addAttribute("debtors", debtors);

        } catch (ServiceException e) {
            LOGGER.errorRequest(request, e.getMessage(), e);
            return ERROR_VIEW;

        }

        return getTemplateName();
    }


    @PostMapping
    public String postDebtors(@PathVariable String companyNumber,
            @PathVariable String transactionId,
            @PathVariable String companyAccountsId,
            @ModelAttribute("debtors") @Valid Debtors debtors,
            BindingResult bindingResult,
            Model model,
            HttpServletRequest request) {

        addBackPageAttributeToModel(model, companyNumber, transactionId, companyAccountsId);

        if (bindingResult.hasErrors()) {
            return getTemplateName();
        }

        try {
            List<ValidationError> validationErrors =
                    debtorsService.submitDebtors(transactionId, companyAccountsId,
                            debtors, companyNumber);

            if (! validationErrors.isEmpty()) {
                bindValidationErrors(bindingResult, validationErrors);
                return getTemplateName();
            }
        } catch (ServiceException e) {
            LOGGER.errorRequest(request, e.getMessage(), e);
            return ERROR_VIEW;
        }

        return navigator.getNextControllerRedirect(this.getClass(), companyNumber, transactionId,
                companyAccountsId);
    }

    @Override
    public boolean willRender(String companyNumber, String transactionId,
            String companyAccountsId) {
        try {
            BalanceSheet balanceSheet = balanceSheetService.getBalanceSheet(
                    transactionId, companyAccountsId, companyNumber);
            return shouldDebtorsNoteRender(balanceSheet);
        } catch (ServiceException e) {
            return false;
        }
    }

    /**
     *
     * Only render debtors note if debtors balance sheet values are not both null or 0
     * @param balanceSheet
     * @return boolean
     */
    private boolean shouldDebtorsNoteRender(BalanceSheet balanceSheet) {
        if (balanceSheet.getCurrentAssets() != null && (balanceSheet.getCurrentAssets().getDebtors() != null)) {


            if (balanceSheet.getCurrentAssets().getDebtors().getPreviousAmount() == null &&
                    balanceSheet.getCurrentAssets().getDebtors().getCurrentAmount() != null &&
                    balanceSheet.getCurrentAssets().getDebtors().getCurrentAmount() != 0) {
                return true;
            }

            if (balanceSheet.getCurrentAssets().getDebtors().getCurrentAmount() == null &&
                    balanceSheet.getCurrentAssets().getDebtors().getPreviousAmount() != null &&
                    balanceSheet.getCurrentAssets().getDebtors().getPreviousAmount() != 0) {
                return true;
            }

            if (balanceSheet.getCurrentAssets().getDebtors().getPreviousAmount() != null &&
                    balanceSheet.getCurrentAssets().getDebtors().getCurrentAmount() != null &&
                    (balanceSheet.getCurrentAssets().getDebtors().getCurrentAmount() != 0 ||
                    balanceSheet.getCurrentAssets().getDebtors().getPreviousAmount() != 0)) {
                return true;
            }
        }
        return false;
    }
}