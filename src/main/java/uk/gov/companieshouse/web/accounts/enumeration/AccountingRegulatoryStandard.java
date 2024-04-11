package uk.gov.companieshouse.web.accounts.enumeration;

public enum AccountingRegulatoryStandard {
    FRS101("These financial statements have been prepared in accordance with the provisions of Financial Reporting Standard 101"),
    FRS102("These financial statements have been prepared in accordance with the provisions of Section 1A (Small Entities) of Financial Reporting Standard 102"),
    OTHER("Other");

    private String regulatoryStandard;

    AccountingRegulatoryStandard(String regulatoryStandard) {
        this.regulatoryStandard = regulatoryStandard;
    }

    @Override
    public String toString() {
        return regulatoryStandard;
    }
}
