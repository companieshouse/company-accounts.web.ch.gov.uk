package uk.gov.companieshouse.web.accounts.controller.smallfull;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import uk.gov.companieshouse.web.accounts.annotation.NextController;
import uk.gov.companieshouse.web.accounts.annotation.PreviousController;
import uk.gov.companieshouse.web.accounts.controller.BaseController;
import uk.gov.companieshouse.web.accounts.controller.ConditionalController;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.creditorsafteroneyear.CreditorsAfterOneYear;
import uk.gov.companieshouse.web.accounts.service.smallfull.CreditorsAfterOneYearService;

@Controller
@NextController(ReviewController.class)
@PreviousController(DebtorsController.class)
@RequestMapping("/company/{companyNumber}/transaction/{transactionId}/company-accounts" +
        "/{companyAccountsId}/small-full/creditors-after-more-than-one-year")
public class CreditorsAfterOneYearController extends BaseController implements
        ConditionalController {

    @Autowired
    private CreditorsAfterOneYearService creditorsAfterOneYearService;

    @Override
    protected String getTemplateName() {
        return "smallfull/creditorsAfterOneYear";
    }

    @GetMapping
    public String getCreditorsAfterOneYear(@PathVariable String companyNumber,
            @PathVariable String transactionId, @PathVariable String companyAccountsId, Model model,
            HttpServletRequest request) {

        addBackPageAttributeToModel(model, companyNumber, transactionId, companyAccountsId);

        try {
            CreditorsAfterOneYear creditorsAfterOneYear =
                    creditorsAfterOneYearService.getCreditorsAfterOneYear(transactionId,
                            companyAccountsId,
                            companyNumber);

            model.addAttribute("creditorsAfterOneYear", creditorsAfterOneYear);
        } catch (ServiceException e) {
            LOGGER.errorRequest(request, e.getMessage(), e);
            return ERROR_VIEW;
        }
        return getTemplateName();
    }

    @Override
    public boolean willRender(String companyNumber, String transactionId,
            String companyAccountsId) throws ServiceException {
        return true;
    }
}
