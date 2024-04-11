package uk.gov.companieshouse.web.accounts.model.accounts;

import jakarta.validation.constraints.NotNull;

public class TypeOfAccounts {
    @NotNull(message = "{typeOfAccounts.selectionNotMade}")
    private String selectedAccountTypeName;

    public String getSelectedAccountTypeName() {
        return selectedAccountTypeName;
    }

    public void setSelectedAccountTypeName(String selectedAccountTypeName) {
        this.selectedAccountTypeName = selectedAccountTypeName;
    }
}
