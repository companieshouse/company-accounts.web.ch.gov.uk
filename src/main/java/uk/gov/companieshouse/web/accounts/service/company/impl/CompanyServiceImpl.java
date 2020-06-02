package uk.gov.companieshouse.web.accounts.service.company.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriTemplate;
import uk.gov.companieshouse.accountsdates.AccountsDatesHelper;
import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.error.ApiErrorResponseException;
import uk.gov.companieshouse.api.handler.exception.URIValidationException;
import uk.gov.companieshouse.api.model.company.CompanyProfileApi;
import uk.gov.companieshouse.api.model.company.account.CompanyAccountApi;
import uk.gov.companieshouse.api.model.company.account.LastAccountsApi;
import uk.gov.companieshouse.api.model.company.account.NextAccountsApi;
import uk.gov.companieshouse.web.accounts.api.ApiClientService;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.company.CompanyDetail;
import uk.gov.companieshouse.web.accounts.model.smallfull.BalanceSheetHeadings;
import uk.gov.companieshouse.web.accounts.service.company.CompanyService;
import uk.gov.companieshouse.web.accounts.transformer.company.CompanyDetailTransformer;

@Service
public class CompanyServiceImpl implements CompanyService {

    @Autowired
    private ApiClientService apiClientService;

    @Autowired
    private CompanyDetailTransformer companyDetailTransformer;

    @Autowired
    private AccountsDatesHelper accountsDatesHelper;

    private static final UriTemplate GET_COMPANY_URI = new UriTemplate("/company/{companyNumber}");

    @Override
    public CompanyProfileApi getCompanyProfile(String companyNumber) throws ServiceException {

        ApiClient apiClient = apiClientService.getApiClient();

        CompanyProfileApi companyProfileApi;

        String uri = GET_COMPANY_URI.expand(companyNumber).toString();

        try {
            companyProfileApi = apiClient.company().get(uri).execute().getData();
        } catch (ApiErrorResponseException e) {

            throw new ServiceException("Error retrieving company profile", e);
        } catch (URIValidationException e) {

            throw new ServiceException("Invalid URI for company resource", e);
        }

        return companyProfileApi;
    }

    @Override
    public CompanyDetail getCompanyDetail(String companyNumber) throws ServiceException {

        return companyDetailTransformer.getCompanyDetail(getCompanyProfile(companyNumber));
    }

    @Override
    public boolean isMultiYearFiler(CompanyProfileApi companyProfile) {

        return Optional.ofNullable(companyProfile)
                .map(CompanyProfileApi::getAccounts)
                .map(CompanyAccountApi::getLastAccounts)
                .map(LastAccountsApi::getPeriodEndOn)
                .isPresent();
    }

    @Override
    public BalanceSheetHeadings getBalanceSheetHeadings(CompanyProfileApi companyProfile) {

        boolean isSameYear = isSameYearFiler(companyProfile);
        BalanceSheetHeadings balanceSheetHeadings = new BalanceSheetHeadings();
        balanceSheetHeadings.setPreviousPeriodHeading(getPreviousPeriodHeading(companyProfile,
                isSameYear));
        balanceSheetHeadings.setCurrentPeriodHeading(getCurrentPeriodHeading(companyProfile,
                isSameYear));
        return balanceSheetHeadings;
    }

    private String getCurrentPeriodHeading(CompanyProfileApi companyProfile, boolean isSameYear) {

        NextAccountsApi nextAccountsApi = companyProfile.getAccounts().getNextAccounts();
        LocalDate currentPeriodEndOn = nextAccountsApi.getPeriodEndOn();
        LocalDate currentPeriodStartOn = nextAccountsApi.getPeriodStartOn();

        return accountsDatesHelper.generateBalanceSheetHeading(currentPeriodStartOn,
                currentPeriodEndOn, isSameYear);
    }

    private String getPreviousPeriodHeading(CompanyProfileApi companyProfile, boolean isSameYear) {

        if (isMultiYearFiler(companyProfile)) {
            LastAccountsApi lastAccountsApi = companyProfile.getAccounts().getLastAccounts();
            LocalDate previousPeriodStartOn = lastAccountsApi.getPeriodStartOn();
            LocalDate previousPeriodEndOn = lastAccountsApi.getPeriodEndOn();

            return accountsDatesHelper.generateBalanceSheetHeading(previousPeriodStartOn,
                    previousPeriodEndOn, isSameYear);
        }
        return null;
    }

    private boolean isSameYearFiler(CompanyProfileApi companyProfile) {

        if (isMultiYearFiler(companyProfile)) {
            LastAccountsApi lastAccountsApi = companyProfile.getAccounts().getLastAccounts();
            LocalDate previousPeriodEndOn = lastAccountsApi.getPeriodEndOn();

            NextAccountsApi nextAccountsApi = companyProfile.getAccounts().getNextAccounts();
            LocalDate currentPeriodEndOn = nextAccountsApi.getPeriodEndOn();

            return accountsDatesHelper.isSameYear(previousPeriodEndOn, currentPeriodEndOn);
        }

        return false;
    }

    @Override
    public List<LocalDate> getFutureDatesForArd(LocalDate periodEndOn) {

    	LocalDate todaysDate = LocalDate.now();
        List<LocalDate> futureValidDates = new ArrayList<>();

        for(int i = 1; i <= 7; i++) {
        	if(periodEndOn.plusDays(i).isAfter(todaysDate)) {
        		break;
        	}
            futureValidDates.add(periodEndOn.plusDays(i));
        }

        Collections.sort(futureValidDates);

        return  futureValidDates;
    }

    @Override
    public List<LocalDate> getPastDatesForArd(LocalDate periodEndOn) {

        List<LocalDate> pastValidDates = new ArrayList<>();

        pastValidDates.add(periodEndOn.minusDays(1));
        pastValidDates.add(periodEndOn.minusDays(2));
        pastValidDates.add(periodEndOn.minusDays(3));
        pastValidDates.add(periodEndOn.minusDays(4));
        pastValidDates.add(periodEndOn.minusDays(5));
        pastValidDates.add(periodEndOn.minusDays(6));
        pastValidDates.add(periodEndOn.minusDays(7));

        Collections.sort(pastValidDates);

        return pastValidDates;
    }
}
