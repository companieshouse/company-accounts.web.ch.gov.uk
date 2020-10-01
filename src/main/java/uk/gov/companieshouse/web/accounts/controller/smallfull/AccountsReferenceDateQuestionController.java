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

import org.springframework.web.servlet.view.UrlBasedViewResolver;
import org.springframework.web.util.UriTemplate;
import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.model.accounts.smallfull.SmallFullApi;
import uk.gov.companieshouse.api.model.company.CompanyProfileApi;
import uk.gov.companieshouse.web.accounts.annotation.NextController;
import uk.gov.companieshouse.web.accounts.api.ApiClientService;
import uk.gov.companieshouse.web.accounts.controller.BaseController;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.smallfull.AccountsReferenceDateQuestion;
import uk.gov.companieshouse.web.accounts.model.state.CompanyAccountsDataState;
import uk.gov.companieshouse.web.accounts.service.cic.CicApprovalService;
import uk.gov.companieshouse.web.accounts.service.company.CompanyService;
import uk.gov.companieshouse.web.accounts.service.smallfull.impl.SmallFullServiceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDate;

@Controller
@NextController(AccountsReferenceDateController.class)
@RequestMapping("/company/{companyNumber}/transaction/{transactionId}/company-accounts/{companyAccountsId}/small-full/accounts-reference-date-question")
public class AccountsReferenceDateQuestionController extends BaseController {

    @Autowired
    private CompanyService companyService;

    @Autowired
    private SmallFullServiceImpl smallFullService;

    @Autowired
    private CicApprovalService cicApprovalService;

    @Autowired
    private ApiClientService apiClientService;

    private static final String ACCOUNTS_REFERENCE_DATE_QUESTION = "accountsReferenceDateQuestion";

    private static final UriTemplate CIC_APPROVAL = new UriTemplate("/company/{companyNumber}/transaction/{transactionId}/company-accounts/{companyAccountsId}/cic/approval?dateInvalidated=true");

    @GetMapping
    public String getAccountsReferenceDateQuestion(@PathVariable String companyNumber,
                                                   @PathVariable String transactionId,
                                                   @PathVariable String companyAccountsId,
                                                   Model model,
                                                   HttpServletRequest request) {

        try {

            AccountsReferenceDateQuestion accountsReferenceDateQuestion = new AccountsReferenceDateQuestion();

            ApiClient apiClient = apiClientService.getApiClient();

            CompanyProfileApi companyProfile = companyService.getCompanyProfile(companyNumber);
            SmallFullApi smallFullAccounts = smallFullService.getSmallFullAccounts(apiClient, transactionId, companyAccountsId);

            accountsReferenceDateQuestion.setPeriodStartOn(companyProfile.getAccounts().getNextAccounts().getPeriodStartOn());
            accountsReferenceDateQuestion.setPeriodEndOn(companyProfile.getAccounts().getNextAccounts().getPeriodEndOn());

            LocalDate smallFullPeriodEndOn = smallFullAccounts.getNextAccounts().getPeriodEndOn();

            if (! accountsReferenceDateQuestion.getPeriodEndOn().equals(smallFullPeriodEndOn)) {
                accountsReferenceDateQuestion.setHasConfirmedAccountingReferenceDate(false);
            } else {
                setHasConfirmedAccountingReferenceDate(request, accountsReferenceDateQuestion);
            }

            model.addAttribute(ACCOUNTS_REFERENCE_DATE_QUESTION, accountsReferenceDateQuestion);

            return getTemplateName();

        } catch (ServiceException e) {
                LOGGER.errorRequest(request, e.getMessage(), e);
                return ERROR_VIEW;
        }
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

        try {
            cacheHasConfirmedAccountingReferenceDate(request, accountsReferenceDateQuestion);

            // If the user elects not to change their ARD
            if (Boolean.TRUE.equals(accountsReferenceDateQuestion.getHasConfirmedAccountingReferenceDate())) {

                // Default the period end date
                smallFullService.updateSmallFullAccounts(null, transactionId, companyAccountsId);

                CompanyProfileApi companyProfile = companyService.getCompanyProfile(companyNumber);

                // If the filing is for a CIC
                if (companyProfile.isCommunityInterestCompany()) {

                    LocalDate periodEndOn = companyProfile.getAccounts().getNextAccounts().getPeriodEndOn();
                    LocalDate cicApprovalDate = cicApprovalService.getCicApproval(transactionId, companyAccountsId).getLocalDate();

                    // And CIC approval date is before the default period end date
                    if (cicApprovalDate != null && !cicApprovalDate.isAfter(periodEndOn)) {

                        // Return the user to CIC approval
                        return UrlBasedViewResolver.REDIRECT_URL_PREFIX + CIC_APPROVAL.expand(companyNumber, transactionId, companyAccountsId).toString();
                    }
                }
            }

            return navigatorService.getNextControllerRedirect(this.getClass(), companyNumber, transactionId, companyAccountsId);
        } catch (ServiceException e) {
            LOGGER.errorRequest(request, e.getMessage(), e);
            return ERROR_VIEW;
        }
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
