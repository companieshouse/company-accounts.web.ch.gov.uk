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
import org.springframework.web.servlet.view.UrlBasedViewResolver;
import org.springframework.web.util.UriTemplate;
import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.model.accounts.smallfull.SmallFullApi;
import uk.gov.companieshouse.web.accounts.annotation.NextController;
import uk.gov.companieshouse.web.accounts.annotation.PreviousController;
import uk.gov.companieshouse.web.accounts.api.ApiClientService;
import uk.gov.companieshouse.web.accounts.controller.BaseController;
import uk.gov.companieshouse.web.accounts.controller.ConditionalController;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.loanstodirectors.AddOrRemoveLoans;
import uk.gov.companieshouse.web.accounts.model.loanstodirectors.LoanToAdd;
import uk.gov.companieshouse.web.accounts.model.state.CompanyAccountsDataState;
import uk.gov.companieshouse.web.accounts.service.smallfull.LoanService;
import uk.gov.companieshouse.web.accounts.service.smallfull.SmallFullService;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@NextController(OffBalanceSheetArrangementsQuestionController.class)
@PreviousController(LoansToDirectorsQuestionController.class)
@RequestMapping("/company/{companyNumber}/transaction/{transactionId}/company-accounts/{companyAccountsId}/small-full/note/add-or-remove-loans")
public class AddOrRemoveLoansController extends BaseController implements ConditionalController {

    @Autowired
    private LoanService loanService;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private SmallFullService smallFullService;

    @Autowired
    private ApiClientService apiClientService;

    private static final UriTemplate URI =
            new UriTemplate("/company/{companyNumber}/transaction/{transactionId}/company-accounts/{companyAccountsId}/small-full/note/add-or-remove-loans");

    private static final String ADD_OR_REMOVE_LOANS = "addOrRemoveLoans";

    private static final String COMPANY_NUMBER = "companyNumber";

    private static final String TRANSACTION_ID = "transactionId";

    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";

    @GetMapping
    public String getAddOrRemoveLoans(@PathVariable String companyNumber,
                                      @PathVariable String transactionId,
                                      @PathVariable String companyAccountsId,
                                      Model model) {

        addBackPageAttributeToModel(model, companyNumber, transactionId, companyAccountsId);

        AddOrRemoveLoans addOrRemoveLoans = new AddOrRemoveLoans();

        ApiClient apiClient = apiClientService.getApiClient();
        try {
            SmallFullApi smallFullApi = smallFullService.getSmallFullAccounts(apiClient, transactionId, companyAccountsId);

            addOrRemoveLoans.setExistingLoans(
                    loanService.getAllLoans(transactionId, companyAccountsId));

            addOrRemoveLoans.setNextAccount(smallFullApi.getNextAccounts());

        } catch (ServiceException e) {

            LOGGER.errorRequest(request, e.getMessage(), e);
            return ERROR_VIEW;
        }

        model.addAttribute(ADD_OR_REMOVE_LOANS, addOrRemoveLoans);
        model.addAttribute(COMPANY_NUMBER, companyNumber);
        model.addAttribute(TRANSACTION_ID, transactionId);
        model.addAttribute(COMPANY_ACCOUNTS_ID, companyAccountsId);

        return getTemplateName();
    }

    @PostMapping(params = "submit")
    public String submitAddOrRemoveLoans(@PathVariable String companyNumber,
                                         @PathVariable String transactionId,
                                         @PathVariable String companyAccountsId,
                                         @ModelAttribute(ADD_OR_REMOVE_LOANS) AddOrRemoveLoans addOrRemoveLoans,
                                         BindingResult bindingResult
    ) {

        try {
            List<ValidationError> validationErrors = loanService.submitAddOrRemoveLoans(transactionId, companyAccountsId, addOrRemoveLoans);

            if(!validationErrors.isEmpty()) {
                bindValidationErrors(bindingResult, validationErrors);
                return getTemplateName();
            }

        } catch (ServiceException e) {

            LOGGER.errorRequest(request, e.getMessage(), e);
            return ERROR_VIEW;
        }

        return navigatorService
                .getNextControllerRedirect(this.getClass(), companyNumber, transactionId,
                        companyAccountsId);
    }

    @PostMapping(params = "add")
    public String addLoan(@PathVariable String companyNumber,
                              @PathVariable String transactionId,
                              @PathVariable String companyAccountsId,
                              @ModelAttribute(ADD_OR_REMOVE_LOANS) AddOrRemoveLoans addOrRemoveLoans,
                              BindingResult bindingResult,
                              Model model) {

        addBackPageAttributeToModel(model, companyNumber, transactionId, companyAccountsId);

        if (bindingResult.hasErrors()) {
            return getTemplateName();
        }

        try {

            List<ValidationError> validationErrors = loanService.createLoan(transactionId, companyAccountsId, addOrRemoveLoans.getLoanToAdd());

            if(!validationErrors.isEmpty()) {
                bindValidationErrors(bindingResult, validationErrors);
                return getTemplateName();
            }

        } catch (ServiceException e) {

            LOGGER.errorRequest(request, e.getMessage(), e);
            return ERROR_VIEW;
        }

        return UrlBasedViewResolver.REDIRECT_URL_PREFIX +
                URI.expand(companyNumber, transactionId, companyAccountsId).toString();
    }

    @GetMapping("remove/{loanId}")
    public String deleteLoan(@PathVariable String companyNumber,
                             @PathVariable String transactionId,
                             @PathVariable String companyAccountsId,
                             @PathVariable String loanId,
                             Model model) {

        addBackPageAttributeToModel(model, companyNumber, transactionId, companyAccountsId);

        try {

            loanService.deleteLoan(transactionId, companyAccountsId, loanId);

        } catch (ServiceException e) {

            LOGGER.errorRequest(request, e.getMessage(), e);
            return ERROR_VIEW;
        }

        return UrlBasedViewResolver.REDIRECT_URL_PREFIX +
                URI.expand(companyNumber, transactionId, companyAccountsId).toString();
    }

    @Override
    protected String getTemplateName() {
        return "smallfull/addOrRemoveLoans";
    }

    @Override
    public boolean willRender(String companyNumber, String transactionId, String companyAccountsId) throws ServiceException {

        CompanyAccountsDataState dataState = getStateFromRequest(request);
        return BooleanUtils.isTrue(dataState.getHasIncludedLoansToDirectors());
    }
}
