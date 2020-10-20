package uk.gov.companieshouse.web.accounts.controller.smallfull;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.UrlBasedViewResolver;
import uk.gov.companieshouse.web.accounts.annotation.PreviousController;
import uk.gov.companieshouse.web.accounts.controller.BaseController;

import javax.servlet.http.HttpServletRequest;

@Controller
@PreviousController(ApprovalController.class)
@RequestMapping("/company/{companyNumber}/transaction/{transactionId}/company-accounts/{companyAccountsId}/small-full/approved-accounts")
public class PaymentDropoutController extends BaseController {

    private static final String YOUR_FILINGS_PATH = "/user/transactions";

    @Override
    protected String getTemplateName() {
        return "smallfull/paymentSessionDropout";
    }

    @GetMapping
    public String getApproval(@PathVariable String companyNumber,
                              @PathVariable String transactionId,
                              @PathVariable String companyAccountsId,
                              Model model) {

        addBackPageAttributeToModel(model, companyNumber, transactionId, companyAccountsId);

        return getTemplateName();
    }

    @PostMapping
    public String postApproval(@PathVariable String companyNumber,
                               @PathVariable String transactionId,
                               @PathVariable String companyAccountsId,
                               Model model,
                               HttpServletRequest request) {

        addBackPageAttributeToModel(model, companyNumber, transactionId, companyAccountsId);

        return UrlBasedViewResolver.REDIRECT_URL_PREFIX +
                YOUR_FILINGS_PATH;
    }
}