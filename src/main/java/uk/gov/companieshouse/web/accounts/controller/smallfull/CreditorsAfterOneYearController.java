package uk.gov.companieshouse.web.accounts.controller.smallfull;

import java.util.List;
import java.util.Optional;
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
import uk.gov.companieshouse.web.accounts.controller.ConditionalController;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.smallfull.BalanceSheet;
import uk.gov.companieshouse.web.accounts.model.smallfull.OtherLiabilitiesOrAssets;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.creditorsafteroneyear.CreditorsAfterOneYear;
import uk.gov.companieshouse.web.accounts.service.smallfull.BalanceSheetService;
import uk.gov.companieshouse.web.accounts.service.smallfull.CreditorsAfterOneYearService;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

@Controller
@NextController(EmployeesQuestionController.class)
@PreviousController(CreditorsWithinOneYearController.class)
@RequestMapping("/company/{companyNumber}/transaction/{transactionId}/company-accounts" +
        "/{companyAccountsId}/small-full/creditors-after-more-than-one-year")
public class CreditorsAfterOneYearController extends BaseController implements
        ConditionalController {

    @Autowired
    private CreditorsAfterOneYearService creditorsAfterOneYearService;

    @Autowired
    private BalanceSheetService balanceSheetService;

    @Override
    protected String getTemplateName() {
        return "smallfull/creditorsAfterOneYear";
    }

    @GetMapping
    public String getCreditorsAfterOneYear(@PathVariable String companyNumber,
            @PathVariable String transactionId, @PathVariable String companyAccountsId, Model model,
            HttpServletRequest request) {

        addBackPageAttributeToModel(model, companyNumber, transactionId, companyAccountsId);

        try {
            CreditorsAfterOneYear creditorsAfterOneYear =
                    creditorsAfterOneYearService.getCreditorsAfterOneYear(transactionId,
                            companyAccountsId,
                            companyNumber);

            model.addAttribute("creditorsAfterOneYear", creditorsAfterOneYear);
        } catch (ServiceException e) {
            LOGGER.errorRequest(request, e.getMessage(), e);
            return ERROR_VIEW;
        }
        return getTemplateName();
    }

    @PostMapping
    public String postCreditorsAfterOneYear(
            @PathVariable String companyNumber,
            @PathVariable String transactionId,
            @PathVariable String companyAccountsId,
            @ModelAttribute("creditorsAfterOneYear") @Valid CreditorsAfterOneYear creditorsAfterOneYear,
            BindingResult bindingResult, Model model, HttpServletRequest request) {

        addBackPageAttributeToModel(model, companyNumber, transactionId, companyAccountsId);

        if (bindingResult.hasErrors()) {
            return getTemplateName();
        }

        try {
            List<ValidationError> validationErrors =
                    creditorsAfterOneYearService.submitCreditorsAfterOneYear(transactionId,
                            companyAccountsId, creditorsAfterOneYear);

            if (! validationErrors.isEmpty()) {
                bindValidationErrors(bindingResult, validationErrors);
                return getTemplateName();
            }
        } catch (ServiceException e) {
            LOGGER.errorRequest(request, e.getMessage(), e);
            return ERROR_VIEW;
        }

        return navigatorService.getNextControllerRedirect(this.getClass(), companyNumber,
                transactionId,
                companyAccountsId);
    }

    @Override
    public boolean willRender(String companyNumber, String transactionId, String companyAccountsId)
            throws ServiceException {

        BalanceSheet balanceSheet =
                balanceSheetService.getBalanceSheet(
                        transactionId, companyAccountsId, companyNumber);

        return hasCreditorsAfter(balanceSheet);
    }

    private boolean hasCreditorsAfter(BalanceSheet balanceSheet) {

        Long currentCreditorsAfter = Optional.of(balanceSheet)
                .map(BalanceSheet::getOtherLiabilitiesOrAssets)
                .map(OtherLiabilitiesOrAssets::getCreditorsAfterOneYear)
                .map(uk.gov.companieshouse.web.accounts.model.smallfull.CreditorsAfterOneYear::getCurrentAmount)
                .orElse(0L);

        Long previousCreditorsAfter = Optional.of(balanceSheet)
                .map(BalanceSheet::getOtherLiabilitiesOrAssets)
                .map(OtherLiabilitiesOrAssets::getCreditorsAfterOneYear)
                .map(uk.gov.companieshouse.web.accounts.model.smallfull.CreditorsAfterOneYear::getPreviousAmount)
                .orElse(0L);

        return !(currentCreditorsAfter.equals(0L) && previousCreditorsAfter.equals(0L));
    }
}
