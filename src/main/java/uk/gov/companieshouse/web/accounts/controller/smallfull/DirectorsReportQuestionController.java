package uk.gov.companieshouse.web.accounts.controller.smallfull;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
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
import uk.gov.companieshouse.web.accounts.model.directorsreport.DirectorsReportQuestion;
import uk.gov.companieshouse.web.accounts.model.state.CompanyAccountsDataState;
import uk.gov.companieshouse.web.accounts.model.state.DirectorsReportStatements;
import uk.gov.companieshouse.web.accounts.service.smallfull.DirectorsReportService;

@Controller
@NextController(AddOrRemoveDirectorsController.class)
@PreviousController(AccountsReferenceDateController.class)
@RequestMapping("/company/{companyNumber}/transaction/{transactionId}/company-accounts/{companyAccountsId}/small-full/directors-report-question")
public class DirectorsReportQuestionController extends BaseController {

    private static final String DIRECTORS_REPORT_QUESTION = "directorsReportQuestion";

    @Autowired
    private DirectorsReportService directorsReportService;

    @GetMapping
    public String getDirectorsReportQuestion(Model model,
                                             @PathVariable String companyNumber,
                                             @PathVariable String transactionId,
                                             @PathVariable String companyAccountsId,
                                             HttpServletRequest request) {

        DirectorsReportQuestion directorsReportQuestion = new DirectorsReportQuestion();
        setIsDirectorsReportIncluded(request, directorsReportQuestion);

        addBackPageAttributeToModel(model, companyNumber, transactionId, companyAccountsId);
        model.addAttribute(DIRECTORS_REPORT_QUESTION, directorsReportQuestion);

        return getTemplateName();
    }

    @PostMapping
    public String submitDirectorsReportQuestion(@PathVariable String companyNumber,
                                                @PathVariable String transactionId,
                                                @PathVariable String companyAccountsId,
                                                @ModelAttribute(DIRECTORS_REPORT_QUESTION) @Valid DirectorsReportQuestion directorsReportQuestion,
                                                BindingResult bindingResult,
                                                Model model,
                                                HttpServletRequest request) {

        addBackPageAttributeToModel(model, companyNumber, transactionId, companyAccountsId);

        if (bindingResult.hasErrors()) {
            return getTemplateName();
        }

        try {
            if (Boolean.TRUE.equals(directorsReportQuestion.getHasIncludedDirectorsReport())) {

                directorsReportService.createDirectorsReport(transactionId, companyAccountsId);
            } else {

                directorsReportService.deleteDirectorsReport(transactionId, companyAccountsId);
            }
        } catch (ServiceException e) {

            LOGGER.errorRequest(request, e.getMessage(), e);
            return ERROR_VIEW;
        }

        cacheIsDirectorsReportIncluded(request, directorsReportQuestion);

        return navigatorService.getNextControllerRedirect(this.getClass(), companyNumber, transactionId, companyAccountsId);
    }

    @Override
    protected String getTemplateName() {

        return "smallfull/directorsReportQuestion";
    }

    private void setIsDirectorsReportIncluded(HttpServletRequest request, DirectorsReportQuestion directorsReportQuestion) {

        CompanyAccountsDataState companyAccountsDataState = getStateFromRequest(request);
        directorsReportQuestion.setHasIncludedDirectorsReport(companyAccountsDataState.getHasIncludedDirectorsReport());
    }

    private void  cacheIsDirectorsReportIncluded(HttpServletRequest request, DirectorsReportQuestion directorsReportQuestion) {

        CompanyAccountsDataState companyAccountsDataState = getStateFromRequest(request);
        companyAccountsDataState.setHasIncludedDirectorsReport(directorsReportQuestion.getHasIncludedDirectorsReport());
        companyAccountsDataState.setDirectorsReportStatements(Boolean.TRUE.equals(directorsReportQuestion.getHasIncludedDirectorsReport()) ? new DirectorsReportStatements() : null);

        updateStateOnRequest(request, companyAccountsDataState);
    }
}
