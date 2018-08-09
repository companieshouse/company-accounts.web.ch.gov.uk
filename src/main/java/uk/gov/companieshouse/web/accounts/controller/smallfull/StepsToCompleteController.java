package uk.gov.companieshouse.web.accounts.controller.smallfull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import uk.gov.companieshouse.api.model.transaction.Transaction;
import uk.gov.companieshouse.web.accounts.controller.BaseController;
import uk.gov.companieshouse.web.accounts.service.transaction.TransactionService;
import uk.gov.companieshouse.web.accounts.util.RequestPathResolver;

@Controller
@RequestMapping(value = "/company/{companyNumber}/small-full/steps-to-complete")
public class StepsToCompleteController {

    private static final String TEMPLATE = "smallfull/stepsToComplete";

    @Autowired
    TransactionService transactionService;

    @GetMapping()
    public String getStepsToComplete() {

        return TEMPLATE;
    }

    @PostMapping()
    public String postStepsToComplete(@PathVariable String companyNumber) {

        Transaction transaction = transactionService.createTransaction(companyNumber);

        // TODO: perform a redirect when a navigation engine is implemented
        return TEMPLATE;
    }
}
