package uk.gov.companieshouse.web.accounts.model.company;

import java.time.LocalDate;

public class CompanyDetail {

    private String companyName;

    private String companyNumber;

    private String registeredOfficeAddress;

    private LocalDate accountsNextMadeUpTo;

    private LocalDate lastAccountsNextMadeUpTo;

    private Boolean isCic;

    private LocalDate nextDue;

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyNumber() {
        return companyNumber;
    }

    public void setCompanyNumber(String companyNumber) {
        this.companyNumber = companyNumber;
    }

    public String getRegisteredOfficeAddress() {
        return registeredOfficeAddress;
    }

    public void setRegisteredOfficeAddress(String registeredOfficeAddress) {
        this.registeredOfficeAddress = registeredOfficeAddress;
    }

    public LocalDate getAccountsNextMadeUpTo() {
        return accountsNextMadeUpTo;
    }

    public void setAccountsNextMadeUpTo(LocalDate accountsNextMadeUpTo) {
        this.accountsNextMadeUpTo = accountsNextMadeUpTo;
    }

    public LocalDate getLastAccountsNextMadeUpTo() {
        return lastAccountsNextMadeUpTo;
    }

    public void setLastAccountsNextMadeUpTo(LocalDate lastAccountsNextMadeUpTo) {
        this.lastAccountsNextMadeUpTo = lastAccountsNextMadeUpTo;
    }

    public Boolean getIsCic() {
        return isCic;
    }

    public void setIsCic(Boolean isCic) {
        this.isCic = isCic;
    }

    public LocalDate getNextDue() {
        return nextDue;
    }

    public void setNextDue(LocalDate nextDue) {
        this.nextDue = nextDue;
    }


}