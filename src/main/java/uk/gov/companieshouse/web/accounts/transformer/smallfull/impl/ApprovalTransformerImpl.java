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

        LocalDate localDate = LocalDate.parse(
                approval.getApprovalDate().getYear() + "-" +
                approval.getApprovalDate().getMonth() + "-" +
                approval.getApprovalDate().getDay(),
                DateTimeFormatter.ofPattern("yyyy-M-d"));

        approvalApi.setDate(localDate);
        approvalApi.setName(approval.getDirectorName());
        return approvalApi;
    }
}
