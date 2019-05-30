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
import uk.gov.companieshouse.web.accounts.service.smallfull.ResumeService;

@Service
public class ResumeServiceImpl implements ResumeService {

    @Autowired
    private ApiClientService apiClientService;

    private static final UriTemplate RESUME_URI = new UriTemplate(
            "/company/{companyNumber}/transaction/{transactionId}/company-accounts/{companyAccountsId}/{endpoint}");

    @Override
    public String getResumeRedirect(String companyNumber, String transactionId, String companyAccountsId) throws ServiceException {


        ApiClient apiClient = apiClientService.getApiClient();
        CompanyProfileApi company = null;

        try {
            company = apiClient.company().get("/company/" + companyNumber).execute().getData();
        } catch (ApiErrorResponseException | URIValidationException e) {
            throw new ServiceException("Error when trying to retrieve company data", e);
        }


        if (company != null && company.isCommunityInterestCompany()) {
            return UrlBasedViewResolver.REDIRECT_URL_PREFIX +
                    RESUME_URI.expand(companyNumber, transactionId,
                            companyAccountsId, "cic/company-activity").toString();
        }

        return UrlBasedViewResolver.REDIRECT_URL_PREFIX +
                RESUME_URI.expand(companyNumber, transactionId,
                                companyAccountsId, "small-full/balance-sheet").toString();
    }
}
