package uk.gov.companieshouse.web.accounts.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import uk.gov.companieshouse.web.accounts.annotation.NextController;

@Controller
@NextController({TestClass1.class, TestClass2.class, TestClass3.class})
@RequestMapping("/company/{companyNumber}/test")
public class TestClass extends BaseController {

    @GetMapping
    public String get(@PathVariable String companyNumber) {

        return "test";
    }

    @PostMapping
    public String post(@PathVariable String companyNumber) {

        return navigatorService.getNextControllerRedirect(this.getClass(), companyNumber, "A", "B");
    }

    @Override
    protected String getTemplateName() {
        return "test";
    }
}
