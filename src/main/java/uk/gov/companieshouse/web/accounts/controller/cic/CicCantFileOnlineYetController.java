package uk.gov.companieshouse.web.accounts.controller.cic;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import uk.gov.companieshouse.web.accounts.controller.BaseController;

@Controller
@RequestMapping("/accounts/cic/cant-file-online-yet")
public class CicCantFileOnlineYetController extends BaseController {

    @Override
    protected String getTemplateName() {
        return "cic/cicCantFileOnlineYet";
    }

    @GetMapping
    String getCantFileOnlineYetRequest(Model model) {

        model.addAttribute("accountType", "full");

        return getTemplateName();
    }
}
