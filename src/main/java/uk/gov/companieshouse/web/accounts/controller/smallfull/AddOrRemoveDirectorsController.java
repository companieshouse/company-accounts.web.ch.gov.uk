package uk.gov.companieshouse.web.accounts.controller.smallfull;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang.BooleanUtils;
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
import uk.gov.companieshouse.api.error.ApiErrorResponseException;
import uk.gov.companieshouse.api.handler.exception.URIValidationException;
import uk.gov.companieshouse.web.accounts.annotation.NextController;
import uk.gov.companieshouse.web.accounts.annotation.PreviousController;
import uk.gov.companieshouse.web.accounts.controller.BaseController;
import uk.gov.companieshouse.web.accounts.controller.ConditionalController;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.directorsreport.AddOrRemoveDirectors;
import uk.gov.companieshouse.web.accounts.model.directorsreport.DirectorToAdd;
import uk.gov.companieshouse.web.accounts.model.state.CompanyAccountsDataState;
import uk.gov.companieshouse.web.accounts.service.smallfull.DirectorService;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

import java.util.List;
import uk.gov.companieshouse.web.accounts.service.smallfull.SecretaryService;
import uk.gov.companieshouse.web.accounts.validation.helper.ServiceExceptionHandler;

@Controller
@NextController(ProfitAndLossQuestionController.class)
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
    private ServiceExceptionHandler serviceExceptionHandler;

    private static final String RESOURCE_NAME = "secretaries";

    @Autowired
    private AddOrRemoveDirectors addOrRemoveDirectors;

    private static final UriTemplate URI =
            new UriTemplate("/company/{companyNumber}/transaction/{transactionId}/company-accounts/{companyAccountsId}/small-full/add-or-remove-directors");

    private static final String ADD_OR_REMOVE_DIRECTORS = "addOrRemoveDirectors";

    private static final String DIRECTOR_TO_ADD = "directorToAdd";

    private static final String COMPANY_NUMBER = "companyNumber";

    private static final String TRANSACTION_ID = "transactionId";

    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";

    @GetMapping
    public String getAddOrRemoveDirectors(@PathVariable String companyNumber,
                                          @PathVariable String transactionId,
                                          @PathVariable String companyAccountsId,
                                          Model model,
                                          HttpServletRequest request) {

        addBackPageAttributeToModel(model, companyNumber, transactionId, companyAccountsId);

        try {
            addOrRemoveDirectors.setExistingDirectors(
                    directorService.getAllDirectors(transactionId, companyAccountsId));

            addOrRemoveDirectors.setSecretary(
                    secretaryService.getSecretary(transactionId, companyAccountsId));

        } catch (ServiceException e) {

            LOGGER.errorRequest(request, e.getMessage(), e);
            return ERROR_VIEW;
        }

        model.addAttribute(ADD_OR_REMOVE_DIRECTORS, addOrRemoveDirectors);
        model.addAttribute(COMPANY_NUMBER, companyNumber);
        model.addAttribute(TRANSACTION_ID, transactionId);
        model.addAttribute(COMPANY_ACCOUNTS_ID, companyAccountsId);
        model.addAttribute(DIRECTOR_TO_ADD, new DirectorToAdd());

        return getTemplateName();
    }

    @GetMapping("/remove/{directorId}")
    public String removeDirector(@PathVariable String companyNumber,
                                 @PathVariable String transactionId,
                                 @PathVariable String companyAccountsId,
                                 @PathVariable String directorId,
                                 HttpServletRequest request) {

        try {
            directorService.deleteDirector(transactionId, companyAccountsId, directorId);

        } catch (ServiceException e) {

            LOGGER.errorRequest(request, e.getMessage(), e);
            return ERROR_VIEW;
        }

        return UrlBasedViewResolver.REDIRECT_URL_PREFIX +
                        URI.expand(companyNumber, transactionId, companyAccountsId).toString();
    }

    @PostMapping("/add/add-director")
    public String addDirector(@PathVariable String companyNumber,
                              @PathVariable String transactionId,
                              @PathVariable String companyAccountsId,
                              @ModelAttribute(DIRECTOR_TO_ADD) @Valid DirectorToAdd directorToAdd,
                              BindingResult bindingResult,
                              Model model,
                              HttpServletRequest request) {

        model.addAttribute(ADD_OR_REMOVE_DIRECTORS, addOrRemoveDirectors);

        if (bindingResult.hasErrors()) {
            return getTemplateName();
        }

        addBackPageAttributeToModel(model, companyNumber, transactionId, companyAccountsId);

        model.addAttribute(DIRECTOR_TO_ADD, directorToAdd);

        try {

            List<ValidationError> validationErrors = directorService.createDirector(transactionId, companyAccountsId, directorToAdd);

            if (!validationErrors.isEmpty()) {
                bindValidationErrors(bindingResult, validationErrors);
                return getTemplateName();
            }

            addOrRemoveDirectors.setDirectorToAdd(directorToAdd);

        } catch (ServiceException e) {

            LOGGER.errorRequest(request, e.getMessage(), e);
            return ERROR_VIEW;

        }

        return UrlBasedViewResolver.REDIRECT_URL_PREFIX +
                URI.expand(companyNumber, transactionId, companyAccountsId).toString();
    }

    @PostMapping
    public String submitAddOrRemoveDirectors(@PathVariable String companyNumber,
                                             @PathVariable String transactionId,
                                             @PathVariable String companyAccountsId,
                                             AddOrRemoveDirectors addOrRemoveDirectors) {


        try {
                if (StringUtils.isNotBlank(addOrRemoveDirectors.getSecretary())) {

                    secretaryService.submitSecretary(transactionId, companyAccountsId, addOrRemoveDirectors);
                } else {

                    secretaryService.deleteSecretary(transactionId, companyAccountsId);
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

        CompanyAccountsDataState companyAccountsDataState = getStateFromRequest(request);
        return BooleanUtils.isTrue(companyAccountsDataState.getHasIncludedDirectorsReport());
    }
}
