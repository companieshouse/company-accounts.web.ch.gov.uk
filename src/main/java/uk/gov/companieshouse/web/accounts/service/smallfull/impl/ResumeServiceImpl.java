package uk.gov.companieshouse.web.accounts.service.smallfull.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.UrlBasedViewResolver;
import org.springframework.web.util.UriTemplate;
import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.error.ApiErrorResponseException;
import uk.gov.companieshouse.api.handler.exception.URIValidationException;
import uk.gov.companieshouse.api.model.company.CompanyProfileApi;
import uk.gov.companieshouse.web.accounts.api.ApiClientService;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.service.company.CompanyService;
import uk.gov.companieshouse.web.accounts.service.smallfull.DirectorsReportService;
import uk.gov.companieshouse.web.accounts.service.smallfull.ProfitAndLossService;
import uk.gov.companieshouse.web.accounts.service.smallfull.ResumeService;

@Service
public class ResumeServiceImpl implements ResumeService {

    @Autowired
    private CompanyService companyService;

    @Autowired
    private DirectorsReportService directorsReportService;

    @Autowired
    private ApiClientService apiClientService;

    @Autowired
    private ProfitAndLossService profitAndLossService;


    private static final UriTemplate RESUME_URI = new UriTemplate(
            "/company/{companyNumber}/transaction/{transactionId}/company-accounts/{companyAccountsId}/{endpoint}");

    @Override
    public String getResumeRedirect(String companyNumber, String transactionId, String companyAccountsId) throws ServiceException {

        CompanyProfileApi company = companyService.getCompanyProfile(companyNumber);

        if (company.isCommunityInterestCompany()) {
            return UrlBasedViewResolver.REDIRECT_URL_PREFIX +
                    RESUME_URI.expand(companyNumber, transactionId,
                            companyAccountsId, "cic/company-activity").toString();
        }

        if (profitAndLossService.getProfitAndLoss(transactionId, companyAccountsId, companyNumber) != null) {
            return UrlBasedViewResolver.REDIRECT_URL_PREFIX +
                    RESUME_URI.expand(companyNumber, transactionId,
                            companyAccountsId, "small-full/profit-and-loss").toString();
        }

        if (directorsReportService.getDirectorsReport(apiClientService.getApiClient(), transactionId, companyAccountsId) != null) {
            return UrlBasedViewResolver.REDIRECT_URL_PREFIX +
                    RESUME_URI.expand(companyNumber, transactionId,
                            companyAccountsId, "small-full/add-or-remove-directors").toString();

        }

        return UrlBasedViewResolver.REDIRECT_URL_PREFIX +
                RESUME_URI.expand(companyNumber, transactionId,
                        companyAccountsId, "small-full/balance-sheet").toString();
    }
}
