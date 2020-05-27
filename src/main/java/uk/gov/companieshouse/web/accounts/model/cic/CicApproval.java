package uk.gov.companieshouse.web.accounts.model.cic;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import uk.gov.companieshouse.web.accounts.model.smallfull.Date;
import uk.gov.companieshouse.web.accounts.validation.ValidationMapping;
import uk.gov.companieshouse.web.accounts.validation.ValidationModel;

@ValidationModel
public class CicApproval {

    @ValidationMapping("$.cic_approval.name")
    private String directorName;

    @ValidationMapping("$.cic_approval.date")
    private Date date;

    private Boolean dateInvalidated;

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

    public Boolean getDateInvalidated() {
        return dateInvalidated;
    }

    public void setDateInvalidated(Boolean dateInvalidated) {
        this.dateInvalidated = dateInvalidated;
    }

    public LocalDate getLocalDate() {
        if (this.date != null) {
            return LocalDate.parse(this.date.getYear() + "-" + this.date.getMonth() + "-" + this.date.getDay(),
                    DateTimeFormatter.ofPattern("yyyy-M-d"));
        }
        return null;
    }
}