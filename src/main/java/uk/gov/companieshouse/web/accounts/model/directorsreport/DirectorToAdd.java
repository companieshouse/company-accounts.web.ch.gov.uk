package uk.gov.companieshouse.web.accounts.model.directorsreport;

import uk.gov.companieshouse.web.accounts.model.smallfull.Date;

public class DirectorToAdd {

    private String name;

    private Boolean wasDirectorAppointedDuringPeriod;

    private Date appointmentDate;

    private Boolean didDirectorResignDuringPeriod;

    private Date resignationDate;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getWasDirectorAppointedDuringPeriod() {
        return wasDirectorAppointedDuringPeriod;
    }

    public void setWasDirectorAppointedDuringPeriod(Boolean wasDirectorAppointedDuringPeriod) {
        this.wasDirectorAppointedDuringPeriod = wasDirectorAppointedDuringPeriod;
    }

    public Date getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(Date appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public Boolean getDidDirectorResignDuringPeriod() {
        return didDirectorResignDuringPeriod;
    }

    public void setDidDirectorResignDuringPeriod(Boolean didDirectorResignDuringPeriod) {
        this.didDirectorResignDuringPeriod = didDirectorResignDuringPeriod;
    }

    public Date getResignationDate() {
        return resignationDate;
    }

    public void setResignationDate(Date resignationDate) {
        this.resignationDate = resignationDate;
    }
}
