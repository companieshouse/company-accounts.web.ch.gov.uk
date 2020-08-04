package uk.gov.companieshouse.web.accounts.controller.smallfull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import uk.gov.companieshouse.web.accounts.controller.BaseController;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.loanstodirectors.AddOrRemoveLoans;
import uk.gov.companieshouse.web.accounts.service.smallfull.LoanService;

import javax.servlet.http.HttpServletRequest;

@Controller
public class AddOrRemoveLoansController extends BaseController {

    @Autowired
    private LoanService loanService;

    @GetMapping
    public String getAddOrRemoveLoans(@PathVariable String companyNumber,
                                      @PathVariable String transactionId,
                                      @PathVariable String companyAccountsId,
                                      Model model,
                                      HttpServletRequest request) {

        addBackPageAttributeToModel(model, companyNumber, transactionId, companyAccountsId);

        AddOrRemoveLoans addOrRemoveLoans = new AddOrRemoveLoans();

        try {
            addOrRemoveLoans.setExistingLoans(
                    loanService.getAllLoans(transactionId, companyAccountsId));

        } catch (ServiceException e) {

            LOGGER.errorRequest(request, e.getMessage(), e);
            return ERROR_VIEW;
        }

        return getTemplateName();
    }

    @PostMapping
    public void submitAddOrRemoveLoans() {
    }

    @Override
    protected String getTemplateName() {
        return "addOrRemoveLoans";
    }
}
