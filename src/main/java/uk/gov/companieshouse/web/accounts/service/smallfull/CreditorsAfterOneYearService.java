//package uk.gov.companieshouse.web.accounts.service.smallfull;
//
//import uk.gov.companieshouse.web.accounts.exception.ServiceException;
//import uk.gov.companieshouse.web.accounts.model.smallfull.notes.creditorsafteroneyear.CreditorsAfterOneYear;
//import uk.gov.companieshouse.web.accounts.validation.ValidationError;
//
//import java.util.List;
//
//public interface CreditorsAfterOneYearService {
//
//    /**
//     * Fetch creditors after one year note
//     *
//     * @param transactionId The id of the CHS transaction
//     * @param companyAccountsId The company accounts identifier
//     * @param companyNumber The company identifier
//     * @return the creditors after one year note
//     * @throws ServiceException if there's an error when retrieving the debtors note
//     */
//    CreditorsAfterOneYear getCreditorsAfterOneYear(String transactionId, String companyAccountsId, String companyNumber)
//            throws ServiceException;
//
//    /**
//     * Submit the creditors after one year note
//     *
//     * @param transactionId The id of the CHS transaction
//     * @param companyAccountsId The company accounts identifier
//     * @param creditorsAfterOneYear note to submit
//     * @return A list of validation errors, or an empty array list if none are present
//     * @throws ServiceException if there's an error on submission
//     */
//    List<ValidationError> submitCreditorsAfterOneYear(String transactionId, String companyAccountsId, CreditorsAfterOneYear creditorsAfterOneYear)
//            throws ServiceException;
//
//    /**
//     * Delete the creditors after one year note
//     *
//     * @param transactionId The id of the CHS transaction
//     * @param companyAccountsId The company accounts identifier
//     * @throws ServiceException if there's an error on deletion
//     */
//    void deleteCreditorsAfterOneYear(String transactionId, String companyAccountsId) throws ServiceException;
//}
