package uk.gov.companieshouse.web.accounts.controller.cic;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.UrlBasedViewResolver;
import uk.gov.companieshouse.web.accounts.controller.BaseController;
import uk.gov.companieshouse.web.accounts.model.accounts.TypeOfAccounts;

import javax.validation.Valid;

@Controller
@RequestMapping("/accounts/cic/select-account-type")
public class CicSelectAccountTypeController extends BaseController {

    private static final String CANT_FILE_ONLINE_YET_URL_LINK = "/accounts/cic/cant-file-online-yet";
    private static final String FILE_FULL_ACCOUNTS_URL_LINK = "/accounts/cic/full-accounts-criteria";

    private static final String BACK_BUTTON_URL_LINK = "/accounts/cic/select-account-type";

    @Override
    protected String getTemplateName() {
        return "accountselector/selectAccountType";
    }

    @GetMapping
    public String getCicTypeOfAccounts(Model model) {

        model.addAttribute("typeOfAccounts", new TypeOfAccounts());

        return getTemplateName();
    }

    @PostMapping
    public String postCicTypeOfAccounts(@ModelAttribute("typeOfAccounts") @Valid TypeOfAccounts typeOfAccounts,
        BindingResult bindingResult, RedirectAttributes attributes) {

            if (bindingResult.hasErrors()) {
                return getTemplateName();
            }

            if (!"full".equalsIgnoreCase(typeOfAccounts.getSelectedAccountTypeName())) {

                attributes.addAttribute("backLink", BACK_BUTTON_URL_LINK);
                attributes.addAttribute("accountType", typeOfAccounts.getSelectedAccountTypeName());

                return UrlBasedViewResolver.REDIRECT_URL_PREFIX + CANT_FILE_ONLINE_YET_URL_LINK;

            } else {
                return UrlBasedViewResolver.REDIRECT_URL_PREFIX + FILE_FULL_ACCOUNTS_URL_LINK;
            }
    }
}
