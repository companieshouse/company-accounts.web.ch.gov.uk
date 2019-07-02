package uk.gov.companieshouse.web.accounts.model.smallfull.notes.employees;

import javax.validation.constraints.NotNull;
import uk.gov.companieshouse.web.accounts.validation.ValidationModel;

@ValidationModel
public class EmployeesQuestion {

    @NotNull(message = "{employeesQuestion.selectionNotMade}")
    private Boolean hasSelectedEmployeesNote;

    public Boolean getHasSelectedEmployeesNote() {
        return hasSelectedEmployeesNote;
    }

    public void setHasSelectedEmployeesNote(Boolean hasSelectedEmployeesNote) {
        this.hasSelectedEmployeesNote = hasSelectedEmployeesNote;
    }
}
