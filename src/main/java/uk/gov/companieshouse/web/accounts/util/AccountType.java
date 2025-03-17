package uk.gov.companieshouse.web.accounts.util;

public enum AccountType {

    PACKAGE_ACCOUNT("package", "packageAccountsEnabled"),
    DORMANT_ACCOUNT("dormant", "dormantCompanyAccountsEnabled"),
    ABRIDGED_ACCOUNT("abridged", "abridgedAccountsEnabled"),
    MICRO_ACCOUNT("micro-entity", "microEntityAccountsEnabled"),
    FULL_ACCOUNT("full", "fullAccountsEnabled");

    private final String name;

    private final String modelAttribute;

    AccountType(final String name, final String modelAttribute) {
        this.name = name;
        this.modelAttribute = modelAttribute;
    }

    @Override
    public String toString() {
        return name;
    }

    public boolean equalsIgnoreCase(String accountType) {
        return name.equalsIgnoreCase(accountType);
    }

    public String getModelAttribute() {
        return modelAttribute;
    }
}
