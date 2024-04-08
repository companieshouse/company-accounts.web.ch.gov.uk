package uk.gov.companieshouse.web.accounts.controller.smallfull;

import java.util.List;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
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
import uk.gov.companieshouse.web.accounts.annotation.NextController;
import uk.gov.companieshouse.web.accounts.annotation.PreviousController;
import uk.gov.companieshouse.web.accounts.controller.BaseController;
import uk.gov.companieshouse.web.accounts.controller.ConditionalController;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.loanstodirectors.LoansToDirectorsAdditionalInfo;
import uk.gov.companieshouse.web.accounts.model.state.CompanyAccountsDataState;
import uk.gov.companieshouse.web.accounts.service.smallfull.LoansToDirectorsAdditionalInfoService;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

@Controller
@PreviousController(LoansToDirectorsAdditionalInfoQuestionController.class)
@NextController(OffBalanceSheetArrangementsQuestionController.class)
@RequestMapping("/company/{companyNumber}/transaction/{transactionId}/company-accounts/{companyAccountsId}/small-full/notes/add-or-remove-loans/additional-information")
public class LoansToDirectorsAdditionalInfoController extends BaseController implements ConditionalController {

    private static final String LOANS_TO_DIRECTORS_ADDITIONAL_INFO = "loansToDirectorsAdditionalInfo";

    @Autowired
    private LoansToDirectorsAdditionalInfoService loansToDirectorsAdditionalInfoService;

    @Autowired
    private HttpServletRequest request;
    
    @Override
    protected String getTemplateName() {
        return "smallfull/loansToDirectorsAdditionalInfo";
    }

    @GetMapping
    public String getLoansToDirectorsAdditionalInfo(@PathVariable String companyNumber,
        @PathVariable String transactionId,
        @PathVariable String companyAccountsId,
        Model model,
        HttpServletRequest request) {

        addBackPageAttributeToModel(model, companyNumber, transactionId, companyAccountsId);

        try {
            LoansToDirectorsAdditionalInfo loanToDirectorsAdditionalInfo = loansToDirectorsAdditionalInfoService.getAdditionalInformation(transactionId, companyAccountsId);
            
            model.addAttribute(LOANS_TO_DIRECTORS_ADDITIONAL_INFO, loanToDirectorsAdditionalInfo);
        } catch (ServiceException e) {
            LOGGER.errorRequest(request, e.getMessage(), e);
            return ERROR_VIEW;
        }

        return getTemplateName();
    }

    @PostMapping
    public String postLoansToDirectorsAdditionalInfo(@PathVariable String companyNumber,
        @PathVariable String transactionId,
        @PathVariable String companyAccountsId,
        @ModelAttribute(LOANS_TO_DIRECTORS_ADDITIONAL_INFO) @Valid LoansToDirectorsAdditionalInfo loanToDirectorsAdditionalInfo,
        BindingResult bindingResult,
        Model model,
        HttpServletRequest request) {

        addBackPageAttributeToModel(model, companyNumber, transactionId, companyAccountsId);

        if (bindingResult.hasErrors()) {
            return getTemplateName();
        }

        try {
            LoansToDirectorsAdditionalInfo existingLoanToDirectorsAdditionalInfo = loansToDirectorsAdditionalInfoService.getAdditionalInformation(transactionId, companyAccountsId);

            List<ValidationError> validationErrors;
            if(existingLoanToDirectorsAdditionalInfo.getAdditionalInfoDetails() == null) {
                validationErrors = loansToDirectorsAdditionalInfoService.createAdditionalInformation(transactionId, companyAccountsId, loanToDirectorsAdditionalInfo);
            } else {
                validationErrors = loansToDirectorsAdditionalInfoService.updateAdditionalInformation(transactionId, companyAccountsId, loanToDirectorsAdditionalInfo);
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
    public boolean willRender(String companyNumber, String transactionId, String companyAccountsId)
            throws ServiceException {

        CompanyAccountsDataState companyAccountsDataState = getStateFromRequest(request);
        return BooleanUtils.isTrue(companyAccountsDataState.getHasIncludedLoansToDirectorsAdditionalInfo());
    }
}