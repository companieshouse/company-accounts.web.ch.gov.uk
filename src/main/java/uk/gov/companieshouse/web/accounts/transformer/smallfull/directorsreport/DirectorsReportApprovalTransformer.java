package uk.gov.companieshouse.web.accounts.transformer.smallfull.directorsreport;

import uk.gov.companieshouse.api.model.accounts.directorsreport.ApprovalApi;
import uk.gov.companieshouse.web.accounts.model.directorsreport.DirectorsReportApproval;

public interface DirectorsReportApprovalTransformer {

    ApprovalApi getDirectorsReportApprovalApi(DirectorsReportApproval directorsReportApproval);

    DirectorsReportApproval getDirectorsReportApproval(ApprovalApi directorsReportApprovalApi);
}
