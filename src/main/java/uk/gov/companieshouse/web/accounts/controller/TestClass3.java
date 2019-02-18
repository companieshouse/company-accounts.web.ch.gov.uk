package uk.gov.companieshouse.web.accounts.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;

@Controller
@RequestMapping("/company/{companyNumber}/test/{a}/{b}/3")
public class TestClass3 extends BaseController implements BranchController, ConditionalController {

    @GetMapping
    public String get(@PathVariable String companyNumber, @PathVariable String a, @PathVariable String b) {

        return "3";
    }

    @Override
    public String getTemplateName() {

        return "3";
    }

    @Override
    public boolean willRender() {

        return true;
    }

    @Override
    public boolean willRender(String a, String b, String c) throws ServiceException {

        return false;
    }
}
