package uk.gov.companieshouse.web.accounts.service.company;

import uk.gov.companieshouse.api.model.company.CompanyProfileApi;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.company.CompanyDetail;
import uk.gov.companieshouse.web.accounts.model.smallfull.BalanceSheetHeadings;

import java.time.LocalDate;
import java.util.List;

public interface CompanyService {
    CompanyProfileApi getCompanyProfile(String companyNumber) throws ServiceException;

    CompanyDetail getCompanyDetail(String companyNumber) throws ServiceException;

    boolean isMultiYearFiler(CompanyProfileApi companyProfileApi);

    BalanceSheetHeadings getBalanceSheetHeadings(CompanyProfileApi companyProfileApi);

    List<LocalDate> getFutureDatesForArd(LocalDate periodEndOn);

    List<LocalDate> getPastDatesForArd(LocalDate periodEndOn);
}
