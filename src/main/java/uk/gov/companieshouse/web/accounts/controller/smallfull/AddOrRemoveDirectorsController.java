package uk.gov.companieshouse.web.accounts.controller.smallfull;

import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import uk.gov.companieshouse.web.accounts.annotation.NextController;
import uk.gov.companieshouse.web.accounts.annotation.PreviousController;
import uk.gov.companieshouse.web.accounts.controller.BaseController;
import uk.gov.companieshouse.web.accounts.controller.ConditionalController;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.directorsreport.AddOrRemoveDirectors;
import uk.gov.companieshouse.web.accounts.model.state.CompanyAccountsDataState;
import uk.gov.companieshouse.web.accounts.service.smallfull.DirectorService;

@Controller
@NextController(ProfitAndLossQuestionController.class)
@PreviousController(DirectorsReportQuestionController.class)
@RequestMapping("/company/{companyNumber}/transaction/{transactionId}/company-accounts/{companyAccountsId}/small-full/add-or-remove-directors")
public class AddOrRemoveDirectorsController extends BaseController implements ConditionalController {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private DirectorService directorService;

    private static final String ADD_OR_REMOVE_DIRECTORS = "addOrRemoveDirectors";

    @GetMapping
    public String getAddOrRemoveDirectors(@PathVariable String companyNumber,
                                          @PathVariable String transactionId,
                                          @PathVariable String companyAccountsId,
                                          Model model,
                                          HttpServletRequest request) {

        addBackPageAttributeToModel(model, companyNumber, transactionId, companyAccountsId);

        AddOrRemoveDirectors addOrRemoveDirectors = new AddOrRemoveDirectors();

        try {
            addOrRemoveDirectors.setExistingDirectors(
                    directorService.getAllDirectors(transactionId, companyAccountsId));

        } catch (ServiceException e) {

            LOGGER.errorRequest(request, e.getMessage(), e);
            return ERROR_VIEW;
        }

        model.addAttribute(ADD_OR_REMOVE_DIRECTORS, addOrRemoveDirectors);

        return getTemplateName();
    }

    @PostMapping
    public String submitAddOrRemoveDirectors(@PathVariable String companyNumber,
                                             @PathVariable String transactionId,
                                             @PathVariable String companyAccountsId) {

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
