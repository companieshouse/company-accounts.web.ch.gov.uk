package uk.gov.companieshouse.web.accounts.controller.payment;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.UrlBasedViewResolver;
import uk.gov.companieshouse.web.accounts.controller.BaseController;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.payment.MakePayment;
import uk.gov.companieshouse.web.accounts.service.payment.PaymentService;

@Controller
@RequestMapping("/company/{companyNumber}/transaction/{transactionId}/company-accounts/{companyAccountsId}/do-you-want-to-make-a-payment")
public class MakePaymentController extends BaseController {

    private static final String TEMPLATE_NAME = "payment/makePayment";

    private static final String YOUR_FILINGS_PATH = "/user/transactions";

    @Autowired
    private PaymentService paymentService;

    @Override
    protected String getTemplateName() {
        return TEMPLATE_NAME;
    }

    @GetMapping
    public String getMakePayment(Model model) {
        model.addAttribute("makePaymentChoice", new MakePayment());
        return getTemplateName();
    }

    @PostMapping
    public String postMakePayment(@PathVariable String transactionId,
        @ModelAttribute("makePaymentChoice") @Valid MakePayment makePaymentChoice, BindingResult bindingResult, HttpServletRequest request) {

        if(bindingResult.hasErrors()) {
            return getTemplateName();
        }

        try {
            if(makePaymentChoice.getMakePaymentChoice()) {
                return UrlBasedViewResolver.REDIRECT_URL_PREFIX +
                    paymentService.createPaymentSessionForTransaction(transactionId);
            } else {
                return UrlBasedViewResolver.REDIRECT_URL_PREFIX
                    + YOUR_FILINGS_PATH;
            }
        } catch (ServiceException e) {
            LOGGER.errorRequest(request, e.getMessage(), e);
            return ERROR_VIEW;
        }
    }
}
