package uk.gov.companieshouse.web.accounts.controller.cic;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import uk.gov.companieshouse.web.accounts.controller.BaseController;

@Controller
@RequestMapping("/accounts/cic/cics-file-paper")
//TODO: Previous controller will be "you can't file online yet" Controller [SFA-1331]
public class CicFileOnPaperController extends BaseController {

    @Override
    protected String getTemplateName() {
        return "cic/cicFileOnPaper";
    }

    @GetMapping
    public String getCicFileOnPaper(Model model) {

        //TODO: Implement the back button as part of the [SFA-1331] story
        return getTemplateName();
    }
}
