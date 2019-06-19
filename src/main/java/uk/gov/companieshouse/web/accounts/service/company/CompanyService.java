package uk.gov.companieshouse.web.accounts.service.company;

import uk.gov.companieshouse.api.model.company.CompanyProfileApi;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.company.CompanyDetail;

public interface CompanyService {

    CompanyProfileApi getCompanyProfile(String companyNumber) throws ServiceException;

    CompanyDetail getCompanyDetail(String companyNumber) throws ServiceException;
}
