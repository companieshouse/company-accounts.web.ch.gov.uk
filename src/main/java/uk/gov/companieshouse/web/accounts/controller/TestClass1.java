package uk.gov.companieshouse.web.accounts.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/company/{companyNumber}/test/1")
public class TestClass1 extends BaseController implements BranchController {

    @GetMapping
    public String get(@PathVariable String companyNumber) {

        return "1";
    }

    @Override
    public String getTemplateName() {
        return "1";
    }

    @Override
    public boolean willRender() {

        return false;
    }
}
