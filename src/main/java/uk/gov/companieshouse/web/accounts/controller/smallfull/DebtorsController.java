package uk.gov.companieshouse.web.accounts.controller.smallfull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import uk.gov.companieshouse.web.accounts.controller.BaseController;
import uk.gov.companieshouse.web.accounts.service.smallfull.DebtorsService;

@Controller
@RequestMapping("/company/{companyNumber}/transaction/{transactionId}/company-accounts/{companyAccountsId}/small-full/debtors")
public class DebtorsController extends BaseController {

    @Autowired
    private DebtorsService debtorsService;

    @GetMapping
    public String getDebtors() {
        return getTemplateName();
    }

    @Override
    protected String getTemplateName() {
        return "smallfull/debtors";
    }
}
