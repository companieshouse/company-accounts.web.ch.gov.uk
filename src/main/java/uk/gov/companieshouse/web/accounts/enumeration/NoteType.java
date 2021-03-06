package uk.gov.companieshouse.web.accounts.enumeration;

public enum NoteType {

    SMALL_FULL_OFF_BALANCE_SHEET_ARRANGEMENTS(false),
    SMALL_FULL_CURRENT_ASSETS_INVESTMENTS(false),
    SMALL_FULL_STOCKS(true),
    SMALL_FULL_CREDITORS_AFTER_ONE_YEAR(true),
    SMALL_FULL_CREDITORS_WITHIN_ONE_YEAR(true),
    SMALL_FULL_DEBTORS(true),
    SMALL_FULL_EMPLOYEES(true),
    SMALL_FULL_TANGIBLE_ASSETS(true),
    SMALL_FULL_ACCOUNTING_POLICIES(false),
    SMALL_FULL_INTANGIBLE_ASSETS(true),
    SMALL_FULL_FIXED_ASSETS_INVESTMENT(false),
    SMALL_FULL_FINANCIAL_COMMITMENTS(false),
    SMALL_FULL_LOAN_TO_DIRECTORS(false);

    private boolean includedDates;

    NoteType(boolean includedDates) {

        this.includedDates = includedDates;
    }

    public boolean hasIncludedDates() {
        return includedDates;
    }
}
