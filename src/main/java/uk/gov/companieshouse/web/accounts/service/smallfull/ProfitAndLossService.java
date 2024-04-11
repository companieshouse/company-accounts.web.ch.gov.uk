package uk.gov.companieshouse.web.accounts.service.smallfull;

import java.util.List;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.profitandloss.ProfitAndLoss;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

public interface ProfitAndLossService {
    ProfitAndLoss getProfitAndLoss(String transactionId, String companyAccountsId, String companyNumber)
            throws ServiceException;

    List<ValidationError> submitProfitAndLoss(String transactionId, String companyAccountsId, String companyNumber, ProfitAndLoss profitAndLoss)
            throws ServiceException;

    void deleteProfitAndLoss(String transactionId, String companyAccountsId, String companyNumber)
            throws ServiceException;
}
