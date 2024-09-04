package uk.gov.companieshouse.web.accounts.service.smallfull;

import java.time.LocalDate;
import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.model.accounts.smallfull.SmallFullApi;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.smallfull.BalanceSheetHeadings;

public interface SmallFullService {

    /**
     * Create a small full resource
     *
     * @param transactionId     The id of the CHS transaction
     * @param companyAccountsId The company accounts identifier
     * @throws ServiceException on creation failure
     */
    void createSmallFullAccounts(String transactionId, String companyAccountsId) throws ServiceException;

    /**
     * Update a small full resource
     *
     * @param periodEndOn       The updated period end date you want to submit, if null is provided, the date will be defaulted to that of the company
     * @param transactionId     The id of the CHS transaction
     * @param companyAccountsId The company accounts identifier
     * @throws ServiceException on creation failure
     */
    void updateSmallFullAccounts(LocalDate periodEndOn, String transactionId, String companyAccountsId) throws ServiceException;

    /**
     * Retrieve a small full resource
     *
     * @param apiClient         The api client with which to execute the get request
     * @param transactionId     The id of the CHS transaction
     * @param companyAccountsId The company accounts identifier
     * @return the small full resource
     * @throws ServiceException or retrieval failure
     */
    SmallFullApi getSmallFullAccounts(ApiClient apiClient,
                                      String transactionId, String companyAccountsId) throws ServiceException;


    /**
     * Checks whether the given small full resource is for a multi-year filer
     *
     * @param smallFullApi      A small full resource
     * @return                  True or false
     */
    boolean isMultiYearFiler(SmallFullApi smallFullApi);

    /**
     * Checks whether the given small full resource is for a multi-year filer
     *
     * @param smallFullApi      A small full resource
     * @return the balance sheet headings for the small full resource
     */
    BalanceSheetHeadings getBalanceSheetHeadings(SmallFullApi smallFullApi);
}
