package uk.gov.companieshouse.web.accounts.service.smallfull.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriTemplate;
import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.error.ApiErrorResponseException;
import uk.gov.companieshouse.api.handler.exception.URIValidationException;
import uk.gov.companieshouse.api.model.accounts.smallfull.creditorsafteroneyear.CreditorsAfterOneYearApi;
import uk.gov.companieshouse.web.accounts.api.ApiClientService;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.smallfull.BalanceSheet;
import uk.gov.companieshouse.web.accounts.model.smallfull.BalanceSheetHeadings;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.creditorsafteroneyear.CreditorsAfterOneYear;
import uk.gov.companieshouse.web.accounts.service.smallfull.BalanceSheetService;
import uk.gov.companieshouse.web.accounts.service.smallfull.CreditorsAfterOneYearService;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.CreditorsAfterOneYearTransformer;

@Service
public class CreditorsAfterOneYearServiceImpl implements CreditorsAfterOneYearService {

    private static final UriTemplate CREDITORS_AFTER_ONE_YEAR_URI =
            new UriTemplate(
                    "/transactions/{transactionId}/company-accounts/{companyAccountsId}/small" +
                            "-full/notes/creditors-after-more-than-one-year");

    private static final String INVALID_URI_MESSAGE =
            "Invalid URI for creditors after one year resource";

    @Autowired
    private BalanceSheetService balanceSheetService;

    @Autowired
    private ApiClientService apiClientService;

    @Autowired
    private CreditorsAfterOneYearTransformer transformer;

    @Override
    public CreditorsAfterOneYear getCreditorsAfterOneYear(String transactionId,
            String companyAccountsId, String companyNumber) throws ServiceException {
        CreditorsAfterOneYearApi creditorsAfterOneYearApi =
                getCreditorsAfterOneYearApi(transactionId, companyAccountsId);
        CreditorsAfterOneYear creditorsAfterOneYear =
                transformer.getCreditorsAfterOneYear(creditorsAfterOneYearApi);

        BalanceSheet balanceSheet =
                balanceSheetService.getBalanceSheet(transactionId, companyAccountsId,
                        companyNumber);
        BalanceSheetHeadings balanceSheetHeadings = balanceSheet.getBalanceSheetHeadings();
        creditorsAfterOneYear.setBalanceSheetHeadings(balanceSheetHeadings);

        return creditorsAfterOneYear;
    }

    private CreditorsAfterOneYearApi getCreditorsAfterOneYearApi(String transactionId,
            String companyAccountsId) throws ServiceException {
        ApiClient apiClient = apiClientService.getApiClient();

        String uri =
                CREDITORS_AFTER_ONE_YEAR_URI.expand(transactionId, companyAccountsId).toString();

        try {
            return apiClient.smallFull().creditorsAfterOnerYear().get(uri).execute();

        } catch (ApiErrorResponseException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND.value()) {
                return null;
            }
            throw new ServiceException("Error when retrieving creditors after one year note", e);
        } catch (URIValidationException e) {
            throw new ServiceException(INVALID_URI_MESSAGE, e);
        }
    }

}
