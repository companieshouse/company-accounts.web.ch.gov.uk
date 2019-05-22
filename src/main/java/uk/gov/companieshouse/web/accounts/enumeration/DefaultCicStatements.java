package uk.gov.companieshouse.web.accounts.enumeration;

public enum DefaultCicStatements {

    CONSULTATION_WITH_STAKEHOLDERS("No consultation with stakeholders"),
    DIRECTORS_REMUNERATION("No remuneration was received"),
    TRANSFER_OF_ASSETS("No transfer of assets other than for full consideration");

    private String defaultStatement;

    DefaultCicStatements(String defaultStatement) {
        this.defaultStatement = defaultStatement;
    }

    public String getDefaultStatement() {
        return defaultStatement;
    }
}
