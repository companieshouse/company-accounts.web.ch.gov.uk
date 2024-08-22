package uk.gov.companieshouse.web.accounts.controller.smallfull;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.model.accounts.smallfull.loanstodirectors.LoansToDirectorsApi;
import uk.gov.companieshouse.web.accounts.annotation.NextController;
import uk.gov.companieshouse.web.accounts.annotation.PreviousController;
import uk.gov.companieshouse.web.accounts.api.ApiClientService;
import uk.gov.companieshouse.web.accounts.controller.BaseController;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.loanstodirectors.LoansToDirectorsQuestion;
import uk.gov.companieshouse.web.accounts.model.state.CompanyAccountsDataState;
import uk.gov.companieshouse.web.accounts.service.smallfull.LoanService;
import uk.gov.companieshouse.web.accounts.service.smallfull.LoansToDirectorsService;

@Controller
@PreviousController(FinancialCommitmentsController.class)
@NextController(AddOrRemoveLoansController.class)
@RequestMapping("/company/{companyNumber}/transaction/{transactionId}/company-accounts/{companyAccountsId}/small-full/notes/loans-to-directors-question")
public class LoansToDirectorsQuestionController extends BaseController {

    private static final String LOANS_TO_DIRECTORS_QUESTION = "loansToDirectorsQuestion";

    @Autowired
    private LoansToDirectorsService loansToDirectorsService;

    @Autowired
    private LoanService loansService;

    @Autowired
    private ApiClientService apiClientService;

    @GetMapping
    public String getLoansToDirectorsQuestion(Model model,
                                             @PathVariable String companyNumber,
                                             @PathVariable String transactionId,
                                             @PathVariable String companyAccountsId,
                                             HttpServletRequest request) {

        LoansToDirectorsQuestion loansToDirectorsQuestion = new LoansToDirectorsQuestion();

        ApiClient apiClient = apiClientService.getApiClient();
        try {
            LoansToDirectorsApi loansToDirectorsApi = loansToDirectorsService.getLoansToDirectors(apiClient, transactionId, companyAccountsId);
            if (loansToDirectorsApi != null && loansToDirectorsApi.getLoans() != null && !loansToDirectorsApi.getLoans().isEmpty()) {
                loansToDirectorsQuestion.setHasIncludedLoansToDirectors(true);
            } else {
                setIsLoansToDirectorsIncluded(request, loansToDirectorsQuestion);
            }
        } catch (ServiceException e) {
            LOGGER.errorRequest(request, e.getMessage(), e);
            return ERROR_VIEW;
        }

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

        ApiClient apiClient = apiClientService.getApiClient();

        try {
            LoansToDirectorsApi loansToDirectorsApi = loansToDirectorsService.getLoansToDirectors(apiClient, transactionId, companyAccountsId);

            if (Boolean.TRUE.equals(loansToDirectorsQuestion.getHasIncludedLoansToDirectors()) && loansToDirectorsApi == null) {
                loansToDirectorsService.createLoansToDirectors(transactionId, companyAccountsId);                
            } else if(Boolean.FALSE.equals(loansToDirectorsQuestion.getHasIncludedLoansToDirectors()) && loansToDirectorsApi != null) {
                deleteLoansToDirectorsLoans(loansToDirectorsApi, transactionId, companyAccountsId);
            }
        } catch (ServiceException e) {
            LOGGER.errorRequest(request, e.getMessage(), e);
            return ERROR_VIEW;
        }

        cacheIsLoansToDirectorsIncluded(request, loansToDirectorsQuestion);

        return navigatorService.getNextControllerRedirect(this.getClass(), companyNumber, transactionId, companyAccountsId);
    }

    private void deleteLoansToDirectorsLoans(LoansToDirectorsApi loansToDirectorsApi, String transactionId, String companyAccountsId) throws ServiceException {
        if (loansToDirectorsApi.getLinks().getAdditionalInformation() != null) {
            Map<String, String> loans = loansToDirectorsApi.getLoans();
            if (loans != null) {
                for (String loanId : loans.keySet()) {
                    loansService.deleteLoan(transactionId, companyAccountsId, loanId);
                }
            }
        } else {
            loansToDirectorsService.deleteLoansToDirectors(transactionId, companyAccountsId);
        }
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
