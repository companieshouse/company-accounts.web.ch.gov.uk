package uk.gov.companieshouse.web.accounts.transformer.smallfull;

import uk.gov.companieshouse.api.model.accounts.smallfull.creditorsafteroneyear.CreditorsAfterOneYearApi;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.creditorsafteroneyear.CreditorsAfterOneYear;

public interface CreditorsAfterOneYearTransformer {

    CreditorsAfterOneYear getCreditorsAfterOneYear(CreditorsAfterOneYearApi creditorsWithinOneYearApi);

    CreditorsAfterOneYearApi getCreditorsWithinOneYearApi(CreditorsAfterOneYear creditorsAfterOneYear);
}
