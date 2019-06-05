package uk.gov.companieshouse.web.accounts.transformer.company.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.api.handler.company.request.CompanyGet;
import uk.gov.companieshouse.api.model.ApiResponse;
import uk.gov.companieshouse.api.model.company.CompanyProfileApi;
import uk.gov.companieshouse.api.model.company.RegisteredOfficeAddressApi;
import uk.gov.companieshouse.api.model.company.account.CompanyAccountApi;
import uk.gov.companieshouse.api.model.company.account.LastAccountsApi;
import uk.gov.companieshouse.web.accounts.model.company.CompanyDetail;
import uk.gov.companieshouse.web.accounts.transformer.company.CompanyDetailTransformer;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CompanyTransformerImplTests {


    @Mock
    private CompanyGet companyGet;

    @Mock
    private ApiResponse<CompanyProfileApi> responseWithData;

    @Mock
    private CompanyDetail companyDetail;

    private CompanyDetailTransformer companyDetailTransformer = new CompanyDetailTransformerImpl();

    private static final String COMPANY_NAME = "company";
    private static final String COMPANY_NUMBER = "number";

    private static final String ADDRESS_LINE_1 = "address line 1";
    private static final String ADDRESS_LINE_2 = "address line 2";
    private static final String POSTAL_CODE = "postal code";
    private static final String ADDRESS_FORMATTED = ADDRESS_LINE_1 + ", " + ADDRESS_LINE_2 + ", " + POSTAL_CODE;

    private static final LocalDate NEXT_ACCOUNT_DATE = LocalDate.of(2016, 5, 3);
    private static final LocalDate LAST_ACCOUNT_DATE = LocalDate.of(2015, 5, 3);

    private CompanyProfileApi createMockCompanyProfileApi(boolean hasRegisteredOfficeAddress, boolean hasAccounts) {

        CompanyProfileApi companyProfile = new CompanyProfileApi();

        companyProfile.setCompanyName(COMPANY_NAME);
        companyProfile.setCompanyNumber(COMPANY_NUMBER);

        if (hasAccounts) {
            CompanyAccountApi companyAccounts = new CompanyAccountApi();
            companyAccounts.setNextMadeUpTo(NEXT_ACCOUNT_DATE);

            LastAccountsApi lastAccounts = new LastAccountsApi();
            lastAccounts.setMadeUpTo(LAST_ACCOUNT_DATE);
            companyAccounts.setLastAccounts(lastAccounts);

            companyProfile.setAccounts(companyAccounts);
        }

        if (hasRegisteredOfficeAddress) {
            RegisteredOfficeAddressApi address = new RegisteredOfficeAddressApi();
            address.setAddressLine1(ADDRESS_LINE_1);
            address.setAddressLine2(ADDRESS_LINE_2);
            address.setPostalCode(POSTAL_CODE);
            companyProfile.setRegisteredOfficeAddress(address);
        }

        return companyProfile;
    }

    @Test
    @DisplayName("Get Company Detail - All fields Populated Path")
    void getCompanyDetailAllPopulated() {

        CompanyDetail companyDetailReturned = companyDetailTransformer.getCompanyDetail(createMockCompanyProfileApi(true, true));

        assertEquals(COMPANY_NAME, companyDetailReturned.getCompanyName());
        assertEquals(COMPANY_NUMBER, companyDetailReturned.getCompanyNumber());
        assertEquals(ADDRESS_FORMATTED, companyDetailReturned.getRegisteredOfficeAddress());
        assertEquals(NEXT_ACCOUNT_DATE, companyDetailReturned.getAccountsNextMadeUpTo());
        assertEquals(LAST_ACCOUNT_DATE, companyDetailReturned.getLastAccountsNextMadeUpTo());
    }

    @Test
    @DisplayName("Get Company Detail - No Accounts Path")
    void getCompanyDetailNoAccounts() {

        CompanyDetail companyDetailReturned = companyDetailTransformer.getCompanyDetail(createMockCompanyProfileApi(true, false));

        assertEquals(COMPANY_NAME, companyDetailReturned.getCompanyName());
        assertEquals(COMPANY_NUMBER, companyDetailReturned.getCompanyNumber());
        assertEquals(ADDRESS_FORMATTED, companyDetailReturned.getRegisteredOfficeAddress());
        assertNull(companyDetailReturned.getAccountsNextMadeUpTo());
        assertNull(companyDetailReturned.getLastAccountsNextMadeUpTo());
    }

    @Test
    @DisplayName("Get Company Detail - No Address Path")
    void getCompanyDetailNoAddress() {

        CompanyDetail companyDetailReturned = companyDetailTransformer.getCompanyDetail(createMockCompanyProfileApi(false, true));

        assertEquals(COMPANY_NAME, companyDetailReturned.getCompanyName());
        assertEquals(COMPANY_NUMBER, companyDetailReturned.getCompanyNumber());
        assertNull(companyDetailReturned.getRegisteredOfficeAddress());
        assertEquals(NEXT_ACCOUNT_DATE, companyDetailReturned.getAccountsNextMadeUpTo());
        assertEquals(LAST_ACCOUNT_DATE, companyDetailReturned.getLastAccountsNextMadeUpTo());
    }
}
