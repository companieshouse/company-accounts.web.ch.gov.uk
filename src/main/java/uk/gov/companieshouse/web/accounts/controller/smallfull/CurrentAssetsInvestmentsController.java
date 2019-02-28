package uk.gov.companieshouse.web.accounts.controller.smallfull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import uk.gov.companieshouse.web.accounts.controller.BaseController;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.CurrentAssetsInvestments;
import uk.gov.companieshouse.web.accounts.service.smallfull.CurrentAssetsInvestmentsService;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/company/{companyNumber}/transaction/{transactionId}/company-accounts/{companyAccountsId}/small-full/current-assets-investments")
public class CurrentAssetsInvestmentsController extends BaseController {

    @Autowired
    CurrentAssetsInvestmentsService currentAssetsInvestmentsService;

    @Override
    protected String getTemplateName() {
        return "smallfull/currentAssetsInvestments";
    }

    @GetMapping
    public String getCurrentAssetsInvestments(@PathVariable String companyNumber,
            @PathVariable String transactionId, @PathVariable String companyAccountsId, Model model,
            HttpServletRequest request) {

        addBackPageAttributeToModel(model, companyNumber, transactionId, companyAccountsId);

        try {
            CurrentAssetsInvestments currentAssetsInvestments =
                currentAssetsInvestmentsService.getCurrentAssetsInvestments(transactionId,
                    companyAccountsId, companyNumber);

            model.addAttribute("currentAssetsInvestments", currentAssetsInvestments);

        } catch (ServiceException e) {
            LOGGER.errorRequest(request, e.getMessage(), e);
            return ERROR_VIEW;
        }

        return getTemplateName();
    }
}
