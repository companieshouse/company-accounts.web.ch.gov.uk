package uk.gov.companieshouse.web.accounts.controller.smallfull;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import uk.gov.companieshouse.web.accounts.controller.BaseController;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.service.transaction.TransactionService;

@Controller
@RequestMapping("/company/{companyNumber}/transaction/{transactionId}/company-accounts/{companyAccountsId}/small-full/approval")
public class ApprovalController extends BaseController {

    private static final String TEMPLATE = "smallfull/approval";

    @Autowired
    private TransactionService transactionService;

    @GetMapping
    public String getApproval() {

        return TEMPLATE;
    }

    @PostMapping
    public String postApproval(@PathVariable String companyNumber,
                               @PathVariable String transactionId,
                               @PathVariable String companyAccountsId,
                               HttpServletRequest request) {

        try {
            transactionService.closeTransaction(transactionId);
        } catch (ServiceException e) {

            LOGGER.errorRequest(request, "Failed to close transaction", e);
            return ERROR_VIEW;
        }

        // TODO: Further implementation when navigation built
        return TEMPLATE;
    }
}
