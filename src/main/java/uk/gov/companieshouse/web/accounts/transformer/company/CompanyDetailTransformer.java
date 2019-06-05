package uk.gov.companieshouse.web.accounts.transformer.company;

import uk.gov.companieshouse.api.model.company.CompanyProfileApi;
import uk.gov.companieshouse.web.accounts.model.company.CompanyDetail;

public interface CompanyDetailTransformer {

    CompanyDetail getCompanyDetail(CompanyProfileApi companyProfile);
}
