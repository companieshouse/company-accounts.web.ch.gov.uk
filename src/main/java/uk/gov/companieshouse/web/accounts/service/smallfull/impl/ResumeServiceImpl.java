package uk.gov.companieshouse.web.accounts.service.smallfull.impl;

import org.springframework.stereotype.Service;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.service.smallfull.ResumeService;

@Service
public class ResumeServiceImpl implements ResumeService {

    @Override
    public String getResumeRedirect(String companyNumber, String transactionId, String companyAccountsId) throws ServiceException {

        return "/company/" + companyNumber + "/transaction/" + transactionId + "/company-accounts/" + companyAccountsId + "/small-full/balance-sheet";
    }
}
