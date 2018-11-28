package uk.gov.companieshouse.web.accounts.service.smallfull.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.UrlBasedViewResolver;
import uk.gov.companieshouse.environment.EnvironmentReader;
import uk.gov.companieshouse.web.accounts.service.smallfull.ResumeService;

@Service
public class ResumeServiceImpl implements ResumeService {

    @Autowired
    EnvironmentReader environmentReader;

    private static final String CHS_URL = "CHS_URL";

    @Override
    public String getResumeRedirect(String companyNumber, String transactionId, String companyAccountsId) {

        return UrlBasedViewResolver.REDIRECT_URL_PREFIX +
                environmentReader.getMandatoryString(CHS_URL) +
                "/company/" + companyNumber +
                "/transaction/" + transactionId +
                "/company-accounts/" + companyAccountsId +
                "/small-full/balance-sheet";
    }
}
