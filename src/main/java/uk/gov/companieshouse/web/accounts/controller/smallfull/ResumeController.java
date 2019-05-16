package uk.gov.companieshouse.web.accounts.controller.smallfull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import uk.gov.companieshouse.web.accounts.service.smallfull.ResumeService;

@Controller
@RequestMapping({"/company/{companyNumber}/transaction/{transactionId}/company-accounts/{companyAccountsId}/small-full/resume",
                "/company/{companyNumber}/transaction/{transactionId}/company-accounts/{companyAccountsId}/resume"})
public class ResumeController {

    @Autowired
    ResumeService resumeService;

    @GetMapping
    public String getResumeRedirect(@PathVariable String companyNumber,
                                    @PathVariable String transactionId,
                                    @PathVariable String companyAccountsId) {

        return resumeService.getResumeRedirect(companyNumber, transactionId, companyAccountsId);
    }
}
