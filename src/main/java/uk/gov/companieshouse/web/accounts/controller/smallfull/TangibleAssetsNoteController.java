package uk.gov.companieshouse.web.accounts.controller.smallfull;

import java.util.List;
import java.util.Optional;
import java.util.OptionalLong;
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
import uk.gov.companieshouse.api.model.company.CompanyProfileApi;
import uk.gov.companieshouse.web.accounts.annotation.NextController;
import uk.gov.companieshouse.web.accounts.annotation.PreviousController;
import uk.gov.companieshouse.web.accounts.controller.BaseController;
import uk.gov.companieshouse.web.accounts.controller.ConditionalController;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.smallfull.BalanceSheet;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.tangible.TangibleAssets;
import uk.gov.companieshouse.web.accounts.service.company.CompanyService;
import uk.gov.companieshouse.web.accounts.service.smallfull.BalanceSheetService;
import uk.gov.companieshouse.web.accounts.service.smallfull.TangibleAssetsNoteService;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

@Controller
@NextController(ReviewController.class)
@PreviousController(OtherAccountingPolicyController.class)
@RequestMapping("/company/{companyNumber}/transaction/{transactionId}/company-accounts/{companyAccountsId}/small-full/note/tangible-assets")
public class TangibleAssetsNoteController extends BaseController implements ConditionalController {

    @Autowired
    private TangibleAssetsNoteService tangibleAssetsNoteService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private BalanceSheetService balanceSheetService;

    @Override
    protected String getTemplateName() {
        return "smallfull/tangibleAssetsNote";
    }

    @GetMapping
    public String getTangibleAssetsNote(@PathVariable String companyNumber,
        @PathVariable String transactionId,
        @PathVariable String companyAccountsId,
        Model model,
        HttpServletRequest request) {

        addBackPageAttributeToModel(model, companyNumber, transactionId, companyAccountsId);

        try {
            TangibleAssets tangibleAssets = tangibleAssetsNoteService
                .getTangibleAssets(transactionId, companyAccountsId, companyNumber);

            addDatesToFormObject(tangibleAssets, companyService.getCompanyProfile(companyNumber));

            model.addAttribute("tangibleAssets", tangibleAssets);

        } catch (ServiceException e) {
            LOGGER.errorRequest(request, e.getMessage(), e);
            return ERROR_VIEW;
        }

        return getTemplateName();
    }

    @PostMapping
    public String postTangibleAssets(@PathVariable String companyNumber,
        @PathVariable String transactionId,
        @PathVariable String companyAccountsId,
        @ModelAttribute("tangibleAssets") @Valid TangibleAssets tangibleAssets,
        BindingResult bindingResult,
        Model model,
        HttpServletRequest request) {

        addBackPageAttributeToModel(model, companyNumber, transactionId, companyAccountsId);

        if (bindingResult.hasErrors()) {
            return getTemplateName();
        }

        try {
            List<ValidationError> validationErrors = tangibleAssetsNoteService
                .postTangibleAssets(transactionId, companyAccountsId, tangibleAssets,
                    companyNumber);
            if (!validationErrors.isEmpty()) {
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

    private void addDatesToFormObject(TangibleAssets tangibleAssets, CompanyProfileApi companyProfile) {
        tangibleAssets.setLastAccountsPeriodEndOn(companyProfile.getAccounts().getLastAccounts().getPeriodEndOn());
        tangibleAssets.setNextAccountsPeriodStartOn(companyProfile.getAccounts().getNextAccounts().getPeriodStartOn());
        tangibleAssets.setNextAccountsPeriodEndOn(companyProfile.getAccounts().getNextAccounts().getPeriodEndOn());
    }

    @Override
    public boolean willRender(String companyNumber, String transactionId,
        String companyAccountsId) {

        try {
            BalanceSheet balanceSheet = balanceSheetService.getBalanceSheet(
                transactionId, companyAccountsId, companyNumber);

            Long currentTangible = Optional.ofNullable(
                balanceSheet.getFixedAssets().getTangibleAssets().getCurrentAmount()).orElse(0L);
            Long previousTangible = Optional.ofNullable(
                balanceSheet.getFixedAssets().getTangibleAssets().getPreviousAmount()).orElse(0L);

            return (currentTangible.equals(0L) && previousTangible.equals(0L));

        } catch (ServiceException e) {
            return false;
        }
    }


}