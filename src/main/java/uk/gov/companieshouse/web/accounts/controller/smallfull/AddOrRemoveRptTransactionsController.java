package uk.gov.companieshouse.web.accounts.controller.smallfull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.model.accounts.smallfull.SmallFullApi;
import uk.gov.companieshouse.web.accounts.annotation.NextController;
import uk.gov.companieshouse.web.accounts.annotation.PreviousController;
import uk.gov.companieshouse.web.accounts.api.ApiClientService;
import uk.gov.companieshouse.web.accounts.controller.BaseController;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.relatedpartytransactions.AddOrRemoveRptTransactions;
import uk.gov.companieshouse.web.accounts.service.smallfull.RptTransactionService;
import uk.gov.companieshouse.web.accounts.service.smallfull.SmallFullService;

import javax.servlet.http.HttpServletRequest;

@Controller
@NextController(OffBalanceSheetArrangementsQuestionController.class)
@PreviousController(RelatedPartyTransactionsQuestionController.class)
@RequestMapping("/company/{companyNumber}/transaction/{transactionId}/company-accounts/{companyAccountsId}/small-full/note/add-or-remove-transactions")
public class AddOrRemoveRptTransactionsController extends BaseController {

    private static final String ADD_OR_REMOVE_RPT_TRANSACTIONS = "addOrRemoveTransactions";

    private static final String COMPANY_NUMBER = "companyNumber";

    private static final String TRANSACTION_ID = "transactionId";

    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";

    @Autowired
    private RptTransactionService rptTransactionService;

    @Autowired
    private SmallFullService smallFullService;

    @Autowired
    private ApiClientService apiClientService;

    @GetMapping
    public String getAddOrRemoveRptTransactions(@PathVariable String companyNumber,
                                                @PathVariable String transactionId,
                                                @PathVariable String companyAccountsId,
                                                Model model,
                                                HttpServletRequest request) {

        addBackPageAttributeToModel(model, companyNumber, transactionId, companyAccountsId);

        AddOrRemoveRptTransactions addOrRemoveRptTransactions = new AddOrRemoveRptTransactions();

        ApiClient apiClient = apiClientService.getApiClient();

        try {

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

        return getTemplateName();
    }

    @PostMapping(params = "submit")
    public String submitAddOrRemoveRptTransactions(@PathVariable String companyNumber,
                                         @PathVariable String transactionId,
                                         @PathVariable String companyAccountsId) {

        return navigatorService
                .getNextControllerRedirect(this.getClass(), companyNumber, transactionId,
                        companyAccountsId);
    }

    @Override
    protected String getTemplateName() {
        return "smallfull/addOrRemoveTransactions";
    }
}