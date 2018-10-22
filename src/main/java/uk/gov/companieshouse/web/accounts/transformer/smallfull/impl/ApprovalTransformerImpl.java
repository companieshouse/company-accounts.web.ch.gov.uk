package uk.gov.companieshouse.web.accounts.transformer.smallfull.impl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.api.model.accounts.smallfull.ApprovalApi;
import uk.gov.companieshouse.web.accounts.model.smallfull.Approval;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.ApprovalTransformer;

@Component
public class ApprovalTransformerImpl implements ApprovalTransformer {

    @Override
    public ApprovalApi getApprovalApi(Approval approval) {

        ApprovalApi approvalApi = new ApprovalApi();
        approvalApi.setDate(getApprovalDate(approval));
        approvalApi.setName(approval.getDirectorName());
        return approvalApi;
    }

    @Override
    public LocalDate getApprovalDate(Approval approval) {

        return LocalDate.parse(
                approval.getDate().getYear() + "-" +
                approval.getDate().getMonth() + "-" +
                approval.getDate().getDay(),
                DateTimeFormatter.ofPattern("yyyy-M-d"));
    }
}
