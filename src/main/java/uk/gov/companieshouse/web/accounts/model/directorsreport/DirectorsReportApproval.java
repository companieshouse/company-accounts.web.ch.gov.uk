package uk.gov.companieshouse.web.accounts.model.directorsreport;

import jakarta.validation.constraints.NotBlank;
import java.util.List;
import uk.gov.companieshouse.web.accounts.model.smallfull.Date;
import uk.gov.companieshouse.web.accounts.validation.ValidationMapping;
import uk.gov.companieshouse.web.accounts.validation.ValidationModel;

@ValidationModel
public class DirectorsReportApproval {

    @NotBlank(message = "{directorsReportApproval.selectionNotMade}")
    @ValidationMapping("$.directors_approval.name")
    private String name;

    @ValidationMapping("$.directors_approval.date")
    private Date date;

    private List<String> approverOptions;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public List<String> getApproverOptions() {
        return approverOptions;
    }

    public void setApproverOptions(List<String> approverOptions) {
        this.approverOptions = approverOptions;
    }
}