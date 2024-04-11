package uk.gov.companieshouse.web.accounts.model.smallfull;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

public class AccountsReferenceDate {
    @NotNull(message = "{accountsReferenceDate.selectionNotMade}")
    private LocalDate chosenDate;

    private List<LocalDate> pastDates;

    private List<LocalDate> futureDates;

    public LocalDate getChosenDate() {
        return chosenDate;
    }

    public void setChosenDate(LocalDate chosenDate) {
        this.chosenDate = chosenDate;
    }

    public List<LocalDate> getPastDates() {
        return pastDates;
    }

    public void setPastDates(List<LocalDate> pastDates) {
        this.pastDates = pastDates;
    }

    public List<LocalDate> getFutureDates() {
        return futureDates;
    }

    public void setFutureDates(List<LocalDate> futureDates) {
        this.futureDates = futureDates;
    }
}
