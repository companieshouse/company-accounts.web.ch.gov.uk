package uk.gov.companieshouse.web.accounts.controller.smallfull;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.apache.commons.lang.BooleanUtils;
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
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.offbalancesheetarrangements.OffBalanceSheetArrangements;
import uk.gov.companieshouse.web.accounts.model.state.CompanyAccountsDataState;
import uk.gov.companieshouse.web.accounts.service.smallfull.OffBalanceSheetArrangementsService;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

@Controller
@NextController(ReviewController.class)
@PreviousController(OffBalanceSheetArrangementsQuestionController.class)
@RequestMapping("/company/{companyNumber}/transaction/{transactionId}/company-accounts/{companyAccountsId}/small-full/off-balance-sheet-arrangements")
public class OffBalanceSheetArrangementsController extends BaseController implements
        ConditionalController {

    private static final String OFF_BALANCE_SHEET_ARRANGEMENTS = "offBalanceSheetArrangements";

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private OffBalanceSheetArrangementsService offBalanceSheetArrangementsService;

    @GetMapping
    public String getOffBalanceSheetArrangements(@PathVariable String companyNumber,
                                                 @PathVariable String transactionId,
                                                 @PathVariable String companyAccountsId,
                                                 Model model) {

        addBackPageAttributeToModel(model, companyNumber, transactionId, companyAccountsId);

        try {
            model.addAttribute(OFF_BALANCE_SHEET_ARRANGEMENTS,
                    offBalanceSheetArrangementsService.getOffBalanceSheetArrangements(transactionId, companyAccountsId));

        } catch (ServiceException e) {

            LOGGER.errorRequest(request, e);
            return ERROR_VIEW;
        }

        return getTemplateName();
    }

    @PostMapping
    public String submitOffBalanceSheetArrangements(@PathVariable String companyNumber,
                                                    @PathVariable String transactionId,
                                                    @PathVariable String companyAccountsId,
                                                    @ModelAttribute(OFF_BALANCE_SHEET_ARRANGEMENTS) @Valid OffBalanceSheetArrangements offBalanceSheetArrangements,
                                                    BindingResult bindingResult,
                                                    Model model) {

        addBackPageAttributeToModel(model, companyNumber, transactionId, companyAccountsId);

        if (bindingResult.hasErrors()) {
            return getTemplateName();
        }

        try {
            List<ValidationError> validationErrors =
                    offBalanceSheetArrangementsService.submitOffBalanceSheetArrangements(
                            transactionId, companyAccountsId, offBalanceSheetArrangements);

            if (!validationErrors.isEmpty()) {
                bindValidationErrors(bindingResult, validationErrors);
                return getTemplateName();
            }
        } catch (ServiceException e) {

            LOGGER.errorRequest(request, e.getMessage(), e);
            return ERROR_VIEW;
        }

        return navigatorService.getNextControllerRedirect(this.getClass(), companyNumber, transactionId, companyAccountsId);
    }

    @Override
    protected String getTemplateName() {
        return "smallfull/offBalanceSheetArrangements";
    }

    @Override
    public boolean willRender(String companyNumber, String transactionId, String companyAccountsId)
            throws ServiceException {

        CompanyAccountsDataState companyAccountsDataState = getStateFromRequest(request);
        return BooleanUtils.isTrue(companyAccountsDataState.getHasIncludedOffBalanceSheetArrangements());
    }
}
