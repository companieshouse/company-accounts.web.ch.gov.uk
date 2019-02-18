package uk.gov.companieshouse.web.accounts.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/company/{companyNumber}/test/2")
public class TestClass2 extends BaseController {

    @GetMapping
    public String get(@PathVariable String companyNumber) {

        return "2";
    }

    @Override
    public String getTemplateName() {

        return "2";
    }
}
