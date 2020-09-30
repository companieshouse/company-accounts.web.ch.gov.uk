package uk.gov.companieshouse.web.accounts.controller.smallfull;

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
import uk.gov.companieshouse.web.accounts.enumeration.NoteType;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.financialcommitments.FinancialCommitments;
import uk.gov.companieshouse.web.accounts.model.state.CompanyAccountsDataState;
import uk.gov.companieshouse.web.accounts.service.NoteService;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@NextController(EmployeesController.class)
@PreviousController(FinancialCommitmentsQuestionController.class)
@RequestMapping("/company/{companyNumber}/transaction/{transactionId}/company-accounts/{companyAccountsId}/small-full/financial-commitments")
public class FinancialCommitmentsController extends BaseController implements ConditionalController {

    private static final String FINANCIAL_COMMITMENTS = "financialCommitments";

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private NoteService<FinancialCommitments> noteService;

    @GetMapping
    public String getFinancialCommitments(@PathVariable String companyNumber,
                                                 @PathVariable String transactionId,
                                                 @PathVariable String companyAccountsId,
                                                 Model model) {

        addBackPageAttributeToModel(model, companyNumber, transactionId, companyAccountsId);

        try {
            model.addAttribute(FINANCIAL_COMMITMENTS,
                    noteService.get(transactionId, companyAccountsId, NoteType.SMALL_FULL_FINANCIAL_COMMITMENTS));

        } catch (ServiceException e) {

            LOGGER.errorRequest(request, e);
            return ERROR_VIEW;
        }

        return getTemplateName();

    }

    @PostMapping
    public String submitFinancialCommitments(@PathVariable String companyNumber,
                                                    @PathVariable String transactionId,
                                                    @PathVariable String companyAccountsId,
                                                   FinancialCommitments financialCommitments,
                                                   Model model) {

        addBackPageAttributeToModel(model, companyNumber, transactionId, companyAccountsId);

        try {
            List<ValidationError> validationErrors =
                    noteService.submit(transactionId, companyAccountsId, financialCommitments, NoteType.SMALL_FULL_FINANCIAL_COMMITMENTS);

        } catch (ServiceException e) {

            LOGGER.errorRequest(request, e.getMessage(), e);
            return ERROR_VIEW;
        }

        return navigatorService.getNextControllerRedirect(this.getClass(), companyNumber, transactionId, companyAccountsId);
    }

    @Override
    protected String getTemplateName() {
        return "smallfull/financialCommitments";
    }

    @Override
    public boolean willRender(String companyNumber, String transactionId, String companyAccountsId) throws ServiceException {

        CompanyAccountsDataState companyAccountsDataState = getStateFromRequest(request);
        return BooleanUtils.isTrue(companyAccountsDataState.getHasIncludedFinancialCommitments());
    }
}
