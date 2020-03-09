package uk.gov.companieshouse.web.accounts.service.smallfull;

import java.util.List;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.offbalancesheetarrangements.OffBalanceSheetArrangements;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

public interface OffBalanceSheetArrangementsService {

    OffBalanceSheetArrangements getOffBalanceSheetArrangements(String transactionId, String companyAccountsId)
            throws ServiceException;

    List<ValidationError> submitOffBalanceSheetArrangements(String transactionId, String companyAccountsId,
            OffBalanceSheetArrangements arrangements) throws ServiceException;

    void deleteOffBalanceSheetArrangements(String transactionId, String companyAccountsId)
            throws ServiceException;
}
