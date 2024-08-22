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
import uk.gov.companieshouse.web.accounts.model.smallfull.OtherLiabilitiesOrAssets;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.creditorswithinoneyear.CreditorsWithinOneYear;
import uk.gov.companieshouse.web.accounts.service.NoteService;
import uk.gov.companieshouse.web.accounts.service.smallfull.BalanceSheetService;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

@Controller
@NextController(CreditorsAfterOneYearController.class)
@PreviousController(CurrentAssetsInvestmentsController.class)
@RequestMapping("/company/{companyNumber}/transaction/{transactionId}/company-accounts/{companyAccountsId}/small-full/creditors-within-one-year")
public class CreditorsWithinOneYearController extends BaseController implements
        ConditionalController {

    @Autowired
    private NoteService<CreditorsWithinOneYear> noteService;

    @Autowired
    private BalanceSheetService balanceSheetService;

    @Override
    protected String getTemplateName() {
        return "smallfull/creditorsWithinOneYear";
    }

    @GetMapping
    public String getCreditorsWithinOneYear(@PathVariable String companyNumber,
            @PathVariable String transactionId, @PathVariable String companyAccountsId, Model model,
            HttpServletRequest request) {

        addBackPageAttributeToModel(model, companyNumber, transactionId, companyAccountsId);

        try {

            model.addAttribute("creditorsWithinOneYear", noteService.get(transactionId, companyAccountsId, NoteType.SMALL_FULL_CREDITORS_WITHIN_ONE_YEAR));
        } catch (ServiceException e) {
            LOGGER.errorRequest(request, e.getMessage(), e);
            return ERROR_VIEW;
        }
        return getTemplateName();
    }

    @PostMapping
    public String postCreditorsWithinOneYear(
            @PathVariable String companyNumber,
            @PathVariable String transactionId,
            @PathVariable String companyAccountsId,
            @ModelAttribute("creditorsWithinOneYear") @Valid CreditorsWithinOneYear creditorsWithinOneYear,
            BindingResult bindingResult, Model model, HttpServletRequest request) {

        addBackPageAttributeToModel(model, companyNumber, transactionId, companyAccountsId);

        if (bindingResult.hasErrors()) {
            return getTemplateName();
        }

        try {
            List<ValidationError> validationErrors =
                    noteService.submit(transactionId, companyAccountsId, creditorsWithinOneYear, NoteType.SMALL_FULL_CREDITORS_WITHIN_ONE_YEAR);

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

        return hasCreditorsWithin(balanceSheet);
    }

    private boolean hasCreditorsWithin(BalanceSheet balanceSheet) {

        Long currentCreditorsWithin = Optional.of(balanceSheet)
                .map(BalanceSheet::getOtherLiabilitiesOrAssets)
                .map(OtherLiabilitiesOrAssets::getCreditorsDueWithinOneYear)
                .map(uk.gov.companieshouse.web.accounts.model.smallfull.CreditorsDueWithinOneYear::getCurrentAmount)
                .orElse(0L);

        Long previousCreditorsWithin = Optional.of(balanceSheet)
                .map(BalanceSheet::getOtherLiabilitiesOrAssets)
                .map(OtherLiabilitiesOrAssets::getCreditorsDueWithinOneYear)
                .map(uk.gov.companieshouse.web.accounts.model.smallfull.CreditorsDueWithinOneYear::getPreviousAmount)
                .orElse(0L);

        return !(currentCreditorsWithin.equals(0L) && previousCreditorsWithin.equals(0L));
    }
}
