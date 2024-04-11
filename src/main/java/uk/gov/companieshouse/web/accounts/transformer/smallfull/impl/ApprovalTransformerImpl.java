package uk.gov.companieshouse.web.accounts.transformer.smallfull.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.api.model.accounts.smallfull.ApprovalApi;
import uk.gov.companieshouse.web.accounts.model.smallfull.Approval;
import uk.gov.companieshouse.web.accounts.transformer.DateTransformer;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.ApprovalTransformer;

@Component
public class ApprovalTransformerImpl implements ApprovalTransformer {
    @Autowired
    private DateTransformer dateTransformer;

    @Override
    public ApprovalApi getApprovalApi(Approval approval) {
        ApprovalApi approvalApi = new ApprovalApi();
        approvalApi.setDate(dateTransformer.toLocalDate(approval.getDate()));
        approvalApi.setName(approval.getDirectorName());
        return approvalApi;
    }

    @Override
    public Approval getApproval(ApprovalApi approvalApi) {
        Approval approval = new Approval();
        approval.setDate(dateTransformer.toDate(approvalApi.getDate()));
        approval.setDirectorName(approvalApi.getName());
        return approval;
    }
}
