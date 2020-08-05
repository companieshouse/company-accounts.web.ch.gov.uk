package uk.gov.companieshouse.web.accounts.controller.smallfull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
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
import uk.gov.companieshouse.web.accounts.annotation.NextController;
import uk.gov.companieshouse.web.accounts.annotation.PreviousController;
import uk.gov.companieshouse.web.accounts.controller.BaseController;
import uk.gov.companieshouse.web.accounts.controller.ConditionalController;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.directorsreport.Director;
import uk.gov.companieshouse.web.accounts.model.directorsreport.DirectorsReportApproval;
import uk.gov.companieshouse.web.accounts.model.state.CompanyAccountsDataState;
import uk.gov.companieshouse.web.accounts.service.smallfull.DirectorService;
import uk.gov.companieshouse.web.accounts.service.smallfull.DirectorsReportApprovalService;
import uk.gov.companieshouse.web.accounts.service.smallfull.SecretaryService;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

@Controller
@NextController(ProfitAndLossQuestionController.class)
@PreviousController(DirectorsReportReviewController.class)
@RequestMapping("/company/{companyNumber}/transaction/{transactionId}/company-accounts/{companyAccountsId}/small-full/directors-report/approval")
public class DirectorsReportApprovalController extends BaseController implements ConditionalController {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private DirectorsReportApprovalService directorsReportApprovalService;

    @Autowired
    private DirectorService directorService;

    @Autowired
    private SecretaryService secretaryService;

    private static final String DIRECTORS_REPORT_APPROVAL = "directorsReportApproval";

    private static final String COMPANY_NUMBER = "companyNumber";

    private static final String TRANSACTION_ID = "transactionId";

    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";

    @GetMapping
    public String getDirectorsReportApproval(@PathVariable String companyNumber,
                                             @PathVariable String transactionId,
                                             @PathVariable String companyAccountsId,
                                             Model model) {

        addBackPageAttributeToModel(model, companyNumber, transactionId, companyAccountsId);

        try {

            DirectorsReportApproval directorsReportApproval =
                    directorsReportApprovalService.getDirectorsReportApproval(transactionId, companyAccountsId);

            List<String> approverOptions = new ArrayList<>();

            for(Director director : directorService.getAllDirectors(transactionId, companyAccountsId)) {

                if (director.getResignationDate() == null) {
                    approverOptions.add(director.getName());
                } else if (director.getAppointmentDate() != null) {
                    if (director.getAppointmentDate().isAfter(director.getResignationDate())) {
                        approverOptions.add(director.getName());
                    }
                }
            }

            String secretary = secretaryService.getSecretary(transactionId, companyAccountsId);
            if (StringUtils.isNotBlank(secretary)) {
                approverOptions.add(secretary);
            }

            directorsReportApproval.setApproverOptions(approverOptions);
            if (approverOptions.size() == 1) {
                directorsReportApproval.setName(approverOptions.get(0));
            }

            model.addAttribute(DIRECTORS_REPORT_APPROVAL, directorsReportApproval);
            model.addAttribute(COMPANY_NUMBER, companyNumber);
            model.addAttribute(TRANSACTION_ID, transactionId);
            model.addAttribute(COMPANY_ACCOUNTS_ID, companyAccountsId);

            return getTemplateName();

        } catch (ServiceException e) {

            LOGGER.errorRequest(request, e.getMessage(), e);
            return ERROR_VIEW;
        }
    }

    @PostMapping
    public String submitDirectorsReportApproval(@PathVariable String companyNumber,
                                                @PathVariable String transactionId,
                                                @PathVariable String companyAccountsId,
                                                @Valid @ModelAttribute(DIRECTORS_REPORT_APPROVAL) DirectorsReportApproval directorsReportApproval,
                                                BindingResult bindingResult,
                                                Model model) {

        addBackPageAttributeToModel(model, companyNumber, transactionId, companyAccountsId);

        if (bindingResult.hasErrors()) {
            return getTemplateName();
        }

        try {
            List<ValidationError> validationErrors =
                    directorsReportApprovalService.submitDirectorsReportApproval(transactionId, companyAccountsId, directorsReportApproval);

            if (!validationErrors.isEmpty()) {
                bindValidationErrors(bindingResult, validationErrors);
                return getTemplateName();
            }

        } catch (ServiceException e) {

            LOGGER.errorRequest(request, e.getMessage(), e);
            return ERROR_VIEW;
        }

        return navigatorService.getNextControllerRedirect(this.getClass(), companyNumber, transactionId, companyAccountsId);
    }

    @Override
    protected String getTemplateName() {
        return "smallfull/directorsReportApproval";
    }

    @Override
    public boolean willRender(String companyNumber, String transactionId, String companyAccountsId)
            throws ServiceException {

        CompanyAccountsDataState companyAccountsDataState = getStateFromRequest(request);
        return BooleanUtils.isTrue(companyAccountsDataState.getHasIncludedDirectorsReport());
    }
}
