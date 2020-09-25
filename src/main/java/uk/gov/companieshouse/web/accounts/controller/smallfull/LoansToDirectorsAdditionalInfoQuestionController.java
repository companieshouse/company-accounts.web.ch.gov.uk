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
import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.model.accounts.smallfull.loanstodirectors.LoansToDirectorsApi;
import uk.gov.companieshouse.web.accounts.annotation.NextController;
import uk.gov.companieshouse.web.accounts.annotation.PreviousController;
import uk.gov.companieshouse.web.accounts.api.ApiClientService;
import uk.gov.companieshouse.web.accounts.controller.BaseController;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.loanstodirectors.LoansToDirectorsAdditionalInfoQuestion;
import uk.gov.companieshouse.web.accounts.model.state.CompanyAccountsDataState;
import uk.gov.companieshouse.web.accounts.service.smallfull.LoansToDirectorsAdditionalInfoService;
import uk.gov.companieshouse.web.accounts.service.smallfull.LoansToDirectorsService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
@PreviousController(AddOrRemoveLoansController.class)
@NextController(LoansToDirectorsAdditionalInfoController.class)
@RequestMapping("/company/{companyNumber}/transaction/{transactionId}/company-accounts/{companyAccountsId}/small-full/notes/add-or-remove-loans/additional-information-question")
public class LoansToDirectorsAdditionalInfoQuestionController extends BaseController {

    private static final String LOANS_TO_DIRECTORS_ADDITIONAL_INFO_QUESTION = "loansToDirectorsAdditionalInfoQuestion";

    @Autowired
    private LoansToDirectorsService loansToDirectorsService;

    @Autowired
    private LoansToDirectorsAdditionalInfoService loansToDirectorsAdditionalInfoService;

    @Autowired
    private ApiClientService apiClientService;

    @Autowired
    private HttpServletRequest request;

    @GetMapping
    public String getLoansToDirectorsAdditionalInfoQuestion(Model model,
                                             @PathVariable String companyNumber,
                                             @PathVariable String transactionId,
                                             @PathVariable String companyAccountsId) {

        LoansToDirectorsAdditionalInfoQuestion loansToDirectorsAdditionalInfoQuestion = new LoansToDirectorsAdditionalInfoQuestion();

        ApiClient apiClient = apiClientService.getApiClient();
        try {
            LoansToDirectorsApi loansToDirectorsApi = loansToDirectorsService.getLoansToDirectors(apiClient, transactionId, companyAccountsId);
            if (loansToDirectorsApi != null && loansToDirectorsApi.getLinks().getAdditionalInformation() != null) {
                loansToDirectorsAdditionalInfoQuestion.setHasIncludedLoansToDirectorsAdditionalInfo(true);
            } else {
                setIsLoansToDirectorsAdditionalInfoIncluded(request, loansToDirectorsAdditionalInfoQuestion);
            }
        } catch (ServiceException e) {
            LOGGER.errorRequest(request, e.getMessage(), e);
            return ERROR_VIEW;
        }

        addBackPageAttributeToModel(model, companyNumber, transactionId, companyAccountsId);
        model.addAttribute(LOANS_TO_DIRECTORS_ADDITIONAL_INFO_QUESTION, loansToDirectorsAdditionalInfoQuestion);

        return getTemplateName();
    }

    @PostMapping
    public String submitLoansToDirectorsAdditionalInfoQuestion(@PathVariable String companyNumber,
                                                @PathVariable String transactionId,
                                                @PathVariable String companyAccountsId,
                                                @ModelAttribute(LOANS_TO_DIRECTORS_ADDITIONAL_INFO_QUESTION) @Valid LoansToDirectorsAdditionalInfoQuestion loansToDirectorsAdditionalInfoQuestionQuestion,
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

            if (loansToDirectorsAdditionalInfoQuestionQuestion.getHasIncludedLoansToDirectorsAdditionalInfo()) {
                if (loansToDirectorsApi == null) {
                    loansToDirectorsService.createLoansToDirectors(transactionId, companyAccountsId);
                }
            } else {
                if (loansToDirectorsApi != null) {
                    if (loansToDirectorsApi.getLoans() == null) {
                        loansToDirectorsService.deleteLoansToDirectors(transactionId, companyAccountsId);
                    } else {
                        if (loansToDirectorsApi.getLinks().getAdditionalInformation() != null) {
                            loansToDirectorsAdditionalInfoService.deleteAdditionalInformation(transactionId, companyAccountsId);
                        }
                    }
                }
            }

        } catch (ServiceException e) {
            LOGGER.errorRequest(request, e.getMessage(), e);
            return ERROR_VIEW;
        }

        cacheIsLoansToDirectorsIncluded(request, loansToDirectorsAdditionalInfoQuestionQuestion);

        return navigatorService.getNextControllerRedirect(this.getClass(), companyNumber, transactionId, companyAccountsId);
    }

    @Override
    protected String getTemplateName() {
        return "smallfull/loansToDirectorsAdditionalInfoQuestion";
    }

    private void setIsLoansToDirectorsAdditionalInfoIncluded(HttpServletRequest request, LoansToDirectorsAdditionalInfoQuestion loansToDirectorsAdditionalInfoQuestion) {

        CompanyAccountsDataState companyAccountsDataState = getStateFromRequest(request);
        loansToDirectorsAdditionalInfoQuestion.setHasIncludedLoansToDirectorsAdditionalInfo(companyAccountsDataState.getHasIncludedLoansToDirectorsAdditionalInfo());
    }

    private void  cacheIsLoansToDirectorsIncluded(HttpServletRequest request,
                                                  LoansToDirectorsAdditionalInfoQuestion loansToDirectorsAdditionalInfoQuestion) {

        CompanyAccountsDataState companyAccountsDataState = getStateFromRequest(request);
        companyAccountsDataState.setHasIncludedLoansToDirectorsAdditionalInfo(loansToDirectorsAdditionalInfoQuestion.getHasIncludedLoansToDirectorsAdditionalInfo());

        updateStateOnRequest(request, companyAccountsDataState);
    }
}
