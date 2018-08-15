package uk.gov.companieshouse.web.accounts.controller.smallfull;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/company/{companyNumber}/transaction/{transactionId}/company-accounts/{companyAccountsId}/small-full/approval")
public class ApprovalController {

    private static final String TEMPLATE = "smallfull/approval";

    @GetMapping
    public String getApproval() {
        return TEMPLATE;
    }

    @PostMapping
    public String postApproval(@PathVariable String companyNumber,
                               @PathVariable String transactionId,
                               @PathVariable String companyAccountsId) {

        // TODO: Further implementation when navigation built
        return TEMPLATE;
    }
}
