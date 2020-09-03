package uk.gov.companieshouse.web.accounts.service.smallfull.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.directorsreport.DirectorsReportReview;
import uk.gov.companieshouse.web.accounts.service.smallfull.AdditionalInformationService;
import uk.gov.companieshouse.web.accounts.service.smallfull.CompanyPolicyOnDisabledEmployeesService;
import uk.gov.companieshouse.web.accounts.service.smallfull.DirectorService;
import uk.gov.companieshouse.web.accounts.service.smallfull.DirectorsReportReviewService;
import uk.gov.companieshouse.web.accounts.service.smallfull.PoliticalAndCharitableDonationsService;
import uk.gov.companieshouse.web.accounts.service.smallfull.PrincipalActivitiesService;
import uk.gov.companieshouse.web.accounts.service.smallfull.SecretaryService;

@Service
public class DirectorsReportReviewServiceImpl implements DirectorsReportReviewService {

    @Autowired
    private DirectorService directorService;

    @Autowired
    private SecretaryService secretaryService;

    @Autowired
    private AdditionalInformationService additionalInformationService;

    @Autowired
    private CompanyPolicyOnDisabledEmployeesService companyPolicyOnDisabledEmployeesService;

    @Autowired
    private PoliticalAndCharitableDonationsService politicalAndCharitableDonationsService;

    @Autowired
    private PrincipalActivitiesService principalActivitiesService;

    @Override
    public DirectorsReportReview getReview(String transactionId, String companyAccountsId)
            throws ServiceException {

        DirectorsReportReview review = new DirectorsReportReview();

        review.setDirectors(directorService.getAllDirectors(transactionId, companyAccountsId, false));
        review.setSecretary(secretaryService.getSecretary(transactionId, companyAccountsId));
        review.setAdditionalInformation(
                additionalInformationService.getAdditionalInformation(transactionId, companyAccountsId));
        review.setCompanyPolicyOnDisabledEmployees(
                companyPolicyOnDisabledEmployeesService.getCompanyPolicyOnDisabledEmployees(transactionId, companyAccountsId));
        review.setPoliticalAndCharitableDonations(
                politicalAndCharitableDonationsService.getPoliticalAndCharitableDonations(transactionId, companyAccountsId));
        review.setPrincipalActivities(
                principalActivitiesService.getPrincipalActivities(transactionId, companyAccountsId));

        return review;
    }
}
