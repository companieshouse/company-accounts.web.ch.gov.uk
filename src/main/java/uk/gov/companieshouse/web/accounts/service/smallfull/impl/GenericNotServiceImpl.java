package uk.gov.companieshouse.web.accounts.service.smallfull.impl;

import com.sun.org.apache.regexp.internal.RE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.util.UriTemplate;
import uk.gov.companieshouse.web.accounts.api.ApiClientService;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.service.smallfull.BalanceSheetService;
import uk.gov.companieshouse.web.accounts.service.smallfull.GenericService;
import uk.gov.companieshouse.web.accounts.service.smallfull.SmallFullService;
import uk.gov.companieshouse.web.accounts.util.NoteTransformerFactory;
import uk.gov.companieshouse.web.accounts.util.ValidationContext;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;
import uk.gov.companieshouse.web.accounts.validation.helper.ServiceExceptionHandler;

import javax.sql.rowset.serial.SerialException;
import java.util.List;

public class GenericNotServiceImpl<T> implements GenericService<T> {

    @Autowired
    private ApiClientService apiClientService;

    @Autowired
    private NoteTransformerFactory transformerFactory;

    @Autowired
    private BalanceSheetService balanceSheetService;

    @Autowired
    private ServiceExceptionHandler serviceExceptionHandler;

    @Autowired
    private ValidationContext validationContext;

    @Autowired
    private SmallFullService smallFullService;

    private static final String RESOURCE_NAME = "employees";

    private static final UriTemplate NOTE_URI = new UriTemplate(
            "/transactions/{transactionId}/company-accounts/{companyAccountsId}/small" +
                    "-full/" + resolveNoteName(RESOURCE_NAME));


    @Override
    public T getNoteType(String transactionId, String companyAccountsId, String companyNumber) throws SerialException {

        NoteTransformer transformer = transformerFactory.getTransformer(RESOURCE_NAME);
        return null;
    }

    @Override
    public List<ValidationError> submitNoteType(String transactionId, String companyAccountsId, T noteService) throws ServiceException {
        return null;
    }

    @Override
    public void deleteNoteService(String transactionId, String companyAccountsId) throws ServiceException {
    }

    private static String resolveNoteName(String resourceName) {

        switch(resourceName) {
            case "debtors":
            case "stocks":
               break;
            case "employees":
                resourceName = "notes/" + resourceName;
        }

        return resourceName;
    }

    private
}
