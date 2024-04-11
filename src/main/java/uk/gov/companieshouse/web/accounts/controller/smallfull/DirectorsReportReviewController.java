package uk.gov.companieshouse.web.accounts.controller.smallfull;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import uk.gov.companieshouse.web.accounts.annotation.NextController;
import uk.gov.companieshouse.web.accounts.annotation.PreviousController;
import uk.gov.companieshouse.web.accounts.api.ApiClientService;
import uk.gov.companieshouse.web.accounts.controller.BaseController;
import uk.gov.companieshouse.web.accounts.controller.ConditionalController;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.service.smallfull.DirectorsReportReviewService;
import uk.gov.companieshouse.web.accounts.service.smallfull.DirectorsReportService;

@Controller
@NextController(DirectorsReportApprovalController.class)
@PreviousController(DirectorsReportAdditionalInformationController.class)
@RequestMapping("/company/{companyNumber}/transaction/{transactionId}/company-accounts/{companyAccountsId}/small-full/directors-report/review")
public class DirectorsReportReviewController extends BaseController implements ConditionalController {
    @Autowired
    private HttpServletRequest request;

    @Autowired
    private DirectorsReportService directorsReportService;

    @Autowired
    private ApiClientService apiClientService;

    @Autowired
    private DirectorsReportReviewService directorsReportReviewService;

    private static final String DIRECTORS_REPORT_REVIEW = "directorsReportReview";

    private static final String COMPANY_NUMBER = "companyNumber";

    private static final String TRANSACTION_ID = "transactionId";

    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";

    @GetMapping
    public String getDirectorsReportReview(@PathVariable String companyNumber,
                                           @PathVariable String transactionId,
                                           @PathVariable String companyAccountsId,
                                           Model model) {
        addBackPageAttributeToModel(model, companyNumber, transactionId, companyAccountsId);

        try {
            model.addAttribute(DIRECTORS_REPORT_REVIEW,
                    directorsReportReviewService.getReview(transactionId, companyAccountsId));
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
    public String submitDirectorsReportReview(@PathVariable String companyNumber,
                                              @PathVariable String transactionId,
                                              @PathVariable String companyAccountsId) {
        return navigatorService
                .getNextControllerRedirect(this.getClass(), companyNumber, transactionId, companyAccountsId);
    }

    @Override
    protected String getTemplateName() {
        return "smallfull/directorsReportReview";
    }

    @Override
    public boolean willRender(String companyNumber, String transactionId, String companyAccountsId)
            throws ServiceException {
        return directorsReportService.getDirectorsReport(apiClientService.getApiClient(), transactionId, companyAccountsId) != null;
    }
}
