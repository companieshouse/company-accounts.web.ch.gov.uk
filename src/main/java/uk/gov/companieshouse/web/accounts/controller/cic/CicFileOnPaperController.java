package uk.gov.companieshouse.web.accounts.controller.cic;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import uk.gov.companieshouse.web.accounts.annotation.PreviousController;
import uk.gov.companieshouse.web.accounts.controller.BaseController;

@Controller
@RequestMapping("/accounts/cic/cics-file-paper")
@PreviousController(CicCantFileOnlineYetController.class)
public class CicFileOnPaperController extends BaseController {

    @Override
    protected String getTemplateName() {
        return "cic/cicFileOnPaper";
    }

    @GetMapping
    public String getCicFileOnPaper(@RequestParam("backLink") String backLink, @RequestParam("accountType") String accountType, Model model) {

        model.addAttribute("backButton", "/accounts/cic/cant-file-online-yet?backLink=" + backLink + "&accountType=" + accountType);

        return getTemplateName();
    }
}
