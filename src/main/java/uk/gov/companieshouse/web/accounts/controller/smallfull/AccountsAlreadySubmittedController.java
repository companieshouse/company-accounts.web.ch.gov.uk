package uk.gov.companieshouse.web.accounts.controller.smallfull;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.UrlBasedViewResolver;
import uk.gov.companieshouse.web.accounts.controller.BaseController;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/company/{companyNumber}/transaction/{transactionId}/company-accounts/{companyAccountsId}/small-full/approved-accounts")
public class AccountsAlreadySubmittedController extends BaseController {

    private static final String YOUR_FILINGS_PATH = "/user/transactions";

    @Override
    protected String getTemplateName() {
        return "smallfull/accountsAlreadySubmitted";
    }

    @GetMapping
    public String getApproval(@PathVariable String companyNumber,
                              @PathVariable String transactionId,
                              @PathVariable String companyAccountsId,
                              Model model) {

        return getTemplateName();
    }

    @PostMapping
    public String postApproval(@PathVariable String companyNumber,
                               @PathVariable String transactionId,
                               @PathVariable String companyAccountsId,
                               Model model,
                               HttpServletRequest request) {

        return UrlBasedViewResolver.REDIRECT_URL_PREFIX +
                YOUR_FILINGS_PATH;
    }
}