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
import uk.gov.companieshouse.web.accounts.model.smallfull.CurrentAssets;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.debtors.Debtors;
import uk.gov.companieshouse.web.accounts.service.NoteService;
import uk.gov.companieshouse.web.accounts.service.smallfull.BalanceSheetService;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

@Controller
@NextController(CurrentAssetsInvestmentsController.class)
@PreviousController(StocksController.class)
@RequestMapping("/company/{companyNumber}/transaction/{transactionId}/company-accounts" +
        "/{companyAccountsId}/small-full/debtors")
public class DebtorsController extends BaseController implements ConditionalController {

    @Autowired
    private NoteService<Debtors> debtorsService;

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
            Model model, HttpServletRequest request) {

        addBackPageAttributeToModel(model, companyNumber, transactionId, companyAccountsId);

        try {
            Debtors debtors = debtorsService.get(transactionId, companyAccountsId,
                    NoteType.SMALL_FULL_DEBTORS);

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
                    debtorsService.submit(transactionId, companyAccountsId,
                            debtors, NoteType.SMALL_FULL_DEBTORS);

            if (! validationErrors.isEmpty()) {
                bindValidationErrors(bindingResult, validationErrors);
                return getTemplateName();
            }
        } catch (ServiceException e) {
            LOGGER.errorRequest(request, e.getMessage(), e);
            return ERROR_VIEW;
        }

        return navigatorService.getNextControllerRedirect(this.getClass(), companyNumber, transactionId,
                companyAccountsId);
    }

    @Override
    public boolean willRender(String companyNumber, String transactionId, String companyAccountsId)
            throws ServiceException {

        BalanceSheet balanceSheet =
                balanceSheetService.getBalanceSheet(
                        transactionId, companyAccountsId, companyNumber);

        return hasDebtors(balanceSheet);
    }

    private boolean hasDebtors(BalanceSheet balanceSheet) {

        Long currentDebtors = Optional.of(balanceSheet)
                .map(BalanceSheet::getCurrentAssets)
                .map(CurrentAssets::getDebtors)
                .map(uk.gov.companieshouse.web.accounts.model.smallfull.Debtors::getCurrentAmount)
                .orElse(0L);

        Long previousDebtors = Optional.of(balanceSheet)
                .map(BalanceSheet::getCurrentAssets)
                .map(CurrentAssets::getDebtors)
                .map(uk.gov.companieshouse.web.accounts.model.smallfull.Debtors::getPreviousAmount)
                .orElse(0L);

        return !(currentDebtors.equals(0L) && previousDebtors.equals(0L));
    }
}
