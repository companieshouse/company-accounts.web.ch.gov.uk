package uk.gov.companieshouse.web.accounts.controller.smallfull;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.apache.commons.lang.StringUtils;
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
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.offbalancesheetarrangements.OffBalanceSheetArrangementsQuestion;
import uk.gov.companieshouse.web.accounts.model.state.CompanyAccountsDataState;
import uk.gov.companieshouse.web.accounts.service.smallfull.OffBalanceSheetArrangementsService;

@Controller
@NextController(OffBalanceSheetArrangementsController.class)
@PreviousController(CreditorsAfterOneYearController.class)
@RequestMapping("/company/{companyNumber}/transaction/{transactionId}/company-accounts/{companyAccountsId}/small-full/off-balance-sheet-arrangements-question")
public class OffBalanceSheetArrangementsQuestionController extends BaseController {

    private static final String OFF_BALANCE_SHEET_ARRANGEMENTS_QUESTION = "offBalanceSheetArrangementsQuestion";

    @Autowired
    private HttpServletRequest request;
    
    @Autowired
    private OffBalanceSheetArrangementsService offBalanceSheetArrangementsService;

    @GetMapping
    public String getOffBalanceSheetArrangementsQuestion(@PathVariable String companyNumber,
                                                         @PathVariable String transactionId,
                                                         @PathVariable String companyAccountsId,
                                                         Model model) {

        addBackPageAttributeToModel(model, companyNumber, transactionId, companyAccountsId);

        OffBalanceSheetArrangementsQuestion offBalanceSheetArrangementsQuestion = new OffBalanceSheetArrangementsQuestion();

        try {
            if (StringUtils.isNotBlank(offBalanceSheetArrangementsService
                    .getOffBalanceSheetArrangements(transactionId, companyAccountsId)
                            .getOffBalanceSheetArrangementsDetails())) {

                offBalanceSheetArrangementsQuestion.setHasIncludedOffBalanceSheetArrangements(true);
            } else {

                setIsOffBalanceSheetArrangementsIncludedFromCache(request,
                        offBalanceSheetArrangementsQuestion);
            }
        } catch (ServiceException e) {

            LOGGER.errorRequest(request, e);
            return ERROR_VIEW;
        }

        model.addAttribute(OFF_BALANCE_SHEET_ARRANGEMENTS_QUESTION, offBalanceSheetArrangementsQuestion);

        return getTemplateName();
    }

    @PostMapping
    public String submitOffBalanceSheetArrangementsQuestion(@PathVariable String companyNumber,
                                                            @PathVariable String transactionId,
                                                            @PathVariable String companyAccountsId,
                                                            @ModelAttribute(OFF_BALANCE_SHEET_ARRANGEMENTS_QUESTION) @Valid OffBalanceSheetArrangementsQuestion offBalanceSheetArrangementsQuestion,
                                                            BindingResult bindingResult,
                                                            Model model) {

        addBackPageAttributeToModel(model, companyNumber, transactionId, companyAccountsId);

        if (bindingResult.hasErrors()) {
            return getTemplateName();
        }

        try {
            if (!offBalanceSheetArrangementsQuestion.getHasIncludedOffBalanceSheetArrangements()) {
                
                offBalanceSheetArrangementsService.deleteOffBalanceSheetArrangements(transactionId, companyAccountsId);
            }
        } catch (ServiceException e) {

            LOGGER.errorRequest(request, e.getMessage(), e);
            return ERROR_VIEW;
        }

        cacheIsOffBalanceSheetArrangementsIncluded(request, offBalanceSheetArrangementsQuestion);

        return navigatorService.getNextControllerRedirect(this.getClass(), companyNumber, transactionId, companyAccountsId);
    }

    private void setIsOffBalanceSheetArrangementsIncludedFromCache(HttpServletRequest request, OffBalanceSheetArrangementsQuestion directorsReportQuestion) {

        CompanyAccountsDataState companyAccountsDataState = getStateFromRequest(request);
        directorsReportQuestion.setHasIncludedOffBalanceSheetArrangements(companyAccountsDataState.getHasIncludedOffBalanceSheetArrangements());
    }

    private void  cacheIsOffBalanceSheetArrangementsIncluded(HttpServletRequest request, OffBalanceSheetArrangementsQuestion directorsReportQuestion) {

        CompanyAccountsDataState companyAccountsDataState = getStateFromRequest(request);
        companyAccountsDataState.setHasIncludedOffBalanceSheetArrangements(directorsReportQuestion.getHasIncludedOffBalanceSheetArrangements());

        updateStateOnRequest(request, companyAccountsDataState);
    }

    @Override
    protected String getTemplateName() {
        return "smallfull/offBalanceSheetArrangementsQuestion";
    }
}
