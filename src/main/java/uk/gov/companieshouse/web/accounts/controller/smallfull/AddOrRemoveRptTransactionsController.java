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
import uk.gov.companieshouse.web.accounts.annotation.PreviousController;
import uk.gov.companieshouse.web.accounts.api.ApiClientService;
import uk.gov.companieshouse.web.accounts.controller.BaseController;
import uk.gov.companieshouse.web.accounts.controller.ConditionalController;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.relatedpartytransactions.AddOrRemoveRptTransactions;
import uk.gov.companieshouse.web.accounts.service.company.CompanyService;
import uk.gov.companieshouse.web.accounts.service.smallfull.RelatedPartyTransactionsService;
import uk.gov.companieshouse.web.accounts.service.smallfull.RptTransactionService;
import uk.gov.companieshouse.web.accounts.service.smallfull.SmallFullService;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@NextController(OffBalanceSheetArrangementsQuestionController.class)
@PreviousController(RelatedPartyTransactionsQuestionController.class)
@RequestMapping("/company/{companyNumber}/transaction/{transactionId}/company-accounts/{companyAccountsId}/small-full/note/add-or-remove-transactions")
public class AddOrRemoveRptTransactionsController extends BaseController implements ConditionalController {

    private static final String ADD_OR_REMOVE_RPT_TRANSACTIONS = "addOrRemoveTransactions";

    private static final UriTemplate URI =
            new UriTemplate("/company/{companyNumber}/transaction/{transactionId}/company-accounts/{companyAccountsId}/small-full/note/add-or-remove-transactions");

    private static final String COMPANY_NUMBER = "companyNumber";

    private static final String TRANSACTION_ID = "transactionId";

    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";

    private static final String MULTI_YEAR_FILER = "multiYearFiler";

    @Autowired
    private RptTransactionService rptTransactionService;

    @Autowired
    private RelatedPartyTransactionsService relatedPartyTransactionsService;

    @Autowired
    private SmallFullService smallFullService;

    @Autowired
    private ApiClientService apiClientService;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private CompanyService companyService;

    @GetMapping
    public String getAddOrRemoveRptTransactions(@PathVariable String companyNumber,
                                                @PathVariable String transactionId,
                                                @PathVariable String companyAccountsId,
                                                Model model,
                                                HttpServletRequest request) {

        addBackPageAttributeToModel(model, companyNumber, transactionId, companyAccountsId);

        AddOrRemoveRptTransactions addOrRemoveRptTransactions = new AddOrRemoveRptTransactions();

        ApiClient apiClient = apiClientService.getApiClient();

        CompanyProfileApi companyProfile;

        try {

             companyProfile = companyService.getCompanyProfile(companyNumber);

            SmallFullApi smallFullApi = smallFullService.getSmallFullAccounts(apiClient, transactionId, companyAccountsId);

            addOrRemoveRptTransactions.setExistingRptTransactions(
                    rptTransactionService.getAllRptTransactions(transactionId, companyAccountsId));

            addOrRemoveRptTransactions.setNextAccount(smallFullApi.getNextAccounts());

        } catch (ServiceException e) {

            LOGGER.errorRequest(request, e.getMessage(), e);
            return ERROR_VIEW;
        }

        model.addAttribute(ADD_OR_REMOVE_RPT_TRANSACTIONS, addOrRemoveRptTransactions);
        model.addAttribute(COMPANY_NUMBER, companyNumber);
        model.addAttribute(TRANSACTION_ID, transactionId);
        model.addAttribute(COMPANY_ACCOUNTS_ID, companyAccountsId);
        model.addAttribute(MULTI_YEAR_FILER, companyService.isMultiYearFiler(companyProfile));

        return getTemplateName();
    }

    @PostMapping(params = "submit")
    public String submitAddOrRemoveRptTransactions(@PathVariable String companyNumber,
                                                   @PathVariable String transactionId,
                                                   @PathVariable String companyAccountsId,
                                                   @ModelAttribute(ADD_OR_REMOVE_RPT_TRANSACTIONS) AddOrRemoveRptTransactions addOrRemoveRptTransactions,
                                                   BindingResult bindingResult,
                                                   Model model) {

        addBackPageAttributeToModel(model, companyNumber, transactionId, companyAccountsId);

        try {

            List<ValidationError> validationErrors = rptTransactionService.submitAddOrRemoveRptTransactions(transactionId, companyAccountsId, addOrRemoveRptTransactions);

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
    public String addRptTransaction(@PathVariable String companyNumber,
                                    @PathVariable String transactionId,
                                    @PathVariable String companyAccountsId,
                                    @ModelAttribute(ADD_OR_REMOVE_RPT_TRANSACTIONS) AddOrRemoveRptTransactions addOrRemoveRptTransactions,
                                    BindingResult bindingResult,
                                    Model model) {

        addBackPageAttributeToModel(model, companyNumber, transactionId, companyAccountsId);

        if (bindingResult.hasErrors()) {
            return getTemplateName();
        }

        try {

            List<ValidationError> validationErrors = rptTransactionService.createRptTransaction(transactionId, companyAccountsId, addOrRemoveRptTransactions);

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

    @Override
    protected String getTemplateName() {
        return "smallfull/addOrRemoveTransactions";
    }

    @Override
    public boolean willRender(String companyNumber, String transactionId, String companyAccountsId) throws ServiceException {

        return relatedPartyTransactionsService.getRelatedPartyTransactions(apiClientService.getApiClient(), transactionId, companyAccountsId) != null;
    }
}
