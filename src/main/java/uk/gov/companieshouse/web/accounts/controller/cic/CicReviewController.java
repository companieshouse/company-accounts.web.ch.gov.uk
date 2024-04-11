package uk.gov.companieshouse.web.accounts.controller.cic;

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
import uk.gov.companieshouse.web.accounts.controller.BaseController;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.cic.CicReview;
import uk.gov.companieshouse.web.accounts.service.cic.CicReviewService;

@Controller
@NextController(CicApprovalController.class)
@PreviousController(TransferOfAssetsController.class)
@RequestMapping("/company/{companyNumber}/transaction/{transactionId}/company-accounts/{companyAccountsId}/cic/review")
public class CicReviewController extends BaseController {
    @Autowired
    private CicReviewService cicReviewService;

    @GetMapping
    public String getCicReviewPage(@PathVariable String companyNumber,
        @PathVariable String transactionId,
        @PathVariable String companyAccountsId,
        Model model,
        HttpServletRequest request) {
        addBackPageAttributeToModel(model, companyNumber, transactionId, companyAccountsId);

        try {
            CicReview cicReview = cicReviewService
                .getReview(transactionId, companyAccountsId);

            model.addAttribute("cicReview", cicReview);
            model.addAttribute("companyNumber", companyNumber);
            model.addAttribute("transactionId", transactionId);
            model.addAttribute("companyAccountsId", companyAccountsId);

        } catch (ServiceException e) {
            LOGGER.errorRequest(request, e.getMessage(), e);
            return ERROR_VIEW;
        }

        return getTemplateName();
    }

    @PostMapping
    public String postCicReviewPage(@PathVariable String companyNumber,
        @PathVariable String transactionId,
        @PathVariable String companyAccountsId,
        HttpServletRequest request) {
        try {
            cicReviewService.acceptStatements(transactionId, companyAccountsId);
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
        return "cic/cicReview";
    }
}
