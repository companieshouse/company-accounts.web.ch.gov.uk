package uk.gov.companieshouse.web.accounts.model.state;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompanyAccountsDataStates {

    @JsonProperty("company_accounts_states")
    private Map<String, CompanyAccountsDataState> companyAccountsDataStatesMap = new HashMap<>();
}
