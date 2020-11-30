package uk.gov.companieshouse.web.accounts.controller.smallfull;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
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
import uk.gov.companieshouse.api.model.accounts.smallfull.relatedpartytransactions.RelatedPartyTransactionsApi;
import uk.gov.companieshouse.web.accounts.annotation.NextController;
import uk.gov.companieshouse.web.accounts.annotation.PreviousController;
import uk.gov.companieshouse.web.accounts.api.ApiClientService;
import uk.gov.companieshouse.web.accounts.controller.BaseController;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.relatedpartytransactions.RelatedPartyTransactionsQuestion;
import uk.gov.companieshouse.web.accounts.model.state.CompanyAccountsDataState;
import uk.gov.companieshouse.web.accounts.service.smallfull.RelatedPartyTransactionsService;

@Controller
@PreviousController(LoansToDirectorsAdditionalInfoController.class)
@NextController(AddOrRemoveRptTransactionsController.class)
@RequestMapping("/company/{companyNumber}/transaction/{transactionId}/company-accounts/{companyAccountsId}/small-full/notes/related-party-transactions-question")
public class RelatedPartyTransactionsQuestionController extends BaseController {

    private static final String RELATED_PARTY_TRANSACTIONS_QUESTION = "relatedPartyTransactionsQuestion";

    @Autowired
    private RelatedPartyTransactionsService relatedPartyTransactionsService;

    @Autowired
    private ApiClientService apiClientService;

    @GetMapping
    public String getRelatedPartyTransactionsQuestion(Model model,
                                             @PathVariable String companyNumber,
                                             @PathVariable String transactionId,
                                             @PathVariable String companyAccountsId,
                                             HttpServletRequest request) {

        RelatedPartyTransactionsQuestion relatedPartyTransactionQuestion = new RelatedPartyTransactionsQuestion();

        ApiClient apiClient = apiClientService.getApiClient();
        try {
            RelatedPartyTransactionsApi relatedPartyTransactionsApi = relatedPartyTransactionsService.getRelatedPartyTransactions(apiClient, transactionId, companyAccountsId);
            if (relatedPartyTransactionsApi != null && relatedPartyTransactionsApi.getTransactions() != null && !relatedPartyTransactionsApi.getTransactions().isEmpty()) {
                relatedPartyTransactionQuestion.setHasIncludedRelatedPartyTransactions(true);
            } else {
                setIsRelatedPartyTransactionsIncluded(request, relatedPartyTransactionQuestion);
            }
        } catch (ServiceException e) {
            LOGGER.errorRequest(request, e.getMessage(), e);
            return ERROR_VIEW;
        }

        addBackPageAttributeToModel(model, companyNumber, transactionId, companyAccountsId);
        model.addAttribute(RELATED_PARTY_TRANSACTIONS_QUESTION, relatedPartyTransactionQuestion);

        return getTemplateName();
    }

    @PostMapping
    public String submitRelatedPartyTransactionsQuestion(@PathVariable String companyNumber,
                                                @PathVariable String transactionId,
                                                @PathVariable String companyAccountsId,
                                                @ModelAttribute(RELATED_PARTY_TRANSACTIONS_QUESTION) @Valid RelatedPartyTransactionsQuestion relatedPartyTransactionsQuestion,
                                                BindingResult bindingResult,
                                                Model model,
                                                HttpServletRequest request) {

        addBackPageAttributeToModel(model, companyNumber, transactionId, companyAccountsId);

        if (bindingResult.hasErrors()) {
            return getTemplateName();
        }

        ApiClient apiClient = apiClientService.getApiClient();

        try {
            RelatedPartyTransactionsApi relatedPartyTransactionsApi = relatedPartyTransactionsService.getRelatedPartyTransactions(apiClient, transactionId, companyAccountsId);

            if (Boolean.TRUE.equals(relatedPartyTransactionsQuestion.getHasIncludedRelatedPartyTransactions()) && relatedPartyTransactionsApi == null) {
                relatedPartyTransactionsService.createRelatedPartyTransactions(transactionId, companyAccountsId);
            } else {
                relatedPartyTransactionsService.deleteRelatedPartyTransactions(transactionId, companyAccountsId);
            }
        } catch (ServiceException e) {
            LOGGER.errorRequest(request, e.getMessage(), e);
            return ERROR_VIEW;
        }

        cacheIsRelatedPartyTransactionsIncluded(request, relatedPartyTransactionsQuestion);

        return navigatorService.getNextControllerRedirect(this.getClass(), companyNumber, transactionId, companyAccountsId);
    }

    @Override
    protected String getTemplateName() {
        return "smallfull/relatedPartyTransactionsQuestion";
    }

    private void setIsRelatedPartyTransactionsIncluded(HttpServletRequest request, RelatedPartyTransactionsQuestion relatedPartyTransactionsQuestion) {

        CompanyAccountsDataState companyAccountsDataState = getStateFromRequest(request);
        relatedPartyTransactionsQuestion.setHasIncludedRelatedPartyTransactions(companyAccountsDataState.getHasIncludedRelatedPartyTransactions());
    }

    private void  cacheIsRelatedPartyTransactionsIncluded(HttpServletRequest request, RelatedPartyTransactionsQuestion relatedPartyTransactionsQuestion) {

        CompanyAccountsDataState companyAccountsDataState = getStateFromRequest(request);
        companyAccountsDataState.setHasIncludedRelatedPartyTransactions(relatedPartyTransactionsQuestion.getHasIncludedRelatedPartyTransactions());

        updateStateOnRequest(request, companyAccountsDataState);
    }
}
