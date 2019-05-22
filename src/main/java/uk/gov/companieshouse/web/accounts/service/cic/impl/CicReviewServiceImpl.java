package uk.gov.companieshouse.web.accounts.service.cic.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.cic.CicReview;
import uk.gov.companieshouse.web.accounts.service.cic.CicReviewService;
import uk.gov.companieshouse.web.accounts.service.cic.statements.CicStatementsService;
import uk.gov.companieshouse.web.accounts.transformer.cic.CicStatementsTransformer;


@Service
public class CicReviewServiceImpl implements CicReviewService {

    @Autowired
    private CicStatementsService cicStatementsService;

    @Autowired
    private CicStatementsTransformer cicStatementsTransformer;


    public CicReview getReview(String transactionId, String companyAccountsId, String companyNumber)
        throws ServiceException {

        return cicStatementsTransformer.getCicReview(
            cicStatementsService.getCicStatementsApi(transactionId, companyAccountsId));

    }
}
