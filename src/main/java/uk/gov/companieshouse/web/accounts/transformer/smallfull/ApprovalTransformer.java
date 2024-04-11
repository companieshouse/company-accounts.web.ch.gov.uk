package uk.gov.companieshouse.web.accounts.transformer.smallfull;

import uk.gov.companieshouse.api.model.accounts.smallfull.ApprovalApi;
import uk.gov.companieshouse.web.accounts.model.smallfull.Approval;

public interface ApprovalTransformer {
    ApprovalApi getApprovalApi(Approval approval);

    Approval getApproval(ApprovalApi approvalApi);
}
