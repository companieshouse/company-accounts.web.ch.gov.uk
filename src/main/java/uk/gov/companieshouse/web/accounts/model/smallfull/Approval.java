package uk.gov.companieshouse.web.accounts.model.smallfull;

import lombok.Getter;
import lombok.Setter;
import uk.gov.companieshouse.web.accounts.validation.ValidationMapping;
import uk.gov.companieshouse.web.accounts.validation.ValidationModel;

@Getter
@Setter
@ValidationModel
public class Approval {

    @ValidationMapping("$.approval.name")
    private String directorName;

    @ValidationMapping("$.approval.date")
    private ApprovalDate approvalDate;
}
