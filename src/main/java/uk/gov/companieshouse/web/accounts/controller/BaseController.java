package uk.gov.companieshouse.web.accounts.controller;

import org.springframework.ui.Model;
import uk.gov.companieshouse.logging.Logger;
import uk.gov.companieshouse.logging.LoggerFactory;
import uk.gov.companieshouse.web.accounts.CompanyAccountsWebApplication;
import uk.gov.companieshouse.web.accounts.util.Navigator;

public abstract class BaseController {

    protected static final Logger LOGGER = LoggerFactory
            .getLogger(CompanyAccountsWebApplication.APPLICATION_NAME_SPACE);

    protected static final String ERROR_VIEW = "error";

    protected BaseController() { }

    protected void addBackPageAttributeToModel(Model model, String... pathVars) {

        model.addAttribute("backPage", Navigator.getPreviousControllerPath(this.getClass(), pathVars));
    }
}
