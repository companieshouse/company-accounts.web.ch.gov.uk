package uk.gov.companieshouse.web.accounts.transformer.cic;


import java.time.LocalDate;
import uk.gov.companieshouse.api.model.accounts.cic.approval.CicApprovalApi;
import uk.gov.companieshouse.web.accounts.model.cic.CicApproval;

public interface CicApprovalTransformer {

    CicApprovalApi getCicApprovalApi(CicApproval cicApproval);

    LocalDate getCicApprovalDate(CicApproval cicApproval);
}
