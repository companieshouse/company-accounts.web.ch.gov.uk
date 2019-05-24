package uk.gov.companieshouse.web.accounts.model.cic;


import lombok.Getter;
import lombok.Setter;
import uk.gov.companieshouse.web.accounts.model.smallfull.ApprovalDate;
import uk.gov.companieshouse.web.accounts.validation.ValidationMapping;
import uk.gov.companieshouse.web.accounts.validation.ValidationModel;

@Getter
@Setter
@ValidationModel
public class CicApproval {

    @ValidationMapping("$.cic_approval.name")
    private String directorName;

    @ValidationMapping("$.cic_approval.date")
    private ApprovalDate date;
}