package uk.gov.companieshouse.web.accounts.controller.cic;

import java.util.Optional;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriTemplate;
import uk.gov.companieshouse.web.accounts.controller.BaseController;

@Controller
@RequestMapping({"/accounts/cic/cant-file-online-yet", "/accounts/cic/{companyNumber}/cant-file-online-yet" })
public class CicCantFileOnlineYetController extends BaseController {
    @Override
    protected String getTemplateName() {
        return "cic/cicCantFileOnlineYet";
    }

    private static final String LINK_URL = "/accounts/cic/cics-file-paper";
    private static final UriTemplate LINK_URL_COMP_NUM = new UriTemplate("/accounts/cic/{companyNumber}/cics-file-paper");

    @GetMapping
    public String getCantFileOnlineYetRequest(@RequestParam("backLink") String backLink, @RequestParam("accountType") String accountType, @PathVariable Optional<String> companyNumber, Model model) {
        if(companyNumber.isPresent()) {
            model.addAttribute("link", LINK_URL_COMP_NUM.expand(companyNumber.get()));
        } else {
            model.addAttribute("link", LINK_URL);
        }

        model.addAttribute("accountType", accountType);

        model.addAttribute("backButton", backLink);

        return getTemplateName();
    }
}
