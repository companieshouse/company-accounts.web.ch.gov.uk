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
import uk.gov.companieshouse.web.accounts.annotation.NextController;
import uk.gov.companieshouse.web.accounts.annotation.PreviousController;
import uk.gov.companieshouse.web.accounts.controller.BaseController;
import uk.gov.companieshouse.web.accounts.controller.ConditionalController;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;

import uk.gov.companieshouse.web.accounts.model.smallfull.notes.intangible.IntangibleAssets;
import uk.gov.companieshouse.web.accounts.service.smallfull.IntangibleAssetsNoteService;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Controller
@NextController(FixedAssetsInvestmentsController.class)
@PreviousController(EmployeesController.class)
@RequestMapping("/company/{companyNumber}/transaction/{transactionId}/company-accounts/{companyAccountsId}/small-full/note/intangible-assets")
public class IntangibleAssetsNoteController extends BaseController implements ConditionalController {

    @Autowired
    private IntangibleAssetsNoteService intangibleAssetsNoteService;


    @Override
    protected String getTemplateName() { return "smallfull/intangibleAssetsNote"; }

    @GetMapping
    public String getIntangibleAssetsNote(@PathVariable String companyNumber,
        @PathVariable String transactionId,
        @PathVariable String companyAccountsId,
        Model model,
        HttpServletRequest request) {

        addBackPageAttributeToModel(model, companyNumber, transactionId, companyAccountsId);

        try {
            IntangibleAssets intangibleAssets = intangibleAssetsNoteService
                    .getIntangibleAssets(transactionId, companyAccountsId, companyNumber);

            model.addAttribute("intangibleAssets", intangibleAssets);

        } catch (ServiceException e) {
            LOGGER.errorRequest(request, e.getMessage(), e);
            return ERROR_VIEW;
        }

        return getTemplateName();
    }

    @PostMapping
    public String postIntangibleAssets(@PathVariable String companyNumber,
                                       @PathVariable String transactionId,
                                       @PathVariable String companyAccountsId,
                                       @ModelAttribute("intangibleAssets") @Valid IntangibleAssets intangibleAssets,
                                       BindingResult bindingResult,
                                       Model model,
                                       HttpServletRequest request) {
        addBackPageAttributeToModel(model, companyNumber, transactionId, companyAccountsId);

        if (bindingResult.hasErrors()) {
            return getTemplateName();
        }

        try {
            List<ValidationError> validationErrors = intangibleAssetsNoteService
                    .postIntangibleAssets(transactionId, companyAccountsId, intangibleAssets,
                            companyNumber);
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
    public boolean willRender(String companyNumber, String transactionId, String companyAccountsId) throws ServiceException {

        return false;
    }

}
