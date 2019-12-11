package uk.gov.companieshouse.web.accounts.model.cic;

import uk.gov.companieshouse.web.accounts.model.smallfull.Date;
import uk.gov.companieshouse.web.accounts.validation.ValidationMapping;
import uk.gov.companieshouse.web.accounts.validation.ValidationModel;

@ValidationModel
public class CicApproval {

    @ValidationMapping("$.cic_approval.name")
    private String directorName;

    @ValidationMapping("$.cic_approval.date")
    private Date date;

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