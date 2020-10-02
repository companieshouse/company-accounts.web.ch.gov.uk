package uk.gov.companieshouse.web.accounts.controller.smallfull;

import javax.servlet.http.HttpServletRequest;

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
import org.springframework.web.servlet.view.UrlBasedViewResolver;
import org.springframework.web.util.UriTemplate;
import uk.gov.companieshouse.api.model.accounts.smallfull.loanstodirectors.LoansToDirectorsApi;
import uk.gov.companieshouse.web.accounts.annotation.NextController;
import uk.gov.companieshouse.web.accounts.annotation.PreviousController;
import uk.gov.companieshouse.web.accounts.api.ApiClientService;
import uk.gov.companieshouse.web.accounts.controller.BaseController;
import uk.gov.companieshouse.web.accounts.controller.ConditionalController;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.directorsreport.AddOrRemoveDirectors;
import uk.gov.companieshouse.web.accounts.service.smallfull.DirectorService;
import uk.gov.companieshouse.web.accounts.service.smallfull.DirectorsReportService;
import uk.gov.companieshouse.web.accounts.service.smallfull.LoansToDirectorsService;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

import java.util.List;
import uk.gov.companieshouse.web.accounts.service.smallfull.SecretaryService;

@Controller
@NextController(PrincipalActivitiesSelectionController.class)
@PreviousController(DirectorsReportQuestionController.class)
@RequestMapping("/company/{companyNumber}/transaction/{transactionId}/company-accounts/{companyAccountsId}/small-full/add-or-remove-directors")
public class AddOrRemoveDirectorsController extends BaseController implements ConditionalController {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private DirectorService directorService;

    @Autowired
    private SecretaryService secretaryService;

    @Autowired
    private DirectorsReportService directorsReportService;

    @Autowired
    private ApiClientService apiClientService;

    @Autowired
    private LoansToDirectorsService loansToDirectorsService;

    private static final UriTemplate URI =
            new UriTemplate("/company/{companyNumber}/transaction/{transactionId}/company-accounts/{companyAccountsId}/small-full/add-or-remove-directors");

    private static final String ADD_OR_REMOVE_DIRECTORS = "addOrRemoveDirectors";

    private static final String COMPANY_NUMBER = "companyNumber";

    private static final String TRANSACTION_ID = "transactionId";

    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";

    @GetMapping
    public String getAddOrRemoveDirectors(@PathVariable String companyNumber,
                                          @PathVariable String transactionId,
                                          @PathVariable String companyAccountsId,
                                          Model model) {

        addBackPageAttributeToModel(model, companyNumber, transactionId, companyAccountsId);

        AddOrRemoveDirectors addOrRemoveDirectors = new AddOrRemoveDirectors();

        boolean displayLoansToDirectorsWarning = false;
        LoansToDirectorsApi loansToDirectorsApi;

        try {
            addOrRemoveDirectors.setExistingDirectors(
                    directorService.getAllDirectors(transactionId, companyAccountsId, false));

            addOrRemoveDirectors.setSecretary(
                    secretaryService.getSecretary(transactionId, companyAccountsId));

            loansToDirectorsApi = loansToDirectorsService.getLoansToDirectors(apiClientService.getApiClient(), transactionId, companyAccountsId);
            if (loansToDirectorsApi != null && loansToDirectorsApi.getLoans() != null) {
                displayLoansToDirectorsWarning = !loansToDirectorsApi.getLoans().isEmpty();
            }

        } catch (ServiceException e) {

            LOGGER.errorRequest(request, e.getMessage(), e);
            return ERROR_VIEW;
        }

        addOrRemoveDirectors.setDisplayLtdWarningBanner(displayLoansToDirectorsWarning);

        model.addAttribute(ADD_OR_REMOVE_DIRECTORS, addOrRemoveDirectors);
        model.addAttribute(COMPANY_NUMBER, companyNumber);
        model.addAttribute(TRANSACTION_ID, transactionId);
        model.addAttribute(COMPANY_ACCOUNTS_ID, companyAccountsId);

        return getTemplateName();
    }

    @GetMapping("/remove/{directorId}")
    public String removeDirector(@PathVariable String companyNumber,
                                 @PathVariable String transactionId,
                                 @PathVariable String companyAccountsId,
                                 @PathVariable String directorId) {

        try {
            directorService.deleteDirector(transactionId, companyAccountsId, directorId);

        } catch (ServiceException e) {

            LOGGER.errorRequest(request, e.getMessage(), e);
            return ERROR_VIEW;
        }

        return UrlBasedViewResolver.REDIRECT_URL_PREFIX +
                        URI.expand(companyNumber, transactionId, companyAccountsId).toString();
    }

    @PostMapping(params = "add")
    public String addDirector(@PathVariable String companyNumber,
                              @PathVariable String transactionId,
                              @PathVariable String companyAccountsId,
                              @ModelAttribute(ADD_OR_REMOVE_DIRECTORS) AddOrRemoveDirectors addOrRemoveDirectors,
                              BindingResult bindingResult,
                              Model model) {

        addBackPageAttributeToModel(model, companyNumber, transactionId, companyAccountsId);

        try {

            List<ValidationError> validationErrors = directorService.createDirector(transactionId, companyAccountsId, addOrRemoveDirectors.getDirectorToAdd());

            if (!validationErrors.isEmpty()) {
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

    @PostMapping(params = "submit")
    public String submitAddOrRemoveDirectors(@PathVariable String companyNumber,
                                             @PathVariable String transactionId,
                                             @PathVariable String companyAccountsId,
                                             @ModelAttribute(ADD_OR_REMOVE_DIRECTORS) AddOrRemoveDirectors addOrRemoveDirectors,
                                             BindingResult bindingResult,
                                             Model model) {

        addBackPageAttributeToModel(model, companyNumber, transactionId, companyAccountsId);

        try {
            List<ValidationError> validationErrors = directorService.submitAddOrRemoveDirectors(transactionId,
                    companyAccountsId, addOrRemoveDirectors);

            if (StringUtils.isNotBlank(addOrRemoveDirectors.getSecretary())) {

                validationErrors.addAll(secretaryService.submitSecretary(transactionId, companyAccountsId, addOrRemoveDirectors));

            } else {

                secretaryService.deleteSecretary(transactionId, companyAccountsId);
            }

            if (!validationErrors.isEmpty()) {
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

    @Override
    protected String getTemplateName() {
        return "smallfull/addOrRemoveDirectors";
    }

    @Override
    public boolean willRender(String companyNumber, String transactionId, String companyAccountsId)
            throws ServiceException {

        return directorsReportService.getDirectorsReport(apiClientService.getApiClient(), transactionId, companyAccountsId) != null;

    }
}
