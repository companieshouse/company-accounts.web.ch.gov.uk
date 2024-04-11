package uk.gov.companieshouse.web.accounts.controller.smallfull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import uk.gov.companieshouse.logging.Logger;
import uk.gov.companieshouse.logging.LoggerFactory;
import uk.gov.companieshouse.web.accounts.CompanyAccountsWebApplication;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.service.smallfull.ResumeService;

@Controller
@RequestMapping({"/company/{companyNumber}/transaction/{transactionId}/company-accounts/{companyAccountsId}/small-full/resume",
                "/company/{companyNumber}/transaction/{transactionId}/company-accounts/{companyAccountsId}/resume"})
public class ResumeController {
    private static final Logger LOGGER = LoggerFactory
            .getLogger(CompanyAccountsWebApplication.APPLICATION_NAME_SPACE);

    @Autowired
    ResumeService resumeService;

    @GetMapping
    public String getResumeRedirect(@PathVariable String companyNumber,
                                    @PathVariable String transactionId,
                                    @PathVariable String companyAccountsId) {
        try {
            return resumeService.getResumeRedirect(companyNumber, transactionId, companyAccountsId);
        } catch (ServiceException e) {
            LOGGER.error(e.getMessage(), e);
            return "error";
        }
    }
}
