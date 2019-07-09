package uk.gov.companieshouse.web.accounts.model.smallfull;

import uk.gov.companieshouse.web.accounts.validation.ValidationMapping;
import uk.gov.companieshouse.web.accounts.validation.ValidationModel;

@ValidationModel
public class Approval {

    @ValidationMapping("$.approval.name")
    private String directorName;

    @ValidationMapping("$.approval.date")
    private ApprovalDate date;

    public String getDirectorName() {
        return directorName;
    }

    public void setDirectorName(String directorName) {
        this.directorName = directorName;
    }

    public ApprovalDate getDate() {
        return date;
    }

    public void setDate(ApprovalDate date) {
        this.date = date;
    }
}
