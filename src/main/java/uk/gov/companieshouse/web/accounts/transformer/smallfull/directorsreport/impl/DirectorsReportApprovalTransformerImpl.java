package uk.gov.companieshouse.web.accounts.transformer.smallfull.directorsreport.impl;

import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.api.model.accounts.directorsreport.ApprovalApi;
import uk.gov.companieshouse.web.accounts.model.directorsreport.DirectorsReportApproval;
import uk.gov.companieshouse.web.accounts.model.smallfull.Date;
import uk.gov.companieshouse.web.accounts.transformer.DateTransformer;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.directorsreport.DirectorsReportApprovalTransformer;

@Component
public class DirectorsReportApprovalTransformerImpl implements DirectorsReportApprovalTransformer {
    @Autowired
    private DateTransformer dateTransformer;

    @Override
    public ApprovalApi getDirectorsReportApprovalApi(DirectorsReportApproval directorsReportApproval) {
        ApprovalApi approvalApi = new ApprovalApi();
        approvalApi.setDate(dateTransformer.toLocalDate(directorsReportApproval.getDate()));
        approvalApi.setName(directorsReportApproval.getName());
        return approvalApi;
    }

    @Override
    public DirectorsReportApproval getDirectorsReportApproval(ApprovalApi approvalApi) {
        DirectorsReportApproval directorsReportApproval = new DirectorsReportApproval();

        Date approvalDate = new Date();
        LocalDate apiDate = approvalApi.getDate();

        approvalDate.setDay(Integer.toString(apiDate.getDayOfMonth()));
        approvalDate.setMonth(Integer.toString(apiDate.getMonthValue()));
        approvalDate.setYear(Integer.toString(apiDate.getYear()));

        directorsReportApproval.setDate(approvalDate);

        directorsReportApproval.setName(approvalApi.getName());

        return directorsReportApproval;
    }
}
