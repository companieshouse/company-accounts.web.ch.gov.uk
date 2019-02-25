package uk.gov.companieshouse.web.accounts.model.smallfull.notes.employees;

import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import uk.gov.companieshouse.web.accounts.validation.ValidationModel;

@Getter
@Setter
@ValidationModel
public class EmployeesQuestion {

    @NotNull(message = "{employeesQuestion.selectionNotMade}")
    private Boolean hasSelectedEmployeesNote;
}