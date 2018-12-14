package uk.gov.companieshouse.web.accounts.controller.smallfull;

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
import uk.gov.companieshouse.web.accounts.model.smallfull.Review;
import uk.gov.companieshouse.web.accounts.service.smallfull.ReviewService;
import uk.gov.companieshouse.web.accounts.util.Navigator;

import javax.servlet.http.HttpServletRequest;

@Controller
@NextController(ApprovalController.class)
@PreviousController(OtherAccountingPolicyController.class)
@RequestMapping("/company/{companyNumber}/transaction/{transactionId}/company-accounts/{companyAccountsId}/small-full/review")
public class ReviewController extends BaseController {

    @Autowired
    ReviewService reviewService;

    @GetMapping
    public String getReviewPage(@PathVariable String companyNumber,
                                @PathVariable String transactionId,
                                @PathVariable String companyAccountsId,
                                Model model,
                                HttpServletRequest request) {

        addBackPageAttributeToModel(model, companyNumber, transactionId, companyAccountsId);

        try {
            Review review = reviewService.getReview(transactionId, companyAccountsId, companyNumber);

            model.addAttribute("review", review);
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
    public String postReviewPage(@PathVariable String companyNumber,
                                 @PathVariable String transactionId,
                                 @PathVariable String companyAccountsId) {

        return Navigator.getNextControllerRedirect(this.getClass(), companyNumber, transactionId, companyAccountsId);
    }

    @Override
    protected String getTemplateName() {
        return "smallfull/review";
    }
}
