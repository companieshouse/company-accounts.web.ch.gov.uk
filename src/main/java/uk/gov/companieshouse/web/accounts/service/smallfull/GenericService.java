package uk.gov.companieshouse.web.accounts.service.smallfull;

import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

import javax.sql.rowset.serial.SerialException;
import java.util.List;

public interface GenericService<T> {

    T getNoteType(String transactionId, String companyAccountsId, String companyNumber)
        throws SerialException;

    List<ValidationError> submitNoteType(String transactionId, String companyAccountsId,
                                            T noteService) throws ServiceException;

    void deleteNoteService(String transactionId, String companyAccountsId) throws ServiceException;
}
