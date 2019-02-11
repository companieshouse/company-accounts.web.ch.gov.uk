package uk.gov.companieshouse.web.accounts.model.accounts;

import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TypeOfAccounts {

    @NotNull(message = "{typeOfAccounts.selectionNotMade}")
    private String selectedTypeOfAccount;
}
