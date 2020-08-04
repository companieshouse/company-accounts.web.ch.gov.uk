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
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.loanstodirectors.LoansToDirectorsQuestion;
import uk.gov.companieshouse.web.accounts.model.state.CompanyAccountsDataState;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
@PreviousController(CreditorsAfterOneYearController.class)
@NextController(OffBalanceSheetArrangementsQuestionController.class)
@RequestMapping("/company/{companyNumber}/transaction/{transactionId}/company-accounts/{companyAccountsId}/small-full/notes/loans-to-directors-question")
public class LoansToDirectorsQuestionController extends BaseController {

    private static final String LOANS_TO_DIRECTORS_QUESTION = "loansToDirectorsQuestion";

    @GetMapping
    public String getLoansToDirectorsQuestion(Model model,
                                             @PathVariable String companyNumber,
                                             @PathVariable String transactionId,
                                             @PathVariable String companyAccountsId,
                                             HttpServletRequest request) {

        LoansToDirectorsQuestion loansToDirectorsQuestion = new LoansToDirectorsQuestion();
        setIsLoansToDirectorsIncluded(request, loansToDirectorsQuestion);

        addBackPageAttributeToModel(model, companyNumber, transactionId, companyAccountsId);
        model.addAttribute(LOANS_TO_DIRECTORS_QUESTION, loansToDirectorsQuestion);

        return getTemplateName();
    }

    @PostMapping
    public String submitLoansToDirectorsQuestion(@PathVariable String companyNumber,
                                                @PathVariable String transactionId,
                                                @PathVariable String companyAccountsId,
                                                @ModelAttribute(LOANS_TO_DIRECTORS_QUESTION) @Valid LoansToDirectorsQuestion loansToDirectorsQuestion,
                                                BindingResult bindingResult,
                                                Model model,
                                                HttpServletRequest request) {

        addBackPageAttributeToModel(model, companyNumber, transactionId, companyAccountsId);

        if (bindingResult.hasErrors()) {
            return getTemplateName();
        }

        cacheIsLoansToDirectorsIncluded(request, loansToDirectorsQuestion);

        return navigatorService.getNextControllerRedirect(this.getClass(), companyNumber, transactionId, companyAccountsId);
    }

    @Override
    protected String getTemplateName() {
        return "smallfull/loansToDirectorsQuestion";
    }

    private void setIsLoansToDirectorsIncluded(HttpServletRequest request, LoansToDirectorsQuestion loansToDirectorsQuestion) {

        CompanyAccountsDataState companyAccountsDataState = getStateFromRequest(request);
        loansToDirectorsQuestion.setHasIncludedLoansToDirectors(companyAccountsDataState.getHasIncludedLoansToDirectors());
    }

    private void  cacheIsLoansToDirectorsIncluded(HttpServletRequest request, LoansToDirectorsQuestion loansToDirectorsQuestion) {

        CompanyAccountsDataState companyAccountsDataState = getStateFromRequest(request);
        companyAccountsDataState.setHasIncludedLoansToDirectors(loansToDirectorsQuestion.getHasIncludedLoansToDirectors());

        updateStateOnRequest(request, companyAccountsDataState);
    }
}
