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
import uk.gov.companieshouse.web.accounts.model.payment.PayFilingFee;
import uk.gov.companieshouse.web.accounts.service.payment.PaymentService;

@Controller
@RequestMapping("/company/{companyNumber}/transaction/{transactionId}/company-accounts/{companyAccountsId}/pay-filing-fee")
public class PayFilingFeeController extends BaseController {

    private static final String TEMPLATE_NAME = "payment/payFilingFee";

    private static final String YOUR_FILINGS_PATH = "/user/transactions";

    private static final String SUMMARY_FALSE_PARAMETER = "?summary=false";

    @Autowired
    private PaymentService paymentService;

    @Override
    protected String getTemplateName() {
        return TEMPLATE_NAME;
    }

    @GetMapping
    public String getPayFilingFee(Model model) {
        model.addAttribute("payFilingFeeChoice", new PayFilingFee());
        return getTemplateName();
    }

    @PostMapping
    public String postPayFilingFee(@PathVariable String transactionId,
        @ModelAttribute("payFilingFeeChoice") @Valid PayFilingFee payFilingFee, BindingResult bindingResult, HttpServletRequest request) {

        if(bindingResult.hasErrors()) {
            return getTemplateName();
        }

        try {
            if(Boolean.TRUE.equals(payFilingFee.getPayFilingFeeChoice())) {
                return UrlBasedViewResolver.REDIRECT_URL_PREFIX +
                    paymentService.createPaymentSessionForTransaction(transactionId) + SUMMARY_FALSE_PARAMETER;
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
