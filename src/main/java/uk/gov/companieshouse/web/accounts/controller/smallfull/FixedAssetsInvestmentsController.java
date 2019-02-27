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
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.fixedassetsinvestments.FixedAssetsInvestments;
import uk.gov.companieshouse.web.accounts.service.smallfull.FixedAssetsInvestmentsService;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Controller
@NextController(ReviewController.class)
@PreviousController(StocksController.class)
@RequestMapping("/company/{companyNumber}/transaction/{transactionId}/company-accounts/{companyAccountsId}/small-full/fixed-assets-investments")
public class FixedAssetsInvestmentsController extends BaseController implements ConditionalController {
    
    @Autowired
    private FixedAssetsInvestmentsService fixedAssetsInvestmentsService;

    @Override
    protected String getTemplateName() {
        return "smallfull/fixedAssetsInvestments";
    }

    @GetMapping
    public String getFixedAssetsInvestments(@PathVariable String companyNumber,
                            @PathVariable String transactionId,
                            @PathVariable String companyAccountsId,
                            Model model, HttpServletRequest request) {

        addBackPageAttributeToModel(model, companyNumber, transactionId, companyAccountsId);

        try {
            FixedAssetsInvestments fixedAssetsInvestments = fixedAssetsInvestmentsService.getFixedAssetsInvestments(transactionId, companyAccountsId, companyNumber);
            model.addAttribute("fixedAssetsInvestments", fixedAssetsInvestments);
        } catch (ServiceException se) {
            LOGGER.errorRequest(request, se.getMessage(), se);
            return ERROR_VIEW;
        }

        return getTemplateName();
    }

    @PostMapping
    public String postFixedAssetsInvestments(@PathVariable String companyNumber,
                              @PathVariable String transactionId,
                              @PathVariable String companyAccountsId,
                              @ModelAttribute("fixedAssetsInvestments") @Valid FixedAssetsInvestments fixedAssetsInvestments,
                              BindingResult bindingResult,
                              Model model,
                              HttpServletRequest request) {

        addBackPageAttributeToModel(model, companyNumber, transactionId, companyAccountsId);

        try {
            List<ValidationError> validationErrors =
                fixedAssetsInvestmentsService.submitFixedAssetsInvestments(transactionId, companyAccountsId, fixedAssetsInvestments, companyNumber);

            if (! validationErrors.isEmpty()) {
                bindValidationErrors(bindingResult, validationErrors);
                return getTemplateName();
            }
        } catch (ServiceException e) {
            LOGGER.errorRequest(request, e.getMessage(), e);
            return ERROR_VIEW;
        }

        return navigatorService.getNextControllerRedirect(
            this.getClass(), companyNumber, transactionId, companyAccountsId);
    }

    @Override
    public boolean willRender(String companyNumber, String transactionId, String companyAccountsId)
            throws ServiceException {

        return true;
    }
}
