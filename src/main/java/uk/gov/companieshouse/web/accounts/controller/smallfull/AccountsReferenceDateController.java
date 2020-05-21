package uk.gov.companieshouse.web.accounts.controller.smallfull;

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
import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.model.accounts.smallfull.SmallFullApi;
import uk.gov.companieshouse.api.model.company.CompanyProfileApi;
import uk.gov.companieshouse.web.accounts.annotation.NextController;
import uk.gov.companieshouse.web.accounts.annotation.PreviousController;
import uk.gov.companieshouse.web.accounts.api.ApiClientService;
import uk.gov.companieshouse.web.accounts.controller.BaseController;
import uk.gov.companieshouse.web.accounts.controller.ConditionalController;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.smallfull.AccountsReferenceDate;
import uk.gov.companieshouse.web.accounts.model.smallfull.AccountsReferenceDateQuestion;
import uk.gov.companieshouse.web.accounts.model.state.CompanyAccountsDataState;
import uk.gov.companieshouse.web.accounts.service.company.impl.CompanyServiceImpl;
import uk.gov.companieshouse.web.accounts.service.navigation.NavigatorService;
import uk.gov.companieshouse.web.accounts.service.smallfull.impl.SmallFullServiceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
@NextController(DirectorsReportQuestionController.class)
@PreviousController(AccountsReferenceDateQuestionController.class)
@RequestMapping("/company/{companyNumber}/transaction/{transactionId}/company-accounts/{companyAccountsId}/small-full/accounts-reference-date")
public class AccountsReferenceDateController extends BaseController implements ConditionalController {

    @Autowired
    private NavigatorService navigatorService;

    @Autowired
    private CompanyServiceImpl companyService;

    @Autowired
    private ApiClientService apiClientService;

    @Autowired
    private SmallFullServiceImpl smallFullService;

    @Autowired
    private HttpServletRequest request;

    private static final String ACCOUNTS_REFERENCE_DATE = "accountsReferenceDate";


    @GetMapping
    public String getAccountsReferenceDateQuestion(@PathVariable String companyNumber,
                                                   @PathVariable String transactionId,
                                                   @PathVariable String companyAccountsId,
                                                   Model model,
                                                   HttpServletRequest request) {

        try {
            addBackPageAttributeToModel(model, companyNumber, transactionId, companyAccountsId);

            AccountsReferenceDate accountsReferenceDate = new AccountsReferenceDate();

            ApiClient apiClient = apiClientService.getApiClient();
            CompanyProfileApi companyProfile = companyService.getCompanyProfile(companyNumber);
            SmallFullApi smallFullAccounts = smallFullService.getSmallFullAccounts(apiClient, transactionId, companyAccountsId);

            accountsReferenceDate.setPastDates(getPastValidDates(companyProfile.getAccounts().getNextAccounts().getPeriodEndOn()));
            accountsReferenceDate.setFutureDates(getFutureValidDates(companyProfile.getAccounts().getNextAccounts().getPeriodEndOn()));

            if(!companyProfile.getAccounts().getNextAccounts().getPeriodEndOn().equals(smallFullAccounts.getNextAccounts().getPeriodEndOn())) {
                accountsReferenceDate.setChosenDate(smallFullAccounts.getNextAccounts().getPeriodEndOn());
            }

            model.addAttribute(ACCOUNTS_REFERENCE_DATE, accountsReferenceDate);

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
                                                      Model model,
                                                      @ModelAttribute(ACCOUNTS_REFERENCE_DATE) @Valid AccountsReferenceDate accountsReferenceDate,
                                                      BindingResult bindingResult,
                                                      HttpServletRequest request) {

        addBackPageAttributeToModel(model, companyNumber, transactionId, companyAccountsId);

        if (bindingResult.hasErrors()) {
            return getTemplateName();
        }

        try {
            smallFullService.updateSmallFullAccounts(accountsReferenceDate.getChosenDate(), transactionId, companyAccountsId );

            return navigatorService.getNextControllerRedirect(this.getClass(), companyNumber, transactionId, companyAccountsId);
        } catch (ServiceException e) {
            LOGGER.errorRequest(request, e.getMessage(), e);
            return ERROR_VIEW;
        }
    }

    @Override
    protected String getTemplateName() {
        return "smallfull/accountsReferenceDate";
    }

    @Override
    public boolean willRender(String companyNumber, String transactionId, String companyAccountsId)
            throws ServiceException {

        CompanyAccountsDataState companyAccountsDataState = getStateFromRequest(request);
        return BooleanUtils.isTrue(!companyAccountsDataState.getHasConfirmedAccountingReferenceDate());
    }

    private List<LocalDate> getFutureValidDates(LocalDate periodEndOn) {

        List<LocalDate> futureValidDates = new ArrayList<>();


        futureValidDates.add(periodEndOn);
        futureValidDates.add(periodEndOn.plusDays(1));
        futureValidDates.add(periodEndOn.plusDays(2));
        futureValidDates.add(periodEndOn.plusDays(3));
        futureValidDates.add(periodEndOn.plusDays(4));
        futureValidDates.add(periodEndOn.plusDays(5));
        futureValidDates.add(periodEndOn.plusDays(6));

        Collections.sort(futureValidDates);

        return  futureValidDates;
    }

    private List<LocalDate> getPastValidDates(LocalDate periodEndOn) {

        List<LocalDate> pastValidDates = new ArrayList<>();

        pastValidDates.add(periodEndOn.minusDays(1));
        pastValidDates.add(periodEndOn.minusDays(2));
        pastValidDates.add(periodEndOn.minusDays(3));
        pastValidDates.add(periodEndOn.minusDays(4));
        pastValidDates.add(periodEndOn.minusDays(5));
        pastValidDates.add(periodEndOn.minusDays(6));
        pastValidDates.add(periodEndOn.minusDays(7));

        Collections.sort(pastValidDates);

        return pastValidDates;
    }
}
