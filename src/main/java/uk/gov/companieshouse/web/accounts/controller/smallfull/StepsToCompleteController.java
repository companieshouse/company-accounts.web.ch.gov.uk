package uk.gov.companieshouse.web.accounts.controller.smallfull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import uk.gov.companieshouse.api.error.ApiErrorResponseException;
import uk.gov.companieshouse.api.model.transaction.Transaction;
import uk.gov.companieshouse.web.accounts.controller.BaseController;
import uk.gov.companieshouse.web.accounts.service.transaction.TransactionService;

@Controller
@RequestMapping(value = "/company/{companyNumber}/small-full/steps-to-complete")
public class StepsToCompleteController extends BaseController {

    private static final String TEMPLATE = "smallfull/stepsToComplete";

    @Autowired
    private TransactionService transactionService;

    @GetMapping
    public String getStepsToComplete() {

        return TEMPLATE;
    }

    @PostMapping
    public String postStepsToComplete(@PathVariable String companyNumber) {

        try {
            String transaction = transactionService.createTransaction(companyNumber);
        } catch (ApiErrorResponseException e) {
            // TODO: handle ApiErrorResponseExceptions (SFA-594)
            LOGGER.error(e);
        }

        // TODO: perform a redirect when navigation is implemented
        return TEMPLATE;
    }
}
