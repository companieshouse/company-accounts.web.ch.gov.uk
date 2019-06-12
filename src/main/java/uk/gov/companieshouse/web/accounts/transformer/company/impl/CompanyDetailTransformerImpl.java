package uk.gov.companieshouse.web.accounts.transformer.company.impl;

import org.springframework.stereotype.Component;
import uk.gov.companieshouse.api.model.company.CompanyProfileApi;
import uk.gov.companieshouse.api.model.company.RegisteredOfficeAddressApi;
import uk.gov.companieshouse.api.model.company.account.CompanyAccountApi;
import uk.gov.companieshouse.api.model.company.account.LastAccountsApi;
import uk.gov.companieshouse.web.accounts.model.company.CompanyDetail;
import uk.gov.companieshouse.web.accounts.transformer.company.CompanyDetailTransformer;

import java.util.Optional;

@Component
public class CompanyDetailTransformerImpl implements CompanyDetailTransformer {

    @Override
    public CompanyDetail getCompanyDetail(CompanyProfileApi companyProfile) {

        CompanyDetail companyDetail = new CompanyDetail();

        companyDetail.setCompanyName(companyProfile.getCompanyName());
        companyDetail.setCompanyNumber(companyProfile.getCompanyNumber());

        RegisteredOfficeAddressApi registeredOfficeAddress = companyProfile
                .getRegisteredOfficeAddress();

        if (registeredOfficeAddress != null) {

            companyDetail.setRegisteredOfficeAddress(
                    ((registeredOfficeAddress.getAddressLine1() == null) ? "": registeredOfficeAddress
                            .getAddressLine1()) +
                            ((registeredOfficeAddress.getAddressLine2() == null) ? "" :", "
                                    + registeredOfficeAddress.getAddressLine2() ) +
                            ((registeredOfficeAddress.getPostalCode() == null) ? "" : ", "
                                    + registeredOfficeAddress.getPostalCode()));
        }

        companyDetail
                .setAccountsNextMadeUpTo(Optional.of(companyProfile)
                        .map(CompanyProfileApi::getAccounts)
                        .map(CompanyAccountApi::getNextMadeUpTo)
                        .orElse(null));

        companyDetail.setLastAccountsNextMadeUpTo(Optional.of(companyProfile)
                .map(CompanyProfileApi::getAccounts)
                .map(CompanyAccountApi::getLastAccounts)
                .map(LastAccountsApi::getMadeUpTo)
                .orElse(null));

        companyDetail.setNextDue(Optional.of(companyProfile)
        .map(CompanyProfileApi::getAccounts)
        .map(CompanyAccountApi::getNextDue)
        .orElse(null));

        companyDetail.setIsCic(companyProfile.isCommunityInterestCompany());

        return companyDetail;
    }
}
