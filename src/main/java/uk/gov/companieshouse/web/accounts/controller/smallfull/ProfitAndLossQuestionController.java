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
import uk.gov.companieshouse.web.accounts.model.profitandloss.ProfitAndLossQuestion;
import uk.gov.companieshouse.web.accounts.model.state.CompanyAccountsDataState;
import uk.gov.companieshouse.web.accounts.service.smallfull.ProfitAndLossService;

@Controller
@PreviousController(DirectorsReportQuestionController.class)
@NextController(ProfitAndLossController.class)
@RequestMapping("/company/{companyNumber}/transaction/{transactionId}/company-accounts/{companyAccountsId}/small-full/profit-and-loss-question")
public class ProfitAndLossQuestionController extends BaseController {

    private static final String PROFIT_AND_LOSS_QUESTION = "profitAndLossQuestion";

    @Autowired
    private ProfitAndLossService profitAndLossService;

    @GetMapping
    public String getProfitAndLossQuestion(@PathVariable String companyNumber,
                                           @PathVariable String transactionId,
                                           @PathVariable String companyAccountsId,
                                           Model model,
                                           HttpServletRequest request) {

        addBackPageAttributeToModel(model, companyNumber, transactionId, companyAccountsId);

        ProfitAndLossQuestion profitAndLossQuestion = new ProfitAndLossQuestion();
        setIsProfitAndLossIncluded(request, profitAndLossQuestion);

        model.addAttribute(PROFIT_AND_LOSS_QUESTION, profitAndLossQuestion);

        return getTemplateName();
    }

    @PostMapping
    public String submitProfitAndLossQuestion(@PathVariable String companyNumber,
                                              @PathVariable String transactionId,
                                              @PathVariable String companyAccountsId,
                                              @ModelAttribute(PROFIT_AND_LOSS_QUESTION) @Valid ProfitAndLossQuestion profitAndLossQuestion,
                                              BindingResult bindingResult,
                                              Model model,
                                              HttpServletRequest request) {

        addBackPageAttributeToModel(model, companyNumber, transactionId, companyAccountsId);

        if (bindingResult.hasErrors()) {
            return getTemplateName();
        }

        if (!profitAndLossQuestion.getHasIncludedProfitAndLoss()) {
            try {
                profitAndLossService.deleteProfitAndLoss(transactionId, companyAccountsId, companyNumber);
            } catch (ServiceException e) {

                LOGGER.errorRequest(request, e.getMessage(), e);
                return ERROR_VIEW;
            }
        }

        cacheIsProfitAndLossIncluded(request, profitAndLossQuestion);

        return navigatorService.getNextControllerRedirect(this.getClass(), companyNumber, transactionId, companyAccountsId);
    }

    @Override
    protected String getTemplateName() {
        return "smallfull/profitAndLossQuestion";
    }

    private void setIsProfitAndLossIncluded(HttpServletRequest request, ProfitAndLossQuestion profitAndLossQuestion) {

        CompanyAccountsDataState companyAccountsDataState = getStateFromRequest(request);
        profitAndLossQuestion.setHasIncludedProfitAndLoss(companyAccountsDataState.getHasIncludedProfitAndLoss());
    }

    private void  cacheIsProfitAndLossIncluded(HttpServletRequest request, ProfitAndLossQuestion profitAndLossQuestion) {

        CompanyAccountsDataState companyAccountsDataState = getStateFromRequest(request);
        companyAccountsDataState.setHasIncludedProfitAndLoss(profitAndLossQuestion.getHasIncludedProfitAndLoss());

        updateStateOnRequest(request, companyAccountsDataState);
    }
}
