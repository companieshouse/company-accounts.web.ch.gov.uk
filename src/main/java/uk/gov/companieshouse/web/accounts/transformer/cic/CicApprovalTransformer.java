package uk.gov.companieshouse.web.accounts.transformer.cic;

import uk.gov.companieshouse.api.model.accounts.cic.approval.CicApprovalApi;
import uk.gov.companieshouse.web.accounts.model.cic.CicApproval;

public interface CicApprovalTransformer {

    CicApprovalApi getCicApprovalApi(CicApproval cicApproval);

    CicApproval getCicApproval(CicApprovalApi cicApprovalApi);
}
