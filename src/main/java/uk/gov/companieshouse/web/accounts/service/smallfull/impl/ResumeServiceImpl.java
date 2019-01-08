package uk.gov.companieshouse.web.accounts.service.smallfull.impl;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.UrlBasedViewResolver;
import uk.gov.companieshouse.web.accounts.service.smallfull.ResumeService;

@Service
public class ResumeServiceImpl implements ResumeService {

    @Override
    public String getResumeRedirect(String companyNumber, String transactionId, String companyAccountsId) {

        return UrlBasedViewResolver.REDIRECT_URL_PREFIX +
                "/company/" + companyNumber +
                "/transaction/" + transactionId +
                "/company-accounts/" + companyAccountsId +
                "/small-full/balance-sheet";
    }
}
