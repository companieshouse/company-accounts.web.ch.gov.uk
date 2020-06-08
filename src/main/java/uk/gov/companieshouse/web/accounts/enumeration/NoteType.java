package uk.gov.companieshouse.web.accounts.enumeration;

public enum NoteType {

    SMALL_FULL_OFF_BALANCE_SHEET_ARRANGEMENTS(false),
    SMALL_FULL_CURRENT_ASSETS_INVESTMENTS(false),
    SMALL_FULL_STOCKS(true),
    SMALL_FULL_CREDITORS_WITHIN_ONE_YEAR(true),
    SMALL_FULL_DEBTORS(true),
    TANGIBLE_ASSETS(true);

    private boolean includedDates;

    NoteType(boolean includedDates) {

        this.includedDates = includedDates;
    }

    public boolean hasIncludedDates() {
        return includedDates;
    }
}
