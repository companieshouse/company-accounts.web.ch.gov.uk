package uk.gov.companieshouse.web.accounts.controller.smallfull;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
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
import uk.gov.companieshouse.web.accounts.enumeration.NoteType;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.financialcommitments.FinancialCommitments;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.financialcommitments.FinancialCommitmentsQuestion;
import uk.gov.companieshouse.web.accounts.model.state.CompanyAccountsDataState;
import uk.gov.companieshouse.web.accounts.service.NoteService;

@Controller
@NextController(FinancialCommitmentsController.class)
@PreviousController(CreditorsAfterOneYearController.class)
@RequestMapping("/company/{companyNumber}/transaction/{transactionId}/company-accounts/{companyAccountsId}/small-full/financial-commitments-question")
public class FinancialCommitmentsQuestionController extends BaseController {

    private static final String FINANCIAL_COMMITMENTS_QUESTION = "financialCommitmentsQuestion";

    @Autowired
    private NoteService<FinancialCommitments> noteService;

    @Autowired
    private HttpServletRequest request;

    @GetMapping
    public String getFinancialCommitmentsQuestion(@PathVariable String companyNumber,
                                                         @PathVariable String transactionId,
                                                         @PathVariable String companyAccountsId,
                                                         Model model) {

        addBackPageAttributeToModel(model, companyNumber, transactionId, companyAccountsId);

        FinancialCommitmentsQuestion financialCommitmentsQuestion = new FinancialCommitmentsQuestion();

        try {
            if (StringUtils.isNotBlank(
                    noteService.get(transactionId, companyAccountsId, NoteType.SMALL_FULL_FINANCIAL_COMMITMENTS)
                            .getFinancialCommitmentsDetails())) {

                financialCommitmentsQuestion.setHasIncludedFinancialCommitments(true);
            } else {

                setIsFinancialCommitmentsIncludedFromCache(request,
                        financialCommitmentsQuestion);
            }
        } catch (ServiceException e) {

            LOGGER.errorRequest(request, e);
            return ERROR_VIEW;
        }

        model.addAttribute(FINANCIAL_COMMITMENTS_QUESTION, financialCommitmentsQuestion);

        return getTemplateName();
    }

    @PostMapping
    public String submitFinancialCommitmentsQuestion(@PathVariable String companyNumber,
                                                            @PathVariable String transactionId,
                                                            @PathVariable String companyAccountsId,
                                                            @ModelAttribute(FINANCIAL_COMMITMENTS_QUESTION) @Valid FinancialCommitmentsQuestion financialCommitmentsQuestion,
                                                            BindingResult bindingResult,
                                                            Model model) {

        addBackPageAttributeToModel(model, companyNumber, transactionId, companyAccountsId);

        if (bindingResult.hasErrors()) {
            return getTemplateName();
        }

        try {
            if (Boolean.FALSE.equals(financialCommitmentsQuestion.getHasIncludedFinancialCommitments())) {
                
                noteService.delete(transactionId, companyAccountsId, NoteType.SMALL_FULL_FINANCIAL_COMMITMENTS);
            }
        } catch (ServiceException e) {

            LOGGER.errorRequest(request, e.getMessage(), e);
            return ERROR_VIEW;
        }

        cacheIsFinancialCommitmentsIncluded(request, financialCommitmentsQuestion);

        return navigatorService.getNextControllerRedirect(this.getClass(), companyNumber, transactionId, companyAccountsId);
    }

    private void setIsFinancialCommitmentsIncludedFromCache(HttpServletRequest request, FinancialCommitmentsQuestion financialCommitmentsQuestion) {

        CompanyAccountsDataState companyAccountsDataState = getStateFromRequest(request);
        financialCommitmentsQuestion.setHasIncludedFinancialCommitments(companyAccountsDataState.getHasIncludedFinancialCommitments());
    }

    private void cacheIsFinancialCommitmentsIncluded(HttpServletRequest request, FinancialCommitmentsQuestion financialCommitmentsQuestion) {

        CompanyAccountsDataState companyAccountsDataState = getStateFromRequest(request);
        companyAccountsDataState.setHasIncludedFinancialCommitments(financialCommitmentsQuestion.getHasIncludedFinancialCommitments());

        updateStateOnRequest(request, companyAccountsDataState);
    }

    @Override
    protected String getTemplateName() {
        return "smallfull/financialCommitmentsQuestion";
    }
}
