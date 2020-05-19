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

import uk.gov.companieshouse.api.model.company.CompanyProfileApi;
import uk.gov.companieshouse.web.accounts.annotation.NextController;
import uk.gov.companieshouse.web.accounts.controller.BaseController;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.smallfull.AccountsReferenceDateQuestion;
import uk.gov.companieshouse.web.accounts.model.state.CompanyAccountsDataState;
import uk.gov.companieshouse.web.accounts.service.company.CompanyService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDate;

@Controller
@NextController(DirectorsReportQuestionController.class)
@RequestMapping("/company/{companyNumber}/transaction/{transactionId}/company-accounts/{companyAccountsId}/small-full/accounts-reference-date-question")
public class AccountsReferenceDateQuestionController extends BaseController {

    @Autowired
    CompanyService companyService;

    private static final String ACCOUNTS_REFERENCE_DATE_QUESTION = "accountsReferenceDateQuestion";

    @GetMapping
    public String getAccountsReferenceDateQuestion(@PathVariable String companyNumber,
                                                   Model model,
                                                   HttpServletRequest request) {

        AccountsReferenceDateQuestion accountsReferenceDateQuestion = new AccountsReferenceDateQuestion();
        setHasConfirmedAccountingReferenceDate(request, accountsReferenceDateQuestion);

        CompanyProfileApi companyProfile;

        try {
            companyProfile = companyService.getCompanyProfile(companyNumber);
        } catch (ServiceException e) {
                LOGGER.errorRequest(request, e.getMessage(), e);
                return ERROR_VIEW;
        }

        LocalDate periodStartOn = companyProfile.getAccounts().getNextAccounts().getPeriodStartOn();
        LocalDate periodEndOn = companyProfile.getAccounts().getNextAccounts().getPeriodEndOn();

        model.addAttribute(ACCOUNTS_REFERENCE_DATE_QUESTION, accountsReferenceDateQuestion);
        model.addAttribute("start_date", periodStartOn);
        model.addAttribute("end_date", periodEndOn);

        return getTemplateName();
    }

    @PostMapping
    public String submitAccountsReferenceDateQuestion(@PathVariable String companyNumber,
                                                @PathVariable String transactionId,
                                                @PathVariable String companyAccountsId,
                                                @ModelAttribute(ACCOUNTS_REFERENCE_DATE_QUESTION) @Valid AccountsReferenceDateQuestion accountsReferenceDateQuestion,
                                                BindingResult bindingResult,
                                                HttpServletRequest request) {

        if (bindingResult.hasErrors()) {
            return getTemplateName();
        }

        cacheHasConfirmedAccountingReferenceDate(request, accountsReferenceDateQuestion);

        return navigatorService.getNextControllerRedirect(this.getClass(), companyNumber, transactionId, companyAccountsId);
    }

    @Override
    protected String getTemplateName() {
        return "smallfull/accountsReferenceDateQuestion";
    }

    private void setHasConfirmedAccountingReferenceDate(HttpServletRequest request, AccountsReferenceDateQuestion accountsReferenceDateQuestion) {

        CompanyAccountsDataState companyAccountsDataState = getStateFromRequest(request);
        accountsReferenceDateQuestion.setHasConfirmedAccountingReferenceDate(companyAccountsDataState.getHasConfirmedAccountingReferenceDate());
    }

    private void  cacheHasConfirmedAccountingReferenceDate(HttpServletRequest request, AccountsReferenceDateQuestion accountsReferenceDateQuestion) {

        CompanyAccountsDataState companyAccountsDataState = getStateFromRequest(request);
        companyAccountsDataState.setHasConfirmedAccountingReferenceDate(accountsReferenceDateQuestion.getHasConfirmedAccountingReferenceDate());

        updateStateOnRequest(request, companyAccountsDataState);
    }
}
