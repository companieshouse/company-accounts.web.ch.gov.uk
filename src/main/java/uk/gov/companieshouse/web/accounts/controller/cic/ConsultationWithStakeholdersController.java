package uk.gov.companieshouse.web.accounts.controller.cic;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import uk.gov.companieshouse.web.accounts.annotation.NextController;
import uk.gov.companieshouse.web.accounts.annotation.PreviousController;
import uk.gov.companieshouse.web.accounts.controller.BaseController;
import uk.gov.companieshouse.web.accounts.controller.ConditionalController;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.cic.statements.ConsultationWithStakeholders;
import uk.gov.companieshouse.web.accounts.model.state.CicStatements;
import uk.gov.companieshouse.web.accounts.model.state.CompanyAccountsDataState;
import uk.gov.companieshouse.web.accounts.service.cic.statements.ConsultationWithStakeholdersService;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

@Controller
@NextController(DirectorsRemunerationSelectionController.class)
@PreviousController(ConsultationWithStakeholdersSelectionController.class)
@RequestMapping("/company/{companyNumber}/transaction/{transactionId}/company-accounts/{companyAccountsId}/cic/consultation")
public class ConsultationWithStakeholdersController extends BaseController implements
    ConditionalController {

    @Autowired
    private ConsultationWithStakeholdersService consultationWithStakeholdersService;

    @Override
    protected String getTemplateName() {
        return "cic/consultationWithStakeholders";
    }

    @GetMapping
    public String getConsulationWithStakeholders(@PathVariable String companyNumber,
        @PathVariable String transactionId,
        @PathVariable String companyAccountsId,
        Model model,
        HttpServletRequest request) {

        addBackPageAttributeToModel(model, companyNumber, transactionId, companyAccountsId);

        try {
            model.addAttribute("consultationWithStakeholders", consultationWithStakeholdersService
                .getConsultationWithStakeholders(transactionId, companyAccountsId));
        } catch (ServiceException e) {

            LOGGER.errorRequest(request, e.getMessage(), e);
            return ERROR_VIEW;
        }

        return getTemplateName();
    }

    @PostMapping
    public String postConsulationWithStakeholders(@PathVariable String companyNumber,
        @PathVariable String transactionId,
        @PathVariable String companyAccountsId,
        @ModelAttribute("consultationWithStakeholders") @Valid ConsultationWithStakeholders consultationWithStakeholders,
        BindingResult bindingResult,
        Model model,
        HttpServletRequest request) {

        addBackPageAttributeToModel(model, companyNumber, transactionId, companyAccountsId);

        if (bindingResult.hasErrors()) {
            return getTemplateName();
        }

        try {
            List<ValidationError> validationErrors = consultationWithStakeholdersService
                .submitConsultationWithStakeholders(transactionId, companyAccountsId,
                    consultationWithStakeholders);

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

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                        .getRequest();

        CompanyAccountsDataState companyAccountsDataState = getStateFromRequest(request);
        return Optional.of(companyAccountsDataState)
                .map(CompanyAccountsDataState::getCicStatements)
                .map(CicStatements::getHasProvidedConsultationWithStakeholders)
                .orElse(false);
    }
}
