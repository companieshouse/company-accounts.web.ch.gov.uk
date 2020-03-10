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

    @JsonProperty("has_included_profit_and_loss")
    private Boolean hasIncludedProfitAndLoss;

    @JsonProperty("has_included_directors_report")
    private Boolean hasIncludedDirectorsReport;

    @JsonProperty("directors_report_statements")
    private DirectorsReportStatements directorsReportStatements;

    @JsonProperty("has_included_off_balance_sheet_arrangements")
    private Boolean hasIncludedOffBalanceSheetArrangements;

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

    public Boolean getHasIncludedProfitAndLoss() {
        return hasIncludedProfitAndLoss;
    }

    public void setHasIncludedProfitAndLoss(Boolean hasIncludedProfitAndLoss) {
        this.hasIncludedProfitAndLoss = hasIncludedProfitAndLoss;
    }

    public Boolean getHasIncludedDirectorsReport() {
        return hasIncludedDirectorsReport;
    }

    public void setHasIncludedDirectorsReport(Boolean hasIncludedDirectorsReport) {
        this.hasIncludedDirectorsReport = hasIncludedDirectorsReport;
    }

    public DirectorsReportStatements getDirectorsReportStatements() {
        return directorsReportStatements;
    }

    public void setDirectorsReportStatements(
            DirectorsReportStatements directorsReportStatements) {
        this.directorsReportStatements = directorsReportStatements;
    }

    public Boolean getHasIncludedOffBalanceSheetArrangements() {
        return hasIncludedOffBalanceSheetArrangements;
    }

    public void setHasIncludedOffBalanceSheetArrangements(
            Boolean hasIncludedOffBalanceSheetArrangements) {
        this.hasIncludedOffBalanceSheetArrangements = hasIncludedOffBalanceSheetArrangements;
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
