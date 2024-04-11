package uk.gov.companieshouse.web.accounts.model.smallfull;

import uk.gov.companieshouse.web.accounts.validation.ValidationMapping;
import uk.gov.companieshouse.web.accounts.validation.ValidationModel;

import jakarta.validation.constraints.NotBlank;
import java.util.List;

@ValidationModel
public class Approval {
    @NotBlank(message = "{approval.selectionNotMade}")
    @ValidationMapping("$.approval.name")
    private String directorName;

    @ValidationMapping("$.approval.date")
    private Date date;

    private List<String> approverOptions;

    public List<String> getApproverOptions() {
        return approverOptions;
    }

    public void setApproverOptions(List<String> approverOptions) {
        this.approverOptions = approverOptions;
    }

    public String getDirectorName() {
        return directorName;
    }

    public void setDirectorName(String directorName) {
        this.directorName = directorName;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
