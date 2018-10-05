package uk.gov.companieshouse.web.accounts.controller.smallfull;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import uk.gov.companieshouse.web.accounts.annotation.NextController;
import uk.gov.companieshouse.web.accounts.annotation.PreviousController;
import uk.gov.companieshouse.web.accounts.controller.BaseController;
import uk.gov.companieshouse.web.accounts.util.Navigator;

@Controller
@NextController(ApprovalController.class)
@PreviousController(BalanceSheetController.class)
@RequestMapping("/company/{companyNumber}/transaction/{transactionId}/company-accounts/{companyAccountsId}/small-full/review")
public class ReviewController extends BaseController {

    @GetMapping
    public String getReviewPage(@PathVariable String companyNumber,
                                @PathVariable String transactionId,
                                @PathVariable String companyAccountsId,
                                Model model) {

        addBackPageAttributeToModel(model, companyNumber, transactionId, companyAccountsId);

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
