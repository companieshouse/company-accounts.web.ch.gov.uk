package uk.gov.companieshouse.web.accounts.model.company;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class CompanyDetail {

    private String companyName;

    private String companyNumber;

    private String registeredOfficeAddress;

    private LocalDate accountsNextMadeUpTo;

    private LocalDate lastAccountsNextMadeUpTo;

    private Boolean isCic;

    private LocalDate nextDue;
}