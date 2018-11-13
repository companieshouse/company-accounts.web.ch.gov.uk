package uk.gov.companieshouse.web.accounts.transformer.smallfull;

import java.time.LocalDate;
import uk.gov.companieshouse.api.model.accounts.smallfull.ApprovalApi;
import uk.gov.companieshouse.web.accounts.model.smallfull.Approval;

public interface ApprovalTransformer {

    ApprovalApi getApprovalApi(Approval approval);

    LocalDate getApprovalDate(Approval approval);
}
