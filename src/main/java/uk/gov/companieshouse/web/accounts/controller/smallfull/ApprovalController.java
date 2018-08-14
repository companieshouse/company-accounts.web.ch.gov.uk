package uk.gov.companieshouse.web.accounts.controller.smallfull;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/company/{companyNumber}/small-full/approval")
public class ApprovalController {

    private static final String TEMPLATE = "smallfull/approval";

    @GetMapping
    public String getSubmissionPage() {
        return TEMPLATE;
    }

    @PostMapping
    public String postSubmissionPage(@PathVariable String companyNumber) {

        // TODO: Further implementation when navigation built
        return TEMPLATE;
    }
}
