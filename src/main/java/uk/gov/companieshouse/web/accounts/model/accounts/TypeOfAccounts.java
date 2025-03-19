package uk.gov.companieshouse.web.accounts.model.accounts;

import jakarta.validation.constraints.NotNull;

public class TypeOfAccounts {

    @NotNull(message = "{typeOfAccounts.selectionNotMade}")
    private String selectedAccountTypeName;

    private String userJourneyAccountsType;
    
    public String getSelectedAccountTypeName() {
        return selectedAccountTypeName;
    }
    
    public void setSelectedAccountTypeName(String selectedAccountTypeName) {
        this.selectedAccountTypeName = selectedAccountTypeName;
    }

    public String getUserJourneyAccountsType() {
        return userJourneyAccountsType;
    }
    
    public void setUserJourneyAccountsType(String userJourneyAccountsType) {
        this.userJourneyAccountsType = userJourneyAccountsType;
    }
}
