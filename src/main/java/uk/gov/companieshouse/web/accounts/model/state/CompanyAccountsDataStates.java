package uk.gov.companieshouse.web.accounts.model.state;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.HashMap;
import java.util.Map;

public class CompanyAccountsDataStates {
    @JsonProperty("company_accounts_data_state_map")
    private Map<String, CompanyAccountsDataState> companyAccountsDataStateMap = new HashMap<>();

    public Map<String, CompanyAccountsDataState> getCompanyAccountsDataStateMap() {
        return companyAccountsDataStateMap;
    }

    public void setCompanyAccountsDataStateMap(
        Map<String, CompanyAccountsDataState> companyAccountsDataStateMap) {
        this.companyAccountsDataStateMap = companyAccountsDataStateMap;
    }
}
