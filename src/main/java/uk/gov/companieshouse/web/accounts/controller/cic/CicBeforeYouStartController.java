package uk.gov.companieshouse.web.accounts.controller.cic;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import uk.gov.companieshouse.web.accounts.annotation.NextController;
import uk.gov.companieshouse.web.accounts.controller.BaseController;

@Controller
@RequestMapping("/accounts/cic/before-you-start")
@NextController(CicSelectAccountTypeController.class)
public class CicBeforeYouStartController extends BaseController {
    @Override
    protected String getTemplateName() {
        return "cic/cicBeforeYouStart";
    }

    @GetMapping
    public String getCicBeforeYouStart() {
        return getTemplateName();
    }

    @PostMapping
    public String postCicbeforeYouStart() {
        return navigatorService.getNextControllerRedirect(this.getClass());
    }
}
