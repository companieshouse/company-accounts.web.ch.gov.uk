package uk.gov.companieshouse.web.accounts.model.state;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import java.io.Serializable;
import java.time.LocalDateTime;

public class CompanyAccountsDataState implements Serializable {

    @JsonProperty("accounting_policies")
    private AccountingPolicies accountingPolicies;

    @JsonProperty("cic_statements")
    private CicStatements cicStatements;
    
    @JsonProperty("has_selected_employees_note")
    private Boolean hasSelectedEmployeesNote;

    @JsonProperty("is_cic")
    private Boolean isCic;

    @JsonProperty("created")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime created = LocalDateTime.now();

    public AccountingPolicies getAccountingPolicies() {
        return accountingPolicies;
    }

    public void setAccountingPolicies(
        AccountingPolicies accountingPolicies) {
        this.accountingPolicies = accountingPolicies;
    }

    public CicStatements getCicStatements() {
        return cicStatements;
    }

    public void setCicStatements(CicStatements cicStatements) {
        this.cicStatements = cicStatements;
    }

    public Boolean getHasSelectedEmployeesNote() {
        return hasSelectedEmployeesNote;
    }

    public void setHasSelectedEmployeesNote(Boolean hasSelectedEmployeesNote) {
        this.hasSelectedEmployeesNote = hasSelectedEmployeesNote;
    }

    public Boolean getIsCic() {
        return isCic;
    }

    public void setIsCic(Boolean isCic) {
        this.isCic = isCic;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }
}
